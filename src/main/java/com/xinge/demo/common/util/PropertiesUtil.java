package com.xinge.demo.common.util;

import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

/**
 * @author duanxq
 * created by duanxq on 2019/2/22
 */
@Component
public class PropertiesUtil implements EmbeddedValueResolverAware {

    private static StringValueResolver valueResolver;

    @Override
    public void setEmbeddedValueResolver(StringValueResolver stringValueResolver) {
        valueResolver = stringValueResolver;
    }

    public static String getPropertiesValue(String name) {
        return valueResolver.resolveStringValue(name);
    }
}
