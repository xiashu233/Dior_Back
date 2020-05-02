package com.dior.dior.controller;

import com.alibaba.fastjson.JSON;
import com.dior.dior.bean.PmsBaseCatalog1;
import com.dior.dior.bean.PmsProductInfo;
import com.dior.dior.bean.UmsMemberFavorite;
import com.dior.dior.exception.HdException;
import com.dior.dior.response.ResponseResultVo;
import com.dior.dior.response.ResultVoStatus;
import com.dior.dior.service.CatalogService;
import com.dior.dior.service.ProcService;
import com.dior.dior.util.QiniuCloudUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

//import com.dior.cloudback.util.QiniuCloudUtil;
//import com.dior.cloudback.util.QiniuMediaUtilService;

@CrossOrigin
@RestController
@RequestMapping("proc")
public class ProcController {

    @Autowired
    CatalogService catalogService;
    @Autowired
    ProcService procService;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value="uploadImg", method= RequestMethod.POST)
    public ResponseResultVo uploadImg(MultipartFile image) {

        if (image.isEmpty()) {
            return ResponseResultVo.faild(ResultVoStatus.FileNotFound);
        }

        try {
            String url = "";
            byte[] bytes = image.getBytes();
            String imageName = UUID.randomUUID().toString();

            //QiniuMediaUtilService qiniuMediaUtilService = new QiniuMediaUtilService();
            QiniuCloudUtil qiniuCloudUtil = new QiniuCloudUtil();
            try {
                //使用base64方式上传到七牛云
//                boolean b = qiniuMediaUtilService.uploadFile(image.getName(), image.getInputStream());
//                if (b){
//                    url = qiniuMediaUtilService.getFileResourceUrl(image.getName());
//                }
                url = qiniuCloudUtil.put64image(bytes,imageName);

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(url);
            return ResponseResultVo.success(url);
        } catch (IOException e) {
            return ResponseResultVo.faild(ResultVoStatus.FileNotFound);
        }
    }

//    @PostMapping(value="uploadFile")
//    public CommonResult uploadFile(MultipartFile file) {
//
//        if (file.isEmpty()) {
//            return new CommonResult(400,"文件为空，请重新上传");
//        }
//
//        try {
//            String url = "";
//            byte[] bytes = file.getBytes();
//            String imageName = UUID.randomUUID().toString();
//
//            //QiniuCloudUtil qiniuUtil = new QiniuCloudUtil();
//            QiniuMediaUtilService qiniuMediaUtilService = new QiniuMediaUtilService();
//            try {
//                //使用base64方式上传到七牛云
//                boolean b = qiniuMediaUtilService.uploadFile(file.getName(), file.getInputStream());
//                if (b){
//                    url = qiniuMediaUtilService.getFileResourceUrl(file.getName());
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            System.out.println(url);
//            return new CommonResult(200,"上传成功",url);
//        } catch (IOException e) {
//            return new CommonResult(500,"上传文件时发生异常，请稍后重试");
//        }
//    }



    @PostMapping("getAllProcByCatalog3/{catalog3}")
    public ResponseResultVo getAllProcByCatalog3(@PathVariable("catalog3")String catalog3){
        List<PmsProductInfo> pmsProductInfoList = procService.getAllProcByCatalog3(catalog3);
        if (pmsProductInfoList.size()>0){
            return ResponseResultVo.success(pmsProductInfoList);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }

    @PostMapping("getAllProcByCatalog3AndPage/{catalog3}/{pageNum}")
    public ResponseResultVo getAllProcByCatalog3AndPage(@PathVariable("catalog3")String catalog3,@PathVariable("pageNum")int pageNum){
        Page<PmsProductInfo> pmsProductInfoPage = PageHelper.startPage(pageNum, 5);
        List<PmsProductInfo> pmsProductInfoList = procService.getAllProcByCatalog3(catalog3);
        System.out.println(pmsProductInfoPage.getPageNum());

        if (pmsProductInfoList.size()>0){
            return ResponseResultVo.success(pmsProductInfoList);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }

    @PostMapping("getProcListByCatalog3/{catalog3}")
    public ResponseResultVo getProcListByCatalog3(@PathVariable("catalog3")String catalog3){
        List<PmsProductInfo> pmsProductInfoList = procService.getProcListByCatalog3(catalog3);
        if (pmsProductInfoList.size()>0){
            return ResponseResultVo.success(pmsProductInfoList);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }

    @RequestMapping("saveProc")
    public ResponseResultVo saveProc(PmsProductInfo pmsProductInfo, @RequestParam("imgList")String imgList,
                                 @RequestParam("defaultImgIndex")int defaultImgIndex,
                                 @RequestParam("attrMap")String attrMapJson){

        if (pmsProductInfo.getId().equals("-1")){
            // 添加商品
            String procId = procService.addProcInfo(pmsProductInfo);

            if (StringUtils.isNotBlank(procId)){
                pmsProductInfo.setId(procId);
                procService.addProcImage(procId,imgList,defaultImgIndex);

                if (StringUtils.isNotBlank(attrMapJson)){
                    procService.addProcAttr(procId,attrMapJson);
                }

                return ResponseResultVo.success();
            }

            return ResponseResultVo.faild(ResultVoStatus.Error);
        }else{
            // 修改商品
            procService.changeProcInfo(pmsProductInfo);
            procService.deleteProcImageByProcId(pmsProductInfo.getId());
            procService.addProcImage(pmsProductInfo.getId(),imgList,defaultImgIndex);
            procService.deleteProcAttrValueByProcId(pmsProductInfo.getId());
            if (StringUtils.isNotBlank(attrMapJson)){
                procService.addProcAttr(pmsProductInfo.getId(),attrMapJson);
            }
            return ResponseResultVo.faild(ResultVoStatus.ChangeError);
        }

    }


    @PostMapping("deleteProc/{id}")
    public ResponseResultVo deleteProc(@PathVariable("id")String id){
        // 返回删除总数
        int delCount = procService.deleteProc(id);
        if (delCount > 0){
            return ResponseResultVo.success();
        }
        return ResponseResultVo.faild(ResultVoStatus.DelError);
    }

    @PostMapping("getProcById/{id}")
    public ResponseResultVo getProcById(@PathVariable("id")String id){
        //
        Object procItemJsonObj = redisTemplate.boundValueOps("ProcItem:" + id + ":info").get();
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        if (procItemJsonObj == null){
            pmsProductInfo = procService.getProcById(id);
            if (pmsProductInfo != null){
                redisTemplate.boundValueOps("ProcItem:" + id + ":info").set(JSON.toJSON(pmsProductInfo));
            }
        }else{
            pmsProductInfo = JSON.parseObject(procItemJsonObj.toString(),pmsProductInfo.getClass());
        }

        if (pmsProductInfo != null){
            return ResponseResultVo.success(pmsProductInfo);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }

    @PostMapping("search/{key}")
    public ResponseResultVo search(@PathVariable("key")String key){
        //
        List<PmsProductInfo> pmsProductInfo = procService.getProcBySearchKey(key);
        if (pmsProductInfo != null){
            return ResponseResultVo.success(pmsProductInfo);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }

    @PostMapping("getFavoriteByMemberId/{MemberId}")
    public ResponseResultVo getFavoriteByMemberId(@PathVariable("MemberId")String MemberId){
        //

        List<UmsMemberFavorite> favoriteList = procService.getFavoriteByMemberId(MemberId);

        List<PmsProductInfo> pmsProductInfo = procService.getProcListByFavoriteList(favoriteList);

        if (pmsProductInfo != null){
            return ResponseResultVo.success(pmsProductInfo);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }


    @PostMapping("addFavorite/{MemberId}/{procId}")
    public ResponseResultVo addFavorite(@PathVariable("MemberId")String memberId,@PathVariable("procId")String procId){

        UmsMemberFavorite umsMemberFavorite = new UmsMemberFavorite();
        umsMemberFavorite.setMemberId(memberId);
        umsMemberFavorite.setProductId(procId);
        UmsMemberFavorite resultFavorite = null;
        try{
            resultFavorite = procService.checkFavorite(umsMemberFavorite);
        }catch (Exception e){
            throw new HdException(ResultVoStatus.GetDataMoreException);
        }


        if (resultFavorite == null){
            procService.addFavorite(umsMemberFavorite);
            if (StringUtils.isNotBlank(umsMemberFavorite.getId())){
                return ResponseResultVo.success(umsMemberFavorite);
            }
        }
        return ResponseResultVo.faild(ResultVoStatus.Error);
    }

    @PostMapping("delFavorite/{MemberId}/{procId}")
    public ResponseResultVo delFavorite(@PathVariable("MemberId")String memberId,@PathVariable("procId")String procId){
        UmsMemberFavorite umsMemberFavorite = new UmsMemberFavorite();
        umsMemberFavorite.setMemberId(memberId);
        umsMemberFavorite.setProductId(procId);
        boolean res = false;
        try{
            res = procService.delFavorite(umsMemberFavorite);
        }catch (Exception e){
            throw new HdException(ResultVoStatus.DelError);
        }


        if (res == true){
            return ResponseResultVo.success();
        }
        return ResponseResultVo.faild(ResultVoStatus.DelError);
    }




}
