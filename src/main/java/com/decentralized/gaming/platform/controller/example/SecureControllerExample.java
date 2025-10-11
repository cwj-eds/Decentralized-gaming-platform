package com.decentralized.gaming.platform.controller.example;

import com.decentralized.gaming.platform.common.RequireAuth;
import com.decentralized.gaming.platform.common.Result;
import com.decentralized.gaming.platform.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 安全Controller示例
 * 演示如何使用@RequireAuth注解和UserContext工具类
 * 
 * ⚠️ 这是一个示例文件，实际使用时可以删除
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@RestController
@RequestMapping("/api/example")
public class SecureControllerExample {
    
    // ========== 示例1：类级别认证 ==========
    // 如果在类上添加@RequireAuth，则所有方法都需要登录
    // @RequireAuth
    
    /**
     * 示例1：需要登录的接口
     * 使用方法级别的@RequireAuth注解
     */
    @RequireAuth
    @GetMapping("/protected")
    public Result<String> protectedEndpoint() {
        // 获取当前登录用户ID
        Long userId = UserContext.getCurrentUserId();
        String username = UserContext.getCurrentUsername();
        
        log.info("用户访问受保护接口，用户ID: {}, 用户名: {}", userId, username);
        
        return Result.success("你好，" + username + "！这是受保护的数据。");
    }
    
    /**
     * 示例2：不需要登录的公开接口
     * 没有@RequireAuth注解
     */
    @GetMapping("/public")
    public Result<String> publicEndpoint() {
        log.info("访问公开接口");
        return Result.success("这是公开数据，无需登录。");
    }
    
    /**
     * 示例3：带资源所有权验证的接口
     * checkOwnership = true 会自动验证当前登录用户是否是资源所有者
     */
    @RequireAuth(checkOwnership = true, ownerIdParam = "userId")
    @GetMapping("/user/{userId}/private-data")
    public Result<String> getUserPrivateData(@PathVariable Long userId) {
        // 如果当前登录用户ID != userId，会自动抛出异常
        // 这里的代码只有在验证通过后才会执行
        
        log.info("用户访问私有数据，用户ID: {}", userId);
        
        return Result.success("这是用户ID " + userId + " 的私有数据");
    }
    
    /**
     * 示例4：手动验证资源所有权
     * 适用于复杂的权限逻辑
     */
    @RequireAuth
    @GetMapping("/resource/{resourceId}")
    public Result<String> getResource(@PathVariable Long resourceId) {
        Long currentUserId = UserContext.getCurrentUserId();
        
        // 假设我们需要从数据库查询资源的所有者
        Long resourceOwnerId = getResourceOwnerIdFromDatabase(resourceId);
        
        // 手动验证资源所有权
        if (!UserContext.isCurrentUser(resourceOwnerId)) {
            return Result.error("无权访问此资源");
        }
        
        // 或者使用更简洁的方式（会抛出异常）
        // UserContext.requireCurrentUser(resourceOwnerId);
        
        return Result.success("资源数据");
    }
    
    /**
     * 示例5：在Service层使用UserContext
     */
    @RequireAuth
    @PostMapping("/create")
    public Result<String> createResource(@RequestBody CreateResourceRequest request) {
        // 要求必须已登录，否则抛出异常
        Long currentUserId = UserContext.requireCurrentUserId();
        
        log.info("用户创建资源，用户ID: {}, 资源名称: {}", currentUserId, request.getName());
        
        // 在实际的Service方法中也可以使用UserContext
        // myService.createResource(request, currentUserId);
        
        return Result.success("资源创建成功，所有者ID: " + currentUserId);
    }
    
    /**
     * 示例6：检查登录状态
     */
    @GetMapping("/check-auth")
    public Result<AuthStatus> checkAuthStatus() {
        boolean isAuthenticated = UserContext.isAuthenticated();
        Long userId = UserContext.getCurrentUserId();
        String username = UserContext.getCurrentUsername();
        
        AuthStatus status = new AuthStatus(isAuthenticated, userId, username);
        
        return Result.success(status);
    }
    
    // ========== 辅助方法和DTO ==========
    
    /**
     * 模拟从数据库获取资源所有者ID
     */
    private Long getResourceOwnerIdFromDatabase(Long resourceId) {
        // 实际实现应该查询数据库
        // return resourceMapper.selectById(resourceId).getOwnerId();
        return 1L; // 示例返回值
    }
    
    /**
     * 创建资源请求DTO
     */
    public static class CreateResourceRequest {
        private String name;
        private String description;
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
    
    /**
     * 认证状态DTO
     */
    public static class AuthStatus {
        private boolean authenticated;
        private Long userId;
        private String username;
        
        public AuthStatus(boolean authenticated, Long userId, String username) {
            this.authenticated = authenticated;
            this.userId = userId;
            this.username = username;
        }
        
        public boolean isAuthenticated() { return authenticated; }
        public void setAuthenticated(boolean authenticated) { this.authenticated = authenticated; }
        
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }
}

