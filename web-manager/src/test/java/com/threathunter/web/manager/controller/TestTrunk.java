package com.threathunter.web.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.threathunter.web.manager.utils.HttpUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanbaowang on 17/9/21.
 */
public class TestTrunk {

    private String host = "127.0.0.1";

    @Test
    public void testReport() throws IOException {
        JSONObject jsonObject = new JSONObject();
        String url = "http://" + host + ":8080/asset-manager/trunk/report";
        jsonObject.put("urls", Arrays.asList("httpmessages/url14", "http://bg.com/url****item3"));
        jsonObject.put("version", 0);
        String json = jsonObject.toJSONString();
        String response = HttpUtils.postJson(url, json);
        System.out.println(response);
    }

    @Test
    public void testSave() throws IOException {
        String url = "http://" + host + ":8080/asset-manager/trunk/save";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 1);
        jsonObject.put("urls", Arrays.asList("httpmessages/url1", "http://bg.com/url****item9"));
        String response = HttpUtils.postJson(url, jsonObject.toJSONString());
        System.out.println(response);
    }

    @Test
    public void testList() throws IOException {
        String url = "http://" + host + ":8080/asset-manager/trunk/list";
        String response = HttpUtils.get(url);
        System.out.println(response);
    }

    @Test
    public void testClear() throws IOException {
        String url = "http://" + host + ":8080/asset-manager/trunk/clear";
        String response = HttpUtils.get(url);
        System.out.println(response);
    }

    @Test
    public void testDelete() throws IOException {
        String url = "http://" + host + ":8080/asset-manager/trunk/delete";
        List<String> urls = Arrays.asList("httpmessages/url1", "http://bg.com/url****item9");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("urls", urls);
        String json = jsonObject.toJSONString();
        String response = HttpUtils.postJson(url, json);
        System.out.println(response);
    }

}
