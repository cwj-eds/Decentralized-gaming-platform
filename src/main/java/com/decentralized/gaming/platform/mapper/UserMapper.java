package com.decentralized.gaming.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.decentralized.gaming.platform.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问层
 * 使用 MyBatis-Plus 内置查询接口，复杂查询通过 LambdaQueryWrapper 实现
 *
 * @author DecentralizedGamingPlatform
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 所有查询方法已迁移到 UserServiceImpl 中使用 MyBatis-Plus 的 LambdaQueryWrapper
    // 如需复杂多表查询，可在此处添加自定义方法或使用 XML 映射文件
}