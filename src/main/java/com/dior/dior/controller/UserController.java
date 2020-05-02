package com.dior.dior.controller;

import com.dior.dior.bean.MailDto;
import com.dior.dior.bean.UmsMember;
import com.dior.dior.bean.UmsMemberReceiveAddress;
import com.dior.dior.exception.HdException;
import com.dior.dior.response.ResponseResultVo;
import com.dior.dior.response.ResultVoStatus;
import com.dior.dior.service.UserService;
import com.dior.dior.util.MailUtil;
import com.dior.dior.util.SendsmsUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 解决跨域问题
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;



    @Autowired
    private Environment env;

    @PostMapping("getAllUser")
    public ResponseResultVo getAllUser(){
        List<UmsMember> userList = userService.getAllUser();
        if (userList != null){
            return ResponseResultVo.success(userList);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }

    @PostMapping("getOneUser/{id}")
    public ResponseResultVo getOneUser(@PathVariable("id") int id){
        UmsMember user = userService.getOneUser(id);
        if (user != null){
            return ResponseResultVo.success(user);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }

    @PostMapping(value="/addUser")
    public ResponseResultVo addOneUser(UmsMember user){
        int userId = 0;
        try{
            userId = userService.addUser(user);
        }catch (Exception e){
            throw new HdException(ResultVoStatus.AddUserException);
        }

        if (userId != 0 ){
            user.setId(String.valueOf(userId));
            return ResponseResultVo.success(user);
        }else{
            return ResponseResultVo.faild(ResultVoStatus.Error);
        }

//        if (user.getUsername().equals("test") ){
//            return new CommonResult<UmsMember>(200,"添加用户成功，此时用户id为：",user);
//        }else{
//            return new CommonResult<UmsMember>(500,"添加用户失败，用户信息可能未补全");
//        }
    }


    @PostMapping("changeUser")
    public ResponseResultVo changeOneUser(@RequestBody UmsMember user){
        UmsMember userChange = userService.changeUser(user);
        if (StringUtils.isNotBlank(userChange.getId())){

            return ResponseResultVo.success(userChange);
        }else{
            return ResponseResultVo.faild(ResultVoStatus.ChangeError);
        }
    }

    @RequestMapping("getAddressByUserId/{id}")
    public ResponseResultVo getAddressByUserId(@PathVariable("id") String id){
        List<UmsMemberReceiveAddress> addressList = userService.getAddressByUserId(id);
        if (addressList.size() > 0){
            return ResponseResultVo.success(addressList);
        }else{
            return ResponseResultVo.faild(ResultVoStatus.GetError);
        }
    }

    @PostMapping("getSmsByPhone/{phone}")
    public ResponseResultVo getSmsByPhone(@PathVariable("phone") String phone){
        int sms = SendsmsUtil.sendSms(phone);
        if (sms != -1){
            return ResponseResultVo.success(sms);
        }else{
            return ResponseResultVo.faild(ResultVoStatus.Error);
        }
    }

    @PostMapping("checkUserByInfo/{phone}/{pwd}")
    public ResponseResultVo checkUserByInfo(@PathVariable("phone") String phone,@PathVariable("pwd") String pwd){
        UmsMember umsMember = userService.checkUserByInfo(phone,pwd);
        if (umsMember != null){
           return ResponseResultVo.success(umsMember);
        }else{
            return ResponseResultVo.faild(ResultVoStatus.GetError);
        }
    }



    @PostMapping("testException")
    public ResponseResultVo testException() throws Exception {
        throw new HdException(ResultVoStatus.Error);
        //return new CommonResult(500,"校验失败，请检查用户手机号或密码是否正确！");

    }
}
