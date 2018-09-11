package com.xinge.demo.service;


import com.xinge.demo.mapper.AreaMapper;
import com.xinge.demo.model.domain.AreaDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 地区service
 *
 * @author duanx
 * @date 2017/9/3
 */
@Service
public class AreaService {

    @Autowired
    private AreaMapper areaMapper;

    /**
     * 获取省份列表
     *
     * @return 省份列表
     */
    public List<AreaDO> getProvinceList() {
        //中华人民共和国 地区编码为 1
        String countryCode = "1";
        return getSubAreaList(countryCode);
    }

    /**
     * 根据地区编号获取下级地区列表
     *
     * @param areaCode 地区编号
     * @return 下级地区列表
     */
    public List<AreaDO> getSubAreaList(String areaCode) {
        AreaDO area = new AreaDO();
        area.setParentCode(areaCode);
        return areaMapper.select(area);
    }

    /**
     * 根据主键查询地区信息
     *
     * @param areaCode 主键
     * @return 地区信息
     */
    public AreaDO getAreaByPK(String areaCode) {
        return areaMapper.selectByPrimaryKey(areaCode);
    }

}
