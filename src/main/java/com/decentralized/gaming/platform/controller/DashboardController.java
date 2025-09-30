package com.decentralized.gaming.platform.controller;

import com.decentralized.gaming.platform.service.UserService;
import com.decentralized.gaming.platform.util.JwtUtils;
import com.decentralized.gaming.platform.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

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
    private final JwtUtils jwtUtils;

    /**
     * 用户仪表板页面
     *
     * @param model 模型
     * @param request HTTP请求
     * @return 视图名称
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpServletRequest request) {
        try {
            // 从请求头中获取JWT令牌
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("未找到有效的Authorization头");
                return "redirect:/auth/login";
            }

            String token = authHeader.substring(7); // 移除"Bearer "前缀

            // 验证JWT令牌
            if (!jwtUtils.validateToken(token)) {
                log.warn("JWT令牌验证失败");
                return "redirect:/auth/login";
            }

            // 从令牌中获取用户ID
            Long userId = jwtUtils.getUserIdFromToken(token);
            if (userId == null) {
                log.warn("无法从JWT令牌中获取用户ID");
                return "redirect:/auth/login";
            }

            // 从数据库获取用户信息
            UserVO user = userService.getUserById(userId);
            if (user == null) {
                log.warn("用户不存在，用户ID: {}", userId);
                return "redirect:/auth/login";
            }

            model.addAttribute("user", user);
            log.info("用户仪表板加载成功，用户ID: {}", userId);
            return "dashboard";
        } catch (Exception e) {
            log.error("加载仪表板页面失败", e);
            return "redirect:/auth/login";
        }
    }
}
