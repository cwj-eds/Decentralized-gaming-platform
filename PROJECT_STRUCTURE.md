# 去中心化游戏平台 - 项目结构说明

## 项目概述
基于Spring Boot 3.x的去中心化游戏平台，集成了区块链技术、AI智能体、游戏制作、资产管理等核心功能。

## 已创建的文件结构

### 1. 实体类 (Entity)
位置：`src/main/java/com/decentralized/gaming/platform/entity/`

- **User.java** - 用户实体类
- **UserBalance.java** - 用户代币余额实体类
- **Game.java** - 游戏实体类（包含GameStatus枚举）
- **GameItem.java** - 游戏道具实体类（包含ItemRarity枚举）
- **Agent.java** - 智能体实体类（包含AgentStatus枚举）
- **MarketplaceItem.java** - 市场商品实体类（包含ItemType和ItemStatus枚举）
- **Transaction.java** - 交易记录实体类（包含TransactionStatus枚举）
- **RechargeRecord.java** - 充值记录实体类（包含RechargeStatus枚举）
- **GameSession.java** - 游戏会话实体类（包含SessionStatus枚举）
- **UserAsset.java** - 用户资产实体类（包含AssetType和AcquisitionType枚举）
- **SystemConfig.java** - 系统配置实体类（包含ConfigType枚举）

### 2. 数据传输对象 (DTO)
位置：`src/main/java/com/decentralized/gaming/platform/dto/`

- **WalletLoginRequest.java** - 钱包登录请求DTO
- **UpdateUserRequest.java** - 更新用户信息请求DTO
- **CreateGameRequest.java** - 创建游戏请求DTO
- **CreateAgentRequest.java** - 创建智能体请求DTO
- **GameGenerationRequest.java** - 游戏生成请求DTO
- **AgentUsageRequest.java** - 智能体使用请求DTO
- **ListItemRequest.java** - 上架商品请求DTO
- **CreateRechargeRequest.java** - 创建充值订单请求DTO
- **ConfirmPaymentRequest.java** - 确认支付请求DTO

### 3. 视图对象 (VO)
位置：`src/main/java/com/decentralized/gaming/platform/vo/`

- **UserVO.java** - 用户视图对象
- **UserBalanceVO.java** - 用户余额视图对象
- **GameVO.java** - 游戏视图对象
- **AgentVO.java** - 智能体视图对象
- **MarketplaceItemVO.java** - 市场商品视图对象
- **TransactionVO.java** - 交易记录视图对象
- **RechargeRecordVO.java** - 充值记录视图对象
- **GameSessionVO.java** - 游戏会话视图对象
- **UserAssetVO.java** - 用户资产视图对象
- **GameGenerationResult.java** - 游戏生成结果视图对象
- **AgentUsageResult.java** - 智能体使用结果视图对象
- **RechargeOrderVO.java** - 充值订单视图对象

### 4. 枚举类 (Enums)
位置：`src/main/java/com/decentralized/gaming/platform/enums/`

- **TokenType.java** - 代币类型枚举
- **PaymentMethod.java** - 支付方式枚举
- **AgentType.java** - 智能体类型枚举
- **GameType.java** - 游戏类型枚举
- **GameDifficulty.java** - 游戏难度枚举
- **BlockchainNetwork.java** - 区块链网络枚举

### 5. 数据库表结构
位置：`src/main/resources/sql/schema.sql`

包含以下数据库表：
- **users** - 用户表
- **user_balances** - 用户代币余额表
- **games** - 游戏表
- **game_items** - 游戏道具表
- **agents** - 智能体表
- **marketplace_items** - 市场商品表
- **transactions** - 交易记录表
- **recharge_records** - 充值记录表
- **game_sessions** - 游戏会话表
- **user_assets** - 用户资产表
- **system_configs** - 系统配置表

## 技术特性

### 1. 数据验证
- 使用Jakarta Validation API进行数据验证
- 支持@NotBlank、@NotNull、@Size、@Email、@Pattern等注解
- 符合Spring Boot 3.x规范

### 2. 数据库设计
- 使用MyBatis-Plus注解进行ORM映射
- 支持自动填充创建时间和更新时间
- 完整的索引和外键约束设计
- 支持JSON字段存储复杂数据

### 3. 业务模型
- 完整的用户管理系统
- 游戏制作和发布流程
- 智能体生态管理
- 去中心化交易市场
- 数字资产管理
- 支付充值系统

### 4. 区块链集成
- 支持多种区块链网络
- NFT资产管理
- 智能合约集成
- 钱包地址验证

## 下一步开发建议

1. **创建Mapper接口** - 为每个实体类创建对应的MyBatis-Plus Mapper接口
2. **实现Service层** - 创建业务逻辑服务类
3. **开发Controller层** - 实现REST API接口
4. **集成Web3.js** - 实现前端区块链交互
5. **添加单元测试** - 为关键业务逻辑编写测试用例
6. **配置安全认证** - 实现JWT或OAuth2认证
7. **集成AI服务** - 实现游戏生成智能体功能

## 注意事项

1. 所有验证注解已更新为Jakarta规范，兼容Spring Boot 3.x
2. 数据库表结构包含完整的索引和外键约束
3. 实体类使用Lombok简化代码
4. 枚举类提供完整的业务状态管理
5. DTO和VO分离，确保数据传输安全

项目结构完整，可以直接开始后续的业务逻辑开发。
