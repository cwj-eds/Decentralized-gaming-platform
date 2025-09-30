package com.decentralized.gaming.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.decentralized.gaming.platform.entity.Agent;
import com.decentralized.gaming.platform.vo.AgentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 智能体数据访问层
 *
 * @author DecentralizedGamingPlatform
 */
@Mapper
public interface AgentMapper extends BaseMapper<Agent> {

    // 所有查询方法已迁移到 AgentServiceImpl 中使用 MyBatis-Plus 的 LambdaQueryWrapper
}

