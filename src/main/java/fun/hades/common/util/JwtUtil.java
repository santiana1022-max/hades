package fun.hades.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类（生成、解析、验证令牌）
 */
@Component
public class JwtUtil {

    // 从配置文件注入参数
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire}")
    private long expire;

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.prefix}")
    private String prefix;

    // 获取签名密钥（安全加密）
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // 1. 生成JWT令牌（核心：传入用户信息，返回带前缀的令牌）
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // 可添加自定义载荷（如用户ID、角色，禁止存敏感信息）
        claims.put("username", userDetails.getUsername());
        return createToken(claims, userDetails.getUsername());
    }

    // 2. 创建令牌（私有方法，封装生成逻辑）
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims) // 载荷
                .setSubject(subject) // 主题（用户名）
                .setIssuedAt(new Date()) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + expire)) // 过期时间
                .signWith(getSignKey(), SignatureAlgorithm.HS256) // 签名算法+密钥
                .compact();
    }

    // 3. 从令牌中提取用户名
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // 4. 提取令牌所有载荷
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token.replace(prefix, "")) // 去掉前缀
                .getBody();
    }

    // 5. 验证令牌有效性（是否过期、签名是否正确、用户名匹配）
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 6. 判断令牌是否过期
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // 7. 获取请求头名称（供过滤器使用）
    public String getHeader() {
        return header;
    }

    // 8. 获取令牌前缀（供过滤器使用）
    public String getPrefix() {
        return prefix;
    }
}
