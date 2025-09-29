-- 去中心化游戏平台测试数据
-- 创建时间: 2024-01-01
-- 版本: 1.0
-- 用途: 为开发和测试提供丰富的测试数据

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 清空所有表数据（按依赖关系顺序）
DELETE FROM transactions;
DELETE FROM game_sessions;
DELETE FROM user_assets;
DELETE FROM recharge_records;
DELETE FROM marketplace_items;
DELETE FROM game_items;
DELETE FROM agents;
DELETE FROM games;
DELETE FROM user_balances;
DELETE FROM users;

-- 重置自增ID
ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE user_balances AUTO_INCREMENT = 1;
ALTER TABLE games AUTO_INCREMENT = 1;
ALTER TABLE game_items AUTO_INCREMENT = 1;
ALTER TABLE agents AUTO_INCREMENT = 1;
ALTER TABLE marketplace_items AUTO_INCREMENT = 1;
ALTER TABLE transactions AUTO_INCREMENT = 1;
ALTER TABLE recharge_records AUTO_INCREMENT = 1;
ALTER TABLE game_sessions AUTO_INCREMENT = 1;
ALTER TABLE user_assets AUTO_INCREMENT = 1;

-- 1. 插入测试用户数据
INSERT INTO users (wallet_address, username, email, avatar_url, password) VALUES
('0x742d35Cc6634C0532925a3b8D9B2b8e0f6F8E7B1', 'Alice_Gamer', 'alice@example.com', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Alice', '$2a$10$N9qo8uLOickgx2ZMRZoMye1Xr6nE3LKXgCehGzvnvFVv8C4vK8gQy'), -- 密码: password123
('0x8ba1f109551bD4328030123f8846755186C4D7c2', 'Bob_Developer', 'bob@example.com', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Bob', '$2a$10$N9qo8uLOickgx2ZMRZoMye1Xr6nE3LKXgCehGzvnvFVv8C4vK8gQy'), -- 密码: password123
('0x1234567890123456789012345678901234567890', 'Charlie_Trader', 'charlie@example.com', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Charlie', '$2a$10$N9qo8uLOickgx2ZMRZoMye1Xr6nE3LKXgCehGzvnvFVv8C4vK8gQy'), -- 密码: password123
('0x9876543210987654321098765432109876543210', 'Diana_Creator', 'diana@example.com', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Diana', '$2a$10$N9qo8uLOickgx2ZMRZoMye1Xr6nE3LKXgCehGzvnvFVv8C4vK8gQy'), -- 密码: password123
('0x1111222233334444555566667777888899990000', 'Eve_Collector', 'eve@example.com', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Eve', '$2a$10$N9qo8uLOickgx2ZMRZoMye1Xr6nE3LKXgCehGzvnvFVv8C4vK8gQy'), -- 密码: password123
('0xaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', 'Frank_Tester', 'frank@example.com', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Frank', '$2a$10$N9qo8uLOickgx2ZMRZoMye1Xr6nE3LKXgCehGzvnvFVv8C4vK8gQy'), -- 密码: password123
('0xbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb', 'Grace_Artist', 'grace@example.com', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Grace', '$2a$10$N9qo8uLOickgx2ZMRZoMye1Xr6nE3LKXgCehGzvnvFVv8C4vK8gQy'), -- 密码: password123
('0xcccccccccccccccccccccccccccccccccccccccc', 'Henry_Gamer', 'henry@example.com', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Henry', '$2a$10$N9qo8uLOickgx2ZMRZoMye1Xr6nE3LKXgCehGzvnvFVv8C4vK8gQy'), -- 密码: password123
('0xdddddddddddddddddddddddddddddddddddddddd', 'Iris_Developer', 'iris@example.com', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Iris', '$2a$10$N9qo8uLOickgx2ZMRZoMye1Xr6nE3LKXgCehGzvnvFVv8C4vK8gQy'), -- 密码: password123
('0xeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee', 'Jack_Trader', 'jack@example.com', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Jack', '$2a$10$N9qo8uLOickgx2ZMRZoMye1Xr6nE3LKXgCehGzvnvFVv8C4vK8gQy'); -- 密码: password123

-- 2. 插入用户代币余额数据
INSERT INTO user_balances (user_id, token_type, balance) VALUES
(1, 'PLATFORM_TOKEN', 1500.50),
(1, 'ETH', 2.5),
(2, 'PLATFORM_TOKEN', 800.25),
(2, 'ETH', 1.8),
(3, 'PLATFORM_TOKEN', 2500.75),
(4, 'PLATFORM_TOKEN', 1200.00),
(4, 'ETH', 3.2),
(5, 'PLATFORM_TOKEN', 500.00),
(6, 'PLATFORM_TOKEN', 300.00),
(7, 'PLATFORM_TOKEN', 1800.00),
(8, 'PLATFORM_TOKEN', 950.00),
(9, 'PLATFORM_TOKEN', 2100.00),
(10, 'PLATFORM_TOKEN', 750.00);

-- 3. 插入游戏数据
INSERT INTO games (title, description, creator_id, game_code, game_assets_url, contract_address, status, play_count, rating) VALUES
('太空射击大战', '经典的太空射击游戏，玩家需要驾驶飞船击退外星人入侵', 2, '<html><body><h1>太空射击游戏</h1><script>console.log("Game loaded");</script></body></html>', 'ipfs://QmSpaceShooter123', '0x742d35Cc6634C0532925a3b8D9B2b8e0f6F8E7B1', 'PUBLISHED', 45, 4.2),
('魔法王国冒险', '在魔法王国中探险，收集宝物，打败怪物', 4, '<html><body><h1>魔法冒险游戏</h1><canvas id="gameCanvas"></canvas></body></html>', 'ipfs://QmMagicAdventure456', '0x8ba1f109551bD4328030123f8846755186C4D7c2', 'PUBLISHED', 32, 4.5),
('赛车竞速挑战', '高速赛车游戏，体验肾上腺素飙升的快感', 2, '<html><body><h1>赛车游戏</h1><div id="raceTrack"></div></body></html>', 'ipfs://QmRacingGame789', '0x1234567890123456789012345678901234567890', 'PUBLISHED', 28, 3.8),
('益智拼图世界', '各种有趣的拼图挑战，考验你的逻辑思维', 4, '<html><body><h1>拼图游戏</h1><div class="puzzle-container"></div></body></html>', 'ipfs://QmPuzzleWorld101', '0x9876543210987654321098765432109876543210', 'PUBLISHED', 67, 4.7),
('僵尸生存大战', '末日世界中生存，收集资源，对抗僵尸大军', 1, '<html><body><h1>生存游戏</h1><div id="survivalMap"></div></body></html>', 'ipfs://QmZombieSurvival202', '0x1111222233334444555566667777888899990000', 'PUBLISHED', 23, 4.0),
('农场经营模拟', '经营你的梦想农场，种植作物，饲养动物', 3, '<html><body><h1>农场游戏</h1><div class="farm-grid"></div></body></html>', 'ipfs://QmFarmSimulator303', '0xaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', 'PUBLISHED', 41, 4.3),
('卡牌对战游戏', '收集卡牌，组建最强牌组，与其他玩家对战', 5, '<html><body><h1>卡牌游戏</h1><div class="card-battle"></div></body></html>', 'ipfs://QmCardBattle404', '0xbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb', 'PUBLISHED', 19, 3.9),
('城市建造大师', '从一个小村庄开始，建造繁荣的现代化城市', 6, '<html><body><h1>城市建造</h1><div class="city-builder"></div></body></html>', 'ipfs://QmCityBuilder505', '0xcccccccccccccccccccccccccccccccccccccccc', 'PUBLISHED', 35, 4.4),
('潜艇深海探险', '驾驶潜艇探索神秘的海底世界，发现隐藏的宝藏', 7, '<html><body><h1>潜艇探险</h1><div class="ocean-explorer"></div></body></html>', 'ipfs://QmSubmarine606', '0xdddddddddddddddddddddddddddddddddddddddd', 'PUBLISHED', 15, 4.1),
('太空殖民模拟', '在遥远的星球上建立殖民地，管理资源，扩展领土', 8, '<html><body><h1>太空殖民</h1><div class="space-colony"></div></body></html>', 'ipfs://QmSpaceColony707', '0xeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee', 'PUBLISHED', 12, 4.6);

-- 4. 插入游戏道具数据
INSERT INTO game_items (game_id, item_name, item_type, rarity, attributes, contract_address, token_id) VALUES
(1, '激光炮', 'WEAPON', 'LEGENDARY', '{"damage": 150, "fire_rate": 2.5, "energy_consumption": 3}', '0x742d35Cc6634C0532925a3b8D9B2b8e0f6F8E7B1', 'TOKEN_001'),
(1, '护盾发生器', 'DEFENSE', 'EPIC', '{"defense": 100, "duration": 30, "cooldown": 60}', '0x742d35Cc6634C0532925a3b8D9B2b8e0f6F8E7B1', 'TOKEN_002'),
(2, '魔法杖', 'WEAPON', 'LEGENDARY', '{"magic_power": 200, "mana_cost": 15, "spell_count": 5}', '0x8ba1f109551bD4328030123f8846755186C4D7c2', 'TOKEN_003'),
(2, '隐形披风', 'ACCESSORY', 'RARE', '{"stealth": 80, "duration": 45, "detection_reduction": 0.3}', '0x8ba1f109551bD4328030123f8846755186C4D7c2', 'TOKEN_004'),
(3, '涡轮增压引擎', 'UPGRADE', 'EPIC', '{"speed_boost": 25, "acceleration": 30, "fuel_efficiency": 0.15}', '0x1234567890123456789012345678901234567890', 'TOKEN_005'),
(4, '黄金拼图块', 'PUZZLE_PIECE', 'RARE', '{"points": 500, "bonus_multiplier": 2, "time_extension": 10}', '0x9876543210987654321098765432109876543210', 'TOKEN_006'),
(5, '医疗包', 'CONSUMABLE', 'COMMON', '{"healing": 50, "use_time": 2, "stack_size": 5}', '0x1111222233334444555566667777888899990000', 'TOKEN_007'),
(6, '超级肥料', 'CONSUMABLE', 'COMMON', '{"growth_boost": 2, "harvest_time": -0.5, "yield_increase": 1.5}', '0xaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', 'TOKEN_008'),
(7, '传说卡牌包', 'CARD_PACK', 'LEGENDARY', '{"card_count": 5, "legendary_chance": 0.25, "rare_chance": 0.6}', '0xbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb', 'TOKEN_009'),
(8, '城市规划蓝图', 'BLUEPRINT', 'EPIC', '{"building_slots": 10, "efficiency_bonus": 0.2, "resource_reduction": 0.15}', '0xcccccccccccccccccccccccccccccccccccccccc', 'TOKEN_010');

-- 5. 插入智能体数据
INSERT INTO agents (name, description, creator_id, agent_type, code_url, model_url, contract_address, price, usage_count, rating, status) VALUES
('游戏制作大师', '专业的游戏生成AI，能够根据用户描述自动创建完整游戏', 2, 'GAME_MAKER', 'ipfs://QmGameMakerAI001', 'ipfs://QmGameMakerModel001', '0x742d35Cc6634C0532925a3b8D9B2b8e0f6F8E7B1', 200.00, 15, 4.8, 'ACTIVE'),
('代码优化助手', '帮助开发者优化游戏代码，提高性能和用户体验', 4, 'GAME_OPTIMIZER', 'ipfs://QmCodeOptimizerAI002', 'ipfs://QmCodeOptimizerModel002', '0x8ba1f109551bD4328030123f8846755186C4D7c2', 150.00, 8, 4.2, 'ACTIVE'),
('游戏测试专家', '自动化游戏测试AI，发现bug和平衡性问题', 2, 'GAME_TESTER', 'ipfs://QmGameTesterAI003', 'ipfs://QmGameTesterModel003', '0x1234567890123456789012345678901234567890', 120.00, 22, 4.5, 'ACTIVE'),
('内容生成器', '为游戏生成丰富的故事情节和对话内容', 4, 'CONTENT_GENERATOR', 'ipfs://QmContentGenAI004', 'ipfs://QmContentGenModel004', '0x9876543210987654321098765432109876543210', 180.00, 6, 4.0, 'ACTIVE'),
('数据分析专家', '分析游戏数据，提供玩家行为洞察和优化建议', 1, 'DATA_ANALYZER', 'ipfs://QmDataAnalyzerAI005', 'ipfs://QmDataAnalyzerModel005', '0x1111222233334444555566667777888899990000', 250.00, 4, 4.7, 'ACTIVE'),
('客服智能体', '24小时在线客服，解决玩家问题和需求', 3, 'CUSTOMER_SERVICE', 'ipfs://QmCustomerServiceAI006', 'ipfs://QmCustomerServiceModel006', '0xaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', 100.00, 35, 4.3, 'ACTIVE'),
('艺术设计助手', '帮助游戏开发者设计美观的UI和游戏美术', 5, 'ART_DESIGNER', 'ipfs://QmArtDesignerAI007', 'ipfs://QmArtDesignerModel007', '0xbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb', 160.00, 12, 4.4, 'ACTIVE'),
('音效制作专家', '专业游戏音效设计和背景音乐创作', 6, 'SOUND_DESIGNER', 'ipfs://QmSoundDesignerAI008', 'ipfs://QmSoundDesignerModel008', '0xcccccccccccccccccccccccccccccccccccccccc', 140.00, 7, 4.1, 'ACTIVE');

-- 6. 插入市场商品数据
INSERT INTO marketplace_items (seller_id, item_type, item_id, price, currency, status) VALUES
(2, 'GAME', 1, 50.00, 'PLATFORM_TOKEN', 'ACTIVE'),
(4, 'GAME', 2, 75.00, 'PLATFORM_TOKEN', 'ACTIVE'),
(2, 'GAME', 3, 60.00, 'PLATFORM_TOKEN', 'ACTIVE'),
(4, 'GAME', 4, 45.00, 'PLATFORM_TOKEN', 'ACTIVE'),
(1, 'GAME', 5, 85.00, 'PLATFORM_TOKEN', 'ACTIVE'),
(3, 'GAME', 6, 70.00, 'PLATFORM_TOKEN', 'ACTIVE'),
(5, 'GAME', 7, 55.00, 'PLATFORM_TOKEN', 'ACTIVE'),
(6, 'GAME', 8, 90.00, 'PLATFORM_TOKEN', 'ACTIVE'),
(7, 'GAME', 9, 65.00, 'PLATFORM_TOKEN', 'ACTIVE'),
(8, 'GAME', 10, 80.00, 'PLATFORM_TOKEN', 'ACTIVE'),
(2, 'AGENT', 1, 200.00, 'PLATFORM_TOKEN', 'ACTIVE'),
(4, 'AGENT', 2, 150.00, 'PLATFORM_TOKEN', 'ACTIVE'),
(2, 'AGENT', 3, 120.00, 'PLATFORM_TOKEN', 'ACTIVE'),
(4, 'AGENT', 4, 180.00, 'PLATFORM_TOKEN', 'ACTIVE'),
(1, 'AGENT', 5, 250.00, 'PLATFORM_TOKEN', 'ACTIVE'),
(3, 'AGENT', 6, 100.00, 'PLATFORM_TOKEN', 'ACTIVE'),
(5, 'AGENT', 7, 160.00, 'PLATFORM_TOKEN', 'ACTIVE'),
(6, 'AGENT', 8, 140.00, 'PLATFORM_TOKEN', 'ACTIVE'),
(1, 'GAME_ITEM', 1, 30.00, 'PLATFORM_TOKEN', 'SOLD'),
(1, 'GAME_ITEM', 2, 25.00, 'PLATFORM_TOKEN', 'ACTIVE'),
(2, 'GAME_ITEM', 3, 35.00, 'PLATFORM_TOKEN', 'ACTIVE'),
(4, 'GAME_ITEM', 4, 20.00, 'PLATFORM_TOKEN', 'ACTIVE');

-- 7. 插入交易记录数据
INSERT INTO transactions (buyer_id, seller_id, marketplace_item_id, amount, currency, tx_hash, status) VALUES
(3, 2, 1, 50.00, 'PLATFORM_TOKEN', '0xa1b2c3d4e5f6789012345890123456789012345678901234567890', 'COMPLETED'),
(5, 4, 2, 75.00, 'PLATFORM_TOKEN', '0xb2c3d4e5f678901234567823456789012345678901234567890123', 'COMPLETED'),
(1, 2, 3, 60.00, 'PLATFORM_TOKEN', '0xc3d4e5f678901234567456789012345678901234567890123456', 'COMPLETED'),
(6, 4, 4, 45.00, 'PLATFORM_TOKEN', '0xd4e5f678901234567890123456789012345678901234567890123456789', 'COMPLETED'),
(7, 1, 5, 85.00, 'PLATFORM_TOKEN', '0xe5f678901234567890189012345678901234567890123456789012', 'PENDING'),
(8, 3, 6, 70.00, 'PLATFORM_TOKEN', '0xf678901234567890123456734567890123456789012345678901234', 'COMPLETED'),
(9, 5, 7, 55.00, 'PLATFORM_TOKEN', '0x7890123456789012345678967890123456789012345678901234567', 'COMPLETED'),
(10, 6, 8, 90.00, 'PLATFORM_TOKEN', '0x9012345678901234567890123456789012345678901234567890', 'COMPLETED'),
(4, 7, 9, 65.00, 'PLATFORM_TOKEN', '0x123456789012345678901678901234567890123456789012345678901', 'COMPLETED'),
(5, 8, 10, 80.00, 'PLATFORM_TOKEN', '0x234567890123456890123456789012345678901234567890123', 'COMPLETED'),
(3, 2, 11, 200.00, 'PLATFORM_TOKEN', '0x345678901234567890129012345678901234567890123456789012345', 'COMPLETED'),
(6, 4, 12, 150.00, 'PLATFORM_TOKEN', '0x456789012345678901201234567890123456789012345678901234567', 'COMPLETED'),
(7, 2, 13, 120.00, 'PLATFORM_TOKEN', '0x56789012345678901234512345678901234567890123456789012345678', 'COMPLETED'),
(8, 4, 14, 180.00, 'PLATFORM_TOKEN', '0x6789012345678901234567891234567890123456789012345678901234567890', 'COMPLETED'),
(9, 1, 15, 250.00, 'PLATFORM_TOKEN', '0x78901234567890123456345678901234567890123456789012345678901', 'COMPLETED'),
(10, 3, 16, 100.00, 'PLATFORM_TOKEN', '0x8901234567890123453456789012345678901234567890123456789012', 'COMPLETED'),
(4, 5, 17, 160.00, 'PLATFORM_TOKEN', '0x901234567890123456745678901234567890123456789012345678901234', 'COMPLETED'),
(5, 6, 18, 140.00, 'PLATFORM_TOKEN', '0x01234567890123456767890123456789012345678901234567890123456', 'COMPLETED'),
(2, 1, 19, 30.00, 'PLATFORM_TOKEN', '0x1234567890123456789012390123567890123456789012345678', 'COMPLETED'),
(3, 1, 20, 25.00, 'PLATFORM_TOKEN', '0x2345678901234567890123450456789012345678901234567890', 'COMPLETED');

-- 8. 插入充值记录数据
INSERT INTO recharge_records (user_id, amount, currency, payment_method, payment_id, tx_hash, status) VALUES
(1, 1000.00, 'PLATFORM_TOKEN', 'CREDIT_CARD', 'pay_001', '0x1111222233334888899990000aaaabbbb', 'COMPLETED'),
(2, 500.00, 'ETH', 'CRYPTO', 'pay_002', '0x22223333444455556666777788cccc', 'COMPLETED'),
(3, 2000.00, 'PLATFORM_TOKEN', 'BANK_TRANSFER', 'pay_003', '0x333344445555000aaaabbbbccccdddd', 'COMPLETED'),
(4, 1500.00, 'PLATFORM_TOKEN', 'ALIPAY', 'pay_004', '0x444455556666777788889ccccddddeeee', 'COMPLETED'),
(5, 800.00, 'PLATFORM_TOKEN', 'WECHAT_PAY', 'pay_005', '0x5555666677778888999ccddddeeeeffff', 'COMPLETED'),
(6, 300.00, 'ETH', 'CRYPTO', 'pay_006', '0x66667777888899990000aaaabbbbccccgggg', 'COMPLETED'),
(7, 1200.00, 'PLATFORM_TOKEN', 'PAYPAL', 'pay_007', '0x7777888899990000aaaabbdeeeeffffgggghhhh', 'COMPLETED'),
(8, 1000.00, 'PLATFORM_TOKEN', 'CREDIT_CARD', 'pay_008', '0x888899990000aaaddeeeeffffgggghhhhiiii', 'COMPLETED'),
(9, 1800.00, 'PLATFORM_TOKEN', 'BANK_TRANSFER', 'pay_009', '0x99990000aaaabeeffffgggghhhhiiii', 'PENDING'),
(10, 600.00, 'PLATFORM_TOKEN', 'ALIPAY', 'pay_010', '0x0000aaaabbbbcccchhhhiiii', 'COMPLETED');

-- 9. 插入游戏会话数据
INSERT INTO game_sessions (user_id, game_id, session_token, start_time, end_time, score, rewards_earned, status) VALUES
(1, 1, 'session_001_alice_space', DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 1 HOUR), 1250, 12.50, 'COMPLETED'),
(3, 2, 'session_002_charlie_magic', DATE_SUB(NOW(), INTERVAL 3 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR), 890, 8.90, 'COMPLETED'),
(5, 4, 'session_003_eve_puzzle', DATE_SUB(NOW(), INTERVAL 1 HOUR), NOW(), 2100, 21.00, 'ACTIVE'),
(2, 3, 'session_004_bob_racing', DATE_SUB(NOW(), INTERVAL 4 HOUR), DATE_SUB(NOW(), INTERVAL 3 HOUR), 750, 7.50, 'COMPLETED'),
(4, 5, 'session_005_diana_zombie', DATE_SUB(NOW(), INTERVAL 5 HOUR), DATE_SUB(NOW(), INTERVAL 3 HOUR), 1800, 18.00, 'COMPLETED'),
(6, 6, 'session_006_frank_farm', DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 1 HOUR), 950, 9.50, 'COMPLETED'),
(7, 7, 'session_007_grace_cards', DATE_SUB(NOW(), INTERVAL 6 HOUR), DATE_SUB(NOW(), INTERVAL 4 HOUR), 1200, 12.00, 'COMPLETED'),
(8, 8, 'session_008_henry_city', DATE_SUB(NOW(), INTERVAL 3 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR), 1600, 16.00, 'COMPLETED'),
(9, 9, 'session_009_iris_submarine', DATE_SUB(NOW(), INTERVAL 1 HOUR), NULL, 800, 0.00, 'ABANDONED'),
(10, 10, 'session_010_jack_colony', DATE_SUB(NOW(), INTERVAL 4 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR), 2200, 22.00, 'COMPLETED'),
(1, 2, 'session_011_alice_magic2', DATE_SUB(NOW(), INTERVAL 30 MINUTE), NULL, 450, 0.00, 'ACTIVE'),
(3, 1, 'session_012_charlie_space', DATE_SUB(NOW(), INTERVAL 6 HOUR), DATE_SUB(NOW(), INTERVAL 5 HOUR), 1350, 13.50, 'COMPLETED'),
(5, 3, 'session_013_eve_racing', DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 1 HOUR), 1100, 11.00, 'COMPLETED'),
(2, 4, 'session_014_bob_puzzle', DATE_SUB(NOW(), INTERVAL 3 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR), 1750, 17.50, 'COMPLETED'),
(4, 6, 'session_015_diana_farm', DATE_SUB(NOW(), INTERVAL 1 HOUR), NULL, 600, 0.00, 'ACTIVE');

-- 10. 插入用户资产数据
INSERT INTO user_assets (user_id, asset_type, asset_id, contract_address, token_id, acquired_at, acquisition_type, is_tradeable) VALUES
(1, 'GAME', 5, '0x111122223333444488899990000', 'GAME_TOKEN_005', DATE_SUB(NOW(), INTERVAL 5 DAY), 'PURCHASED', true),
(2, 'GAME', 1, '0x742d35Cc6634C053296F8E7B1', 'GAME_TOKEN_001', DATE_SUB(NOW(), INTERVAL 7 DAY), 'CREATED', true),
(2, 'GAME', 3, '0x1234567890121234567890', 'GAME_TOKEN_003', DATE_SUB(NOW(), INTERVAL 6 DAY), 'CREATED', true),
(3, 'GAME', 2, '0x8ba1f109551bD4328086C4D7c2', 'GAME_TOKEN_002', DATE_SUB(NOW(), INTERVAL 4 DAY), 'PURCHASED', true),
(3, 'AGENT', 11, '0x34567823456789012345679012345', 'AGENT_TOKEN_011', DATE_SUB(NOW(), INTERVAL 3 DAY), 'PURCHASED', true),
(4, 'GAME', 2, '0x8ba1f109551bD432803016C4D7c2', 'GAME_TOKEN_002', DATE_SUB(NOW(), INTERVAL 8 DAY), 'CREATED', true),
(4, 'GAME', 4, '0x98765432109876543210987543210', 'GAME_TOKEN_004', DATE_SUB(NOW(), INTERVAL 7 DAY), 'CREATED', true),
(4, 'AGENT', 12, '0x45678901234567890126234567', 'AGENT_TOKEN_012', DATE_SUB(NOW(), INTERVAL 5 DAY), 'PURCHASED', true),
(4, 'AGENT', 14, '0x678901234890123456789012234567890', 'AGENT_TOKEN_014', DATE_SUB(NOW(), INTERVAL 4 DAY), 'PURCHASED', true),
(5, 'GAME', 7, '0xbbbbbbbbbbbbbbbbbbbbbbbbbb', 'GAME_TOKEN_007', DATE_SUB(NOW(), INTERVAL 6 DAY), 'PURCHASED', true),
(5, 'AGENT', 17, '0x9012345678901567890123455678901234', 'AGENT_TOKEN_017', DATE_SUB(NOW(), INTERVAL 2 DAY), 'PURCHASED', true),
(6, 'GAME', 8, '0xccccccccccccccccccccccccccccccc', 'GAME_TOKEN_008', DATE_SUB(NOW(), INTERVAL 5 DAY), 'PURCHASED', true),
(6, 'AGENT', 18, '0x012345678123456790123456', 'AGENT_TOKEN_018', DATE_SUB(NOW(), INTERVAL 3 DAY), 'PURCHASED', true),
(7, 'GAME', 9, '0xddddddddddddddddddddddddddddddd', 'GAME_TOKEN_009', DATE_SUB(NOW(), INTERVAL 4 DAY), 'PURCHASED', true),
(8, 'GAME', 10, '0xeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee', 'GAME_TOKEN_010', DATE_SUB(NOW(), INTERVAL 3 DAY), 'PURCHASED', true),
(9, 'GAME', 6, '0xaaaaaaaaaaaaaaaaaaaaaaaaaaaa', 'GAME_TOKEN_006', DATE_SUB(NOW(), INTERVAL 2 DAY), 'REWARDED', true),
(10, 'GAME', 1, '0x742d35Cc6634C0532925a3b8D96FE7B1', 'GAME_TOKEN_001', DATE_SUB(NOW(), INTERVAL 1 DAY), 'PURCHASED', true),
(1, 'GAME_ITEM', 19, '0x78901234567890123456789012345678', 'ITEM_TOKEN_019', DATE_SUB(NOW(), INTERVAL 6 DAY), 'PURCHASED', true),
(1, 'GAME_ITEM', 20, '0x234567890123456767890134567890', 'ITEM_TOKEN_020', DATE_SUB(NOW(), INTERVAL 5 DAY), 'PURCHASED', true);

-- 11. 插入用户资产数据（智能体部分）
INSERT INTO user_assets (user_id, asset_type, asset_id, contract_address, token_id, acquired_at, acquisition_type, is_tradeable) VALUES
(1, 'AGENT', 15, '0x789012345678901234567892345678901', 'AGENT_TOKEN_015', DATE_SUB(NOW(), INTERVAL 7 DAY), 'PURCHASED', true),
(2, 'AGENT', 11, '0x34567890123456789012345678901289012345', 'AGENT_TOKEN_011', DATE_SUB(NOW(), INTERVAL 8 DAY), 'PURCHASED', true),
(2, 'AGENT', 13, '0x5678901234567890123456789012345675678', 'AGENT_TOKEN_013', DATE_SUB(NOW(), INTERVAL 7 DAY), 'PURCHASED', true),
(3, 'AGENT', 16, '0x89012345678901234567890123789012', 'AGENT_TOKEN_016', DATE_SUB(NOW(), INTERVAL 4 DAY), 'PURCHASED', true),
(5, 'AGENT', 17, '0x90123456789012345678901234567201234', 'AGENT_TOKEN_017', DATE_SUB(NOW(), INTERVAL 3 DAY), 'PURCHASED', true),
(6, 'AGENT', 18, '0x012345678901234567890123456789013456', 'AGENT_TOKEN_018', DATE_SUB(NOW(), INTERVAL 4 DAY), 'PURCHASED', true),
(7, 'AGENT', 17, '0x90123456789012345678901234', 'AGENT_TOKEN_017', DATE_SUB(NOW(), INTERVAL 5 DAY), 'PURCHASED', true);

-- 12. 更新游戏的游玩次数和评分（基于游戏会话数据）
UPDATE games SET
  play_count = (SELECT COUNT(*) FROM game_sessions WHERE game_id = games.id),
  rating = CASE
    WHEN id = 1 THEN 4.2
    WHEN id = 2 THEN 4.5
    WHEN id = 3 THEN 3.8
    WHEN id = 4 THEN 4.7
    WHEN id = 5 THEN 4.0
    WHEN id = 6 THEN 4.3
    WHEN id = 7 THEN 3.9
    WHEN id = 8 THEN 4.4
    WHEN id = 9 THEN 4.1
    WHEN id = 10 THEN 4.6
  END
WHERE id > 0;

SET FOREIGN_KEY_CHECKS = 1;

-- 测试数据统计信息
SELECT '测试数据插入完成！' as message;
SELECT
  (SELECT COUNT(*) FROM users) as users_count,
  (SELECT COUNT(*) FROM user_balances) as balances_count,
  (SELECT COUNT(*) FROM games) as games_count,
  (SELECT COUNT(*) FROM game_items) as items_count,
  (SELECT COUNT(*) FROM agents) as agents_count,
  (SELECT COUNT(*) FROM marketplace_items) as marketplace_count,
  (SELECT COUNT(*) FROM transactions) as transactions_count,
  (SELECT COUNT(*) FROM recharge_records) as recharges_count,
  (SELECT COUNT(*) FROM game_sessions) as sessions_count,
  (SELECT COUNT(*) FROM user_assets) as assets_count;
