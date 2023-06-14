package com.xian.edusms.controller;

import com.xian.edusms.service.SmsService;
import com.xian.entities.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/edusms/sms")
public class SmsController {
    @Autowired
    private SmsService smsService;

    /**
     * 注册
     * @param phoneNum
     * @return
     */
    @GetMapping("/registe/{phoneNum}")
    public R sendRegisteCode(@PathVariable("phoneNum") String phoneNum) {
        return smsService.sendRegisteCode(phoneNum);
    }
    /**
     * 注册
     * @param phoneNum
     * @return
     */
    @GetMapping("/login/{phoneNum}")
    public R sendLoginCode(@PathVariable("phoneNum") String phoneNum) {
        return smsService.sendLoginCode(phoneNum);
    }
    /**
     * 更改手机号
     * @param phoneNum
     * @return
     */
    @GetMapping("/Change/{phoneNum}")
    public R sendChangeCode(@PathVariable("phoneNum") String phoneNum) {
        return smsService.sendChangeCode(phoneNum);
    }
}
