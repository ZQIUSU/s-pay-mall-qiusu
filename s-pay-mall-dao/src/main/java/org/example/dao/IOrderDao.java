package org.example.dao;


import org.apache.ibatis.annotations.Mapper;
import org.example.domain.po.PayOrder;

import java.util.List;

@Mapper
public interface IOrderDao {

    void insert(PayOrder payOrder);

    PayOrder queryUnPayOrder(PayOrder payOrder);

    void updateOrderPayInfo(PayOrder payOrder);

    void changeOrderPaySuccess(PayOrder payOrder);

    Boolean changeOrderClose(String orderId);

    List<String> queryTimeoutCloseOrderList();

    List<String> queryNoPayNotifyOrder();
}
