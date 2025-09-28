//package com.decentralized.gaming.platform.controller;
//
//import com.decentralized.gaming.platform.common.Result;
//import com.decentralized.gaming.platform.entity.User;
//import com.decentralized.gaming.platform.service.UserService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
///**
// * 用户控制器
// *
// * @author DecentralizedGamingPlatform
// */
//@Tag(name = "用户管理", description = "用户相关接口")
//@RestController
//@RequestMapping("/api/users")
//@RequiredArgsConstructor
//public class UserController {
//
//    private final UserService userService;
//
//    @Operation(summary = "钱包登录")
//    @PostMapping("/wallet-login")
//    public Result<User> walletLogin(@RequestBody WalletLoginRequest request) {
//        User user = userService.walletLogin(request.getWalletAddress(),
//                                          request.getSignature(),
//                                          request.getMessage());
//        return Result.success(user);
//    }
//
//    @Operation(summary = "获取用户信息")
//    @GetMapping("/{userId}")
//    public Result<User> getUserInfo(@PathVariable Long userId) {
//        User user = userService.getById(userId);
//        return Result.success(user);
//    }
//
//    @Operation(summary = "更新用户信息")
//    @PutMapping("/{userId}")
//    public Result<User> updateUser(@PathVariable Long userId,
//                                  @RequestBody UpdateUserRequest request) {
//        User user = userService.updateUserInfo(userId,
//                                             request.getUsername(),
//                                             request.getEmail(),
//                                             request.getAvatarUrl());
//        return Result.success(user);
//    }
//
//    /**
//     * 钱包登录请求对象
//     */
//    public static class WalletLoginRequest {
//        private String walletAddress;
//        private String signature;
//        private String message;
//
//        // getters and setters
//        public String getWalletAddress() {
//            return walletAddress;
//        }
//
//        public void setWalletAddress(String walletAddress) {
//            this.walletAddress = walletAddress;
//        }
//
//        public String getSignature() {
//            return signature;
//        }
//
//        public void setSignature(String signature) {
//            this.signature = signature;
//        }
//
//        public String getMessage() {
//            return message;
//        }
//
//        public void setMessage(String message) {
//            this.message = message;
//        }
//    }
//
//    /**
//     * 更新用户请求对象
//     */
//    public static class UpdateUserRequest {
//        private String username;
//        private String email;
//        private String avatarUrl;
//
//        // getters and setters
//        public String getUsername() {
//            return username;
//        }
//
//        public void setUsername(String username) {
//            this.username = username;
//        }
//
//        public String getEmail() {
//            return email;
//        }
//
//        public void setEmail(String email) {
//            this.email = email;
//        }
//
//        public String getAvatarUrl() {
//            return avatarUrl;
//        }
//
//        public void setAvatarUrl(String avatarUrl) {
//            this.avatarUrl = avatarUrl;
//        }
//    }
//}
