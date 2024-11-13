package org.example.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AliPayConfigProperties.class)
public class AliPayConfig {

    public AlipayClient alipayClient(AliPayConfigProperties properties){
        return new DefaultAlipayClient(
                properties.getGatewayUrl(),
                properties.getApp_id(),
                properties.getMerchant_private_key(),
                properties.getAlipay_public_key(),
                properties.getFormat(),
                properties.getCharset(),
                properties.getSign_type());
    }

}
