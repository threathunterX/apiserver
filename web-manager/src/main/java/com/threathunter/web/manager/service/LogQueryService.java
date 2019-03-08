package com.threathunter.web.manager.service;

import com.threathunter.model.Event;
import com.threathunter.web.common.config.ConfigUtil;
import com.threathunter.web.manager.mysql.domain.LogQuery;
import com.threathunter.web.manager.mysql.domain.LogQueryStatus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.base64.Base64;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
@Service
@Slf4j
public class LogQueryService {
    private static String QUERY_DIR = ConfigUtil.getString("platform.persistent.query.dir", "/data/tmp/query");
    private static Gson GSON = new GsonBuilder().create();
    private static String RESULT_NAME_RREFIX = "events_query_";
    private static String TOTAL_NAME_PREFIX = "events_query_total_";
    @Autowired
    BabelService babelService;
    @Autowired
    MysqlService mysqlService;

    public static List<String> toShowCols(String json) {
        List<String> list = GSON.fromJson(json, List.class);
        return list;
    }

    public static List<Map<String, Object>> toTerms(String json) {
        List<Map<String, Object>> list = GSON.fromJson(json, List.class);
        return list;
    }

    public static LogQuery asLogQuery(Map<String, Object> parameterMap) {
        LogQuery query = new LogQuery();
        query.setShowColumns((List) parameterMap.get("show_cols"));
        query.setRemark((String) parameterMap.get("remark"));
        query.setEventName((String) parameterMap.get("event_name"));
        query.setTerms((List) parameterMap.get("terms"));
        query.setFromTime(((Double) parameterMap.get("fromtime")).longValue());
        query.setEndTime(((Double) parameterMap.get("endtime")).longValue());
        query.setCreateTime(System.currentTimeMillis());

        return query;
    }

    public static Long getQueryResultSize(int id) {
        String detailFilePath = String.format("%s/%s%d.csv", QUERY_DIR, RESULT_NAME_RREFIX, id);
        File detail = new File(detailFilePath);
        if (detail.exists()) {
            return detail.length();
        }
        return 0L;
    }

    public static Long getTotalSize(int id) {
        String totalFilePath = String.format("%s/%s%d.csv", QUERY_DIR, TOTAL_NAME_PREFIX, id);
        File total = new File(totalFilePath);
        if (total.exists()) {
            try {
                FileReader reader = new FileReader(total);
                BufferedReader br = new BufferedReader(reader);
                String ret = br.readLine();
                return Long.valueOf(ret);
            } catch (FileNotFoundException e) {
                log.error("totalSize:id:" + id, e);
                throw new RuntimeException("totalSize:id:" + id, e);
            } catch (IOException e) {
                log.error("IO:id:" + id, e);
                throw new RuntimeException("IO:id:" + id, e);
            }
        }
        return 0L;
    }

    public static String getDownloadPath(int id) {
        String detailFilePath = String.format("%s%d.csv", RESULT_NAME_RREFIX, id);
        return detailFilePath;
    }

    public List<Map<String, Object>> getPaginate(int id, int page, int pageCount) {
        Event response = babelService.fetchLogQuery(id, page, pageCount);
        Map<String, Object> propertyValues = response.getPropertyValues();
        boolean success = (boolean) propertyValues.get("success");
        if (success) {
            return (List<Map<String, Object>>) propertyValues.get("data");
        }
        return null;
    }

    public List<LogQuery> getAll() {
        return mysqlService.queryLogQuery();
    }

    private void addQueryInfo(LogQuery query) {
        String detailFilePath = String.format("%s/%s%d.csv", QUERY_DIR, RESULT_NAME_RREFIX, query.getId());
        String totalFilePath = String.format("%s/%s%d.csv", QUERY_DIR, TOTAL_NAME_PREFIX, query.getId());
        File detail = new File(detailFilePath);
        if (detail.exists()) {
            detail.length();
        }
    }

    @Transactional
    public boolean insert(LogQuery query) {
        mysqlService.insertLogQuery(query);
        Event response = babelService.createLogQuery(query);
        Map<String, Object> propertyValues = response.getPropertyValues();
        boolean success = (boolean) propertyValues.get("success");
        if (!success) {
            query.setError((String) propertyValues.get("errmsg"));
            query.setStatus(LogQueryStatus.FAILED);
        } else {
            query.setStatus(LogQueryStatus.PROCESS);
        }
        mysqlService.updateLogQuery(query);
        return success;
    }

    public LogQuery getById(int id) {
        return mysqlService.queryLogQuery(id);
    }

    @Transactional
    public void delete(int id) {
        babelService.deleteLogQuery(id);
        mysqlService.deleteLogQuery(id);
    }

    public List<Map<String, Object>> progress() {
        Event event = babelService.progressLogQuery();
        if (event != null) {
            Map<String, Object> propertyValues = event.getPropertyValues();
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) propertyValues.get("data");
            List<Integer> ids = mysqlService.queryLogQueryIdsByNonSuccessStatus();
            List<Integer> notifyIds = new ArrayList<>();
            for (Map<String, Object> map : dataList) {
                Integer id = (Integer) (map.get("id"));
                String status = (String) map.get("status");
                LogQuery logQuery = mysqlService.queryLogQuery(id);
                if (logQuery != null && !logQuery.getStatus().getValue().equals(status)) {
                    logQuery.setStatus(LogQueryStatus.toType(status));
                    mysqlService.updateLogQuery(logQuery);
                }
            }
        }
        List<Map<String, Object>> data = new ArrayList<>();
        List<Integer> successQueryIds = mysqlService.queryLogQueryIdsBySuccessStatus();
        List<Integer> nonSuccessQueryIds = mysqlService.queryLogQueryIdsByNonSuccessStatus();
        for (Integer id : successQueryIds) {
            String status = "success";
            Map<String, Object> stringObjectMap = progressDataEntry(id, status);
            data.add(stringObjectMap);
        }
        for (Integer id : nonSuccessQueryIds) {
            String status = "process";
            Map<String, Object> itemMap = progressDataEntry(id, status);
            data.add(itemMap);
        }
        return data;
    }

    private Map<String, Object> progressDataEntry(Integer id, String status) {
        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("id", Long.valueOf(id));
        itemMap.put("status", status);
        itemMap.put("progress", 0.00D);
        itemMap.put("error", null);
        return itemMap;
    }

    public File asDownloadFile(String name) {
        String detailFilePath = String.format("%s/%s.csv", QUERY_DIR, name);
        File file = new File(detailFilePath);
        return file;
    }

    public String downloadAtBase64(String name) {
        File file = asDownloadFile(name);
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            FileChannel channel = raf.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            List<ChannelBuffer> list = new ArrayList<>();
            while (channel.read(byteBuffer) != -1) {
                byteBuffer.flip();
                byte[] array = byteBuffer.duplicate().array();
                ChannelBuffer channelBuffer = ChannelBuffers.wrappedBuffer(array);
                list.add(channelBuffer);
                byteBuffer.clear();
            }
            ChannelBuffer channelBuffer = ChannelBuffers.wrappedBuffer(list.toArray(new ChannelBuffer[]{}));
            ChannelBuffer encode = Base64.encode(channelBuffer);
            return new String(encode.array());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
