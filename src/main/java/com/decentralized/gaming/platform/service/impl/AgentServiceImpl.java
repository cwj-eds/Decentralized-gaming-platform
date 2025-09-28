//package com.decentralized.gaming.platform.service.impl;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.decentralized.gaming.platform.entity.Agent;
//import com.decentralized.gaming.platform.mapper.AgentMapper;
//import com.decentralized.gaming.platform.service.AgentService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
///**
// * 智能体服务实现类
// *
// * @author DecentralizedGamingPlatform
// */
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class AgentServiceImpl extends ServiceImpl<AgentMapper, Agent> implements AgentService {
//
//    @Override
//    public Agent createAgent(String name, String description, String agentType,
//                           Long creatorId, String codeUrl, String modelUrl, BigDecimal price) {
//        Agent agent = new Agent();
//        agent.setName(name);
//        agent.setDescription(description);
//        agent.setAgentType(agentType);
//        agent.setCreatorId(creatorId);
//        agent.setCodeUrl(codeUrl);
//        agent.setModelUrl(modelUrl);
//        agent.setPrice(price);
//        agent.setStatus(Agent.AgentStatus.ACTIVE.getCode());
//        agent.setUsageCount(0);
//        agent.setCreatedAt(LocalDateTime.now());
//        agent.setUpdatedAt(LocalDateTime.now());
//
//        baseMapper.insert(agent);
//        log.info("智能体创建成功: {}", name);
//
//        return agent;
//    }
//
//    @Override
//    public List<Agent> getUserAgents(Long userId) {
//        return baseMapper.selectList(
//            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Agent>()
//                .eq("creator_id", userId)
//        );
//    }
//
//    @Override
//    public Page<Agent> getAgents(String agentType, int page, int size) {
//        Page<Agent> pageParam = new Page<>(page, size);
//        if (agentType != null && !agentType.isEmpty()) {
//            return baseMapper.selectPage(pageParam, null); // 这里应该添加条件查询
//        }
//        return baseMapper.selectPage(pageParam, null);
//    }
//
//    @Override
//    public String generateGameWithAI(String gameDescription, Long userId) {
//        // TODO: 集成AI服务生成游戏代码
//        log.info("用户 {} 请求AI生成游戏: {}", userId, gameDescription);
//
//        // 模拟AI生成游戏代码
//        String gameCode = "// AI生成的游戏代码\n" +
//                         "// 基于描述: " + gameDescription + "\n" +
//                         "function startGame() {\n" +
//                         "    console.log('游戏开始!');\n" +
//                         "}\n";
//
//        return gameCode;
//    }
//
//    @Override
//    public String useAgent(Long agentId, Long userId, String input) {
//        Agent agent = baseMapper.selectById(agentId);
//        if (agent == null) {
//            throw new RuntimeException("智能体不存在");
//        }
//
//        // 增加使用次数
//        incrementUsageCount(agentId);
//
//        // TODO: 调用实际的智能体服务
//        log.info("用户 {} 使用智能体 {} 处理输入: {}", userId, agent.getName(), input);
//
//        return "智能体处理结果: " + input;
//    }
//
//    @Override
//    public void incrementUsageCount(Long agentId) {
//        Agent agent = baseMapper.selectById(agentId);
//        if (agent != null) {
//            agent.setUsageCount(agent.getUsageCount() + 1);
//            agent.setUpdatedAt(LocalDateTime.now());
//            baseMapper.updateById(agent);
//        }
//    }
//}
