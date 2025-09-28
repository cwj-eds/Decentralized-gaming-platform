package com.decentralized.gaming.platform.vo;

import com.decentralized.gaming.platform.entity.RechargeRecord;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充值记录视图对象
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class RechargeRecordVO {

    /**
     * 充值记录ID
     */
    private Long id;

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
     * 第三方支付ID
     */
    private String paymentId;

    /**
     * 区块链交易哈希
     */
    private String txHash;

    /**
     * 充值状态
     */
    private RechargeRecord.RechargeStatus status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
