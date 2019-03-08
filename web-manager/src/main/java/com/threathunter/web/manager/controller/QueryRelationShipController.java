package com.threathunter.web.manager.controller;


import com.alibaba.fastjson.JSONObject;
import com.threathunter.labrador.common.util.EnumUtil;
import com.threathunter.labrador.core.exception.LabradorException;
import com.threathunter.web.http.route.RequestMethod;
import com.threathunter.web.http.server.HttpRequest;
import com.threathunter.web.http.server.HttpResponse;
import com.threathunter.web.http.server.HttpService;
import com.threathunter.web.manager.rpc.QueryRelationShipClient;
import com.threathunter.web.manager.utils.ControllerException;
import com.xiaoleilu.hutool.lang.Base64;
import com.xiaoleilu.hutool.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@HttpService("/platform")
public class QueryRelationShipController {

    @Autowired
    private QueryRelationShipClient queryRelationShipClient;

    @HttpService(value = "/graph_relationship",
            parameters = "",
            method = {RequestMethod.GET},
            summmary = "",
            description = "")
    public void file(HttpRequest request, HttpResponse response) throws IOException {
        if(!request.getParameterMap().containsKey("key")) {
            throw new ControllerException("miss parameter [key]");
        }

        String key = request.getParameter("key");
        key = Base64.decodeStr(key, CharsetUtil.UTF_8);


        if(!request.getParameterMap().containsKey("dimension")) {
            throw new ControllerException("miss parameter [dimension]");
        }

        String dimension = request.getParameter("dimension");

        dimension = dimension.toLowerCase();

        try {
            EnumUtil.Dimension _dimension = EnumUtil.Dimension.valueOf(dimension);
        } catch (Exception e) {
            log.error("dimension is not valid, must be [uid/ip/did]");
            throw new ControllerException("dimension is not valid, must be [uid/ip/did]");
        }

        Map<String,Object> result = new HashMap<>();
        try {
            result = queryRelationShipClient.loadRelations(key, dimension);
            result.put("success", true);
            result.put("status", 200);
        } catch (LabradorException e) {
            log.error("query failed, ", e);
            result.put("success", false);
            result.put("status", 500);
            result.put("message", e.getMessage());
        }
        JSONObject jsonObject = new JSONObject(result);
        response.setResponseContentTypeJson();
        response.setResult(jsonObject.toJSONString());
    }

}
