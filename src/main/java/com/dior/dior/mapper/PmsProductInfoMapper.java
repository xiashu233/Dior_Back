package com.dior.dior.mapper;

import com.dior.dior.bean.PmsProductInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsProductInfoMapper extends Mapper<PmsProductInfo> {
    List<PmsProductInfo> selectProcBySearchKey(String key);
}
