package com.decentralized.gaming.platform.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 密码工具类
 * 提供密码加密、验证等功能
 *
 * @author DecentralizedGamingPlatform
 */
@Component
public class PasswordUtils {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    /**
     * 加密密码
     *
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public static String encode(String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * 验证密码
     *
     * @param rawPassword     原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 验证密码强度
     *
     * @param password 密码
     * @return 是否符合强度要求（至少6位，包含字母和数字）
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }

        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }

            if (hasLetter && hasDigit) {
                return true;
            }
        }

        return false;
    }

    /**
     * 生成随机密码
     *
     * @param length 密码长度
     * @return 随机密码
     */
    public static String generateRandomPassword(int length) {
        if (length < 6) {
            throw new IllegalArgumentException("密码长度不能小于6位");
        }

        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*";
        String allChars = upperCase + lowerCase + digits + specialChars;

        StringBuilder password = new StringBuilder();

        // 确保至少包含一个大写字母、一个小写字母、一个数字和一个特殊字符
        password.append(upperCase.charAt((int) (Math.random() * upperCase.length())));
        password.append(lowerCase.charAt((int) (Math.random() * lowerCase.length())));
        password.append(digits.charAt((int) (Math.random() * digits.length())));
        password.append(specialChars.charAt((int) (Math.random() * specialChars.length())));

        // 填充剩余长度
        for (int i = 4; i < length; i++) {
            password.append(allChars.charAt((int) (Math.random() * allChars.length())));
        }

        // 打乱字符顺序
        for (int i = 0; i < password.length(); i++) {
            int j = (int) (Math.random() * password.length());
            char temp = password.charAt(i);
            password.setCharAt(i, password.charAt(j));
            password.setCharAt(j, temp);
        }

        return password.toString();
    }

    /**
     * 检查密码是否需要更新（如果使用旧的加密方式）
     *
     * @param encodedPassword 加密后的密码
     * @return 是否需要更新
     */
    public static boolean needsUpdate(String encodedPassword) {
        if (encodedPassword == null || encodedPassword.isEmpty()) {
            return true;
        }

        // 检查是否是旧版本的BCrypt密码（版本标识符）
        return !encodedPassword.startsWith("$2a$") && !encodedPassword.startsWith("$2b$") && !encodedPassword.startsWith("$2y$");
    }
}
