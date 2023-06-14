package com.xian.serviceorder.entities.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RefundVo {
    @ApiModelProperty("订单号")
    @TableField("order_no")
    private String orderNo;

    @ApiModelProperty("退款金额（分）")
    @TableField("total_fee")
    private BigDecimal totalFee;

    @ApiModelProperty("退款流水号")
    @TableField("transaction_id")
    private String transactionId;
}
