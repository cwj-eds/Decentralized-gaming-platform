-- 去中心化游戏平台数据库初始化脚本
-- 使用前请先创建数据库: CREATE DATABASE gaming_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    wallet_address VARCHAR(42) UNIQUE NOT NULL COMMENT '钱包地址',
    username VARCHAR(50) COMMENT '用户名',
    email VARCHAR(100) COMMENT '邮箱',
    avatar_url VARCHAR(255) COMMENT '头像URL',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '删除标记'
);

-- 用户代币余额表
CREATE TABLE IF NOT EXISTS user_balances (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    token_type VARCHAR(20) NOT NULL COMMENT '代币类型',
    balance DECIMAL(20,8) NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '删除标记',
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 游戏表
CREATE TABLE IF NOT EXISTS games (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL COMMENT '游戏标题',
    description TEXT COMMENT '游戏描述',
    creator_id BIGINT NOT NULL COMMENT '创建者ID',
    game_code LONGTEXT COMMENT '游戏代码',
    game_assets_url VARCHAR(255) COMMENT '游戏资产URL(IPFS)',
    contract_address VARCHAR(42) COMMENT '合约地址',
    status ENUM('DRAFT', 'PENDING', 'PUBLISHED', 'REJECTED') DEFAULT 'DRAFT',
    play_count INT DEFAULT 0 COMMENT '游戏次数',
    rating DECIMAL(3,2) DEFAULT 0.0 COMMENT '评分',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '删除标记',
    FOREIGN KEY (creator_id) REFERENCES users(id)
);

-- 游戏道具表
CREATE TABLE IF NOT EXISTS game_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    game_id BIGINT NOT NULL,
    item_name VARCHAR(100) NOT NULL,
    item_type VARCHAR(50) NOT NULL,
    rarity ENUM('COMMON', 'RARE', 'EPIC', 'LEGENDARY') DEFAULT 'COMMON',
    attributes JSON COMMENT '道具属性',
    contract_address VARCHAR(42) COMMENT 'NFT合约地址',
    token_id VARCHAR(100) COMMENT 'NFT Token ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '删除标记',
    FOREIGN KEY (game_id) REFERENCES games(id)
);

-- 智能体表
CREATE TABLE IF NOT EXISTS agents (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
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
    status ENUM('ACTIVE', 'INACTIVE', 'BANNED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '删除标记',
    FOREIGN KEY (creator_id) REFERENCES users(id)
);

-- 市场商品表
CREATE TABLE IF NOT EXISTS marketplace_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    seller_id BIGINT NOT NULL COMMENT '卖家ID',
    item_type ENUM('GAME', 'AGENT', 'GAME_ITEM') NOT NULL,
    item_id BIGINT NOT NULL COMMENT '商品ID',
    price DECIMAL(20,8) NOT NULL COMMENT '价格',
    currency VARCHAR(20) DEFAULT 'PLATFORM_TOKEN' COMMENT '货币类型',
    status ENUM('ACTIVE', 'SOLD', 'CANCELLED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '删除标记',
    FOREIGN KEY (seller_id) REFERENCES users(id)
);

-- 交易记录表
CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    buyer_id BIGINT NOT NULL,
    seller_id BIGINT NOT NULL,
    marketplace_item_id BIGINT NOT NULL,
    amount DECIMAL(20,8) NOT NULL,
    currency VARCHAR(20) NOT NULL,
    tx_hash VARCHAR(66) COMMENT '区块链交易哈希',
    status ENUM('PENDING', 'COMPLETED', 'FAILED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '删除标记',
    FOREIGN KEY (buyer_id) REFERENCES users(id),
    FOREIGN KEY (seller_id) REFERENCES users(id),
    FOREIGN KEY (marketplace_item_id) REFERENCES marketplace_items(id)
);

-- 充值记录表
CREATE TABLE IF NOT EXISTS recharge_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    amount DECIMAL(20,8) NOT NULL COMMENT '充值金额',
    currency VARCHAR(20) NOT NULL COMMENT '货币类型',
    payment_method VARCHAR(50) NOT NULL COMMENT '支付方式',
    payment_id VARCHAR(100) COMMENT '第三方支付ID',
    tx_hash VARCHAR(66) COMMENT '区块链交易哈希',
    status ENUM('PENDING', 'COMPLETED', 'FAILED', 'CANCELLED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '删除标记',
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 游戏会话表
CREATE TABLE IF NOT EXISTS game_sessions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '玩家ID',
    game_id BIGINT NOT NULL COMMENT '游戏ID',
    session_token VARCHAR(100) UNIQUE NOT NULL COMMENT '会话令牌',
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
    end_time TIMESTAMP NULL COMMENT '结束时间',
    score INT DEFAULT 0 COMMENT '游戏得分',
    rewards_earned DECIMAL(20,8) DEFAULT 0 COMMENT '获得奖励',
    status ENUM('ACTIVE', 'COMPLETED', 'ABANDONED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '删除标记',
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (game_id) REFERENCES games(id)
);

-- 用户资产表 (统一管理各类资产所有权)
CREATE TABLE IF NOT EXISTS user_assets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    asset_type ENUM('GAME', 'AGENT', 'GAME_ITEM') NOT NULL COMMENT '资产类型',
    asset_id BIGINT NOT NULL COMMENT '资产ID',
    contract_address VARCHAR(42) COMMENT 'NFT合约地址',
    token_id VARCHAR(100) COMMENT 'NFT Token ID',
    acquired_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '获得时间',
    acquisition_type ENUM('CREATED', 'PURCHASED', 'REWARDED', 'TRANSFERRED') NOT NULL COMMENT '获得方式',
    is_tradeable BOOLEAN DEFAULT TRUE COMMENT '是否可交易',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '删除标记',
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_asset (user_id, asset_type),
    INDEX idx_contract_token (contract_address, token_id)
);

-- 系统配置表
CREATE TABLE IF NOT EXISTS system_configs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_key VARCHAR(100) UNIQUE NOT NULL COMMENT '配置键',
    config_value TEXT NOT NULL COMMENT '配置值',
    config_type VARCHAR(20) DEFAULT 'STRING' COMMENT '配置类型',
    description TEXT COMMENT '配置描述',
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否启用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 插入默认配置
INSERT IGNORE INTO system_configs (config_key, config_value, config_type, description) VALUES
('platform.token.symbol', 'PLT', 'STRING', '平台代币符号'),
('platform.token.contract', '', 'STRING', '平台代币合约地址'),
('game.creation.cost', '100.0', 'DECIMAL', '游戏创建成本'),
('agent.upload.cost', '50.0', 'DECIMAL', '智能体上传成本'),
('marketplace.fee.rate', '0.025', 'DECIMAL', '交易平台手续费率'),
('reward.game.play', '1.0', 'DECIMAL', '游戏游玩奖励'),
('reward.game.create', '10.0', 'DECIMAL', '游戏创建奖励');

-- 创建索引以提高查询性能
CREATE INDEX idx_users_wallet ON users(wallet_address);
CREATE INDEX idx_games_creator ON games(creator_id);
CREATE INDEX idx_games_status ON games(status);
CREATE INDEX idx_agents_creator ON agents(creator_id);
CREATE INDEX idx_agents_type ON agents(agent_type);
CREATE INDEX idx_marketplace_seller ON marketplace_items(seller_id);
CREATE INDEX idx_marketplace_type ON marketplace_items(item_type);
CREATE INDEX idx_transactions_buyer ON transactions(buyer_id);
CREATE INDEX idx_transactions_seller ON transactions(seller_id);

-- 创建视图以简化复杂查询
CREATE OR REPLACE VIEW v_user_game_stats AS
SELECT 
    u.id as user_id,
    u.username,
    u.wallet_address,
    COUNT(g.id) as total_games,
    COUNT(CASE WHEN g.status = 'PUBLISHED' THEN 1 END) as published_games,
    SUM(g.play_count) as total_plays,
    AVG(g.rating) as avg_rating
FROM users u
LEFT JOIN games g ON u.id = g.creator_id AND g.deleted = 0
WHERE u.deleted = 0
GROUP BY u.id, u.username, u.wallet_address;
