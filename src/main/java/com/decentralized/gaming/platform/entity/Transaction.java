package com.decentralized.gaming.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易记录实体类
 *
 * @author DecentralizedGamingPlatform
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("transactions")
public class Transaction {

    /**
     * 交易ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 买家ID
     */
    @TableField("buyer_id")
    private Long buyerId;

    /**
     * 卖家ID
     */
    @TableField("seller_id")
    private Long sellerId;

    /**
     * 市场商品ID
     */
    @TableField("marketplace_item_id")
    private Long marketplaceItemId;

    /**
     * 交易金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 货币类型
     */
    @TableField("currency")
    private String currency;

    /**
     * 区块链交易哈希
     */
    @TableField("tx_hash")
    private String txHash;

    /**
     * 交易状态
     */
    @TableField("status")
    private TransactionStatus status;

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
     * 交易状态枚举
     */
    public enum TransactionStatus {
        PENDING("待处理"),
        COMPLETED("已完成"),
        FAILED("失败");

        private final String description;

        TransactionStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
