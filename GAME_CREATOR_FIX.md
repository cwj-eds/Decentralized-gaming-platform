# 游戏创建者ID问题修复说明

## 🐛 问题描述

**问题现象**：创建的游戏不属于登录用户

**用户疑问**：游戏表和用户表没有关联吗？

## 🔍 问题根源分析

### 1. 数据库设计 ✅ 正常

数据库设计完全正确，游戏表和用户表有明确的关联关系：

**Schema定义**（`src/main/resources/schema.sql`）：
```sql
CREATE TABLE IF NOT EXISTS games (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL COMMENT '游戏标题',
    description TEXT COMMENT '游戏描述',
    creator_id BIGINT NOT NULL COMMENT '创建者ID',  -- ✓ 创建者字段
    -- ... 其他字段
    CONSTRAINT fk_games_creator_id FOREIGN KEY (creator_id) 
        REFERENCES users(id) ON DELETE CASCADE  -- ✓ 外键约束
);
```

**实体类定义**（`src/main/java/.../entity/Game.java`）：
```java
@TableField("creator_id")
private Long creatorId;  // ✓ 创建者ID字段
```

### 2. 后端逻辑 ✅ 已修复

后端在 `AgentController.java` 中已经正确实现了用户ID获取：

```java
@RequireAuth  // ✓ 需要登录认证
@PostMapping("/api/game-maker/generate")
public Result<GameGenerationResult> generateGame(
    @Valid @RequestBody GameGenerationRequest request) {
    
    // ✓ 从认证上下文自动获取当前登录用户ID
    Long userId = UserContext.getCurrentUserId();
    
    log.info("用户 {} 请求生成游戏", userId);
    
    GameGenerationResult result = agentService.generateGame(request, userId);
    return Result.success(result);
}
```

**认证流程**：
1. 用户登录后，后端生成JWT token
2. Token包含用户ID信息，并设置为HttpOnly Cookie
3. 前端请求时，Cookie自动携带
4. `JwtAuthFilter` 拦截请求，验证token，解析用户ID
5. 将用户ID存储到 `SecurityContext`
6. `UserContext.getCurrentUserId()` 从 `SecurityContext` 获取用户ID

### 3. 前端代码 ❌ 有问题（已修复）

**问题代码**（`src/main/resources/templates/agents/game-maker.html`）：

```javascript
// ❌ 旧代码 - 硬编码用户ID为1
const userId = 1; // 临时硬编码

fetch('/agents/api/game-maker/generate?userId=' + userId, {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(requestData)
})
```

**问题分析**：
- 硬编码 `userId = 1`，所有用户创建的游戏都归属于用户1
- URL参数传递用户ID，但后端已不再接受此参数
- 没有利用后端的认证机制

## ✅ 修复方案

### 1. 修复前端游戏创建请求

**修改文件**：`src/main/resources/templates/agents/game-maker.html`

```javascript
// ✅ 新代码 - 不传递userId，让后端自动获取
// 不需要手动传递userId，后端会自动从JWT token中获取当前登录用户ID
// JWT token已经在登录时设置为HttpOnly Cookie，会自动随请求发送

fetch('/agents/api/game-maker/generate', {  // ✓ 删除了 ?userId=1
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(requestData)
})
```

### 2. 完善前端登录处理

**修改文件**：`src/main/resources/static/js/auth.js`

```javascript
// 登录成功后保存token到localStorage（备用）
if (result && result.code === 200) {
    const payload = result.data || {};
    const user = payload.user || payload;
    const token = payload.token; // ✓ 获取JWT token
    
    // 保存用户信息和token
    localStorage.setItem('user', JSON.stringify(user));
    localStorage.setItem('authType', 'account');
    if (token) {
        localStorage.setItem('token', token); // ✓ 保存token
    }
    // ...
}
```

## 🔧 工作原理

### 完整的认证和授权流程

```
1. 用户登录
   ├─> 前端发送登录请求
   ├─> 后端验证用户名密码
   ├─> 生成JWT token（包含userId和username）
   ├─> 设置HttpOnly Cookie: TOKEN=<jwt-token>
   └─> 返回用户信息和token

2. 创建游戏请求
   ├─> 前端发送POST /agents/api/game-maker/generate
   ├─> 浏览器自动携带Cookie: TOKEN=<jwt-token>
   ├─> JwtAuthFilter拦截请求
   │   ├─> 从Cookie中提取token
   │   ├─> 验证token有效性
   │   ├─> 从token中解析userId
   │   └─> 存储userId到SecurityContext
   ├─> @RequireAuth切面验证用户已登录
   ├─> Controller调用UserContext.getCurrentUserId()
   │   └─> 从SecurityContext获取userId
   └─> Service使用正确的userId创建游戏
       └─> game.setCreatorId(userId)
```

### 关键组件说明

**JwtAuthFilter**（`SecurityConfig.java`）：
- 拦截所有请求
- 从Cookie或Authorization Header提取token
- 验证并解析token
- 将用户信息存储到SecurityContext

**UserContext**（`UserContext.java`）：
- 提供统一的用户ID获取接口
- 从SecurityContext读取当前用户信息
- 确保获取的是真实登录用户的ID

**@RequireAuth**（`AuthAspect.java`）：
- AOP切面注解
- 验证方法执行前用户必须已登录
- 未登录则抛出异常

## 📝 测试验证

### 手动测试步骤

1. **登录不同用户**：
   ```bash
   # 用户1登录
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"alice","password":"123456"}' \
     -c cookies1.txt
   
   # 用户2登录
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"bob","password":"123456"}' \
     -c cookies2.txt
   ```

2. **创建游戏**：
   ```bash
   # 用户1创建游戏
   curl -X POST http://localhost:8080/agents/api/game-maker/generate \
     -H "Content-Type: application/json" \
     -b cookies1.txt \
     -d '{"description":"用户1的游戏"}'
   
   # 用户2创建游戏
   curl -X POST http://localhost:8080/agents/api/game-maker/generate \
     -H "Content-Type: application/json" \
     -b cookies2.txt \
     -d '{"description":"用户2的游戏"}'
   ```

3. **验证数据库**：
   ```sql
   -- 查看最近创建的游戏及其创建者
   SELECT g.id, g.title, g.creator_id, u.username
   FROM games g
   LEFT JOIN users u ON g.creator_id = u.id
   ORDER BY g.created_at DESC
   LIMIT 10;
   ```

### 自动化测试脚本

运行测试脚本验证修复：
```powershell
.\test-game-creator.ps1
```

该脚本将：
1. 登录两个不同的用户
2. 分别创建游戏
3. 验证创建者ID是否正确

## 🎯 预期结果

修复后的行为：

| 操作 | 登录用户 | 创建游戏 | creator_id | 结果 |
|------|---------|---------|-----------|------|
| 用户alice登录并创建游戏 | alice (id=1) | 游戏A | 1 | ✅ 正确 |
| 用户bob登录并创建游戏 | bob (id=2) | 游戏B | 2 | ✅ 正确 |
| 未登录用户尝试创建 | 无 | - | - | ❌ 返回401未授权 |

## 📋 检查清单

修复完成后请确认：

- [x] 前端代码已删除硬编码的userId
- [x] 前端不再在URL中传递userId参数
- [x] 前端登录后正确保存token
- [x] 后端使用@RequireAuth注解保护接口
- [x] 后端使用UserContext.getCurrentUserId()获取用户ID
- [ ] 不同用户创建游戏，creator_id不同（需要实际测试）
- [ ] 未登录无法创建游戏（需要实际测试）
- [ ] "我的游戏"页面显示正确的游戏（需要实际测试）

## 🔐 安全性改进

修复后的安全优势：

1. **防止用户ID伪造**：
   - 旧方案：前端传递userId，可被篡改
   - 新方案：服务端从JWT token解析，无法伪造

2. **强制用户认证**：
   - 使用@RequireAuth注解
   - 未登录用户自动返回401或重定向到登录页

3. **HttpOnly Cookie**：
   - Cookie标记为HttpOnly，JavaScript无法访问
   - 防止XSS攻击窃取token

## 📚 相关文档

- [CREATOR_ID_BUG_FIX.md](./CREATOR_ID_BUG_FIX.md) - 详细的修复记录
- [ASSET_AUTHENTICATION_GUIDE.md](./ASSET_AUTHENTICATION_GUIDE.md) - 认证指南
- [去中心化游戏平台开发文档.md](./去中心化游戏平台开发文档.md) - 整体开发文档

## 🎉 总结

**问题本质**：前端代码硬编码了用户ID，导致所有游戏都归属于ID为1的用户

**解决方案**：删除前端硬编码，利用后端已有的JWT认证机制自动获取当前用户ID

**数据库关联**：✅ 游戏表和用户表通过外键约束正确关联，设计无问题

**修复状态**：✅ 代码已修复，需要重启应用并进行实际测试验证

---

**修复日期**：2025-10-11  
**修复文件**：
- `src/main/resources/templates/agents/game-maker.html`
- `src/main/resources/static/js/auth.js`

