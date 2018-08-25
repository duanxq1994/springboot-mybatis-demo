package com.xinge.demo.service;

import com.xinge.demo.Application;
import com.xinge.demo.model.domain.BankDO;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author duanxq
 * @date 2018/1/31
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BankServiceTest {

    @Autowired
    private BankService bankService;

    @Test
    @Ignore
    public void queryByBankName() {
        BankDO bankDO = bankService.queryByBankName("工商银行");
        assertEquals(bankDO.getName(),"工商银行");
    }


}