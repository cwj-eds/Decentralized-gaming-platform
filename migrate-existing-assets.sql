-- ================================================================
-- 迁移现有智能体和游戏到用户资产表
-- 
-- 用途: 将系统中已存在的智能体和游戏添加到 user_assets 表
-- 执行时机: 部署新功能后首次运行
-- 注意: 本脚本具有幂等性，可以安全地多次执行
-- ================================================================

-- 开始事务
START TRANSACTION;

-- ================================================================
-- 步骤1: 迁移现有智能体到资产表
-- ================================================================

SELECT '开始迁移智能体...' AS 状态;

INSERT INTO user_assets (
    user_id, 
    asset_type, 
    asset_id, 
    contract_address,
    token_id,
    acquisition_type, 
    is_tradeable, 
    created_at, 
    updated_at
)
SELECT 
    a.creator_id AS user_id,
    'AGENT' AS asset_type,
    a.id AS asset_id,
    a.contract_address,
    NULL AS token_id,
    'CREATED' AS acquisition_type,
    true AS is_tradeable,
    a.created_at,
    a.updated_at
FROM agents a
WHERE a.id NOT IN (
    -- 排除已经添加到资产表的智能体
    SELECT ua.asset_id 
    FROM user_assets ua 
    WHERE ua.asset_type = 'AGENT'
);

-- 显示迁移的智能体数量
SELECT 
    CONCAT('✓ 成功迁移 ', ROW_COUNT(), ' 个智能体到资产表') AS 结果;

-- ================================================================
-- 步骤2: 迁移现有游戏到资产表
-- ================================================================

SELECT '开始迁移游戏...' AS 状态;

INSERT INTO user_assets (
    user_id, 
    asset_type, 
    asset_id, 
    contract_address,
    token_id,
    acquisition_type, 
    is_tradeable, 
    created_at, 
    updated_at
)
SELECT 
    g.creator_id AS user_id,
    'GAME' AS asset_type,
    g.id AS asset_id,
    g.contract_address,
    NULL AS token_id,
    'CREATED' AS acquisition_type,
    true AS is_tradeable,
    g.created_at,
    g.updated_at
FROM games g
WHERE g.id NOT IN (
    -- 排除已经添加到资产表的游戏
    SELECT ua.asset_id 
    FROM user_assets ua 
    WHERE ua.asset_type = 'GAME'
);

-- 显示迁移的游戏数量
SELECT 
    CONCAT('✓ 成功迁移 ', ROW_COUNT(), ' 个游戏到资产表') AS 结果;

-- ================================================================
-- 步骤3: 验证迁移结果
-- ================================================================

SELECT '验证迁移结果...' AS 状态;

-- 智能体统计
SELECT 
    '智能体' AS 资产类型,
    COUNT(*) AS 总数,
    (SELECT COUNT(*) FROM user_assets WHERE asset_type = 'AGENT') AS 资产表数量,
    CASE 
        WHEN COUNT(*) = (SELECT COUNT(*) FROM user_assets WHERE asset_type = 'AGENT') 
        THEN '✓ 一致' 
        ELSE '✗ 不一致' 
    END AS 状态
FROM agents;

-- 游戏统计
SELECT 
    '游戏' AS 资产类型,
    COUNT(*) AS 总数,
    (SELECT COUNT(*) FROM user_assets WHERE asset_type = 'GAME') AS 资产表数量,
    CASE 
        WHEN COUNT(*) = (SELECT COUNT(*) FROM user_assets WHERE asset_type = 'GAME') 
        THEN '✓ 一致' 
        ELSE '✗ 不一致' 
    END AS 状态
FROM games;

-- ================================================================
-- 步骤4: 详细验证报告
-- ================================================================

SELECT '生成详细验证报告...' AS 状态;

-- 检查是否有智能体未迁移
SELECT 
    '未迁移的智能体' AS 检查项,
    COUNT(*) AS 数量,
    CASE 
        WHEN COUNT(*) = 0 THEN '✓ 所有智能体已迁移'
        ELSE CONCAT('⚠️  还有 ', COUNT(*), ' 个智能体未迁移')
    END AS 状态
FROM agents a
LEFT JOIN user_assets ua ON ua.user_id = a.creator_id 
    AND ua.asset_type = 'AGENT' 
    AND ua.asset_id = a.id
WHERE ua.id IS NULL;

-- 检查是否有游戏未迁移
SELECT 
    '未迁移的游戏' AS 检查项,
    COUNT(*) AS 数量,
    CASE 
        WHEN COUNT(*) = 0 THEN '✓ 所有游戏已迁移'
        ELSE CONCAT('⚠️  还有 ', COUNT(*), ' 个游戏未迁移')
    END AS 状态
FROM games g
LEFT JOIN user_assets ua ON ua.user_id = g.creator_id 
    AND ua.asset_type = 'GAME' 
    AND ua.asset_id = g.id
WHERE ua.id IS NULL;

-- 检查是否有重复记录
SELECT 
    '重复资产记录' AS 检查项,
    COUNT(*) AS 数量,
    CASE 
        WHEN COUNT(*) = 0 THEN '✓ 没有重复记录'
        ELSE CONCAT('⚠️  发现 ', COUNT(*), ' 个重复记录')
    END AS 状态
FROM (
    SELECT user_id, asset_type, asset_id, COUNT(*) as cnt
    FROM user_assets
    GROUP BY user_id, asset_type, asset_id
    HAVING cnt > 1
) duplicates;

-- ================================================================
-- 步骤5: 用户资产统计
-- ================================================================

SELECT '生成用户资产统计...' AS 状态;

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
HAVING 总资产数量 > 0
ORDER BY 总资产数量 DESC;

-- ================================================================
-- 步骤6: 提交或回滚
-- ================================================================

-- 如果所有检查都通过，提交事务
-- 如果发现问题，可以回滚: ROLLBACK;

SELECT '迁移完成！请检查上述验证结果。' AS 状态;
SELECT '如果结果正确，请执行: COMMIT;' AS 提示;
SELECT '如果发现问题，请执行: ROLLBACK;' AS 提示;

-- 取消注释下面的行以自动提交
-- COMMIT;

-- ================================================================
-- 清理重复记录脚本（如果需要）
-- ================================================================

/*
-- 如果发现重复记录，可以使用以下脚本清理
-- 保留最早的记录，删除重复项

DELETE t1 FROM user_assets t1
INNER JOIN user_assets t2 
WHERE 
    t1.id > t2.id 
    AND t1.user_id = t2.user_id 
    AND t1.asset_type = t2.asset_type 
    AND t1.asset_id = t2.asset_id;
*/

-- ================================================================
-- 创建唯一索引（防止未来出现重复）
-- ================================================================

/*
-- 如果还没有唯一索引，可以创建一个
-- 注意: 创建前确保没有重复数据

CREATE UNIQUE INDEX IF NOT EXISTS idx_user_asset_unique 
ON user_assets(user_id, asset_type, asset_id);
*/

-- ================================================================
-- 迁移脚本说明
-- ================================================================

/*
【使用说明】

1. 执行前准备:
   - 备份数据库
   - 确保应用已部署最新代码
   - 在测试环境先行测试

2. 执行步骤:
   mysql -u root -p decentralized_gaming_platform < migrate-existing-assets.sql

3. 检查结果:
   - 查看输出的验证报告
   - 确认所有状态都是 ✓
   - 确认资产数量一致

4. 提交更改:
   - 如果验证通过，执行 COMMIT;
   - 如果发现问题，执行 ROLLBACK;

5. 后续操作:
   - 运行测试脚本验证功能
   - 检查日志确认无异常
   - 监控系统运行状况

【回滚方案】

如果迁移后发现问题，可以回滚:

-- 回滚事务（如果还未提交）
ROLLBACK;

-- 或删除迁移的记录（如果已提交）
DELETE FROM user_assets 
WHERE acquisition_type = 'CREATED'
AND created_at < NOW();

【注意事项】

- 本脚本具有幂等性，可以安全地多次执行
- 不会影响新创建的智能体和游戏（它们会自动添加）
- 迁移过程在事务中进行，确保数据一致性
- 建议在低峰期执行

【联系支持】

如有问题，请联系开发团队或查看文档:
- ASSET_AUTO_ADD_IMPLEMENTATION.md
- ASSET_AUTO_ADD_GUIDE.md
- ASSET_AUTO_ADD_SUMMARY.md
*/

