package com.xian.serviceorder.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xian.entities.R;
import com.xian.serviceorder.entities.PayLog;
import com.xian.serviceorder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author xian
 * @since 2022/06/26 23:32
 */
@CrossOrigin
@RestController
@RequestMapping("/serviceorder/paylog")
public class PayLogController {
    @Autowired
    private PayLogService payLogService;
    @PostMapping("/{current}/{count}")
    public R getPayLog(@PathVariable(value = "current")Integer current, @PathVariable(value = "count",required = false)Integer count, @RequestBody(required = false) PayLog paylog) {
        QueryWrapper<PayLog> payLogQueryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(paylog)) {
            String transactionId = paylog.getTransactionId();
            String orderNo = paylog.getOrderNo();
            Integer payType = paylog.getPayType();
            if (StringUtils.hasText(transactionId)) {
                payLogQueryWrapper.eq("transaction_id",transactionId);
            } else if (StringUtils.hasText(orderNo)) {
                payLogQueryWrapper.eq("order_no",orderNo);
            } else if (ObjectUtil.isNotNull(payType)) {
                payLogQueryWrapper.eq("pay_type",payType);
            }
        }
        payLogQueryWrapper.orderByDesc("gmt_create");
        return pageList(current,count,payLogQueryWrapper);
    }

    @DeleteMapping("/{payLogId}")
    public R delOrder(@PathVariable("payLogId")String payLogId) {
        return R.returnR(payLogService.removeById(payLogId));
    }

    private R pageList(Integer current, Integer count, QueryWrapper<PayLog> wrapper) {
        Page<PayLog> page = new Page<>(current,count);
        payLogService.page(page,wrapper);
        return R.ok().data("total",page.getTotal()).data("pages",page.getPages()).data("current",page.getCurrent()).data("list",page.getRecords());
    }
}
