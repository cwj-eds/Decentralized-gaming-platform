package com.decentralized.gaming.platform.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 资产管理面板VO
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class AssetDashboardVO {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 钱包地址
     */
    private String walletAddress;
    
    /**
     * 总资产价值
     */
    private BigDecimal totalAssetValue;
    
    /**
     * 代币余额列表
     */
    private List<UserBalanceVO> balances;
    
    /**
     * 我的游戏数量
     */
    private Integer gameCount;
    
    /**
     * 我的智能体数量
     */
    private Integer agentCount;
    
    /**
     * 我的道具数量
     */
    private Integer itemCount;
    
    /**
     * 最近获得的资产
     */
    private List<UserAssetVO> recentAssets;
}

