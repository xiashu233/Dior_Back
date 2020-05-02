package com.dior.dior.service;

import com.dior.dior.bean.PmsBaseCatalog1;
import com.dior.dior.bean.PmsProductInfo;
import com.dior.dior.bean.UmsMemberFavorite;

import java.util.List;

public interface ProcService {
    String addProcInfo(PmsProductInfo pmsProductInfo);

    void addProcImage(String id, String imgList, int defaultImgIndex);

    void addProcAttr(String procId, String attrMapJson);

    List<PmsProductInfo> getAllProcByCatalog3(String catalog3);

    int deleteProc(String id);

    PmsProductInfo getProcById(String id);

    List<PmsProductInfo> getProcBySearchKey(String key);

    void changeProcInfo(PmsProductInfo pmsProductInfo);

    void deleteProcImageByProcId(String id);

    void deleteProcAttrValueByProcId(String id);

    List<PmsProductInfo> getProcListByCatalog3(String catalog3);

    String getProcDefaultImgSrc(String productId);

    List<UmsMemberFavorite> getFavoriteByMemberId(String memberId);

    List<PmsProductInfo> getProcListByFavoriteList(List<UmsMemberFavorite> favoriteList);

    UmsMemberFavorite checkFavorite(UmsMemberFavorite umsMemberFavorite);

    void addFavorite(UmsMemberFavorite umsMemberFavorite);

    boolean delFavorite(UmsMemberFavorite umsMemberFavorite);

}
