package com.decentralized.gaming.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充值记录实体类
 *
 * @author DecentralizedGamingPlatform
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("recharge_records")
public class RechargeRecord {

    /**
     * 充值记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 充值金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 货币类型
     */
    @TableField("currency")
    private String currency;

    /**
     * 支付方式
     */
    @TableField("payment_method")
    private String paymentMethod;

    /**
     * 第三方支付ID
     */
    @TableField("payment_id")
    private String paymentId;

    /**
     * 区块链交易哈希
     */
    @TableField("tx_hash")
    private String txHash;

    /**
     * 充值状态
     */
    @TableField("status")
    private RechargeStatus status;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 充值状态枚举
     */
    public enum RechargeStatus {
        PENDING("待处理"),
        COMPLETED("已完成"),
        FAILED("失败"),
        CANCELLED("已取消");

        private final String description;

        RechargeStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
