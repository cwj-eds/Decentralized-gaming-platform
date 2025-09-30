package com.decentralized.gaming.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.decentralized.gaming.platform.entity.Agent;
import com.decentralized.gaming.platform.vo.AgentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 智能体数据访问层
 *
 * @author DecentralizedGamingPlatform
 */
@Mapper
public interface AgentMapper extends BaseMapper<Agent> {

    /**
     * 分页查询活跃的智能体
     *
     * @param page 分页参数
     * @return 智能体分页结果
     */
    @Select("SELECT a.*, u.username as creator_name " +
            "FROM agents a " +
            "LEFT JOIN users u ON a.creator_id = u.id " +
            "WHERE a.status = 'ACTIVE' " +
            "ORDER BY a.usage_count DESC, a.created_at DESC")
    IPage<AgentVO> selectActiveAgentsWithCreator(Page<AgentVO> page);

    /**
     * 根据创建者ID查询智能体
     *
     * @param page 分页参数
     * @param creatorId 创建者ID
     * @return 智能体分页结果
     */
    @Select("SELECT a.*, u.username as creator_name " +
            "FROM agents a " +
            "LEFT JOIN users u ON a.creator_id = u.id " +
            "WHERE a.creator_id = #{creatorId} " +
            "ORDER BY a.created_at DESC")
    IPage<AgentVO> selectAgentsByCreator(Page<AgentVO> page, @Param("creatorId") Long creatorId);

    /**
     * 根据类型查询智能体
     *
     * @param page 分页参数
     * @param agentType 智能体类型
     * @return 智能体分页结果
     */
    @Select("SELECT a.*, u.username as creator_name " +
            "FROM agents a " +
            "LEFT JOIN users u ON a.creator_id = u.id " +
            "WHERE a.status = 'ACTIVE' AND a.agent_type = #{agentType} " +
            "ORDER BY a.usage_count DESC, a.created_at DESC")
    IPage<AgentVO> selectAgentsByType(Page<AgentVO> page, @Param("agentType") String agentType);

    /**
     * 查询热门智能体
     *
     * @param limit 限制数量
     * @return 热门智能体列表
     */
    @Select("SELECT a.*, u.username as creator_name " +
            "FROM agents a " +
            "LEFT JOIN users u ON a.creator_id = u.id " +
            "WHERE a.status = 'ACTIVE' " +
            "ORDER BY a.usage_count DESC, a.rating DESC " +
            "LIMIT #{limit}")
    List<AgentVO> selectPopularAgents(@Param("limit") int limit);

    /**
     * 根据ID查询智能体详情
     *
     * @param id 智能体ID
     * @return 智能体详情
     */
    @Select("SELECT a.*, u.username as creator_name " +
            "FROM agents a " +
            "LEFT JOIN users u ON a.creator_id = u.id " +
            "WHERE a.id = #{id}")
    AgentVO selectAgentDetailById(@Param("id") Long id);

    /**
     * 增加智能体使用次数
     *
     * @param id 智能体ID
     * @return 影响行数
     */
    @Select("UPDATE agents SET usage_count = usage_count + 1 WHERE id = #{id}")
    int incrementUsageCount(@Param("id") Long id);
}
