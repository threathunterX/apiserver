package com.threathunter.web.manager.controller;

import org.springframework.beans.factory.annotation.*;
import com.threathunter.web.manager.service.*;
import com.threathunter.web.http.server.*;
import com.threathunter.web.manager.utils.*;
import java.util.*;
import com.threathunter.web.manager.mysql.domain.*;
import com.threathunter.web.http.route.*;
import com.google.gson.reflect.*;
import com.google.gson.*;
import java.lang.reflect.*;
import org.slf4j.*;

@HttpService({ "/platform/notices" })
public class SlotNoticeController
{
    private static final Logger log;
    @Autowired
    SlotNoticeService noticeService;
    @Autowired
    StrategyCustService strategyService;
    
    @HttpService(method = { RequestMethod.GET })
    public void notices(final HttpRequest request, final HttpResponse response) {
        final Map<String, String> parameterMap = (Map<String, String>)request.getParameterMap();
        SlotNoticeController.log.info("uri:/platform/notices, method:GET. Request parameter: = {} ", (Object)parameterMap);
        final Integer limit = Integer.valueOf(parameterMap.getOrDefault("limit", "25"));
        final String key = parameterMap.get("key");
        final String fromtime = parameterMap.get("fromtime");
        final String endtime = parameterMap.get("endtime");
        final Integer page = Integer.valueOf(parameterMap.getOrDefault("offset", "1"));
        final boolean filterExpire = Boolean.valueOf(parameterMap.getOrDefault("filter_expire", "false"));
        final String test = parameterMap.get("test");
        final Map<String, List<String>> rawQueryString = (Map<String, List<String>>)request.getRawQueryString();
        List<String> strategies = rawQueryString.get("strategy");
        final List<String> decisions = rawQueryString.get("decision");
        final List<String> checkTypes = rawQueryString.get("checkType");
        final List<String> sceneTypes = rawQueryString.get("sceneType");
        final List<String> tags = rawQueryString.get("tag");
        if (tags != null && tags.size() > 0) {
            final List<String> strategyNames = this.strategyService.selectNameByTags(tags);
            if (strategyNames.size() == 0) {
                final Map map = GsonUtil.fromJson("{'total_page': 0, 'count': 0, 'items': []}", Map.class);
                ResponseUtils.setSuccessResult(response, map);
                SlotNoticeController.log.info("GetAll:uri:/platform/notices, method:get, response result:{}", (Object)response.getResult());
                return;
            }
            if (strategies == null) {
                strategies = new ArrayList<String>();
            }
            strategies.addAll(strategyNames);
        }
        final SlotNoticeService.Criteria criteria = new SlotNoticeService.Criteria();
        criteria.setKey(key);
        if (fromtime != null) {
            criteria.setFromTime(Long.valueOf(fromtime));
        }
        if (endtime != null) {
            criteria.setEndTime(Long.valueOf(endtime));
        }
        criteria.setStrategies(strategies);
        criteria.setSceneTypes(sceneTypes);
        criteria.setCheckTypes(checkTypes);
        criteria.setDecisions(decisions);
        criteria.setFilterExpire(filterExpire);
        criteria.setPage(page);
        criteria.setLimit(limit);
        if (test == null) {
            criteria.setTest(null);
        }
        else {
            criteria.setTest((long)("true".equalsIgnoreCase(test) ? 1 : 0));
        }
        final Map<String, Object> paginate = this.noticeService.aggregateNotice(criteria);
        final List<SlotNotice> notices = (List<SlotNotice>)paginate.get("notices");
        this.noticeService.appendWhiteInfo(notices);
        ResponseUtils.setSuccessResult(response, paginate);
        SlotNoticeController.log.info("GetAll:uri:/platform/notices, method:get, response result:{}", (Object)response.getResult());
    }
    
    @HttpService(method = { RequestMethod.POST })
    public void addNotice(final HttpRequest request, final HttpResponse response) {
        final String content = request.getContent();
        SlotNoticeController.log.info(String.format("addNotice:uri:/platform/notices, method:post. Request parameter: = %s ", content));
        final Gson gson = new GsonBuilder().create();
        final Type listSlotNotice = new TypeToken<List<SlotNotice>>() {}.getType();
        final List<SlotNotice> notices = (List<SlotNotice>)gson.fromJson(content, listSlotNotice);
        this.noticeService.add(notices);
        ResponseUtils.setSuccessResult(response);
        SlotNoticeController.log.info("addNotice:uri:/platform/notices, method:get, response result:{}", (Object)response.getResult());
    }
    
    @HttpService(method = { RequestMethod.DELETE })
    public void deleteNotice(final HttpRequest request, final HttpResponse response) {
        final Map<String, String> parameterMap = (Map<String, String>)request.getParameterMap();
        SlotNoticeController.log.info("uri:/platform/notices, method:DELETE. Request parameter: = {} ", (Object)parameterMap);
        final String key = parameterMap.get("key");
        final String fromtime = parameterMap.get("fromtime");
        final String endtime = parameterMap.get("endtime");
        if (key == null || fromtime == null || endtime == null) {
            SlotNoticeController.log.info("deleteNotice:uri:/platform/notices, method:DELETE, response result:{}", (Object)response.getResult());
            ResponseUtils.setSuccessResult(response, new ArrayList());
            return;
        }
        final Long longFromtime = Long.valueOf(fromtime);
        final Long longEndtime = Long.valueOf(endtime);
        this.noticeService.delete(key, longFromtime, longEndtime);
        ResponseUtils.setSuccessResult(response);
        SlotNoticeController.log.info("deleteNotice:uri:/platform/notices, method:DELETE, response result:{}", (Object)response.getResult());
    }
    
    static {
        log = LoggerFactory.getLogger((Class)SlotNoticeController.class);
    }
}
