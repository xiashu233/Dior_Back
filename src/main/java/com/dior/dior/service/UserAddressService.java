package com.dior.dior.service;

import com.dior.dior.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface UserAddressService {
    UmsMemberReceiveAddress addUserAddress(UmsMemberReceiveAddress address);

    UmsMemberReceiveAddress getUserInfoAddress(String userId);

    UmsMemberReceiveAddress changeUserAddress(UmsMemberReceiveAddress address);

    UmsMemberReceiveAddress getUserDefaultAddress(String userId);

    boolean delAddress(String addressId);

    UmsMemberReceiveAddress setAddressIsDefault(String addressId);

    List<UmsMemberReceiveAddress> getUserNoDefaultAddress(String userId);

    UmsMemberReceiveAddress getUserAddressByAddressId(String addressId);
}
