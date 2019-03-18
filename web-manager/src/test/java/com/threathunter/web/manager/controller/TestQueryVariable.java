package com.threathunter.web.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.threathunter.web.manager.utils.HttpUtils;
import com.xiaoleilu.hutool.lang.Base64;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

/**
 * 
 */
public class TestQueryVariable {
    private String host = "172.16.10.241";
//      private String host = "127.0.0.1";
    @Test
    public void testQueryVariable() throws IOException {
        String url = "http://" + host + ":8080/query/variable";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", "123");
        jsonObject.put("variables", Arrays.asList("uid__registration__account__mail__profile", "uid__registration__account__mobile__profile",
                "uid__registration__account__username__profile", "uid__account_register_timestamp__profile",
                "uid__account_token_change_mobile__profile",
                "uid__account_token_change_mail__profile", "uid__account_token_change_mail_timestamp__profile",
                "uid__account_token_change_mobile_timestamp__profile", "uid__account_token_change_mail_count__profile",
                "uid__account_token_change_mobile_count__profile", "uid__account_login_timestamp_last10__profile",
                "uid__account_login_ip_last10__profile", "uid__account_login_geocity_last10__profile", "uid__registration__account__ip__profile"));
        String response = HttpUtils.postJson(url, jsonObject.toJSONString());
        System.out.println(response);
    }

}
