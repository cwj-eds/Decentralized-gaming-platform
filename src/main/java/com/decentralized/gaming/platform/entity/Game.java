package com.decentralized.gaming.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 游戏表
 *
 * @author DecentralizedGamingPlatform
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("games")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 游戏标题
     */
    @TableField("title")
    private String title;

    /**
     * 游戏描述
     */
    @TableField("description")
    private String description;

    /**
     * 创建者ID
     */
    @TableField("creator_id")
    private Long creatorId;

    /**
     * 游戏代码
     */
    @TableField("game_code")
    private String gameCode;

    /**
     * 游戏资产URL(IPFS)
     */
    @TableField("game_assets_url")
    private String gameAssetsUrl;

    /**
     * 合约地址
     */
    @TableField("contract_address")
    private String contractAddress;

    /**
     * 状态
     */
    @TableField("status")
    private String status;

    /**
     * 游戏次数
     */
    @TableField("play_count")
    private Integer playCount;

    /**
     * 评分
     */
    @TableField("rating")
    private BigDecimal rating;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;

    /**
     * 删除标记
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;

    // 游戏状态枚举
    public enum GameStatus {
        DRAFT("DRAFT", "草稿"),
        PENDING("PENDING", "待审核"),
        PUBLISHED("PUBLISHED", "已发布"),
        REJECTED("REJECTED", "已拒绝");

        private final String code;
        private final String desc;

        GameStatus(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
}
