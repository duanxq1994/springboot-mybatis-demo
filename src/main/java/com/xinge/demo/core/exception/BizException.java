package com.xinge.demo.core.exception;

/**
 *
 * @author AX
 * @date 2016/7/29
 */
public class BizException extends RuntimeException {

    private final int code;

    private final String errorMsg;

    public BizException(int code) {
        this.code = code;
        this.errorMsg = "";
    }

    public BizException(int code, String errorMsg) {
        this.code = code;
        this.errorMsg = errorMsg;
    }

    public int getCode() {
        return code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
