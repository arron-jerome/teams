package com.disney.teams.model.vo;

import java.io.Serializable;

public abstract class BaseStatusCode implements Serializable {

    private static final long serialVersionUID = 968330307493536297L;

    public abstract String getBaseCode();

    public final static String SUCCESS_CODE = "2000";
    public final static String SUCCESS_MSG = "success";

    //	客户端常用错误码
    public static final String CLIENT_ERROR_CODE = "A001";
    // 参数为空
    public static final String CLIENT_EMPTY_PARAMETER_CODE = "A002";
    // 未登录
    public static final String CLIENT_NOT_LOGIN_CODE = "A003";
    //	访问拒绝
    public static final String CLIENT_ERROR_ACCESS_DENY_CODE = "A004";
    //	接口访问太频繁
    public static final String CLIENT_ERROR_ACCESS_TOO_FREQUENTLY_CODE = "A005";
    // 重复提交
    public static final String CLIENT_ERROR_RESUBMIT_CODE = "A006";

    //	服务器常用错误码
    public final static String SERVER_ERROR_CODE = "B001";
    public static final String SERVER_ERROR_MSG = "Server Error";
    public final static String SERVER_BUSY_ERROR_CODE = "B002";

    // Third Partner Error
    public static final String TP_ERROR_CODE = "C001";
    // Database Error
    public static final String DATABASE_ERROR = "C001";
    // Cache Error
    public static final String CACHE_ERROR = "C003";


    private final String code;
    private final String message;

    public BaseStatusCode() {
        this.code = this.getBaseCode() + SERVER_ERROR_CODE;
        this.message = SERVER_ERROR_MSG;
    }

    public BaseStatusCode(String code, String message) {
        this.code = this.getBaseCode() + (code == null ? SERVER_ERROR_CODE : code);
        this.message = (message == null ? SERVER_ERROR_MSG : message);
    }

    public final String getCode() {
        return code;
    }

    public final String getMessage() {
        return message;
    }

    public String toString() {
        return code + "(" + message + ")";
    }
}
