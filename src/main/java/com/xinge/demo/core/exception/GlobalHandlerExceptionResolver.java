package com.xinge.demo.core.exception;

import com.xinge.demo.common.constant.StringConstant;
import com.xinge.demo.common.util.MapUtil;
import com.xinge.demo.common.util.RequestUtil;
import com.xinge.demo.core.entity.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 全局异常处理
 *
 * @author duanxq
 * @date 2017/12/1
 */
@Slf4j
public class GlobalHandlerExceptionResolver extends DefaultHandlerExceptionResolver {

    @Override
    public ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (!(handler instanceof HandlerMethod)) {
            return null;
        }
        HandlerMethod method = (HandlerMethod) handler;
        ResponseBody responseBody = method.getMethodAnnotation(ResponseBody.class);
        RestController restController = method.getBeanType().getAnnotation(RestController.class);
        if (responseBody == null && restController == null) {
            return null;
        }
        if (ex instanceof BizException) {
            return handlerBizException((BizException) ex, request, response, handler);
        } else if (ex instanceof IllegalArgumentException) {
            return handlerIllegalArgumentException((IllegalArgumentException) ex, request, response, handler);
        } else if (ex instanceof DataAccessException) {
            return handlerDataAccessException((DataAccessException) ex, request, response, handler);
        } else {
            ModelAndView mv = super.doResolveException(request, response, handler, ex);
            if (mv != null) {
                return mv;
            }
            return handlerException(ex, request, response, handler);
        }
    }

    private ModelAndView handlerBizException(BizException e, HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        Integer code = e.getCode();
        String message = StringUtils.defaultIfBlank(e.getMessage(), ErrorCode.BIZ_ERROR.getMsg());
        Object data = e.getData();
        return createResultEntity(code, message, message, data);
    }

    private ModelAndView handlerIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        Integer code = ErrorCode.ARGUMENTS_ERROR.getCode();
        String message = StringUtils.defaultIfBlank(e.getMessage(), ErrorCode.ARGUMENTS_ERROR.getMsg());
        return createResultEntity(code, message, message);
    }

    @Override
    protected ModelAndView handleBindException(BindException ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        Integer code = ErrorCode.ARGUMENTS_ERROR.getCode();
        String message = ex.getFieldError().getDefaultMessage();
        String error = message;
        //错误信息长度限制为20
        int lengthLimit = 20;
        if (StringUtils.isBlank(message) || message.length() > lengthLimit) {
            message = ErrorCode.ARGUMENTS_ERROR.getMsg();
        }
        return createResultEntity(code, error, message);
    }

    @Override
    protected ModelAndView handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        Integer code = ErrorCode.ARGUMENTS_ERROR.getCode();
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String message = StringUtils.defaultIfBlank(fieldError.getDefaultMessage(), ErrorCode.ARGUMENTS_ERROR.getMsg());
        String error = String.format("[%s]%s", fieldError.getField(), message);
        return createResultEntity(code, error, message);
    }

    private ModelAndView handlerDataAccessException(DataAccessException e, HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        Integer code = ErrorCode.PERSISTENCE_ERROR.getCode();
        String error = ExceptionUtils.getRootCauseMessage(e);
        String message = ErrorCode.PERSISTENCE_ERROR.getMsg();
        log.warn("", e);
        return createResultEntity(code, error, message);
    }

    private ModelAndView handlerException(Exception e, HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        Integer code = ErrorCode.SYSTEM_ERROR.getCode();
        String error = ExceptionUtils.getRootCauseMessage(e);
        String message = ErrorCode.SYSTEM_ERROR.getMsg();
        log.warn(message, e);
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
    private ModelAndView createResultEntity(Integer code, String error, String message) {
        return createResultEntity(code, error, message, null);
    }

    /**
     * 生成返回的实体
     *
     * @param code    错误编号
     * @param error   错误提示
     * @param message 错误信息
     * @param data    报错并返回的实体信息
     * @return 返回实体
     */
    private ModelAndView createResultEntity(Integer code, String error, String message, Object data) {
        ResultEntity<Object> resultEntity = new ResultEntity<>(code);
        resultEntity.setError(error);
        resultEntity.setMessage(message);
        resultEntity.setData(data);
        HttpServletRequest request = RequestUtil.getRequest();
        request.setAttribute(StringConstant.REQUEST_MAP, resultEntity);
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setAttributesMap(MapUtil.beanToMap(resultEntity));
        return new ModelAndView(view);
    }

}
