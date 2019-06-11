package com.xinge.demo.core.exception;

import com.xinge.demo.common.util.MapUtil;
import com.xinge.demo.core.entity.ResultEntity;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * @author BG343674
 * created by BG343674 on 2019/6/11
 */
public class MvUtil {

    public static ModelAndView entity2MV(ResultEntity resultEntity) {
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setAttributesMap(MapUtil.beanToMap(resultEntity));
        return new ModelAndView(view);
    }

}
