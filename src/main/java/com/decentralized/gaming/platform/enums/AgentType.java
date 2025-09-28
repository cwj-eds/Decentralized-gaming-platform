package com.decentralized.gaming.platform.enums;

/**
 * 智能体类型枚举
 *
 * @author DecentralizedGamingPlatform
 */
public enum AgentType {

    /**
     * 游戏制作智能体
     */
    GAME_MAKER("GAME_MAKER", "游戏制作智能体"),

    /**
     * 游戏测试智能体
     */
    GAME_TESTER("GAME_TESTER", "游戏测试智能体"),

    /**
     * 游戏优化智能体
     */
    GAME_OPTIMIZER("GAME_OPTIMIZER", "游戏优化智能体"),

    /**
     * 内容生成智能体
     */
    CONTENT_GENERATOR("CONTENT_GENERATOR", "内容生成智能体"),

    /**
     * 数据分析智能体
     */
    DATA_ANALYZER("DATA_ANALYZER", "数据分析智能体"),

    /**
     * 客服智能体
     */
    CUSTOMER_SERVICE("CUSTOMER_SERVICE", "客服智能体");

    private final String code;
    private final String description;

    AgentType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static AgentType fromCode(String code) {
        for (AgentType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown agent type code: " + code);
    }
}
