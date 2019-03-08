package com.threathunter.web.manager.controller;

import com.threathunter.web.manager.service.*;
import org.springframework.beans.factory.annotation.*;
import com.threathunter.web.http.server.HttpRequest;
import com.threathunter.web.http.server.HttpResponse;
import com.threathunter.web.http.server.HttpService;
import com.threathunter.web.manager.utils.*;
import com.google.gson.*;
import com.threathunter.web.manager.mysql.domain.*;
import com.threathunter.web.http.route.*;
import java.util.*;
import com.threathunter.web.http.constant.*;
import io.netty.handler.codec.http.*;
import java.io.*;
import org.slf4j.*;

@HttpService({ "/platform/persistent_query" })
public class LogQueryController
{
    private static final Logger log;
    @Autowired
    LogQueryService service;
    
    @HttpService(method = { RequestMethod.POST })
    public void createQuery(final HttpRequest request, final HttpResponse response) throws Exception {
        final String content = request.getContent();
        LogQueryController.log.info(String.format("uri:/platform/persistent_query, method:post. Request parameter: = %s ", content));
        final Gson gson = new GsonBuilder().create();
        final Map<String, Object> gs = (Map<String, Object>)gson.fromJson(content, (Class)Map.class);
        final LogQuery query = LogQueryService.asLogQuery(gs);
        final boolean success = this.service.insert(query);
        if (success) {
            ResponseUtils.setSuccessResult(response, query);
            LogQueryController.log.info("uri:/platform/persistent_query, method:post, response result:{}", (Object)response.getResult());
            return;
        }
        throw new RuntimeException(query.getError());
    }
    
    @HttpService(method = { RequestMethod.GET })
    public void getAll(final HttpRequest request, final HttpResponse response) {
        LogQueryController.log.info(String.format("uri:/platform/persistent_query, method:get. Request parameter: = %s ", request.getParameterMap()));
        final List<LogQuery> querys = this.service.getAll();
        final List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        for (final LogQuery query : querys) {
            final Map<String, Object> map = new HashMap<String, Object>();
            map.put("download_path", query.getDownloadPath());
            map.put("endtime", query.getEndTime());
            map.put("error", query.getError());
            map.put("event_name", query.getEventName());
            map.put("filesize", LogQueryService.getQueryResultSize(query.getId()));
            map.put("fromtime", query.getFromTime());
            map.put("id", query.getId());
            map.put("remark", query.getRemark());
            map.put("show_cols", query.getShowColumns());
            map.put("status", query.getStatus());
            map.put("terms", query.getTerms());
            map.put("total", LogQueryService.getTotalSize(query.getId()));
            ret.add(map);
        }
        ResponseUtils.setSuccessResult(response, ret);
        LogQueryController.log.info("GetAll:uri:/platform/persistent_query, response result:{}", (Object)response.getResult());
    }
    
    @HttpService(value = { "/{id}" }, method = { RequestMethod.DELETE })
    public void delete(final HttpRequest request, final HttpResponse response) {
        LogQueryController.log.info(String.format("uri:/platform/persistent_query, method:delete. Request parameter: = %s ", request.getParameterMap()));
        final Map<String, Object> attributeMap = (Map<String, Object>)request.getAttributeMap();
        final Map<String, String> map = (Map<String, String>) attributeMap.get(HttpConstant.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        final String str = map.get("id");
        final int id = Integer.valueOf(str);
        this.service.delete(id);
        ResponseUtils.setSuccessResult(response);
        LogQueryController.log.info("delete:uri:/platform/persistent_query, response result:{}", (Object)response.getResult());
    }
    
    @HttpService(value = { "/data" }, method = { RequestMethod.GET })
    public void paginate(final HttpRequest request, final HttpResponse response) {
        LogQueryController.log.info(String.format("uri:/platform/persistent_query/data, method:get. Request parameter: = %s ", request.getParameterMap()));
        int id = 0;
        int page = 0;
        int pageCount = 0;
        id = Integer.valueOf(request.getParameter("id"));
        page = Integer.valueOf(request.getParameter("page"));
        pageCount = Integer.valueOf(request.getParameter("page_count"));
        if (id == 0 || page == 0 || pageCount == 0) {
            throw new RuntimeException("paginate:parameter(error)");
        }
        final Map<String, Object> map = new HashMap<String, Object>();
        final LogQuery query = this.service.getById(id);
        map.put("download_path", LogQueryService.getDownloadPath(query.getId()));
        map.put("filesize", LogQueryService.getQueryResultSize(query.getId()));
        map.put("id", query.getId());
        map.put("total", LogQueryService.getTotalSize(query.getId()));
        map.put("values", this.service.getPaginate(id, page, pageCount));
        ResponseUtils.setSuccessResult(response, map);
        LogQueryController.log.info("uri:/platform/persistent_query/data, method:get, response result:{}", (Object)response.getResult());
    }
    
    @HttpService(value = { "/progress" }, method = { RequestMethod.GET })
    public void progress(final HttpRequest request, final HttpResponse response) {
        LogQueryController.log.info(String.format("uri:/platform/persistent_query/progress, method:get. Request parameter: = %s ", request.getParameterMap()));
        final List<Map<String, Object>> progress = this.service.progress();
        ResponseUtils.setSuccessResult(response, progress);
        LogQueryController.log.info("uri:/platform/persistent_query/data, method:get, response result:{}", (Object)response.getResult());
    }
    
    @HttpService(value = { "/download/{name}" }, method = { RequestMethod.GET })
    public void download(final HttpRequest request, final HttpResponse response) {
        final Map<String, Object> attributeMap = (Map<String, Object>)request.getAttributeMap();
        final Map<String, String> map = (Map<String, String>)attributeMap.get(HttpConstant.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        final String name = map.get("name");
        LogQueryController.log.info(String.format("uri:/platform/persistent_query/download, method:get. name: = %s ", name));
        final String data = this.service.downloadAtBase64(name);
        final File file = this.service.asDownloadFile(name);
        response.setResponseContentTypeStream(file.getName());
        response.setHeader(HttpHeaderNames.CONNECTION.toString(), HttpHeaderValues.KEEP_ALIVE.toString());
        try {
            final InputStream input = new FileInputStream(file);
            final ByteArrayOutputStream output = new ByteArrayOutputStream((int)file.length());
            int byteReads;
            while ((byteReads = input.read()) != -1) {
                output.write(byteReads);
            }
            input.close();
            output.close();
            response.setOutputStream(output);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        catch (Exception ex) {}
    }
    
    static {
        log = LoggerFactory.getLogger((Class)LogQueryController.class);
    }
}
