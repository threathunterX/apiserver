package com.threathunter.web.manager.controller;

import com.xiaoleilu.hutool.lang.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
@Slf4j
public class PorterControllerTest extends HttpClientBase {

    @Test
    public void testUpload() {
        String uri = "http://localhost:8080/platform/porters/file";
        //把一个普通参数和文件上传给下面这个地址    是一个servlet
        httpPost = new HttpPost(uri);
        //要上传的文件的路径
        String filePath = "/tmp/ip.csv";
        //把文件转换成流对象FileBody
        FileBody bin = new FileBody(new File(filePath));
        //普通字段  重新设置了编码方式
        StringBody schema = new StringBody("csv", ContentType.create("text/plain", Consts.UTF_8));

        StringBody name = new StringBody(Base64.encode("1ip1.csv"), ContentType.create("text/plain", Consts.UTF_8));
        StringBody remark = new StringBody(Base64.encode("12345678901234567890"), ContentType.create("text/plain", Consts.UTF_8));
//        StringBody schema= new StringBody("csv", ContentType.create("text/plain", Consts.UTF_8));
        StringBody key = new StringBody("key111111", ContentType.create("text/plain", Consts.UTF_8));
        HttpEntity entity = MultipartEntityBuilder.create()
                .addPart("payload", bin).addPart("name", name)
                .addPart("remark", remark).addPart("key", key)
                .addPart("schema", schema)
                .build();
        httpPost.setEntity(entity);
        assertResponse(httpPost);
    }

    public void assertResponse(HttpUriRequest request) {
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(request);
            assertThat(httpResponse).isNotNull();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetAllFiles() {
        String uri = "http://localhost:8080//platform/porters/files";
        //把一个普通参数和文件上传给下面这个地址    是一个servlet
        httpGet = new HttpGet(uri);
        assertResponse(httpGet);
    }

    public void testGetFileById() {
    }

    @Test
    public void testDeleteFileById() {
        String uri = "http://172.16.10.65:8080/platform/porters/file?id=1";
        httpDelete = new HttpDelete(uri);
        assertResponse(httpDelete);
    }

}
