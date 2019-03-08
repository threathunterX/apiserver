package com.threathunter.web.http.interceptor;

import com.threathunter.web.http.server.HttpRequest;
import com.threathunter.web.http.server.HttpResponse;

/**
 * 类似于spring mvc的拦截器
 */
public interface HandlerInterceptor {

    /**
     * handler处理前执行
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    boolean preHandle(HttpRequest request, HttpResponse response) throws Exception;

    /**
     * handler处理后执行
     * @param request
     * @param response
     * @throws Exception
     */
    void postHandle(HttpRequest request, HttpResponse response) throws Exception;

}
