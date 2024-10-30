package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.res.WeixinQrCodeRes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


//localhost:80/Test/getRes

@Slf4j
@RestController()
@RequestMapping("Test")
public class TestController {
    @GetMapping("/getRes")
    public WeixinQrCodeRes get(@RequestParam(value="name") String name){
        WeixinQrCodeRes weixinQrCodeRes = new WeixinQrCodeRes();
        weixinQrCodeRes.setUrl("123");
        System.out.println(name);
        return weixinQrCodeRes;
    }
}
