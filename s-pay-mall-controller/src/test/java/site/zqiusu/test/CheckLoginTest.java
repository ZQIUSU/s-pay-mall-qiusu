package site.zqiusu.test;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import site.zqiusu.service.ILoginService;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CheckLoginTest {

    @Resource
    private ILoginService loginService;

    @Test
    public void loginService_test() throws Exception {
        String ticket = loginService.createQrCodeTicket();
        String openid = "omADk6tkNC439hXDMlSgf-3ESlrU";
        loginService.saveLoginState(ticket, openid);
        String s = loginService.checkLogin(ticket);
        log.info(s);
    }
}
