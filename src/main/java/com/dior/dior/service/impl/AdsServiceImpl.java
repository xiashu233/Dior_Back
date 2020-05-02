package com.dior.dior.service.impl;


import com.dior.dior.bean.PmsProductAds;
import com.dior.dior.mapper.PmsProductAdsMapper;
import com.dior.dior.service.AdsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AdsServiceImpl implements AdsService {

    @Autowired
    PmsProductAdsMapper pmsProductAdsMapper;

    @Override
    public String saveBanner(PmsProductAds pmsProductAds) {
        if (pmsProductAds.getId().equals("-1")){
            pmsProductAds.setId(null);
        }
        pmsProductAdsMapper.insertSelective(pmsProductAds);
        return pmsProductAds.getId();
    }

    @Override
    public List<PmsProductAds> getAllCatalog1Ads() {
        PmsProductAds pmsProductAds = new PmsProductAds();
        pmsProductAds.setCatalog1Id("0");
        return pmsProductAdsMapper.select(pmsProductAds);
    }

    @Override
    public int changeBanner(PmsProductAds pmsProductAds) {
        Example example = new Example(PmsProductAds.class);
        example.createCriteria().andEqualTo("id",pmsProductAds.getId());

        return pmsProductAdsMapper.updateByExample(pmsProductAds,example);
    }

    @Override
    public int delBannerById(String id) {
        return pmsProductAdsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<PmsProductAds> getAllBannerByGW() {
        PmsProductAds pmsProductAds = new PmsProductAds();
        pmsProductAds.setCatalog1Id("0");
        return pmsProductAdsMapper.select(pmsProductAds);
    }
}
