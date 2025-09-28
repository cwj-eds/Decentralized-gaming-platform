package com.decentralized.gaming.platform.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 创建智能体请求DTO
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class CreateAgentRequest {

    /**
     * 智能体名称
     */
    @NotBlank(message = "智能体名称不能为空")
    @Size(max = 100, message = "智能体名称长度不能超过100个字符")
    private String name;

    /**
     * 智能体描述
     */
    @Size(max = 1000, message = "智能体描述长度不能超过1000个字符")
    private String description;

    /**
     * 智能体类型
     */
    @NotBlank(message = "智能体类型不能为空")
    @Size(max = 50, message = "智能体类型长度不能超过50个字符")
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
     * 价格
     */
    @NotNull(message = "价格不能为空")
    private BigDecimal price;
}
