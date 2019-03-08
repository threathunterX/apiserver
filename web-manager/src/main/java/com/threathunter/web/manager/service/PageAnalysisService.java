package com.threathunter.web.manager.service;

import com.threathunter.labrador.common.util.ThreadLocalDateUtil;
import com.threathunter.web.common.config.ConfigUtil;
import com.threathunter.web.manager.page.analysis.PageAnalysisContainer;
import com.threathunter.web.manager.page.analysis.PageNode;
import com.threathunter.web.manager.utils.ControllerException;
import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.util.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.xson.core.XSON;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.StampedLock;

@Slf4j
@Service
public class PageAnalysisService implements InitializingBean {

    private volatile static String lastDay;

    private volatile static long lastSyncTs;

    //1分钟同步一次
    public static int SYNC_PERIOD_SECOND = 1000 * 60;

    private static final StampedLock lock = new StampedLock();

    //构建url
    public void buildUrls(Map<String, Integer> urlCounts) {
        log.warn("build urls " + urlCounts);
        long stamp = lock.writeLock();
        try {
            maybeSync();
        } catch (Exception e) {
            log.error("sync page analysis failed  ", e);
        }
        lock.unlockWrite(stamp);
        PageNode root = PageAnalysisContainer.getRootNode();
        for (Map.Entry<String, Integer> entry : urlCounts.entrySet()) {
            String url = entry.getKey();
            int count = entry.getValue();
            List<String> parts = CollectionUtil.newArrayList(url.split("/"));
            long loopStamp = lock.writeLock();
            root.build(PageAnalysisContainer.getRootNode(), root.getText(), parts, count);
            lock.unlockWrite(loopStamp);
        }
    }

    //同步方法，由buildUrls方法进行驱动，距上次构建超过SYNC_PERIOD_SECOND后进行同步。
    private void maybeSync() {
        String currentDay = ThreadLocalDateUtil.formatDay(new Date());
        if (StringUtils.isBlank(lastDay)) {
            lastDay = currentDay;
        }
        //此次日期和上次不同，则清除数据
        if (!currentDay.equals(lastDay)) {
            PageAnalysisContainer.clear();
        }
        long currentTs = System.currentTimeMillis();
        String binFileName = String.format("page_%s_%s.bin", currentDay, String.valueOf(currentTs));
        if(lastSyncTs > 0) {
            log.warn("last sync ts {}, curent ts {}, interval ts {} ", lastSyncTs, currentTs, currentTs - lastSyncTs);
            if (currentTs - lastSyncTs > SYNC_PERIOD_SECOND) {
                sync(binFileName);
                lastSyncTs = currentTs;
            }
        } else {
            lastSyncTs = currentTs;
        }
    }

    private void sync(String binFileName) {
        File pageDataDir = new File(ConfigUtil.getString("page.analysis.data.dir"));
        if (!pageDataDir.exists()) {
            boolean createSucc = pageDataDir.mkdirs();
            if (!createSucc) {
                throw new ControllerException("create dir " + pageDataDir.getAbsolutePath() + " failed");
            }
        }
        byte[] data = XSON.encode(PageAnalysisContainer.getRootNode());
        FileUtil.writeBytes(data, new File(pageDataDir, binFileName));
        log.warn("save page data to file {}", binFileName);
        for (File currentBinFile : pageDataDir.listFiles()) {
            if (!(currentBinFile.getName().endsWith(binFileName))) {
                currentBinFile.delete();
            }
        }
    }

    public void editTag(String uri, String manTag) {
        PageNode rootNode = getRootNode();
        List<String> parts = CollectionUtil.newArrayList(uri.split("/"));
        PageNode locate = rootNode.locate(rootNode, parts);
        if(null == locate) {
            throw new IllegalArgumentException("uri " + uri + " is not exists");
        }
        locate.setManTag(manTag);

        String currentDay = ThreadLocalDateUtil.formatDay(new Date());
        long currentTs = System.currentTimeMillis();
        String binFileName = String.format("page_%s_%s.bin", currentDay, String.valueOf(currentTs));
        sync(binFileName);
    }

    //列出给出uri的子结点，为空则给出根的子结点
    public List<PageNode> parentUri(String uri) {
        Set<PageNode> results = new HashSet<>();
        PageNode rootNode = getRootNode();
        if (StringUtils.isBlank(uri)) {
            results.addAll(rootNode.getSubNodes().values());
        } else {
            List<String> parts = CollectionUtil.newArrayList(uri.split("/"));
            PageNode locate = rootNode.locate(rootNode, parts);
            if (null != locate) {
                results.addAll(locate.getSubNodes().values());
            }
        }
        List<PageNode> sortNodes = CollectionUtil.sort(results, (PageNode node1, PageNode node2) ->
                node2.getPathVisit() - node1.getPathVisit());
        return sortNodes;
    }

    //根据关键字模糊查找
    public List<PageNode> search(String keyword) {
        Set<PageNode> keywordResults = new HashSet<>();
        Set<PageNode> searchResults = new HashSet<>();
        PageNode rootNode = getRootNode();
        rootNode.search(keyword, keywordResults);
        for (PageNode keywordResult : keywordResults) {
            keywordResult.foundChildrenUrl(searchResults);
        }
        List<PageNode> sortNodes = CollectionUtil.sort(searchResults, (PageNode node1, PageNode node2) ->
                node2.getUrlVisit() - node1.getUrlVisit());
        return sortNodes;
    }

    private PageNode getRootNode() {
        long stamp = lock.tryOptimisticRead();//获得一个乐观锁
        PageNode rootNode = PageAnalysisContainer.getRootNode();
        if(!lock.validate(stamp)) {
            stamp = lock.readLock();//获得一个悲观锁
            rootNode = PageAnalysisContainer.getRootNode();
            lock.unlockRead(stamp);
        }
        return rootNode;
    }

    public void clear() {
        long stamp = lock.writeLock();
        PageAnalysisContainer.clear();
        lock.unlockRead(stamp);
    }

    public static void main(String[] args) {
        PageNode rootNode = PageAnalysisContainer.getRootNode();
        Set<PageNode> results = new HashSet<>();
        for (Map.Entry<String, PageNode> nodeEntry : rootNode.getSubNodes().entrySet()) {
            nodeEntry.getValue().foundChildrenUrl(results);
        }
        System.out.println(results);
    }


    //系统启动时，从文件中加载数据
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            restore();
        }catch (Exception e) {
            log.error("init PageAnalysisService Error, ", e);
        }
    }

    private void restore() {
        File pageDataDir = new File(ConfigUtil.getString("page.analysis.data.dir"));
        String maxFileName = "";
        if(!pageDataDir.exists()) {
            pageDataDir.mkdirs();
        }
        for(File currentDataFile : pageDataDir.listFiles()) {
            try {
                String currentDataFilePath = currentDataFile.getCanonicalPath();
                if(StringUtils.isBlank(maxFileName)) {
                    maxFileName = currentDataFilePath;
                } else {
                    if(currentDataFilePath.compareTo(maxFileName) > 0) {
                        maxFileName = currentDataFilePath;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String currentDay = ThreadLocalDateUtil.formatDay(new Date());
        if(StringUtils.isNotBlank(maxFileName) ) {
            if(maxFileName.contains(currentDay)) {
                log.warn("restore from data path {}", maxFileName);
                File dataFile = new File(maxFileName);
                byte[] data = FileUtil.readBytes(dataFile);
                PageNode node = XSON.decode(data);
                PageAnalysisContainer.setRootNode(node);
            }
        }

    }
}
