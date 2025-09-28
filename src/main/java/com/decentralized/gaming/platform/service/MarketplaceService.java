package com.decentralized.gaming.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.decentralized.gaming.platform.entity.MarketplaceItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * 交易市场服务接口
 *
 * @author DecentralizedGamingPlatform
 */
public interface MarketplaceService extends IService<MarketplaceItem> {

    /**
     * 上架商品
     * 
     * @param sellerId 卖家ID
     * @param itemType 商品类型
     * @param itemId 商品ID
     * @param price 价格
     * @param currency 货币类型
     * @return 市场商品信息
     */
    MarketplaceItem listItem(Long sellerId, String itemType, Long itemId, 
                           BigDecimal price, String currency);

    /**
     * 购买商品
     * 
     * @param itemId 商品ID
     * @param buyerId 买家ID
     * @return 交易结果
     */
    Boolean purchaseItem(Long itemId, Long buyerId);

    /**
     * 获取市场商品列表
     * 
     * @param itemType 商品类型
     * @param page 页码
     * @param size 页面大小
     * @return 商品分页列表
     */
    Page<MarketplaceItem> getMarketplaceItems(String itemType, int page, int size);

    /**
     * 获取用户出售的商品
     * 
     * @param userId 用户ID
     * @return 商品列表
     */
    List<MarketplaceItem> getUserSellingItems(Long userId);

    /**
     * 下架商品
     * 
     * @param itemId 商品ID
     * @param userId 用户ID
     * @return 是否成功
     */
    Boolean cancelItem(Long itemId, Long userId);

    /**
     * 获取商品详情
     * 
     * @param itemId 商品ID
     * @return 商品信息
     */
    MarketplaceItem getItemDetail(Long itemId);
}
