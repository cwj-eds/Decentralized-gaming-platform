package com.decentralized.gaming.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.decentralized.gaming.platform.entity.GameItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 游戏道具Mapper接口
 *
 * @author DecentralizedGamingPlatform
 */
@Mapper
public interface GameItemMapper extends BaseMapper<GameItem> {
}

