package com.threathunter.web.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.threathunter.labrador.spl.check.CheckResponse;
import com.threathunter.web.http.route.RequestMethod;
import com.threathunter.web.http.server.HttpRequest;
import com.threathunter.web.http.server.HttpResponse;
import com.threathunter.web.http.server.HttpService;
import com.threathunter.web.manager.rpc.SplClient;
import com.threathunter.web.manager.utils.ControllerException;
import com.xiaoleilu.hutool.lang.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanbaowang on 17/10/30.
 */
@HttpService("/spl")
public class SplController {

    @Autowired
    private SplClient splClient;

    @HttpService(value = "/verify",
            parameters = "name:message,required:true,type:string",
            method = {RequestMethod.GET},
            summmary = "Spl验证接口",
            description = "Spl验证接口，检测Spl的正确性"
    )
    public void verify(HttpRequest request, HttpResponse response) throws IOException {
        if (!request.getParameterMap().containsKey("expression")) {
            throw new ControllerException("miss parameter [expression]");
        }
        String expression = Base64.decodeStr(request.getParameter("expression"), "utf-8");
        CheckResponse checkResponse = splClient.verify(expression);
        Map<String,Object> respMap = new HashMap<>();
        respMap.put("status", 200);
        respMap.put("success", checkResponse.isSuccess());
        if(checkResponse.isSuccess()) {
            respMap.put("message", "successful");
        } else {
            respMap.put("message", checkResponse.getMessage());
        }
        JSONObject jsonObject = new JSONObject(respMap);
        response.setResponseContentTypeJson();
        response.setResult(jsonObject.toJSONString());
    }

}
