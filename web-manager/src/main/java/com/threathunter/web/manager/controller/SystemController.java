package com.threathunter.web.manager.controller;

import com.threathunter.web.http.route.RequestMethod;
import com.threathunter.web.http.server.HttpRequest;
import com.threathunter.web.http.server.HttpResponse;
import com.threathunter.web.http.server.HttpService;
import com.threathunter.web.manager.service.SystemSnapshotService;
import com.threathunter.web.manager.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
@Slf4j
@HttpService("/system")
public class SystemController {
    /**
     * API-License与version获取
     *
     * @param request
     * @param response
     */
    @Autowired
    SystemSnapshotService service;

    private static final Logger log = LoggerFactory.getLogger(SystemController.class);

    @HttpService(value = "/license", method = {RequestMethod.GET})
    public void getLicense(HttpRequest request, HttpResponse response) {
        log.info(String.format("uri:/system/license, method:get, request parameter: = %s ", request.getParameterMap()));
        try {
            Map<String, Object> licenseInfo = service.getLicenseInfo();
            ResponseUtils.setSuccessResult(response, licenseInfo);
        } catch (Exception e) {
            log.info(String.format("SystemController getLicence fail."), e);
            ResponseUtils.setErrorResult(response, e);
        }
        log.info("uri:/system/license, method:get, response result:{}", response.getResult());
    }

    /**
     * API-License与version获取
     *
     * @param request
     * @param response
     */
    @HttpService(value = "/performance/digest", method = {RequestMethod.GET})
    public void getDigest(HttpRequest request, HttpResponse response) {
        log.info(String.format("uri:/system/performance/digest, method:get. Request parameter: = %s ", request.getParameterMap()));
        try {
            Map<String, Object> digestMap = service.getDigest();
            ResponseUtils.setSuccessResult(response, digestMap);
        } catch (Exception e) {
            log.info(String.format("SystemController getDigest fail."), e);
            ResponseUtils.setErrorResult(response, e);
        }
        log.info("uri:/system/performance/digest, method:get, response result:{}", response.getResult());
    }
}
