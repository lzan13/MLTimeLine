package com.vmloft.develop.app.timeline.common;

public class CResult {

    public int code = 0;
    public String msg = "";
    public Object object = null;

    public CResult() {}

    public CResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CResult(int code, String msg, Object object) {
        this.code = code;
        this.msg = msg;
        this.object = object;
    }

    public CResult(Object object) {
        this.object = object;
    }
}
