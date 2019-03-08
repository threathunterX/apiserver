package com.threathunter.web.http.route;


import com.threathunter.web.http.server.HttpRequest;

public interface RequestCondition<T> {

    T combine(T other);

    T getMatchingCondition(HttpRequest request);

    int compareTo(T other, HttpRequest request);

}
