package com.decentralized.gaming.platform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 银行转账充值请求
 */
@Data
public class BankRechargeRequest {
    @NotNull
    @Positive
    private BigDecimal amount;

    @NotBlank
    private String currency; // e.g. PLATFORM_TOKEN

    @NotBlank
    private String bankName;

    @NotBlank
    private String accountNo;

    @NotBlank
    private String accountName;

    @NotBlank
    private String referenceNo; // 转账备注/参考号
}


