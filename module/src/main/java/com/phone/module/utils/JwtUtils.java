package com.phone.module.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    /** token 默认过期时间：30天，单位毫秒 */
    private static final long TOKEN_EXPIRED_TIME = 30L * 24 * 60 * 60 * 1000;

    public static final String JWT_ID = "apitokenfor1000coins";

    /** JWT 秘钥 */
    private static final String SECRET = "*WRt^%4CZ@%hyYe&FKEmtuFOa^@$&B!0pYz9JlG2GUlcNbHi&XqbvhIUhfY%9r796zYzfu2JaOWZGnN0HxR32PD4hO0X3EeoWk6NfZwtDm0PvmuAJz2vidM7JTXFPE6tDdqUDl9/+4MTsmKlJSs";

    /** SecretKey 对象（Java 8 + JJWT 0.9.x 使用） */
    private static final SecretKey SECRET_KEY = new SecretKeySpec(
            SECRET.getBytes(StandardCharsets.UTF_8),
            SignatureAlgorithm.HS512.getJcaName()
    );

    /**
     * 创建 JWT
     * @param claims 自定义 payload
     * @param expireMillis 过期时间（毫秒），传 null 使用默认
     * @return token 字符串
     */
    public static String createJWT(Map<String, Object> claims, Long expireMillis) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = (expireMillis != null) ? nowMillis + expireMillis : nowMillis + TOKEN_EXPIRED_TIME;

        Date now = new Date(nowMillis);
        Date exp = new Date(expMillis);

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId(JWT_ID)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY);

        return builder.compact();
    }

    /**
     * 验证 JWT 并返回 Claims
     * @param token JWT 字符串
     * @return Claims 或 null
     */
    public static Claims verifyJwt(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /** 测试示例 */
    public static void main(String[] args) {
        // Java 8 创建 Map
        Map<String, Object> payload = new HashMap<>();
        payload.put("status", "success");

        // 生成 token
        String token = createJWT(payload, null);
        System.out.println("生成的 Token: " + token);

        // 验证 token
        Claims claims = verifyJwt(token);
        if (claims != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            System.out.println("status: " + claims.get("status"));
            System.out.println("iat: " + sdf.format(claims.getIssuedAt()));
            System.out.println("exp: " + sdf.format(claims.getExpiration()));
        } else {
            System.out.println("Token 无效或已过期");
        }
    }
}
