package com.xinge.demo.core;

import com.xinge.demo.common.constant.StringConstant;
import com.xinge.demo.common.util.RequestUtil;
import com.xinge.demo.core.entity.ResultEntity;
import com.xinge.demo.core.entity.SuccessResult;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;


/**
 * 通用json结果返回处理
 * @author duanxq
 */
@ControllerAdvice(basePackages = "com.xinge.demo.web")
@Order(0)
public class JsonResultBodyAdvice implements ResponseBodyAdvice<Object> {


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        // 这里把返回的结果设置到request中，供拦截器使用【使用ResponseBody的时候，拦截器postHandler中无法取到返回结果】
        HttpServletRequest req = RequestUtil.getRequest();
        if (!(body instanceof SuccessResult)) {
            ResultEntity<Object> resultEntity = new ResultEntity<>();
            resultEntity.setData(body);
            req.setAttribute(StringConstant.REQUEST_MAP, resultEntity);
            return resultEntity;
        }
        req.setAttribute(StringConstant.REQUEST_MAP, body);
        return body;
    }

}
