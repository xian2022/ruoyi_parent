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
 * 
 * </p>
 *
 * @author xian
 * @since 2022/06/28 20:05
 */
@Getter
@Setter
@TableName("t_refund_log")
@ApiModel(value = "RefundLog对象", description = "")
public class RefundLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    @ApiModelProperty("订单号")
    @TableField("order_no")
    private String orderNo;

    @ApiModelProperty("退款完成时间")
    @TableField("refund_time")
    private Date refundTime;

    @ApiModelProperty("退款金额（分）")
    @TableField("total_fee")
    private BigDecimal totalFee;

    @ApiModelProperty("退款流水号")
    @TableField("transaction_id")
    private String transactionId;

    @ApiModelProperty("退款状态")
    @TableField("trade_state")
    private String tradeState;

    @ApiModelProperty("退款方式（1：微信 2：支付宝）")
    @TableField("refund_type")
    private Integer refundType;

    @ApiModelProperty("其他属性")
    @TableField("attr")
    private String attr;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic("0")
    private Integer isDeleted;

    @ApiModelProperty("创建时间")
    @TableField(value = "gmt_create",fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty("修改时间")
    @TableField(value = "gmt_modified",fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
