# 去中心化游戏平台 (Decentralized Gaming Platform)

基于区块链技术的综合性游戏生态系统，集成了游戏制作、智能体平台、资产管理、交易市场等核心功能。

## 🎮 项目特性

- **AI游戏制作**: 用户通过自然语言描述即可生成游戏
- **智能体生态**: 用户可上传和交易各类智能体
- **数字资产管理**: 统一管理游戏、智能体、代币等资产
- **去中心化交易**: 基于区块链的资产交易市场
- **钱包集成**: 通过Web3钱包实现用户身份验证
- **代币经济**: 完整的代币充值、消费、奖励体系

## 🛠️ 技术栈

| 技术层 | 技术选型 | 版本 |
|--------|----------|------|
| 前端 | Thymeleaf | 3.x |
| 后端 | Spring Boot | 3.3.4 |
| 数据层 | MyBatis-Plus | 3.5.9 |
| AI集成 | Spring AI | 1.0.0-M6 |
| 数据库 | MySQL | 8.0+ |
| 缓存 | Redis | 7.0+ |
| 存储 | IPFS | - |
| API文档 | Swagger | 2.2.0 |
| 区块链 | Web3j | 4.10.3 |
| 主链 | 以太坊 | - |
| 智能合约 | Solidity | 0.8.x |

## 📋 环境要求

- JDK 17+
- MySQL 8.0+
- Redis 7.0+
- Maven 3.6+
- Node.js (可选，用于前端开发)

## 🚀 快速启动

### 1. 克隆项目
```bash
git clone <repository-url>
cd DecentralizedGamingPlatform
```

### 2. 数据库配置

创建MySQL数据库：
```sql
CREATE DATABASE gaming_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

执行初始化脚本：
```bash
mysql -u root -p gaming_platform < src/main/resources/sql/init.sql
```

### 3. 配置文件

修改 `src/main/resources/application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gaming_platform?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
  
  data:
    redis:
      host: localhost
      port: 6379
      password: # 如果有密码请填写
```

### 4. 启动Redis

确保Redis服务正在运行：
```bash
redis-server
```

### 5. 运行项目

使用Maven启动：
```bash
mvn spring-boot:run
```

或者使用IDE直接运行 `DecentralizedGamingPlatformApplication.java`

### 6. 访问应用

- **主页**: http://localhost:8080
- **API文档**: http://localhost:8080/swagger-ui.html

## 📁 项目结构

```
DecentralizedGamingPlatform/
├── src/main/java/com/decentralized/gaming/platform/
│   ├── controller/          # 控制器层
│   │   └── blockchain/      # 区块链相关控制器
│   ├── service/            # 业务逻辑层
│   │   ├── blockchain/     # 区块链服务接口
│   │   │   └── impl/       # 区块链服务实现类
│   │   └── impl/           # 其他服务实现类
│   ├── mapper/             # 数据访问层
│   ├── entity/             # 实体类
│   ├── common/             # 通用类
│   ├── config/             # 配置类
│   ├── exception/          # 异常处理
│   ├── contracts/          # 智能合约接口
│   └── DecentralizedGamingPlatformApplication.java  # 启动类
├── src/main/resources/
│   ├── templates/          # Thymeleaf模板
│   │   ├── layout/         # 布局文件
│   │   ├── games/          # 游戏页面
│   │   ├── agents/         # 智能体页面
│   │   ├── assets/         # 资产管理页面
│   │   ├── marketplace/    # 交易市场页面
│   │   └── payment/        # 支付页面
│   ├── static/             # 静态资源
│   │   ├── css/            # 样式文件
│   │   ├── js/             # JavaScript文件
│   │   └── images/         # 图片资源
│   ├── sql/                # 数据库脚本
│   └── application.yml     # 配置文件
└── pom.xml                 # Maven配置
```

## 🔧 开发指南

### 添加新功能

1. 在相应的包中创建实体类 (entity)
2. 创建对应的Mapper接口 (mapper)
3. 实现业务逻辑 (service/impl)
4. 创建控制器 (controller)
5. 添加前端页面 (templates)

### API接口

所有API接口都以 `/api` 开头，主要模块包括：

- `/api/users` - 用户管理
- `/api/games` - 游戏管理
- `/api/agents` - 智能体管理
- `/api/marketplace` - 交易市场
- `/api/payment` - 支付充值
- `/api/blockchain` - 区块链服务
  - `/api/blockchain/health` - 区块链健康检查
  - `/api/blockchain/balance/{address}` - 查询余额
  - `/api/blockchain/events` - 事件监听
  - `/api/blockchain/transactions` - 交易管理

### 数据库变更

如需修改数据库结构：
1. 更新对应的实体类
2. 修改 `init.sql` 脚本
3. 如果是线上环境，创建迁移脚本

## 🌐 Web3集成

### 钱包连接

项目集成了Web3.js，支持MetaMask等Web3钱包：

```javascript
// 连接钱包
await connectWallet();

// 用户登录
await walletLogin();
```

### 智能合约

项目集成了完整的智能合约服务架构，支持：
- **平台代币 (ERC20)**: 通过 `PlatformTokenService` 管理
- **游戏NFT (ERC721)**: 通过 `GameNFTService` 管理
- **智能体NFT (ERC721)**: 通过 `AgentNFTService` 管理
- **交易市场合约**: 通过 `MarketplaceService` 管理
- **奖励系统**: 通过 `RewardsService` 管理

### 区块链服务架构

项目采用接口-实现类的分层架构：

- **服务接口层**: 定义清晰的服务契约
- **服务实现层**: 提供具体的业务逻辑实现
- **依赖注入**: 支持Spring的依赖注入机制
- **错误处理**: 统一的异常处理和重试机制
- **性能优化**: 缓存机制和异步操作
- **监控指标**: 完整的性能监控和指标收集

详细架构说明请参考 [区块链服务架构文档](docs/blockchain-service-architecture.md)

## 📚 API文档

启动项目后访问 http://localhost:8080/swagger-ui.html 查看完整的API文档。

## 🧪 测试

运行单元测试：
```bash
mvn test
```

## 📦 部署

### 打包应用
```bash
mvn clean package
```

### Docker部署
```bash
# 构建镜像
docker build -t gaming-platform .

# 运行容器
docker run -p 8080:8080 gaming-platform
```

## 🤝 贡献

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 详情请查看 [LICENSE](LICENSE) 文件

## 🔗 相关链接

- [Spring Boot 文档](https://spring.io/projects/spring-boot)
- [MyBatis-Plus 文档](https://baomidou.com/)
- [Web3.js 文档](https://web3js.readthedocs.io/)
- [Thymeleaf 文档](https://www.thymeleaf.org/)

## 📞 联系我们

如有问题或建议，请通过以下方式联系：

- 项目Issues: [GitHub Issues](https://github.com/your-repo/issues)
- 邮箱: contact@example.com

---

⚡ **快速开始你的区块链游戏开发之旅！**
