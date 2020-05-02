package com.dior.dior.controller;

import com.dior.dior.bean.CommonResult;
import com.dior.dior.response.ResponseResultVo;
import com.dior.dior.response.ResultVoStatus;
import com.dior.dior.util.QiniuCloudUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/back")
public class BackController {


    @RequestMapping(value="uploadImg", method= RequestMethod.POST)
    public ResponseResultVo uploadImg(MultipartFile image) {

        if (image.isEmpty()) {
            return ResponseResultVo.faild(ResultVoStatus.FileNotFound);
        }

        try {
            String url = "";
            byte[] bytes = image.getBytes();
            String imageName = UUID.randomUUID().toString();

            QiniuCloudUtil qiniuUtil = new QiniuCloudUtil();
            try {
                //使用base64方式上传到七牛云
                url = qiniuUtil.put64image(bytes, imageName);

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(url);
            return ResponseResultVo.success(url);
        } catch (IOException e) {
            return ResponseResultVo.faild(ResultVoStatus.FileNotFound);
        }
    }
}
