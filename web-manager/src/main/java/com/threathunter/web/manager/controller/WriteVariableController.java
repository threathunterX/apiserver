package com.threathunter.web.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.threathunter.web.http.route.RequestMethod;
import com.threathunter.web.http.server.HttpRequest;
import com.threathunter.web.http.server.HttpResponse;
import com.threathunter.web.http.server.HttpService;
import com.threathunter.web.manager.rpc.VariableWriteClient;
import com.threathunter.web.manager.utils.ControllerException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 */
@HttpService("/write")
public class WriteVariableController {

    @Autowired
    private VariableWriteClient variableWriteClient;

    @HttpService(value = "/variable", parameters = "",
            method = {RequestMethod.POST},
            summmary = "",
            description = ""
    )
    public void writeVariable(HttpRequest request, HttpResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> parameterMap = mapper.reader(Map.class).readValue(request.getContent());
        String name = "";
        if (parameterMap.containsKey("name")) {
            name = (String) parameterMap.get("name");
        }

        if(!parameterMap.containsKey("variable")) {
            throw new ControllerException("miss parameter [variable]");
        }

        String variable = (String) parameterMap.get("variable");

        if(!parameterMap.containsKey("kv")) {
            throw new ControllerException("miss parameter [kv]");
        }

        Object kvObject = parameterMap.get("kv");

        if(!(kvObject instanceof Map)) {
            throw new ControllerException("kv is not instaceof map");
        }
        Map<String,Object> result = new HashMap<>();
        Map<String,Object> kv = (Map<String, Object>) parameterMap.get("kv");
        try {
            this.variableWriteClient.writeVariable(name, variable, kv);
            result.put("status", 200);
            result.put("success", true);
        } catch (Exception e) {
            result.put("status", 500);
            result.put("success", false);
            result.put("message", e.getMessage());
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(result);
        response.setResponseContentTypeJson();
        response.setResult(jsonObject.toJSONString());
    }
}
