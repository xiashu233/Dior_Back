package com.dior.dior.service;

import com.dior.dior.bean.OmsCartItem;

import java.util.List;

public interface CartService {
    List<OmsCartItem> getCartItemsByUserId(String memberId);

    OmsCartItem AddCartItemTotal(String id);

    OmsCartItem MinusCartItemTotal(String id);

    boolean delAddress(String cartItemId);

    OmsCartItem checkAddCart(String memberId, String productId);

    boolean AddCartItem(OmsCartItem cartItem);

    OmsCartItem getCartItemById(String s);

    void delCartItems(String[] cartItems);
}
