package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.res.WeixinQrCodeRes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


//localhost:80/Test/getRes

@Slf4j
@RequestMapping("Test")
public class TestController {
    @ResponseBody
    @GetMapping("/getRes")
    public WeixinQrCodeRes get(@RequestParam(value="name",required = false) String name){
        WeixinQrCodeRes weixinQrCodeRes = new WeixinQrCodeRes();
        weixinQrCodeRes.setUrl("123");
        System.out.println(name);
        return weixinQrCodeRes;
    }
}
