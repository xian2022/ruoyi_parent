package com.serviceorder;

import com.xian.serviceorder.utils.OrderUtil;
import org.junit.jupiter.api.Test;

public class TestApp {
    @Test
    public void snowFlake() throws Exception {
//        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(12L);
//        System.out.println(snowflakeIdWorker.nextId());
//        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//        System.out.println(time);
        // 20220626183438
//        Date date1 = new SimpleDateFormat("yyyyMMddHHmmss").parse("20230626183438");
//        Date date2 = new SimpleDateFormat("yyyyMMddHHmmss").parse("20220626183438");
//        System.out.println(date1.getTime()-date2.getTime());
//        String macAddress = OrderUtil.getIpAddress();
//        System.out.println(macAddress);
        // System.out.println(OrderUtil.createId("1530399918999474177", "1538092457735057410"));
        System.out.println(OrderUtil.createId());
    }
}
