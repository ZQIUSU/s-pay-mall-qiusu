package site.zqiusu.service;

import site.zqiusu.domain.req.ShopCartReq;
import site.zqiusu.domain.res.PayOrderRes;

import java.util.List;

public interface IOrderService {
    PayOrderRes createOrder (ShopCartReq ShopCartReq) throws Exception;

    void changeOrderPaySuccess(String orderId);

    List<String> queryNoPayNotifyOrder();

    List<String> queryTimeoutCloseOrderList();

    boolean changeOrderClose(String orderId);
}
