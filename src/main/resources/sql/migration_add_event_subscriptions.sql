-- 创建事件订阅表
CREATE TABLE IF NOT EXISTS event_subscriptions (
    subscription_id VARCHAR(64) PRIMARY KEY COMMENT '订阅ID',
    contract_type VARCHAR(50) NOT NULL COMMENT '合约类型',
    contract_address VARCHAR(42) NOT NULL COMMENT '合约地址',
    event_type VARCHAR(50) NOT NULL COMMENT '事件类型',
    event_signature VARCHAR(66) NOT NULL COMMENT '事件签名',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '订阅状态：ACTIVE, PAUSED, CANCELLED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    last_event_time TIMESTAMP NULL COMMENT '最后事件时间',
    event_count BIGINT DEFAULT 0 COMMENT '事件计数',
    INDEX idx_contract_type (contract_type),
    INDEX idx_contract_address (contract_address),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='事件订阅表';
