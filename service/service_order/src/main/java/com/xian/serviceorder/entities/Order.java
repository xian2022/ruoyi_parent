package com.xian.serviceorder.entities;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 订单
 * </p>
 *
 * @author xian
 * @since 2022/06/25 11:53
 */
@Getter
@Setter
@TableName("t_order")
@ApiModel(value = "Order对象", description = "订单")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("订单号")
    @TableField("order_no")
    private String orderNo;

    @ApiModelProperty("课程id")
    @TableField("course_id")
    private String courseId;

    @ApiModelProperty("课程名称")
    @TableField("course_title")
    private String courseTitle;

    @ApiModelProperty("课程封面")
    @TableField("course_cover")
    private String courseCover;

    @ApiModelProperty("讲师名称")
    @TableField("teacher_name")
    private String teacherName;

    @ApiModelProperty("会员id")
    @TableField("member_id")
    private String memberId;

    @ApiModelProperty("会员昵称")
    @TableField("nickname")
    private String nickname;

    @ApiModelProperty("会员手机")
    @TableField("mobile")
    private String mobile;

    @ApiModelProperty("订单金额（分）")
    @TableField("total_fee")
    private BigDecimal totalFee;

    @ApiModelProperty("支付类型（1：微信 2：支付宝）")
    @TableField("pay_type")
    private Integer payType;

    @ApiModelProperty("订单状态（0：未支付 1：已支付）")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic("0")
    private Integer isDeleted;

    @ApiModelProperty("创建时间")
    @TableField(value = "gmt_create",fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(value = "gmt_modified",fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
