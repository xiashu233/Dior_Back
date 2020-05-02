package com.dior.dior.mapper;

import com.dior.dior.bean.OmsOrder;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;

public interface OmsOrderMapper extends Mapper<OmsOrder> {
    int updateOrderStatus(String out_trade_no, String memberId, Date date);
}
