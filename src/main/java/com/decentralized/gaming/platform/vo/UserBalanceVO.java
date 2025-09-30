package com.decentralized.gaming.platform.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户余额VO
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class UserBalanceVO {
    
    /**
     * 余额ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 代币类型
     */
    private String tokenType;
    
    /**
     * 代币符号
     */
    private String tokenSymbol;
    
    /**
     * 余额
     */
    private BigDecimal balance;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}