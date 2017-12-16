package com.xinge.demo.web.controller;


import com.xinge.demo.model.domain.AreaDO;
import com.xinge.demo.model.entity.BatchResultDTO;
import com.xinge.demo.model.entity.ResultEntity;
import com.xinge.demo.service.AreaService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author shanyiliang
 *         地区信息接口
 *         created by shanyiliang on 2017/9/12
 */
@RestController
@RequestMapping(value = "common/area", method = RequestMethod.POST)
public class AreaController {

    private AreaService areaService;

    /**
     * 获取省份列表
     *
     * @return 省份列表
     */
    @RequestMapping("province.json")
    public Object provinceList() {
        ResultEntity result = new ResultEntity(ResultEntity.ERROR);
        result.setList(areaService.getProvinceList());
        result.setCode(ResultEntity.SUCCESS);
        return result;
    }

    /**
     * 根据地区编号，获取下级地区列表
     *
     * @param areaCode 地区编号
     * @return 地区列表
     */
    @RequestMapping("subList.json")
    public Object areaList(String areaCode) {

        ResultEntity result = new ResultEntity(ResultEntity.ERROR);
        if (StringUtils.isNotBlank(areaCode)) {
            result.setList(areaService.getSubAreaList(areaCode));
            result.setCode(ResultEntity.SUCCESS);
        } else {
            areaCode = null;
        }

        return result;
    }

    @RequestMapping("detail.json")
    public Object getAreaDetailByCode(String areaCode) {
        ResultEntity result = new ResultEntity(ResultEntity.ERROR);
        List<AreaDO> areaList = new ArrayList<>();
        while (StringUtils.isNotBlank(areaCode) && !"1".equals(areaCode)) {
            AreaDO module = areaService.getAreaByPK(areaCode);
            if (module != null) {
                areaList.add(module);
                areaCode = module.getParentCode();
            } else {
                areaCode = null;
            }
        }
        //级别高的在前
        Collections.reverse(areaList);
        result.setList(areaList);
        result.setCode(ResultEntity.SUCCESS);
        return result;
    }


    @Autowired
    public void setAreaService(AreaService areaService) {
        this.areaService = areaService;
    }
}
