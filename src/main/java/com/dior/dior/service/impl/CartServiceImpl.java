package com.dior.dior.service.impl;

import com.dior.dior.bean.OmsCartItem;
import com.dior.dior.bean.UmsMember;
import com.dior.dior.mapper.OmsCartItemMapper;
import com.dior.dior.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    OmsCartItemMapper omsCartItemMapper;

    @Override
    public List<OmsCartItem> getCartItemsByUserId(String memberId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);

        return omsCartItemMapper.select(omsCartItem);
    }

    @Override
    public OmsCartItem AddCartItemTotal(String id) {
        Example example = new Example(OmsCartItem.class);
        example.createCriteria().andEqualTo("id",id);
        OmsCartItem omsCartItem = omsCartItemMapper.selectByPrimaryKey(id);
        omsCartItem.setQuantity(omsCartItem.getQuantity().add(new BigDecimal(1)));
        omsCartItem.setTotalPrice(omsCartItem.getQuantity().multiply(omsCartItem.getPrice()));
        omsCartItemMapper.updateByExample(omsCartItem,example);
        return omsCartItem;
    }

    @Override
    public OmsCartItem MinusCartItemTotal(String id) {

        OmsCartItem omsCartItem = omsCartItemMapper.selectByPrimaryKey(id);
        if (omsCartItem.getQuantity().compareTo(new BigDecimal(1)) <= 0){
            return null;
        }

        Example example = new Example(OmsCartItem.class);
        example.createCriteria().andEqualTo("id",id);
        omsCartItem.setQuantity(omsCartItem.getQuantity().subtract(new BigDecimal(1)));
        omsCartItem.setTotalPrice(omsCartItem.getQuantity().multiply(omsCartItem.getPrice()));
        omsCartItemMapper.updateByExample(omsCartItem,example);
        return omsCartItem;
    }

    @Override
    public boolean delAddress(String cartItemId) {
        return omsCartItemMapper.deleteByPrimaryKey(cartItemId)>0;
    }

    @Override
    public OmsCartItem checkAddCart(String memberId, String productId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductId(productId);
        return omsCartItemMapper.selectOne(omsCartItem);
    }

    @Override
    public boolean AddCartItem(OmsCartItem cartItem) {

        return omsCartItemMapper.insertSelective(cartItem)>0;
    }

    @Override
    public OmsCartItem getCartItemById(String id) {
        return omsCartItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public void delCartItems(String[] cartItems) {
        for (int i = 0; i < cartItems.length; i++) {
            omsCartItemMapper.deleteByPrimaryKey(cartItems[i]);
        }
    }
}
