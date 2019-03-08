package com.threathunter.web.manager.page.analysis;

import com.threathunter.web.manager.service.PageAnalysisService;
import com.xiaoleilu.hutool.util.CollectionUtil;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestPageAnalysis extends TestCase {

    static {
        report();
    }

    private static void report() {
        PageAnalysisService analysisService = new PageAnalysisService();
        Map<String, Integer> pageCounts = new HashMap<>();
        pageCounts.put("threathunter.com/product/b1", 4);
        pageCounts.put("threathunter.com/product/b2", 5);
        pageCounts.put("threathunter.com/product/b1/b1_detail", 3);
        pageCounts.put("threathunter.com/product", 10);
        pageCounts.put("threathunter.com1/produ/c2", 7);
        analysisService.buildUrls(pageCounts);
        analysisService.editTag("threathunter.com/product", "产品页");
    }

    @Test
    public void clear() {
        PageAnalysisService analysisService = new PageAnalysisService();
        analysisService.clear();

    }

    @Test
    public void testParentUri() {
        PageAnalysisService analysisService = new PageAnalysisService();
        String keyword = "threathunter.com/product/b1";
        List<PageNode> firstLevelNodes = analysisService.parentUri(keyword);
        for (PageNode pageNode : firstLevelNodes) {
            System.out.println(pageNode);
        }
    }

    @Test
    public void testSearch() {
        clear();
        PageAnalysisService analysisService = new PageAnalysisService();
        String keyword = "threathunter.com1";
        List<PageNode> nodes = analysisService.search(keyword);
        List<PageNode> sortNodes = CollectionUtil.sort(nodes, (PageNode node1, PageNode node2) ->
                node2.getUrlVisit() - node1.getUrlVisit());
        System.out.println(sortNodes.size());
        for (PageNode sortNode : sortNodes) {
            System.out.println(sortNode);
        }
    }

    @Test
    public void  testSync() {
        PageAnalysisService.SYNC_PERIOD_SECOND = 10;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        report();
    }
}
