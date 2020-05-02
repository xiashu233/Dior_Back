package com.dior.dior.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.dior.dior.bean.*;
import com.dior.dior.mapper.OmsOrderItemMapper;
import com.dior.dior.mapper.OmsOrderMapper;
import com.dior.dior.service.OrderService;
import com.dior.dior.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private SnowFlake snowFlake=new SnowFlake(2,3);

    @Autowired
    OmsOrderMapper orderMapper;
    @Autowired
    OmsOrderItemMapper orderItemMapper;
    @Autowired
    AlipayClient alipayClient;

    @Override
    public OmsOrder addOrder(UmsMember user, UmsMemberReceiveAddress address, BigDecimal totalAmount) {
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setMemberId(user.getId());
        omsOrder.setOrderSn(String.valueOf(snowFlake.nextId()));
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        omsOrder.setCreateTime(sdf.format(new Date()));
        omsOrder.setMemberUsername(user.getUsername());
        omsOrder.setTotalAmount(totalAmount);
        omsOrder.setPayType("0");
        omsOrder.setSourceType(0);
        omsOrder.setStatus("0");
        omsOrder.setOrderType(0);
        omsOrder.setDeliveryCompany("迪奥专送");

        omsOrder.setReceiverName(address.getName());
        omsOrder.setReceiverPhone(address.getPhoneNumber());
        omsOrder.setReceiverPostCode(address.getPostCode());
        omsOrder.setReceiverProvince(address.getProvince());
        omsOrder.setReceiverCity(address.getCity());
        omsOrder.setReceiverRegion(address.getRegion());
        omsOrder.setReceiverDetailAddress(address.getDetailAddress());
        orderMapper.insert(omsOrder);
        return omsOrder;
    }

    @Override
    public void addOrderItem(OmsOrder omsOrder, List<OmsCartItem> omsCartItemList) {
        OmsOrderItem omsOrderItem = new OmsOrderItem();
        for (OmsCartItem omsCartItem : omsCartItemList) {
            omsOrderItem.setId(null);

            omsOrderItem.setOrderId(omsOrder.getId());
            omsOrderItem.setOrderSn(omsOrder.getOrderSn());

            omsOrderItem.setProductId(omsCartItem.getProductId());
            omsOrderItem.setProductPic(omsCartItem.getProductPic());
            omsOrderItem.setProductName(omsCartItem.getProductName());
            omsOrderItem.setProductPrice(omsCartItem.getPrice().toString());
            omsOrderItem.setProductQuantity(omsCartItem.getQuantity());
            omsOrderItem.setProductCatalog3Id(omsCartItem.getProductCatalog3Id());
            omsOrderItem.setSp1(omsCartItem.getSp1());
            omsOrderItem.setSp2(omsCartItem.getSp2());
            omsOrderItem.setSp3(omsCartItem.getSp3());
            omsOrderItem.setProductAttr(omsCartItem.getProductAttr());
            omsOrderItem.setTotalPrice(omsCartItem.getTotalPrice());

            orderItemMapper.insertSelective(omsOrderItem);


        }
    }

    @Override
    public boolean ChangeOrderStatus(String out_trade_no, String memberId) {
        int num = orderMapper.updateOrderStatus(out_trade_no,memberId,new Date());
        return num > 0;
    }

    @Override
    public List<OmsOrder> getAllOrderByMemberId(String memberId) {
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setMemberId(memberId);
        return orderMapper.select(omsOrder);
    }

    @Override
    public List<OmsOrderItem> getAllOrderItemByOrderId(String orderId) {
        OmsOrderItem omsOrderItem = new OmsOrderItem();
        omsOrderItem.setOrderId(orderId);
        return orderItemMapper.select(omsOrderItem);
    }

    @Override
    public Map<String, Object> checkAlipayPayment(String out_trad_no) {
        // 创建返回值Map
        Map<String,Object> resultMap = new HashMap<>();

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("out_trade_no",out_trad_no);
        request.setBizContent(JSON.toJSONString(requestMap));
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response.isSuccess()){
            System.out.println("已创建交易，调用成功");
            resultMap.put("out_trade_no",response.getOutTradeNo());
            resultMap.put("trade_no",response.getTradeNo());
            resultMap.put("buyer_logon_id",response.getBuyerLogonId());
            resultMap.put("trade_status",response.getTradeStatus());
            resultMap.put("call_back_content",response.getMsg());
        }else{
            System.out.println("有可能交易未创建，调用失败");
        }

        return resultMap;
    }
}
