package site.zqiusu.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import site.zqiusu.service.ISMSService;

import javax.annotation.PostConstruct;

/**
 * 阿里云短信服务 - 生产环境使用
 * 需要配置 aliyun.sms.enabled=true 以及相关密钥
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "aliyun.sms.enabled", havingValue = "true")
public class AliyunSMSServiceImpl implements ISMSService {

    @Value("${aliyun.sms.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.sms.access-key-secret}")
    private String accessKeySecret;

    @Value("${aliyun.sms.sign-name}")
    private String signName;

    @Value("${aliyun.sms.template-code}")
    private String templateCode;

    private Client client;

    @PostConstruct
    public void init() throws Exception {
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret);
        config.endpoint = "dysmsapi.aliyuncs.com";
        this.client = new Client(config);
        log.info("阿里云短信服务初始化成功");
    }

    @Override
    public void sendVerificationCode(String phone, String code) throws Exception {
        SendSmsRequest request = new SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName(signName)
                .setTemplateCode(templateCode)
                .setTemplateParam("{\"code\":\"" + code + "\"}");

        SendSmsResponse response = client.sendSms(request);
        if (response.getBody() == null || !"OK".equals(response.getBody().getCode())) {
            String errMsg = response.getBody() != null ? response.getBody().getMessage() : "null response";
            log.error("短信发送失败 phone:{} errMsg:{}", phone, errMsg);
            throw new RuntimeException("短信发送失败: " + errMsg);
        }
        log.info("短信发送成功 phone:{}", phone);
    }
}
