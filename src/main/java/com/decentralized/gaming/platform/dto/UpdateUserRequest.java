package com.decentralized.gaming.platform.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * 更新用户信息请求DTO
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class UpdateUserRequest {

    /**
     * 用户名
     */
    @Size(max = 50, message = "用户名长度不能超过50个字符")
    private String username;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    /**
     * 头像URL
     */
    @Size(max = 255, message = "头像URL长度不能超过255个字符")
    private String avatarUrl;

    /**
     * 密码（可选，用于修改密码）
     */
    @Size(min = 6, max = 255, message = "密码长度必须在6-255个字符之间")
    private String password;
}
