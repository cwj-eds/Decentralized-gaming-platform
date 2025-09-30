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
     * 用户信息
     */
    private Object user;

    /**
     * 登录成功消息
     */
    private String message;

    /**
     * 登录状态
     */
    private boolean success;
}

