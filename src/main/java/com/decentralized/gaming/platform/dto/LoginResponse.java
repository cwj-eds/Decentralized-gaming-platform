package com.decentralized.gaming.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应DTO
 *
 * @author DecentralizedGamingPlatform
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    /**
     * JWT访问令牌
     */
    private String accessToken;

    /**
     * 令牌类型
     */
    private String tokenType = "Bearer";

    /**
     * 过期时间（秒）
     */
    private Long expiresIn;

    /**
     * 用户信息
     */
    private Object user;
}
