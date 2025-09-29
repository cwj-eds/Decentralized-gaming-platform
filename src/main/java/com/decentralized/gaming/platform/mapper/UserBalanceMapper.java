package com.decentralized.gaming.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.decentralized.gaming.platform.entity.UserBalance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * 用户代币余额Mapper接口
 *
 * @author DecentralizedGamingPlatform
 */
@Mapper
public interface UserBalanceMapper extends BaseMapper<UserBalance> {

    /**
     * 根据用户ID和代币类型查询余额
     *
     * @param userId 用户ID
     * @param tokenType 代币类型
     * @return 余额信息
     */
    @Select("SELECT * FROM user_balances WHERE user_id = #{userId} AND token_type = #{tokenType}")
    UserBalance findByUserIdAndTokenType(@Param("userId") Long userId, @Param("tokenType") String tokenType);

    /**
     * 更新用户代币余额
     *
     * @param userId 用户ID
     * @param tokenType 代币类型
     * @param newBalance 新余额
     * @return 影响行数
     */
    @Update("UPDATE user_balances SET balance = #{newBalance}, updated_at = NOW() WHERE user_id = #{userId} AND token_type = #{tokenType}")
    int updateBalance(@Param("userId") Long userId, @Param("tokenType") String tokenType, @Param("newBalance") BigDecimal newBalance);

    /**
     * 增加用户代币余额
     *
     * @param userId 用户ID
     * @param tokenType 代币类型
     * @param amount 增加金额
     * @return 影响行数
     */
    @Update("UPDATE user_balances SET balance = balance + #{amount}, updated_at = NOW() WHERE user_id = #{userId} AND token_type = #{tokenType}")
    int increaseBalance(@Param("userId") Long userId, @Param("tokenType") String tokenType, @Param("amount") BigDecimal amount);

    /**
     * 减少用户代币余额
     *
     * @param userId 用户ID
     * @param tokenType 代币类型
     * @param amount 减少金额
     * @return 影响行数
     */
    @Update("UPDATE user_balances SET balance = balance - #{amount}, updated_at = NOW() WHERE user_id = #{userId} AND token_type = #{tokenType} AND balance >= #{amount}")
    int decreaseBalance(@Param("userId") Long userId, @Param("tokenType") String tokenType, @Param("amount") BigDecimal amount);
}
