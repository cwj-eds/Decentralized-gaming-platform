//package com.decentralized.gaming.platform.service.impl;
//
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.decentralized.gaming.platform.entity.User;
//import com.decentralized.gaming.platform.exception.BusinessException;
//import com.decentralized.gaming.platform.mapper.UserMapper;
//import com.decentralized.gaming.platform.service.UserService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
///**
// * 用户服务实现类
// *
// * @author DecentralizedGamingPlatform
// */
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
//
//    @Override
//    public User findByWalletAddress(String walletAddress) {
//        if (StringUtils.isBlank(walletAddress)) {
//            throw new BusinessException("钱包地址不能为空");
//        }
//
//        return baseMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>()
//                .eq("wallet_address", walletAddress));
//    }
//
//    @Override
//    public User walletLogin(String walletAddress, String signature, String message) {
//        if (StringUtils.isBlank(walletAddress)) {
//            throw new BusinessException("钱包地址不能为空");
//        }
//
//        // TODO: 验证签名
//
//        // 查找或创建用户
//        User user = findByWalletAddress(walletAddress);
//        if (user == null) {
//            user = new User();
//            user.setWalletAddress(walletAddress);
//            user.setUsername("用户_" + walletAddress.substring(walletAddress.length() - 6));
//            user.setCreatedAt(LocalDateTime.now());
//            user.setUpdatedAt(LocalDateTime.now());
//            baseMapper.insert(user);
//            log.info("创建新用户: {}", walletAddress);
//        } else {
//            user.setUpdatedAt(LocalDateTime.now());
//            baseMapper.updateById(user);
//            log.info("用户登录: {}", walletAddress);
//        }
//
//        return user;
//    }
//
//    @Override
//    public User updateUserInfo(Long userId, String username, String email, String avatarUrl) {
//        User user = baseMapper.selectById(userId);
//        if (user == null) {
//            throw new BusinessException("用户不存在");
//        }
//
//        if (StringUtils.isNotBlank(username)) {
//            user.setUsername(username);
//        }
//        if (StringUtils.isNotBlank(email)) {
//            user.setEmail(email);
//        }
//        if (StringUtils.isNotBlank(avatarUrl)) {
//            user.setAvatarUrl(avatarUrl);
//        }
//
//        user.setUpdatedAt(LocalDateTime.now());
//        baseMapper.updateById(user);
//
//        return user;
//    }
//}
