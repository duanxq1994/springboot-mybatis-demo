package com.xinge.demo.web.controller;

import com.xinge.demo.mapper.BankMapper;
import com.xinge.demo.model.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
     * 连连支付支持银行列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "bankList.json", method = RequestMethod.POST)
    public Object bankList() {
        ResultEntity result = new ResultEntity(ResultEntity.ERROR);
        result.setList(bankMapper.selectAll());
        result.setCode(ResultEntity.SUCCESS);
        return result;
    }

}
