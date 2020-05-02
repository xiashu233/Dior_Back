package com.dior.dior.bean;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @param
 * @return
 */

public class PmsProductInfo implements Serializable {

    @Id
    @GeneratedValue(generator="JDBC")
    private String id;

    @Column
    private String productName;
    @Column
    private String description;
    @Column
    private String catalog3Id;
    @Column
    private Double productPrice;
    @Column
    private String productStatus;
    @Column
    private String productTag;

    @Transient
    private Map<String,List<PmsProductSaleAttrValue>> spuSaleAttrValueList;
    @Transient
    private List<PmsProductImage> spuImageList;
    @Transient
    private String defaultImgUrl;
    @Transient
    private boolean isUserFavorite;


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Map<String, List<PmsProductSaleAttrValue>> getSpuSaleAttrValueList() {
        return spuSaleAttrValueList;
    }

    public void setSpuSaleAttrValueList(Map<String, List<PmsProductSaleAttrValue>> spuSaleAttrValueList) {
        this.spuSaleAttrValueList = spuSaleAttrValueList;
    }

    public List<PmsProductImage> getSpuImageList() {
        return spuImageList;
    }

    public void setSpuImageList(List<PmsProductImage> spuImageList) {
        this.spuImageList = spuImageList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCatalog3Id() {
        return catalog3Id;
    }

    public void setCatalog3Id(String catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public String getProductTag() {
        return productTag;
    }

    public void setProductTag(String productTag) {
        this.productTag = productTag;
    }

    public String getDefaultImgUrl() {
        return defaultImgUrl;
    }

    public void setDefaultImgUrl(String defaultImgUrl) {
        this.defaultImgUrl = defaultImgUrl;
    }

    public boolean isUserFavorite() {
        return isUserFavorite;
    }

    public void setUserFavorite(boolean userFavorite) {
        isUserFavorite = userFavorite;
    }
}


