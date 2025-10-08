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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.decentralized.gaming.platform.util.JwtUtils;
import jakarta.servlet.http.Cookie;

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

    @Autowired
    private JwtUtils jwtUtils;

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
    public Result<Void> logout(HttpServletResponse response) {
        log.info("用户退出登录");
        // 覆盖并清除 TOKEN Cookie（与登录时同名同路径）
        Cookie expired = new Cookie("TOKEN", "");
        expired.setPath("/");
        expired.setHttpOnly(true);
        expired.setMaxAge(0);
        response.addCookie(expired);
        return Result.success();
    }

    /**
     * 获取当前登录用户信息（基于JWT）
     * 无有效登录则返回200 + data=null，便于前端无感知处理
     */
    @GetMapping("/me")
    @Operation(summary = "当前用户", description = "根据请求中的TOKEN返回当前登录用户信息")
    public Result<UserVO> me(HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = resolveToken(request);
            if (token == null || !jwtUtils.validateToken(token)) {
                return Result.success(null);
            }

            Long userId = jwtUtils.getUserIdFromToken(token);
            if (userId == null) {
                return Result.success(null);
            }

            UserVO user = userService.getUserById(userId);
            return Result.success(user);
        } catch (Exception e) {
            log.error("获取当前用户失败", e);
            return Result.success(null);
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring(7);
        }
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("TOKEN".equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        return null;
    }
}






