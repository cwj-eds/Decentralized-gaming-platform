package com.decentralized.gaming.platform.controller;

import com.decentralized.gaming.platform.service.UserService;
import com.decentralized.gaming.platform.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 仪表板控制器
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DashboardController {

    private final UserService userService;

    /**
     * 用户仪表板页面
     *
     * @param model 模型
     * @return 视图名称
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            // 这里应该从JWT令牌中获取当前用户
            // 目前使用模拟数据
            UserVO user = new UserVO();
            user.setId(1L);
            user.setUsername("testuser");
            user.setEmail("test@example.com");
            user.setWalletAddress("0x742d35cc6aa8b8c7d8f5e8b9c8b7c6d5e4f3a2b1c");
            user.setAvatarUrl("/images/default-avatar.png");
            user.setCreatedAt(java.time.LocalDateTime.now());
            user.setUpdatedAt(java.time.LocalDateTime.now());

            model.addAttribute("user", user);
            return "dashboard";
        } catch (Exception e) {
            log.error("加载仪表板页面失败", e);
            return "redirect:/auth/login";
        }
    }
}
