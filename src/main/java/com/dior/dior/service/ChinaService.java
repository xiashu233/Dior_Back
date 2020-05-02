package com.dior.dior.service;

import com.dior.dior.bean.China;

import java.util.List;

public interface ChinaService {
    List<China> getAllChinaCityByPid(String pid);

    List<China> getAllCityByProvinceName(String provinceName);

    List<China> getAllRegionByCityName(String cityName);
}
