package com.decentralized.gaming.platform.service;

import com.decentralized.gaming.platform.dto.LoginResponse;
import com.decentralized.gaming.platform.dto.UserLoginRequest;
import com.decentralized.gaming.platform.dto.UserRegisterRequest;
import com.decentralized.gaming.platform.dto.WalletLoginRequest;
import com.decentralized.gaming.platform.entity.User;
import com.decentralized.gaming.platform.vo.UserVO;

/**
 * 用户服务接口
 *
 * @author DecentralizedGamingPlatform
 */
public interface UserService {

    /**
     * 用户名密码登录
     *
     * @param request 用户登录请求
     * @return 登录响应
     */
    LoginResponse userLogin(UserLoginRequest request);

    /**
     * 用户注册
     *
     * @param request 注册请求
     * @return 登录响应
     */
    LoginResponse register(UserRegisterRequest request);

    /**
     * 钱包登录/注册
     *
     * @param request 钱包登录请求
     * @return 登录响应
     */
    LoginResponse walletLogin(WalletLoginRequest request);

    /**
     * 验证钱包签名
     *
     * @param walletAddress 钱包地址
     * @param message 原始消息
     * @param signature 签名
     * @return 是否验证成功
     */
    boolean verifyWalletSignature(String walletAddress, String message, String signature);

    /**
     * 创建新用户
     *
     * @param user 用户信息
     * @return 创建后的用户
     */
    User createUser(User user);

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 更新后的用户
     */
    User updateUser(User user);

    /**
     * 根据钱包地址查找用户
     *
     * @param walletAddress 钱包地址
     * @return 用户信息
     */
    User findByWalletAddress(String walletAddress);

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User findByUsername(String username);

    /**
     * 根据邮箱查找用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    User findByEmail(String email);

    /**
     * 根据ID查找用户
     *
     * @param id 用户ID
     * @return 用户信息
     */
    User findById(Long id);

    /**
     * 检查钱包地址是否已注册
     *
     * @param walletAddress 钱包地址
     * @return 是否已注册
     */
    boolean isWalletRegistered(String walletAddress);

    /**
     * 检查用户名是否已存在
     *
     * @param username 用户名
     * @return 是否已存在
     */
    boolean isUsernameExists(String username);

    /**
     * 检查邮箱是否已存在
     *
     * @param email 邮箱
     * @return 是否已存在
     */
    boolean isEmailExists(String email);

    /**
     * 设置用户密码
     *
     * @param userId 用户ID
     * @param password 密码
     */
    void setUserPassword(Long userId, String password);

    /**
     * 验证用户密码
     *
     * @param userId 用户ID
     * @param password 密码
     * @return 是否正确
     */
    boolean verifyUserPassword(Long userId, String password);

    /**
     * 初始化用户代币余额
     *
     * @param userId 用户ID
     */
    void initializeUserBalance(Long userId);

    /**
     * 根据ID获取用户VO
     *
     * @param id 用户ID
     * @return 用户VO
     */
    UserVO getUserById(Long id);

    /**
     * 实体转VO
     *
     * @param user 用户实体
     * @return 用户VO
     */
    UserVO convertToVO(User user);
}






