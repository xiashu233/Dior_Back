package com.dior.dior.controller;

import com.dior.dior.bean.China;
import com.dior.dior.response.ResponseResultVo;
import com.dior.dior.response.ResultVoStatus;
import com.dior.dior.service.ChinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/china")
public class ChinaController {
    @Autowired
    ChinaService chinaService;

    @PostMapping("getAllChinaCityByPid/{pid}")
    public ResponseResultVo getAllChinaCityByPid(@PathVariable("pid")String pid){
        List<China> chinaList = chinaService.getAllChinaCityByPid(pid);
        if (chinaList.size() > 0){
            return ResponseResultVo.success(chinaList);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }

    @PostMapping("getAllProvince")
    public ResponseResultVo getAllProvince(){
        return getAllChinaCityByPid("0");
    }

    @PostMapping("getAllCityByProvinceName/{provinceName}")
    public ResponseResultVo getAllCityByProvinceName(@PathVariable String provinceName){
        List<China> chinaList = chinaService.getAllCityByProvinceName(provinceName);
        if (chinaList.size() > 0){
            return ResponseResultVo.success(chinaList);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }

    @PostMapping("getAllRegionByCityName/{CityName}")
    public ResponseResultVo getAllRegionByCityName(@PathVariable String CityName){
        List<China> chinaList = chinaService.getAllRegionByCityName(CityName);
        if (chinaList.size() > 0){
            return ResponseResultVo.success(chinaList);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }
}
