package site.zqiusu.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

//请求返参
public class Constants {

    public final static String SPLIT = ",";

    @AllArgsConstructor
    @Getter
    public enum ResponseCode {
        SUCCESS("0000", "调用成功"),
        UN_ERROR("0001", "调用失败"),
        ILLEGAL_PARAMETER("0002", "非法参数"),
        NO_LOGIN("0003", "未登录"),
        SMS_SEND_FAIL("0004", "短信发送失败"),
        SMS_CODE_INVALID("0005", "验证码无效或已过期"),
        SMS_CODE_FREQUENT("0006", "发送验证码过于频繁"),
        TOKEN_INVALID("0007", "Token无效或已过期"),
        ACCOUNT_DISABLED("0008", "账户已被禁用"),
        ;

        private String code;
        private String info;

    }

    @Getter
    @AllArgsConstructor
    public enum UserStatusEnum {
        ENABLE("ENABLE", "启用"),
        DISABLE("DISABLE", "禁用"),
        ;

        private final String code;
        private final String desc;
    }

    public final static String SMS_CODE_KEY = "sms:code:";
    public final static String SMS_LIMIT_KEY = "sms:limit:";
    public final static String LOGIN_TOKEN_KEY = "login:token:";

    @Getter
    @AllArgsConstructor
    public enum OrderStatusEnum {

        CREATE("CREATE", "创建完成 - 如果调单了，也会从创建记录重新发起创建支付单"),
        PAY_WAIT("PAY_WAIT", "等待支付 - 订单创建完成后，创建支付单"),
        PAY_SUCCESS("PAY_SUCCESS", "支付成功 - 接收到支付回调消息"),
        DEAL_DONE("DEAL_DONE", "交易完成 - 商品发货完成"),
        CLOSE("CLOSE", "超时关单 - 超市未支付"),
        ;

        private final String code;
        private final String desc;

    }

}
