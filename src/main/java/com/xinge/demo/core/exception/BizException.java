package com.xinge.demo.core.exception;

/**
 *
 * @author AX
 * @date 2016/7/29
 */
public class BizException extends RuntimeException {

    private final int code;

    private Object data;

    public BizException(String errorMsg) {
        super(errorMsg);
        this.code = ErrorCode.BIZ_ERROR.getCode();
    }

    public BizException(int code, String errorMsg) {
        super(errorMsg);
        this.code = code;
    }

    public BizException(int code, String errorMsg, Object data) {
        super(errorMsg);
        this.code = code;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

}
