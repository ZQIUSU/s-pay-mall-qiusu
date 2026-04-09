package site.zqiusu.service;

public interface ISMSService {

    void sendVerificationCode(String phone, String code) throws Exception;
}
