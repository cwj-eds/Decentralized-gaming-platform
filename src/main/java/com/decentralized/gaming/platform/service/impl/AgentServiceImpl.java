package com.decentralized.gaming.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.decentralized.gaming.platform.common.PageResult;
import com.decentralized.gaming.platform.dto.CreateAgentRequest;
import com.decentralized.gaming.platform.dto.GameGenerationRequest;
import com.decentralized.gaming.platform.entity.Agent;
import com.decentralized.gaming.platform.entity.Game;
import com.decentralized.gaming.platform.entity.User;
import com.decentralized.gaming.platform.exception.BusinessException;
import com.decentralized.gaming.platform.mapper.AgentMapper;
import com.decentralized.gaming.platform.mapper.GameMapper;
import com.decentralized.gaming.platform.mapper.UserMapper;
import com.decentralized.gaming.platform.service.AgentService;
import com.decentralized.gaming.platform.vo.AgentVO;
import com.decentralized.gaming.platform.vo.AgentStatistics;
import com.decentralized.gaming.platform.vo.GameGenerationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 智能体服务实现类
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AgentServiceImpl implements AgentService {

    private final AgentMapper agentMapper;
    private final GameMapper gameMapper;
    private final UserMapper userMapper;

    @Override
    public PageResult<AgentVO> getActiveAgents(int page, int size) {
        Page<Agent> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Agent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Agent::getStatus, Agent.AgentStatus.ACTIVE)
                   .orderByDesc(Agent::getUsageCount)
                   .orderByDesc(Agent::getCreatedAt);
        
        IPage<Agent> result = agentMapper.selectPage(pageParam, queryWrapper);
        
        // 转换为AgentVO并填充创建者信息
        List<AgentVO> agentVOs = result.getRecords().stream().map(agent -> {
            AgentVO agentVO = new AgentVO();
            BeanUtils.copyProperties(agent, agentVO);
            
            // 获取创建者信息
            User creator = userMapper.selectById(agent.getCreatorId());
            if (creator != null) {
                agentVO.setCreatorName(creator.getUsername());
            }
            
            return agentVO;
        }).collect(Collectors.toList());
        
        return PageResult.of(agentVOs, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public PageResult<AgentVO> getAgentsByCreator(Long creatorId, int page, int size) {
        Page<Agent> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Agent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Agent::getCreatorId, creatorId)
                   .orderByDesc(Agent::getCreatedAt);
        
        IPage<Agent> result = agentMapper.selectPage(pageParam, queryWrapper);
        
        // 转换为AgentVO并填充创建者信息
        List<AgentVO> agentVOs = result.getRecords().stream().map(agent -> {
            AgentVO agentVO = new AgentVO();
            BeanUtils.copyProperties(agent, agentVO);
            
            // 获取创建者信息
            User creator = userMapper.selectById(agent.getCreatorId());
            if (creator != null) {
                agentVO.setCreatorName(creator.getUsername());
            }
            
            return agentVO;
        }).collect(Collectors.toList());
        
        return PageResult.of(agentVOs, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public PageResult<AgentVO> getAgentsByType(String agentType, int page, int size) {
        Page<Agent> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Agent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Agent::getStatus, Agent.AgentStatus.ACTIVE)
                   .eq(Agent::getAgentType, agentType)
                   .orderByDesc(Agent::getUsageCount)
                   .orderByDesc(Agent::getCreatedAt);
        
        IPage<Agent> result = agentMapper.selectPage(pageParam, queryWrapper);
        
        // 转换为AgentVO并填充创建者信息
        List<AgentVO> agentVOs = result.getRecords().stream().map(agent -> {
            AgentVO agentVO = new AgentVO();
            BeanUtils.copyProperties(agent, agentVO);
            
            // 获取创建者信息
            User creator = userMapper.selectById(agent.getCreatorId());
            if (creator != null) {
                agentVO.setCreatorName(creator.getUsername());
            }
            
            return agentVO;
        }).collect(Collectors.toList());
        
        return PageResult.of(agentVOs, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public List<AgentVO> getPopularAgents(int limit) {
        LambdaQueryWrapper<Agent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Agent::getStatus, Agent.AgentStatus.ACTIVE)
                   .orderByDesc(Agent::getUsageCount)
                   .orderByDesc(Agent::getRating)
                   .last("LIMIT " + limit);
        
        List<Agent> agents = agentMapper.selectList(queryWrapper);
        
        // 转换为AgentVO并填充创建者信息
        return agents.stream().map(agent -> {
            AgentVO agentVO = new AgentVO();
            BeanUtils.copyProperties(agent, agentVO);
            
            // 获取创建者信息
            User creator = userMapper.selectById(agent.getCreatorId());
            if (creator != null) {
                agentVO.setCreatorName(creator.getUsername());
            }
            
            return agentVO;
        }).collect(Collectors.toList());
    }

    @Override
    public AgentVO getAgentDetail(Long id) {
        Agent agent = agentMapper.selectById(id);
        if (agent == null) {
            throw new BusinessException("智能体不存在");
        }
        
        // 转换为AgentVO并填充创建者信息
        AgentVO agentVO = new AgentVO();
        BeanUtils.copyProperties(agent, agentVO);
        
        // 获取创建者信息
        User creator = userMapper.selectById(agent.getCreatorId());
        if (creator != null) {
            agentVO.setCreatorName(creator.getUsername());
        }
        
        return agentVO;
    }

    @Override
    @Transactional
    public AgentVO createAgent(CreateAgentRequest request, Long creatorId) {
        // 验证用户是否存在
        User user = userMapper.selectById(creatorId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 创建智能体
        Agent agent = new Agent();
        agent.setName(request.getName());
        agent.setDescription(request.getDescription());
        agent.setCreatorId(creatorId);
        agent.setAgentType(request.getAgentType());
        agent.setCodeUrl(request.getCodeUrl());
        agent.setModelUrl(request.getModelUrl());
        agent.setPrice(request.getPrice());
        agent.setUsageCount(0);
        agent.setRating(BigDecimal.ZERO);
        agent.setStatus(Agent.AgentStatus.ACTIVE);
        agent.setCreatedAt(LocalDateTime.now());
        agent.setUpdatedAt(LocalDateTime.now());

        agentMapper.insert(agent);

        // 转换为VO并返回
        AgentVO agentVO = new AgentVO();
        BeanUtils.copyProperties(agent, agentVO);
        agentVO.setCreatorName(user.getUsername());
        return agentVO;
    }

    @Override
    @Transactional
    public GameGenerationResult generateGame(GameGenerationRequest request, Long userId) {
        log.info("开始为用户 {} 生成游戏，描述: {}", userId, request.getDescription());

        // 验证用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 查找游戏制作智能体
        LambdaQueryWrapper<Agent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Agent::getAgentType, "GAME_MAKER")
                   .eq(Agent::getStatus, Agent.AgentStatus.ACTIVE)
                   .orderByDesc(Agent::getUsageCount)
                   .last("LIMIT 1");

        Agent gameMakerAgent = agentMapper.selectOne(queryWrapper);
        if (gameMakerAgent == null) {
            throw new BusinessException("游戏制作智能体不可用");
        }

        try {
            // 模拟AI游戏生成过程
            String generatedGameCode = generateGameCode(request);
            String gameTitle = generateGameTitle(request.getDescription());
            
            // 创建游戏
            Game game = new Game();
            game.setTitle(gameTitle);
            game.setDescription(request.getDescription());
            game.setCreatorId(userId);
            game.setGameCode(generatedGameCode);
            game.setGameAssetsUrl("/images/default-game.png"); // 默认游戏图片
            game.setStatus(Game.GameStatus.DRAFT);
            game.setPlayCount(0);
            game.setRating(BigDecimal.ZERO);
            game.setCreatedAt(LocalDateTime.now());
            game.setUpdatedAt(LocalDateTime.now());

            gameMapper.insert(game);

            // 增加智能体使用次数
            incrementUsageCount(gameMakerAgent.getId());

            // 构建返回结果
            GameGenerationResult result = new GameGenerationResult();
            result.setSuccess(true);
            result.setGameId(game.getId());
            result.setGameTitle(game.getTitle());
            result.setGameCode(generatedGameCode);
            result.setMessage("游戏生成成功！");

            log.info("游戏生成成功，游戏ID: {}, 标题: {}", game.getId(), game.getTitle());
            return result;

        } catch (Exception e) {
            log.error("游戏生成失败", e);
            throw new BusinessException("游戏生成失败: " + e.getMessage());
        }
    }

    @Override
    public boolean useAgent(Long agentId, Long userId) {
        // 验证智能体是否存在且可用
        Agent agent = agentMapper.selectById(agentId);
        if (agent == null || agent.getStatus() != Agent.AgentStatus.ACTIVE) {
            throw new BusinessException("智能体不可用");
        }

        // 验证用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 增加使用次数
        incrementUsageCount(agentId);
        
        log.info("用户 {} 使用了智能体 {}", userId, agentId);
        return true;
    }

    @Override
    public void incrementUsageCount(Long agentId) {
        // 使用MyBatis-Plus的UpdateWrapper来更新使用次数
        LambdaUpdateWrapper<Agent> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Agent::getId, agentId)
                    .setSql("usage_count = usage_count + 1");
        
        agentMapper.update(null, updateWrapper);
    }

    @Override
    public AgentStatistics getAgentStatistics() {
        // 查询总智能体数
        LambdaQueryWrapper<Agent> totalWrapper = new LambdaQueryWrapper<>();
        long totalAgents = agentMapper.selectCount(totalWrapper);

        // 查询活跃智能体数
        LambdaQueryWrapper<Agent> activeWrapper = new LambdaQueryWrapper<>();
        activeWrapper.eq(Agent::getStatus, Agent.AgentStatus.ACTIVE);
        long activeAgents = agentMapper.selectCount(activeWrapper);

        // 查询游戏制作智能体数
        LambdaQueryWrapper<Agent> gameMakerWrapper = new LambdaQueryWrapper<>();
        gameMakerWrapper.eq(Agent::getAgentType, "GAME_MAKER")
                       .eq(Agent::getStatus, Agent.AgentStatus.ACTIVE);
        long gameMakerAgents = agentMapper.selectCount(gameMakerWrapper);

        // 查询总使用次数
        List<Agent> allAgents = agentMapper.selectList(null);
        long totalUsageCount = allAgents.stream()
                .mapToLong(agent -> agent.getUsageCount() != null ? agent.getUsageCount() : 0)
                .sum();

        return new AgentStatistics(totalAgents, activeAgents, gameMakerAgents, totalUsageCount);
    }

    /**
     * 生成游戏代码（模拟AI生成）
     */
    private String generateGameCode(GameGenerationRequest request) {
        // 这里应该调用真实的AI服务，现在先返回模拟代码
        StringBuilder code = new StringBuilder();
        code.append("// AI生成的游戏代码\n");
        code.append("// 游戏描述: ").append(request.getDescription()).append("\n");
        code.append("// 游戏类型: ").append(request.getGameType() != null ? request.getGameType() : "休闲游戏").append("\n");
        code.append("// 游戏难度: ").append(request.getDifficulty() != null ? request.getDifficulty() : "简单").append("\n\n");
        
        code.append("class Game {\n");
        code.append("    constructor() {\n");
        code.append("        this.score = 0;\n");
        code.append("        this.level = 1;\n");
        code.append("    }\n\n");
        code.append("    start() {\n");
        code.append("        console.log('游戏开始！');\n");
        code.append("        this.gameLoop();\n");
        code.append("    }\n\n");
        code.append("    gameLoop() {\n");
        code.append("        // 游戏主循环\n");
        code.append("        this.update();\n");
        code.append("        this.render();\n");
        code.append("    }\n\n");
        code.append("    update() {\n");
        code.append("        // 更新游戏状态\n");
        code.append("    }\n\n");
        code.append("    render() {\n");
        code.append("        // 渲染游戏画面\n");
        code.append("    }\n");
        code.append("}\n\n");
        code.append("// 启动游戏\n");
        code.append("const game = new Game();\n");
        code.append("game.start();\n");
        
        return code.toString();
    }

    /**
     * 生成游戏标题
     */
    private String generateGameTitle(String description) {
        // 简单的标题生成逻辑，实际应该使用AI
        if (description.contains("射击")) {
            return "AI射击游戏";
        } else if (description.contains("跑酷")) {
            return "AI跑酷游戏";
        } else if (description.contains("解谜")) {
            return "AI解谜游戏";
        } else if (description.contains("策略")) {
            return "AI策略游戏";
        } else {
            return "AI生成游戏 - " + description.substring(0, Math.min(20, description.length()));
        }
    }

}
