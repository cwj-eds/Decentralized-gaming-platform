package com.decentralized.gaming.platform.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * 创建充值订单请求DTO
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class CreateRechargeRequest {

    /**
     * 充值金额
     */
    @NotNull(message = "充值金额不能为空")
    @Positive(message = "充值金额必须大于0")
    private BigDecimal amount;

    /**
     * 货币类型
     */
    @NotNull(message = "货币类型不能为空")
    private String currency;

    /**
     * 支付方式
     */
    @NotNull(message = "支付方式不能为空")
    private String paymentMethod;
}
