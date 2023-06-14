package com.xian.vod.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
public class ConstantPropertiesUtils implements InitializingBean {
    //读取配置文件
    @Value("${aliyun.oss.file.keyid}")
    private String keyId;
    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;
    @Value("${aliyun.oss.file.regionId}")
    private String regionId;

    //定义公开静态的常量
    public static String KEY_ID;
    public static String KEY_SECRET;
    public static String KEY_REGIONID;
    @Override
    public void afterPropertiesSet() throws Exception {
        KEY_ID=keyId;
        KEY_SECRET=keySecret;
        KEY_REGIONID=regionId;
    }
}
