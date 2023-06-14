package com.xian.aclservice.mapper;

import com.xian.aclservice.entities.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xian
 * @since 2022/07/01 12:45
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    List<String> getRoleIdByUserId(@Param("userId") String userId);

    List<String> getPermissionIds(@Param("roleId") String roleId);
}
