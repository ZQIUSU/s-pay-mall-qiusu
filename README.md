# s-pay-mall

基于 Spring Boot 的支付商城示例项目：商品下单、支付宝沙箱支付、微信公众号扫码登录、订单状态流转、Kafka 消息与定时任务（未支付提醒 / 超时关单）、支付成功后的微信模板消息通知。

## 功能概览

- 商品购物车下单与支付单创建  
- 支付宝沙箱：下单、异步回调验签  
- 微信公众号：扫码登录、模板消息  
- 订单生命周期：创建 → 待支付 → 支付成功 → 完成 / 超时关单  
- Kafka 生产与消费  
- 定时任务：未支付订单通知、超时自动关单  

## 技术栈

| 类别 | 技术 |
|------|------|
| 运行时 | Java 8 |
| 框架 | Spring Boot 2.7.12 |
| 持久化 | MyBatis、MySQL 8.x |
| 消息 | Spring Kafka |
| HTTP 客户端 | Retrofit2 |
| 支付 | 支付宝沙箱 SDK |
| 其他 | Lombok、Fastjson2、JJWT 等 |

## 模块结构

```
s-pay-mall-zqiusu/          # 父工程
├── s-pay-mall-common/    # 通用常量、异常、统一响应、微信工具
├── s-pay-mall-domain/    # 领域模型（PO / Req / Res / VO）
├── s-pay-mall-dao/       # MyBatis 数据访问
├── s-pay-mall-service/   # 业务与外部 RPC / 微信 API
└── s-pay-mall-controller/  # 启动入口、REST、定时任务、MQ 监听
```

依赖关系：`common` ← `domain` ← `dao` ← `service` ← `controller`。

## 本地运行要点

1. **JDK**：Java 8  
2. **构建**：`mvn clean package`（可按需跳过测试，见父 `pom` 配置）  
3. **配置**：开发环境见 `s-pay-mall-controller` 下的 `application-dev.yml`（数据库、支付宝、微信、Kafka 等需按环境填写）  
4. **默认端口**：`8080`  

## 许可证

Apache License 2.0

## 作者

[zqiusu](https://github.com/ZQIUSU)
