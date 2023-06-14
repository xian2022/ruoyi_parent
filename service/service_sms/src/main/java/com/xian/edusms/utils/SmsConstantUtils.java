package com.xian.edusms.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
public class SmsConstantUtils implements InitializingBean {
    @Value("${smsSignId}")
    private String smsSignId;
    @Value("${appcode}")
    private String appcode;
    public static String SMSSIGNID;
    public static String APPCODE;
    public static final String GENERAL_CODE = "908e94ccf08b4476ba6c876d13f084ad";
    public static final String REGISTE_CODE = "a09602b817fd47e59e7c6e603d3f088d";
    public static final String MODIFY_CODE = "29833afb9ae94f21a3f66af908d54627";
    public static final String LOGIN_CODE = "02551a4313154fe4805794ca069d70bf";
    public static final String CHANGE_CODE = "ea66d14c664649a69a19a6b47ba028db";

    @Override
    public void afterPropertiesSet() throws Exception {
        APPCODE = this.appcode;
        SMSSIGNID = this.smsSignId;
    }
}
