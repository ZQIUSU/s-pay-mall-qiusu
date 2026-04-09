package site.zqiusu.test.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class WeiXinApiServiceTest {

    @Value("${weixin.config.app_id}")
    private String appId;

    @Value("${weixin.config.app_secret}")
    private String appSecret;

    private String grant_type= "client_credential";

    @Test
    public void test_http(){
        try {
            String urlStr = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=%s&appid=%s&secret=%s",grant_type,appId,appSecret);
            // 创建URL对象
            URL url = new URL(urlStr);
            // 打开连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置请求方法
            conn.setRequestMethod("GET");
            // 获取响应码
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // 读取响应
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // 打印响应
            System.out.println(response.toString());

            // 断开连接
            conn.disconnect();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }


}
