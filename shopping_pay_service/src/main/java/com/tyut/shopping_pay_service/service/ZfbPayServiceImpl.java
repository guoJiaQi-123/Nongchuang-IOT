package com.tyut.shopping_pay_service.service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayOpenPublicTemplateMessageIndustryModifyRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.tyut.shopping_common.pojo.Orders;
import com.tyut.shopping_common.pojo.Payment;
import com.tyut.shopping_common.result.BusException;
import com.tyut.shopping_common.result.CodeEnum;
import com.tyut.shopping_common.service.ZfbPayService;
import com.tyut.shopping_pay_service.ZfbPayConfig;
import com.tyut.shopping_pay_service.mapper.PaymentMapper;
import com.tyut.shopping_pay_service.util.ZfbVerifierUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;


import java.util.Map;

/**
 * @version v1.0
 * @author OldGj 2024/6/26
 * @apiNote 支付宝支付业务层实现类
 */
@DubboService
public class ZfbPayServiceImpl implements ZfbPayService {


    @Autowired
    private AlipayClient alipayClient;
    @Autowired
    private ZfbPayConfig zfbPayConfig;
    @Autowired
    private PaymentMapper paymentMapper;

    /**
     * 生成二维码
     * @param orders 订单对象
     * @return 二维码字符串
     */
    @Override
    public String pcPay(Orders orders) {
        /**
         * 判断订单状态，未支付才会生成二维码
         */
        if (orders.getStatus() != 1){
            throw new BusException(CodeEnum.ORDER_STATUS_ERROR);
        }

        // 1.创建请求对象
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        // 2.设置请求内容  2.1 设置回调地址：http://kalista.natapp1.cc/user/payment/success/notify
        request.setNotifyUrl(zfbPayConfig.getNotifyUrl() + zfbPayConfig.getPcNotify());
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orders.getId()); // 订单编号
        bizContent.put("total_amount", orders.getPayment()); // 订单金额
        bizContent.put("subject", orders.getCartGoods().get(0).getGoodsName()); //订单标题
        request.setBizContent(bizContent.toJSONString());
        try {
            // 3.发送请求
            AlipayTradePrecreateResponse response = alipayClient.execute(request);
            // 4.返回二维码
            return response.getQrCode();
        } catch (AlipayApiException e) {
            throw new BusException(CodeEnum.QR_CODE_ERROR);
        }
    }

    @Override
    public void checkSign(Map<String, Object> paramMap) {
        // 获取所有参数
        Map<String, String[]> requestParameterMap = (Map<String, String[]>) paramMap.get("requestParameterMap");
        // 验签
        boolean valid = ZfbVerifierUtils.isValid(requestParameterMap, zfbPayConfig.getPublicKey());
        // 验签失败，抛出异常
        if (!valid) {
            throw new BusException(CodeEnum.CHECK_SIGN_ERROR);
        }
    }


    @Override
    public void addPayment(Payment payment) {
        paymentMapper.insert(payment);
    }
}
