package com.decentralized.gaming.platform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Web控制器
 * 处理页面请求
 *
 * @author DecentralizedGamingPlatform
 */
@Controller
public class WebController {

    /**
     * 登录页面
     */
    @GetMapping("/auth/login")
    public String login() {
        return "auth/login";
    }

    /**
     * 注册页面
     */
    @GetMapping("/auth/register")
    public String register() {
        return "auth/register";
    }

    /**
     * 首页
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * IPFS 联调测试页
     */
    @GetMapping("/ipfs/test")
    public String ipfsTest() {
        return "ipfs/test";
    }
}






