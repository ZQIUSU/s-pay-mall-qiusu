package site.zqiusu.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    private JwtUtil() {
    }

    public static String generateToken(String userId, String phone, String nickname, String secretKey, long expireMinutes) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expireMinutes * 60 * 1000);
        return Jwts.builder()
                .setSubject(userId)
                .claim("userId", userId)
                .claim("phone", phone)
                .claim("nickname", nickname)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public static Claims parseToken(String token, String secretKey) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getUserIdFromToken(String token, String secretKey) {
        Claims claims = parseToken(token, secretKey);
        return claims.get("userId", String.class);
    }
}
