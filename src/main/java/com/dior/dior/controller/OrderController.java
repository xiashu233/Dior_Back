package com.dior.dior.controller;



import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.dior.dior.bean.*;
import com.dior.dior.config.AlipayConfig;
import com.dior.dior.dto.OrderSuccessInfo;
import com.dior.dior.dto.PayingInfo;
import com.dior.dior.exception.HdException;
import com.dior.dior.mq.RabbitSenderService;
import com.dior.dior.response.ResponseResultVo;
import com.dior.dior.response.ResultVoStatus;
import com.dior.dior.service.*;
import com.dior.dior.util.MailUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@CrossOrigin
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    @Autowired
    ProcService procService;
    @Autowired
    CartService cartService;
    @Autowired
    UserAddressService userAddressService;

    @Autowired
    AlipayClient alipayClient;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RabbitSenderService senderService;

    @PostMapping("addOrder")
    @ResponseBody
    public ResponseResultVo addOrder(@RequestParam("dataList")String dataList,
                                     @RequestParam("memberId")int memberId,
                                     @RequestParam("addressId")String addressId){

        String[] cartItems = dataList.split(",");
        List<OmsCartItem> omsCartItemList = new ArrayList<>();
        BigDecimal totalAmount = new BigDecimal(0);
        for (int i = 0; i < cartItems.length; i++) {
            omsCartItemList.add(cartService.getCartItemById(cartItems[i]));
            totalAmount = totalAmount.add(omsCartItemList.get(i).getTotalPrice());
        }



        UmsMember oneUser = userService.getOneUser(memberId);
        UmsMemberReceiveAddress userAddressByAddress = userAddressService.getUserAddressByAddressId(addressId);

        OmsOrder omsOrder = orderService.addOrder(oneUser,userAddressByAddress,totalAmount);
        String orderId = omsOrder.getId();
        if (StringUtils.isNotBlank(orderId)){


            try{
                orderService.addOrderItem(omsOrder,omsCartItemList);

                cartService.delCartItems(cartItems);
            }catch (Exception e){
                throw new HdException(ResultVoStatus.DelCartItemException);
            }

            String mail = userService.getUserMailByMeberId(memberId);

            // 发送消息
            OrderSuccessInfo orderInfo = new OrderSuccessInfo();
            orderInfo.setEmail(mail);
            orderInfo.setOrderSn(omsOrder.getOrderSn());
            senderService.sendOrderSuccessEmailMsg(orderInfo);
            return ResponseResultVo.success(omsOrder.getOrderSn());
        }


        return ResponseResultVo.faild(ResultVoStatus.Error);

    }

    @RequestMapping("pay/{memberId}/{orderSn}")
    @ResponseBody
    public ResponseResultVo aliPayCallbackReturn(@PathVariable("memberId")int memberId,@PathVariable("orderSn")String orderSn){

        // 获得一个支付宝请求的客户端(它并不是一个链接，而是一个封装好的http的表单请求)
        String form = null;
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request

        // 回调函数
        alipayRequest.setReturnUrl(AlipayConfig.return_payment_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_payment_url);
        String out_trade_no = orderSn + "-" + memberId;
        Map<String,Object> map = new HashMap<>();
        map.put("out_trade_no",out_trade_no);
        map.put("product_code","FAST_INSTANT_TRADE_PAY");
        map.put("total_amount",0.01);
        map.put("subject","您甄选的商品，送给您最好的！");

        String param = JSON.toJSONString(map);

        alipayRequest.setBizContent(param);

        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单

            StringBuilder stringBuilder = new StringBuilder();
            String[] split = form.split("\n");
            for (int i = 0; i < split.length - 1; i++) {
                stringBuilder.append(split[i]);
                stringBuilder.append("\n");
            }
            form = stringBuilder.toString();

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        PayingInfo payingInfo = new PayingInfo();
        payingInfo.setCount(30);
        payingInfo.setMemberId(String.valueOf(memberId));
        payingInfo.setOut_trad_no(out_trade_no);
        senderService.sendPayingOrderExpireMsg(payingInfo);

        return ResponseResultVo.success(form);

    }



    @RequestMapping("callback/notify")
    public String aliPayCallbacknotifys(HttpServletRequest request){

        String sign = request.getParameter("sign");
        String trade_no = request.getParameter("trade_no");

        String out_trade_noAndMemberId = request.getParameter("out_trade_no");
        String out_trade_no = out_trade_noAndMemberId.split("-")[0];
        String memberId = out_trade_noAndMemberId.split("-")[1];

        String trade_status = request.getParameter("trade_status");
        String total_amount = request.getParameter("total_amount");
        String subject = request.getParameter("subject");
        String callback_content = request.getQueryString();



        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setSubject(subject);
        paymentInfo.setOrderSn(out_trade_no);
        paymentInfo.setPaymentStatus("已支付");
        // 支付宝的交易凭证号
        paymentInfo.setAlipayTradeNo(trade_no);
        // 回调请求字符串
        paymentInfo.setCallbackContent(callback_content);
        paymentInfo.setCallbackTime(new Date());

        // 更新用户的支付状态
        // 进行支付更新的幂等性检查
        boolean res = orderService.ChangeOrderStatus(out_trade_no,memberId);


        return "redirect:http://localhost:7584/User/myOrder.aspx";
    }


    @PostMapping("getAllOrderByMemberId/{memberId}")
    @ResponseBody
    public ResponseResultVo getAllOrderByMemberId(@PathVariable("memberId")String memberId){
        List<OmsOrder> omsOrders = orderService.getAllOrderByMemberId(memberId);
        if (omsOrders.size() > 0){
            return ResponseResultVo.success(omsOrders);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }

    @PostMapping("getAllOrderItemByOrderId/{orderId}")
    @ResponseBody
    public ResponseResultVo getAllOrderItemByOrderId(@PathVariable("orderId")String orderId){
        List<OmsOrderItem> omsOrderItems = orderService.getAllOrderItemByOrderId(orderId);
        if (omsOrderItems.size() > 0){
            return ResponseResultVo.success(omsOrderItems);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }

}
