package com.xian.eduucenter.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xian.eduucenter.utils.MD5;
import com.xian.utils.ConstantUtil;
import com.xian.eduucenter.entities.vo.LoginVo;
import com.xian.eduucenter.entities.Member;
import com.xian.eduucenter.entities.vo.RegisterVo;
import com.xian.eduucenter.mapper.MemberMapper;
import com.xian.eduucenter.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xian.utils.JwtUtil;
import com.xian.entities.R;
import com.xian.utils.RedisUtil;
import com.xian.servicebase.handler.RuoyiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author xian
 * @since 2022/06/02 08:49
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private MemberMapper memberMapper;
    @Override
    public R login(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        if (!StringUtils.hasText(mobile) | !StringUtils.hasText(password)) {
            throw new RuoyiException(20001, "登录失败！参数非法！");
        }
        Member one = getOne(new QueryWrapper<Member>().eq("mobile", mobile));
        if (ObjectUtil.isNull(one)) {
            throw new RuoyiException(20001, "手机号或密码错误！");
        }
        if (!one.getPassword().equals(MD5.encrypt(password))) {
            throw new RuoyiException(20001, "手机号或密码错误！");
        }
        one.setPassword(null);
        String token = JwtUtil.getJwtToken(one.getId(), one.getNickname());
        redisUtil.set(ConstantUtil.LOGIN_USER_KEY + one.getId(), JSONUtil.toJsonStr(one), 30, TimeUnit.MINUTES);
        return R.ok().data("token", token);
    }

    @Override
    public R loginBySMS(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String code = loginVo.getCode();
        if (!StringUtils.hasText(mobile) | !StringUtils.hasText(code)) {
            return R.fail().message("登录失败！参数非法！");
        }
        String key = ConstantUtil.LOGIN_CODE_KEY + mobile;
        String value = redisUtil.get(key);
        redisUtil.delete(key);
        if (!code.equals(value)) {
            return R.fail().message("验证码不正确！");
        }
        Member one = getOne(new QueryWrapper<Member>().eq("mobile", mobile));
        if (ObjectUtil.isNull(one)) {
            return R.fail().message("手机号不存在！");
        }
        one.setPassword(null);
        String token = JwtUtil.getJwtToken(one.getId(), one.getNickname());
        redisUtil.set(ConstantUtil.LOGIN_USER_KEY + one.getId(), JSONUtil.toJsonStr(one), 30, TimeUnit.MINUTES);
        return R.ok().data("token", token);
    }
    @Override
    public R getLoginInfo(HttpServletRequest request) {
        String memberId;
        try {
            memberId = JwtUtil.getIdByJwtToken(request);
        } catch (Exception e) {
            return R.fail().message("登录已过期！").code(50014);
        }
        String json = redisUtil.get(ConstantUtil.LOGIN_USER_KEY + memberId);
        if (!StringUtils.hasText(json)) {
            return R.fail().message("token已失效！请重新登陆！");
        }
        Member member = JSONUtil.toBean(json,Member.class);
        return R.ok().data("items",member);
    }

    @Override
    public R register(RegisterVo registerVo) {
        if ((!StringUtils.hasText(registerVo.getNickname()) ||
        !StringUtils.hasText(registerVo.getMobile()) ||
        !StringUtils.hasText(registerVo.getPassword()) ||
        !StringUtils.hasText(registerVo.getCode()))) {
            throw new RuoyiException(20001, "提交的信息有误！");
        }
        String code = redisUtil.get(ConstantUtil.REGISTER_CODE_KEY + registerVo.getMobile());
        if (code != null && !code.equals(registerVo.getCode())) {
            throw new RuoyiException(20001, "验证码不正确！");
        }
        redisUtil.delete(ConstantUtil.REGISTER_CODE_KEY + registerVo.getMobile());
        Member member = BeanUtil.copyProperties(registerVo, Member.class);
        member.setPassword(MD5.encrypt(member.getPassword()));
        member.setAvatar("https://eduservice-9528.oss-cn-beijing.aliyuncs.com/avatar/2022/05/17/d9b5dad6c095420fbc4358f81a02750aavatar.jpeg");
        try {
            save(member);
        } catch (Exception e) {
            throw new RuoyiException(20001, "手机号已存在！");
        }
        return R.ok().message("注册成功！");
    }
}
