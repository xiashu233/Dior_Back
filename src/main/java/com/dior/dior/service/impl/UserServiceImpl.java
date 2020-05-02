package com.dior.dior.service.impl;

import com.dior.dior.bean.UmsMember;
import com.dior.dior.bean.UmsMemberReceiveAddress;
import com.dior.dior.mapper.UmsMemberReceiveAddressMapper;
import com.dior.dior.mapper.UserMapper;
import com.dior.dior.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UmsMemberReceiveAddressMapper userAddressMapper;

    @Override
    public List<UmsMember> getAllUser() {
        return userMapper.selectAll();
    }

    @Override
    public UmsMember getOneUser(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public int addUser(UmsMember user) {
        int userId = userMapper.insertSelective(user);
        return userId;
    }

    @Override
    public UmsMember changeUser(UmsMember user) {
        return null;
    }

    @Override
    public List<UmsMemberReceiveAddress> getAddressByUserId(String id) {
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId(id);
        List<UmsMemberReceiveAddress> addressList = userAddressMapper.select(umsMemberReceiveAddress);
        return addressList;
    }

    @Override
    public UmsMember checkUserByInfo(String phone, String pwd) {
        UmsMember umsMember = new UmsMember();
        umsMember.setPhone(phone);
        umsMember.setPassword(pwd);

        return userMapper.selectOne(umsMember);
    }

    @Override
    public String getUserMailByMeberId(int memberId) {

        return userMapper.selectByPrimaryKey(memberId).getMail();
    }


}
