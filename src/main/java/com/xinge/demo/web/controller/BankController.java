package com.xinge.demo.web.controller;

import com.xinge.demo.mapper.BankMapper;
import com.xinge.demo.model.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * created by duanxq on 2017/11/20
 *
 * @author duanxq
 */
@RestController
public class BankController {

    @Autowired
    private BankMapper bankMapper;

    /**
     * 银行列表
     *
     * @return
     */
    @PostMapping(value = "bankList.json")
    public Object bankList() {
        ResultEntity result = new ResultEntity(ResultEntity.ERROR);
        result.setList(bankMapper.selectAll());
        result.setCode(ResultEntity.SUCCESS);
        return result;
    }

}
