package com.dior.dior.service.impl;


import com.dior.dior.bean.PmsBaseCatalog1;
import com.dior.dior.bean.PmsBaseCatalog2;
import com.dior.dior.bean.PmsBaseCatalog3;
import com.dior.dior.bean.PmsProductInfo;
import com.dior.dior.mapper.Catalog1Mapper;
import com.dior.dior.mapper.Catalog2Mapper;
import com.dior.dior.mapper.Catalog3Mapper;
import com.dior.dior.service.CatalogService;
import com.dior.dior.util.FileDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    Catalog1Mapper catalog1Mapper;
    @Autowired
    Catalog2Mapper catalog2Mapper;
    @Autowired
    Catalog3Mapper catalog3Mapper;
    @Autowired
    FileDataUtil fileDataUtil;

    @Override
    public List<PmsBaseCatalog1> getAllCatalogOne() {
        List<PmsBaseCatalog1> pmsBaseCatalog1s = catalog1Mapper.selectAll();
        return pmsBaseCatalog1s;
    }

    @Override
    public List<PmsBaseCatalog2> getAllCatalogTwo(String id) {
        PmsBaseCatalog2 pmsBaseCatalog2 = new PmsBaseCatalog2();
        pmsBaseCatalog2.setCatalog1Id(id);
        List<PmsBaseCatalog2> catalog2List = catalog2Mapper.select(pmsBaseCatalog2);
        return catalog2List;
    }

    @Override
    public List<PmsBaseCatalog3> getAllCatalogThree(String id) {
        PmsBaseCatalog3 pmsBaseCatalog3 = new PmsBaseCatalog3();
        pmsBaseCatalog3.setCatalog2Id(id);
        List<PmsBaseCatalog3> catalog3List = catalog3Mapper.select(pmsBaseCatalog3);
        return catalog3List;
    }

    @Override
    public int addCatalog1(String catalog1txt) {
        PmsBaseCatalog1 pmsBaseCatalog1 = new PmsBaseCatalog1();
        pmsBaseCatalog1.setName(catalog1txt);
        return catalog1Mapper.insertSelective(pmsBaseCatalog1);
    }

    @Override
    public int addCatalog2(int catalog1, String catalog2txt) {
        PmsBaseCatalog2 pmsBaseCatalog2Ts = new PmsBaseCatalog2();
        pmsBaseCatalog2Ts.setCatalog1Id(catalog1 + "");
        pmsBaseCatalog2Ts.setName(catalog2txt);
        if (catalog2Mapper.select(pmsBaseCatalog2Ts).size() > 0){
            return -1;
        }

        PmsBaseCatalog2 pmsBaseCatalog2 = new PmsBaseCatalog2();
        pmsBaseCatalog2.setCatalog1Id(catalog1 + "");
        pmsBaseCatalog2.setName(catalog2txt);
        return catalog2Mapper.insertSelective(pmsBaseCatalog2);
    }

    @Override
    public int addCatalog3(int catalog2, String catalog3txt) {
        PmsBaseCatalog3 pmsBaseCatalog3Ts = new PmsBaseCatalog3();
        pmsBaseCatalog3Ts.setCatalog2Id(catalog2 + "");
        pmsBaseCatalog3Ts.setName(catalog3txt);
        if (catalog3Mapper.select(pmsBaseCatalog3Ts).size() > 0){
            return -1;
        }

        PmsBaseCatalog3 pmsBaseCatalog3 = new PmsBaseCatalog3();
        pmsBaseCatalog3.setCatalog2Id(catalog2 + "");
        pmsBaseCatalog3.setName(catalog3txt);
        return catalog3Mapper.insertSelective(pmsBaseCatalog3);
    }

    @Override
    public List<PmsBaseCatalog1> getAllCatalog1() {
        return catalog1Mapper.selectAll();
    }

    @Override
    public List<PmsBaseCatalog3> getAllCatalog3ByCatalog3(String catalog3Id) {
        return catalog3Mapper.selectAllCatalog3ByCatalog3(catalog3Id);
    }

    @Override
    public List<PmsBaseCatalog2> getAllCatalog2ByCatalog3(String catalog3Id) {
        return catalog2Mapper.getAllCatalog2ByCatalog3(catalog3Id);
    }

    @Override
    public List<PmsProductInfo> getAllProcByCatalog2(String catalog2Id) {
        List<PmsProductInfo> pmsProductInfos = catalog2Mapper.selectAllProcByCatalog2(catalog2Id);
        fileDataUtil.fillDefault(pmsProductInfos);
        return pmsProductInfos;
    }

    @Override
    public List<PmsBaseCatalog1> getMenu() {
        List<PmsBaseCatalog1> pmsBaseCatalog1s = catalog1Mapper.selectAll();
        for (PmsBaseCatalog1 pmsBaseCatalog1 : pmsBaseCatalog1s) {
            List<PmsBaseCatalog2> pmsBaseCatalog2List = catalog2Mapper.selectAllCatalog2ByCatalog1(pmsBaseCatalog1.getId());
            pmsBaseCatalog1.setCatalog2s(pmsBaseCatalog2List);
            for (PmsBaseCatalog2 pmsBaseCatalog2 : pmsBaseCatalog2List) {
                pmsBaseCatalog2.setCatalog3List(catalog3Mapper.selectAllCatalog3ByCatalog2(pmsBaseCatalog2.getId()));
            }
        }
        return pmsBaseCatalog1s;
    }

    @Override
    public PmsBaseCatalog1 getMenuByCatalog1(String catalog1) {
        PmsBaseCatalog1 pmsBaseCatalog1 = catalog1Mapper.selectByPrimaryKey(catalog1);
        List<PmsBaseCatalog2> pmsBaseCatalog2List = catalog2Mapper.selectAllCatalog2ByCatalog1(pmsBaseCatalog1.getId());
        pmsBaseCatalog1.setCatalog2s(pmsBaseCatalog2List);
        for (PmsBaseCatalog2 pmsBaseCatalog2 : pmsBaseCatalog2List) {
            pmsBaseCatalog2.setCatalog3List(catalog3Mapper.selectAllCatalog3ByCatalog2(pmsBaseCatalog2.getId()));
        }
        return pmsBaseCatalog1;
    }
}
