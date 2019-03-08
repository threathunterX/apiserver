package com.threathunter.web.manager.controller;

import com.threathunter.labrador.core.exception.LabradorException;
import com.threathunter.web.http.route.RequestMethod;
import com.threathunter.web.http.server.HttpRequest;
import com.threathunter.web.http.server.HttpResponse;
import com.threathunter.web.http.server.HttpService;
import com.threathunter.web.manager.handler.DefaultValueHandler;
import com.threathunter.web.manager.handler.HourlyMapHandler;
import com.threathunter.web.manager.handler.SortMapHandler;
import com.threathunter.web.manager.handler.ValueHandler;
import com.threathunter.web.manager.rpc.GlobalVariableClient;
import com.threathunter.web.manager.utils.ResponseUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
@Slf4j
@HttpService("/zues/profile/metrics")
public class ZuesController implements InitializingBean, ApplicationContextAware {
    Gson gson = new GsonBuilder().create();
    GlobalVariableClient client;
    ValueHandler handler;
    private ApplicationContext applicationContext;

    @HttpService(value = "/query", method = {RequestMethod.POST})
    public void query(HttpRequest request, HttpResponse response) throws LabradorException {
        Map<String, String> parameterMap = request.getParameterMap();
        log.info("uri://platform/behavior/clicks_period, method:post. Request parameter: = {} ", parameterMap);
        String json = (String) parameterMap.get("var_list");
        String day = (String) parameterMap.get("day");//yyyyMMdd
        List<String> vars = (List<String>) gson.fromJson(json, List.class);
        Map<String, Object> query = client.query(day, vars);
        Map<String, Object> resultMap = (Map<String, Object>) query.get("result");
        Map<String, Object> ret = new HashMap<>();
        for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            handler.handle(key, value, ret);

        }
        ResponseUtils.setSuccessResult(response, ret);
        log.info("uri://platform/behavior/clicks_period, method:post. Response result: = {} ", response);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        client = applicationContext.getBean(GlobalVariableClient.class);
        handler = new HourlyMapHandler();
        handler.add(new SortMapHandler());
        handler.add(new DefaultValueHandler());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
