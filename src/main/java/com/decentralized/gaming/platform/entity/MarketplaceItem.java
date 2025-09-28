package com.decentralized.gaming.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 市场商品表
 *
 * @author DecentralizedGamingPlatform
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("marketplace_items")
public class MarketplaceItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 卖家ID
     */
    @TableField("seller_id")
    private Long sellerId;

    /**
     * 商品类型
     */
    @TableField("item_type")
    private String itemType;

    /**
     * 商品ID
     */
    @TableField("item_id")
    private Long itemId;

    /**
     * 价格
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 货币类型
     */
    @TableField("currency")
    private String currency;

    /**
     * 状态
     */
    @TableField("status")
    private String status;

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

    // 商品类型枚举
    public enum ItemType {
        GAME("GAME", "游戏"),
        AGENT("AGENT", "智能体"),
        GAME_ITEM("GAME_ITEM", "游戏道具");

        private final String code;
        private final String desc;

        ItemType(String code, String desc) {
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

    // 商品状态枚举
    public enum ItemStatus {
        ACTIVE("ACTIVE", "在售"),
        SOLD("SOLD", "已售出"),
        CANCELLED("CANCELLED", "已下架");

        private final String code;
        private final String desc;

        ItemStatus(String code, String desc) {
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
