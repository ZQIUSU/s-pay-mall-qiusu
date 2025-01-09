package org.example.controller;

import org.example.service.impl.PassWordGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PassWordController {
    @GetMapping("/generate-password")
    public String PassWord(){
        return PassWordGenerator.generatePassword();
    }
}
