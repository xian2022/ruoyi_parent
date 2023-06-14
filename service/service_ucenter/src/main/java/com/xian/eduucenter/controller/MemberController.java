package com.xian.eduucenter.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xian.annotation.CheckMemberLogin;
import com.xian.eduucenter.annotations.LoginCount;
import com.xian.eduucenter.annotations.RegisterCount;
import com.xian.eduucenter.entities.Member;
import com.xian.eduucenter.entities.vo.ChangePhone;
import com.xian.eduucenter.entities.vo.LoginVo;
import com.xian.eduucenter.entities.vo.MemberInfo;
import com.xian.eduucenter.entities.vo.RegisterVo;
import com.xian.eduucenter.feign.OrderFeign;
import com.xian.eduucenter.service.MemberService;
import com.xian.eduucenter.utils.MD5;
import com.xian.entities.R;
import com.xian.utils.ConstantUtil;
import com.xian.utils.JwtUtil;
import com.xian.utils.RedisUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author xian
 * @since 2022/06/02 08:49
 */
@CrossOrigin
@RestController
@RequestMapping("/eduucenter/member")
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private OrderFeign orderFeign;

    @ApiOperation("会员登录")
    @PostMapping("/login")
    @LoginCount
    public R login(@RequestBody LoginVo loginVo) {
        return memberService.login(loginVo);
    }

    @ApiOperation("会员登录")
    @PostMapping("/loginBySMS")
    @LoginCount
    public R loginBySMS(@RequestBody LoginVo loginVo) {
        return memberService.loginBySMS(loginVo);
    }

    @ApiOperation("会员注册")
    @PostMapping("/register")
    @RegisterCount
    public R register(@RequestBody RegisterVo registerVo) {
        return memberService.register(registerVo);
    }

    @CheckMemberLogin
    @ApiOperation("根据用户token获取登录信息")
    @GetMapping("/auth/getLoginInfo")
    public R getLoginInfo(HttpServletRequest request) {
        return memberService.getLoginInfo(request);
    }

    @CheckMemberLogin
    @ApiOperation("根据用户Id获取用户信息")
    @GetMapping("/getMemberInfoById/{memberId}")
    public R getMemberInfoById(@PathVariable("memberId")String memberId) {
        Member member = memberService.getById(memberId);
        if (ObjectUtil.isNull(member)) {
            return R.fail().message("用户不存在！");
        }
        MemberInfo memberInfo = BeanUtil.copyProperties(member, MemberInfo.class, "password");
        return R.ok().data("member",memberInfo);
    }

    @CheckMemberLogin
    @ApiOperation("根据用户id获取用户个人中心的信息")
    @GetMapping("/getMemberAllInfo/{memberId}")
    public R getMemberAllInfo(@PathVariable("memberId")String memberId) {
        Member member = memberService.getById(memberId);
        if (ObjectUtil.isNull(member)) {
            return R.fail().message("用户不存在！");
        }
        MemberInfo memberInfo = BeanUtil.copyProperties(member, MemberInfo.class, "password");
        R allInfo = orderFeign.getOrderAndCourseBymemberId(memberId);
        return allInfo.data("member",memberInfo);
    }

    @CheckMemberLogin
    @PutMapping("/updateMemberInfo")
    public R updateMemberInfo(@RequestBody MemberInfo memberInfo) {
        if (ObjectUtil.isNull(memberInfo)) {
            return R.fail().message("更新信息失败！");
        }
        String id = memberInfo.getId();
        Integer age = memberInfo.getAge();
        String avatar = memberInfo.getAvatar();
        String nickName = memberInfo.getNickname();
        String password = memberInfo.getPassword();
        String sign = memberInfo.getSign();
        if (!StrUtil.isEmpty(id)) {
            Member member = memberService.getById(id);
            if (!StrUtil.isEmpty(password)) {
                member.setPassword(MD5.encrypt(password));
            } else if (!StrUtil.isEmpty(nickName) && !nickName.equals(member.getNickname())) {
                member.setNickname(nickName);
            } else if (!StrUtil.isEmpty(avatar) && !avatar.equals(member.getAvatar())) {
                member.setAvatar(avatar);
            } else if (!StrUtil.isEmpty(sign) && !sign.equals(member.getSign())) {
                member.setSign(sign);
            } else if (ObjectUtil.isNotNull(age) && !age.equals(member.getAge())) {
                member.setAge(age);
            }
            return R.returnR(memberService.updateById(member));
        }
        return R.fail().message("参数非法！");
    }

    @CheckMemberLogin
    @PostMapping("/changePhone")
    public R changePhone(@RequestBody ChangePhone changePhone) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String id = JwtUtil.getIdByJwtToken(request);
            if (!StringUtils.hasText(redisUtil.get(ConstantUtil.LOGIN_USER_KEY+id))) {

            }
        } catch (Exception e) {
            return R.fail().message("登录已过期！").code(50014);
        }
        String code = redisUtil.get(ConstantUtil.CHANGE_PHONE_CODE_KEY + changePhone.getOldMobile());
        redisUtil.delete(ConstantUtil.CHANGE_PHONE_CODE_KEY + changePhone.getOldMobile());
        if (!StringUtils.hasText(code)) {
            return R.fail().message("验证码已过期，请重新获取！");
        }
        if (!code.equals(changePhone.getCode())) {
            return R.fail().message("验证码不正确！");
        }
        String mobile = changePhone.getMobile();
        String memberId = changePhone.getId();
        return R.returnR(memberService.update(new UpdateWrapper<Member>().eq("id",memberId).set("mobile",mobile)));
    }

    @PostMapping("/all/{current}/{count}")
    public R getAllUser(@PathVariable("current")Integer current, @PathVariable("count") Integer count, @RequestBody MemberInfo memberInfo) {
        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        if (memberInfo!=null) {
            String mobile = memberInfo.getMobile();
            String nickName = memberInfo.getNickname();
            Integer sex = memberInfo.getSex();
            String sign = memberInfo.getSign();
            Integer age = memberInfo.getAge();
            String avatar = memberInfo.getAvatar();
            if (!ObjectUtils.isEmpty(mobile)) {
                wrapper.like("mobile",mobile);
            } else if (!ObjectUtils.isEmpty(nickName)) {
                wrapper.like("nickname",nickName);
            } else if (!ObjectUtils.isEmpty(sex)) {
                wrapper.like("sex",sex);
            } else if (!ObjectUtils.isEmpty(sign)) {
                wrapper.like("sign",sign);
            } else if (!ObjectUtils.isEmpty(age)) {
                wrapper.like("age",age);
            } else if (!ObjectUtils.isEmpty(avatar)) {
                wrapper.like("avatar",avatar);
            }
            wrapper.orderByDesc("gmt_create");
        }
        return pageList(current,count,wrapper);
    }

    @DeleteMapping("/{memberId}")
    public R delUser(@PathVariable("memberId") String memberId) {
        memberService.removeById(memberId);
        return R.ok();
    }

    @PostMapping("/add")
    public R addUser(@RequestBody MemberInfo memberInfo) {
        Member member = BeanUtil.copyProperties(memberInfo, Member.class);
        member.setPassword(memberInfo.getPassword());
        memberService.save(member);
        return R.ok();
    }

    @PostMapping("/update")
    public R updateUser(@RequestBody MemberInfo memberInfo) {
        Member member = BeanUtil.copyProperties(memberInfo, Member.class);
        member.setPassword(memberInfo.getPassword());
        memberService.updateById(member);
        return R.ok();
    }

    @PostMapping("/delBatch")
    public R delMembers(@RequestBody List<String> memberIds) {
        memberService.removeBatchByIds(memberIds);
        return R.ok();
    }

    private R pageList(Integer current, Integer count, QueryWrapper<Member> wrapper) {
        Page<Member> page = new Page<>(current,count);
        memberService.page(page,wrapper);
        List<Member> records = page.getRecords();
        List<MemberInfo> memberInfos = BeanUtil.copyToList(records, MemberInfo.class);
        if (ObjectUtil.isNotNull(memberInfos) && memberInfos.size() > 0) {
            memberInfos.forEach(memberInfo -> {
                memberInfo.setPassword(null);
            });
        }
        return R.ok().data("total",page.getTotal()).data("pages",page.getPages()).data("current",page.getCurrent()).data("items",memberInfos);
    }
}
