package com.decentralized.gaming.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户资产实体类 (统一管理各类资产所有权)
 *
 * @author DecentralizedGamingPlatform
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_assets")
public class UserAsset {

    /**
     * 资产ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 资产类型
     */
    @TableField("asset_type")
    private AssetType assetType;

    /**
     * 资产ID
     */
    @TableField("asset_id")
    private Long assetId;

    /**
     * NFT合约地址
     */
    @TableField("contract_address")
    private String contractAddress;

    /**
     * NFT Token ID
     */
    @TableField("token_id")
    private String tokenId;

    /**
     * 获得时间
     */
    @TableField("acquired_at")
    private LocalDateTime acquiredAt;

    /**
     * 获得方式
     */
    @TableField("acquisition_type")
    private AcquisitionType acquisitionType;

    /**
     * 是否可交易
     */
    @TableField("is_tradeable")
    private Boolean isTradeable;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 资产类型枚举
     */
    public enum AssetType {
        GAME("游戏"),
        AGENT("智能体"),
        GAME_ITEM("游戏道具");

        private final String description;

        AssetType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 获得方式枚举
     */
    public enum AcquisitionType {
        CREATED("创建"),
        PURCHASED("购买"),
        REWARDED("奖励"),
        TRANSFERRED("转移");

        private final String description;

        AcquisitionType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
