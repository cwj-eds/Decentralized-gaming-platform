package com.decentralized.gaming.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.decentralized.gaming.platform.entity.Game;
import org.apache.ibatis.annotations.Mapper;

/**
 * 游戏Mapper接口
 *
 * @author DecentralizedGamingPlatform
 */
@Mapper
public interface GameMapper extends BaseMapper<Game> {

}
