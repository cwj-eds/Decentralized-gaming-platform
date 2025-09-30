package com.decentralized.gaming.platform.vo;

import lombok.Data;

/**
 * 智能体统计信息视图对象
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class AgentStatistics {
    
    /**
     * 总智能体数
     */
    private long totalAgents;
    
    /**
     * 活跃智能体数
     */
    private long activeAgents;
    
    /**
     * 游戏制作智能体数
     */
    private long gameMakerAgents;
    
    /**
     * 总使用次数
     */
    private long totalUsageCount;
    
    public AgentStatistics() {}
    
    public AgentStatistics(long totalAgents, long activeAgents, long gameMakerAgents, long totalUsageCount) {
        this.totalAgents = totalAgents;
        this.activeAgents = activeAgents;
        this.gameMakerAgents = gameMakerAgents;
        this.totalUsageCount = totalUsageCount;
    }
}

