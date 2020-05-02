package com.dior.dior.controller;

import com.alibaba.fastjson.JSON;
import com.dior.dior.bean.*;
import com.dior.dior.response.ResponseResultVo;
import com.dior.dior.response.ResultVoStatus;
import com.dior.dior.service.CatalogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("catalog")
public class CatalogController {
    @Autowired
    CatalogService catalogService;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("addCatalog")
    public ResponseResultVo addCatalog(@RequestParam("catalog1")int catalog1, @RequestParam("catalog2")int catalog2,
                                       @RequestParam("catalog1txt")String catalog1txt, @RequestParam("catalog2txt")String catalog2txt,
                                       @RequestParam("catalog3txt")String catalog3txt){

        if (catalog1 == -1 && !StringUtils.isEmpty(catalog1txt)){
            catalog1 = catalogService.addCatalog1(catalog1txt);
        }
        if (catalog2 == -1 && catalog1 > 0 && !StringUtils.isEmpty(catalog2txt)){
            catalog2 = catalogService.addCatalog2(catalog1,catalog2txt);
        }
        int catalog3 = 0;
        if (catalog2 > 0){
            catalog3 = catalogService.addCatalog3(catalog2,catalog3txt);
        }
        if (catalog3 != -1 && !StringUtils.isEmpty(catalog3txt)){
            return ResponseResultVo.success();
        }
        return ResponseResultVo.faild(ResultVoStatus.Error);
    }

    @PostMapping("getAllCatalog1")
    public ResponseResultVo getAllCatalog1(){
        List<PmsBaseCatalog1> pmsBaseCatalog1s = catalogService.getAllCatalog1();
        if (pmsBaseCatalog1s.size() > 0){
            return ResponseResultVo.success(pmsBaseCatalog1s);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }

    @PostMapping("getcatalog2/{id}")
    public ResponseResultVo getcatalog2(@PathVariable("id") String id){
        List<PmsBaseCatalog2> catalog2List = catalogService.getAllCatalogTwo(id);
        if (catalog2List.size() > 0){
            return ResponseResultVo.success(catalog2List);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }

    @PostMapping("getcatalog3/{id}")
    public ResponseResultVo getcatalog3(@PathVariable("id") String id){
        List<PmsBaseCatalog3> catalog3List = catalogService.getAllCatalogThree(id);
        if (catalog3List.size() > 0){
            return ResponseResultVo.success(catalog3List);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }

    @PostMapping("getAllCatalog3ByCatalog3/{catalog3Id}")
    public ResponseResultVo getAllCatalog3ByCatalog3(@PathVariable("catalog3Id") String catalog3Id){
        List<PmsBaseCatalog3> catalog3List = catalogService.getAllCatalog3ByCatalog3(catalog3Id);
        if (catalog3List.size() > 0){
            return ResponseResultVo.success(catalog3List);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }

    @PostMapping("getAllProcByCatalog2/{catalog2Id}")
    public ResponseResultVo getAllProcByCatalog2(@PathVariable("catalog2Id") String catalog2Id){
        List<PmsProductInfo> pmsProductInfos = catalogService.getAllProcByCatalog2(catalog2Id);
        if (pmsProductInfos.size() > 0){
            return ResponseResultVo.success(pmsProductInfos);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }

    @PostMapping("getAllCatalog2ByCatalog3/{catalog3Id}")
    public ResponseResultVo getAllCatalog2ByCatalog3(@PathVariable("catalog3Id") String catalog3Id){
        List<PmsBaseCatalog2> catalog2List = catalogService.getAllCatalog2ByCatalog3(catalog3Id);
        if (catalog2List.size() > 0){
            return ResponseResultVo.success(catalog2List);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }

    @PostMapping("getMenu")
    public ResponseResultVo getMenu(){
        Object allMenu = redisTemplate.boundValueOps("AllMenu").get();
        List<PmsBaseCatalog1> pmsBaseCatalog1List = new ArrayList<PmsBaseCatalog1>();
        if (allMenu == null){
            pmsBaseCatalog1List = catalogService.getMenu();
            redisTemplate.boundValueOps("AllMenu").set(JSON.toJSON(pmsBaseCatalog1List).toString());
        }else{
            pmsBaseCatalog1List = JSON.parseObject(allMenu.toString(),pmsBaseCatalog1List.getClass());
        }

        return ResponseResultVo.success(pmsBaseCatalog1List);
    }

    @PostMapping("getMenuByCatalog1/{catalog1}")
    public ResponseResultVo getMenuByCatalog1(@PathVariable("catalog1")String catalog1){
        Object menuItem = redisTemplate.boundValueOps("Menu:" + catalog1 + ":info").get();
        PmsBaseCatalog1 pmsBaseCatalog1 = new PmsBaseCatalog1();
        if (menuItem == null){
            pmsBaseCatalog1 = catalogService.getMenuByCatalog1(catalog1);
            redisTemplate.boundValueOps("Menu:" + catalog1 + ":info").set(JSON.toJSON(pmsBaseCatalog1).toString());
        }else{
            pmsBaseCatalog1 = JSON.parseObject(menuItem.toString(),pmsBaseCatalog1.getClass());
        }
        if (pmsBaseCatalog1 != null){
            return ResponseResultVo.success(pmsBaseCatalog1);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);

    }

}
