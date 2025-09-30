package com.decentralized.gaming.platform.util;

import org.springframework.stereotype.Component;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 密码工具类
 * 提供密码加密、验证等功能
 *
 * @author DecentralizedGamingPlatform
 */
@Component
public class PasswordUtils {

    private static final String SALT_PREFIX = "$2a$12$";
    private static final SecureRandom random = new SecureRandom();

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
        
        try {
            // 生成随机盐
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            
            // 使用SHA-256进行哈希
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(rawPassword.getBytes());
            
            // 组合盐和哈希值
            byte[] combined = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hashedPassword, 0, combined, salt.length, hashedPassword.length);
            
            return SALT_PREFIX + Base64.getEncoder().encodeToString(combined);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("密码加密失败", e);
        }
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
        
        try {
            // 检查是否是我们的格式
            if (!encodedPassword.startsWith(SALT_PREFIX)) {
                return false;
            }
            
            // 解码
            String encoded = encodedPassword.substring(SALT_PREFIX.length());
            byte[] combined = Base64.getDecoder().decode(encoded);
            
            if (combined.length < 16) {
                return false;
            }
            
            // 提取盐和哈希值
            byte[] salt = new byte[16];
            byte[] storedHash = new byte[combined.length - 16];
            System.arraycopy(combined, 0, salt, 0, 16);
            System.arraycopy(combined, 16, storedHash, 0, storedHash.length);
            
            // 重新计算哈希
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] computedHash = md.digest(rawPassword.getBytes());
            
            // 比较哈希值
            return MessageDigest.isEqual(storedHash, computedHash);
        } catch (Exception e) {
            return false;
        }
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

        // 检查是否是我们新的加密格式
        return !encodedPassword.startsWith(SALT_PREFIX);
    }
}
