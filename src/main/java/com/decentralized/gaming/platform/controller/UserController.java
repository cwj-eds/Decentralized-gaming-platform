package com.decentralized.gaming.platform.controller;

import com.decentralized.gaming.platform.common.Result;
import com.decentralized.gaming.platform.dto.LoginResponse;
import com.decentralized.gaming.platform.dto.UserRegisterRequest;
import com.decentralized.gaming.platform.dto.WalletLoginRequest;
import com.decentralized.gaming.platform.service.UserService;
import com.decentralized.gaming.platform.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
@Tag(name = "用户管理API", description = "用户登录注册相关接口")
public class UserController {

    private final UserService userService;

    /**
     * 钱包登录/注册
     *
     * @param request 钱包登录请求
     * @return 登录结果
     */
    @PostMapping("/wallet-login")
    @Operation(summary = "钱包登录/注册", description = "通过钱包签名进行用户登录或注册")
    public ResponseEntity<Result<LoginResponse>> walletLogin(
            @Valid @RequestBody @Parameter(description = "钱包登录请求") WalletLoginRequest request) {

        log.info("收到钱包登录请求，钱包地址: {}", request.getWalletAddress());

        try {
            LoginResponse response = userService.walletLogin(request);
            return ResponseEntity.ok(Result.success(response, "登录成功"));
        } catch (Exception e) {
            log.error("钱包登录失败", e);
            return ResponseEntity.badRequest().body(Result.error(e.getMessage()));
        }
    }

    /**
     * 用户注册
     *
     * @param request 注册请求
     * @return 注册结果
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "创建新用户账户")
    public ResponseEntity<Result<LoginResponse>> register(
            @Valid @RequestBody @Parameter(description = "注册请求") UserRegisterRequest request) {

        log.info("收到用户注册请求，用户名: {}", request.getUsername());

        try {
            LoginResponse response = userService.register(request);
            return ResponseEntity.ok(Result.success(response, "注册成功"));
        } catch (Exception e) {
            log.error("用户注册失败", e);
            return ResponseEntity.badRequest().body(Result.error(e.getMessage()));
        }
    }

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/{userId}")
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户详细信息")
    public ResponseEntity<Result<UserVO>> getUserInfo(
            @Parameter(description = "用户ID") @PathVariable @NotBlank Long userId) {

        log.info("获取用户信息，用户ID: {}", userId);

        try {
            var user = userService.findById(userId);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            UserVO userVO = userService.convertToVO(user);
            return ResponseEntity.ok(Result.success(userVO));
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return ResponseEntity.badRequest().body(Result.error(e.getMessage()));
        }
    }

    /**
     * 检查钱包地址是否已注册
     *
     * @param walletAddress 钱包地址
     * @return 是否已注册
     */
    @GetMapping("/check-wallet")
    @Operation(summary = "检查钱包地址是否已注册", description = "验证钱包地址是否已经注册过用户")
    public ResponseEntity<Result<Boolean>> checkWalletRegistered(
            @Parameter(description = "钱包地址") @RequestParam @NotBlank String walletAddress) {

        log.info("检查钱包地址是否已注册: {}", walletAddress);

        try {
            boolean isRegistered = userService.isWalletRegistered(walletAddress);
            return ResponseEntity.ok(Result.success(isRegistered));
        } catch (Exception e) {
            log.error("检查钱包地址失败", e);
            return ResponseEntity.badRequest().body(Result.error(e.getMessage()));
        }
    }

    /**
     * 验证钱包签名
     *
     * @param walletAddress 钱包地址
     * @param message 原始消息
     * @param signature 签名
     * @return 验证结果
     */
    @PostMapping("/verify-signature")
    @Operation(summary = "验证钱包签名", description = "验证钱包签名是否有效")
    public ResponseEntity<Result<Boolean>> verifySignature(
            @Parameter(description = "钱包地址") @RequestParam @NotBlank String walletAddress,
            @Parameter(description = "原始消息") @RequestParam @NotBlank String message,
            @Parameter(description = "签名") @RequestParam @NotBlank String signature) {

        log.info("验证钱包签名，钱包地址: {}", walletAddress);

        try {
            boolean isValid = userService.verifyWalletSignature(walletAddress, message, signature);
            return ResponseEntity.ok(Result.success(isValid));
        } catch (Exception e) {
            log.error("验证钱包签名失败", e);
            return ResponseEntity.badRequest().body(Result.error(e.getMessage()));
        }
    }

    /**
     * 获取用户统计数据
     *
     * @return 用户统计信息
     */
    @GetMapping("/stats")
    @Operation(summary = "获取用户统计数据", description = "获取用户的游戏、智能体、资产等统计信息")
    public ResponseEntity<Result<Object>> getUserStats() {
        try {
            // 这里应该从数据库中获取实际的统计数据
            // 目前返回模拟数据
            var stats = new java.util.HashMap<String, Object>();
            stats.put("totalGames", 5);
            stats.put("totalAgents", 3);
            stats.put("totalAssets", 12);
            stats.put("totalTransactions", 8);

            return ResponseEntity.ok(Result.success(stats));
        } catch (Exception e) {
            log.error("获取用户统计数据失败", e);
            return ResponseEntity.badRequest().body(Result.error(e.getMessage()));
        }
    }

    /**
     * 获取用户代币余额
     *
     * @return 用户余额信息
     */
    @GetMapping("/balance")
    @Operation(summary = "获取用户代币余额", description = "获取用户的平台代币余额")
    public ResponseEntity<Result<Object>> getUserBalance() {
        try {
            // 这里应该从数据库中获取实际的余额数据
            // 目前返回模拟数据
            var balance = new java.util.HashMap<String, Object>();
            balance.put("tokenType", "PLT");
            balance.put("balance", 1234.56);

            return ResponseEntity.ok(Result.success(balance));
        } catch (Exception e) {
            log.error("获取用户余额失败", e);
            return ResponseEntity.badRequest().body(Result.error(e.getMessage()));
        }
    }

    /**
     * 获取用户最近活动
     *
     * @return 用户活动记录
     */
    @GetMapping("/activities")
    @Operation(summary = "获取用户最近活动", description = "获取用户的最近活动记录")
    public ResponseEntity<Result<java.util.List<Object>>> getUserActivities() {
        try {
            // 这里应该从数据库中获取实际的活动数据
            // 目前返回模拟数据
            var activities = new java.util.ArrayList<Object>();

            var activity1 = new java.util.HashMap<String, Object>();
            activity1.put("type", "login");
            activity1.put("description", "钱包连接成功");
            activity1.put("createdAt", java.time.Instant.now().toString());
            activities.add(activity1);

            var activity2 = new java.util.HashMap<String, Object>();
            activity2.put("type", "reward");
            activity2.put("description", "获得 100 PLT 奖励");
            activity2.put("createdAt", java.time.Instant.now().minusSeconds(7200).toString());
            activities.add(activity2);

            var activity3 = new java.util.HashMap<String, Object>();
            activity3.put("type", "game_play");
            activity3.put("description", "游玩游戏《太空大战》");
            activity3.put("createdAt", java.time.Instant.now().minusSeconds(86400).toString());
            activities.add(activity3);

            return ResponseEntity.ok(Result.success(activities));
        } catch (Exception e) {
            log.error("获取用户活动失败", e);
            return ResponseEntity.badRequest().body(Result.error(e.getMessage()));
        }
    }

    /**
     * 获取当前用户信息
     *
     * @return 当前登录用户信息
     */
    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的基本信息")
    public ResponseEntity<Result<UserVO>> getCurrentUser() {
        try {
            // 这里应该从JWT令牌中获取当前用户ID
            // 目前返回模拟数据
            UserVO user = new UserVO();
            user.setId(1L);
            user.setUsername("testuser");
            user.setEmail("test@example.com");
            user.setWalletAddress("0x742d35cc6aa8b8c7d8f5e8b9c8b7c6d5e4f3a2b1c");
            user.setAvatarUrl("/images/default-avatar.png");
            user.setCreatedAt(java.time.LocalDateTime.now());
            user.setUpdatedAt(java.time.LocalDateTime.now());

            return ResponseEntity.ok(Result.success(user));
        } catch (Exception e) {
            log.error("获取当前用户信息失败", e);
            return ResponseEntity.badRequest().body(Result.error(e.getMessage()));
        }
    }
}
