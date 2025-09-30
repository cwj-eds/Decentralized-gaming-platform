package com.decentralized.gaming.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.decentralized.gaming.platform.entity.UserBalance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户余额Mapper接口
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
     * @return 用户余额
     */
    @Select("SELECT * FROM user_balances WHERE user_id = #{userId} AND token_type = #{tokenType}")
    UserBalance selectByUserIdAndTokenType(@Param("userId") Long userId, @Param("tokenType") String tokenType);

    /**
     * 根据用户ID查询所有余额
     *
     * @param userId 用户ID
     * @return 用户余额列表
     */
    @Select("SELECT * FROM user_balances WHERE user_id = #{userId}")
    List<UserBalance> selectByUserId(@Param("userId") Long userId);

    /**
     * 更新用户余额
     *
     * @param userId 用户ID
     * @param tokenType 代币类型
     * @param balance 新余额
     * @return 影响行数
     */
    @Update("UPDATE user_balances SET balance = #{balance}, updated_at = NOW() WHERE user_id = #{userId} AND token_type = #{tokenType}")
    int updateBalance(@Param("userId") Long userId, @Param("tokenType") String tokenType, @Param("balance") BigDecimal balance);

    /**
     * 增加用户余额
     *
     * @param userId 用户ID
     * @param tokenType 代币类型
     * @param amount 增加金额
     * @return 影响行数
     */
    @Update("UPDATE user_balances SET balance = balance + #{amount}, updated_at = NOW() WHERE user_id = #{userId} AND token_type = #{tokenType}")
    int addBalance(@Param("userId") Long userId, @Param("tokenType") String tokenType, @Param("amount") BigDecimal amount);

    /**
     * 减少用户余额
     *
     * @param userId 用户ID
     * @param tokenType 代币类型
     * @param amount 减少金额
     * @return 影响行数
     */
    @Update("UPDATE user_balances SET balance = balance - #{amount}, updated_at = NOW() WHERE user_id = #{userId} AND token_type = #{tokenType} AND balance >= #{amount}")
    int subtractBalance(@Param("userId") Long userId, @Param("tokenType") String tokenType, @Param("amount") BigDecimal amount);

    /**
     * 检查用户余额是否足够
     *
     * @param userId 用户ID
     * @param tokenType 代币类型
     * @param amount 需要金额
     * @return 是否足够
     */
    @Select("SELECT COUNT(*) > 0 FROM user_balances WHERE user_id = #{userId} AND token_type = #{tokenType} AND balance >= #{amount}")
    boolean checkBalanceSufficient(@Param("userId") Long userId, @Param("tokenType") String tokenType, @Param("amount") BigDecimal amount);
}
