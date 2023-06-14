package com.xian.aclservice.service;

import com.xian.aclservice.entities.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author xian
 * @since 2022/07/01 12:45
 */
public interface UserService extends IService<User> {

    void delUserAndRole(String userId);

    void delUserAndRoles(List<String> userIds);
}
