package com.decentralized.gaming.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 智能体实体类
 *
 * @author DecentralizedGamingPlatform
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("agents")
public class Agent {

    /**
     * 智能体ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 智能体名称
     */
    @TableField("name")
    private String name;

    /**
     * 智能体描述
     */
    @TableField("description")
    private String description;

    /**
     * 创建者ID
     */
    @TableField("creator_id")
    private Long creatorId;

    /**
     * 智能体类型
     */
    @TableField("agent_type")
    private String agentType;

    /**
     * 代码存储URL(IPFS)
     */
    @TableField("code_url")
    private String codeUrl;

    /**
     * 模型文件URL(IPFS)
     */
    @TableField("model_url")
    private String modelUrl;

    /**
     * 合约地址
     */
    @TableField("contract_address")
    private String contractAddress;

    /**
     * 价格
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 使用次数
     */
    @TableField("usage_count")
    private Integer usageCount;

    /**
     * 评分
     */
    @TableField("rating")
    private BigDecimal rating;

    /**
     * 状态
     */
    @TableField("status")
    private AgentStatus status;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 智能体状态枚举
     */
    public enum AgentStatus {
        ACTIVE("活跃"),
        INACTIVE("非活跃"),
        BANNED("已禁用");

        private final String description;

        AgentStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
