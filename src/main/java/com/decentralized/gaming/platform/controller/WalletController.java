package com.decentralized.gaming.platform.controller;

import com.decentralized.gaming.platform.common.Result;
import com.decentralized.gaming.platform.dto.WalletLoginRequest;
import com.decentralized.gaming.platform.dto.LoginResponse;
import com.decentralized.gaming.platform.entity.User;
import com.decentralized.gaming.platform.service.WalletService;
import com.decentralized.gaming.platform.service.UserService;
import com.decentralized.gaming.platform.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;
import com.decentralized.gaming.platform.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 钱包登录
     */
    @PostMapping("/login")
    @Operation(summary = "钱包登录", description = "通过钱包签名进行用户认证")
    public Result<UserVO> walletLogin(@Valid @RequestBody WalletLoginRequest request, HttpServletResponse response) {
        log.info("钱包登录请求: {}", request.getWalletAddress());

        // 走统一的用户登录/注册逻辑，保证用户真实落库并返回携带token
        LoginResponse login = userService.walletLogin(request);

        // 设置 HttpOnly TOKEN Cookie，页面访问将自动携带
        try {
            if (login != null && login.isSuccess() && login.getToken() != null) {
                jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("TOKEN", login.getToken());
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                // 可选：cookie.setSecure(true);
                response.addCookie(cookie);
            }
        } catch (Exception ignored) {
        }

        // 统一将用户VO返回给前端
        Object userObj = (login != null) ? login.getUser() : null;
        UserVO userVO = (userObj instanceof UserVO) ? (UserVO) userObj : null;
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