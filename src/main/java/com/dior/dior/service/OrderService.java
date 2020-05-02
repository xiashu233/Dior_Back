package com.dior.dior.service;

import com.dior.dior.bean.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface OrderService {

    OmsOrder addOrder(UmsMember oneUser, UmsMemberReceiveAddress userAddressByAddress, BigDecimal totalAmount);

    void addOrderItem(OmsOrder omsOrder, List<OmsCartItem> omsCartItemList);

    boolean ChangeOrderStatus(String out_trade_no, String memberId);

    List<OmsOrder> getAllOrderByMemberId(String memberId);

    List<OmsOrderItem> getAllOrderItemByOrderId(String orderId);

    Map<String, Object> checkAlipayPayment(String out_trad_no);
}
