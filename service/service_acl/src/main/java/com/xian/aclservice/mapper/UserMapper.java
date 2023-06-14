package com.xian.aclservice.mapper;

import com.xian.aclservice.entities.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author xian
 * @since 2022/07/01 12:45
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
