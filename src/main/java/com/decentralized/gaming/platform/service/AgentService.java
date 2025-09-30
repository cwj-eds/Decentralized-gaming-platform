package com.decentralized.gaming.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.decentralized.gaming.platform.common.PageResult;
import com.decentralized.gaming.platform.dto.CreateAgentRequest;
import com.decentralized.gaming.platform.dto.GameGenerationRequest;
import com.decentralized.gaming.platform.vo.AgentVO;
import com.decentralized.gaming.platform.vo.AgentStatistics;
import com.decentralized.gaming.platform.vo.GameGenerationResult;

import java.util.List;

/**
 * 智能体服务接口
 *
 * @author DecentralizedGamingPlatform
 */
public interface AgentService {

    /**
     * 分页查询活跃的智能体
     *
     * @param page 页码
     * @param size 每页大小
     * @return 智能体分页结果
     */
    PageResult<AgentVO> getActiveAgents(int page, int size);

    /**
     * 根据创建者ID查询智能体
     *
     * @param creatorId 创建者ID
     * @param page 页码
     * @param size 每页大小
     * @return 智能体分页结果
     */
    PageResult<AgentVO> getAgentsByCreator(Long creatorId, int page, int size);

    /**
     * 根据类型查询智能体
     *
     * @param agentType 智能体类型
     * @param page 页码
     * @param size 每页大小
     * @return 智能体分页结果
     */
    PageResult<AgentVO> getAgentsByType(String agentType, int page, int size);

    /**
     * 查询热门智能体
     *
     * @param limit 限制数量
     * @return 热门智能体列表
     */
    List<AgentVO> getPopularAgents(int limit);

    /**
     * 根据ID查询智能体详情
     *
     * @param id 智能体ID
     * @return 智能体详情
     */
    AgentVO getAgentDetail(Long id);

    /**
     * 创建智能体
     *
     * @param request 创建智能体请求
     * @param creatorId 创建者ID
     * @return 创建的智能体
     */
    AgentVO createAgent(CreateAgentRequest request, Long creatorId);

    /**
     * 使用游戏制作智能体生成游戏
     *
     * @param request 游戏生成请求
     * @param userId 用户ID
     * @return 游戏生成结果
     */
    GameGenerationResult generateGame(GameGenerationRequest request, Long userId);

    /**
     * 使用智能体
     *
     * @param agentId 智能体ID
     * @param userId 用户ID
     * @return 使用结果
     */
    boolean useAgent(Long agentId, Long userId);

    /**
     * 更新智能体使用次数
     *
     * @param agentId 智能体ID
     */
    void incrementUsageCount(Long agentId);

    /**
     * 获取智能体统计信息
     *
     * @return 统计信息
     */
    AgentStatistics getAgentStatistics();
}
