-- 去中心化游戏平台数据库表结构
-- 创建时间: 2024-01-01
-- 版本: 1.0

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 用户表
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `wallet_address` VARCHAR(42) NOT NULL COMMENT '钱包地址',
    `username` VARCHAR(50) DEFAULT NULL COMMENT '用户名',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `avatar_url` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_wallet_address` (`wallet_address`),
    KEY `idx_username` (`username`),
    KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 用户代币余额表
DROP TABLE IF EXISTS `user_balances`;
CREATE TABLE `user_balances` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '余额ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `token_type` VARCHAR(20) NOT NULL COMMENT '代币类型',
    `balance` DECIMAL(20,8) NOT NULL DEFAULT 0 COMMENT '余额',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_token` (`user_id`, `token_type`),
    KEY `idx_user_id` (`user_id`),
    CONSTRAINT `fk_user_balances_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户代币余额表';

-- 游戏表
DROP TABLE IF EXISTS `games`;
CREATE TABLE `games` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '游戏ID',
    `title` VARCHAR(100) NOT NULL COMMENT '游戏标题',
    `description` TEXT COMMENT '游戏描述',
    `creator_id` BIGINT NOT NULL COMMENT '创建者ID',
    `game_code` LONGTEXT COMMENT '游戏代码',
    `game_assets_url` VARCHAR(255) COMMENT '游戏资产URL(IPFS)',
    `contract_address` VARCHAR(42) COMMENT '合约地址',
    `status` ENUM('DRAFT', 'PENDING', 'PUBLISHED', 'REJECTED') DEFAULT 'DRAFT' COMMENT '游戏状态',
    `play_count` INT DEFAULT 0 COMMENT '游戏次数',
    `rating` DECIMAL(3,2) DEFAULT 0.0 COMMENT '评分',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_creator_id` (`creator_id`),
    KEY `idx_status` (`status`),
    KEY `idx_play_count` (`play_count`),
    KEY `idx_rating` (`rating`),
    CONSTRAINT `fk_games_creator_id` FOREIGN KEY (`creator_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏表';

-- 游戏道具表
DROP TABLE IF EXISTS `game_items`;
CREATE TABLE `game_items` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '道具ID',
    `game_id` BIGINT NOT NULL COMMENT '游戏ID',
    `item_name` VARCHAR(100) NOT NULL COMMENT '道具名称',
    `item_type` VARCHAR(50) NOT NULL COMMENT '道具类型',
    `rarity` ENUM('COMMON', 'RARE', 'EPIC', 'LEGENDARY') DEFAULT 'COMMON' COMMENT '稀有度',
    `attributes` JSON COMMENT '道具属性',
    `contract_address` VARCHAR(42) COMMENT 'NFT合约地址',
    `token_id` VARCHAR(100) COMMENT 'NFT Token ID',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_game_id` (`game_id`),
    KEY `idx_item_type` (`item_type`),
    KEY `idx_rarity` (`rarity`),
    KEY `idx_contract_token` (`contract_address`, `token_id`),
    CONSTRAINT `fk_game_items_game_id` FOREIGN KEY (`game_id`) REFERENCES `games` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏道具表';

-- 智能体表
DROP TABLE IF EXISTS `agents`;
CREATE TABLE `agents` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '智能体ID',
    `name` VARCHAR(100) NOT NULL COMMENT '智能体名称',
    `description` TEXT COMMENT '智能体描述',
    `creator_id` BIGINT NOT NULL COMMENT '创建者ID',
    `agent_type` VARCHAR(50) NOT NULL COMMENT '智能体类型',
    `code_url` VARCHAR(255) COMMENT '代码存储URL(IPFS)',
    `model_url` VARCHAR(255) COMMENT '模型文件URL(IPFS)',
    `contract_address` VARCHAR(42) COMMENT '合约地址',
    `price` DECIMAL(20,8) DEFAULT 0 COMMENT '价格',
    `usage_count` INT DEFAULT 0 COMMENT '使用次数',
    `rating` DECIMAL(3,2) DEFAULT 0.0 COMMENT '评分',
    `status` ENUM('ACTIVE', 'INACTIVE', 'BANNED') DEFAULT 'ACTIVE' COMMENT '状态',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_creator_id` (`creator_id`),
    KEY `idx_agent_type` (`agent_type`),
    KEY `idx_status` (`status`),
    KEY `idx_usage_count` (`usage_count`),
    KEY `idx_rating` (`rating`),
    CONSTRAINT `fk_agents_creator_id` FOREIGN KEY (`creator_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='智能体表';

-- 市场商品表
DROP TABLE IF EXISTS `marketplace_items`;
CREATE TABLE `marketplace_items` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品ID',
    `seller_id` BIGINT NOT NULL COMMENT '卖家ID',
    `item_type` ENUM('GAME', 'AGENT', 'GAME_ITEM') NOT NULL COMMENT '商品类型',
    `item_id` BIGINT NOT NULL COMMENT '商品ID',
    `price` DECIMAL(20,8) NOT NULL COMMENT '价格',
    `currency` VARCHAR(20) DEFAULT 'PLATFORM_TOKEN' COMMENT '货币类型',
    `status` ENUM('ACTIVE', 'SOLD', 'CANCELLED') DEFAULT 'ACTIVE' COMMENT '状态',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_seller_id` (`seller_id`),
    KEY `idx_item_type` (`item_type`),
    KEY `idx_item_id` (`item_id`),
    KEY `idx_status` (`status`),
    KEY `idx_price` (`price`),
    CONSTRAINT `fk_marketplace_items_seller_id` FOREIGN KEY (`seller_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='市场商品表';

-- 交易记录表
DROP TABLE IF EXISTS `transactions`;
CREATE TABLE `transactions` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '交易ID',
    `buyer_id` BIGINT NOT NULL COMMENT '买家ID',
    `seller_id` BIGINT NOT NULL COMMENT '卖家ID',
    `marketplace_item_id` BIGINT NOT NULL COMMENT '市场商品ID',
    `amount` DECIMAL(20,8) NOT NULL COMMENT '交易金额',
    `currency` VARCHAR(20) NOT NULL COMMENT '货币类型',
    `tx_hash` VARCHAR(66) COMMENT '区块链交易哈希',
    `status` ENUM('PENDING', 'COMPLETED', 'FAILED') DEFAULT 'PENDING' COMMENT '交易状态',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_buyer_id` (`buyer_id`),
    KEY `idx_seller_id` (`seller_id`),
    KEY `idx_marketplace_item_id` (`marketplace_item_id`),
    KEY `idx_status` (`status`),
    KEY `idx_tx_hash` (`tx_hash`),
    CONSTRAINT `fk_transactions_buyer_id` FOREIGN KEY (`buyer_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_transactions_seller_id` FOREIGN KEY (`seller_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_transactions_marketplace_item_id` FOREIGN KEY (`marketplace_item_id`) REFERENCES `marketplace_items` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交易记录表';

-- 充值记录表
DROP TABLE IF EXISTS `recharge_records`;
CREATE TABLE `recharge_records` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '充值记录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `amount` DECIMAL(20,8) NOT NULL COMMENT '充值金额',
    `currency` VARCHAR(20) NOT NULL COMMENT '货币类型',
    `payment_method` VARCHAR(50) NOT NULL COMMENT '支付方式',
    `payment_id` VARCHAR(100) COMMENT '第三方支付ID',
    `tx_hash` VARCHAR(66) COMMENT '区块链交易哈希',
    `status` ENUM('PENDING', 'COMPLETED', 'FAILED', 'CANCELLED') DEFAULT 'PENDING' COMMENT '充值状态',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_payment_id` (`payment_id`),
    KEY `idx_tx_hash` (`tx_hash`),
    CONSTRAINT `fk_recharge_records_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充值记录表';

-- 游戏会话表
DROP TABLE IF EXISTS `game_sessions`;
CREATE TABLE `game_sessions` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '会话ID',
    `user_id` BIGINT NOT NULL COMMENT '玩家ID',
    `game_id` BIGINT NOT NULL COMMENT '游戏ID',
    `session_token` VARCHAR(100) NOT NULL COMMENT '会话令牌',
    `start_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
    `end_time` TIMESTAMP NULL COMMENT '结束时间',
    `score` INT DEFAULT 0 COMMENT '游戏得分',
    `rewards_earned` DECIMAL(20,8) DEFAULT 0 COMMENT '获得奖励',
    `status` ENUM('ACTIVE', 'COMPLETED', 'ABANDONED') DEFAULT 'ACTIVE' COMMENT '会话状态',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_session_token` (`session_token`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_game_id` (`game_id`),
    KEY `idx_status` (`status`),
    CONSTRAINT `fk_game_sessions_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_game_sessions_game_id` FOREIGN KEY (`game_id`) REFERENCES `games` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏会话表';

-- 用户资产表 (统一管理各类资产所有权)
DROP TABLE IF EXISTS `user_assets`;
CREATE TABLE `user_assets` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '资产ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `asset_type` ENUM('GAME', 'AGENT', 'GAME_ITEM') NOT NULL COMMENT '资产类型',
    `asset_id` BIGINT NOT NULL COMMENT '资产ID',
    `contract_address` VARCHAR(42) COMMENT 'NFT合约地址',
    `token_id` VARCHAR(100) COMMENT 'NFT Token ID',
    `acquired_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '获得时间',
    `acquisition_type` ENUM('CREATED', 'PURCHASED', 'REWARDED', 'TRANSFERRED') NOT NULL COMMENT '获得方式',
    `is_tradeable` BOOLEAN DEFAULT TRUE COMMENT '是否可交易',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_asset` (`user_id`, `asset_type`),
    KEY `idx_contract_token` (`contract_address`, `token_id`),
    KEY `idx_asset_type` (`asset_type`),
    KEY `idx_asset_id` (`asset_id`),
    CONSTRAINT `fk_user_assets_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户资产表';

-- 系统配置表
DROP TABLE IF EXISTS `system_configs`;
CREATE TABLE `system_configs` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
    `config_value` TEXT NOT NULL COMMENT '配置值',
    `config_type` VARCHAR(20) DEFAULT 'STRING' COMMENT '配置类型',
    `description` TEXT COMMENT '配置描述',
    `is_active` BOOLEAN DEFAULT TRUE COMMENT '是否启用',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`),
    KEY `idx_is_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 插入默认配置
INSERT INTO `system_configs` (`config_key`, `config_value`, `config_type`, `description`) VALUES
('platform.token.symbol', 'PLT', 'STRING', '平台代币符号'),
('platform.token.contract', '', 'STRING', '平台代币合约地址'),
('game.creation.cost', '100.0', 'DECIMAL', '游戏创建成本'),
('agent.upload.cost', '50.0', 'DECIMAL', '智能体上传成本'),
('marketplace.fee.rate', '0.025', 'DECIMAL', '交易平台手续费率'),
('reward.game.play', '1.0', 'DECIMAL', '游戏游玩奖励'),
('reward.game.create', '10.0', 'DECIMAL', '游戏创建奖励'),
('platform.name', '去中心化游戏平台', 'STRING', '平台名称'),
('platform.version', '1.0.0', 'STRING', '平台版本'),
('blockchain.network', 'testnet', 'STRING', '区块链网络');

SET FOREIGN_KEY_CHECKS = 1;
