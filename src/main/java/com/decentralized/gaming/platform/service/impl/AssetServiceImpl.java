package com.decentralized.gaming.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.decentralized.gaming.platform.common.PageResult;
import com.decentralized.gaming.platform.entity.*;
import com.decentralized.gaming.platform.mapper.*;
import com.decentralized.gaming.platform.service.AssetService;
import com.decentralized.gaming.platform.vo.AssetDashboardVO;
import com.decentralized.gaming.platform.vo.UserAssetVO;
import com.decentralized.gaming.platform.vo.UserBalanceVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 资产管理服务实现类
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {
    
    private final UserAssetMapper userAssetMapper;
    private final UserBalanceMapper userBalanceMapper;
    private final UserMapper userMapper;
    private final GameMapper gameMapper;
    private final AgentMapper agentMapper;
    private final GameItemMapper gameItemMapper;
    
    @Override
    public AssetDashboardVO getAssetDashboard(Long userId) {
        log.info("获取用户资产管理面板，用户ID: {}", userId);
        
        AssetDashboardVO dashboard = new AssetDashboardVO();
        
        // 获取用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            log.warn("用户不存在，用户ID: {}", userId);
            // 返回一个空的dashboard对象，而不是null
            dashboard.setUserId(userId);
            dashboard.setUsername("未知用户");
            dashboard.setWalletAddress("");
            dashboard.setTotalAssetValue(BigDecimal.ZERO);
            dashboard.setGameCount(0);
            dashboard.setAgentCount(0);
            dashboard.setItemCount(0);
            dashboard.setBalances(new ArrayList<>());
            dashboard.setRecentAssets(new ArrayList<>());
            return dashboard;
        }
        
        dashboard.setUserId(userId);
        dashboard.setUsername(user.getUsername());
        dashboard.setWalletAddress(user.getWalletAddress());
        
        // 获取用户余额
        List<UserBalanceVO> balances = getUserBalances(userId);
        dashboard.setBalances(balances);
        
        // 计算总资产价值（这里简化处理，实际应该根据市场价格计算）
        BigDecimal totalValue = balances.stream()
                .map(UserBalanceVO::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dashboard.setTotalAssetValue(totalValue);
        
        // 获取资产统计
        AssetStatistics stats = getAssetStatistics(userId);
        dashboard.setGameCount(stats.getGameCount());
        dashboard.setAgentCount(stats.getAgentCount());
        dashboard.setItemCount(stats.getItemCount());
        
        // 获取最近获得的资产（最近5个）
        List<UserAssetVO> recentAssets = getRecentAssets(userId, 5);
        dashboard.setRecentAssets(recentAssets);
        
        return dashboard;
    }
    
    @Override
    public PageResult<UserAssetVO> getUserAssets(Long userId, UserAsset.AssetType assetType, int page, int size) {
        log.info("获取用户资产，用户ID: {}, 资产类型: {}, 页码: {}, 大小: {}", userId, assetType, page, size);
        
        Page<UserAsset> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<UserAsset> queryWrapper = new LambdaQueryWrapper<UserAsset>()
                .eq(UserAsset::getUserId, userId)
                .orderByDesc(UserAsset::getCreatedAt);
        
        if (assetType != null) {
            queryWrapper.eq(UserAsset::getAssetType, assetType);
        }
        
        IPage<UserAsset> assetPage = userAssetMapper.selectPage(pageParam, queryWrapper);
        
        List<UserAssetVO> assetVOs = assetPage.getRecords().stream()
                .map(this::convertToUserAssetVO)
                .collect(Collectors.toList());
        
        return PageResult.of(assetVOs, assetPage.getTotal(), (long) page, (long) size);
    }
    
    @Override
    public PageResult<UserAssetVO> getUserGames(Long userId, int page, int size) {
        return getUserAssets(userId, UserAsset.AssetType.GAME, page, size);
    }
    
    @Override
    public PageResult<UserAssetVO> getUserAgents(Long userId, int page, int size) {
        return getUserAssets(userId, UserAsset.AssetType.AGENT, page, size);
    }
    
    @Override
    public PageResult<UserAssetVO> getUserItems(Long userId, int page, int size) {
        return getUserAssets(userId, UserAsset.AssetType.GAME_ITEM, page, size);
    }
    
    @Override
    public List<UserBalanceVO> getUserBalances(Long userId) {
        log.info("获取用户代币余额，用户ID: {}", userId);
        
        LambdaQueryWrapper<UserBalance> queryWrapper = new LambdaQueryWrapper<UserBalance>()
                .eq(UserBalance::getUserId, userId)
                .orderByDesc(UserBalance::getUpdatedAt);
        
        List<UserBalance> balances = userBalanceMapper.selectList(queryWrapper);
        
        return balances.stream()
                .map(this::convertToUserBalanceVO)
                .collect(Collectors.toList());
    }
    
    @Override
    public UserBalanceVO getUserBalance(Long userId, String tokenType) {
        log.info("获取用户指定代币余额，用户ID: {}, 代币类型: {}", userId, tokenType);
        
        LambdaQueryWrapper<UserBalance> queryWrapper = new LambdaQueryWrapper<UserBalance>()
                .eq(UserBalance::getUserId, userId)
                .eq(UserBalance::getTokenType, tokenType);
        
        UserBalance balance = userBalanceMapper.selectOne(queryWrapper);
        
        if (balance == null) {
            // 如果不存在，创建默认余额
            balance = new UserBalance();
            balance.setUserId(userId);
            balance.setTokenType(tokenType);
            balance.setBalance(BigDecimal.ZERO);
            userBalanceMapper.insert(balance);
        }
        
        return convertToUserBalanceVO(balance);
    }
    
    @Override
    @Transactional
    public boolean addUserAsset(Long userId, UserAsset.AssetType assetType, Long assetId, 
                               UserAsset.AcquisitionType acquisitionType, String contractAddress, String tokenId) {
        log.info("添加用户资产，用户ID: {}, 资产类型: {}, 资产ID: {}, 获得方式: {}", 
                userId, assetType, assetId, acquisitionType);
        
        // 检查是否已存在
        LambdaQueryWrapper<UserAsset> queryWrapper = new LambdaQueryWrapper<UserAsset>()
                .eq(UserAsset::getUserId, userId)
                .eq(UserAsset::getAssetType, assetType)
                .eq(UserAsset::getAssetId, assetId);
        
        UserAsset existingAsset = userAssetMapper.selectOne(queryWrapper);
        if (existingAsset != null) {
            log.warn("用户资产已存在，用户ID: {}, 资产类型: {}, 资产ID: {}", userId, assetType, assetId);
            return false;
        }
        
        UserAsset userAsset = new UserAsset();
        userAsset.setUserId(userId);
        userAsset.setAssetType(assetType);
        userAsset.setAssetId(assetId);
        userAsset.setAcquisitionType(acquisitionType);
        userAsset.setContractAddress(contractAddress);
        userAsset.setTokenId(tokenId);
        userAsset.setIsTradeable(true);
        
        int result = userAssetMapper.insert(userAsset);
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean removeUserAsset(Long userId, UserAsset.AssetType assetType, Long assetId) {
        log.info("移除用户资产，用户ID: {}, 资产类型: {}, 资产ID: {}", userId, assetType, assetId);
        
        LambdaQueryWrapper<UserAsset> queryWrapper = new LambdaQueryWrapper<UserAsset>()
                .eq(UserAsset::getUserId, userId)
                .eq(UserAsset::getAssetType, assetType)
                .eq(UserAsset::getAssetId, assetId);
        
        int result = userAssetMapper.delete(queryWrapper);
        return result > 0;
    }
    
    @Override
    public boolean hasAsset(Long userId, UserAsset.AssetType assetType, Long assetId) {
        LambdaQueryWrapper<UserAsset> queryWrapper = new LambdaQueryWrapper<UserAsset>()
                .eq(UserAsset::getUserId, userId)
                .eq(UserAsset::getAssetType, assetType)
                .eq(UserAsset::getAssetId, assetId);
        
        UserAsset asset = userAssetMapper.selectOne(queryWrapper);
        return asset != null;
    }
    
    @Override
    @Transactional
    public boolean updateAssetTradeableStatus(Long userId, UserAsset.AssetType assetType, Long assetId, Boolean isTradeable) {
        log.info("更新资产可交易状态，用户ID: {}, 资产类型: {}, 资产ID: {}, 可交易: {}", 
                userId, assetType, assetId, isTradeable);
        
        LambdaQueryWrapper<UserAsset> queryWrapper = new LambdaQueryWrapper<UserAsset>()
                .eq(UserAsset::getUserId, userId)
                .eq(UserAsset::getAssetType, assetType)
                .eq(UserAsset::getAssetId, assetId);
        
        UserAsset asset = userAssetMapper.selectOne(queryWrapper);
        if (asset == null) {
            log.warn("用户资产不存在，用户ID: {}, 资产类型: {}, 资产ID: {}", userId, assetType, assetId);
            return false;
        }
        
        asset.setIsTradeable(isTradeable);
        int result = userAssetMapper.updateById(asset);
        return result > 0;
    }
    
    @Override
    public AssetStatistics getAssetStatistics(Long userId) {
        log.info("获取用户资产统计，用户ID: {}", userId);
        
        AssetStatistics stats = new AssetStatistics();
        
        // 统计总资产数
        LambdaQueryWrapper<UserAsset> totalQuery = new LambdaQueryWrapper<UserAsset>()
                .eq(UserAsset::getUserId, userId);
        Long totalCount = userAssetMapper.selectCount(totalQuery);
        stats.setTotalAssets(totalCount.intValue());
        
        // 统计游戏数量
        LambdaQueryWrapper<UserAsset> gameQuery = new LambdaQueryWrapper<UserAsset>()
                .eq(UserAsset::getUserId, userId)
                .eq(UserAsset::getAssetType, UserAsset.AssetType.GAME);
        Long gameCount = userAssetMapper.selectCount(gameQuery);
        stats.setGameCount(gameCount.intValue());
        
        // 统计智能体数量
        LambdaQueryWrapper<UserAsset> agentQuery = new LambdaQueryWrapper<UserAsset>()
                .eq(UserAsset::getUserId, userId)
                .eq(UserAsset::getAssetType, UserAsset.AssetType.AGENT);
        Long agentCount = userAssetMapper.selectCount(agentQuery);
        stats.setAgentCount(agentCount.intValue());
        
        // 统计道具数量
        LambdaQueryWrapper<UserAsset> itemQuery = new LambdaQueryWrapper<UserAsset>()
                .eq(UserAsset::getUserId, userId)
                .eq(UserAsset::getAssetType, UserAsset.AssetType.GAME_ITEM);
        Long itemCount = userAssetMapper.selectCount(itemQuery);
        stats.setItemCount(itemCount.intValue());
        
        // 统计可交易资产数量
        LambdaQueryWrapper<UserAsset> tradeableQuery = new LambdaQueryWrapper<UserAsset>()
                .eq(UserAsset::getUserId, userId)
                .eq(UserAsset::getIsTradeable, true);
        Long tradeableCount = userAssetMapper.selectCount(tradeableQuery);
        stats.setTradeableCount(tradeableCount.intValue());
        
        return stats;
    }
    
    /**
     * 获取最近获得的资产
     */
    private List<UserAssetVO> getRecentAssets(Long userId, int limit) {
        Page<UserAsset> pageParam = new Page<>(1, limit);
        LambdaQueryWrapper<UserAsset> queryWrapper = new LambdaQueryWrapper<UserAsset>()
                .eq(UserAsset::getUserId, userId)
                .orderByDesc(UserAsset::getCreatedAt);
        
        IPage<UserAsset> assetPage = userAssetMapper.selectPage(pageParam, queryWrapper);
        
        return assetPage.getRecords().stream()
                .map(this::convertToUserAssetVO)
                .collect(Collectors.toList());
    }
    
    /**
     * 转换为UserAssetVO
     */
    private UserAssetVO convertToUserAssetVO(UserAsset userAsset) {
        UserAssetVO vo = new UserAssetVO();
        BeanUtils.copyProperties(userAsset, vo);
        
        // 根据资产类型获取资产名称和描述
        String assetName = "";
        String assetDescription = "";
        
        try {
            switch (userAsset.getAssetType()) {
                case GAME:
                    Game game = gameMapper.selectById(userAsset.getAssetId());
                    if (game != null) {
                        assetName = game.getTitle();
                        assetDescription = game.getDescription();
                    }
                    break;
                case AGENT:
                    Agent agent = agentMapper.selectById(userAsset.getAssetId());
                    if (agent != null) {
                        assetName = agent.getName();
                        assetDescription = agent.getDescription();
                    }
                    break;
                case GAME_ITEM:
                    GameItem item = gameItemMapper.selectById(userAsset.getAssetId());
                    if (item != null) {
                        assetName = item.getItemName();
                        assetDescription = item.getItemType();
                    }
                    break;
            }
        } catch (Exception e) {
            log.warn("获取资产信息失败，资产类型: {}, 资产ID: {}, 错误: {}", 
                    userAsset.getAssetType(), userAsset.getAssetId(), e.getMessage());
        }
        
        vo.setAssetName(assetName);
        vo.setAssetDescription(assetDescription);
        
        return vo;
    }
    
    /**
     * 转换为UserBalanceVO
     */
    private UserBalanceVO convertToUserBalanceVO(UserBalance userBalance) {
        UserBalanceVO vo = new UserBalanceVO();
        BeanUtils.copyProperties(userBalance, vo);
        
        // 设置代币符号
        String tokenSymbol = "PLT"; // 默认平台代币
        if ("PLATFORM_TOKEN".equals(userBalance.getTokenType())) {
            tokenSymbol = "PLT";
        } else if ("ETH".equals(userBalance.getTokenType())) {
            tokenSymbol = "ETH";
        } else if ("USDT".equals(userBalance.getTokenType())) {
            tokenSymbol = "USDT";
        }
        vo.setTokenSymbol(tokenSymbol);
        
        return vo;
    }
}
