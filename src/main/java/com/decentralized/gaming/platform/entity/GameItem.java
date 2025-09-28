package com.decentralized.gaming.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 游戏道具实体类
 *
 * @author DecentralizedGamingPlatform
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("game_items")
public class GameItem {

    /**
     * 道具ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 游戏ID
     */
    @TableField("game_id")
    private Long gameId;

    /**
     * 道具名称
     */
    @TableField("item_name")
    private String itemName;

    /**
     * 道具类型
     */
    @TableField("item_type")
    private String itemType;

    /**
     * 稀有度
     */
    @TableField("rarity")
    private ItemRarity rarity;

    /**
     * 道具属性(JSON格式)
     */
    @TableField("attributes")
    private String attributes;

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
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 道具稀有度枚举
     */
    public enum ItemRarity {
        COMMON("普通"),
        RARE("稀有"),
        EPIC("史诗"),
        LEGENDARY("传说");

        private final String description;

        ItemRarity(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
