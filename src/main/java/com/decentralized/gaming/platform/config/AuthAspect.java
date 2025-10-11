package com.decentralized.gaming.platform.config;

import com.decentralized.gaming.platform.common.RequireAuth;
import com.decentralized.gaming.platform.exception.BusinessException;
import com.decentralized.gaming.platform.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 权限验证切面
 * 自动拦截标记了@RequireAuth注解的方法，进行用户认证验证
 *
 * @author DecentralizedGamingPlatform
 */
@Aspect
@Component
@Slf4j
public class AuthAspect {
    
    /**
     * 在执行标记了@RequireAuth的方法之前进行验证
     */
    @Before("@annotation(com.decentralized.gaming.platform.common.RequireAuth) || @within(com.decentralized.gaming.platform.common.RequireAuth)")
    public void checkAuth(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        // 获取@RequireAuth注解
        RequireAuth requireAuth = method.getAnnotation(RequireAuth.class);
        if (requireAuth == null) {
            // 如果方法上没有，尝试从类上获取
            requireAuth = method.getDeclaringClass().getAnnotation(RequireAuth.class);
        }
        
        // 检查是否已登录
        Long currentUserId = UserContext.getCurrentUserId();
        if (currentUserId == null) {
            log.warn("访问需要认证的接口但未登录，方法: {}", method.getName());
            throw new BusinessException("请先登录");
        }
        
        log.debug("用户认证通过，用户ID: {}, 方法: {}", currentUserId, method.getName());
        
        // 如果需要检查资源所有权
        if (requireAuth != null && requireAuth.checkOwnership()) {
            checkResourceOwnership(joinPoint, requireAuth, currentUserId);
        }
    }
    
    /**
     * 检查资源所有权
     */
    private void checkResourceOwnership(JoinPoint joinPoint, RequireAuth requireAuth, Long currentUserId) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = joinPoint.getArgs();
        
        String ownerIdParam = requireAuth.ownerIdParam();
        
        // 遍历参数，查找userId参数
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            
            // 检查参数名是否匹配
            if (parameter.getName().equals(ownerIdParam)) {
                Object arg = args[i];
                if (arg instanceof Long) {
                    Long resourceOwnerId = (Long) arg;
                    if (!currentUserId.equals(resourceOwnerId)) {
                        log.warn("用户尝试访问其他用户的资源，当前用户ID: {}, 资源所有者ID: {}", 
                                currentUserId, resourceOwnerId);
                        throw new BusinessException("无权访问其他用户的资源");
                    }
                }
                return;
            }
            
            // 检查是否有@RequestParam或@PathVariable注解
            RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
            if (requestParam != null && ownerIdParam.equals(requestParam.value())) {
                Object arg = args[i];
                if (arg instanceof Long) {
                    Long resourceOwnerId = (Long) arg;
                    if (!currentUserId.equals(resourceOwnerId)) {
                        log.warn("用户尝试访问其他用户的资源，当前用户ID: {}, 资源所有者ID: {}", 
                                currentUserId, resourceOwnerId);
                        throw new BusinessException("无权访问其他用户的资源");
                    }
                }
                return;
            }
            
            PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
            if (pathVariable != null && ownerIdParam.equals(pathVariable.value())) {
                Object arg = args[i];
                if (arg instanceof Long) {
                    Long resourceOwnerId = (Long) arg;
                    if (!currentUserId.equals(resourceOwnerId)) {
                        log.warn("用户尝试访问其他用户的资源，当前用户ID: {}, 资源所有者ID: {}", 
                                currentUserId, resourceOwnerId);
                        throw new BusinessException("无权访问其他用户的资源");
                    }
                }
                return;
            }
        }
    }
}

