package org.example.domain.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
/**
 * 购物车Req对象
 */
public class ShopCartReq {

    private String userId;

    private String productId;

}
