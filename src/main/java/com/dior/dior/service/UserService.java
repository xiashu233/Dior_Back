package com.dior.dior.service;

import com.dior.dior.bean.UmsMember;
import com.dior.dior.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface UserService {
    List<UmsMember> getAllUser();

    UmsMember getOneUser(int id);

    int addUser(UmsMember user);

    UmsMember changeUser(UmsMember user);

    List<UmsMemberReceiveAddress> getAddressByUserId(String id);

    UmsMember checkUserByInfo(String phone, String pwd);

    String getUserMailByMeberId(int memberId);
}
