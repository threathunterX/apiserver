package com.threathunter.web.manager.controller;

import com.threathunter.web.manager.utils.HttpUtils;
import com.xiaoleilu.hutool.lang.Base64;
import org.junit.Test;

import java.io.IOException;

/**
 * 
 */
public class TestSpl {
    private String host = "172.16.10.241";

    @Test
    public void testVerify() throws IOException {
        String expression = "$CHECKNOTICE(\"ip\", c_ip, \"IP大量请求不加载静态资源\", 3600) > 0";
        String base64Expression = Base64.encode(expression, "utf-8");
        String url = "http://" + host + ":8080/spl/verify?expression=" + base64Expression;
        System.out.println(url);
        String response = HttpUtils.get(url);
        System.out.println(response);
    }
}
