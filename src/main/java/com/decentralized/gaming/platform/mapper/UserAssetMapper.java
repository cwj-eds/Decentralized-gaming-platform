package com.decentralized.gaming.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.decentralized.gaming.platform.entity.UserAsset;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户资产Mapper接口
 *
 * @author DecentralizedGamingPlatform
 */
@Mapper
public interface UserAssetMapper extends BaseMapper<UserAsset> {
}

