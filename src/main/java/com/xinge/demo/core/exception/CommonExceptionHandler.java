package com.xinge.demo.core.exception;

import com.xinge.demo.common.constant.StringConstant;
import com.xinge.demo.common.util.RequestUtil;
import com.xinge.demo.core.entity.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author BG343674
 * created by BG343674 on 2019/6/11
 */
@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handlerBizException(BizException e) {
        Integer code = e.getCode();
        String message = StringUtils.defaultIfBlank(e.getMessage(), ErrorCode.BIZ_ERROR.getMsg());
        Object data = e.getData();
        return createResultEntity(new ResultEntity<>(code, message, message, data));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handlerIllegalArgumentException(IllegalArgumentException e) {
        Integer code = ErrorCode.ARGUMENTS_ERROR.getCode();
        String message = StringUtils.defaultIfBlank(e.getMessage(), ErrorCode.ARGUMENTS_ERROR.getMsg());
        return createResultEntity(new ResultEntity(code, message, message));
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handlerDataAccessException(DataAccessException e) {
        Integer code = ErrorCode.PERSISTENCE_ERROR.getCode();
        String error = ExceptionUtils.getRootCauseMessage(e);
        String message = ErrorCode.PERSISTENCE_ERROR.getMsg();
        log.warn("", e);
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
