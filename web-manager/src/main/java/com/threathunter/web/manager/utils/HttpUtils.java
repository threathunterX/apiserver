package com.threathunter.web.manager.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by wanbaowang on 17/9/21.
 */
public class HttpUtils {

    public static String postJson(String postUrl, String json) throws IOException {
        int timeout = 1000;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();
        CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

        HttpPost httpPost = new HttpPost(postUrl);
        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
        StringEntity stringEntity = new StringEntity(json, "UTF-8");
        stringEntity.setContentEncoding("UTF-8");
        httpPost.setEntity(stringEntity);
        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
            @Override
            public String handleResponse(final HttpResponse response)
                    throws ClientProtocolException, IOException {//
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {

                    HttpEntity entity = response.getEntity();

                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException(
                            "Unexpected response status: " + status);
                }
            }
        };
        String responseBody = httpclient.execute(httpPost, responseHandler);
        return responseBody;
    }

    public static String get(String url) throws IOException {
        int timeout = 5000;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();
        CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse httpResponse = httpclient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        String response = "";
        if (httpEntity != null) {
            // 打印响应内容
            try {
                response = EntityUtils.toString(httpEntity, "UTF-8");
                httpclient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    public static void main(String[] args) throws IOException {
        String url = "https://www.google.com/search?ei=fDoqWvvzFYSojwPOraWADQ&q=httpclient+java+set+timeout&oq=httpclient+java++set+timeout&gs_l=psy-ab.3.0.0j0i22i30k1l4.155853.159820.0.162766.12.10.0.0.0.0.392.1430.3-4.4.0....0...1c.1.64.psy-ab..8.4.1428...0i67k1j0i22i10i30k1.0.ehenSvhByTk";
        System.out.println(get(url));
    }
}
