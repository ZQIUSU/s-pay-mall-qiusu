package site.zqiusu.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.zqiusu.common.constants.Constants;
import site.zqiusu.common.exception.AppException;
import site.zqiusu.common.util.JwtUtil;
import site.zqiusu.dao.IUserDao;
import site.zqiusu.domain.po.UserAccount;
import site.zqiusu.domain.res.LoginRes;
import site.zqiusu.service.ILoginService;
import site.zqiusu.service.ISMSService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SMSLoginServiceImpl implements ILoginService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expire-minutes}")
    private long expireMinutes;

    @Resource
    private IUserDao userDao;

    @Resource
    private ISMSService smsService;

    @Resource
    private RedissonClient redissonClient;

    private static final String PHONE_REGEX = "^1[3-9]\\d{9}$";

    @Override
    public void sendSMSCode(String phone) throws Exception {
        // 1. 校验手机号格式
        if (StringUtils.isBlank(phone) || !phone.matches(PHONE_REGEX)) {
            throw new AppException(Constants.ResponseCode.ILLEGAL_PARAMETER.getCode(), "手机号格式不正确");
        }

        // 2. 检查发送频率限制（60秒内不能重复发送）
        RBucket<String> limitBucket = redissonClient.getBucket(Constants.SMS_LIMIT_KEY + phone);
        if (limitBucket.isExists()) {
            throw new AppException(Constants.ResponseCode.SMS_CODE_FREQUENT.getCode(),
                    Constants.ResponseCode.SMS_CODE_FREQUENT.getInfo());
        }

        // 3. 生成6位随机验证码
        String code = String.format("%06d", new Random().nextInt(1000000));

        // 4. 调用阿里云短信服务发送
        smsService.sendVerificationCode(phone, code);

        // 5. 存储验证码到 Redis（5分钟有效）
        RBucket<String> codeBucket = redissonClient.getBucket(Constants.SMS_CODE_KEY + phone);
        codeBucket.set(code, 5, TimeUnit.MINUTES);

        // 6. 设置发送频率限制（60秒）
        limitBucket.set("1", 60, TimeUnit.SECONDS);

        log.info("验证码发送成功 phone:{}", phone);
    }

    @Override
    public LoginRes smsLogin(String phone, String code) {
        // 1. 校验参数
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(code)) {
            throw new AppException(Constants.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Constants.ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        // 2. 校验验证码
        RBucket<String> codeBucket = redissonClient.getBucket(Constants.SMS_CODE_KEY + phone);
        String storedCode = codeBucket.get();
        if (StringUtils.isBlank(storedCode) || !storedCode.equals(code)) {
            throw new AppException(Constants.ResponseCode.SMS_CODE_INVALID.getCode(),
                    Constants.ResponseCode.SMS_CODE_INVALID.getInfo());
        }

        // 3. 查询用户（注册即登录）
        UserAccount userAccount = userDao.queryByPhone(phone);
        boolean isNewUser = false;
        if (null == userAccount) {
            // 自动注册
            userAccount = UserAccount.builder()
                    .phone(phone)
                    .nickname("用户" + phone.substring(phone.length() - 4))
                    .status(Constants.UserStatusEnum.ENABLE.getCode())
                    .build();
            userDao.insert(userAccount);
            isNewUser = true;
            log.info("新用户注册 phone:{} userId:{}", phone, userAccount.getId());
        }

        // 4. 校验用户状态
        if (Constants.UserStatusEnum.DISABLE.getCode().equals(userAccount.getStatus())) {
            throw new AppException(Constants.ResponseCode.ACCOUNT_DISABLED.getCode(),
                    Constants.ResponseCode.ACCOUNT_DISABLED.getInfo());
        }

        // 5. 生成 JWT Token
        String userId = String.valueOf(userAccount.getId());
        String token = JwtUtil.generateToken(userId, phone, userAccount.getNickname(), secretKey, expireMinutes);

        // 6. 存储登录信息到 Redis
        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("token", token);
        tokenInfo.put("userId", userId);
        tokenInfo.put("phone", phone);
        tokenInfo.put("nickname", userAccount.getNickname());
        RBucket<String> tokenBucket = redissonClient.getBucket(Constants.LOGIN_TOKEN_KEY + userId);
        tokenBucket.set(JSON.toJSONString(tokenInfo), expireMinutes, TimeUnit.MINUTES);

        // 7. 删除已使用的验证码（一次性）
        codeBucket.delete();

        log.info("用户登录成功 phone:{} userId:{} isNewUser:{}", phone, userId, isNewUser);

        return LoginRes.builder()
                .token(token)
                .userId(userId)
                .nickname(userAccount.getNickname())
                .isNewUser(isNewUser)
                .build();
    }

    @Override
    public void logout(String userId) {
        RBucket<String> tokenBucket = redissonClient.getBucket(Constants.LOGIN_TOKEN_KEY + userId);
        tokenBucket.delete();
        log.info("用户登出成功 userId:{}", userId);
    }
}
