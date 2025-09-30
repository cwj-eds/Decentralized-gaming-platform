package com.decentralized.gaming.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.decentralized.gaming.platform.dto.LoginResponse;
import com.decentralized.gaming.platform.dto.UserLoginRequest;
import com.decentralized.gaming.platform.dto.UserRegisterRequest;
import com.decentralized.gaming.platform.dto.WalletLoginRequest;
import com.decentralized.gaming.platform.entity.User;
import com.decentralized.gaming.platform.entity.UserBalance;
import com.decentralized.gaming.platform.exception.BusinessException;
import com.decentralized.gaming.platform.mapper.UserBalanceMapper;
import com.decentralized.gaming.platform.mapper.UserMapper;
import com.decentralized.gaming.platform.service.blockchain.BlockchainService;
import com.decentralized.gaming.platform.service.PasswordService;
import com.decentralized.gaming.platform.service.UserService;
import com.decentralized.gaming.platform.util.JwtUtils;
import com.decentralized.gaming.platform.vo.UserVO;
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
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserBalanceMapper userBalanceMapper;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private BlockchainService blockchainService;

    @Override
    @Transactional
    public LoginResponse userLogin(UserLoginRequest request) {
        log.info("用户登录，用户名: {}", request.getUsername());

        try {
            // 参数验证
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                throw new BusinessException("用户名不能为空");
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                throw new BusinessException("密码不能为空");
            }

            // 查找用户（支持用户名或邮箱登录）
            User user = null;
            try {
                // 先尝试按用户名查找
                LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(User::getUsername, request.getUsername());
                user = userMapper.selectOne(queryWrapper);

                // 如果用户名没找到，尝试按邮箱查找
                if (user == null) {
                    queryWrapper.clear();
                    queryWrapper.eq(User::getEmail, request.getUsername());
                    user = userMapper.selectOne(queryWrapper);
                }
            } catch (Exception e) {
                log.error("数据库查询用户失败", e);
                throw new BusinessException("数据库查询失败，请稍后重试");
            }

            if (user == null) {
                log.warn("用户不存在: {}", request.getUsername());
                throw new BusinessException("用户不存在，请先注册");
            }

            // 验证密码
            try {
                if (!passwordService.verifyPassword(request.getPassword(), user.getPassword())) {
                    log.warn("密码错误，用户: {}", request.getUsername());
                    throw new BusinessException("密码错误，请检查后重试");
                }
            } catch (Exception e) {
                log.error("密码验证失败", e);
                throw new BusinessException("密码验证失败，请稍后重试");
            }

            // 更新最后登录时间
            try {
                user.setUpdatedAt(LocalDateTime.now());
                userMapper.updateById(user);
            } catch (Exception e) {
                log.error("更新用户登录时间失败", e);
                // 不抛出异常，允许登录继续
            }

            UserVO userVO = convertToVO(user);

            log.info("用户登录成功，用户ID: {}, 用户名: {}", user.getId(), user.getUsername());
            return new LoginResponse(userVO, "登录成功", true);

        } catch (BusinessException e) {
            throw e; // 重新抛出业务异常
        } catch (Exception e) {
            log.error("用户登录过程中发生未知异常", e);
            throw new BusinessException("登录过程中发生系统异常，请稍后重试");
        }
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
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, request.getUsername());
        if (userMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("用户名已存在");
        }

        // 检查邮箱是否已存在
        queryWrapper.clear();
        queryWrapper.eq(User::getEmail, request.getEmail());
        if (userMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("邮箱已存在");
        }

        // 检查钱包地址是否已存在（如果提供）
        if (request.getWalletAddress() != null && !request.getWalletAddress().trim().isEmpty()) {
            queryWrapper.clear();
            queryWrapper.eq(User::getWalletAddress, request.getWalletAddress())
                       .isNotNull(User::getWalletAddress);
            if (userMapper.selectCount(queryWrapper) > 0) {
                throw new BusinessException("钱包地址已注册");
            }
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

        UserVO userVO = convertToVO(user);

        log.info("用户注册成功，用户ID: {}", user.getId());
        return new LoginResponse(userVO, "注册成功", true);
    }

    @Override
    @Transactional
    public LoginResponse walletLogin(WalletLoginRequest request) {
        log.info("用户钱包登录，钱包地址: {}", request.getWalletAddress());

        // 验证签名
        if (!verifyWalletSignature(request.getWalletAddress(), request.getMessage(), request.getSignature())) {
            throw new BusinessException("钱包签名验证失败");
        }

        // 检查用户是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getWalletAddress, request.getWalletAddress())
                   .isNotNull(User::getWalletAddress);
        User user = userMapper.selectOne(queryWrapper);

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

        UserVO userVO = convertToVO(user);
        return new LoginResponse(userVO, "登录成功", true);
    }

    @Override
    public boolean verifyWalletSignature(String walletAddress, String message, String signature) {
        try {
            // 验证钱包地址格式
            if (!walletAddress.startsWith("0x") || walletAddress.length() != 42) {
                return false;
            }

            // 验证签名长度（65字节的签名）
            if (signature == null || !signature.startsWith("0x") || signature.length() != 132) {
                return false;
            }

            // 使用BlockchainService进行真实的签名验证
            boolean isValid = blockchainService.verifySignature(message, signature, walletAddress);
            if (isValid) {
                log.info("钱包签名验证通过，地址: {}", walletAddress);
            } else {
                log.warn("钱包签名验证失败，地址: {}", walletAddress);
            }
            return isValid;

        } catch (Exception e) {
            log.error("钱包签名验证失败", e);
            return false;
        }
    }

    @Override
    @Transactional
    public User createUser(User user) {
        // 检查钱包地址是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getWalletAddress, user.getWalletAddress())
                   .isNotNull(User::getWalletAddress);
        if (userMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("钱包地址已注册");
        }

        // 检查用户名是否已存在（如果提供）
        if (user.getUsername() != null) {
            queryWrapper.clear();
            queryWrapper.eq(User::getUsername, user.getUsername());
            if (userMapper.selectCount(queryWrapper) > 0) {
                throw new BusinessException("用户名已存在");
            }
        }

        // 检查邮箱是否已存在（如果提供）
        if (user.getEmail() != null) {
            queryWrapper.clear();
            queryWrapper.eq(User::getEmail, user.getEmail());
            if (userMapper.selectCount(queryWrapper) > 0) {
                throw new BusinessException("邮箱已存在");
            }
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
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getWalletAddress, walletAddress)
                   .isNotNull(User::getWalletAddress);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User findByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User findByEmail(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User findById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public boolean isWalletRegistered(String walletAddress) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getWalletAddress, walletAddress)
                   .isNotNull(User::getWalletAddress);
        return userMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public boolean isUsernameExists(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        return userMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public boolean isEmailExists(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        return userMapper.selectCount(queryWrapper) > 0;
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
    public UserVO getUserById(Long id) {
        User user = findById(id);
        return convertToVO(user);
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
