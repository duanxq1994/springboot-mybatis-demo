package com.xinge.demo.service;

import com.xinge.demo.core.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

/**
 * @author BG343674
 * created by BG343674 on 2019/9/20
 */
@Slf4j
@Service
public class RetryServiceTest {


    @Retry
    public Object test(Integer i, String s) throws Exception {
        log.info("------");
        int i1 = RandomUtils.nextInt(1, 10);
        if (i1 > 5) {
            return i1;
        }

        throw new Exception("");
    }


}
