package com.threathunter.web.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.threathunter.web.manager.utils.HttpUtils;
import com.xiaoleilu.hutool.lang.Base64;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestPageAnalysis extends TestCase {

    private String host = "127.0.0.1";

    @Test
    public void testReportUrl() throws IOException {
        String url = "http://" + host + ":8080/page_analysis/report_url";
        Map<String, Integer> pageCounts = new HashMap<>();
        pageCounts.put("threathunter.com/product/b1", 4);
        pageCounts.put("threathunter.com/product/b2", 5);
        pageCounts.put("threathunter.com/product/b1/b1_detail", 3);
        pageCounts.put("threathunter.com/product", 10);
        pageCounts.put("threathunter.com1/produ/c2", 7);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("visit_times", pageCounts);
        String json = jsonObject.toJSONString();
        String response = HttpUtils.postJson(url, json);
        System.out.println(response);
    }

    @Test
    public void testEditTag() throws Exception {
        String uri = "threathunter.com/product";
        String manTag = "产品页";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uri", uri);
        jsonObject.put("man_tag", manTag);
        String json = jsonObject.toJSONString();
        String url = "http://" + host + ":8080/page_analysis/tag/edit";
        String response = HttpUtils.postJson(url, json);
        System.out.println(response);

    }

    @Test
    public void  testParentUri() throws IOException {
        String uri = "threathunter.com";
        String base64Expression = Base64.encode(uri, "utf-8");
        String url = "http://" + host + ":8080/page_analysis/parent_uri?uri=" + base64Expression;
        System.out.println(url);
        String response = HttpUtils.get(url);
        System.out.println(response);
    }

    @Test
    public void clear() throws IOException {
        String url = "http://" + host + ":8080/page_analysis/clear";
        String response = HttpUtils.get(url);
        System.out.println(response);
    }


    @Test
    public void testSearch() throws IOException {
        String keyword = "threathunter.com";
        keyword = Base64.encode(keyword, "utf-8");
        String url = "http://" + host + ":8080/page_analysis/search?start=0&keyword=" + keyword;
        System.out.println(url);
        String response = HttpUtils.get(url);
        System.out.println(response);
    }
}
