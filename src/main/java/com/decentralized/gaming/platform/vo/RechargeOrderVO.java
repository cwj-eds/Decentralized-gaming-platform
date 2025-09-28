package com.decentralized.gaming.platform.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充值订单视图对象
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class RechargeOrderVO {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 充值金额
     */
    private BigDecimal amount;

    /**
     * 货币类型
     */
    private String currency;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 支付URL
     */
    private String paymentUrl;

    /**
     * 支付二维码
     */
    private String paymentQrCode;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
