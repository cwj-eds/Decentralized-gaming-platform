package com.decentralized.gaming.platform.controller;

import com.decentralized.gaming.platform.common.PageResult;
import com.decentralized.gaming.platform.common.RequireAuth;
import com.decentralized.gaming.platform.common.Result;
import com.decentralized.gaming.platform.entity.UserAsset;
import com.decentralized.gaming.platform.service.AssetService;
import com.decentralized.gaming.platform.util.UserContext;
import com.decentralized.gaming.platform.vo.AssetDashboardVO;
import com.decentralized.gaming.platform.vo.UserAssetVO;
import com.decentralized.gaming.platform.vo.UserBalanceVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资产管理控制器
 * 所有接口都需要用户登录认证
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Controller
@RequestMapping("/assets")
@RequiredArgsConstructor
@RequireAuth  // 类级别的认证注解，所有方法都需要登录
public class AssetController {
    
    private final AssetService assetService;
    
    /**
     * 资产管理首页
     */
    @GetMapping
    public String index(Model model) {
        log.info("访问资产管理首页");
        
        // 使用UserContext统一获取当前登录用户ID
        Long currentUserId = UserContext.getCurrentUserId();
        
        AssetDashboardVO dashboard = assetService.getAssetDashboard(currentUserId);
        model.addAttribute("dashboard", dashboard);
        
        return "assets/dashboard";
    }
    
    /**
     * 我的游戏页面
     */
    @GetMapping("/games")
    public String myGames(@RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size,
                         Model model) {
        log.info("访问我的游戏页面，页码: {}, 大小: {}", page, size);
        
        Long currentUserId = UserContext.getCurrentUserId();
        
        PageResult<UserAssetVO> games = assetService.getUserGames(currentUserId, page, size);
        
        model.addAttribute("games", games);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        
        return "assets/my-games";
    }
    
    /**
     * 我的智能体页面
     */
    @GetMapping("/agents")
    public String myAgents(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size,
                          Model model) {
        log.info("访问我的智能体页面，页码: {}, 大小: {}", page, size);
        
        Long currentUserId = UserContext.getCurrentUserId();
        
        PageResult<UserAssetVO> agents = assetService.getUserAgents(currentUserId, page, size);
        
        model.addAttribute("agents", agents);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        
        return "assets/my-agents";
    }
    
    /**
     * 我的道具页面
     */
    @GetMapping("/items")
    public String myItems(@RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size,
                         Model model) {
        log.info("访问我的道具页面，页码: {}, 大小: {}", page, size);
        
        Long currentUserId = UserContext.getCurrentUserId();
        
        PageResult<UserAssetVO> items = assetService.getUserItems(currentUserId, page, size);
        
        model.addAttribute("items", items);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        
        return "assets/my-items";
    }
    
    /**
     * 代币管理页面
     */
    @GetMapping("/tokens")
    public String tokens(Model model) {
        log.info("访问代币管理页面");
        
        Long currentUserId = UserContext.getCurrentUserId();
        
        List<UserBalanceVO> balances = assetService.getUserBalances(currentUserId);
        
        model.addAttribute("balances", balances);
        
        return "assets/tokens";
    }
    
    // ========== API接口 ==========
    
    /**
     * 获取资产管理面板数据
     */
    @GetMapping("/api/dashboard")
    @ResponseBody
    public Result<AssetDashboardVO> getDashboard() {
        log.info("获取资产管理面板数据");
        
        Long currentUserId = UserContext.getCurrentUserId();
        
        AssetDashboardVO dashboard = assetService.getAssetDashboard(currentUserId);
        
        return Result.success(dashboard);
    }
    
    /**
     * 获取用户资产列表
     */
    @GetMapping("/api/assets")
    @ResponseBody
    public Result<PageResult<UserAssetVO>> getAssets(
            @RequestParam(required = false) String assetType,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("获取用户资产列表，资产类型: {}, 页码: {}, 大小: {}", assetType, page, size);
        
        Long currentUserId = UserContext.getCurrentUserId();
        
        UserAsset.AssetType type = null;
        if (assetType != null && !assetType.isEmpty()) {
            try {
                type = UserAsset.AssetType.valueOf(assetType.toUpperCase());
            } catch (IllegalArgumentException e) {
                return Result.error("无效的资产类型");
            }
        }
        
        PageResult<UserAssetVO> assets = assetService.getUserAssets(currentUserId, type, page, size);
        
        return Result.success(assets);
    }
    
    /**
     * 获取用户代币余额
     */
    @GetMapping("/api/balances")
    @ResponseBody
    public Result<List<UserBalanceVO>> getBalances() {
        log.info("获取用户代币余额");
        
        Long currentUserId = UserContext.getCurrentUserId();
        
        List<UserBalanceVO> balances = assetService.getUserBalances(currentUserId);
        
        return Result.success(balances);
    }
    
    /**
     * 获取用户指定代币余额
     */
    @GetMapping("/api/balances/{tokenType}")
    @ResponseBody
    public Result<UserBalanceVO> getBalance(@PathVariable String tokenType) {
        log.info("获取用户指定代币余额，代币类型: {}", tokenType);
        
        Long currentUserId = UserContext.getCurrentUserId();
        
        UserBalanceVO balance = assetService.getUserBalance(currentUserId, tokenType);
        
        return Result.success(balance);
    }
    
    /**
     * 添加用户资产
     */
    @PostMapping("/api/assets")
    @ResponseBody
    public Result<Boolean> addAsset(@RequestBody AddAssetRequest request) {
        log.info("添加用户资产，请求: {}", request);
        
        Long currentUserId = UserContext.getCurrentUserId();
        
        boolean success = assetService.addUserAsset(
                currentUserId,
                request.getAssetType(),
                request.getAssetId(),
                request.getAcquisitionType(),
                request.getContractAddress(),
                request.getTokenId()
        );
        
        if (success) {
            return Result.success(true);
        } else {
            return Result.error("添加资产失败");
        }
    }
    
    /**
     * 移除用户资产
     */
    @DeleteMapping("/api/assets/{assetType}/{assetId}")
    @ResponseBody
    public Result<Boolean> removeAsset(@PathVariable String assetType, @PathVariable Long assetId) {
        log.info("移除用户资产，资产类型: {}, 资产ID: {}", assetType, assetId);
        
        Long currentUserId = UserContext.getCurrentUserId();
        
        UserAsset.AssetType type;
        try {
            type = UserAsset.AssetType.valueOf(assetType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Result.error("无效的资产类型");
        }
        
        boolean success = assetService.removeUserAsset(currentUserId, type, assetId);
        
        if (success) {
            return Result.success(true);
        } else {
            return Result.error("移除资产失败");
        }
    }
    
    /**
     * 更新资产可交易状态
     */
    @PutMapping("/api/assets/{assetType}/{assetId}/tradeable")
    @ResponseBody
    public Result<Boolean> updateTradeableStatus(@PathVariable String assetType, 
                                                @PathVariable Long assetId,
                                                @RequestBody UpdateTradeableRequest request) {
        log.info("更新资产可交易状态，资产类型: {}, 资产ID: {}, 可交易: {}", 
                assetType, assetId, request.getIsTradeable());
        
        Long currentUserId = UserContext.getCurrentUserId();
        
        UserAsset.AssetType type;
        try {
            type = UserAsset.AssetType.valueOf(assetType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Result.error("无效的资产类型");
        }
        
        boolean success = assetService.updateAssetTradeableStatus(
                currentUserId, type, assetId, request.getIsTradeable());
        
        if (success) {
            return Result.success(true);
        } else {
            return Result.error("更新资产状态失败");
        }
    }
    
    /**
     * 检查用户是否拥有指定资产
     */
    @GetMapping("/api/assets/{assetType}/{assetId}/has")
    @ResponseBody
    public Result<Boolean> hasAsset(@PathVariable String assetType, @PathVariable Long assetId) {
        log.info("检查用户是否拥有指定资产，资产类型: {}, 资产ID: {}", assetType, assetId);
        
        Long currentUserId = UserContext.getCurrentUserId();
        
        UserAsset.AssetType type;
        try {
            type = UserAsset.AssetType.valueOf(assetType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Result.error("无效的资产类型");
        }
        
        boolean hasAsset = assetService.hasAsset(currentUserId, type, assetId);
        
        return Result.success(hasAsset);
    }
    
    /**
     * 获取资产统计信息
     */
    @GetMapping("/api/statistics")
    @ResponseBody
    public Result<AssetService.AssetStatistics> getStatistics() {
        log.info("获取资产统计信息");
        
        Long currentUserId = UserContext.getCurrentUserId();
        
        AssetService.AssetStatistics statistics = assetService.getAssetStatistics(currentUserId);
        
        return Result.success(statistics);
    }
    
    // ========== 请求DTO ==========
    
    /**
     * 添加资产请求
     */
    public static class AddAssetRequest {
        private UserAsset.AssetType assetType;
        private Long assetId;
        private UserAsset.AcquisitionType acquisitionType;
        private String contractAddress;
        private String tokenId;
        
        // Getters and Setters
        public UserAsset.AssetType getAssetType() { return assetType; }
        public void setAssetType(UserAsset.AssetType assetType) { this.assetType = assetType; }
        
        public Long getAssetId() { return assetId; }
        public void setAssetId(Long assetId) { this.assetId = assetId; }
        
        public UserAsset.AcquisitionType getAcquisitionType() { return acquisitionType; }
        public void setAcquisitionType(UserAsset.AcquisitionType acquisitionType) { this.acquisitionType = acquisitionType; }
        
        public String getContractAddress() { return contractAddress; }
        public void setContractAddress(String contractAddress) { this.contractAddress = contractAddress; }
        
        public String getTokenId() { return tokenId; }
        public void setTokenId(String tokenId) { this.tokenId = tokenId; }
    }
    
    /**
     * 更新可交易状态请求
     */
    public static class UpdateTradeableRequest {
        private Boolean isTradeable;
        
        // Getters and Setters
        public Boolean getIsTradeable() { return isTradeable; }
        public void setIsTradeable(Boolean isTradeable) { this.isTradeable = isTradeable; }
    }
}

