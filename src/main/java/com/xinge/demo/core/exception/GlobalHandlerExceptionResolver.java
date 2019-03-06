package com.xinge.demo.core.exception;

import com.xinge.demo.common.constant.StringConstant;
import com.xinge.demo.common.util.MapUtil;
import com.xinge.demo.common.util.RequestUtil;
import com.xinge.demo.model.entity.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
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
public class GlobalHandlerExceptionResolver extends AbstractHandlerExceptionResolver {

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
        ModelAndView mv;
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        if (ex instanceof BizException) {
            mv = handlerBizException((BizException) ex);
        } else if (ex instanceof IllegalArgumentException) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            mv = handlerIllegalArgumentException((IllegalArgumentException) ex);
        } else if (ex instanceof BindException) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            mv = handlerBindException((BindException) ex);
        } else if (ex instanceof MethodArgumentNotValidException) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            mv = handlerMethodArgumentNotValidException((MethodArgumentNotValidException) ex);
        } else if (ex instanceof DataAccessException) {
            mv = handlerDataAccessException((DataAccessException) ex);
        } else {
            mv = handlerException(ex);
        }
        return mv;
    }

    private ModelAndView handlerBizException(BizException e) {
        Integer code = e.getCode();
        String message = StringUtils.defaultIfBlank(e.getMessage(), ErrorCode.BIZ_ERROR.getMsg());
        return createResultEntity(code, message, message);
    }

    private ModelAndView handlerIllegalArgumentException(IllegalArgumentException e) {
        Integer code = ErrorCode.ARGUMENTS_ERROR.getCode();
        String message = StringUtils.defaultIfBlank(e.getMessage(), ErrorCode.ARGUMENTS_ERROR.getMsg());
        return createResultEntity(code, message, message);
    }

    private ModelAndView handlerBindException(BindException e) {
        Integer code = ErrorCode.ARGUMENTS_ERROR.getCode();
        String message = e.getFieldError().getDefaultMessage();
        String error = message;
        //错误信息长度限制为20
        int lengthLimit = 20;
        if (StringUtils.isBlank(message) || message.length() > lengthLimit) {
            message = ErrorCode.ARGUMENTS_ERROR.getMsg();
        }
        return createResultEntity(code, error, message);
    }

    private ModelAndView handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Integer code = ErrorCode.ARGUMENTS_ERROR.getCode();
        String message = StringUtils.defaultIfBlank(e.getBindingResult().getFieldError().getDefaultMessage(), ErrorCode.ARGUMENTS_ERROR.getMsg());
        return createResultEntity(code, message, message);
    }

    private ModelAndView handlerDataAccessException(DataAccessException e) {
        Integer code = ErrorCode.PERSISTENCE_ERROR.getCode();
        String error = ExceptionUtils.getRootCauseMessage(e);
        String message = ErrorCode.PERSISTENCE_ERROR.getMsg();
        log.warn("", e);
        return createResultEntity(code, error, message);
    }

    private ModelAndView handlerException(Exception e) {
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
        ResultEntity resultEntity = new ResultEntity(code);
        resultEntity.setError(error);
        resultEntity.setMessage(message);
        HttpServletRequest request = RequestUtil.getRequest();
        request.setAttribute(StringConstant.REQUEST_MAP, resultEntity);
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setAttributesMap(MapUtil.beanToMap(resultEntity));
        return new ModelAndView(view);
    }

}
