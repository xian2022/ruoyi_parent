package com.xian.serviceorder.entities.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderVo {

    @TableField("order_no")
    private String orderNo;

    @ApiModelProperty("课程id")
    private String courseId;

    @ApiModelProperty("课程名称")
    private String courseTitle;

    @ApiModelProperty("课程封面")
    private String courseCover;

    @ApiModelProperty("讲师名称")
    private String teacherName;

    @ApiModelProperty("会员id")
    private String memberId;

    @ApiModelProperty("会员昵称")
    private String nickname;

    @ApiModelProperty("会员手机")
    private String mobile;

    @ApiModelProperty("订单金额（分）")
    private BigDecimal totalFee;

    @ApiModelProperty("支付类型（1：微信 2：支付宝）")
    private Integer payType;

    @ApiModelProperty("创建时间")
    @TableField(value = "gmt_create",fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(value = "gmt_modified",fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;
}
