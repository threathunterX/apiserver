package com.threathunter.web.manager.utils;

/**
 * Created by wanbaowang on 17/9/19.
 */
public class ControllerException extends IllegalStateException {

    public ControllerException(String massage) {
        super(massage);
    }

    public ControllerException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
