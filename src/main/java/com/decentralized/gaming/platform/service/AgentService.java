package com.decentralized.gaming.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.decentralized.gaming.platform.entity.Agent;

import java.math.BigDecimal;
import java.util.List;

/**
 * 智能体服务接口
 *
 * @author DecentralizedGamingPlatform
 */
public interface AgentService extends IService<Agent> {

    /**
     * 创建智能体
     * 
     * @param name 智能体名称
     * @param description 智能体描述
     * @param agentType 智能体类型
     * @param creatorId 创建者ID
     * @param codeUrl 代码URL
     * @param modelUrl 模型URL
     * @param price 价格
     * @return 智能体信息
     */
    Agent createAgent(String name, String description, String agentType, 
                     Long creatorId, String codeUrl, String modelUrl, BigDecimal price);

    /**
     * 获取用户智能体列表
     * 
     * @param userId 用户ID
     * @return 智能体列表
     */
    List<Agent> getUserAgents(Long userId);

    /**
     * 获取智能体列表
     * 
     * @param agentType 智能体类型
     * @param page 页码
     * @param size 页面大小
     * @return 智能体分页列表
     */
    Page<Agent> getAgents(String agentType, int page, int size);

    /**
     * 使用智能体生成游戏
     * 
     * @param gameDescription 游戏描述
     * @param userId 用户ID
     * @return 生成的游戏代码
     */
    String generateGameWithAI(String gameDescription, Long userId);

    /**
     * 使用智能体
     * 
     * @param agentId 智能体ID
     * @param userId 用户ID
     * @param input 输入内容
     * @return 输出结果
     */
    String useAgent(Long agentId, Long userId, String input);

    /**
     * 增加智能体使用次数
     * 
     * @param agentId 智能体ID
     */
    void incrementUsageCount(Long agentId);
}
