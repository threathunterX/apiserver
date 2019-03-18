package com.threathunter.web.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.threathunter.web.manager.utils.HttpUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 */
public class TestStrategyTrigger {
    private static String host = "172.16.10.116";

    public static void main(String[] args) throws IOException {
        String url = "http://" + host + ":8080/strategy/trigger";
        Map<String,Object> properties = new HashMap<>();
        properties.put("c_ip", "1.2.3.4");
        properties.put("uid", "456");
        properties.put("did", "did123");
        properties.put("strategylist", Arrays.asList("user_login_succ_$_did_exception_uid"));
        Map<String,Object> kv = new HashMap<>();
        kv.put("kv", properties);
        JSONObject jsonObject = new JSONObject(kv);
        String response = HttpUtils.postJson(url, jsonObject.toJSONString());
        System.out.println(response);
    }
}
