package com.xinge.demo.core.exception;

import com.xinge.demo.common.constant.StringConstant;
import com.xinge.demo.model.entity.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 *
 * @author duanxq
 * @date 2017/12/1
 */
@Slf4j
//@ResponseBody
//@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private HttpServletRequest request;

    @ExceptionHandler(BizException.class)
    public Object handlerBizException(BizException e) {
        Integer code = e.getCode();
        String message = e.getMessage();
        String error = StringUtils.defaultIfBlank(message, ErrorCode.BIZ_ERROR.getMsg());
        return createResultEntity(code, error, message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Object handlerIllegalArgumentException(IllegalArgumentException e) {
        Integer code = ErrorCode.ARGUMENTS_ERROR.getCode();
        String message = e.getMessage();
        String error = StringUtils.defaultIfBlank(message, ErrorCode.ARGUMENTS_ERROR.getMsg());
        return createResultEntity(code, error, message);
    }

    @ExceptionHandler(BindException.class)
    public Object handlerBindException(BindException e) {
        Integer code = ErrorCode.ARGUMENTS_ERROR.getCode();
        String message = e.getFieldError().getDefaultMessage();
        String error;
        //错误信息长度限制为20
        int lengthLimit = 20;
        if (StringUtils.isNotBlank(message) && message.length() <= lengthLimit) {
            error = message;
        } else {
            error = ErrorCode.ARGUMENTS_ERROR.getMsg();
        }
        return createResultEntity(code, error, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Integer code = ErrorCode.ARGUMENTS_ERROR.getCode();
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        String error = StringUtils.defaultIfBlank(message, ErrorCode.ARGUMENTS_ERROR.getMsg());
        return createResultEntity(code, error, message);
    }

    @ExceptionHandler(DataAccessException.class)
    public Object handlerDataAccessException(DataAccessException e) {
        Integer code = ErrorCode.PERSISTENCE_ERROR.getCode();
        String message = e.getMessage();
        String error = ErrorCode.PERSISTENCE_ERROR.getMsg();
        log.warn(e.getMessage());
        return createResultEntity(code, error, message);
    }

    @ExceptionHandler(Exception.class)
    public Object handlerException(Exception e) {
        Integer code = ErrorCode.SYSTEM_ERROR.getCode();
        String message = e.getMessage();
        String error = ErrorCode.SYSTEM_ERROR.getMsg();
        log.warn(error, e);
        return createResultEntity(code, error, message);
    }

    /**
     * 生成返回的实体
     *
     * @param code    错误编号
     * @param error   错误提示
     * @param message 错误信息
     * @return 返回实体
     */
    private ResultEntity createResultEntity(Integer code, String error, String message) {
        ResultEntity resultEntity = new ResultEntity(code);
        resultEntity.setError(error);
        resultEntity.setMessage(message);
        request.setAttribute(StringConstant.REQUEST_MAP, resultEntity);
        return resultEntity;
    }

}
