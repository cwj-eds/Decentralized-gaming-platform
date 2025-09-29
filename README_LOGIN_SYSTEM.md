# 去中心化游戏平台 - 登录注册系统

## 概述

去中心化游戏平台提供了完整的用户认证系统，支持钱包登录/注册和传统用户名密码注册两种方式。系统采用JWT令牌进行身份验证，确保API的安全性。

## 功能特性

### ✅ 已完成的核心功能

1. **双重登录方式**
   - 🔐 **钱包登录/注册**：使用MetaMask等Web3钱包进行签名认证
   - 👤 **传统注册**：用户名+邮箱+密码的传统注册方式

2. **JWT认证系统**
   - 🔑 JWT令牌生成和验证
   - 🔒 安全的API访问控制
   - ⏰ 令牌过期管理

3. **前端集成**
   - 🎨 响应式登录/注册页面
   - 🌐 Web3.js区块链集成
   - 📱 移动端适配设计

4. **安全特性**
   - 🔐 BCrypt密码加密
   - ✍️ ECDSA签名验证
   - 🛡️ 输入验证和SQL注入防护

## 技术架构

### 后端技术栈
- **Spring Boot 3.3.4**：企业级Java框架
- **MyBatis-Plus 3.5.9**：数据库操作框架
- **MySQL 8.0+**：关系型数据库
- **JWT**：JSON Web Token认证
- **Spring Security**：安全框架
- **Web3j 4.10.3**：区块链集成

### 前端技术栈
- **Thymeleaf**：服务端模板引擎
- **Web3.js**：区块链JavaScript库
- **Bootstrap 5**：CSS框架
- **JavaScript ES6+**：现代JavaScript

## 数据库设计

### 用户表结构
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    wallet_address VARCHAR(42) UNIQUE NOT NULL COMMENT '钱包地址',
    username VARCHAR(50) COMMENT '用户名',
    email VARCHAR(100) COMMENT '邮箱',
    avatar_url VARCHAR(255) COMMENT '头像URL',
    password VARCHAR(255) COMMENT '密码（加密存储）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 用户代币余额表
```sql
CREATE TABLE user_balances (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    token_type VARCHAR(20) NOT NULL COMMENT '代币类型',
    balance DECIMAL(20,8) NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## API 接口文档

### 1. 钱包登录/注册
**POST** `/api/users/wallet-login`

**请求体：**
```json
{
    "walletAddress": "0x742d35cc6aa8b8c7d8f5e8b9c8b7c6d5e4f3a2b1c",
    "signature": "0x...",
    "message": "登录到去中心化游戏平台\n时间戳: 1234567890"
}
```

**响应：**
```json
{
    "success": true,
    "message": "登录成功",
    "data": {
        "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
        "tokenType": "Bearer",
        "expiresIn": 86400,
        "user": {
            "id": 1,
            "walletAddress": "0x742d35cc6aa8b8c7d8f5e8b9c8b7c6d5e4f3a2b1c",
            "username": "testuser",
            "email": "test@example.com"
        }
    }
}
```

### 2. 用户注册
**POST** `/api/users/register`

**请求体：**
```json
{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "confirmPassword": "password123",
    "walletAddress": "0x742d35cc6aa8b8c7d8f5e8b9c8b7c6d5e4f3a2b1c"
}
```

### 3. 获取用户信息
**GET** `/api/users/{userId}`

需要Authorization头：`Bearer {token}`

### 4. 检查钱包是否已注册
**GET** `/api/users/check-wallet?walletAddress=0x...`

## 前端页面

### 登录页面 (`/auth/login`)
- **钱包连接登录**：一键连接MetaMask钱包进行签名登录
- **传统登录**：用户名密码登录（预留接口）
- **响应式设计**：适配桌面和移动设备

### 注册页面 (`/auth/register`)
- **钱包连接注册**：连接钱包后自动注册
- **传统注册**：完整注册表单
- **密码强度检查**：实时密码强度提示
- **表单验证**：实时输入验证

## 使用流程

### 1. 钱包登录流程
1. 用户点击"连接MetaMask钱包"按钮
2. 浏览器调用`eth_requestAccounts`获取用户钱包地址
3. 前端生成登录消息并请求用户签名
4. 调用`/api/users/wallet-login`接口进行登录/注册
5. 后端验证签名并创建或更新用户信息
6. 返回JWT令牌并跳转到首页

### 2. 传统注册流程
1. 用户填写用户名、邮箱、密码等信息
2. 前端验证表单数据完整性
3. 调用`/api/users/register`接口
4. 创建用户账户并返回JWT令牌
5. 跳转到登录页面

## JWT认证机制

### 令牌结构
```json
{
    "alg": "HS512",
    "typ": "JWT"
}
{
    "sub": "1",
    "walletAddress": "0x742d35cc6aa8b8c7d8f5e8b9c8b7c6d5e4f3a2b1c",
    "iat": 1234567890,
    "exp": 1234654290
}
```

### 认证流程
1. 用户登录成功后，后端生成JWT令牌
2. 前端将令牌存储在localStorage中
3. 后续API请求在Authorization头中携带Bearer令牌
4. 后端验证令牌有效性并提取用户信息

### 令牌刷新
当前版本不支持令牌刷新，过期后需要重新登录。

## 安全特性

### 1. 密码安全
- **BCrypt加密**：使用强度为12的BCrypt哈希
- **密码强度验证**：至少6位，包含字母和数字
- **防暴力破解**：密码错误次数限制（可扩展）

### 2. 签名验证
- **ECDSA签名**：验证钱包签名真实性
- **防重放攻击**：使用时间戳防止消息重放
- **地址验证**：验证恢复的地址与提供的地址匹配

### 3. 输入验证
- **钱包地址格式**：严格的以太坊地址格式验证
- **邮箱格式**：标准邮箱格式验证
- **SQL注入防护**：使用MyBatis-Plus参数化查询

## 部署和运行

### 环境要求
- **Java 17+**
- **MySQL 8.0+**
- **MetaMask浏览器扩展**

### 数据库初始化
```sql
-- 运行项目中的schema.sql文件
source src/main/resources/sql/schema.sql
```

### 启动应用
```bash
# 编译项目
mvn clean compile

# 运行应用
mvn spring-boot:run

# 访问地址
# 前端页面: http://localhost:8080
# 登录页面: http://localhost:8080/auth/login
# 注册页面: http://localhost:8080/auth/register
# API文档: http://localhost:8080/swagger-ui.html
```

## 配置说明

### application.yml配置
```yaml
app:
  jwt:
    secret: decentralized-gaming-platform-secret-key-2024
    expiration: 86400  # 24小时
```

### 数据库配置
在`system_configs`表中的JWT相关配置：
- `auth.jwt.secret`: JWT签名密钥
- `auth.jwt.expiration`: 令牌过期时间（秒）

## 测试指南

### 功能测试
1. **钱包登录测试**：
   - 安装MetaMask
   - 连接测试网络
   - 访问登录页面
   - 点击"连接MetaMask钱包"
   - 确认签名请求
   - 验证自动跳转

2. **传统注册测试**：
   - 访问注册页面
   - 填写注册信息
   - 验证密码强度提示
   - 提交注册表单
   - 验证跳转到登录页面

3. **API测试**：
   - 使用Postman测试各个API接口
   - 验证JWT令牌在API调用中的作用

### 压力测试
- 并发用户登录测试
- 大量API请求测试
- 数据库连接池测试

## 扩展功能

### 计划功能
- [ ] 密码重置功能
- [ ] 邮箱验证
- [ ] 手机短信验证
- [ ] OAuth2.0集成
- [ ] 多因素认证
- [ ] 社交登录

### 技术升级
- [ ] Redis会话存储
- [ ] 微服务架构
- [ ] Docker容器化
- [ ] Kubernetes集群部署

## 故障排除

### 常见问题

1. **MetaMask连接失败**
   - 检查浏览器是否安装MetaMask扩展
   - 确认MetaMask已连接到正确的网络

2. **签名验证失败**
   - 确保钱包网络与后端配置一致
   - 检查签名消息格式

3. **JWT令牌无效**
   - 检查令牌是否过期
   - 验证令牌签名密钥

4. **数据库连接失败**
   - 检查MySQL服务状态
   - 验证数据库连接配置

### 调试技巧
1. 查看浏览器控制台错误信息
2. 检查后端日志文件
3. 使用Postman测试API接口
4. 查看数据库表结构和数据

## 开发指南

### 代码结构
```
src/main/java/com/decentralized/gaming/platform/
├── config/          # 配置类
├── controller/      # REST控制器
├── dto/            # 数据传输对象
├── entity/         # 实体类
├── mapper/         # 数据访问层
├── service/        # 业务逻辑层
├── util/           # 工具类
└── vo/             # 视图对象
```

### 关键文件
- `UserController.java`：用户相关API接口
- `UserService.java`：用户业务逻辑接口
- `JwtUtils.java`：JWT工具类
- `login.html`：登录页面
- `register.html`：注册页面

## 贡献指南

1. Fork项目仓库
2. 创建功能分支
3. 编写测试用例
4. 提交Pull Request
5. 等待代码审查

## 许可证

本项目采用MIT许可证，详见LICENSE文件。

---

*本文档最后更新时间：2024年*
