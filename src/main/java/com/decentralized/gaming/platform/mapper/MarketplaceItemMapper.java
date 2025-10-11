package com.decentralized.gaming.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.decentralized.gaming.platform.entity.MarketplaceItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 市场商品 Mapper
 */
@Mapper
public interface MarketplaceItemMapper extends BaseMapper<MarketplaceItem> {
    
    /**
     * 条件更新状态为SOLD（仅当状态为ACTIVE时）
     * 用于防止并发重复购买
     * 
     * @param id 商品ID
     * @return 更新的行数
     */
    @Update("UPDATE marketplace_items SET status = 'SOLD', updated_at = NOW() " +
            "WHERE id = #{id} AND status = 'ACTIVE'")
    int markSoldIfActive(@Param("id") Long id);
    
    /**
     * 条件更新状态为CANCELLED（仅当状态为ACTIVE时）
     * 用于取消上架
     * 
     * @param id 商品ID
     * @return 更新的行数
     */
    @Update("UPDATE marketplace_items SET status = 'CANCELLED', updated_at = NOW() " +
            "WHERE id = #{id} AND status = 'ACTIVE'")
    int markCancelledIfActive(@Param("id") Long id);
}


