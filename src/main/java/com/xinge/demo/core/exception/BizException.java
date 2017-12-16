package com.xinge.demo.core.exception;

/**
 *
 * @author AX
 * @date 2016/7/29
 */
public class BizException extends RuntimeException {

    private int code = ErrorCode.BIZ_ERROR.getCode();

    private String errorMsg;

    public BizException(int code) {
        this.code = code;
    }

    public BizException(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public BizException(int code, String errorMsg) {
        this.code = code;
        this.errorMsg = errorMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
