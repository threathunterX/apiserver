package com.threathunter.web.manager.controller;

import com.threathunter.web.http.route.RequestMethod;
import com.threathunter.web.http.server.HttpRequest;
import com.threathunter.web.http.server.HttpResponse;
import com.threathunter.web.http.server.HttpService;
import com.threathunter.web.manager.mysql.domain.Porter;
import com.threathunter.web.manager.service.PortersService;
import com.threathunter.web.manager.utils.ResponseUtils;
import com.xiaoleilu.hutool.lang.Base64;
import io.netty.handler.codec.http.multipart.FileUpload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
@Slf4j
@HttpService("/platform/porters")
public class PortersController {

    @Autowired
    PortersService portersService;

    @HttpService(value = "/files", method = {RequestMethod.GET})
    public void getAllFiles(HttpRequest request, HttpResponse response) {
        log.info(String.format("uri:/platform/porters/files, method:get. Request parameter: = %s ", request.getParameterMap()));
        try {
            List<Porter> porters = portersService.getAll();
            ResponseUtils.setSuccessResult(response, porters);
        } catch (Exception e) {
            log.info(String.format("porter getAllFiles fail."), e);
            ResponseUtils.setErrorResult(response, e);
        }
        log.info("GetAll:uri:/platform/porters/files method:get, response result:{}", response.getResult());
    }

    @HttpService(value = "/file", method = {RequestMethod.DELETE})
    public void deleteFileById(HttpRequest request, HttpResponse response) {
        log.info(String.format("uri:/platform/porters/file, method: delete. Request parameters= %s", request.getParameterMap()));
        String id = request.getParameter("id");
        if (id == null) {
            log.error("delete file id is Null");
            throw new RuntimeException("id");
        }
        try {
            int intId = Integer.valueOf(id);
            portersService.deletePorter(intId);
            ResponseUtils.setSuccessResult(response);
        } catch (Exception e) {
            log.error("delete");
            ResponseUtils.setErrorResult(response, e);
        }
        log.info("deleteFileById:uri:/platform/porters/files method:DELETE, response result:{}", response.getResult());
    }

    //http://wiki.threathunter.net/pages/viewpage.action?pageId=20096361
    @HttpService(value = "/file", method = {RequestMethod.POST})
    public void insertFile(HttpRequest request, HttpResponse response) {
        log.info(String.format("uri:/platform/porters/file, method:post. Request parameters= %s", request.getParameterMap()));
        String schema = request.getParameter("schema");
        String name = new String(Base64.decode(request.getParameter(("name"))), Charset.forName("utf-8"));
        String remark = new String(Base64.decode(request.getParameter(("remark"))), Charset.forName("utf-8"));
        String key = request.getParameter("key");
        if (key == null || name == null || schema == null) {
            log.error("key = {}, name = {}, schema = {} have NULL value, return;", key, name, schema);
            throw new RuntimeException(String.format("key = %s, name = %s, schema = %s have NULL value", key, name, schema));
        }

        Porter porter = new Porter();
        porter.setSchema(schema);
        porter.setRemark(remark);
        porter.setName(name);
        porter.setKey(key);
        try {
            //保存文件和存储到数据库
            File placeFileToHold = portersService.createFile(name);
            File dest = getUploadFile(request, placeFileToHold);
            //csv 需要count 其他的不需要。（前端要求）
            if ("csv".equals(schema)) {
                long count = getLineCount(dest);
                porter.setCount(count);
            } else {
                porter.setCount(-1);
            }

            portersService.save(porter);
            //返回数据
            log.info(String.format("porter insert success. porter : %s", porter.toString()));
            ResponseUtils.setSuccessResult(response);
        } catch (Exception e) {
            //返回数据
            log.info(String.format("porter insert fail."), e);
            ResponseUtils.setErrorResult(response, e);
        }
        log.info("insertFile:uri:/platform/porters/file method:post, response result:{}", response.getResult());
    }

    private long getLineCount(File upload) {
        long count = 0;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(upload.getAbsolutePath()), "utf-8"));
            count = reader.lines().count();
//            count = Files.lines(upload.toPath()).count();
        } catch (IOException e) {
            log.error("file");
        }
        return count;
    }


    private File getUploadFile(HttpRequest request, File file) {
        for (FileUpload fileUpload : request.getFileMap().values()) {
            if (fileUpload.isCompleted()) {
                try {
                    fileUpload.renameTo(file);
                    break;
                } catch (IOException e) {
                    log.error("porters file upload fail, {}", e);
                    throw new RuntimeException("File upload failed", e);
                }
            }
        }
        return file;
    }

}
