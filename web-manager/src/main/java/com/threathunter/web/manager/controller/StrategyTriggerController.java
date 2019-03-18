package com.threathunter.web.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.threathunter.web.http.route.RequestMethod;
import com.threathunter.web.http.server.HttpRequest;
import com.threathunter.web.http.server.HttpResponse;
import com.threathunter.web.http.server.HttpService;
import com.threathunter.web.manager.rpc.StrategyTriggerClient;
import com.threathunter.web.manager.utils.ControllerException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 */
@HttpService("/strategy")
public class StrategyTriggerController {

    @Autowired
    private StrategyTriggerClient strategyTriggerClient;

    @HttpService(value = "/trigger",
            parameters = "",
            method = {RequestMethod.POST},
            summmary = "",
            description = ""
    )
    public void strategyTrigger(HttpRequest request, HttpResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> parameterMap = mapper.reader(Map.class).readValue(request.getContent());
        if (!parameterMap.containsKey("kv")) {
            throw new ControllerException("miss parameter [kv]");
        }

        Object kvObject = parameterMap.get("kv");
        if(!(kvObject instanceof Map)) {
            throw new ControllerException("kv is not instanceof map");
        }

        Map<String,Object> kv = (Map<String, Object>) parameterMap.get("kv");
        if(!(kv.containsKey("strategylist"))) {
            throw new ControllerException("kv miss strategylist parameter");
        }

        Object strategyListObject = kv.get("strategylist");
        if(!(strategyListObject instanceof List)) {
            throw new ControllerException("strategylist is not instanceof list");
        }

        Map<String,Object> result = new HashMap<>();
        try {
            String triggerResult = strategyTriggerClient.strategyTrigger(kv);
            result.put("status", 200);
            result.put("success", true);
            result.put("result", triggerResult);
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
