package com.xian.serviceorder.controller.front;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xian.annotation.CheckMemberLogin;
import com.xian.serviceorder.feign.EduFeign;
import com.xian.serviceorder.rabbitmq.utils.BindingUtil;
import com.xian.serviceorder.rabbitmq.utils.MQConstant;
import com.xian.entities.R;
import com.xian.utils.RedisUtil;
import com.xian.serviceorder.entities.Order;
import com.xian.serviceorder.entities.PayLog;
import com.xian.serviceorder.entities.vo.OrderVo;
import com.xian.serviceorder.service.OrderService;
import com.xian.serviceorder.service.PayLogService;
import com.xian.serviceorder.utils.AliPayConstantUtil;
import com.xian.utils.ConstantUtil;
import com.xian.serviceorder.utils.OrderUtil;
import com.xian.serviceorder.websocket.WebSocket;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author xian
 * @since 2022/06/25 11:53
 */
@CrossOrigin
@RestController
@RequestMapping("/serviceorder/front/order")
public class OrderFrontController {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AliPayConstantUtil aliPayConstantUtil;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PayLogService payLogService;
    @Autowired
    private WebSocket webSocket;
    @Resource
    private EduFeign eduFeign;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private BindingUtil bindingUtil;

    @CheckMemberLogin
    @PostMapping("/createOrder")
    public R createOrder(@RequestBody OrderVo orderVo) {
        if (
                !StringUtils.hasText(orderVo.getCourseId()) && !StringUtils.hasText(orderVo.getCourseTitle()) &&
                !StringUtils.hasText(orderVo.getCourseCover()) && !StringUtils.hasText(orderVo.getTeacherName()) &&
                !StringUtils.hasText(orderVo.getMemberId()) && !StringUtils.hasText(orderVo.getNickname()) &&
                !StringUtils.hasText(orderVo.getMobile()) && !ObjectUtil.isNull(orderVo.getTotalFee()) && !ObjectUtil.isNull(orderVo.getPayType())
        ) {
            return R.fail().message("提交信息不完整！");
        }
        String memberId = orderVo.getMemberId();
        String courseId = orderVo.getCourseId();
        Order order = BeanUtil.copyProperties(orderVo, Order.class);
        String orderId = OrderUtil.createId();
        order.setOrderNo(orderId);
        order.setStatus(0);
        String key = courseId+":"+memberId;
        Boolean success = redisUtil.setNx(ConstantUtil.ORDER_ID + orderId, ConstantUtil.ORDER_KEY + key, 30L, TimeUnit.MINUTES);
        if (!success) {
            return R.fail().message("请勿重复下单！").data("orderNo", orderId);
        }
        orderService.save(order);
        String jsonStr = JSONUtil.toJsonStr(order);
        bindingUtil.createOrderQueueAndBinding(orderId);
        amqpTemplate.convertAndSend(
                MQConstant.ORDER_EXCHANGE, //发送至订单交换机
                MQConstant.ROUTING_KEY_ORDER+orderId, //订单定routingKey
                orderId //订单号   这里可以传对象 比如直接传订单对象
                , message -> {
                    message.getMessageProperties().setExpiration(1000 * 60 * 30 + "");
                    return message;
                }
        );
        redisUtil.setNx(ConstantUtil.ORDER_KEY + key, jsonStr, 30L, TimeUnit.MINUTES);
        return R.ok().data("orderNo", orderId);
    }

    @CheckMemberLogin
    @GetMapping("/getInfoByOrderNo/{OrderNo}")
    public R getInfoByOrderNo(@PathVariable("OrderNo") String orderNo) {
        if (!StringUtils.hasText(orderNo)) {
            return R.fail().message("提交信息不完整！");
        }
        return checkOrder(orderNo);
    }

    @GetMapping("/getIsBuyCourse/{memberId}/{courseId}")
    public R getIsBuyCourse(@PathVariable("memberId") String memberId,@PathVariable("courseId") String courseId) {
        QueryWrapper<Order> orderWrapper = new QueryWrapper<>();
        orderWrapper.eq("course_id",courseId);
        orderWrapper.eq("member_id",memberId);
        orderWrapper.eq("status",1);
        Order one = orderService.getOne(orderWrapper);
        return ObjectUtil.isNotNull(one)?R.ok():R.fail();
    }

    public R alipay(BigDecimal price,String orderId) throws AlipayApiException {
        //调用封装好的方法（给支付宝接口发送请求）
        return sendRequestToAlipay(orderId,price,"若毅学堂");
    }
    public R wechat(BigDecimal price,String orderId) throws AlipayApiException {
        //调用封装好的方法（给支付宝接口发送请求）
        return sendRequestToAlipay(orderId,price,"若毅学堂");
    }
    @CheckMemberLogin
    @GetMapping("/pay/{type}/{orderNo}")
    public R pay(@PathVariable("type")Integer type,@PathVariable("orderNo")String orderNo) throws AlipayApiException {
        R r = checkOrder(orderNo);
        if (!r.getSuccess()) {
            return r;
        }
        Order order = (Order) r.getData().get("jsonStr");
        BigDecimal fee = order.getTotalFee();
        return type == 1 ? wechat(fee,orderNo):alipay(fee,orderNo);
    }

    /*
    参数1：订单号
    参数2：订单金额
    参数3：订单名称
     */
    //支付宝官方提供的接口
    private R sendRequestToAlipay(String orderId,BigDecimal totalAmount,String subject) throws AlipayApiException {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = aliPayConstantUtil.getAlipayClient();
        //设置请求参数
        AlipayTradePagePayRequest  alipayRequest = aliPayConstantUtil.setBizContent(orderId,totalAmount,subject);
        //请求
        AlipayTradePagePayResponse response = alipayClient.pageExecute(alipayRequest);
        return response.isSuccess()?R.ok().data("form",response.getBody()):R.fail().message("创建支付失败！请稍后再试！");
    }

    private R checkOrder(String orderNo) {
        String orderInfoKey = redisUtil.get(ConstantUtil.ORDER_ID+orderNo);
        Order order = orderService.getOne(new QueryWrapper<Order>().eq("order_no",orderNo));
        if (!StringUtils.hasText(orderInfoKey)) {
            if (order.getStatus() == 0) {
                orderService.update(new UpdateWrapper<Order>().eq("order_no",orderNo).set("status",-2));
                return R.fail().message("订单已过期！").data("jsonStr",order);
            }
        } else {
            String jsonStr = redisUtil.get(orderInfoKey);
            if (!StringUtils.hasText(jsonStr)) {
                redisUtil.delete(ConstantUtil.ORDER_ID+orderNo);
                if (order.getStatus() == 0) {
                    orderService.update(new UpdateWrapper<Order>().eq("order_no",orderNo).set("status",-2));
                    return R.fail().message("订单已过期！").data("jsonStr",order);
                }
            } else {
                return R.ok().data("jsonStr",order).data("ttl",redisUtil.ttl(orderInfoKey));
            }
        }
        return R.ok().data("jsonStr",order);
    }

    @GetMapping("/returnUrl")
    @ResponseBody
    public String returnUrlMethod(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, AlipayApiException {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            params.put(name, valueStr);
        }
        // 商户订单号
        String orderNo = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        amqpTemplate.receive(MQConstant.ORDER_QUEUE+orderNo);
        // 支付宝交易流水号
        String trade_no = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        BigDecimal money = BigDecimal.valueOf(Double.parseDouble(new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8)));
        String orderKey = redisUtil.get(ConstantUtil.ORDER_ID + orderNo);
        redisUtil.delete(ConstantUtil.ORDER_ID + orderNo);
        String orderJson = redisUtil.get(orderKey);
        redisUtil.delete(orderKey);
        Order order = JSONUtil.toBean(orderJson, Order.class);
        redisUtil.delete("order:id:"+order.getOrderNo());
        order.setPayType(2);
        PayLog payLog = new PayLog();
        payLog.setOrderNo(order.getOrderNo());
        payLog.setPayTime(new Date());
        payLog.setPayType(2);
        payLog.setAttr(JSONUtil.toJsonStr(params));
        payLog.setTransactionId(trade_no);
        order.setStatus(0);
        payLog.setTradeState("FILED");
        payLog.setTotalFee(money);
        // System.out.println(params);//查看参数都有哪些
        //验证签名（支付宝公钥）
        try {
            boolean signVerified = AlipaySignature.rsaCheckV1(params, AliPayConstantUtil.ALIPAY_PUBLIC_KEY, AliPayConstantUtil.CHARSET, AliPayConstantUtil.SIGN_TYPE); // 调用SDK验证签名
            //验证签名通过
            if(signVerified){
                // 付款金额
                order.setStatus(1);
                payLog.setTradeState("SUCCESS");
                redisUtil.zIncrementScore(ConstantUtil.COURSE_BUY_KEY, order.getCourseId(), 1);
                webSocket.sendMessageToClient(orderNo,R.ok().message("支付成功!").data("商户订单号",orderNo));
            } else {
                webSocket.sendMessageToClient(orderNo,R.fail().message("支付失败!").data("失败!",20005));
            }
        }
        finally {
            orderService.updateById(order);
            payLogService.save(payLog);
        }
        return "<script>window.close();</script>";
    }
    @CheckMemberLogin
    @GetMapping("/getOrderBymemberId/{memberId}/{current}/{count}")
    public R getOrderBymemberId(@PathVariable("memberId") String memberId, @PathVariable("current") Integer current, @PathVariable("count") Integer count) {
        QueryWrapper<Order> orderWrapper = new QueryWrapper<>();
        orderWrapper.eq("member_id", memberId);
        Page<Order> page = new Page<>(current,count);
        orderService.page(page,orderWrapper);
        return R.ok().data("order",page.getRecords()).data("total",page.getTotal()).data("pages",page.getPages()).data("current",page.getCurrent());
    }
    @CheckMemberLogin
    @GetMapping("/getOrderBymemberId/{memberId}")
    public R getOrderBymemberId(@PathVariable("memberId") String memberId) {
        QueryWrapper<Order> orderWrapper = new QueryWrapper<>();
        orderWrapper.eq("member_id", memberId);
        List<Order> orderList = getOrderList(memberId, orderWrapper);
        return R.ok().data("order",orderList);
    }
    @CheckMemberLogin
    @GetMapping("/getOrderAndCourseBymemberId/{memberId}")
    public R getOrderAndCourseBymemberId(@PathVariable("memberId") String memberId) {
        R courseList = getCourseBymemberId(memberId);
        System.out.println(courseList);
        R order = getOrderBymemberId(memberId);
        order.data("courseInfo",courseList.getData().get("courseInfo"));
        return order;
    }
    @CheckMemberLogin
    @GetMapping("/getCourseBymemberId/{memberId}")
    public R getCourseBymemberId(@PathVariable("memberId") String memberId) {
        QueryWrapper<Order> orderWrapper = new QueryWrapper<>();
        orderWrapper.eq("status", 1);
        orderWrapper.eq("member_id", memberId);
        List<Order> orders = getOrderList(memberId,orderWrapper);
        if (orders.size() == 0) {
            return R.ok().data("courseInfo","");
        }
        List<String> courseIdList = orders.stream().map(Order::getCourseId).collect(Collectors.toList());
        return eduFeign.getCourseInfoByBatchId(courseIdList);
    }

    @CheckMemberLogin
    @GetMapping("/status/{orderNo}")
    public void getOrderStatus(@PathVariable("orderNo") String orderNo) throws AlipayApiException {
            if (StringUtils.hasText(orderNo)) {
                AlipayClient alipayClient = aliPayConstantUtil.getAlipayClient();
                AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
                JSONObject bizContent = new JSONObject();
                bizContent.put("out_trade_no", orderNo);
                request.setBizContent(bizContent.toString());
                AlipayTradeQueryResponse response = alipayClient.execute(request);
                String orderKey = redisUtil.get(ConstantUtil.ORDER_ID + orderNo);
                if (!StrUtil.isEmpty(orderKey) && response.isSuccess()) {
                    String tradeNo = response.getTradeNo();
                    redisUtil.delete(ConstantUtil.ORDER_ID + orderNo);
                    String orderJson = redisUtil.get(orderKey);
                    redisUtil.delete(orderKey);
                    Order order = JSONUtil.toBean(orderJson, Order.class);
                    order.setPayType(2);
                    PayLog payLog = new PayLog();
                    payLog.setOrderNo(order.getOrderNo());
                    payLog.setPayTime(new Date());
                    payLog.setPayType(2);
                    payLog.setTransactionId(tradeNo);
                    order.setStatus(1);
                    payLog.setTradeState("SUCCESS");
                    redisUtil.zIncrementScore(ConstantUtil.COURSE_BUY_KEY, order.getCourseId(), 1);
                    orderService.updateById(order);
                    payLogService.save(payLog);
                    webSocket.sendMessageToClient(orderNo, R.ok().code(200).message("支付成功!").data("商户订单号", orderNo));
                } else if (StringUtils.hasText(orderKey)) {
                    webSocket.sendMessageToClient(orderNo, R.ok().code(400).message("订单暂未支付!").data("商户订单号", orderNo));
                } else {
                    Order order = orderService.getOne(new QueryWrapper<Order>().eq("order_no", orderNo));
                    if (ObjectUtil.isNotNull(order.getStatus()) && order.getStatus() == 1) {
                        webSocket.sendMessageToClient(orderNo, R.ok().code(200).message("支付成功!").data("商户订单号", orderNo));
                    } else {
                        webSocket.sendMessageToClient(orderNo, R.fail().code(500).message("订单已失效!").data("商户订单号", orderNo));
                    }
                }
            } else {
                throw new NullPointerException("参数非法！订单号不能为空！");
            }
    }

    private List<Order> getOrderList(String memberId,QueryWrapper<Order> orderWrapper) {
        return orderService.list(orderWrapper);
    }
}
