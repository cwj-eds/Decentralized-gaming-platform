package com.decentralized.gaming.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.decentralized.gaming.platform.entity.MarketplaceItem;

/**
 * 市场商品 Mapper
 */
public interface MarketplaceItemMapper extends BaseMapper<MarketplaceItem> {
    @org.apache.ibatis.annotations.Update("UPDATE marketplace_items SET status='SOLD', updated_at=NOW() WHERE id=#{id} AND status='ACTIVE'")
    int markSoldIfActive(Long id);

    @org.apache.ibatis.annotations.Update("UPDATE marketplace_items SET status='CANCELLED', updated_at=NOW() WHERE id=#{id} AND status='ACTIVE'")
    int markCancelledIfActive(Long id);
}


