package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class PassWordGenerator {
    public static String generatePassword(){
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        int specialSymbolIndex = random.nextInt(10);
        int intIndex;
        do{
            intIndex = random.nextInt(10);
        }while(intIndex==specialSymbolIndex);
        char[] specialSymbols = {'@','.','_'};
        char specialSymbol = specialSymbols[random.nextInt(specialSymbols.length)];
        for(int i=0;i<10;i++){
            if(i == specialSymbolIndex){
                password.append(specialSymbol);
            }if(i == intIndex){
                password.append(random.nextInt(10));
            }else {
                boolean isUpperCase = random.nextBoolean();
                if(isUpperCase){
                    password.append((char)(random.nextInt(26)+'A'));
                }else{
                    password.append((char)(random.nextInt(26)+'a'));
                }
            }
        }
        String lastPassword = password.toString();
        log.info("生成密码成功:{}",lastPassword);
        return lastPassword;
    }
}
