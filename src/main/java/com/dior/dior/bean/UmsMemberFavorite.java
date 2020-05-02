package com.dior.dior.bean;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;

public class UmsMemberFavorite  implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String id;

    private String memberId;

    private String productId;

    @Transient
    private PmsProductInfo PmsProductInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
