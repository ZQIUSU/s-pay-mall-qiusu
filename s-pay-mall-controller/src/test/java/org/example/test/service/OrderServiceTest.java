package org.example.test.service;

import org.example.domain.req.ShopCartReq;
import org.example.domain.res.PayOrderRes;
import org.example.service.IOrderService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    @Resource
    private IOrderService orderService;

    @Test
    public void test_createOrder() throws Exception {
        ShopCartReq ShopCartReq = new ShopCartReq();
        ShopCartReq.setUserId("xiaofuge");
        ShopCartReq.setProductId("10001");
        PayOrderRes payOrderRes = orderService.createOrder(ShopCartReq);
        log.info("请求参数:{}", JSON.toJSONString(ShopCartReq));
        log.info("测试结果:{}", JSON.toJSONString(payOrderRes));
    }

}
