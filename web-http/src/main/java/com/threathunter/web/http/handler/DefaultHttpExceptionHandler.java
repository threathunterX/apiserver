package com.threathunter.web.http.handler;

import com.threathunter.web.common.json.JsonUtils;
import com.threathunter.web.common.utils.ExceptionUtils;
import com.threathunter.web.http.server.HttpRequest;
import com.threathunter.web.http.server.HttpResponse;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DefaultHttpExceptionHandler implements HttpExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultHttpExceptionHandler.class);

    public void exceptionHandle(HttpRequest request, HttpResponse response, Exception exception) {
        logger.error("exception thrown:{}", exception);
        Map<String, Object> headMap = Maps.newLinkedHashMap();
        headMap.put("requestId", request.getRequestId());
        headMap.put("status", 600);
        headMap.put("msg", "系统异常：" + ExceptionUtils.getErrorMessageWithNestedException(exception));
        Map<String, Object> resultMap = Maps.newLinkedHashMap();
        resultMap.put("head", headMap);
        String result = JsonUtils.toJson(resultMap);
        logger.info("result:{}", result);
        response.setResult(result);
    }

}
