package com.xian.eduucenter.entities.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MemberInfo {
    @ApiModelProperty("会员id")
    private String id;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("性别 1 女，2 男")
    private Integer sex;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("用户签名")
    private String sign;

    @ApiModelProperty("是否禁用 1（true）已禁用，  0（false）未禁用")
    private Integer isDisabled;
}
