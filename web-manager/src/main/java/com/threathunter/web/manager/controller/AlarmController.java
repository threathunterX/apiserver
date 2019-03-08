package com.threathunter.web.manager.controller;

import com.threathunter.web.http.route.RequestMethod;
import com.threathunter.web.http.server.HttpRequest;
import com.threathunter.web.http.server.HttpResponse;
import com.threathunter.web.http.server.HttpService;
import com.threathunter.web.manager.service.AlarmService;
import com.threathunter.web.manager.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 * <p>
 * API-风险名单每小时统计
 * wiki:http://wiki.threathunter.net/pages/viewpage.action?pageId=24249821
 */
@Slf4j
@HttpService("/platform/alarm/statistics")
public class AlarmController {
    private static final Logger logger = LoggerFactory.getLogger(AlarmController.class);
    @Autowired
    AlarmService alarmService;

    @HttpService(method = RequestMethod.GET)
    public void getStatistics(HttpRequest request, HttpResponse response) {
        Map<String, String> parameterMap = request.getParameterMap();
        logger.info("uri:/platform/alarm/statistics, method:GET. Request parameter: = {} ", parameterMap);
        String fromTime = parameterMap.get("fromtime");
        String endTime = parameterMap.get("endtime");
        if (fromTime == null || endTime == null){
		    final List list = new ArrayList();
            ResponseUtils.setSuccessResult(response, list);
            logger.warn("fromtime={},endtime={}", fromTime, endTime);
            return;
		}
        Long from = Long.valueOf(fromTime);
        Long end = Long.valueOf(endTime);
        List<Map<String, Object>> statistic = alarmService.getStatistic(from, end);
        ResponseUtils.setSuccessResult(response, statistic);
        logger.info("uri:/platform/alarm/statistics, method:post. Response result: = {} ", response);
    }
}
