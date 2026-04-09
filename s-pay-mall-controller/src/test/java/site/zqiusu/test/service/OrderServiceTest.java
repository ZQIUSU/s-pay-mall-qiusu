package site.zqiusu.test.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import site.zqiusu.dao.IOrderDao;
import site.zqiusu.domain.po.PayOrder;
import site.zqiusu.domain.req.ShopCartReq;
import site.zqiusu.domain.res.PayOrderRes;
import site.zqiusu.service.IOrderService;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    @Resource
    private IOrderDao orderDao;

    @Resource
    private IOrderService orderService;

    @Test
    public void test_createOrder() throws Exception {
        ShopCartReq ShopCartReq = new ShopCartReq();
        ShopCartReq.setUserId("wyc");
        ShopCartReq.setProductId("10003");
        PayOrderRes payOrderRes = orderService.createOrder(ShopCartReq);
        log.info("请求参数:{}", JSON.toJSONString(ShopCartReq));
        log.info("测试结果:{}", JSON.toJSONString(payOrderRes));
    }

    @Test
    public void test_changeOrderPaySuccess(){
        ShopCartReq shopCartReq = new ShopCartReq();
        shopCartReq.setUserId("wyc");
        shopCartReq.setProductId("10003");
        PayOrder payOrder = orderDao.queryPayOrder(shopCartReq);
        orderService.changeOrderPaySuccess(payOrder.getOrderId());
        log.info("查询结果:{}",JSON.toJSONString(payOrder));
    }

    @Test
    public void test_queryNoPayNotifyOrder(){
        List<String> strings = orderService.queryNoPayNotifyOrder();
        log.info(JSON.toJSONString(strings));
    }

    @Test
    public void test_queryTimeoutCloseOrderList(){
        List<String> strings = orderService.queryTimeoutCloseOrderList();
        log.info(JSON.toJSONString(strings));
    }

    @Test
    public void test_changeOrderClose(){
        ShopCartReq shopCartReq = new ShopCartReq();
        shopCartReq.setUserId("wyc");
        shopCartReq.setProductId("10003");
        PayOrder payOrder = orderDao.queryPayOrder(shopCartReq);
        orderService.changeOrderClose(payOrder.getOrderId());

    }

}
