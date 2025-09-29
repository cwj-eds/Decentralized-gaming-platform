package com.decentralized.gaming.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.decentralized.gaming.platform.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

/**
 * 用户Mapper接口
 *
 * @author DecentralizedGamingPlatform
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Select("SELECT * FROM users WHERE username = #{username}")
    Optional<User> findByUsername(@Param("username") String username);

    /**
     * 根据邮箱查找用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    @Select("SELECT * FROM users WHERE email = #{email}")
    Optional<User> findByEmail(@Param("email") String email);

    /**
     * 根据钱包地址查找用户
     *
     * @param walletAddress 钱包地址
     * @return 用户信息
     */
    @Select("SELECT * FROM users WHERE wallet_address = #{walletAddress}")
    Optional<User> findByWalletAddress(@Param("walletAddress") String walletAddress);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM users WHERE username = #{username}")
    boolean existsByUsername(@Param("username") String username);

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM users WHERE email = #{email}")
    boolean existsByEmail(@Param("email") String email);

    /**
     * 检查钱包地址是否存在
     *
     * @param walletAddress 钱包地址
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM users WHERE wallet_address = #{walletAddress} AND wallet_address IS NOT NULL")
    boolean existsByWalletAddress(@Param("walletAddress") String walletAddress);
}

