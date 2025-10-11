# 智能体和游戏自动添加到用户资产 - 使用指南

## 📚 目录

1. [功能概述](#功能概述)
2. [技术实现](#技术实现)
3. [使用方法](#使用方法)
4. [测试验证](#测试验证)
5. [API文档](#api文档)
6. [故障排查](#故障排查)

---

## 功能概述

### 🎯 目标

当用户创建智能体或生成游戏后，系统会自动将这些资产添加到用户的资产表（`user_assets`）中，实现统一的资产管理。

### ✨ 特性

- ✅ 自动资产记录：创建即拥有
- ✅ 统一管理：所有资产在一个表中
- ✅ 完整追踪：记录获得方式和时间
- ✅ 安全可靠：防重复添加机制
- ✅ 容错处理：失败不影响主流程

### 📊 资产类型

| 资产类型 | 枚举值 | 说明 |
|---------|--------|------|
| 游戏 | GAME | 用户创建的游戏 |
| 智能体 | AGENT | 用户创建的智能体 |
| 游戏道具 | GAME_ITEM | 游戏内道具 |

### 🔄 获得方式

| 获得方式 | 枚举值 | 说明 |
|---------|--------|------|
| 创建 | CREATED | 用户自己创建 |
| 购买 | PURCHASED | 从市场购买 |
| 奖励 | REWARDED | 系统奖励 |
| 转移 | TRANSFERRED | 他人转移 |

---

## 技术实现

### 架构设计

```
┌─────────────────────┐
│  AgentController    │
│  ┌───────────────┐  │
│  │ createAgent() │  │
│  │ generateGame()│  │
│  └───────┬───────┘  │
└──────────┼──────────┘
           │
           ▼
┌─────────────────────┐
│  AgentService       │
│  ┌───────────────┐  │
│  │ createAgent() │──┼──► 1. 创建智能体
│  │               │  │    2. 添加到资产表 ✨
│  │ generateGame()│──┼──► 1. 生成游戏
│  │               │  │    2. 添加到资产表 ✨
│  └───────────────┘  │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│  AssetService       │
│  ┌───────────────┐  │
│  │ addUserAsset()│  │
│  └───────────────┘  │
└─────────────────────┘
```

### 核心代码

**AgentServiceImpl.java**

```java
@Service
public class AgentServiceImpl implements AgentService {
    
    private final AssetService assetService;
    
    @Override
    @Transactional
    public AgentVO createAgent(CreateAgentRequest request, Long creatorId) {
        // 1. 创建智能体
        Agent agent = new Agent();
        // ... 设置属性
        agentMapper.insert(agent);
        
        // 2. 自动添加到用户资产 ✨
        try {
            assetService.addUserAsset(
                creatorId, 
                UserAsset.AssetType.AGENT, 
                agent.getId(), 
                UserAsset.AcquisitionType.CREATED,
                agent.getContractAddress(),
                null
            );
            log.info("智能体已添加到用户资产");
        } catch (Exception e) {
            log.error("添加资产失败", e);
            // 不影响主流程
        }
        
        return agentVO;
    }
    
    @Override
    @Transactional
    public GameGenerationResult generateGame(GameGenerationRequest request, Long userId) {
        // 1. 生成游戏
        Game game = new Game();
        // ... 设置属性
        gameMapper.insert(game);
        
        // 2. 自动添加到用户资产 ✨
        try {
            assetService.addUserAsset(
                userId, 
                UserAsset.AssetType.GAME, 
                game.getId(), 
                UserAsset.AcquisitionType.CREATED,
                game.getContractAddress(),
                null
            );
            log.info("游戏已添加到用户资产");
        } catch (Exception ex) {
            log.error("添加资产失败", ex);
            // 不影响主流程
        }
        
        return result;
    }
}
```

---

## 使用方法

### 方式1：通过Web界面

#### 创建智能体

1. 登录系统
2. 访问 `/agents/upload` 页面
3. 填写智能体信息：
   - 名称
   - 描述
   - 类型
   - 代码URL
   - 模型URL
   - 价格
4. 点击"创建"按钮
5. ✅ 智能体自动添加到"我的资产"

#### 生成游戏

1. 登录系统
2. 访问 `/agents/game-maker` 页面
3. 输入游戏描述
4. 选择游戏类型和难度
5. 点击"生成游戏"按钮
6. ✅ 游戏自动添加到"我的资产"

### 方式2：通过API调用

#### 创建智能体API

**请求**

```http
POST /agents/api/create
Content-Type: application/json
Cookie: SESSION_ID=xxx

{
  "name": "我的智能体",
  "description": "智能体描述",
  "agentType": "GAME_MAKER",
  "codeUrl": "ipfs://QmXXX",
  "modelUrl": "ipfs://QmYYY",
  "price": 0
}
```

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 5,
    "name": "我的智能体",
    "description": "智能体描述",
    "creatorId": 2,
    "creatorName": "testuser",
    "agentType": "GAME_MAKER",
    "status": "ACTIVE"
  }
}
```

**结果**: 智能体自动添加到 `user_assets` 表

#### 生成游戏API

**请求**

```http
POST /agents/api/game-maker/generate
Content-Type: application/json
Cookie: SESSION_ID=xxx

{
  "description": "一个简单的跑酷游戏",
  "gameType": "休闲游戏",
  "difficulty": "简单"
}
```

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "success": true,
    "gameId": 8,
    "gameTitle": "AI跑酷游戏",
    "gameCode": "// 游戏代码...",
    "message": "游戏生成成功！"
  }
}
```

**结果**: 游戏自动添加到 `user_assets` 表

---

## 测试验证

### 自动化测试脚本

运行 PowerShell 测试脚本：

```powershell
.\test-asset-auto-add.ps1
```

**测试流程**：

1. ✅ 注册新用户
2. ✅ 用户登录
3. ✅ 创建智能体
4. ✅ 生成游戏
5. ✅ 验证智能体资产
6. ✅ 验证游戏资产
7. ✅ 查看资产仪表板

### 数据库验证

运行 SQL 查询脚本：

```sql
-- 使用 verify-assets.sql 中的查询

-- 1. 查看所有用户资产
SELECT * FROM user_assets ORDER BY created_at DESC;

-- 2. 查看特定用户的资产
SELECT 
    ua.*,
    CASE 
        WHEN ua.asset_type = 'AGENT' THEN a.name
        WHEN ua.asset_type = 'GAME' THEN g.title
    END as asset_name
FROM user_assets ua
LEFT JOIN agents a ON ua.asset_type = 'AGENT' AND ua.asset_id = a.id
LEFT JOIN games g ON ua.asset_type = 'GAME' AND ua.asset_id = g.id
WHERE ua.user_id = 2;

-- 3. 检查是否有未添加的资产
SELECT a.* FROM agents a
LEFT JOIN user_assets ua ON ua.asset_id = a.id AND ua.asset_type = 'AGENT'
WHERE ua.id IS NULL;
```

### 手动测试步骤

#### 测试1：创建智能体

1. 打开浏览器，访问 `http://localhost:8080`
2. 登录系统（如果未登录）
3. 访问智能体上传页面：`http://localhost:8080/agents/upload`
4. 填写表单并提交
5. 访问"我的资产"页面：`http://localhost:8080/assets/my-agents`
6. ✅ 验证：刚创建的智能体显示在列表中

#### 测试2：生成游戏

1. 访问游戏制作页面：`http://localhost:8080/agents/game-maker`
2. 输入游戏描述并提交
3. 访问"我的资产"页面：`http://localhost:8080/assets/my-games`
4. ✅ 验证：刚生成的游戏显示在列表中

---

## API文档

### 查看用户资产

#### 获取智能体资产

```http
GET /assets/api/agents?page=1&size=10
Cookie: SESSION_ID=xxx
```

**响应**

```json
{
  "code": 200,
  "data": {
    "records": [
      {
        "id": 1,
        "userId": 2,
        "assetType": "AGENT",
        "assetId": 5,
        "assetName": "我的智能体",
        "assetDescription": "智能体描述",
        "acquisitionType": "CREATED",
        "isTradeable": true,
        "createdAt": "2024-01-20T10:30:00"
      }
    ],
    "total": 1,
    "page": 1,
    "size": 10
  }
}
```

#### 获取游戏资产

```http
GET /assets/api/games?page=1&size=10
Cookie: SESSION_ID=xxx
```

**响应**

```json
{
  "code": 200,
  "data": {
    "records": [
      {
        "id": 2,
        "userId": 2,
        "assetType": "GAME",
        "assetId": 8,
        "assetName": "AI跑酷游戏",
        "assetDescription": "一个简单的跑酷游戏",
        "acquisitionType": "CREATED",
        "isTradeable": true,
        "createdAt": "2024-01-20T11:00:00"
      }
    ],
    "total": 1,
    "page": 1,
    "size": 10
  }
}
```

#### 获取资产仪表板

```http
GET /assets/api/dashboard
Cookie: SESSION_ID=xxx
```

**响应**

```json
{
  "code": 200,
  "data": {
    "userId": 2,
    "username": "testuser",
    "walletAddress": "0x1234...",
    "totalAssetValue": "100.00",
    "gameCount": 3,
    "agentCount": 2,
    "itemCount": 0,
    "balances": [
      {
        "tokenType": "ETH",
        "tokenSymbol": "ETH",
        "balance": "1.5"
      }
    ],
    "recentAssets": [...]
  }
}
```

---

## 故障排查

### 问题1：资产未自动添加

**症状**: 创建智能体或游戏后，在"我的资产"中看不到

**可能原因**:
1. 未登录或登录过期
2. AssetService 添加失败
3. 数据库约束冲突

**解决方法**:

1. 检查用户是否已登录
2. 查看服务器日志：
   ```bash
   tail -f logs/application.log | grep "添加.*资产"
   ```
3. 检查数据库约束：
   ```sql
   -- 查看是否有重复记录
   SELECT * FROM user_assets 
   WHERE user_id = 2 AND asset_type = 'AGENT' AND asset_id = 5;
   ```
4. 手动添加资产（临时方案）：
   ```sql
   INSERT INTO user_assets (user_id, asset_type, asset_id, acquisition_type, is_tradeable)
   VALUES (2, 'AGENT', 5, 'CREATED', true);
   ```

### 问题2：重复添加资产

**症状**: 同一个智能体/游戏被添加多次

**可能原因**: 
- 防重复检查失败
- 并发请求

**解决方法**:

1. 检查数据库唯一约束：
   ```sql
   -- 添加唯一索引（如果不存在）
   CREATE UNIQUE INDEX idx_user_asset_unique 
   ON user_assets(user_id, asset_type, asset_id);
   ```

2. 清理重复记录：
   ```sql
   -- 保留最早的记录，删除重复项
   DELETE FROM user_assets
   WHERE id NOT IN (
       SELECT MIN(id) 
       FROM user_assets 
       GROUP BY user_id, asset_type, asset_id
   );
   ```

### 问题3：循环依赖错误

**症状**: 应用启动失败，报循环依赖错误

**可能原因**: Spring Bean 循环依赖

**解决方法**:

当前实现已避免循环依赖：
- `AgentService` 依赖 `AssetService` ✅
- `AssetService` 不依赖 `AgentService` ✅

如果仍有问题，可以使用 `@Lazy` 注解：

```java
@Service
public class AgentServiceImpl implements AgentService {
    private final AssetService assetService;
    
    public AgentServiceImpl(@Lazy AssetService assetService) {
        this.assetService = assetService;
    }
}
```

### 问题4：权限问题

**症状**: 获取用户ID失败，返回 null

**可能原因**:
- 未添加 `@RequireAuth` 注解
- Session 过期

**解决方法**:

1. 确保Controller方法有 `@RequireAuth` 注解：
   ```java
   @RequireAuth
   @PostMapping("/api/create")
   public Result<AgentVO> createAgent(...) {
       Long userId = UserContext.getCurrentUserId();
       // ...
   }
   ```

2. 检查拦截器配置：
   ```java
   @Configuration
   public class WebMvcConfig implements WebMvcConfigurer {
       @Override
       public void addInterceptors(InterceptorRegistry registry) {
           registry.addInterceptor(new AuthInterceptor())
                   .addPathPatterns("/**");
       }
   }
   ```

---

## 📝 日志说明

### 成功日志

```
[INFO] 智能体已添加到用户资产，用户ID: 2, 智能体ID: 5
[INFO] 游戏已添加到用户资产，用户ID: 2, 游戏ID: 8
```

### 警告日志

```
[WARN] 用户资产已存在，用户ID: 2, 资产类型: AGENT, 资产ID: 5
```

### 错误日志

```
[ERROR] 添加智能体到用户资产失败，用户ID: 2, 智能体ID: 5
java.lang.Exception: ...
```

---

## 🎉 总结

智能体和游戏自动添加到用户资产功能现已完成，主要特点：

1. ✅ **自动化**: 创建即拥有，无需手动操作
2. ✅ **统一管理**: 所有资产在一个表中
3. ✅ **安全可靠**: 防重复、容错处理
4. ✅ **易于扩展**: 支持更多资产类型
5. ✅ **完整追踪**: 记录获得方式和时间

---

## 📚 相关文档

- [ASSET_AUTO_ADD_IMPLEMENTATION.md](./ASSET_AUTO_ADD_IMPLEMENTATION.md) - 详细实现文档
- [verify-assets.sql](./verify-assets.sql) - SQL查询脚本
- [test-asset-auto-add.ps1](./test-asset-auto-add.ps1) - 自动化测试脚本

---

**更新时间**: 2024-01-20
**版本**: 1.0.0

