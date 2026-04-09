package site.zqiusu.interceptor;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import site.zqiusu.common.constants.Constants;
import site.zqiusu.common.response.Response;
import site.zqiusu.common.util.JwtUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expire-minutes}")
    private long expireMinutes;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // OPTIONS 预检请求直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 1. 获取 Authorization header
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isBlank(authorization) || !authorization.startsWith("Bearer ")) {
            writeUnauthorizedResponse(response);
            return false;
        }

        String token = authorization.substring(7);

        // 2. 解析 JWT
        Claims claims;
        try {
            claims = JwtUtil.parseToken(token, secretKey);
        } catch (Exception e) {
            log.warn("JWT 解析失败: {}", e.getMessage());
            writeUnauthorizedResponse(response);
            return false;
        }

        // 3. 提取 userId 并校验 Redis
        String userId = claims.get("userId", String.class);
        if (StringUtils.isBlank(userId)) {
            writeUnauthorizedResponse(response);
            return false;
        }

        RBucket<String> tokenBucket = redissonClient.getBucket(Constants.LOGIN_TOKEN_KEY + userId);
        String storedTokenInfo = tokenBucket.get();
        if (StringUtils.isBlank(storedTokenInfo)) {
            writeUnauthorizedResponse(response);
            return false;
        }

        // 4. 校验 token 是否匹配（防止并发登录后旧 token 复用）
        if (!storedTokenInfo.contains(token)) {
            writeUnauthorizedResponse(response);
            return false;
        }

        // 5. 自动续期：剩余 TTL < 30 分钟时续期
        long remainTtl = tokenBucket.remainTimeToLive();
        if (remainTtl > 0 && remainTtl < 30 * 60 * 1000) {
            tokenBucket.expire(expireMinutes, TimeUnit.MINUTES);
            log.debug("Token 自动续期 userId:{}", userId);
        }

        // 6. 将 userId 设置到 request 属性，供下游使用
        request.setAttribute("userId", userId);
        return true;
    }

    private void writeUnauthorizedResponse(HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=utf-8");
        Response<Void> result = Response.<Void>builder()
                .code(Constants.ResponseCode.TOKEN_INVALID.getCode())
                .info(Constants.ResponseCode.TOKEN_INVALID.getInfo())
                .build();
        response.getWriter().write(JSON.toJSONString(result));
    }
}
