package com.decentralized.gaming.platform.vo;

import lombok.Data;

/**
 * 游戏生成结果视图对象
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class GameGenerationResult {

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 游戏ID
     */
    private Long gameId;

    /**
     * 游戏标题
     */
    private String gameTitle;

    /**
     * 游戏代码
     */
    private String gameCode;

    /**
     * 游戏资产URL
     */
    private String gameAssetsUrl;

    /**
     * 生成时间（秒）
     */
    private Long generationTime;

    /**
     * 错误消息
     */
    private String errorMessage;

    /**
     * 生成日志
     */
    private String generationLog;
}
