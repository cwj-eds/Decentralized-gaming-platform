package com.decentralized.gaming.platform.controller;

import com.decentralized.gaming.platform.common.Result;
import com.decentralized.gaming.platform.dto.WalletLoginRequest;
import com.decentralized.gaming.platform.entity.User;
import com.decentralized.gaming.platform.service.WalletService;
import com.decentralized.gaming.platform.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 钱包控制器
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@RestController
@RequestMapping("/api/wallet")
@Tag(name = "钱包管理", description = "钱包连接、登录、验证等接口")
public class WalletController {

    @Autowired
    private WalletService walletService;

    /**
     * 钱包登录
     */
    @PostMapping("/login")
    @Operation(summary = "钱包登录", description = "通过钱包签名进行用户认证")
    public Result<UserVO> walletLogin(@Valid @RequestBody WalletLoginRequest request) {
        log.info("钱包登录请求: {}", request.getWalletAddress());
        
        // 验证并认证用户
        User user = walletService.authenticateWallet(
            request.getWalletAddress(),
            request.getSignature(),
            request.getMessage()
        );

        // 转换为VO
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setWalletAddress(user.getWalletAddress());
        userVO.setUsername(user.getUsername());
        userVO.setEmail(user.getEmail());
        userVO.setAvatarUrl(user.getAvatarUrl());
        userVO.setCreatedAt(user.getCreatedAt());

        log.info("用户登录成功: {}", user.getWalletAddress());
        return Result.success(userVO);
    }

    /**
     * 生成登录消息
     */
    @GetMapping("/login-message")
    @Operation(summary = "生成登录消息", description = "生成用于签名的登录消息")
    public Result<String> generateLoginMessage() {
        long timestamp = System.currentTimeMillis();
        String message = walletService.generateLoginMessage(timestamp);
        return Result.success(message);
    }

    /**
     * 验证地址格式
     */
    @GetMapping("/validate-address")
    @Operation(summary = "验证地址格式", description = "验证以太坊地址格式是否正确")
    public Result<Boolean> validateAddress(@RequestParam String address) {
        boolean isValid = walletService.isValidAddress(address);
        return Result.success(isValid);
    }

    /**
     * 获取地址校验和格式
     */
    @GetMapping("/checksum-address")
    @Operation(summary = "获取校验和地址", description = "将地址转换为EIP-55校验和格式")
    public Result<String> getChecksumAddress(@RequestParam String address) {
        String checksumAddress = walletService.toChecksumAddress(address);
        return Result.success(checksumAddress);
    }
} 