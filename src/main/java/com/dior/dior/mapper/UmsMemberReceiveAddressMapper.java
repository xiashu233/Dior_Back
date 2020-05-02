package com.dior.dior.mapper;

import com.dior.dior.bean.UmsMemberReceiveAddress;
import tk.mybatis.mapper.common.Mapper;

public interface UmsMemberReceiveAddressMapper extends Mapper<UmsMemberReceiveAddress> {
    void updateAddressAllSetZero(String memberId);

    void updateAddressIsDefault(String addressId);
}
