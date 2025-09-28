package com.decentralized.gaming.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.decentralized.gaming.platform.entity.MarketplaceItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 市场商品Mapper接口
 *
 * @author DecentralizedGamingPlatform
 */
@Mapper
public interface MarketplaceItemMapper extends BaseMapper<MarketplaceItem> {

}
