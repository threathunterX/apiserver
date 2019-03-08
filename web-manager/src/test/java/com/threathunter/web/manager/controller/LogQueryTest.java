package com.threathunter.web.manager.controller;

import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
public class LogQueryTest extends HttpClientBase {

    @Ignore //nginx will do the download file, netty would do this job.
    @Test
    public void testDownload() {
        String uri = "http://localhost:8080/platform/persistent_query/download/events_query_1.csv";
        //把一个普通参数和文件上传给下面这个地址    是一个servlet
        httpGet = new HttpGet(uri);
        assertResponse(httpGet);
    }

    @Test
    public void testProgress() {
        String uri = "http://localhost:8080/platform/persistent_query/progress";
        httpGet = new HttpGet(uri);
        assertResponse(httpGet);
    }


    @Test
    public void testZeus() {
        String uri = "http://localhost:8080/zues/profile/metrics/test?var=test1&var=test2";
        httpGet = new HttpGet(uri);
        assertResponse(httpGet);
    }


    @Test
    public void testData() {
        String uri = "http://localhost:8080/platform/persistent_query/data?id=1;page=1;page_count=20";
        httpGet = new HttpGet(uri);
        assertResponse(httpGet);
    }

    public void assertResponse(HttpUriRequest request) {
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(request);
            assertThat(httpResponse).isNotNull();
            String result = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetAll() {
        String uri = "http://localhost:8080/platform/persistent_query";
        httpGet = new HttpGet(uri);
        assertResponse(httpGet);
    }

    //  String json="{\"show_cols\":[\"c_ip\",\"sid\",\"uid\",\"page\",\"platform\",\"timestamp\"],\"remark\":\"\",\"event_name\":\"HTTP_DYNAMIC\",\"terms\":[{\"left\":\"c_ip\",\"op\":\"contain\",\"right\":\".\"}],\"fromtime\":1515816181000,\"endtime\":1515988990000}";
    @Test
    public void create() throws UnsupportedEncodingException {
        String uri = "http://localhost:8080/platform/persistent_query";
        String json = "{\"show_cols\":[\"c_ip\",\"sid\",\"uid\",\"page\",\"platform\",\"timestamp\"],\"remark\":\"\",\"event_name\":\"HTTP_DYNAMIC\",\"terms\":[{\"left\":\"c_ip\",\"op\":\"contain\",\"right\":\".\"}],\"fromtime\":1515816181000,\"endtime\":1515988990000}";
        StringEntity entity = new StringEntity(json);
        httpPost = new HttpPost(uri);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        assertResponse(httpPost);
    }

    @Test
    public void delete() throws UnsupportedEncodingException {
        String uri = "http://localhost:8080/platform/persistent_query/13";
        HttpDelete request = new HttpDelete(uri);
        assertResponse(request);

    }
}
