package com.xian.edusms.service.Impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.xian.edusms.entities.Result;
import com.xian.edusms.service.SmsService;
import com.xian.edusms.utils.HttpUtils;
import com.xian.edusms.utils.SmsConstantUtils;
import com.xian.entities.R;
import com.xian.utils.ConstantUtil;
import com.xian.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {
    @Resource
    private RedisUtil redisUtil;
    @Override
    public R sendRegisteCode(String phoneNum) {
        try {
            String key = ConstantUtil.REGISTER_CODE_KEY + phoneNum;
            return sendSMS(key,phoneNum, SmsConstantUtils.REGISTE_CODE);
        } catch (Exception e) {
            return R.fail();
        }
    }

    @Override
    public R sendLoginCode(String phoneNum) {
        try {
            String key = ConstantUtil.LOGIN_CODE_KEY + phoneNum;
            return sendSMS(key,phoneNum, SmsConstantUtils.LOGIN_CODE);
        } catch (Exception e) {
            return R.fail();
        }
    }

    @Override
    public R sendChangeCode(String phoneNum) {
        try {
            String key = ConstantUtil.CHANGE_PHONE_CODE_KEY + phoneNum;
            return sendSMS(key, phoneNum, SmsConstantUtils.CHANGE_CODE);
        } catch (Exception e) {
            return R.fail();
        }
    }

    private R sendSMS(String key,String mobile,String templateId) {
        String code = RandomUtil.randomNumbers(6);
        String host = "https://gyytz.market.alicloudapi.com";
        String path = "/sms/smsSend";
        String method = "POST";
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + SmsConstantUtils.APPCODE);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", mobile);
        querys.put("param", "**code**:"+code+",**minute**:5");
        querys.put("smsSignId", SmsConstantUtils.SMSSIGNID);
        querys.put("templateId", templateId);
        Map<String, String> bodys = new HashMap<>();
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            //获取response的body
            Result result = JSONUtil.toBean(EntityUtils.toString(response.getEntity()), Result.class);
            if (result.getCode() == 0) {
                redisUtil.set(key,code,5, TimeUnit.MINUTES);
                log.info("\n手机号为："+mobile+" 您的短信验证码为："+code);
                return R.ok();
            }
            return R.fail().message(result.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.fail().message("出错啦，请稍后再试！");
    }
}
