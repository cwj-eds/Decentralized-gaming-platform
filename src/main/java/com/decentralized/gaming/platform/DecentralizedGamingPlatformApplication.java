package com.decentralized.gaming.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 去中心化游戏平台启动类
 *
 * @author DecentralizedGamingPlatform
 * @since 2024
 */
@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@MapperScan("com.decentralized.gaming.platform.mapper")
@EnableAsync
@EnableTransactionManagement
public class DecentralizedGamingPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(DecentralizedGamingPlatformApplication.class, args);
        System.out.println("===========================================");
        System.out.println("    去中心化游戏平台启动成功!");
        System.out.println("    访问地址: http://localhost:8080");
        System.out.println("    API文档: http://localhost:8080/swagger-ui.html");
        System.out.println("===========================================");
    }
}
