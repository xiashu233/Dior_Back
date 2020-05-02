package com.dior.dior.service.impl;


import com.alibaba.fastjson.JSON;
import com.dior.dior.bean.*;
import com.dior.dior.mapper.PmsProductImageMapper;
import com.dior.dior.mapper.PmsProductInfoMapper;
import com.dior.dior.mapper.PmsProductSaleAttrValueMapper;
import com.dior.dior.mapper.UmsMemberFavoriteMapper;
import com.dior.dior.service.ProcService;
import com.dior.dior.util.FileDataUtil;
import com.dior.dior.util.IntParseAttrName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class ProcServiceImpl implements ProcService {
    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;
    @Autowired
    PmsProductImageMapper pmsProductImageMapper;
    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;
    @Autowired
    FileDataUtil fileDataUtil;
    @Autowired
    UmsMemberFavoriteMapper favoriteMapper;


    @Override
    public String addProcInfo(PmsProductInfo pmsProductInfo) {
        pmsProductInfoMapper.insertSelective(pmsProductInfo);
        return pmsProductInfo.getId();
    }

    @Override
    public void addProcImage(String productId, String imgList,int defaultImgIndex) {
        String[] imgs = imgList.split(",");
        for (int i = 1; i < imgs.length; i++) {
            PmsProductImage pmsProductImage = new PmsProductImage();
            if (i == (defaultImgIndex + 1)){
                pmsProductImage.setIsDefault("1");
            }
            pmsProductImage.setProductId(productId);
            pmsProductImage.setImgUrl(imgs[i]);
            pmsProductImage.setImgName(imgs[i]);
            pmsProductImageMapper.insertSelective(pmsProductImage);
        }

    }

    @Override
    public void addProcAttr(String procId,String attrMapJson) {
       Map<String,String> attrMap = JSON.parseObject(attrMapJson, Map.class);

        for (String attrItemIndex : attrMap.keySet()) {
            String attrItemValue = attrMap.get(attrItemIndex);
            String[] attrItemValues = attrItemValue.split(",");
            for (int i = 1; i < attrItemValues.length; i++) {
                PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
                pmsProductSaleAttrValue.setProductId(procId);
                pmsProductSaleAttrValue.setSaleAttrId(attrItemIndex);
                pmsProductSaleAttrValue.setSaleAttrValueName(attrItemValues[i]);

                pmsProductSaleAttrValueMapper.insertSelective(pmsProductSaleAttrValue);
            }

        }
    }

    @Override
    public List<PmsProductInfo> getAllProcByCatalog3(String catalog3) {
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3);
        List<PmsProductInfo> pmsProductInfoList = pmsProductInfoMapper.select(pmsProductInfo);
        fileDataUtil.fillData(pmsProductInfoList);
        return pmsProductInfoList;
    }



    @Override
    public int deleteProc(String id) {
        int delCount = pmsProductInfoMapper.deleteByPrimaryKey(id);
        if (delCount > 0){
            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
            pmsProductSaleAttrValue.setProductId(id);
            pmsProductSaleAttrValueMapper.delete(pmsProductSaleAttrValue);

            PmsProductImage pmsProductImage = new PmsProductImage();
            pmsProductImage.setProductId(id);
            pmsProductImageMapper.delete(pmsProductImage);
        }
        return delCount;
    }

    @Override
    public PmsProductInfo getProcById(String id) {

        PmsProductInfo pmsProductInfo = pmsProductInfoMapper.selectByPrimaryKey(id);
            if (pmsProductInfo != null){
                PmsProductImage pmsProductImage = new PmsProductImage();
                pmsProductImage.setProductId(id);
                List<PmsProductImage> images = pmsProductImageMapper.select(pmsProductImage);
                pmsProductInfo.setSpuImageList(images);

                PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
                pmsProductSaleAttrValue.setProductId(id);

                List<PmsProductSaleAttrValue> valueList = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
                Map<String,List<PmsProductSaleAttrValue>> valueMap = new HashMap<>();
                for (PmsProductSaleAttrValue productSaleAttrValue : valueList) {
                    if (valueMap.get(IntParseAttrName.parse(productSaleAttrValue.getSaleAttrId())) != null){
                        valueMap.get(IntParseAttrName.parse(productSaleAttrValue.getSaleAttrId())).add(productSaleAttrValue);
                    }else{
                        List<PmsProductSaleAttrValue> values = new ArrayList<>();
                        values.add(productSaleAttrValue);
                        valueMap.put(IntParseAttrName.parse(productSaleAttrValue.getSaleAttrId()),values);
                    }
                }
                pmsProductInfo.setSpuSaleAttrValueList(valueMap);
            }

        return pmsProductInfo;
    }

    @Override
    public List<PmsProductInfo> getProcBySearchKey(String key) {
        List<PmsProductInfo> pmsProductInfoList = pmsProductInfoMapper.selectProcBySearchKey(key);

        fileDataUtil.fillDefault(pmsProductInfoList);
        return pmsProductInfoList;
    }

    @Override
    public void changeProcInfo(PmsProductInfo pmsProductInfo) {
        Example example = new Example(pmsProductInfo.getClass());
        example.createCriteria().andEqualTo("id",pmsProductInfo.getId());
        pmsProductInfoMapper.updateByExample(pmsProductInfo,example);

    }

    @Override
    public void deleteProcImageByProcId(String id) {
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(id);
        pmsProductImageMapper.delete(pmsProductImage);
    }

    @Override
    public void deleteProcAttrValueByProcId(String id) {
        PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
        pmsProductSaleAttrValue.setProductId(id);
        pmsProductSaleAttrValueMapper.delete(pmsProductSaleAttrValue);
    }

    @Override
    public List<PmsProductInfo> getProcListByCatalog3(String catalog3) {
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3);
        List<PmsProductInfo> pmsProductInfoList = pmsProductInfoMapper.select(pmsProductInfo);
        fillDefault(pmsProductInfoList);
        return pmsProductInfoList;
    }

    @Override
    public String getProcDefaultImgSrc(String productId) {
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(productId);
        pmsProductImage.setIsDefault("1");
        return pmsProductImageMapper.selectOne(pmsProductImage).getImgUrl();
    }

    @Override
    public List<UmsMemberFavorite> getFavoriteByMemberId(String memberId) {
        UmsMemberFavorite umsMemberFavorite = new UmsMemberFavorite();
        umsMemberFavorite.setMemberId(memberId);

        return favoriteMapper.select(umsMemberFavorite);
    }

    @Override
    public List<PmsProductInfo> getProcListByFavoriteList(List<UmsMemberFavorite> favoriteList) {
        List<PmsProductInfo> procList = new ArrayList<>();
        Iterator<UmsMemberFavorite> iterator = favoriteList.iterator();
        while (iterator.hasNext()){
            UmsMemberFavorite next = iterator.next();
            procList.add(getProcById(next.getProductId()));
        }
        fillDefaultImgUrl(procList);
        return procList;
    }

    @Override
    public UmsMemberFavorite checkFavorite(UmsMemberFavorite umsMemberFavorite) {
        return favoriteMapper.selectOne(umsMemberFavorite);
    }

    @Override
    public void addFavorite(UmsMemberFavorite umsMemberFavorite) {
        favoriteMapper.insertSelective(umsMemberFavorite);
    }

    @Override
    public boolean delFavorite(UmsMemberFavorite umsMemberFavorite) {
        return favoriteMapper.delete(umsMemberFavorite)>0;
    }



    public void fillData(List<PmsProductInfo> pmsProductInfoList) {
        for (PmsProductInfo productInfo : pmsProductInfoList) {
            PmsProductImage pmsProductImage = new PmsProductImage();
            pmsProductImage.setProductId(productInfo.getId());
            List<PmsProductImage> images = pmsProductImageMapper.select(pmsProductImage);
            productInfo.setSpuImageList(images);

        }
    }

    public void fillDefault(List<PmsProductInfo> pmsProductInfoList) {
        for (PmsProductInfo productInfo : pmsProductInfoList) {
            PmsProductImage pmsProductImage = new PmsProductImage();
            pmsProductImage.setProductId(productInfo.getId());
            pmsProductImage.setIsDefault("1");
            List<PmsProductImage> images = pmsProductImageMapper.select(pmsProductImage);
            productInfo.setSpuImageList(images);
        }
    }

    public void fillDefaultImgUrl(List<PmsProductInfo> pmsProductInfoList) {
        for (PmsProductInfo productInfo : pmsProductInfoList) {
            productInfo.setDefaultImgUrl(pmsProductImageMapper.selectDefaultImgUrl(productInfo.getId()));
        }
    }


}
