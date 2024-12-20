package org.example.domain.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayOrder {
    /** 自增id */
    private Long id;
    /** 用户id */
    private String userId;
    /** 商品id */
    private String productId;
    /** 商品名称 */
    private String productName;
    /** 订单id */
    private String orderId;
    /** 下单时间 */
    private Date orderTime;
    /** 订单金额 */
    private BigDecimal totalAmount;
    /** 订单状态 */
    private String status;
    /** 支付url */
    private String payUrl;
    /** 支付时间 */
    private Date payTime;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

}
