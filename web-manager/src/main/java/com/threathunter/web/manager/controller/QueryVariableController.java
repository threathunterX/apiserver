package com.threathunter.web.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.threathunter.labrador.core.exception.LabradorException;
import com.threathunter.web.http.route.RequestMethod;
import com.threathunter.web.http.server.HttpRequest;
import com.threathunter.web.http.server.HttpResponse;
import com.threathunter.web.http.server.HttpService;
import com.threathunter.web.manager.rpc.VariableClient;
import com.threathunter.web.manager.utils.ControllerException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 
 */
@HttpService("/query")
public class QueryVariableController {
    @Autowired
    private VariableClient variableClient;

    @HttpService(value = "/variable",
            parameters = "name:key,required:true,type:string|" +
                    "name:variables,required:true,struct:array,type:string",
            method = {RequestMethod.POST},
            summmary = "查询变量接口",
            description = "根据提供的变量名称查询变量"
    )
    public void queryVariable(HttpRequest request, HttpResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> parameterMap = mapper.reader(Map.class).readValue(request.getContent());

        if (!parameterMap.containsKey("key")) {
            throw new ControllerException("miss parameter [key]");
        }

        if (!parameterMap.containsKey("variables")) {
            throw new ControllerException("miss parameter [variable]");
        }
        String key = (String) parameterMap.get("key");

        List<String> variables = (List<String>) (List) parameterMap.get("variables");
        Map<String, Object> resultMap = null;
        try {
            resultMap = variableClient.queryVariable(key, variables);
        } catch (LabradorException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(resultMap);
        response.setResponseContentTypeJson();
        response.setResult(jsonObject.toJSONString());
    }
}
