package site.zqiusu.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.zqiusu.listener.OrderPaySuccessListener;

import java.util.concurrent.TimeUnit;

@Configuration
public class GuavaConfig {
    //本地缓存微信获取到的accesstoken2小时过期,openid1小时过期。
    @Bean
    public Cache<String, String> weixinAccessToken() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.HOURS)
                .build();
    }

    @Bean
    public Cache<String, String> openidToken() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build();
    }


    @Bean
    public EventBus eventBusListener(OrderPaySuccessListener listener){
        //创建事件总线
        EventBus eventBus = new EventBus();
        //注册订阅者
        eventBus.register(listener);
        return eventBus;
    }
}
