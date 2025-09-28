package com.decentralized.gaming.platform.dto;

import com.decentralized.gaming.platform.entity.MarketplaceItem;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * 上架商品请求DTO
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class ListItemRequest {

    /**
     * 商品类型
     */
    @NotNull(message = "商品类型不能为空")
    private MarketplaceItem.ItemType itemType;

    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空")
    private Long itemId;

    /**
     * 价格
     */
    @NotNull(message = "价格不能为空")
    @Positive(message = "价格必须大于0")
    private BigDecimal price;

    /**
     * 货币类型
     */
    private String currency = "PLATFORM_TOKEN";
}
