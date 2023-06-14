package com.xian.aclservice.mapper;

import com.xian.aclservice.entities.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 权限 Mapper 接口
 * </p>
 *
 * @author xian
 * @since 2022/07/01 12:45
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    List<String> getPermissionIdsByRoleIdList(@Param("roleIds") List<String> roleIds);

    void deletePermissionByRoleIds(@Param("roleIds") List<String> roleIds);
}
