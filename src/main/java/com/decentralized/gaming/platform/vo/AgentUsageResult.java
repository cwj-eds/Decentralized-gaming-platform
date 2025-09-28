package com.decentralized.gaming.platform.vo;

import lombok.Data;

/**
 * 智能体使用结果视图对象
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class AgentUsageResult {

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 输出结果
     */
    private String output;

    /**
     * 处理时间（毫秒）
     */
    private Long processingTime;

    /**
     * 错误消息
     */
    private String errorMessage;

    /**
     * 使用费用
     */
    private String usageCost;

    /**
     * 剩余余额
     */
    private String remainingBalance;
}
