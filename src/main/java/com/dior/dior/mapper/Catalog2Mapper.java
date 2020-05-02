package com.dior.dior.mapper;


import com.dior.dior.bean.PmsBaseCatalog2;
import com.dior.dior.bean.PmsProductInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface Catalog2Mapper extends Mapper<PmsBaseCatalog2> {
    List<PmsBaseCatalog2> getAllCatalog2ByCatalog3(String catalog3Id);

    List<PmsProductInfo> selectAllProcByCatalog2(String catalog2Id);

    List<PmsBaseCatalog2> selectAllCatalog2ByCatalog1(String id);
}
