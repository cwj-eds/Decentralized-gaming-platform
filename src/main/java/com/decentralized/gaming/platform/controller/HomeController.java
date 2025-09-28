//package com.decentralized.gaming.platform.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
///**
// * 主页控制器
// *
// * @author DecentralizedGamingPlatform
// */
//@Controller
//public class HomeController {
//
//    /**
//     * 首页
//     */
//    @GetMapping("/")
//    public String index(Model model) {
//        // TODO: 添加首页数据
//        model.addAttribute("pageTitle", "首页");
//
//        // 模拟统计数据
//        model.addAttribute("totalGames", 0);
//        model.addAttribute("totalAgents", 0);
//        model.addAttribute("totalUsers", 0);
//        model.addAttribute("totalTransactions", 0);
//
//        return "index";
//    }
//
//    /**
//     * 游戏中心
//     */
//    @GetMapping("/games")
//    public String games(Model model) {
//        model.addAttribute("pageTitle", "游戏中心");
//        return "games/list";
//    }
//
//    /**
//     * 智能体平台
//     */
//    @GetMapping("/agents")
//    public String agents(Model model) {
//        model.addAttribute("pageTitle", "智能体平台");
//        return "agents/list";
//    }
//
//    /**
//     * 游戏制作页面
//     */
//    @GetMapping("/agents/game-maker")
//    public String gameMaker(Model model) {
//        model.addAttribute("pageTitle", "AI游戏制作");
//        return "agents/game-maker";
//    }
//
//    /**
//     * 交易市场
//     */
//    @GetMapping("/marketplace")
//    public String marketplace(Model model) {
//        model.addAttribute("pageTitle", "交易市场");
//        return "marketplace/index";
//    }
//
//    /**
//     * 资产管理
//     */
//    @GetMapping("/assets")
//    public String assets(Model model) {
//        model.addAttribute("pageTitle", "资产管理");
//        return "assets/dashboard";
//    }
//
//    /**
//     * 充值页面
//     */
//    @GetMapping("/payment/recharge")
//    public String recharge(Model model) {
//        model.addAttribute("pageTitle", "账户充值");
//        return "payment/recharge";
//    }
//}
