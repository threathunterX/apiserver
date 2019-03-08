package com.threathunter.web.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.threathunter.web.http.route.RequestMethod;
import com.threathunter.web.http.server.HttpRequest;
import com.threathunter.web.http.server.HttpResponse;
import com.threathunter.web.http.server.HttpService;
import com.threathunter.web.manager.service.SlotQueryService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 * keys	list-string	否	需要查询的key，可能没有
 * 一般为具体的一组ip或者一组uid或者did，如果没有则不需要给
 * timestamp	long	是	查询的时间
 * 查询的数据为此timestamp所在小时
 * dimension	string	是	变量维度
 * 目前有：ip，uid，did，page以及global，other
 * variables	list-string	是	需要查询的变量名列表
 */
@Slf4j
@HttpService("/platform/stats/slot/query")
public class SlotQueryController {
    private static final Logger logger = LoggerFactory.getLogger(SlotQueryController.class);
    @Autowired
    SlotQueryService service;

    @HttpService(method = {RequestMethod.POST})
    public void query(HttpRequest request, HttpResponse response) {
        String content = request.getContent();
        logger.info(String.format("uri:/platform/stats/slot/query, method:post. Request parameter: = %s ", content));
        Gson gson = new GsonBuilder().create();
        Map<String, Object> gs = gson.fromJson(content, Map.class);
        Map<String, Object> ret = service.query(gs);
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("status", 200);
        retMap.put("values", ret);
        JSONObject jsonObject = new JSONObject(retMap);
        response.setResponseContentTypeJson();
        response.setResult(jsonObject.toJSONString());
        //"values"
        logger.info("uri:/platform/stats/slot/query, method:post, response result:{}", response.getResult());
    }
}
