package com.xian.edusms;

import cn.hutool.core.util.RandomUtil;
import com.xian.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class SmsTestApplication {
    @Resource
    private RedisUtil redisUtil;
    @Test
    public void test1() {
        for (int i = 0; i < 10; i++) {
            String code = RandomUtil.randomNumbers(6);
        }
    }
}
