# 智能体和游戏自动添加到用户资产功能实现

## 📋 功能概述

**需求**: 当用户创建智能体或生成游戏后，自动将其添加到该用户的资产表（`user_assets`）中，实现资产所有权的统一管理。

## ✅ 实现内容

### 1. 修改文件

**文件**: `src/main/java/com/decentralized/gaming/platform/service/impl/AgentServiceImpl.java`

### 2. 实现的功能

#### 功能1：创建智能体后自动添加到资产表

**位置**: `createAgent()` 方法（第176-223行）

**实现逻辑**:
1. 创建智能体实体并保存到数据库
2. 调用 `assetService.addUserAsset()` 将智能体添加到用户资产表
3. 资产类型：`UserAsset.AssetType.AGENT`
4. 获得方式：`UserAsset.AcquisitionType.CREATED`

**代码片段**:
```java
agentMapper.insert(agent);

// 将智能体添加到创建者的资产表中
try {
    assetService.addUserAsset(
        creatorId, 
        UserAsset.AssetType.AGENT, 
        agent.getId(), 
        UserAsset.AcquisitionType.CREATED,
        agent.getContractAddress(),
        null
    );
    log.info("智能体已添加到用户资产，用户ID: {}, 智能体ID: {}", creatorId, agent.getId());
} catch (Exception e) {
    log.error("添加智能体到用户资产失败，用户ID: {}, 智能体ID: {}", creatorId, agent.getId(), e);
    // 不抛出异常，避免影响智能体创建流程
}
```

#### 功能2：生成游戏后自动添加到资产表

**位置**: `generateGame()` 方法（第226-302行）

**实现逻辑**:
1. 使用游戏制作智能体生成游戏
2. 创建游戏实体并保存到数据库
3. 调用 `assetService.addUserAsset()` 将游戏添加到用户资产表
4. 资产类型：`UserAsset.AssetType.GAME`
5. 获得方式：`UserAsset.AcquisitionType.CREATED`

**代码片段**:
```java
gameMapper.insert(game);

// 将游戏添加到创建者的资产表中
try {
    assetService.addUserAsset(
        userId, 
        UserAsset.AssetType.GAME, 
        game.getId(), 
        UserAsset.AcquisitionType.CREATED,
        game.getContractAddress(),
        null
    );
    log.info("游戏已添加到用户资产，用户ID: {}, 游戏ID: {}", userId, game.getId());
} catch (Exception ex) {
    log.error("添加游戏到用户资产失败，用户ID: {}, 游戏ID: {}", userId, game.getId(), ex);
    // 不抛出异常，避免影响游戏创建流程
}
```

### 3. 依赖注入

**新增依赖**: 在 `AgentServiceImpl` 中注入 `AssetService`

```java
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AgentServiceImpl implements AgentService {
    private final AgentMapper agentMapper;
    private final GameMapper gameMapper;
    private final UserMapper userMapper;
    private final AssetService assetService;  // ✅ 新增依赖
}
```

**新增导入**:
```java
import com.decentralized.gaming.platform.service.AssetService;
import com.decentralized.gaming.platform.entity.UserAsset;
```

## 🔄 业务流程

### 创建智能体流程

```
用户请求创建智能体
    ↓
AgentController.createAgent()
    ↓
AgentService.createAgent()
    ↓
1. 验证用户存在
2. 创建智能体实体
3. 保存到数据库（agents表）
4. 添加到用户资产表（user_assets表）✅ 新增
5. 返回智能体信息
```

### 生成游戏流程

```
用户请求生成游戏
    ↓
AgentController.generateGame()
    ↓
AgentService.generateGame()
    ↓
1. 验证用户存在
2. 查找游戏制作智能体
3. 生成游戏代码
4. 创建游戏实体
5. 保存到数据库（games表）
6. 添加到用户资产表（user_assets表）✅ 新增
7. 增加智能体使用次数
8. 返回生成结果
```

## 📊 数据库影响

### user_assets 表结构

| 字段 | 类型 | 说明 | 示例值 |
|------|------|------|--------|
| id | BIGINT | 主键 | 自增 |
| user_id | BIGINT | 用户ID | 登录用户的ID |
| asset_type | VARCHAR(20) | 资产类型 | AGENT / GAME |
| asset_id | BIGINT | 资产ID | 对应智能体或游戏的ID |
| contract_address | VARCHAR(42) | 合约地址 | 如果有NFT合约 |
| token_id | VARCHAR(255) | Token ID | NFT的Token ID |
| acquisition_type | VARCHAR(20) | 获得方式 | CREATED |
| is_tradeable | BOOLEAN | 是否可交易 | true |
| created_at | TIMESTAMP | 创建时间 | 自动填充 |
| updated_at | TIMESTAMP | 更新时间 | 自动填充 |

### 资产记录示例

**创建智能体后**:
```sql
INSERT INTO user_assets (user_id, asset_type, asset_id, acquisition_type, is_tradeable)
VALUES (2, 'AGENT', 5, 'CREATED', true);
```

**生成游戏后**:
```sql
INSERT INTO user_assets (user_id, asset_type, asset_id, acquisition_type, is_tradeable)
VALUES (2, 'GAME', 8, 'CREATED', true);
```

## 🛡️ 异常处理

### 设计原则

**容错处理**: 添加资产失败不影响主流程

```java
try {
    assetService.addUserAsset(...);
    log.info("资产添加成功");
} catch (Exception e) {
    log.error("添加资产失败", e);
    // 不抛出异常，避免影响创建流程
}
```

### 原因

1. **核心业务优先**: 智能体/游戏创建成功是核心
2. **可补救性**: 可以后续通过管理工具补充资产记录
3. **用户体验**: 避免因资产记录失败导致创建失败
4. **日志追踪**: 通过日志记录异常，便于后续处理

## 🔍 验证方法

### 1. 创建智能体验证

```bash
# 1. 登录并创建智能体
curl -X POST http://localhost:8080/agents/api/create \
  -H "Content-Type: application/json" \
  -H "Cookie: SESSION_ID=xxx" \
  -d '{
    "name": "测试智能体",
    "description": "测试描述",
    "agentType": "GAME_MAKER",
    "codeUrl": "ipfs://xxx",
    "modelUrl": "ipfs://yyy",
    "price": 0
  }'

# 2. 查询用户资产
curl http://localhost:8080/assets/api/agents?page=1&size=10 \
  -H "Cookie: SESSION_ID=xxx"
```

### 2. 生成游戏验证

```bash
# 1. 登录并生成游戏
curl -X POST http://localhost:8080/agents/api/game-maker/generate \
  -H "Content-Type: application/json" \
  -H "Cookie: SESSION_ID=xxx" \
  -d '{
    "description": "一个简单的跑酷游戏",
    "gameType": "休闲",
    "difficulty": "简单"
  }'

# 2. 查询用户游戏资产
curl http://localhost:8080/assets/api/games?page=1&size=10 \
  -H "Cookie: SESSION_ID=xxx"
```

### 3. 数据库验证

```sql
-- 查询用户的所有资产
SELECT 
    ua.id,
    ua.user_id,
    ua.asset_type,
    ua.asset_id,
    ua.acquisition_type,
    ua.created_at,
    CASE 
        WHEN ua.asset_type = 'AGENT' THEN a.name
        WHEN ua.asset_type = 'GAME' THEN g.title
    END as asset_name
FROM user_assets ua
LEFT JOIN agents a ON ua.asset_type = 'AGENT' AND ua.asset_id = a.id
LEFT JOIN games g ON ua.asset_type = 'GAME' AND ua.asset_id = g.id
WHERE ua.user_id = 2  -- 替换为实际用户ID
ORDER BY ua.created_at DESC;
```

## 📈 功能优势

### 1. 统一资产管理

- ✅ 所有用户资产在一个表中统一管理
- ✅ 方便查询用户拥有的所有资产
- ✅ 支持资产的转移和交易

### 2. 完整的所有权追踪

- ✅ 记录资产获得方式（创建、购买、奖励、转移）
- ✅ 记录获得时间
- ✅ 支持NFT合约地址和Token ID

### 3. 便于功能扩展

- ✅ 资产交易市场
- ✅ 资产转移
- ✅ 资产NFT化
- ✅ 资产价值统计

### 4. 用户体验提升

- ✅ "我的资产"页面可以展示所有资产
- ✅ 资产统计更准确
- ✅ 资产权限控制更清晰

## 🔐 安全性考虑

### 1. 用户身份验证

- ✅ 使用 `@RequireAuth` 注解确保用户已登录
- ✅ 使用 `UserContext.getCurrentUserId()` 获取当前用户ID
- ✅ 避免从前端传递用户ID，防止伪造

### 2. 防止重复添加

`AssetService.addUserAsset()` 方法内部会检查是否已存在：

```java
// 检查是否已存在
LambdaQueryWrapper<UserAsset> queryWrapper = new LambdaQueryWrapper<UserAsset>()
    .eq(UserAsset::getUserId, userId)
    .eq(UserAsset::getAssetType, assetType)
    .eq(UserAsset::getAssetId, assetId);

UserAsset existingAsset = userAssetMapper.selectOne(queryWrapper);
if (existingAsset != null) {
    log.warn("用户资产已存在");
    return false;
}
```

### 3. 事务一致性

- ✅ 创建和添加资产都在 `@Transactional` 事务中
- ✅ 确保数据一致性

## 📝 日志记录

### 成功日志

```
智能体已添加到用户资产，用户ID: 2, 智能体ID: 5
游戏已添加到用户资产，用户ID: 2, 游戏ID: 8
```

### 错误日志

```
添加智能体到用户资产失败，用户ID: 2, 智能体ID: 5
添加游戏到用户资产失败，用户ID: 2, 游戏ID: 8
```

## 🚀 后续优化建议

### 1. 异步处理

考虑使用消息队列异步处理资产添加，进一步解耦：

```java
// 发送消息到队列
assetEventPublisher.publish(new AssetCreatedEvent(userId, AssetType.AGENT, agentId));
```

### 2. 重试机制

如果资产添加失败，可以实现重试机制：

```java
@Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
public boolean addUserAsset(...) {
    // ...
}
```

### 3. 资产快照

定期创建资产快照，用于审计和恢复：

```java
public void createAssetSnapshot(Long userId) {
    // 记录用户在某个时间点的所有资产
}
```

### 4. 资产转移历史

记录资产的完整转移历史：

```sql
CREATE TABLE asset_transfer_history (
    id BIGINT PRIMARY KEY,
    asset_id BIGINT,
    asset_type VARCHAR(20),
    from_user_id BIGINT,
    to_user_id BIGINT,
    transfer_type VARCHAR(20),
    created_at TIMESTAMP
);
```

## ✨ 总结

本次实现完成了智能体和游戏创建后自动添加到用户资产表的功能，实现了：

1. ✅ 自动资产所有权记录
2. ✅ 统一的资产管理体系
3. ✅ 完善的异常处理
4. ✅ 详细的日志记录
5. ✅ 良好的安全性和可扩展性

用户现在可以在"我的资产"页面看到自己创建的所有智能体和游戏，资产管理功能更加完善。

