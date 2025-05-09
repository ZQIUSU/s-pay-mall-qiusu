package org.example.test;

import lombok.extern.slf4j.Slf4j;
import org.example.service.ILoginService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class WeiXinPortalTest {
    @Resource
    private ILoginService loginService;

    @Test
    public void test() throws Exception {
        String qrCodeTicket = loginService.createQrCodeTicket();
        System.out.println(qrCodeTicket);
    }
}
