# AGENTS.md - 项目知识库

> 本文件供 AI Agent 快速了解项目全貌，避免上下文丢失。每次开发前应先读取此文件。

## 1. 项目简介

s-pay-mall 是一个**支付商城系统**，核心功能包括：

- 商品下单与支付单创建
- 支付宝沙箱支付对接（下单、异步回调验签）
- 微信公众号扫码登录
- 订单生命周期管理（创建 → 待支付 → 支付成功 → 交易完成 → 超时关单）
- 未支付订单定时通知与超时自动关单（定时任务）
- Kafka 消息队列（生产/消费）
- 支付成功后发送微信模板消息通知

项目作者：zqiusu
License：Apache 2.0

## 2. 技术栈

| 类别 | 技术 | 版本 |
|------|------|------|
| 语言 | Java | 1.8 |
| 框架 | Spring Boot | 2.7.12 |
| ORM | MyBatis | 2.1.4 (starter) |
| 数据库 | MySQL | 8.x (connector 8.0.23) |
| 缓存 | Redisson | 3.26.0 (已引入但当前注释未启用) |
| 消息队列 | Spring Kafka | 2.8.1 |
| HTTP 客户端 | Retrofit2 | 2.9.0 |
| 支付 | 支付宝沙箱 SDK | 4.38.157.ALL |
| 认证 | JJWT + java-jwt | 0.9.1 / 4.4.0 |
| 序列化 | Fastjson2 | 2.0.28 |
| 工具 | Lombok, Guava, commons-lang3, commons-codec | - |
| XML 处理 | dom4j + XStream | 1.6.1 / 1.4.10 |
| 代码质量 | Qodana (CI) | 2025.2 |
| 构建 | Maven | - |

## 3. 模块结构

```
s-pay-mall-zqiusu/                    # 父工程 (pom)
├── s-pay-mall-common/                # 通用模块 (jar)
│   └── site.zqiusu.common
│       ├── constants/Constants       # 响应码枚举、订单状态枚举
│       ├── exception/AppException    # 自定义业务异常
│       ├── response/Response<T>      # 统一响应体 (code/info/data)
│       └── weixin/                   # 微信工具类(签名/消息/XML)
│
├── s-pay-mall-domain/                # 领域模型 (jar) → 依赖 common
│   └── site.zqiusu.domain
│       ├── po/PayOrder               # 订单持久化对象
│       ├── req/                      # 请求对象 (ShopCartReq, WeixinQrCodeReq)
│       ├── res/                      # 响应对象 (PayOrderRes, WeixinTokenRes, WeixinQrCodeRes)
│       └── vo/                       # 值对象 (ProductVO, WeixinTemplateMessageVO)
│
├── s-pay-mall-dao/                   # 数据访问层 (jar) → 依赖 domain
│   └── site.zqiusu.dao
│       └── IOrderDao                 # @Mapper 订单 DAO 接口
│
├── s-pay-mall-service/               # 业务逻辑层 (jar) → 依赖 domain + dao
│   └── site.zqiusu.service
│       ├── IOrderService             # 订单服务接口
│       ├── ILoginService             # 登录服务接口
│       ├── impl/
│       │   ├── OrderServiceImpl      # 订单服务实现
│       │   └── WeixinLoginServiceImpl # 微信登录实现
│       ├── rpc/ProductRPC            # 商品 RPC (Retrofit2)
│       └── weixin/IWeixinApiService  # 微信 API 服务接口
│
├── s-pay-mall-controller/            # 启动模块 (jar) → 依赖 service
│   └── site.zqiusu
│       ├── Application               # @SpringBootApplication 启动类
│       ├── config/                   # 配置类 (AliPay, GuavaCache, Retrofit2)
│       ├── controller/               # REST 控制器
│       │   ├── AliPayController      # 支付宝支付 (/api/v1/alipay/)
│       │   ├── LoginController       # 登录 (/api/v1/login/)
│       │   └── WeixinPortalController # 微信入口
│       ├── dto/                      # 请求 DTO
│       ├── job/                      # 定时任务
│       │   ├── NoPayNotifyOrderJob   # 未支付订单通知
│       │   └── TimeoutCloseOrderJob  # 超时关单
│       ├── listener/
│       │   └── OrderPaySuccessListener # 支付成功事件监听
│       └── mq/                       # 消息队列
│           ├── producer/KafkaProducer  # Kafka 生产者 (Topic: Hello-Kafka)
│           └── consumer/KafkaConsumer  # Kafka 消费者
│
└── docs/dev-ops/                     # 运维配置
    ├── docker-compose-environment-aliyun.yml
    ├── natapp/                       # 内网穿透
    ├── nginx/html/                   # 前端页面
    └── mysql/sql/nacos.sql           # Nacos 初始化 SQL
```

### 模块依赖关系

```
common ← domain ← dao ← service ← controller (启动入口)
```

## 4. 编码规范

### 包名与命名

- 根包名：`site.zqiusu`（已从 `org.example` 迁移完成）
- 接口命名：`I` 前缀，如 `IOrderService`、`IOrderDao`
- 实现类命名：接口名 + `Impl`，如 `OrderServiceImpl`
- DTO 命名：以 `DTO` 后缀结尾，如 `CreatePayRequestDTO`
- 请求/响应对象：`XxxReq` / `XxxRes`
- 持久化对象：`XxxPO`，放在 `po` 包下
- 值对象：`XxxVO`，放在 `vo` 包下
- 枚举常量：内部枚举类方式，如 `Constants.ResponseCode`、`Constants.OrderStatusEnum`

### 代码风格

- 使用 Lombok（`@Data`, `@Builder`, `@AllArgsConstructor`, `@NoArgsConstructor`, `@Slf4j`）
- 统一响应体：`Response<T>`，包含 `code`（String）、`info`（String）、`data`（T 泛型）
- 响应码：`Constants.ResponseCode` 枚举（`0000` 成功、`0001` 失败、`0002` 非法参数、`0003` 未登录）
- 自定义异常：`AppException`，携带 `code` 和 `info`
- Controller 层使用 `@RestController` + `@CrossOrigin("*")`
- API 路径格式：`/api/v1/{模块}/`，如 `/api/v1/alipay/`
- 日志使用 SLF4J（`@Slf4j` 或 `LoggerFactory.getLogger()`）
- 依赖注入使用 `@Resource`（非 `@Autowired`）

### 数据库

- MySQL 8.x，驱动 `com.mysql.cj.jdbc.Driver`
- MyBatis XML 映射文件：`src/main/resources/mybatis/mapper/*.xml`
- MyBatis 配置文件：`src/main/resources/mybatis/config/mybatis-config.xml`
- 数据库名：`s-pay-mall`

### 配置

- 主配置文件：`application-dev.yml`（开发环境）
- 服务端口：8080
- 支付宝配置前缀：`alipay.*`
- 微信配置前缀：`weixin.config.*`
- Kafka 配置前缀：`spring.kafka.*`

### 构建与部署

- 构建工具：Maven
- 打包名：`s-pay-mall-app`
- 跳过测试：`maven-surefire-plugin` 配置 `skipTests=true`
- 代码质量：Qodana CI（GitHub Actions），配置文件 `qodana.yaml`
