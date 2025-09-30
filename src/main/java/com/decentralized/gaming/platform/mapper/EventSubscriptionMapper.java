package com.decentralized.gaming.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.decentralized.gaming.platform.entity.EventSubscription;
import org.apache.ibatis.annotations.Mapper;

/**
 * 事件订阅Mapper
 *
 * @author DecentralizedGamingPlatform
 */
@Mapper
public interface EventSubscriptionMapper extends BaseMapper<EventSubscription> {
}
