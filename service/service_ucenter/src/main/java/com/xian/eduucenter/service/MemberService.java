package com.xian.eduucenter.service;

import com.xian.eduucenter.entities.vo.LoginVo;
import com.xian.eduucenter.entities.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xian.eduucenter.entities.vo.RegisterVo;
import com.xian.entities.R;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author xian
 * @since 2022/06/02 08:49
 */
public interface MemberService extends IService<Member> {
    R login(LoginVo loginVo);

    R register(RegisterVo registerVo);

    R getLoginInfo(HttpServletRequest request);

    R loginBySMS(LoginVo loginVo);
}
