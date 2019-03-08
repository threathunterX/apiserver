package com.threathunter.web.http.handler;

import com.threathunter.web.http.server.HttpRequest;
import com.threathunter.web.http.server.HttpResponse;

public interface HttpRequestHandler {

    void handleRequest(HttpRequest request, HttpResponse response) throws Exception;

}
