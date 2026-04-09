package site.zqiusu.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import site.zqiusu.common.constants.Constants;
import site.zqiusu.common.response.Response;
import site.zqiusu.domain.req.SMSLoginReq;
import site.zqiusu.domain.req.SendSMSCodeReq;
import site.zqiusu.domain.res.LoginRes;
import site.zqiusu.service.ILoginService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/login/")
public class LoginController {

    @Resource
    private ILoginService loginService;

    /**
     * 发送短信验证码
     */
    @RequestMapping(value = "sms/send", method = RequestMethod.POST)
    public Response<Void> sendSMSCode(@RequestBody SendSMSCodeReq req) {
        try {
            log.info("发送短信验证码 phone:{}", req.getPhone());
            loginService.sendSMSCode(req.getPhone());
            return Response.<Void>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("发送短信验证码失败 phone:{}", req.getPhone(), e);
            if (e instanceof site.zqiusu.common.exception.AppException) {
                site.zqiusu.common.exception.AppException ae = (site.zqiusu.common.exception.AppException) e;
                return Response.<Void>builder()
                        .code(ae.getCode())
                        .info(ae.getInfo())
                        .build();
            }
            return Response.<Void>builder()
                    .code(Constants.ResponseCode.SMS_SEND_FAIL.getCode())
                    .info(Constants.ResponseCode.SMS_SEND_FAIL.getInfo())
                    .build();
        }
    }

    /**
     * 验证码登录/注册
     */
    @RequestMapping(value = "sms/verify", method = RequestMethod.POST)
    public Response<LoginRes> smsLogin(@RequestBody SMSLoginReq req) {
        try {
            log.info("验证码登录 phone:{}", req.getPhone());
            LoginRes loginRes = loginService.smsLogin(req.getPhone(), req.getCode());
            return Response.<LoginRes>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .data(loginRes)
                    .build();
        } catch (Exception e) {
            log.error("验证码登录失败 phone:{}", req.getPhone(), e);
            if (e instanceof site.zqiusu.common.exception.AppException) {
                site.zqiusu.common.exception.AppException ae = (site.zqiusu.common.exception.AppException) e;
                return Response.<LoginRes>builder()
                        .code(ae.getCode())
                        .info(ae.getInfo())
                        .build();
            }
            return Response.<LoginRes>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    /**
     * 登出
     */
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public Response<Void> logout(HttpServletRequest request) {
        try {
            String userId = (String) request.getAttribute("userId");
            loginService.logout(userId);
            return Response.<Void>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("登出失败", e);
            return Response.<Void>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
