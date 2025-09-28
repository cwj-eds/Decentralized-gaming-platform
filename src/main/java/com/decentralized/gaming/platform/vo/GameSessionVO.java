package com.decentralized.gaming.platform.vo;

import com.decentralized.gaming.platform.entity.GameSession;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 游戏会话视图对象
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class GameSessionVO {

    /**
     * 会话ID
     */
    private Long id;

    /**
     * 玩家ID
     */
    private Long userId;

    /**
     * 玩家用户名
     */
    private String playerName;

    /**
     * 游戏ID
     */
    private Long gameId;

    /**
     * 游戏标题
     */
    private String gameTitle;

    /**
     * 会话令牌
     */
    private String sessionToken;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 游戏得分
     */
    private Integer score;

    /**
     * 获得奖励
     */
    private BigDecimal rewardsEarned;

    /**
     * 会话状态
     */
    private GameSession.SessionStatus status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
