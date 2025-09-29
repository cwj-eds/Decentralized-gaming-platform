package com.decentralized.gaming.platform.service;

import com.decentralized.gaming.platform.exception.BusinessException;
import com.decentralized.gaming.platform.util.PasswordUtils;
import org.springframework.stereotype.Service;

/**
 * 密码服务类
 * 处理密码相关的业务逻辑
 *
 * @author DecentralizedGamingPlatform
 */
@Service
public class PasswordService {

    /**
     * 验证用户密码
     *
     * @param rawPassword     用户输入的原始密码
     * @param encodedPassword 数据库中存储的加密密码
     * @return 是否匹配
     */
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }

        return PasswordUtils.matches(rawPassword, encodedPassword);
    }

    /**
     * 加密密码
     *
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public String encodePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new BusinessException("密码不能为空");
        }

        // 验证密码强度
        if (!PasswordUtils.isValidPassword(rawPassword)) {
            throw new BusinessException("密码强度不符合要求，至少6位且包含字母和数字");
        }

        return PasswordUtils.encode(rawPassword);
    }

    /**
     * 生成随机密码
     *
     * @param length 密码长度
     * @return 随机密码
     */
    public String generateRandomPassword(int length) {
        return PasswordUtils.generateRandomPassword(length);
    }

    /**
     * 检查密码是否需要更新
     *
     * @param encodedPassword 加密后的密码
     * @return 是否需要更新
     */
    public boolean needsPasswordUpdate(String encodedPassword) {
        return PasswordUtils.needsUpdate(encodedPassword);
    }

    /**
     * 验证密码修改请求
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param confirmPassword 确认新密码
     */
    public void validatePasswordChange(String oldPassword, String newPassword, String confirmPassword) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new BusinessException("新密码不能为空");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new BusinessException("两次输入的密码不一致");
        }

        if (oldPassword != null && oldPassword.equals(newPassword)) {
            throw new BusinessException("新密码不能与旧密码相同");
        }

        // 验证密码强度
        if (!PasswordUtils.isValidPassword(newPassword)) {
            throw new BusinessException("密码强度不符合要求，至少6位且包含字母和数字");
        }
    }
}
