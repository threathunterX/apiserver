package com.threathunter.web.manager.controller;

import com.threathunter.web.manager.service.*;
import org.springframework.beans.factory.annotation.*;
import com.threathunter.web.http.server.*;
import com.threathunter.web.manager.utils.*;
import com.google.gson.Gson;
import java.util.*;
import com.threathunter.web.http.route.*;
import org.slf4j.*;

@HttpService({ "/platform/network" })
public class NetworkController
{
    private static final Logger log;
    @Autowired
    NetworkStatisticsService service;
    
    @HttpService(value = { "/statistics" }, method = { RequestMethod.GET })
    public void getStatistics(final HttpRequest request, final HttpResponse response) {
        NetworkController.log.info(String.format("uri:/platform/network/statistics, method:get. Request parameter: = %s ", request.getParameterMap()));
        try {
            final List<Map<String, Object>> statistics = this.service.getStatistics();
            ResponseUtils.setSuccessResult(response, statistics);
        }
        catch (Exception e) {
            NetworkController.log.info(String.format("porter getAllFiles fail.", new Object[0]), (Throwable)e);
            ResponseUtils.setErrorResult(response, e);
        }
        NetworkController.log.info("uri:/platform/network/statistics, response result:{}", (Object)response.getResult());
    }
    
    static {
        log = LoggerFactory.getLogger((Class)NetworkController.class);
    }
}
