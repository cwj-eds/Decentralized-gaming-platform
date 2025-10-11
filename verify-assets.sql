-- ================================================================
-- 验证智能体和游戏自动添加到用户资产功能
-- ================================================================

-- 1. 查看所有用户资产（包含资产详细信息）
SELECT 
    ua.id AS 资产记录ID,
    u.username AS 用户名,
    ua.user_id AS 用户ID,
    ua.asset_type AS 资产类型,
    ua.asset_id AS 资产ID,
    CASE 
        WHEN ua.asset_type = 'AGENT' THEN a.name
        WHEN ua.asset_type = 'GAME' THEN g.title
        WHEN ua.asset_type = 'GAME_ITEM' THEN gi.item_name
        ELSE '未知'
    END AS 资产名称,
    CASE 
        WHEN ua.asset_type = 'AGENT' THEN a.description
        WHEN ua.asset_type = 'GAME' THEN g.description
        WHEN ua.asset_type = 'GAME_ITEM' THEN gi.item_type
        ELSE ''
    END AS 资产描述,
    ua.acquisition_type AS 获得方式,
    ua.is_tradeable AS 是否可交易,
    ua.contract_address AS 合约地址,
    ua.token_id AS TokenID,
    ua.created_at AS 获得时间
FROM user_assets ua
LEFT JOIN users u ON ua.user_id = u.id
LEFT JOIN agents a ON ua.asset_type = 'AGENT' AND ua.asset_id = a.id
LEFT JOIN games g ON ua.asset_type = 'GAME' AND ua.asset_id = g.id
LEFT JOIN game_items gi ON ua.asset_type = 'GAME_ITEM' AND ua.asset_id = gi.id
ORDER BY ua.created_at DESC;

-- ================================================================

-- 2. 按用户统计资产数量
SELECT 
    u.id AS 用户ID,
    u.username AS 用户名,
    COUNT(CASE WHEN ua.asset_type = 'AGENT' THEN 1 END) AS 智能体数量,
    COUNT(CASE WHEN ua.asset_type = 'GAME' THEN 1 END) AS 游戏数量,
    COUNT(CASE WHEN ua.asset_type = 'GAME_ITEM' THEN 1 END) AS 道具数量,
    COUNT(ua.id) AS 总资产数量
FROM users u
LEFT JOIN user_assets ua ON u.id = ua.user_id
GROUP BY u.id, u.username
ORDER BY 总资产数量 DESC;

-- ================================================================

-- 3. 查看最近创建的智能体及其资产记录
SELECT 
    a.id AS 智能体ID,
    a.name AS 智能体名称,
    a.creator_id AS 创建者ID,
    u.username AS 创建者用户名,
    a.agent_type AS 智能体类型,
    a.created_at AS 创建时间,
    CASE 
        WHEN ua.id IS NOT NULL THEN '✓ 已添加'
        ELSE '✗ 未添加'
    END AS 资产状态,
    ua.id AS 资产记录ID,
    ua.acquisition_type AS 获得方式
FROM agents a
LEFT JOIN users u ON a.creator_id = u.id
LEFT JOIN user_assets ua ON ua.user_id = a.creator_id 
    AND ua.asset_type = 'AGENT' 
    AND ua.asset_id = a.id
ORDER BY a.created_at DESC
LIMIT 10;

-- ================================================================

-- 4. 查看最近生成的游戏及其资产记录
SELECT 
    g.id AS 游戏ID,
    g.title AS 游戏标题,
    g.creator_id AS 创建者ID,
    u.username AS 创建者用户名,
    g.status AS 游戏状态,
    g.created_at AS 创建时间,
    CASE 
        WHEN ua.id IS NOT NULL THEN '✓ 已添加'
        ELSE '✗ 未添加'
    END AS 资产状态,
    ua.id AS 资产记录ID,
    ua.acquisition_type AS 获得方式
FROM games g
LEFT JOIN users u ON g.creator_id = u.id
LEFT JOIN user_assets ua ON ua.user_id = g.creator_id 
    AND ua.asset_type = 'GAME' 
    AND ua.asset_id = g.id
ORDER BY g.created_at DESC
LIMIT 10;

-- ================================================================

-- 5. 查找未添加到资产表的智能体（异常情况）
SELECT 
    a.id AS 智能体ID,
    a.name AS 智能体名称,
    a.creator_id AS 创建者ID,
    u.username AS 创建者用户名,
    a.created_at AS 创建时间,
    '⚠️ 未添加到资产表' AS 状态
FROM agents a
LEFT JOIN users u ON a.creator_id = u.id
LEFT JOIN user_assets ua ON ua.user_id = a.creator_id 
    AND ua.asset_type = 'AGENT' 
    AND ua.asset_id = a.id
WHERE ua.id IS NULL
ORDER BY a.created_at DESC;

-- ================================================================

-- 6. 查找未添加到资产表的游戏（异常情况）
SELECT 
    g.id AS 游戏ID,
    g.title AS 游戏标题,
    g.creator_id AS 创建者ID,
    u.username AS 创建者用户名,
    g.created_at AS 创建时间,
    '⚠️ 未添加到资产表' AS 状态
FROM games g
LEFT JOIN users u ON g.creator_id = u.id
LEFT JOIN user_assets ua ON ua.user_id = g.creator_id 
    AND ua.asset_type = 'GAME' 
    AND ua.asset_id = g.id
WHERE ua.id IS NULL
ORDER BY g.created_at DESC;

-- ================================================================

-- 7. 查看特定用户的所有资产
-- 替换 @user_id 为实际的用户ID
SET @user_id = 2;

SELECT 
    ua.id AS 资产记录ID,
    ua.asset_type AS 资产类型,
    ua.asset_id AS 资产ID,
    CASE 
        WHEN ua.asset_type = 'AGENT' THEN a.name
        WHEN ua.asset_type = 'GAME' THEN g.title
        WHEN ua.asset_type = 'GAME_ITEM' THEN gi.item_name
    END AS 资产名称,
    ua.acquisition_type AS 获得方式,
    ua.is_tradeable AS 可交易,
    ua.created_at AS 获得时间
FROM user_assets ua
LEFT JOIN agents a ON ua.asset_type = 'AGENT' AND ua.asset_id = a.id
LEFT JOIN games g ON ua.asset_type = 'GAME' AND ua.asset_id = g.id
LEFT JOIN game_items gi ON ua.asset_type = 'GAME_ITEM' AND ua.asset_id = gi.id
WHERE ua.user_id = @user_id
ORDER BY ua.created_at DESC;

-- ================================================================

-- 8. 资产获得方式统计
SELECT 
    ua.acquisition_type AS 获得方式,
    ua.asset_type AS 资产类型,
    COUNT(*) AS 数量
FROM user_assets ua
GROUP BY ua.acquisition_type, ua.asset_type
ORDER BY ua.acquisition_type, ua.asset_type;

-- ================================================================

-- 9. 检查数据完整性（所有创建的智能体都应该在资产表中）
SELECT 
    '智能体总数' AS 项目,
    COUNT(*) AS 数量
FROM agents
UNION ALL
SELECT 
    '资产表中的智能体数' AS 项目,
    COUNT(*) AS 数量
FROM user_assets
WHERE asset_type = 'AGENT'
UNION ALL
SELECT 
    '游戏总数' AS 项目,
    COUNT(*) AS 数量
FROM games
UNION ALL
SELECT 
    '资产表中的游戏数' AS 项目,
    COUNT(*) AS 数量
FROM user_assets
WHERE asset_type = 'GAME';

-- ================================================================

-- 10. 查看最近的资产活动
SELECT 
    ua.id AS 活动ID,
    u.username AS 用户名,
    ua.asset_type AS 资产类型,
    CASE 
        WHEN ua.asset_type = 'AGENT' THEN a.name
        WHEN ua.asset_type = 'GAME' THEN g.title
        WHEN ua.asset_type = 'GAME_ITEM' THEN gi.item_name
    END AS 资产名称,
    ua.acquisition_type AS 活动类型,
    ua.created_at AS 活动时间
FROM user_assets ua
LEFT JOIN users u ON ua.user_id = u.id
LEFT JOIN agents a ON ua.asset_type = 'AGENT' AND ua.asset_id = a.id
LEFT JOIN games g ON ua.asset_type = 'GAME' AND ua.asset_id = g.id
LEFT JOIN game_items gi ON ua.asset_type = 'GAME_ITEM' AND ua.asset_id = gi.id
ORDER BY ua.created_at DESC
LIMIT 20;

-- ================================================================

