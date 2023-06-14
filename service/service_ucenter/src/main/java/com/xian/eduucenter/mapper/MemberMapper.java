package com.xian.eduucenter.mapper;

import com.xian.eduucenter.entities.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author xian
 * @since 2022/06/02 08:49
 */
@Mapper
public interface MemberMapper extends BaseMapper<Member> {

    Integer countRegister(@Param("day") String day);
}
