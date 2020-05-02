package com.dior.dior.mapper;

import com.dior.dior.bean.PmsBaseCatalog3;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface Catalog3Mapper extends Mapper<PmsBaseCatalog3> {

    List<PmsBaseCatalog3> selectAllCatalog3ByCatalog3(String catalog3);

    List<PmsBaseCatalog3> selectAllCatalog3ByCatalog2(String id);
}
