package com.dior.dior.controller;

import com.dior.dior.bean.UmsMember;
import com.dior.dior.bean.UmsMemberReceiveAddress;
import com.dior.dior.exception.HdException;
import com.dior.dior.response.ResponseResultVo;
import com.dior.dior.response.ResultVoStatus;
import com.dior.dior.service.UserAddressService;
import com.dior.dior.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/address")
public class UserAddressController {

    @Autowired
    UserAddressService userAddressService;
    @Autowired
    UserService userService;

    @PostMapping("addUserAddress")
    public ResponseResultVo addUserAddress(UmsMemberReceiveAddress address){

            UmsMember oneUser = userService.getOneUser(Integer.parseInt(address.getMemberId()));
            if (oneUser != null){
                UmsMemberReceiveAddress addAddress = null;
                try{
                    if ("-1".equals(address.getId())){
                        address.setId(null);
                        addAddress = userAddressService.addUserAddress(address);
                    }else{
                        addAddress = userAddressService.changeUserAddress(address);
                    }

                    if ("1".equals(address.getDefaultStatus())){
                        userAddressService.setAddressIsDefault(address.getId());
                    }

                    if (StringUtils.isNotBlank(addAddress.getId())){
                        return ResponseResultVo.success(addAddress);
                    }
                }catch (Exception e){
                    throw new  HdException(ResultVoStatus.AddUserAddressException);
                }

            }
            return ResponseResultVo.faild(ResultVoStatus.Error);
    }

    @PostMapping("getUserInfoAddress/{userId}")
    public ResponseResultVo getUserInfoAddress(@PathVariable("userId")String userId){
        UmsMemberReceiveAddress userInfoAddress = userAddressService.getUserInfoAddress(userId);
        if (userInfoAddress != null){
           return ResponseResultVo.success(userInfoAddress);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }

    @PostMapping("getUserDefaultAddress/{userId}")
    public ResponseResultVo getUserDefaultAddress(@PathVariable("userId")String userId){
        UmsMemberReceiveAddress userInfoAddress = userAddressService.getUserDefaultAddress(userId);
        if (userInfoAddress != null){
            return ResponseResultVo.success(userInfoAddress);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }

    @PostMapping("getUserNoDefaultAddress/{userId}")
    public ResponseResultVo getUserNoDefaultAddress(@PathVariable("userId")String userId){
        List<UmsMemberReceiveAddress> userNoDefaultAddress = userAddressService.getUserNoDefaultAddress(userId);
        if (userNoDefaultAddress.size() > 0){
            return ResponseResultVo.success(userNoDefaultAddress);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }

    @PostMapping("getUserAddressByAddressId/{addressId}")
    public ResponseResultVo getUserAddressByAddressId(@PathVariable("addressId")String addressId){
        UmsMemberReceiveAddress userInfoAddress = userAddressService.getUserAddressByAddressId(addressId);
        if (userInfoAddress != null){
            return ResponseResultVo.success(userInfoAddress);
        }
        return ResponseResultVo.faild(ResultVoStatus.GetError);
    }

    @PostMapping("delAddress/{addressId}")
    public ResponseResultVo delAddress(@PathVariable("addressId")String addressId){
        boolean result = false;
        try{
            result = userAddressService.delAddress(addressId);
        }catch (Exception e){
            throw new HdException(ResultVoStatus.DelUserAddressException);

        }
        if (result){
            return ResponseResultVo.success();
        }
        return ResponseResultVo.faild(ResultVoStatus.DelError);
    }

    @PostMapping("setAddressIsDefault/{addressId}")
    public ResponseResultVo setAddressIsDefault(@PathVariable("addressId")String addressId){
        UmsMemberReceiveAddress userInfoAddress = userAddressService.setAddressIsDefault(addressId);
        if (userInfoAddress != null){
            return ResponseResultVo.success(userInfoAddress);
        }
        return ResponseResultVo.faild(ResultVoStatus.ChangeError);
    }
}
