package com.decentralized.gaming.platform.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 确认支付请求DTO
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class ConfirmPaymentRequest {

    /**
     * 第三方支付ID
     */
    @NotBlank(message = "支付ID不能为空")
    private String paymentId;

    /**
     * 交易哈希
     */
    private String txHash;

    /**
     * 支付状态
     */
    @NotBlank(message = "支付状态不能为空")
    private String paymentStatus;
}
