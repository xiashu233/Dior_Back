package com.dior.dior.util;

import com.dior.dior.bean.PmsProductImage;
import com.dior.dior.bean.PmsProductInfo;
import com.dior.dior.mapper.PmsProductImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileDataUtil {

    @Autowired
    PmsProductImageMapper pmsProductImageMapper;

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
