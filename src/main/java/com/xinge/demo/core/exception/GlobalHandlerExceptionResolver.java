package com.xinge.demo.core.exception;

import com.xinge.demo.common.constant.StringConstant;
import com.xinge.demo.common.util.RequestUtil;
import com.xinge.demo.model.entity.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 *
 * @author duanxq
 * @date 2017/12/1
 */
@Slf4j
public class GlobalHandlerExceptionResolver extends AbstractHandlerExceptionResolver{

    @Override
    public ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        HandlerMethod method = (HandlerMethod)handler;
        ResponseBody responseBody = method.getMethodAnnotation(ResponseBody.class);
        RestController restController = method.getBeanType().getAnnotation(RestController.class);
        if (responseBody == null && restController == null) {
            return null;
        }
        ModelAndView mv;
        if (ex instanceof BizException) {
            mv = handlerBizException((BizException) ex);
        } else if(ex instanceof IllegalArgumentException){
            mv = handlerIllegalArgumentException((IllegalArgumentException) ex);
        } else if(ex instanceof BindException){
            mv = handlerBindException((BindException) ex);
        } else if(ex instanceof MethodArgumentNotValidException){
            mv = handlerMethodArgumentNotValidException((MethodArgumentNotValidException) ex);
        } else if(ex instanceof DataAccessException){
            mv = handlerDataAccessException((DataAccessException) ex);
        } else {
            mv = handlerException(ex);
        }
        return mv;
    }

    private ModelAndView handlerBizException(BizException e) {
        Integer code = e.getCode();
        String message = e.getMessage();
        String error = StringUtils.defaultIfBlank(message, ErrorCode.BIZ_ERROR.getMsg());
        return createResultEntity(code, error, message);
    }

    private ModelAndView handlerIllegalArgumentException(IllegalArgumentException e) {
        Integer code = ErrorCode.ARGUMENTS_ERROR.getCode();
        String message = e.getMessage();
        String error = StringUtils.defaultIfBlank(message, ErrorCode.ARGUMENTS_ERROR.getMsg());
        return createResultEntity(code, error, message);
    }

    private ModelAndView handlerBindException(BindException e) {
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

    private ModelAndView handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Integer code = ErrorCode.ARGUMENTS_ERROR.getCode();
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        String error = StringUtils.defaultIfBlank(message, ErrorCode.ARGUMENTS_ERROR.getMsg());
        return createResultEntity(code, error, message);
    }

    private ModelAndView handlerDataAccessException(DataAccessException e) {
        Integer code = ErrorCode.PERSISTENCE_ERROR.getCode();
        String message = e.getMessage();
        String error = ErrorCode.PERSISTENCE_ERROR.getMsg();
        log.warn(e.getMessage());
        return createResultEntity(code, error, message);
    }

    private ModelAndView handlerException(Exception e) {
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
    private ModelAndView createResultEntity(Integer code, String error, String message) {
        ResultEntity resultEntity = new ResultEntity(code);
        resultEntity.setError(error);
        resultEntity.setMsg(message);
        HttpServletRequest request = RequestUtil.getRequest();
        request.setAttribute(StringConstant.REQUEST_MAP, resultEntity);
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setAttributesMap(resultEntity);
        return new ModelAndView(view);
    }

}
