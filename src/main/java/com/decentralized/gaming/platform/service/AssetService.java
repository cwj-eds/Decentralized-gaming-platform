package com.decentralized.gaming.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.decentralized.gaming.platform.common.PageResult;
import com.decentralized.gaming.platform.entity.UserAsset;
import com.decentralized.gaming.platform.vo.AssetDashboardVO;
import com.decentralized.gaming.platform.vo.UserAssetVO;
import com.decentralized.gaming.platform.vo.UserBalanceVO;

import java.util.List;

/**
 * 资产管理服务接口
 *
 * @author DecentralizedGamingPlatform
 */
public interface AssetService {
    
    /**
     * 获取用户资产管理面板
     *
     * @param userId 用户ID
     * @return 资产管理面板信息
     */
    AssetDashboardVO getAssetDashboard(Long userId);
    
    /**
     * 获取用户所有资产
     *
     * @param userId 用户ID
     * @param assetType 资产类型（可选）
     * @param page 页码
     * @param size 每页大小
     * @return 用户资产分页结果
     */
    PageResult<UserAssetVO> getUserAssets(Long userId, UserAsset.AssetType assetType, int page, int size);
    
    /**
     * 获取用户游戏资产
     *
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 用户游戏资产分页结果
     */
    PageResult<UserAssetVO> getUserGames(Long userId, int page, int size);
    
    /**
     * 获取用户智能体资产
     *
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 用户智能体资产分页结果
     */
    PageResult<UserAssetVO> getUserAgents(Long userId, int page, int size);
    
    /**
     * 获取用户道具资产
     *
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 用户道具资产分页结果
     */
    PageResult<UserAssetVO> getUserItems(Long userId, int page, int size);
    
    /**
     * 获取用户代币余额
     *
     * @param userId 用户ID
     * @return 用户代币余额列表
     */
    List<UserBalanceVO> getUserBalances(Long userId);
    
    /**
     * 获取用户指定代币余额
     *
     * @param userId 用户ID
     * @param tokenType 代币类型
     * @return 用户代币余额
     */
    UserBalanceVO getUserBalance(Long userId, String tokenType);
    
    /**
     * 添加用户资产
     *
     * @param userId 用户ID
     * @param assetType 资产类型
     * @param assetId 资产ID
     * @param acquisitionType 获得方式
     * @param contractAddress 合约地址（可选）
     * @param tokenId Token ID（可选）
     * @return 是否成功
     */
    boolean addUserAsset(Long userId, UserAsset.AssetType assetType, Long assetId, 
                        UserAsset.AcquisitionType acquisitionType, String contractAddress, String tokenId);
    
    /**
     * 移除用户资产
     *
     * @param userId 用户ID
     * @param assetType 资产类型
     * @param assetId 资产ID
     * @return 是否成功
     */
    boolean removeUserAsset(Long userId, UserAsset.AssetType assetType, Long assetId);
    
    /**
     * 检查用户是否拥有指定资产
     *
     * @param userId 用户ID
     * @param assetType 资产类型
     * @param assetId 资产ID
     * @return 是否拥有
     */
    boolean hasAsset(Long userId, UserAsset.AssetType assetType, Long assetId);
    
    /**
     * 更新资产可交易状态
     *
     * @param userId 用户ID
     * @param assetType 资产类型
     * @param assetId 资产ID
     * @param isTradeable 是否可交易
     * @return 是否成功
     */
    boolean updateAssetTradeableStatus(Long userId, UserAsset.AssetType assetType, Long assetId, Boolean isTradeable);
    
    /**
     * 获取用户资产统计
     *
     * @param userId 用户ID
     * @return 资产统计信息
     */
    AssetStatistics getAssetStatistics(Long userId);
    
    /**
     * 资产统计信息
     */
    class AssetStatistics {
        private Integer totalAssets;
        private Integer gameCount;
        private Integer agentCount;
        private Integer itemCount;
        private Integer tradeableCount;
        
        // Getters and Setters
        public Integer getTotalAssets() { return totalAssets; }
        public void setTotalAssets(Integer totalAssets) { this.totalAssets = totalAssets; }
        
        public Integer getGameCount() { return gameCount; }
        public void setGameCount(Integer gameCount) { this.gameCount = gameCount; }
        
        public Integer getAgentCount() { return agentCount; }
        public void setAgentCount(Integer agentCount) { this.agentCount = agentCount; }
        
        public Integer getItemCount() { return itemCount; }
        public void setItemCount(Integer itemCount) { this.itemCount = itemCount; }
        
        public Integer getTradeableCount() { return tradeableCount; }
        public void setTradeableCount(Integer tradeableCount) { this.tradeableCount = tradeableCount; }
    }
}

