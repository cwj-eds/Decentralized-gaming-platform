package com.decentralized.gaming.platform.vo;

import com.decentralized.gaming.platform.entity.Game;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 游戏视图对象
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class GameVO {

    /**
     * 游戏ID
     */
    private Long id;

    /**
     * 游戏标题
     */
    private String title;

    /**
     * 游戏描述
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
     * 游戏资产URL
     */
    private String gameAssetsUrl;

    /**
     * 合约地址
     */
    private String contractAddress;

    /**
     * 游戏状态
     */
    private Game.GameStatus status;

    /**
     * 游戏次数
     */
    private Integer playCount;

    /**
     * 评分
     */
    private BigDecimal rating;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
