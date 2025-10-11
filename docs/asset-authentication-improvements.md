# 资产管理认证改进文档

## 概述

本次改进实现了资产管理模块的统一用户认证和授权机制，确保所有资产相关的操作都经过严格的用户身份验证，防止用户访问或修改其他用户的资产。

## 改进内容

### 1. 创建用户上下文工具类 (UserContext)

**文件位置**: `src/main/java/com/decentralized/gaming/platform/util/UserContext.java`

**功能**:
- 统一获取当前登录用户的ID和用户名
- 提供便捷的认证状态检查方法
- 提供资源所有权验证方法

**主要方法**:
```java
// 获取当前登录用户ID
Long userId = UserContext.getCurrentUserId();

// 获取当前登录用户名
String username = UserContext.getCurrentUsername();

// 检查是否已登录
boolean isAuth = UserContext.isAuthenticated();

// 要求必须已登录，否则抛出异常
Long userId = UserContext.requireCurrentUserId();

// 检查是否是指定用户
boolean isSameUser = UserContext.isCurrentUser(userId);

// 要求必须是指定用户，否则抛出异常
UserContext.requireCurrentUser(userId);
```

### 2. 增强JWT认证过滤器

**文件位置**: `src/main/java/com/decentralized/gaming/platform/config/SecurityConfig.java`

**改进内容**:
- 在JWT验证通过后，将用户ID存储到`SecurityContext`的`Authentication`对象的`details`中
- 同时将用户ID存储到`HttpServletRequest`的属性中作为备用方案
- 确保后续的请求处理过程中可以方便地获取用户ID

**核心代码**:
```java
// 将用户ID存储到authentication的details中
authentication.setDetails(userId);

// 将用户ID存储到request属性中（备用方案）
request.setAttribute("userId", userId);
request.setAttribute("username", username);
```

### 3. 创建权限验证注解 (@RequireAuth)

**文件位置**: `src/main/java/com/decentralized/gaming/platform/common/RequireAuth.java`

**功能**:
- 标记需要用户登录认证的Controller方法或类
- 支持类级别和方法级别的注解
- 支持资源所有权验证

**使用示例**:
```java
// 类级别注解 - 所有方法都需要认证
@RequireAuth
@Controller
@RequestMapping("/assets")
public class AssetController {
    // 所有方法都需要登录
}

// 方法级别注解
@RequireAuth
@GetMapping("/api/dashboard")
public Result<AssetDashboardVO> getDashboard() {
    // 需要登录
}

// 带资源所有权验证的注解
@RequireAuth(checkOwnership = true, ownerIdParam = "userId")
@GetMapping("/api/user/{userId}/profile")
public Result<UserProfile> getUserProfile(@PathVariable Long userId) {
    // 需要登录，且只能访问自己的资料
}
```

### 4. 创建权限验证切面 (AuthAspect)

**文件位置**: `src/main/java/com/decentralized/gaming/platform/config/AuthAspect.java`

**功能**:
- 自动拦截标记了`@RequireAuth`注解的方法
- 验证用户是否已登录
- 可选：验证资源所有权，防止用户访问其他用户的资源

**验证流程**:
1. 检查方法或类是否有`@RequireAuth`注解
2. 从`UserContext`获取当前登录用户ID
3. 如果用户未登录，抛出`BusinessException("请先登录")`
4. 如果注解设置了`checkOwnership = true`，进一步验证资源所有权
5. 如果用户试图访问其他用户的资源，抛出`BusinessException("无权访问其他用户的资源")`

### 5. 重构AssetController

**文件位置**: `src/main/java/com/decentralized/gaming/platform/controller/AssetController.java`

**改进内容**:
- 在类级别添加`@RequireAuth`注解，确保所有接口都需要登录
- 移除了每个方法中重复的`getCurrentUserId`和认证检查代码
- 统一使用`UserContext.getCurrentUserId()`获取用户ID
- 移除了不必要的`HttpServletRequest`参数
- 简化了代码结构，提高了可维护性

**重构前**:
```java
@GetMapping("/api/dashboard")
@ResponseBody
public Result<AssetDashboardVO> getDashboard(HttpServletRequest request) {
    Long currentUserId = getCurrentUserId(request);
    if (currentUserId == null) {
        return Result.error("未找到有效的用户认证信息");
    }
    // ... 业务逻辑
}

private Long getCurrentUserId(HttpServletRequest request) {
    // 复杂的token解析逻辑
}
```

**重构后**:
```java
@GetMapping("/api/dashboard")
@ResponseBody
public Result<AssetDashboardVO> getDashboard() {
    Long currentUserId = UserContext.getCurrentUserId();
    // ... 业务逻辑
}
```

### 6. 添加AspectJ依赖

**文件位置**: `pom.xml`

**添加的依赖**:
```xml
<!-- Spring AOP for aspect-oriented programming -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

## 安全性改进

### 防止的安全问题

1. **未授权访问**: 通过`@RequireAuth`注解和`AuthAspect`切面，确保所有资产相关的接口都必须登录才能访问

2. **横向越权**: 通过资源所有权验证，防止用户A访问或修改用户B的资产

3. **Token伪造**: 通过JWT的签名验证机制，确保token的真实性

4. **过期Token**: 通过JWT的过期时间验证，确保token的时效性

### 多层防护机制

1. **第一层 - Spring Security**: 
   - `SecurityFilterChain`配置了哪些路径需要认证
   - `JwtAuthFilter`验证JWT token的有效性

2. **第二层 - AOP切面**: 
   - `AuthAspect`切面拦截所有标记了`@RequireAuth`的方法
   - 自动验证用户是否已登录
   - 可选验证资源所有权

3. **第三层 - Service层**: 
   - Service层的所有方法都接收userId参数
   - 只查询和操作指定用户的数据
   - 通过MyBatis Plus的条件查询确保数据隔离

4. **第四层 - 数据库层**: 
   - 所有资产相关的表都有`user_id`字段
   - 查询时必须带上`user_id`条件

## 使用指南

### 为新的Controller添加认证

#### 方式1：类级别注解（推荐）
```java
@RequireAuth  // 所有方法都需要登录
@Controller
@RequestMapping("/myresource")
public class MyResourceController {
    
    @GetMapping("/list")
    public Result<List<MyResource>> list() {
        Long userId = UserContext.getCurrentUserId();
        // 业务逻辑
    }
}
```

#### 方式2：方法级别注解
```java
@Controller
@RequestMapping("/myresource")
public class MyResourceController {
    
    @RequireAuth  // 只有这个方法需要登录
    @GetMapping("/list")
    public Result<List<MyResource>> list() {
        Long userId = UserContext.getCurrentUserId();
        // 业务逻辑
    }
    
    @GetMapping("/public")
    public Result<String> publicEndpoint() {
        // 不需要登录
    }
}
```

#### 方式3：带资源所有权验证
```java
@Controller
@RequestMapping("/myresource")
public class MyResourceController {
    
    @RequireAuth(checkOwnership = true, ownerIdParam = "userId")
    @GetMapping("/user/{userId}/data")
    public Result<MyData> getUserData(@PathVariable Long userId) {
        // 自动验证：当前登录用户ID == userId
        // 业务逻辑
    }
}
```

### 在Service层使用

```java
@Service
public class MyService {
    
    public void doSomething() {
        // 获取当前登录用户ID
        Long userId = UserContext.getCurrentUserId();
        
        // 检查是否已登录
        if (!UserContext.isAuthenticated()) {
            throw new BusinessException("请先登录");
        }
        
        // 要求必须已登录（推荐）
        Long userId = UserContext.requireCurrentUserId();
        
        // 检查是否有权访问资源
        UserContext.requireCurrentUser(resourceOwnerId);
    }
}
```

## 测试验证

### 1. 测试未登录访问
```bash
# 不带token访问资产接口
curl http://localhost:8080/assets/api/dashboard

# 预期结果：401 Unauthorized 或重定向到登录页
```

### 2. 测试已登录访问
```bash
# 带有效token访问资产接口
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" \
     http://localhost:8080/assets/api/dashboard

# 预期结果：返回当前登录用户的资产数据
```

### 3. 测试横向越权
```bash
# 用户A的token尝试访问用户B的资源
curl -H "Authorization: Bearer USER_A_TOKEN" \
     http://localhost:8080/api/user/USER_B_ID/profile

# 预期结果：403 Forbidden 或 "无权访问其他用户的资源"
```

## 注意事项

1. **Cookie和Header支持**: JWT token可以通过Cookie或Authorization Header传递
   - Cookie名称：`TOKEN`
   - Header格式：`Authorization: Bearer <token>`

2. **异常处理**: 
   - 未登录会抛出`BusinessException("请先登录")`
   - 无权访问会抛出`BusinessException("无权访问其他用户的资源")`
   - 需要确保有全局异常处理器捕获这些异常

3. **日志记录**: 
   - 所有认证失败和越权尝试都会被记录到日志
   - 便于安全审计和问题排查

4. **性能考虑**: 
   - `UserContext`直接从`SecurityContext`获取用户信息，性能开销很小
   - AOP切面只在需要时才执行验证逻辑
   - JWT token只在Filter中验证一次

## 后续改进建议

1. **角色权限管理**: 
   - 当前只验证是否登录和资源所有权
   - 后续可以扩展支持基于角色的访问控制(RBAC)

2. **操作审计**: 
   - 记录所有资产操作的审计日志
   - 包括操作人、操作时间、操作内容等

3. **限流和防护**: 
   - 对敏感接口添加限流保护
   - 防止暴力破解和DDoS攻击

4. **Token刷新机制**: 
   - 实现token自动刷新机制
   - 提升用户体验，避免频繁登录

## 总结

通过本次改进，资产管理模块实现了：
- ✅ 统一的用户认证机制
- ✅ 自动化的权限验证
- ✅ 防止横向越权访问
- ✅ 代码简化和可维护性提升
- ✅ 多层安全防护
- ✅ 详细的日志记录

所有资产相关的操作现在都经过严格的用户身份验证，确保了数据安全和用户隐私。

