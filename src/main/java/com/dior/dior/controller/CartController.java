package com.dior.dior.controller;

import com.dior.dior.bean.OmsCartItem;
import com.dior.dior.bean.PmsProductInfo;
import com.dior.dior.exception.HdException;
import com.dior.dior.response.ResponseResultVo;
import com.dior.dior.response.ResultVoStatus;
import com.dior.dior.service.CartService;
import com.dior.dior.service.ProcService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;
    @Autowired
    ProcService procService;

    @PostMapping("getCartItemsByUserId/{memberId}")
    public ResponseResultVo getCartItemsByUserId(@PathVariable("memberId")String memberId){
        List<OmsCartItem> omsCartItemList = cartService.getCartItemsByUserId(memberId);
        if (omsCartItemList.size() > 0){
            return ResponseResultVo.success(omsCartItemList);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetDataException);
    }

    @PostMapping("AddCartItem")
    public ResponseResultVo AddCartItem(OmsCartItem cartItem){
        String memberId = cartItem.getMemberId();
        String productId = cartItem.getProductId();
        OmsCartItem omsCartItem = null;
        try{
            omsCartItem = cartService.checkAddCart(memberId,productId);
        }catch (Exception e){
            throw new HdException(ResultVoStatus.GetDataException);
        }


        if (omsCartItem != null){
            OmsCartItem omsCartItem1 = cartService.AddCartItemTotal(omsCartItem.getId());
            return ResponseResultVo.success(omsCartItem1);
        }else{
            PmsProductInfo procById = procService.getProcById(cartItem.getProductId());
            cartItem.setPrice(new BigDecimal(procById.getProductPrice()));
            cartItem.setTotalPrice(cartItem.getPrice());
            cartItem.setProductName(procById.getProductName());
            cartItem.setCreateDate(new Date());
            String procDefaultImgSrc =  procService.getProcDefaultImgSrc(cartItem.getProductId());
            cartItem.setProductPic(procDefaultImgSrc);
            cartItem.setQuantity(new BigDecimal(1));
            cartItem.setProductCatalog3Id(procById.getCatalog3Id());
            boolean result = cartService.AddCartItem(cartItem);
            if (result){
                return ResponseResultVo.success(cartItem);
            }
        }

        return ResponseResultVo.faild(ResultVoStatus.Error);

    }

    @PostMapping("AddCartItemTotal/{id}")
    public ResponseResultVo AddCartItemTotal(@PathVariable("id")String id){
        OmsCartItem updCartItem = cartService.AddCartItemTotal(id);
        if (StringUtils.isNotBlank(updCartItem.getId())){
            return ResponseResultVo.success(updCartItem);
        }
        return ResponseResultVo.faild(ResultVoStatus.Error);
    }

    @PostMapping("MinusCartItemTotal/{id}")
    public ResponseResultVo MinusCartItemTotal(@PathVariable("id")String id){
        OmsCartItem updCartItem = null;
        try{
            updCartItem = cartService.MinusCartItemTotal(id);
        }catch (Exception e){
            throw new HdException(ResultVoStatus.OPCartItemException);
        }
        if (updCartItem != null){
            return ResponseResultVo.success(updCartItem);
        }
        return ResponseResultVo.faild(ResultVoStatus.ChangeError);
    }

    @PostMapping("delCartItem/{cartItemId}")
    public ResponseResultVo delAddress(@PathVariable("cartItemId")String cartItemId){
        boolean result = false;
        try{
            result = cartService.delAddress(cartItemId);
        }catch (Exception e){
            throw new HdException(ResultVoStatus.DelCartItemException);
        }

        if (result){
            return ResponseResultVo.success(result);
        }
        return ResponseResultVo.faild(ResultVoStatus.DelError);
    }

}
