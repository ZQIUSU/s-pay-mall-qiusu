package site.zqiusu.domain.res;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.zqiusu.common.constants.Constants;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderRes {
    private String userId;
    private String orderId;
    private String payUrl;
    private Constants.OrderStatusEnum orderStatusEnum;
}
