package com.decentralized.gaming.platform.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security配置
 *
 * @author DecentralizedGamingPlatform
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF（因为使用JWT）
            .csrf(csrf -> csrf.disable())

            // 设置会话管理为无状态
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 配置授权规则
            .authorizeHttpRequests(authz -> authz
                // 允许访问静态资源
                .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                // 允许访问首页和认证页面
                .requestMatchers("/", "/index.html", "/auth/**", "/api/auth/**").permitAll()
                // 允许访问API文档
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                // API接口需要认证
                .requestMatchers("/api/**").authenticated()
                // 其他请求需要认证
                .anyRequest().authenticated()
            )

            // 添加JWT认证过滤器
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
