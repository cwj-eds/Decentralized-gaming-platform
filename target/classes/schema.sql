-- H2数据库初始化脚本

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    wallet_address VARCHAR(42) UNIQUE COMMENT '钱包地址',
    username VARCHAR(50) COMMENT '用户名',
    email VARCHAR(100) COMMENT '邮箱',
    avatar_url VARCHAR(255) COMMENT '头像URL',
    password VARCHAR(255) COMMENT '密码（加密存储）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '删除标记'
);

-- 用户代币余额表
CREATE TABLE IF NOT EXISTS user_balances (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token_type VARCHAR(20) NOT NULL COMMENT '代币类型',
    balance DECIMAL(20,8) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '删除标记'
);

-- 游戏表
CREATE TABLE IF NOT EXISTS games (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL COMMENT '游戏标题',
    description TEXT COMMENT '游戏描述',
    creator_id BIGINT NOT NULL COMMENT '创建者ID',
    game_code LONGTEXT COMMENT '游戏代码',
    game_assets_url VARCHAR(255) COMMENT '游戏资产URL(IPFS)',
    contract_address VARCHAR(42) COMMENT '合约地址',
    status VARCHAR(20) DEFAULT 'DRAFT',
    play_count INT DEFAULT 0 COMMENT '游戏次数',
    rating DECIMAL(3,2) DEFAULT 0.0 COMMENT '评分',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '删除标记'
);

-- 智能体表
CREATE TABLE IF NOT EXISTS agents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '智能体名称',
    description TEXT COMMENT '智能体描述',
    creator_id BIGINT NOT NULL COMMENT '创建者ID',
    agent_type VARCHAR(50) NOT NULL COMMENT '智能体类型',
    code_url VARCHAR(255) COMMENT '代码存储URL(IPFS)',
    model_url VARCHAR(255) COMMENT '模型文件URL(IPFS)',
    contract_address VARCHAR(42) COMMENT '合约地址',
    price DECIMAL(20,8) DEFAULT 0 COMMENT '价格',
    usage_count INT DEFAULT 0 COMMENT '使用次数',
    rating DECIMAL(3,2) DEFAULT 0.0 COMMENT '评分',
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '删除标记'
);

-- 市场商品表
CREATE TABLE IF NOT EXISTS marketplace_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id BIGINT NOT NULL COMMENT '卖家ID',
    item_type VARCHAR(20) NOT NULL,
    item_id BIGINT NOT NULL COMMENT '商品ID',
    price DECIMAL(20,8) NOT NULL COMMENT '价格',
    currency VARCHAR(20) DEFAULT 'PLATFORM_TOKEN' COMMENT '货币类型',
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '删除标记'
);

-- 插入示例数据
INSERT INTO users (wallet_address, username, email) VALUES 
('0x1234567890123456789012345678901234567890', 'Alice', 'alice@example.com'),
('0x0987654321098765432109876543210987654321', 'Bob', 'bob@example.com');

INSERT INTO games (title, description, creator_id, status, play_count, rating) VALUES 
('太空大战', '经典的太空射击游戏', 1, 'PUBLISHED', 150, 4.5),
('贪吃蛇', '经典的贪吃蛇游戏', 2, 'PUBLISHED', 89, 4.2),
('俄罗斯方块', '经典的俄罗斯方块游戏', 1, 'PUBLISHED', 234, 4.8);

INSERT INTO agents (name, description, creator_id, agent_type, usage_count, rating) VALUES 
('游戏制作助手', 'AI驱动的游戏代码生成器', 1, 'GAME_MAKER', 25, 4.7),
('智能NPC', '游戏中的智能非玩家角色', 2, 'NPC_AI', 18, 4.3);
