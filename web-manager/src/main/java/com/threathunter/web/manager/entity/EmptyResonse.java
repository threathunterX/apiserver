package com.threathunter.web.manager.entity;

/**
 * Created by wanbaowang on 17/9/19.
 */
public class EmptyResonse {
    private int status;
    private String msg;

    public EmptyResonse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static EmptyResonse getEmptyResponse() {
        return new EmptyResonse(200, "ok");
    }
}
