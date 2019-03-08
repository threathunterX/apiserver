package com.threathunter.web.manager.controller;

import com.threathunter.web.http.route.RequestMethod;
import com.threathunter.web.http.server.HttpRequest;
import com.threathunter.web.http.server.HttpResponse;
import com.threathunter.web.http.server.HttpService;
import com.threathunter.web.manager.service.ClickStreamQueryService;
import com.threathunter.web.manager.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 *
 * API-Clickstream查询
 * wiki:http://wiki.threathunter.net/pages/viewpage.action?pageId=24248695
 */

@HttpService({ "/platform/behavior" })
public class ClickSteamQueryController
{
    private static final Logger log;
    @Autowired
    ClickStreamQueryService service;
    
    @HttpService(value = { "/clicks_period" }, method = { RequestMethod.GET })
    public void queryPeriod(final HttpRequest request, final HttpResponse response) {
        final Map<String, String> parameterMap = (Map<String, String>)request.getParameterMap();
        ClickSteamQueryController.log.info("uri://platform/behavior/clicks_period, method:post. Request parameter: = {} ", (Object)parameterMap);
        final String key = parameterMap.get("key");
        final String dimension = parameterMap.get("key_type");
        final Long fromTime = Long.valueOf(parameterMap.get("from_time"));
        final Long endTime = Long.valueOf(parameterMap.get("end_time"));
        final Map<String, Object> ret = this.service.queryPeriod(key, dimension, fromTime, endTime);
        ResponseUtils.setSuccessResult(response, ret);
        ClickSteamQueryController.log.info("uri://platform/behavior/clicks_period, method:post. Response result: = {} ", (Object)response);
    }
    
    @HttpService(value = { "/visit_stream" }, method = { RequestMethod.GET })
    public void queryVisit(final HttpRequest request, final HttpResponse response) {
        final Map<String, String> parameterMap = (Map<String, String>)request.getParameterMap();
        ClickSteamQueryController.log.info("uri://platform/behavior/visit_stream, method:post. Request parameter: = {} ", (Object)parameterMap);
        final String key = parameterMap.get("key");
        final String dimension = parameterMap.get("key_type");
        final Long fromTime = Long.valueOf(parameterMap.get("from_time"));
        final Long endTime = Long.valueOf(parameterMap.get("end_time"));
        final List<Map<String, Object>> ret = this.service.queryVisit(key, dimension, fromTime, endTime);
        ResponseUtils.setSuccessResult(response, ret);
        ClickSteamQueryController.log.info("uri://platform/behavior/visit_stream, method:post. Response result: = {} ", (Object)response);
    }
    
    @HttpService(value = { "/clicks" }, method = { RequestMethod.POST })
    public void queryClicks(final HttpRequest request, final HttpResponse response) {
        final String content = request.getContent();
        ClickSteamQueryController.log.info("uri://platform/behavior/clicks, method:post. Request parameter: = {} ", (Object)content);
        final Gson gson = new GsonBuilder().create();
        final Map<String, Object> gs = (Map<String, Object>)gson.fromJson(content, (Class)Map.class);
        final String key = (String)gs.get("key");
        final String dimension = (String)gs.get("key_type");
        final Long fromTime = Long.valueOf((String)gs.get("from_time"));
        //默认取十分钟数据
        Long endTime = new Long(fromTime + 10 * 60 * 1000);
        if (gs.get("end_time") instanceof  Long){
            endTime = (Long) gs.get("end_time");
        }else if (gs.get("end_time") instanceof  String) {
            endTime = Long.valueOf((String)gs.get("end_time"));
        }

        final List query = (List)gs.getOrDefault("query", new ArrayList());
        final List<Map<String, Object>> ret = this.service.queryClicks(key, dimension, fromTime, endTime, query);
        ResponseUtils.setSuccessResult(response, ret);
        ClickSteamQueryController.log.info("uri://platform/behavior/clicks, method:post. Response result: = {} ", (Object)response);
    }
    
    static {
        log = LoggerFactory.getLogger((Class)ClickSteamQueryController.class);
    }
}
