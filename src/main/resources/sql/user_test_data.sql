-- 用户测试数据
-- 创建时间: 2024-01-01
-- 版本: 1.0

-- 插入测试用户数据
INSERT INTO `users` (`id`, `wallet_address`, `username`, `email`, `avatar_url`, `password`) VALUES
(1, '0x1234567890123456789012345678901234567890', '测试用户1', 'test1@example.com', 'https://via.placeholder.com/100', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi'),
(2, '0x2345678901234567890123456789012345678901', '测试用户2', 'test2@example.com', 'https://via.placeholder.com/100', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi'),
(3, '0x3456789012345678901234567890123456789012', '测试用户3', 'test3@example.com', 'https://via.placeholder.com/100', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi');

-- 插入测试游戏数据
INSERT INTO `games` (`id`, `title`, `description`, `creator_id`, `game_code`, `status`, `play_count`, `rating`) VALUES
(1, '测试游戏1', '这是一个测试游戏', 1, 'console.log("Hello World");', 'PUBLISHED', 100, 4.5),
(2, '测试游戏2', '这是另一个测试游戏', 1, 'console.log("Hello Game");', 'PUBLISHED', 50, 4.0),
(3, '测试游戏3', '这是第三个测试游戏', 2, 'console.log("Hello Player");', 'PUBLISHED', 75, 4.2);

-- 插入测试智能体数据
INSERT INTO `agents` (`id`, `name`, `description`, `creator_id`, `agent_type`, `price`, `usage_count`, `rating`, `status`) VALUES
(1, '游戏制作智能体', '专门用于制作游戏的AI智能体', 1, 'GAME_MAKER', 100.0, 25, 4.8, 'ACTIVE'),
(2, '代码生成智能体', '用于生成代码的AI智能体', 1, 'CODE_GENERATOR', 50.0, 15, 4.5, 'ACTIVE'),
(3, '测试智能体', '用于测试的AI智能体', 2, 'TESTER', 25.0, 10, 4.0, 'ACTIVE');

-- 插入测试游戏道具数据
INSERT INTO `game_items` (`id`, `game_id`, `item_name`, `item_type`, `rarity`, `attributes`, `contract_address`, `token_id`) VALUES
(1, 1, '魔法剑', 'WEAPON', 'RARE', '{"attack": 50, "durability": 100}', '0x3456789012345678901234567890123456789012', '1'),
(2, 1, '治疗药水', 'POTION', 'COMMON', '{"heal": 30}', '0x3456789012345678901234567890123456789012', '2'),
(3, 2, '护盾', 'ARMOR', 'EPIC', '{"defense": 75, "magic_resist": 50}', '0x3456789012345678901234567890123456789012', '3'),
(4, 3, '传奇宝石', 'GEM', 'LEGENDARY', '{"power": 100, "special": "fire_damage"}', '0x3456789012345678901234567890123456789012', '4');
