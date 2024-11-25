package org.example.service;

import com.alipay.api.AlipayApiException;
import org.example.domain.req.ShopCartReq;
import org.example.domain.res.PayOrderRes;

import java.util.List;

public interface IOrderService {
    PayOrderRes createOrder (ShopCartReq ShopCartReq) throws Exception;

    void changeOrderPaySuccess(String orderId);

    List<String> queryNoPayNotifyOrder();

    List<String> queryTimeoutCloseOrderList();

    boolean changeOrderClose(String orderId);
}
