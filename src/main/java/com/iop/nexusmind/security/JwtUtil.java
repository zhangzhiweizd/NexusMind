package com.iop.nexusmind.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration-ms:86400000}")
    private long jwtExpirationMs;

    @Value("${jwt.refresh-expiration-ms:604800000}")
    private long refreshExpirationMs;

    private SecretKey key;

    /**
     * 初始化方法，将Base64编码的密钥字符串转换为SecretKey对象
     */
    @PostConstruct
    public void init() {
        this.key = (SecretKey) Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    /**
     * 生成访问令牌（Access Token）
     * @param userDetails 用户详情信息
     * @return JWT访问令牌
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * 生成访问令牌（带额外声明）
     * @param extraClaims 额外的声明信息
     * @param userDetails 用户详情信息
     * @return JWT访问令牌
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    /**
     * 生成刷新令牌（Refresh Token）
     * @param userDetails 用户详情信息
     * @return JWT刷新令牌
     */
    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .signWith(key)
                .compact();
    }

    /**
     * 从JWT令牌中提取用户名
     * @param token JWT令牌
     * @return 用户名
     */
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * 验证JWT令牌是否有效
     * @param token JWT令牌
     * @param userDetails 用户详情信息
     * @return 令牌是否有效
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * 检查令牌是否已过期
     * @param token JWT令牌
     * @return 是否已过期
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 从令牌中提取过期时间
     * @param token JWT令牌
     * @return 过期时间
     */
    private Date extractExpiration(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    /**
     * 验证JWT令牌的完整性和有效性
     * 捕获各种JWT异常并返回验证结果
     * @param token JWT令牌
     * @return 令牌是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT token: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT token: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }
}
