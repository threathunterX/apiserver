package com.threathunter.web.http.interceptor;

import com.threathunter.web.http.server.HttpRequest;
import com.threathunter.web.http.server.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpendTimeInterceptor implements HandlerInterceptor {

    private ThreadLocal<Long> spendTimeLocal = new ThreadLocal<>();

    private static final Logger logger = LoggerFactory.getLogger(SpendTimeInterceptor.class);

    public boolean preHandle(HttpRequest request, HttpResponse response) throws Exception {
        spendTimeLocal.set(System.currentTimeMillis());
        return true;
    }

    public void postHandle(HttpRequest request, HttpResponse response) throws Exception {
        long start = spendTimeLocal.get();
        logger.info("spend time " + String.valueOf(System.currentTimeMillis() - start));
        spendTimeLocal.remove();
    }

}
