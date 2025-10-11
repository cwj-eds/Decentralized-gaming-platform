package com.decentralized.gaming.platform.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 用户上下文工具类
 * 用于获取当前登录用户的信息
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
public class UserContext {
    
    /**
     * 用户ID的属性名（存储在Authentication的details中）
     */
    public static final String USER_ID_KEY = "userId";
    
    /**
     * 用户名的属性名（存储在Authentication的principal中）
     */
    public static final String USERNAME_KEY = "username";
    
    /**
     * 获取当前登录用户的ID
     *
     * @return 用户ID，如果未登录则返回null
     */
    public static Long getCurrentUserId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                log.debug("用户未认证");
                return null;
            }
            
            // 尝试从details中获取userId
            Object details = authentication.getDetails();
            if (details instanceof Long) {
                return (Long) details;
            }
            
            // 尝试从principal中获取userId
            Object principal = authentication.getPrincipal();
            if (principal instanceof Long) {
                return (Long) principal;
            }
            
            log.debug("无法从认证信息中获取用户ID");
            return null;
        } catch (Exception e) {
            log.error("获取当前用户ID失败", e);
            return null;
        }
    }
    
    /**
     * 获取当前登录用户的用户名
     *
     * @return 用户名，如果未登录则返回null
     */
    public static String getCurrentUsername() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return null;
            }
            
            Object principal = authentication.getPrincipal();
            if (principal instanceof String) {
                return (String) principal;
            }
            
            return authentication.getName();
        } catch (Exception e) {
            log.error("获取当前用户名失败", e);
            return null;
        }
    }
    
    /**
     * 检查是否已登录
     *
     * @return 是否已登录
     */
    public static boolean isAuthenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return authentication != null && authentication.isAuthenticated();
        } catch (Exception e) {
            log.error("检查登录状态失败", e);
            return false;
        }
    }
    
    /**
     * 检查当前用户是否是指定用户
     *
     * @param userId 要检查的用户ID
     * @return 是否是指定用户
     */
    public static boolean isCurrentUser(Long userId) {
        if (userId == null) {
            return false;
        }
        Long currentUserId = getCurrentUserId();
        return userId.equals(currentUserId);
    }
    
    /**
     * 要求必须已登录，否则抛出异常
     *
     * @return 当前用户ID
     * @throws SecurityException 如果未登录
     */
    public static Long requireCurrentUserId() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new SecurityException("未登录或登录已过期");
        }
        return userId;
    }
    
    /**
     * 要求当前用户必须是指定用户，否则抛出异常
     *
     * @param userId 要检查的用户ID
     * @throws SecurityException 如果不是指定用户
     */
    public static void requireCurrentUser(Long userId) {
        if (!isCurrentUser(userId)) {
            throw new SecurityException("无权访问其他用户的资源");
        }
    }
}

