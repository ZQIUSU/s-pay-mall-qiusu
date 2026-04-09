package site.zqiusu.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import site.zqiusu.service.ISMSService;

/**
 * Mock短信服务 - 本地开发测试使用
 * 验证码直接输出到日志，无需真实短信网关
 *
 * 默认启用，当配置 aliyun.sms.enabled=true 时切换到阿里云短信服务
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "aliyun.sms.enabled", havingValue = "false", matchIfMissing = true)
public class MockSMSServiceImpl implements ISMSService {

    @Override
    public void sendVerificationCode(String phone, String code) {
        log.info("╔════════════════════════════════════════╗");
        log.info("║           Mock 短信服务                 ║");
        log.info("╠════════════════════════════════════════╣");
        log.info("║  手机号: {}                    ║", phone);
        log.info("║  验证码: {}                          ║", code);
        log.info("║  有效期: 5分钟                         ║");
        log.info("╚════════════════════════════════════════╝");
    }
}
