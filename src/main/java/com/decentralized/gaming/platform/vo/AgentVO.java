package com.decentralized.gaming.platform.vo;

import com.decentralized.gaming.platform.entity.Agent;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 智能体视图对象
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class AgentVO {

    /**
     * 智能体ID
     */
    private Long id;

    /**
     * 智能体名称
     */
    private String name;

    /**
     * 智能体描述
     */
    private String description;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 创建者用户名
     */
    private String creatorName;

    /**
     * 智能体类型
     */
    private String agentType;

    /**
     * 代码URL
     */
    private String codeUrl;

    /**
     * 模型URL
     */
    private String modelUrl;

    /**
     * 合约地址
     */
    private String contractAddress;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 使用次数
     */
    private Integer usageCount;

    /**
     * 评分
     */
    private BigDecimal rating;

    /**
     * 状态
     */
    private Agent.AgentStatus status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
