package com.threathunter.web.manager.utils;

/**
 * 
 */
public class ControllerException extends IllegalStateException {

    public ControllerException(String massage) {
        super(massage);
    }

    public ControllerException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
