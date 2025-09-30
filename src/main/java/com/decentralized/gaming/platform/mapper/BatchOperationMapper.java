package com.decentralized.gaming.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.decentralized.gaming.platform.entity.BatchOperation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 批量操作Mapper
 *
 * @author DecentralizedGamingPlatform
 */
@Mapper
public interface BatchOperationMapper extends BaseMapper<BatchOperation> {
}
