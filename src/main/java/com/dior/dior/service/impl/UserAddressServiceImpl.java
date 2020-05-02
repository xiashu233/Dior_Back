package com.dior.dior.service.impl;

import com.dior.dior.bean.UmsMemberReceiveAddress;
import com.dior.dior.mapper.UmsMemberReceiveAddressMapper;
import com.dior.dior.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserAddressServiceImpl implements UserAddressService {

    @Autowired
    UmsMemberReceiveAddressMapper addressMapper;

    @Override
    public UmsMemberReceiveAddress addUserAddress(UmsMemberReceiveAddress address) {
        addressMapper.insertSelective(address);
        return address;
    }

    @Override
    public UmsMemberReceiveAddress getUserInfoAddress(String userId) {
        UmsMemberReceiveAddress userAddress = new UmsMemberReceiveAddress();
        userAddress.setMemberId(userId);
        userAddress.setDefaultStatus("2");

        return addressMapper.selectOne(userAddress);
    }

    @Override
    public UmsMemberReceiveAddress changeUserAddress(UmsMemberReceiveAddress address) {
        Example example = new Example(UmsMemberReceiveAddress.class);
        example.createCriteria().andEqualTo("id",address.getId());
        addressMapper.updateByExample(address,example);
        return address;
    }

    @Override
    public UmsMemberReceiveAddress getUserDefaultAddress(String userId) {
        UmsMemberReceiveAddress userAddress = new UmsMemberReceiveAddress();
        userAddress.setMemberId(userId);
        userAddress.setDefaultStatus("1");

        return addressMapper.selectOne(userAddress);
    }

    @Override
    public boolean delAddress(String addressId) {
        return addressMapper.deleteByPrimaryKey(addressId)>0;
    }

    @Override
    public UmsMemberReceiveAddress setAddressIsDefault(String addressId) {
        UmsMemberReceiveAddress address = addressMapper.selectByPrimaryKey(addressId);
        addressMapper.updateAddressAllSetZero(address.getMemberId());

        addressMapper.updateAddressIsDefault(addressId);
        address.setDefaultStatus("1");
        return address;
    }

    @Override
    public List<UmsMemberReceiveAddress> getUserNoDefaultAddress(String userId) {
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId(userId);
        umsMemberReceiveAddress.setDefaultStatus("0");
        return addressMapper.select(umsMemberReceiveAddress);
    }

    @Override
    public UmsMemberReceiveAddress getUserAddressByAddressId(String addressId) {
        return addressMapper.selectByPrimaryKey(addressId);
    }
}
