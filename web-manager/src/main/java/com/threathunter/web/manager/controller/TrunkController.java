package com.threathunter.web.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.threathunter.web.http.route.RequestMethod;
import com.threathunter.web.http.server.HttpRequest;
import com.threathunter.web.http.server.HttpResponse;
import com.threathunter.web.http.server.HttpService;
import com.threathunter.web.manager.entity.EmptyResonse;
import com.threathunter.web.manager.entity.Response;
import com.threathunter.web.manager.mysql.domain.Trunk;
import com.threathunter.web.manager.service.MysqlService;
import com.threathunter.web.manager.service.PageAnalysisService;
import com.threathunter.web.manager.service.RedisService;
import com.threathunter.web.manager.utils.Constant;
import com.threathunter.web.manager.utils.ControllerException;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wanbaowang on 17/9/18.
 */
@HttpService("/asset-manager/trunk")
public class TrunkController {

    private static final Logger logger = LoggerFactory.getLogger(TrunkController.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private MysqlService mysqlService;

    @Autowired
    private PageAnalysisService pageAnalysisService;

    @HttpService(value = "/report",
            parameters = "name:urls,required:true,struct:array,type:string|" +
                    "name:version,required:true,type:integer",
            method = {RequestMethod.POST},
            summmary = "Bones上报接口",
            description = "Bones上报，机器提交行为"
    )
    public void report(HttpRequest request, HttpResponse response) throws IOException {
        if (StringUtils.isBlank(request.getContent())) {
            throw new ControllerException("body is empty");
        }
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> parameterMap = mapper.reader(Map.class).readValue(request.getContent());
        if (!parameterMap.containsKey("urls")) {
            throw new ControllerException("miss parameter [urls]");
        }

        if (!parameterMap.containsKey("version")) {
            throw new ControllerException("miss parameter [version]");
        }

        Object objVersion = parameterMap.get("version");
        if (!(objVersion instanceof Number)) {
            throw new ControllerException("parameter [version] is not number");
        }

        long version = Long.valueOf(objVersion.toString());

        List<String> urls = (List<String>) (List) parameterMap.get("urls");
        Set<String> urlSet = new HashSet<>();
        Set<String> trunkSet = new HashSet<>();
        for (String url : urls) {
            if (url.contains(Constant.TRUNK_CHARACTER)) {
                trunkSet.add(url);
            } else {
                urlSet.add(url);
            }
        }
        this.redisService.report(urlSet, trunkSet, version);
        logger.info("report action, parameter: " + request.getContent());
        response.setResponseContentTypeJson();
        response.setResult(mapper.writeValueAsString(EmptyResonse.getEmptyResponse()));
    }

    @HttpService(value = "/clear", method = {RequestMethod.GET}, summmary = "清空已提交的Bones", description = "清除Bones数据，Version升级，人工清除行为")
    public void clear(HttpRequest request, HttpResponse response) throws IOException {
        this.redisService.deleteTrunks();
        this.redisService.incrementVersion();
        EmptyResonse clearResp = EmptyResonse.getEmptyResponse();
        ObjectMapper mapper = new ObjectMapper();
        response.setResponseContentTypeJson();
        String respValue = mapper.writeValueAsString(clearResp);
        logger.info("clear action, result is:" + respValue);
        response.setResult(respValue);
    }

    @HttpService(value = "/save", method = {RequestMethod.POST},
            parameters = "name:urls,required:true,struct:array,type:string|" +
                    "name:status,required:true,type:integer",
            summmary = "Bones黑白名单提交",
            description = "Bones黑白名单提交，人工提交行为"
    )
    public void save(HttpRequest request, HttpResponse response) throws IOException {
        if (StringUtils.isBlank(request.getContent())) {
            throw new ControllerException("body is empty");
        }
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> parameterMap = mapper.reader(Map.class).readValue(request.getContent());
        if (!parameterMap.containsKey("urls")) {
            throw new ControllerException("miss parameter [messages.urls]");
        }

        if (!parameterMap.containsKey("status")) {
            throw new ControllerException("miss parameter [messages.status]");
        }

        Object _status = parameterMap.get("status");
        if (!(_status instanceof Integer)) {
            throw new ControllerException("[messages.status] is not Integer type");
        }

        List<String> urls = (List<String>) (List) parameterMap.get("urls");

        Integer status = Integer.valueOf(String.valueOf(_status));
        for (String url : urls) {
            Trunk trunk = new Trunk();
            trunk.setStatus(status);
            trunk.setTrunk(url);
            mysqlService.trunkSave(trunk);
        }
        logger.info("save action, parameter: " + request.getContent());
        response.setResponseContentTypeJson();
        response.setResult(mapper.writeValueAsString(EmptyResonse.getEmptyResponse()));
    }

    @HttpService(value = "/list", method = {RequestMethod.GET},
            summmary = "获取黑白名单及机器提交的Bones列表", description = "获取黑白名单及机器提交的Bones列表及Version")
    public void list(HttpRequest request, HttpResponse response) throws IOException {
        Set<String> trunks = this.redisService.getTrunks();
        long version = this.redisService.getCurrentVersion();
        Response listResp = Response.getResponse(version);
        listResp.setTrunks(trunks);
        List<Trunk> mysqlTrunks = this.mysqlService.list();
        for (Trunk mysqlTrunk : mysqlTrunks) {
            if (mysqlTrunk.getStatus() == 1) {
                listResp.addWhiteTrunk(mysqlTrunk.getTrunk());
            } else {
                listResp.addBlackTrunk(mysqlTrunk.getTrunk());
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        response.setResponseContentTypeJson();
        String respValue = mapper.writeValueAsString(listResp);
        logger.info("list action, result is:" + respValue);
        response.setResult(respValue);
    }

    @HttpService(value = "/delete", method = {RequestMethod.POST},
            parameters = "name:urls,required:true,struct:array,type:string",
            summmary = "删除黑白名单列表",
            description = "删除黑白名单列表，人工提交行为"
    )
    public void delete(HttpRequest request, HttpResponse response) throws IOException {
        if (StringUtils.isBlank(request.getContent())) {
            throw new ControllerException("body is empty");
        }
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> parameterMap = mapper.reader(Map.class).readValue(request.getContent());
        if (!parameterMap.containsKey("urls")) {
            throw new ControllerException("miss parameter [messages.urls]");
        }

        List<String> urls = (List<String>) (List) parameterMap.get("urls");
        mysqlService.batchDelete(urls);
        logger.info("save action, parameter: " + request.getContent());
        response.setResponseContentTypeJson();
        response.setResult(mapper.writeValueAsString(EmptyResonse.getEmptyResponse()));
    }

}
