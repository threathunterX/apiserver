package com.threathunter.web.manager.controller;

import com.threathunter.web.manager.service.*;
import org.springframework.beans.factory.annotation.*;
import com.threathunter.web.http.server.*;
import org.apache.commons.lang3.*;
import com.threathunter.web.manager.utils.*;
import org.codehaus.jackson.map.*;
import com.alibaba.fastjson.*;
import java.io.*;
import com.threathunter.web.http.route.*;
import com.xiaoleilu.hutool.lang.Base64;
import com.xiaoleilu.hutool.util.*;
import com.threathunter.web.manager.page.analysis.*;
import java.util.*;
import org.slf4j.*;

@HttpService({ "/page_analysis" })
public class PageAnalysisController
{
    private static final Logger log;
    @Autowired
    private PageAnalysisService pageAnalysisService;
    
    @HttpService(value = { "/report_url" }, method = { RequestMethod.POST })
    public void reportUrl(final HttpRequest request, final HttpResponse response) throws IOException {
        if (StringUtils.isBlank((CharSequence)request.getContent())) {
            throw new ControllerException("body is empty");
        }
        final ObjectMapper mapper = new ObjectMapper();
        final Map<String, Object> parameterMap = (Map<String, Object>)mapper.reader((Class)Map.class).readValue(request.getContent());
        if (!parameterMap.containsKey("visit_times")) {
            throw new ControllerException("miss parameter [visit_times]");
        }
        final Object visitTimesObject = parameterMap.get("visit_times");
        if (!(visitTimesObject instanceof Map)) {
            throw new ControllerException("visit_times is not instanceof Map");
        }
        final Map<String, Integer> visitTimes = (Map<String, Integer>)visitTimesObject;
        this.pageAnalysisService.buildUrls(visitTimes);
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", (Object)true);
        jsonObject.put("status", (Object)200);
        response.setResponseContentTypeJson();
        response.setResult(jsonObject.toJSONString());
    }
    
    @HttpService(value = { "/tag/edit" }, method = { RequestMethod.POST })
    public void edit(final HttpRequest request, final HttpResponse response) throws Exception {
        if (StringUtils.isBlank((CharSequence)request.getContent())) {
            throw new ControllerException("body is empty");
        }
        final ObjectMapper mapper = new ObjectMapper();
        final Map<String, Object> parameterMap = (Map<String, Object>)mapper.reader((Class)Map.class).readValue(request.getContent());
        if (!parameterMap.containsKey("uri")) {
            throw new ControllerException("miss parameter [uri]");
        }
        final String uri = String.valueOf(parameterMap.get("uri"));
        if (!parameterMap.containsKey("man_tag")) {
            throw new ControllerException("miss parameter [man_tag]");
        }
        final String manTag = String.valueOf(parameterMap.get("man_tag"));
        try {
            this.pageAnalysisService.editTag(uri, manTag);
        }
        catch (Exception e) {
            throw e;
        }
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", (Object)true);
        jsonObject.put("status", (Object)200);
        response.setResponseContentTypeJson();
        response.setResult(jsonObject.toJSONString());
    }
    
    @HttpService(value = { "clear" }, method = { RequestMethod.GET })
    public void clear(final HttpRequest request, final HttpResponse response) {
        this.pageAnalysisService.clear();
        response.setResult("ok");
    }
    
    @HttpService(value = { "/parent_uri" }, parameters = "", method = { RequestMethod.GET }, summmary = "", description = "")
    public void parentUri(final HttpRequest request, final HttpResponse response) throws IOException {
        String uri = "";
        if (request.getParameterMap().containsKey("uri")) {
            uri = Base64.decodeStr((String)request.getParameterMap().get("uri"));
        }
        final List<PageNode> nodeList = this.pageAnalysisService.parentUri(uri);
        PageAnalysisController.log.warn("query [page_analysis.parent_uri]: uri {}, result {}", (Object)uri, (Object)nodeList);
        Map<String, Object> results = CollectionUtil.createMap(HashMap.class);
        results.put("status", 200);
        List<Map<String, Object>> itemValues = new ArrayList<>();
        for (PageNode node : nodeList) {
            Map<String, Object> item = new HashMap<>();
            item.put("uri", node.getFullPath());
            if (node.getSubNodes().size() > 0) {
                item.put("cnt", node.getPathVisit());
            } else {
                item.put("cnt", node.getUrlVisit());
            }
            item.put("is_leaf", node.getSubNodes().size() == 0 ? true : false);
            item.put("is_url", node.isUrl());
            item.put("man_tag", StringUtils.isBlank((CharSequence)node.getManTag()) ? "" : node.getManTag());
            itemValues.add(item);
        }
        results.put("result", itemValues);
        JSONObject jsonObject = new JSONObject(results);
        response.setResponseContentTypeJson();
        response.setResult(jsonObject.toJSONString());
    }
    
    @HttpService(value = { "/search" }, parameters = "", method = { RequestMethod.GET }, summmary = "", description = "")
    public void search(final HttpRequest request, final HttpResponse response) throws IOException {
        String keyword = "";
        if (!request.getParameterMap().containsKey("keyword")) {
            throw new ControllerException("miss parameter [keyword]");
        }
        keyword = Base64.decodeStr((String)request.getParameterMap().get("keyword"));
        if (!request.getParameterMap().containsKey("start")) {
            throw new ControllerException("miss parameter [start]");
        }
        final Object cntObject = request.getParameter("start");
        System.out.println(cntObject.getClass());
        PageAnalysisController.log.warn("query [page_analysis.search]: keyword:{}", (Object)keyword);
        int requestStart = 0;
        try {
            requestStart = Integer.valueOf(cntObject.toString());
        }
        catch (Exception e) {
            throw new ControllerException("parameter [start] is not number");
        }
        int start = (requestStart < 0) ? 0 : requestStart;
        int end = start + 10;
        final List<PageNode> nodeList = this.pageAnalysisService.search(keyword);
        if (start > nodeList.size()) {
            start = nodeList.size();
        }
        if (end > nodeList.size()) {
            end = nodeList.size();
        }
        if (start > end) {
            start = end;
        }
        final List<PageNode> pageNodes = nodeList.subList(start, end);
        Map<String, Object> results = CollectionUtil.createMap(HashMap.class);
        results.put("status", 200);
        results.put("total", nodeList.size());
        List<Map<String, Object>> itemValues = new ArrayList<>();
        for (PageNode pageNode : pageNodes) {
            Map<String, Object> item = new HashMap<>();
            item.put("uri", pageNode.getFullPath());
            item.put("cnt", pageNode.getUrlVisit());
            item.put("man_tag", StringUtils.isBlank(pageNode.getManTag()) ? "" : pageNode.getManTag());
            item.put("is_leaf", pageNode.getSubNodes().size() == 0 ? true : false);
            itemValues.add(item);
        }
        results.put("result", itemValues);
        PageAnalysisController.log.warn("query [page_analysis.search]: results:{}", (Object)results);
        final JSONObject jsonObject = new JSONObject((Map)results);
        response.setResponseContentTypeJson();
        response.setResult(jsonObject.toJSONString());
    }
    
    public static void main(final String[] args) {
    }
    
    static {
        log = LoggerFactory.getLogger((Class)PageAnalysisController.class);
    }
}
