package com.xinge.demo.core.exception;

import com.xinge.demo.common.util.MapUtil;
import com.xinge.demo.core.entity.ResultEntity;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;

import java.util.Map;

/**
 * @author duanxq
 * @date 2019/3/24
 */
@Component
public class MyErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode((Integer) errorAttributes.get("status"));
        resultEntity.setMessage((String) errorAttributes.get("error"));
        resultEntity.setError((String) errorAttributes.get("exception"));
        Map<String, Object> map = MapUtil.beanToMap(resultEntity);
        map.putAll(errorAttributes);
        return map;
    }

    @Override
    public Throwable getError(RequestAttributes requestAttributes) {
        return super.getError(requestAttributes);
    }
}
