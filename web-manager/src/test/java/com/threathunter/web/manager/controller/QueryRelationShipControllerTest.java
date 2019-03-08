package com.threathunter.web.manager.controller;

import com.threathunter.web.manager.utils.HttpUtils;
import com.xiaoleilu.hutool.lang.Base64;
import org.junit.Test;

import java.io.IOException;

public class QueryRelationShipControllerTest {

    private String host = "127.0.0.1";

    @Test
    public void testReportUrl() throws IOException {
//        String uid = "uid70001";
        String uid = "456";
        String base64 = Base64.encode(uid, "UTF-8");
        String url = "http://" + host + ":8080//platform/graph_relationship?key=%s&dimension=%s";
        url = String.format(url, base64, "uid");
        System.out.println(url);
        System.out.println(url);
        String response = HttpUtils.get(url);
        System.out.println(response);
    }

}