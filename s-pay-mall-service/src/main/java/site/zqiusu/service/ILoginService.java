package site.zqiusu.service;

import site.zqiusu.domain.res.LoginRes;

public interface ILoginService {

    void sendSMSCode(String phone) throws Exception;

    LoginRes smsLogin(String phone, String code);

    void logout(String userId);
}
