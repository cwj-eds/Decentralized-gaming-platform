-- 数据库迁移脚本：为users表添加password字段
-- 版本: 1.1.0
-- 执行时间: 2024-01-01
-- 说明: 为用户表添加密码字段，支持传统密码登录

-- 检查password字段是否已存在
SET @column_exists = (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'users'
    AND COLUMN_NAME = 'password'
);

-- 如果password字段不存在，则添加该字段
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE users ADD COLUMN password VARCHAR(255) DEFAULT NULL COMMENT "密码（加密存储）" AFTER avatar_url',
    'SELECT "password字段已存在，跳过添加操作"'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 更新系统配置版本
UPDATE system_configs
SET config_value = '1.1.0'
WHERE config_key = 'platform.version';

-- 添加密码相关的系统配置
INSERT INTO system_configs (config_key, config_value, config_type, description) VALUES
('password.min_length', '6', 'INTEGER', '密码最小长度'),
('password.require_special_char', 'false', 'BOOLEAN', '是否要求特殊字符'),
('password.max_age_days', '90', 'INTEGER', '密码最大有效期（天）'),
('password.encrypt_algorithm', 'BCRYPT', 'STRING', '密码加密算法');

-- 迁移完成提示
SELECT '数据库迁移完成！用户表已添加password字段' as message;
