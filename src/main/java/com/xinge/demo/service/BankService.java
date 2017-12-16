package com.xinge.demo.service;

import com.xinge.demo.model.domain.BankDO;
import org.springframework.stereotype.Service;

/**
 * created by duanxq on 2017/11/1
 *
 * @author duanxq
 */
@Service
public class BankService extends BaseService<BankDO> {

    /**
     * 根据银行名称查询
     *
     * @param bankName 银行名称
     * @return 银行信息
     */
    public BankDO queryByBankName(String bankName) {
        BankDO query = new BankDO();
        query.setName(bankName);
        return queryForObject(query);
    }

}
