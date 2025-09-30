# 区块链服务架构说明

## 架构概览

重构后的区块链服务采用接口-实现类的分层架构，遵循依赖倒置原则和单一职责原则。

## 包结构

```
com.decentralized.gaming.platform.service.blockchain/
├── 接口层 (Interfaces)
│   ├── BlockchainService.java
│   ├── BlockchainRetryService.java
│   ├── BlockchainCacheService.java
│   ├── PlatformTokenService.java
│   ├── GameNFTService.java
│   ├── AgentNFTService.java
│   ├── MarketplaceService.java
│   ├── RewardsService.java
│   ├── EventListeningService.java
│   ├── ContractConfigService.java
│   ├── BlockchainMetricsService.java
│   ├── TransactionMonitoringService.java
│   └── TransactionHistoryService.java
└── impl/ (实现层)
    ├── BlockchainServiceImpl.java
    ├── BlockchainRetryServiceImpl.java
    ├── BlockchainCacheServiceImpl.java
    ├── PlatformTokenServiceImpl.java
    ├── GameNFTServiceImpl.java
    ├── AgentNFTServiceImpl.java
    ├── MarketplaceServiceImpl.java
    ├── RewardsServiceImpl.java
    ├── EventListeningServiceImpl.java
    ├── ContractConfigServiceImpl.java
    ├── BlockchainMetricsServiceImpl.java
    ├── TransactionMonitoringServiceImpl.java
    └── TransactionHistoryServiceImpl.java
```

## 服务分层

### 1. 基础服务层
- **BlockchainService**: 提供区块链基础操作
- **BlockchainRetryService**: 提供重试和降级机制
- **BlockchainCacheService**: 提供数据缓存功能

### 2. 业务服务层
- **PlatformTokenService**: 平台代币操作
- **GameNFTService**: 游戏NFT操作
- **AgentNFTService**: 代理NFT操作
- **MarketplaceService**: 市场交易操作
- **RewardsService**: 奖励发放操作

### 3. 监控服务层
- **EventListeningService**: 事件监听和处理
- **TransactionMonitoringService**: 交易状态监控
- **TransactionHistoryService**: 交易历史查询
- **BlockchainMetricsService**: 性能指标收集

### 4. 配置服务层
- **ContractConfigService**: 合约配置管理

## 依赖关系

```
Controller Layer
    ↓ (依赖注入)
Service Interface Layer
    ↓ (实现)
Service Implementation Layer
    ↓ (使用)
Web3j & Blockchain Network
```

## 关键特性

### 1. 依赖注入
- 所有服务都通过Spring的`@Autowired`进行依赖注入
- 控制器只依赖接口，不依赖具体实现

### 2. 错误处理
- 统一的异常处理机制
- 重试和降级策略

### 3. 性能优化
- 缓存机制减少区块链调用
- 异步操作提高响应速度
- 指标收集支持性能监控

### 4. 可扩展性
- 接口设计支持多种实现
- 便于添加新功能和服务

## 使用模式

### 控制器使用服务
```java
@RestController
public class BlockchainController {
    @Autowired
    private BlockchainService blockchainService;
    
    @GetMapping("/balance/{address}")
    public Result<Object> getBalance(@PathVariable String address) {
        BigDecimal balance = blockchainService.getBalance(address);
        return Result.success(balance);
    }
}
```

### 服务间协作
```java
@Service
public class GameNFTServiceImpl implements GameNFTService {
    @Autowired
    private BlockchainService blockchainService;
    
    @Autowired
    private BlockchainRetryService retryService;
    
    @Override
    public TransactionReceipt createGame(...) {
        return retryService.executeWithRetry(() -> {
            // 使用blockchainService进行区块链操作
            return performBlockchainOperation();
        }, "创建游戏");
    }
}
```

## 测试策略

### 1. 单元测试
- 为每个接口创建测试类
- 使用Mock对象模拟依赖
- 验证接口契约的正确性

### 2. 集成测试
- 测试Spring容器的依赖注入
- 验证服务间的协作
- 测试端到端的功能

### 3. 性能测试
- 验证缓存机制的效果
- 测试重试机制的性能影响
- 监控指标收集的准确性

## 最佳实践

### 1. 接口设计
- 保持接口的简洁和专注
- 使用合适的参数类型和返回值
- 提供清晰的文档注释

### 2. 实现类设计
- 遵循单一职责原则
- 使用依赖注入而非硬编码依赖
- 提供适当的错误处理

### 3. 测试设计
- 为每个公共方法编写测试
- 使用有意义的测试数据
- 验证边界条件和异常情况

## 未来扩展

### 1. 新服务添加
- 定义新的服务接口
- 创建对应的实现类
- 更新依赖注入配置

### 2. 功能增强
- 在现有接口中添加新方法
- 保持向后兼容性
- 更新相关文档

### 3. 性能优化
- 添加更多缓存策略
- 优化异步操作
- 增强监控能力
