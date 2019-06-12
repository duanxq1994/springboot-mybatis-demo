package com.xinge.demo.core.exception;

import com.xinge.demo.common.constant.StringConstant;
import com.xinge.demo.common.util.RequestUtil;
import com.xinge.demo.core.entity.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常exception处理
 *
 * @author duanxq
 * @date 2017/12/1
 */
@Slf4j
public class CustomDefaultHandlerExceptionResolver extends DefaultHandlerExceptionResolver {

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView modelAndView = super.doResolveException(request, response, handler, ex);
        if (modelAndView != null) {
            return modelAndView;
        }
        return handleException(ex, request, response, handler);
    }

    private ModelAndView handleException(Exception ex, HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        Integer code = ErrorCode.SYSTEM_ERROR.getCode();
        String error = ExceptionUtils.getRootCauseMessage(ex);
        String message = ErrorCode.SYSTEM_ERROR.getMsg();
        log.warn(message, ex);
        return createResultEntity(new ResultEntity(code, error, message));
    }

    /**
     * 生成返回的实体
     *
     * @param resultEntity    实体信息
     * @return 返回实体
     */
    private ModelAndView createResultEntity(ResultEntity resultEntity) {
        HttpServletRequest request = RequestUtil.getRequest();
        request.setAttribute(StringConstant.REQUEST_MAP, resultEntity);
        return MvUtil.entity2MV(resultEntity);
    }

}
