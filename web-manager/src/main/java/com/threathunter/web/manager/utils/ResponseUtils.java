package com.threathunter.web.manager.utils;

import com.alibaba.fastjson.JSONObject;
import com.threathunter.web.http.server.HttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
public class ResponseUtils {
    public static void setSuccessResult(HttpResponse response) {
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("status", 200);
        JSONObject jsonObject = new JSONObject(retMap);
        response.setResponseContentTypeJson();
        response.setResult(jsonObject.toJSONString());
    }

    public static void setErrorResult(HttpResponse response, Exception e) {
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("status", 500);
        retMap.put("message", e.getMessage());
        JSONObject jsonObject = new JSONObject(retMap);
        response.setResponseContentTypeJson();
        response.setResult(jsonObject.toJSONString());
    }

    public static void setSuccessResult(HttpResponse response, Object result) {
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("status", 200);
        retMap.put("result", result);
        JSONObject jsonObject = new JSONObject(retMap);
        response.setResponseContentTypeJson();
        response.setResult(jsonObject.toJSONString());
    }

}
