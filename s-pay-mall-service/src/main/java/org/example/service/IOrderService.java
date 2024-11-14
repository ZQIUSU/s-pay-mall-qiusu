package org.example.service;

import com.alipay.api.AlipayApiException;
import org.example.domain.req.ShopCartReq;
import org.example.domain.res.PayOrderRes;

public interface IOrderService {
    PayOrderRes createOrder (ShopCartReq ShopCartReq) throws Exception;
}
