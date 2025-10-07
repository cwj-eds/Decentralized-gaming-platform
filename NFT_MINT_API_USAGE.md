# NFT铸造API使用文档

## 📋 概述

本文档介绍了去中心化游戏平台中智能体和游戏NFT铸造相关的API接口，方便其他开发人员调用。

## 🚀 快速开始

### 基础URL
```
http://localhost:8080/api/nft
```

### 认证方式
目前API不需要特殊认证，后续可集成JWT或其他认证方式。

## 🎯 API接口详情

### 1. 智能体NFT铸造

#### 1.1 铸造智能体NFT
```http
POST /api/nft/agent/mint
Content-Type: application/json

{
    "creatorPrivateKey": "0x1234567890abcdef...",
    "agentName": "GPT-4游戏助手",
    "agentDescription": "一个专门用于游戏开发的AI助手",
    "agentAvatar": "https://example.com/agent-avatar.png",
    "agentType": "GPT-4",
    "capabilities": "游戏开发、关卡设计、角色创建",
    "personality": "友好、专业、创新",
    "codeHash": "0x1234567890abcdef",
    "modelHash": "0xabcdef1234567890",
    "price": "1000000000000000000"
}
```

**响应示例:**
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "success": true,
        "tokenId": "1",
        "transactionHash": "0x1234567890abcdef...",
        "contractAddress": "0x9fE46736679d2D9a65F0992F2272dE9f3c7fa6e0",
        "metadataUri": "https://ipfs.io/ipfs/QmXXX...",
        "gasUsed": "150000",
        "timestamp": 1640995200000
    }
}
```

#### 1.2 管理员铸造智能体NFT
```http
POST /api/nft/agent/admin-mint?adminPrivateKey=0x...&to=0x...
Content-Type: application/json

{
    "agentName": "管理员智能体",
    "agentDescription": "管理员创建的智能体",
    "agentAvatar": "https://example.com/admin-agent.png",
    "agentType": "Admin",
    "capabilities": "管理、监控、维护",
    "personality": "专业、可靠",
    "codeHash": "0xadmin1234567890",
    "modelHash": "0xmodel1234567890",
    "price": "0"
}
```

#### 1.3 获取智能体铸造费用
```http
GET /api/nft/agent/mint-fee
```

**响应示例:**
```json
{
    "code": 200,
    "message": "success",
    "data": "1000000000000000000"
}
```

#### 1.4 验证智能体NFT所有权
```http
GET /api/nft/agent/{tokenId}/owner/{ownerAddress}
```

**响应示例:**
```json
{
    "code": 200,
    "message": "success",
    "data": true
}
```

#### 1.5 获取智能体NFT信息
```http
GET /api/nft/agent/{tokenId}/info
```

**响应示例:**
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "owner": "0x742d35Cc6634C0532925a3b8D4C9db96C4b4d8b6",
        "tokenURI": "https://ipfs.io/ipfs/QmXXX...",
        "tokenId": "1"
    }
}
```

### 2. 游戏NFT铸造

#### 2.1 铸造游戏NFT
```http
POST /api/nft/game/mint
Content-Type: application/json

{
    "creatorPrivateKey": "0x1234567890abcdef...",
    "gameName": "去中心化冒险游戏",
    "gameDescription": "一个基于区块链的冒险游戏",
    "gameImageUrl": "https://example.com/game-cover.png",
    "gameType": "RPG",
    "difficulty": "MEDIUM",
    "tags": "冒险,角色扮演,区块链",
    "codeHash": "0x1234567890abcdef",
    "price": "5000000000000000000"
}
```

**响应示例:**
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "success": true,
        "tokenId": "2",
        "transactionHash": "0xabcdef1234567890...",
        "contractAddress": "0x9fE46736679d2D9a65F0992F2272dE9f3c7fa6e0",
        "metadataUri": "https://ipfs.io/ipfs/QmYYY...",
        "gasUsed": "180000",
        "timestamp": 1640995200000
    }
}
```

#### 2.2 生成并铸造游戏NFT
```http
POST /api/nft/game/generate-mint
Content-Type: application/json

{
    "creatorPrivateKey": "0x1234567890abcdef...",
    "gameName": "AI生成游戏",
    "gameDescription": "使用AI智能体生成的游戏",
    "gameImageUrl": "https://example.com/ai-game.png",
    "gameType": "PUZZLE",
    "difficulty": "EASY",
    "tags": "AI生成,益智,创新",
    "codeHash": "0xai1234567890",
    "price": "3000000000000000000"
}
```

#### 2.3 管理员铸造游戏NFT
```http
POST /api/nft/game/admin-mint?adminPrivateKey=0x...&to=0x...
Content-Type: application/json

{
    "gameName": "管理员游戏",
    "gameDescription": "管理员创建的游戏",
    "gameImageUrl": "https://example.com/admin-game.png",
    "gameType": "ADMIN",
    "difficulty": "HARD",
    "tags": "管理,特殊",
    "codeHash": "0xadmin1234567890",
    "price": "0"
}
```

#### 2.4 获取游戏铸造费用
```http
GET /api/nft/game/mint-fee
```

#### 2.5 验证游戏NFT所有权
```http
GET /api/nft/game/{tokenId}/owner/{ownerAddress}
```

#### 2.6 获取游戏NFT信息
```http
GET /api/nft/game/{tokenId}/info
```

## 🔧 开发集成示例

### Java客户端示例

```java
@Service
public class NFTMintClient {
    
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8080/api/nft";
    
    // 铸造智能体NFT
    public NFTMintResponse mintAgentNFT(AgentMintRequest request) {
        String url = baseUrl + "/agent/mint";
        Result<NFTMintResponse> result = restTemplate.postForObject(url, request, Result.class);
        return result.getData();
    }
    
    // 铸造游戏NFT
    public NFTMintResponse mintGameNFT(GameMintRequest request) {
        String url = baseUrl + "/game/mint";
        Result<NFTMintResponse> result = restTemplate.postForObject(url, request, Result.class);
        return result.getData();
    }
    
    // 验证NFT所有权
    public boolean verifyOwnership(String type, BigInteger tokenId, String ownerAddress) {
        String url = baseUrl + "/" + type + "/" + tokenId + "/owner/" + ownerAddress;
        Result<Boolean> result = restTemplate.getForObject(url, Result.class);
        return result.getData();
    }
}
```

### JavaScript客户端示例

```javascript
class NFTMintClient {
    constructor(baseUrl = 'http://localhost:8080/api/nft') {
        this.baseUrl = baseUrl;
    }
    
    // 铸造智能体NFT
    async mintAgentNFT(request) {
        const response = await fetch(`${this.baseUrl}/agent/mint`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(request)
        });
        return await response.json();
    }
    
    // 铸造游戏NFT
    async mintGameNFT(request) {
        const response = await fetch(`${this.baseUrl}/game/mint`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(request)
        });
        return await response.json();
    }
    
    // 验证NFT所有权
    async verifyOwnership(type, tokenId, ownerAddress) {
        const response = await fetch(`${this.baseUrl}/${type}/${tokenId}/owner/${ownerAddress}`);
        return await response.json();
    }
}

// 使用示例
const client = new NFTMintClient();

// 铸造智能体NFT
const agentRequest = {
    creatorPrivateKey: "0x1234567890abcdef...",
    agentName: "我的AI助手",
    agentDescription: "一个智能游戏助手",
    agentType: "GPT-4",
    capabilities: "游戏开发、关卡设计",
    personality: "友好、专业"
};

client.mintAgentNFT(agentRequest).then(response => {
    console.log('智能体NFT铸造成功:', response);
});
```

## 📊 错误处理

### 常见错误码

| 错误码 | 说明 | 解决方案 |
|--------|------|----------|
| 400 | 请求参数错误 | 检查请求参数格式和必填字段 |
| 500 | 服务器内部错误 | 检查私钥格式和区块链连接状态 |
| 1001 | 铸造费用不足 | 确保账户有足够的代币余额 |
| 1002 | 合约调用失败 | 检查合约地址和网络连接 |

### 错误响应示例

```json
{
    "code": 500,
    "message": "铸造智能体NFT失败: 私钥格式错误",
    "data": null
}
```

## 🔒 安全注意事项

1. **私钥安全**: 私钥应该安全存储，不要硬编码在客户端代码中
2. **网络安全**: 生产环境请使用HTTPS
3. **权限控制**: 管理员接口需要严格的权限验证
4. **输入验证**: 所有输入参数都需要进行验证和过滤

## 📈 性能优化建议

1. **批量操作**: 对于大量NFT铸造，考虑实现批量铸造接口
2. **异步处理**: 对于耗时的操作，考虑使用异步处理
3. **缓存机制**: 对于频繁查询的数据，可以添加缓存
4. **Gas优化**: 合理设置Gas价格和限制

## 🚀 扩展功能

### 计划中的功能

1. **批量铸造**: 支持一次铸造多个NFT
2. **稀有度系统**: 为NFT添加稀有度属性
3. **交易功能**: 支持NFT的买卖交易
4. **元数据更新**: 支持NFT元数据的更新
5. **事件监听**: 提供NFT相关事件的监听接口

### 自定义扩展

开发人员可以通过以下方式扩展功能：

1. **实现自定义元数据服务**: 继承`NFTMetadataService`接口
2. **添加新的NFT类型**: 创建新的DTO和服务实现
3. **集成外部服务**: 如AI服务、IPFS服务等
4. **自定义验证逻辑**: 在服务层添加业务验证

## 📞 技术支持

如有问题或建议，请联系开发团队或提交Issue。

---

**版本**: 1.0.0  
**更新时间**: 2024-01-01  
**维护者**: DecentralizedGamingPlatform Team
