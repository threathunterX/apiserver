package com.threathunter.web.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.threathunter.web.manager.utils.HttpUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanbaowang on 17/11/21.
 */
public class TestWriteVariable {
//    private String host = "172.16.10.116";
          private String host = "127.0.0.1";
    @Test
    public void testQueryVariable() throws IOException {
        String url = "http://" + host + ":8080/write/variable";

        Map<String,Object> kv = new HashMap<>();
        kv.put("uid", "abc");
        kv.put("result", "T");
        kv.put("c_ip", "2.4.6.8");
        String name = "ACCOUNT_REGISTRATION";
        String variable = "";
//        String variable = "uid__account_register_timestamp__profile";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("kv", kv);
        jsonObject.put("variable", variable);
        for(int i=0;i<10;i++) {
            String response = HttpUtils.postJson(url, jsonObject.toJSONString());
            System.out.println(response);
        }
    }
}
