package com.threathunter.web.manager;

import org.mockserver.client.server.MockServerClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class MockTest {

    public static void main(String[] args) {
        List<String> params = new ArrayList<>();
        StringBuilder str = new StringBuilder(params.toString());
        System.out.println(str.toString());

/*        HttpRequest[] recordedRequests = new MockServerClient("localhost", 1080)
                .retrieveRecordedRequests(
                        request()
                                .withPath("/some/path")
                                .withMethod("POST")
                );

       for(HttpRequest request : recordedRequests) {
           String body = request.getBody().getValue().toString();
           System.out.println(body);

       }*/

        String sessionId = UUID.randomUUID().toString();
        new MockServerClient("127.0.0.1", 1080)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/test1")

                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody("{ name: 'value' }")
                );

    }

}
