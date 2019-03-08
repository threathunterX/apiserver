package com.threathunter.web.manager.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
@Slf4j
public class HttpClientBase {
    CloseableHttpClient httpClient = null;
    HttpPost httpPost = null;
    HttpGet httpGet = null;
    HttpDelete httpDelete = null;

    @Before
    public void setUp() {
        httpClient = HttpClients.createDefault();
    }
}
