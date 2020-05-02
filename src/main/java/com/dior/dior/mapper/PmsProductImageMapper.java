package com.dior.dior.mapper;

import com.dior.dior.bean.PmsProductImage;
import tk.mybatis.mapper.common.Mapper;

public interface PmsProductImageMapper extends Mapper<PmsProductImage> {
    String selectDefaultImgUrl(String id);
}
