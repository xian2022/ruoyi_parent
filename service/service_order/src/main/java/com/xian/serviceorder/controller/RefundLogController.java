package com.xian.serviceorder.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xian.entities.R;
import com.xian.serviceorder.entities.RefundLog;
import com.xian.serviceorder.service.RefundLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xian
 * @since 2022/06/28 20:05
 */
@CrossOrigin
@RestController
@RequestMapping("/serviceorder/refundlog")
public class RefundLogController {
    @Autowired
    private RefundLogService refundLogService;
    @PostMapping("/{current}/{count}")
    public R getRefundLog(@PathVariable(value = "current")Integer current, @PathVariable(value = "count",required = false)Integer count, @RequestBody(required = false) RefundLog refundlog) {
        QueryWrapper<RefundLog> refundLogQueryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(refundlog)) {
            String transactionId = refundlog.getTransactionId();
            String orderNo = refundlog.getOrderNo();
            Integer refundType = refundlog.getRefundType();
            if (StringUtils.hasText(transactionId)) {
                refundLogQueryWrapper.eq("transaction_id",transactionId);
            } else if (StringUtils.hasText(orderNo)) {
                refundLogQueryWrapper.eq("order_no",orderNo);
            } else if (ObjectUtil.isNotNull(refundType)) {
                refundLogQueryWrapper.eq("refund_type",refundType);
            }
        }
        refundLogQueryWrapper.orderByDesc("gmt_create");
        return pageList(current,count,refundLogQueryWrapper);
    }

    @DeleteMapping("/{refundLogId}")
    public R delOrder(@PathVariable("refundLogId")String refundLogId) {
        return R.returnR(refundLogService.removeById(refundLogId));
    }

    private R pageList(Integer current, Integer count, QueryWrapper<RefundLog> wrapper) {
        Page<RefundLog> page = new Page<>(current,count);
        refundLogService.page(page,wrapper);
        return R.ok().data("total",page.getTotal()).data("pages",page.getPages()).data("current",page.getCurrent()).data("list",page.getRecords());
    }

}
