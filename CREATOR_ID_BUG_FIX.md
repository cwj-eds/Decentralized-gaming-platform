# 游戏创建者ID错误修复

## 🐛 问题描述

**现象**: 所有登录用户创建的游戏，创建者ID（`creator_id`）都是1，而不是实际登录用户的ID

**影响**:
- ❌ 无法区分谁创建了哪个游戏
- ❌ "我的游戏"功能异常
- ❌ 用户权限管理失效
- ❌ 游戏所有权归属错误

## 🔍 问题根源

在 `AgentController.java` 中发现了两个严重问题：

### 问题1：硬编码的默认用户ID

**位置**: 第122行
```java
if (userId == null) {
    userId = 1L; // 默认用户ID - 问题所在！
}
```

**问题**: 如果没有传递userId参数，就默认为1

### 问题2：依赖前端传递用户ID

**位置**: 第156行
```java
public Result<GameGenerationResult> generateGame(
    @Valid @RequestBody GameGenerationRequest request,
    @RequestParam Long userId) {  // ❌ 依赖前端传递
    // ...
}
```

**问题**:
- 使用 `@RequestParam Long userId` 需要前端手动传递
- 前端可能不传或传错
- 没有使用服务端的认证信息
- 安全风险：用户可以伪造其他用户的ID

## ✅ 修复方案

### 修改内容

修改了 `AgentController.java` 的4个方法：

#### 1. `myAgents()` - 我的智能体页面

**修改前**:
```java
@GetMapping("/my-agents")
public String myAgents(..., @RequestParam(required = false) Long userId, ...) {
    if (userId == null) {
        userId = 1L; // ❌ 硬编码默认值
    }
    // ...
}
```

**修改后**:
```java
@RequireAuth  // ✅ 添加认证
@GetMapping("/my-agents")
public String myAgents(...) {
    Long userId = UserContext.getCurrentUserId(); // ✅ 自动获取
    // ...
}
```

#### 2. `createAgent()` - 创建智能体API

**修改前**:
```java
@PostMapping("/api/create")
public Result<AgentVO> createAgent(..., @RequestParam Long userId) {
    // ❌ 依赖前端传递userId
}
```

**修改后**:
```java
@RequireAuth  // ✅ 添加认证
@PostMapping("/api/create")
public Result<AgentVO> createAgent(...) {
    Long userId = UserContext.getCurrentUserId(); // ✅ 自动获取
    // ...
}
```

#### 3. `generateGame()` - 生成游戏API（核心修复）

**修改前**:
```java
@PostMapping("/api/game-maker/generate")
public Result<GameGenerationResult> generateGame(
    @Valid @RequestBody GameGenerationRequest request,
    @RequestParam Long userId) {  // ❌ 依赖前端传递
    // ...
}
```

**修改后**:
```java
@RequireAuth  // ✅ 添加认证
@PostMapping("/api/game-maker/generate")
public Result<GameGenerationResult> generateGame(
    @Valid @RequestBody GameGenerationRequest request) {
    
    // ✅ 从认证信息中自动获取当前登录用户ID
    Long userId = UserContext.getCurrentUserId();
    
    log.info("用户 {} 请求生成游戏", userId);
    
    GameGenerationResult result = agentService.generateGame(request, userId);
    return Result.success(result);
}
```

#### 4. `useAgent()` - 使用智能体API

**修改前**:
```java
@PostMapping("/api/{agentId}/use")
public Result<Boolean> useAgent(..., @RequestParam Long userId) {
    // ❌ 依赖前端传递userId
}
```

**修改后**:
```java
@RequireAuth  // ✅ 添加认证
@PostMapping("/api/{agentId}/use")
public Result<Boolean> useAgent(...) {
    Long userId = UserContext.getCurrentUserId(); // ✅ 自动获取
    // ...
}
```

### 关键改进

1. ✅ **添加了 `@RequireAuth` 注解**
   - 确保用户必须登录才能访问
   - 自动验证JWT token

2. ✅ **移除了 `@RequestParam Long userId` 参数**
   - 不再依赖前端传递
   - 防止用户ID伪造

3. ✅ **使用 `UserContext.getCurrentUserId()`**
   - 从SecurityContext中自动获取
   - 基于JWT token解析
   - 100%准确

4. ✅ **添加了日志记录**
   - 便于追踪和调试
   - 安全审计

## 📊 影响范围

### 修改的文件
- `src/main/java/com/decentralized/gaming/platform/controller/AgentController.java`

### 影响的功能
1. ✅ 游戏创建 - 创建者ID现在正确
2. ✅ 智能体创建 - 创建者ID现在正确
3. ✅ 我的智能体 - 现在显示当前用户的智能体
4. ✅ 使用智能体 - 正确记录使用者

### 向后兼容性
- ⚠️ **不兼容**: 前端需要移除手动传递的 `userId` 参数
- ⚠️ **不兼容**: 所有相关API调用需要携带有效的JWT token

## 🔒 安全性提升

### 修复前的安全问题
1. ❌ 用户可以伪造其他用户的ID创建游戏
2. ❌ 未登录用户可以调用API
3. ❌ 游戏所有权可以被篡改
4. ❌ 无法追踪真实的创建者

### 修复后的安全保障
1. ✅ 用户ID从JWT token解析，无法伪造
2. ✅ 必须登录才能创建游戏
3. ✅ 游戏所有权由服务端控制
4. ✅ 完整的操作日志记录

## 🧪 测试验证

### 测试场景1: 不同用户创建游戏

```bash
# 用户A登录
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"userA","password":"password"}'
# 获得 TOKEN_A

# 用户A创建游戏
curl -X POST http://localhost:8080/agents/api/game-maker/generate \
  -H "Authorization: Bearer TOKEN_A" \
  -H "Content-Type: application/json" \
  -d '{"description":"Test Game A"}'
# 预期: creator_id = userA的ID

# 用户B登录
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"userB","password":"password"}'
# 获得 TOKEN_B

# 用户B创建游戏
curl -X POST http://localhost:8080/agents/api/game-maker/generate \
  -H "Authorization: Bearer TOKEN_B" \
  -H "Content-Type: application/json" \
  -d '{"description":"Test Game B"}'
# 预期: creator_id = userB的ID
```

**预期结果**: 每个游戏的 `creator_id` 是对应登录用户的真实ID

### 测试场景2: 未登录访问

```bash
# 不带token访问
curl -X POST http://localhost:8080/agents/api/game-maker/generate \
  -H "Content-Type: application/json" \
  -d '{"description":"Test Game"}'
```

**预期结果**: 401 Unauthorized - "请先登录"

### 测试场景3: 伪造用户ID（安全测试）

```bash
# 用户A登录后，尝试传递其他用户的ID（前端如果还这样调用）
curl -X POST "http://localhost:8080/agents/api/game-maker/generate?userId=999" \
  -H "Authorization: Bearer TOKEN_A" \
  -H "Content-Type: application/json" \
  -d '{"description":"Test Game"}'
```

**预期结果**: 
- 如果前端传递了userId参数，服务端会忽略它
- 实际使用的是从JWT token解析出的用户ID
- creator_id = userA的真实ID（不是999）

## 📝 数据库验证

查询游戏表验证创建者ID：

```sql
-- 查看最近创建的游戏及其创建者
SELECT 
    g.id,
    g.title,
    g.creator_id,
    u.username AS creator_name,
    g.created_at
FROM games g
LEFT JOIN users u ON g.creator_id = u.id
ORDER BY g.created_at DESC
LIMIT 10;
```

**预期结果**: 每个游戏的 `creator_id` 对应实际创建者的用户ID

## 🔄 前端需要的修改

### 修改前的前端代码

```javascript
// ❌ 旧代码 - 手动传递userId
async function generateGame(description) {
    const userId = getCurrentUserId(); // 从localStorage获取
    
    const response = await fetch('/agents/api/game-maker/generate?userId=' + userId, {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + getToken(),
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ description })
    });
    
    return await response.json();
}
```

### 修改后的前端代码

```javascript
// ✅ 新代码 - 不需要手动传递userId
async function generateGame(description) {
    // 不再需要获取userId，服务端自动从token解析
    
    const response = await fetch('/agents/api/game-maker/generate', {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + getToken(),
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ description })
    });
    
    return await response.json();
}
```

**关键变化**:
1. ❌ 移除了URL参数 `?userId=...`
2. ✅ 只需要在Header中携带JWT token
3. ✅ 服务端自动处理用户身份

## 💡 技术原理

### 用户ID获取流程

```
1. 前端发送请求
   ↓
   Header: Authorization: Bearer <JWT_TOKEN>

2. JwtAuthFilter拦截
   ↓
   验证token → 解析userId → 存储到SecurityContext

3. @RequireAuth切面验证
   ↓
   检查用户是否已登录

4. Controller方法执行
   ↓
   UserContext.getCurrentUserId() → 从SecurityContext获取

5. Service层执行业务
   ↓
   使用正确的userId创建游戏
```

### 关键组件

1. **JwtAuthFilter** (SecurityConfig.java)
   - 验证JWT token
   - 解析用户ID
   - 存储到SecurityContext

2. **AuthAspect** (AuthAspect.java)
   - 拦截 @RequireAuth 注解的方法
   - 验证用户登录状态

3. **UserContext** (UserContext.java)
   - 统一获取当前用户ID
   - 从SecurityContext读取

4. **AgentController** (AgentController.java)
   - 使用 @RequireAuth 注解
   - 调用 UserContext.getCurrentUserId()

## 📋 检查清单

修复后请确认：

- [x] 编译通过（`mvn clean compile`）
- [ ] 单元测试通过
- [ ] 不同用户创建游戏，creator_id不同
- [ ] 未登录无法创建游戏
- [ ] "我的游戏"显示正确
- [ ] 日志中记录了正确的用户ID
- [ ] 前端已移除手动传递userId的代码

## 🎉 修复完成

**状态**: ✅ 已修复  
**编译**: ✅ 成功  
**影响**: 游戏创建、智能体创建、用户权限  
**优先级**: 🔴 高（核心功能bug）  
**安全性**: ⬆️ 显著提升  

---

**修复时间**: 2024年10月11日  
**修复人**: AI Assistant  
**测试状态**: 等待验证  
**部署要求**: 需要重新编译和部署

