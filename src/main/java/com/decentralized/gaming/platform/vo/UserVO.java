package com.decentralized.gaming.platform.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户视图对象
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class UserVO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 钱包地址
     */
    private String walletAddress;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
