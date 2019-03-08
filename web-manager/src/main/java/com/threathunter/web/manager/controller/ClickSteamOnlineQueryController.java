package com.threathunter.web.manager.controller;

import com.threathunter.web.manager.service.*;
import org.springframework.beans.factory.annotation.*;
import com.threathunter.web.http.server.*;
import com.threathunter.web.manager.utils.*;
import com.threathunter.web.http.route.*;
import java.util.*;
import com.google.gson.*;
import org.slf4j.*;

@HttpService({ "/platform/online" })
public class ClickSteamOnlineQueryController
{
    private static final Logger log;
    @Autowired
    ClickStreamQueryService service;
    
    @HttpService(value = { "/clicks_period" }, method = { RequestMethod.GET })
    public void queryPeriod(final HttpRequest request, final HttpResponse response) {
        ClickSteamOnlineQueryController.log.info("===========enter...clicks_perid=================");
        final Map<String, String> parameterMap = (Map<String, String>)request.getParameterMap();
        ClickSteamOnlineQueryController.log.info("uri://platform/online/clicks_period, method:post. Request parameter: = {} ", (Object)parameterMap);
        final String key = parameterMap.get("key");
        final String dimension = parameterMap.get("key_type");
        final Long fromTime = Long.valueOf(parameterMap.get("from_time"));
        final Long endTime = Long.valueOf(parameterMap.get("end_time"));
        final Map<String, Object> ret = this.service.queryPeriod(key, dimension, fromTime, endTime);
        ResponseUtils.setSuccessResult(response, ret);
        ClickSteamOnlineQueryController.log.info("uri://platform/online/clicks_period, method:post. Response result: = {} ", (Object)response);
    }
    
    @HttpService(value = { "/visit_stream" }, method = { RequestMethod.GET })
    public void queryVisit(final HttpRequest request, final HttpResponse response) {
        final Map<String, String> parameterMap = (Map<String, String>)request.getParameterMap();
        ClickSteamOnlineQueryController.log.info("uri://platform/online/visit_stream, method:post. Request parameter: = {} ", (Object)parameterMap);
        final String key = parameterMap.get("key");
        final String dimension = parameterMap.get("key_type");
        final Long fromTime = Long.valueOf(parameterMap.get("from_time"));
        final Long endTime = Long.valueOf(parameterMap.get("end_time"));
        final List<Map<String, Object>> ret = this.service.queryVisit(key, dimension, fromTime, endTime);
        ResponseUtils.setSuccessResult(response, ret);
        ClickSteamOnlineQueryController.log.info("uri://platform/online/visit_stream, method:post. Response result: = {} ", (Object)response);
    }
    
    @HttpService(value = { "/clicks" }, method = { RequestMethod.POST })
    public void queryClicks(final HttpRequest request, final HttpResponse response) {
        final String content = request.getContent();
        ClickSteamOnlineQueryController.log.info("uri://platform/online/clicks, method:post. Request parameter: = {} ", (Object)content);
        final Gson gson = new GsonBuilder().create();
        final Map<String, Object> gs = (Map<String, Object>)gson.fromJson(content, (Class)Map.class);
        final String key = (String) gs.get("key");
        final String dimension = (String) gs.get("key_type");
        final Long fromTime = Long.valueOf((String) gs.get("from_time"));
        final Long endTime = Long.valueOf((String) gs.get("end_time"));
        final List query = (List) gs.getOrDefault("query", new ArrayList());
        final List<Map<String, Object>> ret = this.service.queryClicks(key, dimension, fromTime, endTime, query);
        ResponseUtils.setSuccessResult(response, ret);
        ClickSteamOnlineQueryController.log.info("uri://platform/online/clicks, method:post. Response result: = {} ", (Object)response);
    }
    
    static {
        log = LoggerFactory.getLogger((Class)ClickSteamOnlineQueryController.class);
    }
}
