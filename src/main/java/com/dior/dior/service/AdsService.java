package com.dior.dior.service;


import com.dior.dior.bean.PmsProductAds;

import java.util.List;

public interface AdsService {
    String saveBanner(PmsProductAds pmsProductAds);

    List<PmsProductAds> getAllCatalog1Ads();

    int changeBanner(PmsProductAds pmsProductAds);

    int delBannerById(String id);

    List<PmsProductAds> getAllBannerByGW();
}
