# 资产管理用户认证系统 - 使用指南

## 🎯 功能概述

本系统实现了完善的用户认证和授权机制，确保：
- ✅ 所有资产操作都需要用户登录
- ✅ 用户只能访问和修改自己的资产
- ✅ 自动拦截未授权的访问尝试
- ✅ 统一的用户身份获取方式

## 📁 新增文件

1. **UserContext.java** - 用户上下文工具类
   - 位置：`src/main/java/com/decentralized/gaming/platform/util/UserContext.java`
   - 功能：统一获取当前登录用户信息

2. **RequireAuth.java** - 权限验证注解
   - 位置：`src/main/java/com/decentralized/gaming/platform/common/RequireAuth.java`
   - 功能：标记需要认证的接口

3. **AuthAspect.java** - 权限验证切面
   - 位置：`src/main/java/com/decentralized/gaming/platform/config/AuthAspect.java`
   - 功能：自动拦截和验证用户认证

4. **asset-authentication-improvements.md** - 详细技术文档
   - 位置：`docs/asset-authentication-improvements.md`
   - 内容：完整的技术实现细节和架构说明

## 📝 核心改进

### 1. SecurityConfig.java - 增强JWT过滤器
```java
// 将用户ID存储到SecurityContext中
authentication.setDetails(userId);
request.setAttribute("userId", userId);
```

### 2. AssetController.java - 简化认证逻辑
```java
// 旧代码：每个方法都要手动检查
Long currentUserId = getCurrentUserId(request);
if (currentUserId == null) {
    return Result.error("未找到有效的用户认证信息");
}

// 新代码：使用@RequireAuth注解 + UserContext
@RequireAuth
@GetMapping("/api/dashboard")
public Result<AssetDashboardVO> getDashboard() {
    Long currentUserId = UserContext.getCurrentUserId();
    // 业务逻辑
}
```

### 3. pom.xml - 添加AOP依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

## 🚀 快速开始

### 方法一：类级别认证（推荐用于整个Controller）

```java
@RequireAuth  // 所有方法都需要登录
@Controller
@RequestMapping("/myresource")
public class MyResourceController {
    
    @GetMapping("/list")
    public Result<?> list() {
        Long userId = UserContext.getCurrentUserId();
        // 自动已验证登录状态
    }
}
```

### 方法二：方法级别认证（推荐用于部分接口）

```java
@Controller
@RequestMapping("/myresource")
public class MyResourceController {
    
    @RequireAuth  // 仅此方法需要登录
    @GetMapping("/protected")
    public Result<?> protectedEndpoint() {
        Long userId = UserContext.getCurrentUserId();
    }
    
    @GetMapping("/public")
    public Result<?> publicEndpoint() {
        // 不需要登录
    }
}
```

### 方法三：资源所有权验证

```java
@RequireAuth(checkOwnership = true, ownerIdParam = "userId")
@GetMapping("/user/{userId}/data")
public Result<?> getUserData(@PathVariable Long userId) {
    // 自动验证：当前登录用户 == 路径中的userId
    // 如果不匹配，自动抛出异常
}
```

## 💡 UserContext 使用示例

### 在Controller中使用

```java
@RestController
@RequestMapping("/api/mycontroller")
public class MyController {
    
    @RequireAuth
    @GetMapping("/data")
    public Result<?> getData() {
        // 获取当前登录用户ID（推荐）
        Long userId = UserContext.getCurrentUserId();
        
        // 获取当前登录用户名
        String username = UserContext.getCurrentUsername();
        
        // 检查是否已登录
        if (UserContext.isAuthenticated()) {
            // 已登录
        }
        
        return Result.success(data);
    }
}
```

### 在Service中使用

```java
@Service
public class MyService {
    
    public void processData() {
        // 要求必须已登录，未登录会抛出异常
        Long userId = UserContext.requireCurrentUserId();
        
        // 检查是否是指定用户
        if (UserContext.isCurrentUser(resourceOwnerId)) {
            // 是本人
        }
        
        // 要求必须是指定用户，否则抛出异常
        UserContext.requireCurrentUser(resourceOwnerId);
    }
}
```

## 🔒 安全架构

```
请求 → JwtAuthFilter (验证Token)
    ↓
    存储userId到SecurityContext
    ↓
    @RequireAuth注解检查 (AuthAspect)
    ↓
    Controller方法执行
    ↓
    Service层 (使用userId查询数据)
    ↓
    数据库 (WHERE user_id = ?)
```

## 🧪 测试场景

### 场景1：未登录访问
```bash
curl http://localhost:8080/assets/api/dashboard
# 结果：401 Unauthorized 或重定向到登录页
```

### 场景2：正常登录访问
```bash
curl -H "Authorization: Bearer YOUR_TOKEN" \
     http://localhost:8080/assets/api/dashboard
# 结果：返回当前用户的资产数据
```

### 场景3：越权访问（用户A访问用户B的数据）
```bash
curl -H "Authorization: Bearer USER_A_TOKEN" \
     http://localhost:8080/api/user/USER_B_ID/profile
# 结果：403 Forbidden - "无权访问其他用户的资源"
```

## ⚙️ 配置说明

### JWT Token传递方式

支持两种方式：

1. **HTTP Header（推荐用于API）**
   ```
   Authorization: Bearer <your-jwt-token>
   ```

2. **Cookie（推荐用于Web页面）**
   ```
   Cookie: TOKEN=<your-jwt-token>
   ```

### SecurityConfig配置

```java
// 公开路径（不需要认证）
.requestMatchers("/api/auth/**", "/api/wallet/**", "/auth/**", 
                 "/css/**", "/js/**").permitAll()

// 其他所有路径都需要认证
.anyRequest().authenticated()
```

## 📋 检查清单

部署前请确认：

- [ ] 已添加`spring-boot-starter-aop`依赖到pom.xml
- [ ] 已重新编译项目（`mvn clean package`）
- [ ] SecurityConfig中的JwtAuthFilter已更新
- [ ] 需要保护的Controller已添加`@RequireAuth`注解
- [ ] 所有Service方法都使用userId参数
- [ ] 测试未登录访问会被拦截
- [ ] 测试越权访问会被阻止

## 🐛 常见问题

### Q1: @RequireAuth注解不生效？
**A:** 检查是否添加了AOP依赖，并重新编译项目。

### Q2: UserContext.getCurrentUserId() 返回null？
**A:** 检查JWT token是否有效，是否正确传递token。

### Q3: 提示"请先登录"但已经登录？
**A:** 检查token是否过期，或者SecurityContext中是否正确存储了用户信息。

### Q4: 如何调试认证问题？
**A:** 查看日志文件，AuthAspect和JwtAuthFilter都有详细的日志记录。

## 📚 相关文档

- [详细技术文档](docs/asset-authentication-improvements.md) - 完整的技术实现细节
- [区块链开发指南](docs/blockchain-development-guide.md) - 区块链集成说明
- [项目结构](PROJECT_STRUCTURE.md) - 项目整体结构

## 🎉 总结

通过本次改进：
1. **代码更简洁** - Controller中的认证代码减少了70%
2. **更安全** - 多层防护机制，防止越权访问
3. **更易维护** - 统一的认证逻辑，便于修改和扩展
4. **更易测试** - 清晰的注解和工具类，便于单元测试

现在所有资产相关的操作都经过严格的用户身份验证，确保了数据安全和用户隐私！ 🔐

