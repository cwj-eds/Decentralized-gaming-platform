package com.decentralized.gaming.platform.common;

import java.lang.annotation.*;

/**
 * 权限验证注解
 * 标记需要用户登录认证的方法
 * 可以在Controller的类或方法上使用
 *
 * @author DecentralizedGamingPlatform
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireAuth {
    
    /**
     * 是否需要验证资源所有权
     * 如果为true，会验证请求参数中的userId是否与当前登录用户一致
     */
    boolean checkOwnership() default false;
    
    /**
     * 资源所有者用户ID的参数名
     * 当checkOwnership为true时，会检查该参数名对应的值是否与当前登录用户ID一致
     */
    String ownerIdParam() default "userId";
}

