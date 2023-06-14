package com.xian.serviceorder.utils;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AliPayConstantUtil implements InitializingBean {

    @Value("${alipay.appid}")
    private String appid;
    @Value("${alipay.appPrivateKey}")
    private String appPrivateKey;
    @Value("${alipay.charset}")
    private String charset;
    @Value("${alipay.alipayPublicKey}")
    private String alipayPublicKey;
    @Value("${alipay.gatewayUrl}")
    private String gatewayUrl;
    @Value("${alipay.format}")
    private String format;
    @Value("${alipay.signType}")
    private String signType;
    @Value("${alipay.notifyUrl}")
    private String notifyUrl;
    @Value("${alipay.returnUrl}")
    private String returnUrl;

    //appid
    public static String APP_ID;
    //应用私钥
    public static String APP_PRIVATE_KEY;
    public static String CHARSET;
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY;
    //这是沙箱接口路径,正式路径为https://openapi.alipay.com/gateway.do
    public static String GATEWAY_URL;
    public static String FORMAT;
    //签名方式
    public static String SIGN_TYPE;
    //支付宝异步通知路径,付款完毕后会异步调用本项目的方法,必须为公网地址
    public static String NOTIFY_URL;
    //支付宝同步通知路径,也就是当付款完毕后跳转本项目的页面,可以不是公网地址
    public static String RETURN_URL;

    public static AlipayTradePagePayRequest alipayPayRequest;
    public static AlipayTradeRefundRequest alipayRefundRequest;

    @Override
    public void afterPropertiesSet() throws Exception {
        APP_ID=appid;
        APP_PRIVATE_KEY=appPrivateKey;
        CHARSET=charset;
        ALIPAY_PUBLIC_KEY=alipayPublicKey;
        GATEWAY_URL=gatewayUrl;
        FORMAT=format;
        SIGN_TYPE=signType;
        NOTIFY_URL=notifyUrl;
        RETURN_URL=returnUrl;
        alipayPayRequest = new AlipayTradePagePayRequest ();
        alipayRefundRequest = new AlipayTradeRefundRequest ();
        alipayPayRequest.setReturnUrl(RETURN_URL);
        alipayPayRequest.setNotifyUrl(NOTIFY_URL);
    }

    public AlipayTradePagePayRequest setBizContent(String orderId, BigDecimal totalAmount, String subject) {
        alipayPayRequest.setBizContent(
                "{\"out_trade_no\":\"" + orderId + "\","
                        + "\"total_amount\":\"" + totalAmount + "\","
                        + "\"subject\":\"" + subject + "\","
                        + "\"body\":\"" + "\","
                        + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}"
        );
        return alipayPayRequest;
    }

    public AlipayTradePagePayRequest setBizContent(String orderId, BigDecimal totalAmount, String subject, String body) {
        alipayPayRequest.setBizContent(
                "{\"out_trade_no\":\"" + orderId + "\","
                + "\"total_amount\":\"" + totalAmount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}"
        );
        return alipayPayRequest;
    }

    public AlipayTradeRefundRequest refundSetBizContent(String trade_no, BigDecimal refund_amount ,String out_trade_no) {
        alipayRefundRequest.setBizContent(
                "{\"trade_no\":\"" + trade_no + "\"," + "\"refund_amount\":\"" + refund_amount + "\","
                        + "\"out_request_no\":\"" + out_trade_no + "\"}"
        );
        return alipayRefundRequest;
    }

    public AlipayClient getAlipayClient() {
        return new DefaultAlipayClient(GATEWAY_URL,APP_ID,APP_PRIVATE_KEY,FORMAT,CHARSET,ALIPAY_PUBLIC_KEY,SIGN_TYPE);
    }
}
