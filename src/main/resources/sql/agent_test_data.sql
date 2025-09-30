-- 智能体测试数据
INSERT INTO agents (name, description, creator_id, agent_type, code_url, model_url, price, usage_count, rating, status, created_at, updated_at) VALUES
('AI游戏制作师', '专业的游戏制作智能体，能够根据用户描述生成完整的游戏代码和资源。支持多种游戏类型，包括动作、解谜、策略等。', 1, 'GAME_MAKER', 'https://github.com/example/game-maker', 'https://huggingface.co/example/game-maker-model', 0.00, 156, 4.8, 'ACTIVE', NOW(), NOW()),

('智能对话助手', '基于大语言模型的对话智能体，能够回答游戏相关问题，提供攻略建议，协助用户解决技术问题。', 1, 'CHAT', 'https://github.com/example/chat-agent', 'https://huggingface.co/example/chat-model', 0.00, 89, 4.5, 'ACTIVE', NOW(), NOW()),

('数据分析师', '专业的数据分析智能体，能够分析游戏数据、用户行为，提供数据洞察和优化建议。', 2, 'ANALYSIS', 'https://github.com/example/analytics-agent', 'https://huggingface.co/example/analytics-model', 50.00, 23, 4.2, 'ACTIVE', NOW(), NOW()),

('创意设计师', 'AI创意设计智能体，能够生成游戏美术资源、UI设计、角色设计等创意内容。', 2, 'CREATIVE', 'https://github.com/example/creative-agent', 'https://huggingface.co/example/creative-model', 100.00, 45, 4.6, 'ACTIVE', NOW(), NOW()),

('教育导师', '游戏开发教育智能体，提供编程教学、游戏设计理论、最佳实践指导等教育内容。', 1, 'EDUCATION', 'https://github.com/example/education-agent', 'https://huggingface.co/example/education-model', 0.00, 67, 4.7, 'ACTIVE', NOW(), NOW()),

('工具助手', '多功能工具智能体，提供代码格式化、性能优化、测试生成等开发工具功能。', 3, 'TOOL', 'https://github.com/example/tool-agent', 'https://huggingface.co/example/tool-model', 25.00, 34, 4.3, 'ACTIVE', NOW(), NOW()),

('音乐制作师', 'AI音乐制作智能体，能够为游戏生成背景音乐、音效、主题曲等音频内容。', 3, 'CREATIVE', 'https://github.com/example/music-agent', 'https://huggingface.co/example/music-model', 75.00, 12, 4.1, 'ACTIVE', NOW(), NOW()),

('测试专家', '游戏测试智能体，能够自动生成测试用例、执行测试、发现bug并提供修复建议。', 2, 'TOOL', 'https://github.com/example/testing-agent', 'https://huggingface.co/example/testing-model', 60.00, 28, 4.4, 'ACTIVE', NOW(), NOW()),

('营销助手', '游戏营销智能体，提供市场分析、推广策略、用户获取等营销相关服务。', 1, 'ANALYSIS', 'https://github.com/example/marketing-agent', 'https://huggingface.co/example/marketing-model', 80.00, 19, 4.0, 'ACTIVE', NOW(), NOW()),

('本地化专家', '游戏本地化智能体，支持多语言翻译、文化适配、本地化测试等功能。', 3, 'TOOL', 'https://github.com/example/localization-agent', 'https://huggingface.co/example/localization-model', 40.00, 15, 4.2, 'ACTIVE', NOW(), NOW());

