package com.decentralized.gaming.platform.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 用于生成、验证和解析JWT令牌
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Component
public class JwtUtils {

    @Value("${app.jwt.secret:decentralized-gaming-platform-secret-key-2024}")
    private String secret;

    @Value("${app.jwt.expiration:86400000}") // 24小时
    private Long expiration;

    /**
     * 生成JWT令牌
     *
     * @param userId 用户ID
     * @param username 用户名
     * @return JWT令牌
     */
    public String generateToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        return createToken(claims, username);
    }

    /**
     * 创建令牌
     *
     * @param claims 声明
     * @param subject 主题
     * @return JWT令牌
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从令牌中获取用户ID
     *
     * @param token JWT令牌
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Object userIdObj = claims.get("userId");
            if (userIdObj instanceof Integer) {
                return ((Integer) userIdObj).longValue();
            } else if (userIdObj instanceof Long) {
                return (Long) userIdObj;
            }
            return null;
        } catch (Exception e) {
            log.error("从令牌中获取用户ID失败", e);
            return null;
        }
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token JWT令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            log.error("从令牌中获取用户名失败", e);
            return null;
        }
    }

    /**
     * 验证令牌
     *
     * @param token JWT令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return !isTokenExpired(claims);
        } catch (Exception e) {
            log.error("令牌验证失败", e);
            return false;
        }
    }

    /**
     * 验证令牌和用户名
     *
     * @param token JWT令牌
     * @param username 用户名
     * @return 是否有效
     */
    public boolean validateToken(String token, String username) {
        try {
            String tokenUsername = getUsernameFromToken(token);
            return username.equals(tokenUsername) && validateToken(token);
        } catch (Exception e) {
            log.error("令牌和用户名验证失败", e);
            return false;
        }
    }

    /**
     * 从令牌中获取声明
     *
     * @param token JWT令牌
     * @return 声明
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 检查令牌是否过期
     *
     * @param claims 声明
     * @return 是否过期
     */
    private boolean isTokenExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 获取签名密钥
     *
     * @return 签名密钥
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 刷新令牌
     *
     * @param token 原令牌
     * @return 新令牌
     */
    public String refreshToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            String username = claims.getSubject();
            Long userId = getUserIdFromToken(token);
            return generateToken(userId, username);
        } catch (Exception e) {
            log.error("刷新令牌失败", e);
            return null;
        }
    }

    /**
     * 获取令牌过期时间
     *
     * @param token JWT令牌
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getExpiration();
        } catch (Exception e) {
            log.error("获取令牌过期时间失败", e);
            return null;
        }
    }

    /**
     * 检查令牌是否即将过期（剩余时间少于1小时）
     *
     * @param token JWT令牌
     * @return 是否即将过期
     */
    public boolean isTokenExpiringSoon(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            if (expiration == null) {
                return true;
            }
            
            long timeUntilExpiry = expiration.getTime() - System.currentTimeMillis();
            long oneHour = 60 * 60 * 1000; // 1小时
            
            return timeUntilExpiry < oneHour;
        } catch (Exception e) {
            log.error("检查令牌过期状态失败", e);
            return true;
        }
    }
}
