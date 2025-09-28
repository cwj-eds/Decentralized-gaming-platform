package com.decentralized.gaming.platform.vo;

import com.decentralized.gaming.platform.entity.Transaction;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易记录视图对象
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class TransactionVO {

    /**
     * 交易ID
     */
    private Long id;

    /**
     * 买家ID
     */
    private Long buyerId;

    /**
     * 买家用户名
     */
    private String buyerName;

    /**
     * 卖家ID
     */
    private Long sellerId;

    /**
     * 卖家用户名
     */
    private String sellerName;

    /**
     * 市场商品ID
     */
    private Long marketplaceItemId;

    /**
     * 商品名称
     */
    private String itemName;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 货币类型
     */
    private String currency;

    /**
     * 区块链交易哈希
     */
    private String txHash;

    /**
     * 交易状态
     */
    private Transaction.TransactionStatus status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
