package com.decentralized.gaming.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.decentralized.gaming.platform.entity.Agent;
import org.apache.ibatis.annotations.Mapper;

/**
 * 智能体Mapper接口
 *
 * @author DecentralizedGamingPlatform
 */
@Mapper
public interface AgentMapper extends BaseMapper<Agent> {

}