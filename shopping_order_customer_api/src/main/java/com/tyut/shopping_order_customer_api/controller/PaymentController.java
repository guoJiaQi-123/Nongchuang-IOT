package com.tyut.shopping_order_customer_api.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tyut.shopping_common.pojo.Orders;
import com.tyut.shopping_common.pojo.Payment;
import com.tyut.shopping_common.result.BaseResult;
import com.tyut.shopping_common.service.OrdersService;
import com.tyut.shopping_common.service.ZfbPayService;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @version v1.0
 * @author OldGj 2024/6/26
 * @apiNote 支付相关控制器
 */
@RestController
@RequestMapping("/user/payment")
public class PaymentController {

    @DubboReference
    private OrdersService ordersService;
    @DubboReference(timeout = 120000, retries = 1) // 超时时间为120秒，因为需要调用支付宝的支付api返回支付二维码，所有耗时较长
    private ZfbPayService zfbPayService;


    /**
     * 支付宝支付，生成支付二维码
     * @param orderId
     * @return
     */
    @PostMapping("/pcPay")
    public BaseResult<String> pcPay(String orderId) {
        Orders orders = ordersService.findById(orderId);
        String qrcode = zfbPayService.pcPay(orders);
        return BaseResult.ok(qrcode);
    }

    /**
     * 该方法是用户扫码支付后支付宝调用的。支付后的回调方法
     * @return
     */
    @PostMapping("/success/notify")
    @GlobalTransactional // seata管理的分布式事务
    public BaseResult successNotify(HttpServletRequest request) {
        // 1.验签
        // 1.验签
        Map<String, String[]> parameterMap = request.getParameterMap();
        // 新Map
        Map<String, String[]> newMap = new HashMap<>(parameterMap);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("requestParameterMap", newMap);
        zfbPayService.checkSign(paramMap);


        String trade_status = request.getParameter("trade_status");// 订单状态
        String out_trade_no = request.getParameter("out_trade_no");// 订单编号


        // 如果支付成功
        if (trade_status.equals("TRADE_SUCCESS")) {
            // 2.修改订单状态
            Orders orders = ordersService.findById(out_trade_no);
            orders.setStatus(2); // 订单状态为已付款
            orders.setPaymentTime(new Date());
            orders.setPaymentType(2); // 支付宝支付
            ordersService.update(orders);

            // 3.添加交易记录
            Payment payment = new Payment();
            payment.setOrderId(out_trade_no); // 订单编号
            payment.setTransactionId(out_trade_no); // 交易号
            payment.setTradeType("支付宝支付"); // 交易类型
            payment.setTradeState(trade_status); // 交易状态
            payment.setPayerTotal(orders.getPayment()); // 付款数
            payment.setContent(JSON.toJSONString(request.getParameterMap())); // 支付详情
            payment.setCreateTime(new Date()); // 支付时间
            zfbPayService.addPayment(payment);
        }
        return BaseResult.ok();
    }
}
