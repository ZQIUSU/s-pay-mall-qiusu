package site.zqiusu.dao;


import org.apache.ibatis.annotations.Mapper;
import site.zqiusu.domain.po.PayOrder;
import site.zqiusu.domain.req.ShopCartReq;

import java.util.List;

@Mapper
public interface IOrderDao {

    void insert(PayOrder payOrder);//插入

    PayOrder queryPayOrder(ShopCartReq shopCartReq);

    PayOrder queryUnPayOrder(PayOrder payOrder);//查询未支付用户

    void updateOrderPayInfo(PayOrder payOrder);//将

    void changeOrderPaySuccess(PayOrder payOrder);

    Boolean changeOrderClose(String orderId);

    List<String> queryTimeoutCloseOrderList();

    List<String> queryNoPayNotifyOrder();
}
