-- 创建批量操作表
CREATE TABLE IF NOT EXISTS batch_operations (
    batch_id VARCHAR(64) PRIMARY KEY COMMENT '批量操作ID',
    operation_type VARCHAR(50) NOT NULL COMMENT '操作类型：NFT_TRANSFER, NFT_APPROVE, TOKEN_TRANSFER等',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '操作状态：PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED',
    total_operations INT NOT NULL COMMENT '总操作数',
    completed_operations INT DEFAULT 0 COMMENT '已完成操作数',
    failed_operations INT DEFAULT 0 COMMENT '失败操作数',
    progress INT DEFAULT 0 COMMENT '进度百分比 (0-100)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    start_time TIMESTAMP NULL COMMENT '开始时间',
    end_time TIMESTAMP NULL COMMENT '结束时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    error_message TEXT NULL COMMENT '错误信息',
    operation_details TEXT NULL COMMENT '操作详情（JSON格式）',
    created_by BIGINT NULL COMMENT '创建者用户ID',
    INDEX idx_operation_type (operation_type),
    INDEX idx_status (status),
    INDEX idx_created_by (created_by),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='批量操作表';
