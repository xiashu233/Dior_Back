package com.dior.dior.controller;

import com.dior.dior.bean.CommonResult;
import com.dior.dior.bean.PmsProductAds;
import com.dior.dior.response.ResponseResultVo;
import com.dior.dior.response.ResultVoStatus;
import com.dior.dior.service.AdsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequestMapping("ads")
@RestController
public class AdsController {
    @Autowired
    AdsService adsService;

    @PostMapping("/saveBanner")
    public ResponseResultVo saveBanner(PmsProductAds pmsProductAds){
        String adsId = "";
        if (StringUtils.isNotBlank(pmsProductAds.getId())){
           int changeSize = adsService.changeBanner(pmsProductAds);
           if (changeSize > 0){
               return ResponseResultVo.success(pmsProductAds);
           }
            return ResponseResultVo.faild(ResultVoStatus.Error);
        }else{
            adsId = adsService.saveBanner(pmsProductAds);
            if (StringUtils.isNotBlank(adsId)){
                return ResponseResultVo.success(pmsProductAds);
            }
            return ResponseResultVo.faild(ResultVoStatus.Error);
        }


    }

    @PostMapping("/delBanner/{id}")
    public ResponseResultVo delBanner(@PathVariable("id")String id){
       if (adsService.delBannerById(id)>0){
           return ResponseResultVo.success();
       }
        return ResponseResultVo.faild(ResultVoStatus.DelError);
    }

    @PostMapping("/getAllBanner/0")
    public ResponseResultVo getAllBanner(){
        List<PmsProductAds> pmsProductAds = adsService.getAllBannerByGW();
        if (pmsProductAds.size() > 0){
            return ResponseResultVo.success(pmsProductAds);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetDataException);
    }
}
