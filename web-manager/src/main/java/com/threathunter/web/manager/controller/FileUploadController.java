package com.threathunter.web.manager.controller;

import com.threathunter.web.http.server.*;
import io.netty.handler.codec.http.multipart.*;
import java.io.*;
import com.threathunter.web.http.route.*;
import java.util.*;
import com.threathunter.web.http.constant.*;
import org.slf4j.*;

@HttpService({ "/file" })
public class FileUploadController
{
    private static final Logger log;
    
    @HttpService(value = { "/upload" }, parameters = "name:urls,required:true,struct:array,type:string|name:version,required:true,type:integer", method = { RequestMethod.POST })
    public void upload(final HttpRequest request, final HttpResponse response) throws IOException {
        System.out.println(request.getFileMap());
        for (final FileUpload fileUpload : request.getFileMap().values()) {
            if (fileUpload.isCompleted()) {
                fileUpload.renameTo(new File("/tmp/123"));
            }
        }
        System.out.println(request.getParameterMap());
        System.out.println(request.getAttributeMap());
        response.setResult("ok");
    }
    
    @HttpService({ "/hello/{p1}/doing/{something}" })
    public void hello(final HttpRequest request, final HttpResponse response) {
        System.out.println("===========");
        final Map<String, Object> attributeMap = (Map<String, Object>)request.getAttributeMap();
        final Map<String, String> uriVariables = (Map<String, String>)attributeMap.get(HttpConstant.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        System.out.println(uriVariables);
        System.out.println("***********");
        FileUploadController.log.info("----------come into TestCtrl[hello][{}][something][{}]", (Object)request.getPathValue("p1"), (Object)request.getPathValue("something"));
        FileUploadController.log.info("client ip is : {}", (Object)request.getIp());
        for (final Map.Entry<String, String> header : request.getHeaderMap().entrySet()) {
            FileUploadController.log.info("request header : {}={}", (Object)header.getKey(), (Object)header.getValue());
        }
        for (final Map.Entry<String, String> parameter : request.getParameterMap().entrySet()) {
            FileUploadController.log.info("request parameter : {}={}", (Object)parameter.getKey(), (Object)parameter.getValue());
        }
        for (final Map.Entry<String, FileUpload> file : request.getFileMap().entrySet()) {
            FileUploadController.log.info("request parameter : {}={}", (Object)file.getKey(), (Object)file.getValue().getFilename());
        }
        FileUploadController.log.info("request body is : {}", (Object)request.getBody());
        response.setResponseContentTypePlain();
        response.setResult("TestCtrl[hello] OK !");
    }
    
    static {
        log = LoggerFactory.getLogger((Class)FileUploadController.class);
    }
}
