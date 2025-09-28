package com.decentralized.gaming.platform.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 钱包登录请求DTO
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class WalletLoginRequest {

    /**
     * 钱包地址
     */
    @NotBlank(message = "钱包地址不能为空")
    @Pattern(regexp = "^0x[a-fA-F0-9]{40}$", message = "钱包地址格式不正确")
    private String walletAddress;

    /**
     * 签名消息
     */
    @NotBlank(message = "签名消息不能为空")
    private String signature;

    /**
     * 原始消息
     */
    @NotBlank(message = "原始消息不能为空")
    private String message;

    /**
     * 用户名（可选）
     */
    private String username;

    /**
     * 邮箱（可选）
     */
    private String email;
}
