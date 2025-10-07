package com.decentralized.gaming.platform.controller;

import com.decentralized.gaming.platform.common.Result;
import com.decentralized.gaming.platform.dto.*;
import com.decentralized.gaming.platform.service.UserService;
import com.decentralized.gaming.platform.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

/**
 * 认证控制器
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name = "用户认证", description = "用户登录、注册、认证等接口")
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * 用户名密码登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户名密码登录", description = "通过用户名和密码进行用户登录")
    public Result<LoginResponse> login(@Valid @RequestBody UserLoginRequest request, HttpServletResponse response) {
        log.info("用户登录请求: {}", request.getUsername());

        try {
            LoginResponse loginResponse = userService.userLogin(request);

            if (loginResponse.isSuccess()) {
                if (loginResponse.getToken() != null) {
                    // 设置 HttpOnly Cookie，前端页面可自动携带
                    jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("TOKEN", loginResponse.getToken());
                    cookie.setPath("/");
                    cookie.setHttpOnly(true);
                    // 可选：设置安全标记，若启用 HTTPS 则打开
                    // cookie.setSecure(true);
                    response.addCookie(cookie);
                }
                log.info("用户登录成功: {}", request.getUsername());
            }

            return Result.success(loginResponse);
        } catch (Exception e) {
            log.error("登录失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "创建新用户账号")
    public Result<LoginResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        log.info("用户注册请求: {}", request.getUsername());

        try {
            LoginResponse response = userService.register(request);
            return Result.success(response);
        } catch (Exception e) {
            log.error("注册失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    @Operation(summary = "退出登录", description = "用户退出登录")
    public Result<Void> logout() {
        log.info("用户退出登录");
        return Result.success();
    }
}






