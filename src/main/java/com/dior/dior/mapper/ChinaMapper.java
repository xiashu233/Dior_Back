package com.dior.dior.mapper;

import com.dior.dior.bean.China;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ChinaMapper extends Mapper<China> {
    List<China> selectAllCityByProvinceName(String provinceName);

    List<China> selectAllRegionByCityName(String cityName);
}
