package com.xinge.demo.core.exception;

/**
 *
 * @author lu
 * @date 16/8/3
 */
public enum ErrorCode {

    SYSTEM_ERROR(1, "系统异常"),
    PERSISTENCE_ERROR(2, "数据库异常"),
    ARGUMENTS_ERROR(3, "参数错误"),
    BIZ_ERROR(4, "业务异常"),


    OTHER_ERROR(9999, "其它异常");

    //异常码
    private int code;
    //异常信息
    private String msg;

    private ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    //根据异常码获取异常信息
    public static String getMsg(int code) {
        for (ErrorCode esnCode : ErrorCode.values()) {
            if (esnCode.getCode() == code)
                return esnCode.getMsg();
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
