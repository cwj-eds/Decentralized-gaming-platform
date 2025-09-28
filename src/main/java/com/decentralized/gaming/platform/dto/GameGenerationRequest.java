package com.decentralized.gaming.platform.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 游戏生成请求DTO
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class GameGenerationRequest {

    /**
     * 游戏描述
     */
    @NotBlank(message = "游戏描述不能为空")
    @Size(max = 2000, message = "游戏描述长度不能超过2000个字符")
    private String description;

    /**
     * 游戏类型
     */
    private String gameType;

    /**
     * 游戏难度
     */
    private String difficulty;

    /**
     * 游戏主题
     */
    private String theme;

    /**
     * 特殊要求
     */
    @Size(max = 1000, message = "特殊要求长度不能超过1000个字符")
    private String specialRequirements;
}
