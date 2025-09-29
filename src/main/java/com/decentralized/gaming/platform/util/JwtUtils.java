package com.decentralized.gaming.platform.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT工具类
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Component
public class JwtUtils {

    @Value("${app.jwt.secret:mySecretKey}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:86400}")
    private Long jwtExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * 生成JWT令牌
     *
     * @param userId 用户ID
     * @param walletAddress 钱包地址
     * @return JWT令牌
     */
    public String generateToken(Long userId, String walletAddress) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration * 1000);

        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("walletAddress", walletAddress)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 从令牌中获取用户ID
     *
     * @param token JWT令牌
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    /**
     * 从令牌中获取钱包地址
     *
     * @param token JWT令牌
     * @return 钱包地址
     */
    public String getWalletAddressFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("walletAddress", String.class);
    }

    /**
     * 验证令牌
     *
     * @param token JWT令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT令牌验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 检查令牌是否过期
     *
     * @param token JWT令牌
     * @return 是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 获取令牌过期时间
     *
     * @return 过期时间（秒）
     */
    public Long getExpirationTime() {
        return jwtExpiration;
    }
}
