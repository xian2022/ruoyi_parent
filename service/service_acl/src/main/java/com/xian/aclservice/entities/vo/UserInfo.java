package com.xian.aclservice.entities.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserInfo {
    @ApiModelProperty("用户id")
    private String id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户名")
    private String newPassWord;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("用户角色")
    private List<RoleVo> roles;

    @ApiModelProperty("角色Id")
    private List<String> roleIds;
}
