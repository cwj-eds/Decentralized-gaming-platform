package com.decentralized.gaming.platform.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 智能体使用请求DTO
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class AgentUsageRequest {

    /**
     * 输入内容
     */
    @NotBlank(message = "输入内容不能为空")
    @Size(max = 5000, message = "输入内容长度不能超过5000个字符")
    private String input;

    /**
     * 使用参数
     */
    private String parameters;

    /**
     * 上下文信息
     */
    private String context;
}
