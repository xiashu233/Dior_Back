package com.dior.dior.service.impl;

import com.dior.dior.bean.China;
import com.dior.dior.mapper.ChinaMapper;
import com.dior.dior.service.ChinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChinaServiceImpl implements ChinaService {

    @Autowired
    ChinaMapper chinaMapper;


    @Override
    public List<China> getAllChinaCityByPid(String pid) {
        China china = new China();
        china.setPid(pid);
        return chinaMapper.select(china);
    }

    @Override
    public List<China> getAllCityByProvinceName(String provinceName) {
        return chinaMapper.selectAllCityByProvinceName(provinceName);
    }

    @Override
    public List<China> getAllRegionByCityName(String cityName) {
        return chinaMapper.selectAllRegionByCityName(cityName);
    }
}
