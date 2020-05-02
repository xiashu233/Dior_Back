package com.dior.dior.service;



import com.dior.dior.bean.PmsBaseCatalog1;
import com.dior.dior.bean.PmsBaseCatalog2;
import com.dior.dior.bean.PmsBaseCatalog3;
import com.dior.dior.bean.PmsProductInfo;

import java.util.List;

public interface CatalogService {
    List<PmsBaseCatalog1> getAllCatalogOne();

    List<PmsBaseCatalog2> getAllCatalogTwo(String id);

    List<PmsBaseCatalog3> getAllCatalogThree(String id);

    int addCatalog1(String catalog1txt);

    int addCatalog2(int catalog1, String catalog2txt);

    int addCatalog3(int catalog2, String catalog3txt);

    List<PmsBaseCatalog1> getAllCatalog1();

    List<PmsBaseCatalog3> getAllCatalog3ByCatalog3(String catalog3Id);

    List<PmsBaseCatalog2> getAllCatalog2ByCatalog3(String catalog3Id);

    List<PmsProductInfo> getAllProcByCatalog2(String catalog2Id);

    List<PmsBaseCatalog1> getMenu();

    PmsBaseCatalog1 getMenuByCatalog1(String catalog1);
}
