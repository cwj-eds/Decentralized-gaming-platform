package com.decentralized.gaming.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.decentralized.gaming.platform.entity.User;

/**
 * 用户服务接口
 *
 * @author DecentralizedGamingPlatform
 */
public interface UserService extends IService<User> {

    /**
     * 根据钱包地址查询用户
     * 
     * @param walletAddress 钱包地址
     * @return 用户信息
     */
    User findByWalletAddress(String walletAddress);

    /**
     * 钱包登录
     * 
     * @param walletAddress 钱包地址
     * @param signature 签名
     * @param message 消息
     * @return 用户信息
     */
    User walletLogin(String walletAddress, String signature, String message);

    /**
     * 更新用户信息
     * 
     * @param userId 用户ID
     * @param username 用户名
     * @param email 邮箱
     * @param avatarUrl 头像URL
     * @return 更新后的用户信息
     */
    User updateUserInfo(Long userId, String username, String email, String avatarUrl);
}
