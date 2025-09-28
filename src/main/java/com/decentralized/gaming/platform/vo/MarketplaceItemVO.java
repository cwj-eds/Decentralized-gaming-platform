package com.decentralized.gaming.platform.vo;

import com.decentralized.gaming.platform.entity.MarketplaceItem;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 市场商品视图对象
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class MarketplaceItemVO {

    /**
     * 商品ID
     */
    private Long id;

    /**
     * 卖家ID
     */
    private Long sellerId;

    /**
     * 卖家用户名
     */
    private String sellerName;

    /**
     * 商品类型
     */
    private MarketplaceItem.ItemType itemType;

    /**
     * 商品ID
     */
    private Long itemId;

    /**
     * 商品名称
     */
    private String itemName;

    /**
     * 商品描述
     */
    private String itemDescription;

    /**
     * 商品图片URL
     */
    private String itemImageUrl;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 货币类型
     */
    private String currency;

    /**
     * 状态
     */
    private MarketplaceItem.ItemStatus status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
