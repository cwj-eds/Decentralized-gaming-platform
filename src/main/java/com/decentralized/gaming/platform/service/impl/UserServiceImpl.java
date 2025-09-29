package com.decentralized.gaming.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.decentralized.gaming.platform.dto.LoginResponse;
import com.decentralized.gaming.platform.dto.UserRegisterRequest;
import com.decentralized.gaming.platform.dto.WalletLoginRequest;
import com.decentralized.gaming.platform.entity.User;
import com.decentralized.gaming.platform.entity.UserBalance;
import com.decentralized.gaming.platform.exception.BusinessException;
import com.decentralized.gaming.platform.mapper.UserBalanceMapper;
import com.decentralized.gaming.platform.mapper.UserMapper;
import com.decentralized.gaming.platform.service.PasswordService;
import com.decentralized.gaming.platform.service.UserService;
import com.decentralized.gaming.platform.util.JwtUtils;
import com.decentralized.gaming.platform.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户服务实现类
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserBalanceMapper userBalanceMapper;
    private final PasswordService passwordService;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public LoginResponse walletLogin(WalletLoginRequest request) {
        log.info("用户钱包登录，钱包地址: {}", request.getWalletAddress());

        // 验证签名
        if (!verifyWalletSignature(request.getWalletAddress(), request.getMessage(), request.getSignature())) {
            throw new BusinessException("钱包签名验证失败");
        }

        // 检查用户是否已存在
        User user = userMapper.findByWalletAddress(request.getWalletAddress()).orElse(null);

        if (user == null) {
            // 新用户，创建用户
            user = createUserFromRequest(request);
            user = userMapper.insert(user) > 0 ? user : null;

            if (user == null) {
                throw new BusinessException("用户创建失败");
            }

            // 初始化用户代币余额
            initializeUserBalance(user.getId());

            log.info("新用户创建成功，用户ID: {}", user.getId());
        } else {
            // 现有用户，更新最后登录时间
            user.setUpdatedAt(LocalDateTime.now());
            userMapper.updateById(user);
            log.info("用户登录成功，用户ID: {}", user.getId());
        }

        // 生成JWT令牌
        String token = jwtUtils.generateToken(user.getId(), user.getWalletAddress());
        UserVO userVO = convertToVO(user);

        return new LoginResponse(token, "Bearer", jwtUtils.getExpirationTime(), userVO);
    }

    @Override
    @Transactional
    public LoginResponse register(UserRegisterRequest request) {
        log.info("用户注册，用户名: {}", request.getUsername());

        // 验证密码确认
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("两次输入的密码不一致");
        }

        // 检查用户名是否已存在
        if (userMapper.existsByUsername(request.getUsername())) {
            throw new BusinessException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (userMapper.existsByEmail(request.getEmail())) {
            throw new BusinessException("邮箱已存在");
        }

        // 检查钱包地址是否已存在（如果提供）
        if (request.getWalletAddress() != null && !request.getWalletAddress().trim().isEmpty() && userMapper.existsByWalletAddress(request.getWalletAddress())) {
            throw new BusinessException("钱包地址已注册");
        }

        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setWalletAddress(request.getWalletAddress());
        user.setPassword(passwordService.encodePassword(request.getPassword()));

        user = userMapper.insert(user) > 0 ? user : null;

        if (user == null) {
            throw new BusinessException("用户创建失败");
        }

        // 初始化用户代币余额
        initializeUserBalance(user.getId());

        // 生成JWT令牌
        String token = jwtUtils.generateToken(user.getId(), user.getWalletAddress());
        UserVO userVO = convertToVO(user);

        log.info("用户注册成功，用户ID: {}", user.getId());
        return new LoginResponse(token, "Bearer", jwtUtils.getExpirationTime(), userVO);
    }

    @Override
    public boolean verifyWalletSignature(String walletAddress, String message, String signature) {
        try {
            // 验证钱包地址格式
            if (!walletAddress.startsWith("0x") || walletAddress.length() != 42) {
                return false;
            }

            // 简单的签名验证 - 在实际项目中应该使用Web3j的正确API
            log.warn("签名验证功能暂未完全实现，使用简化验证");

            // 验证签名长度（65字节的签名）
            if (signature == null || !signature.startsWith("0x") || signature.length() != 132) {
                return false;
            }

            return true; // 临时返回true，实际应该验证签名

        } catch (Exception e) {
            log.error("钱包签名验证失败", e);
            return false;
        }
    }

    @Override
    @Transactional
    public User createUser(User user) {
        // 检查钱包地址是否已存在
        if (userMapper.existsByWalletAddress(user.getWalletAddress())) {
            throw new BusinessException("钱包地址已注册");
        }

        // 检查用户名是否已存在（如果提供）
        if (user.getUsername() != null && userMapper.existsByUsername(user.getUsername())) {
            throw new BusinessException("用户名已存在");
        }

        // 检查邮箱是否已存在（如果提供）
        if (user.getEmail() != null && userMapper.existsByEmail(user.getEmail())) {
            throw new BusinessException("邮箱已存在");
        }

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        if (userMapper.insert(user) > 0) {
            // 初始化用户代币余额
            initializeUserBalance(user.getId());
            return user;
        }

        throw new BusinessException("用户创建失败");
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        if (userMapper.updateById(user) > 0) {
            return userMapper.selectById(user.getId());
        }
        throw new BusinessException("用户更新失败");
    }

    @Override
    public User findByWalletAddress(String walletAddress) {
        return userMapper.findByWalletAddress(walletAddress).orElse(null);
    }

    @Override
    public User findById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public boolean isWalletRegistered(String walletAddress) {
        return userMapper.existsByWalletAddress(walletAddress);
    }

    @Override
    @Transactional
    public void setUserPassword(Long userId, String password) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setPassword(passwordService.encodePassword(password));
        updateUser.setUpdatedAt(LocalDateTime.now());

        if (userMapper.updateById(updateUser) <= 0) {
            throw new BusinessException("密码设置失败");
        }
    }

    @Override
    public boolean verifyUserPassword(Long userId, String password) {
        User user = userMapper.selectById(userId);
        if (user == null || user.getPassword() == null) {
            return false;
        }

        return passwordService.verifyPassword(password, user.getPassword());
    }

    @Override
    @Transactional
    public void initializeUserBalance(Long userId) {
        // 检查是否已存在用户余额
        LambdaQueryWrapper<UserBalance> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserBalance::getUserId, userId)
                   .eq(UserBalance::getTokenType, "PLATFORM_TOKEN");

        Long count = userBalanceMapper.selectCount(queryWrapper);
        if (count > 0) {
            return; // 已经初始化过
        }

        // 创建初始代币余额记录
        UserBalance balance = new UserBalance();
        balance.setUserId(userId);
        balance.setTokenType("PLATFORM_TOKEN");
        balance.setBalance(BigDecimal.ZERO);

        userBalanceMapper.insert(balance);
    }

    @Override
    public UserVO convertToVO(User user) {
        if (user == null) {
            return null;
        }

        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setWalletAddress(user.getWalletAddress());
        vo.setUsername(user.getUsername());
        vo.setEmail(user.getEmail());
        vo.setAvatarUrl(user.getAvatarUrl());
        vo.setCreatedAt(user.getCreatedAt());
        vo.setUpdatedAt(user.getUpdatedAt());

        return vo;
    }

    /**
     * 从钱包登录请求创建用户
     */
    private User createUserFromRequest(WalletLoginRequest request) {
        User user = new User();
        user.setWalletAddress(request.getWalletAddress());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        // 如果提供了密码，进行加密
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(passwordService.encodePassword(request.getPassword()));
        }

        return user;
    }
}
