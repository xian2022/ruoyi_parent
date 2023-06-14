package com.xian.serviceorder.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xian.entities.R;
import com.xian.serviceorder.entities.Order;
import com.xian.serviceorder.entities.PayLog;
import com.xian.serviceorder.entities.RefundLog;
import com.xian.serviceorder.entities.vo.OrderVo;
import com.xian.serviceorder.entities.vo.RefundVo;
import com.xian.serviceorder.service.OrderService;
import com.xian.serviceorder.service.PayLogService;
import com.xian.serviceorder.service.RefundLogService;
import com.xian.serviceorder.utils.AliPayConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

@CrossOrigin
@RestController
@RequestMapping("/serviceorder/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private PayLogService payLogService;
    @Autowired
    private RefundLogService refundLogService;
    @Autowired
    private AliPayConstantUtil aliPayConstantUtil;
    @PostMapping("/{current}/{count}")
    public R getOrder(@PathVariable(value = "current")Integer current, @PathVariable(value = "count",required = false)Integer count, @RequestBody(required = false) OrderVo orderVo) {
        QueryWrapper<Order> orderWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(orderVo)) {
            String courseTitle = orderVo.getCourseTitle();
            String nickname = orderVo.getNickname();
            String orderNo = orderVo.getOrderNo();
            Integer payType = orderVo.getPayType();
            String mobile = orderVo.getMobile();
            if (StringUtils.hasText(courseTitle)) {
                orderWrapper.like("course_title",courseTitle);
            } else if (StringUtils.hasText(nickname)) {
                orderWrapper.like("nickname",nickname);
            } else if (StringUtils.hasText(orderNo)) {
                orderWrapper.eq("order_no",orderNo);
            } else if (ObjectUtil.isNotNull(payType)) {
                orderWrapper.eq("pay_type",payType);
            } else if (StringUtils.hasText(mobile)) {
                orderWrapper.like("mobile",mobile);
            }
        }
        orderWrapper.orderByDesc("gmt_create");
        return pageList(current,count,orderWrapper);
    }

    @DeleteMapping("/{orderId}")
    @Transactional
    public R delOrder(@PathVariable("orderId")String orderId) {
        long count = payLogService.count(new QueryWrapper<PayLog>().eq("order_no", orderId));
        if (count>0) {
            return R.returnR(orderService.remove(new QueryWrapper<Order>().eq("order_no",orderId))&&payLogService.remove(new QueryWrapper<PayLog>().eq("order_no",orderId)));
        }
        return R.returnR(orderService.remove(new QueryWrapper<Order>().eq("order_no",orderId)));
    }

    @PostMapping("/refund")
    public R refund(@RequestBody RefundVo refundVo) {
        AlipayClient alipayClient = aliPayConstantUtil.getAlipayClient();
        String orderNo = refundVo.getOrderNo();
        QueryWrapper<PayLog> payLogQueryWrapper = new QueryWrapper<PayLog>()
                .eq("order_no",orderNo);
        PayLog payLog = payLogService.getOne(payLogQueryWrapper);
        String transactionId = payLog.getTransactionId();
        BigDecimal totalFee = refundVo.getTotalFee();
        AlipayTradeRefundRequest req = aliPayConstantUtil.refundSetBizContent(transactionId, totalFee, orderNo);
        // 订单支付时传入的商户订单号,不能和 trade_no同时为空
        // String trade_no = "4674334545";
        // 需要退款的金额
        // String refund_amount = "199.00";
        // 表示支持部分退款
        // String out_request_no = "HZ01RF001";
        AlipayTradeRefundResponse resp = null;
        try {
            resp = alipayClient.execute(req);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (resp.isSuccess()) {
            UpdateWrapper<Order> orderUpdateWrapper = new UpdateWrapper<Order>()
                    .eq("order_no", orderNo)
                    .set("status", -1);
            payLog.setTradeState("REFUND");
            RefundLog refundLog = new RefundLog();
            refundLog.setRefundTime(new Date());
            refundLog.setOrderNo(orderNo);
            refundLog.setTotalFee(totalFee);
            refundLog.setTransactionId(transactionId);
            refundLog.setTradeState(payLog.getTradeState());
            refundLog.setRefundType(payLog.getPayType());
            refundLog.setAttr("退款成功！");
            orderService.update(orderUpdateWrapper);
            payLogService.removeById(payLog);
            refundLogService.save(refundLog);
            return R.ok().message("退款成功！");
        } else {
            return R.fail().message("退款失败！");
        }
    }

    private R pageList(Integer current, Integer count, QueryWrapper<Order> wrapper) {
        Page<Order> page = new Page<>(current,count);
        orderService.page(page,wrapper);
        return R.ok().data("total",page.getTotal()).data("pages",page.getPages()).data("current",page.getCurrent()).data("list",page.getRecords());
    }
}
