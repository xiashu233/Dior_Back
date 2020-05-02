package com.dior.dior.dto;

import java.io.Serializable;

public class OrderSuccessInfo implements Serializable {

    private String email;
    private String orderSn;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }
}
