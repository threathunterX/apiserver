package com.threathunter.web.http.handler;

import com.threathunter.web.http.server.HttpResponse;
import com.threathunter.web.http.server.HttpRequest;

public interface HttpExceptionHandler {

    /**
     * service处理异常执行
     * @param request
     * @param response
     * @param exception
     */
    void exceptionHandle(HttpRequest request, HttpResponse response, Exception exception);

}
