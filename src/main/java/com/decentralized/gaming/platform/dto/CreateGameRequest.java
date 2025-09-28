package com.decentralized.gaming.platform.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 创建游戏请求DTO
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class CreateGameRequest {

    /**
     * 游戏标题
     */
    @NotBlank(message = "游戏标题不能为空")
    @Size(max = 100, message = "游戏标题长度不能超过100个字符")
    private String title;

    /**
     * 游戏描述
     */
    @Size(max = 1000, message = "游戏描述长度不能超过1000个字符")
    private String description;

    /**
     * 游戏代码
     */
    private String gameCode;

    /**
     * 游戏资产URL
     */
    private String gameAssetsUrl;
}
