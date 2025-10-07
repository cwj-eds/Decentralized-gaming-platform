package com.decentralized.gaming.platform.controller.blockchain;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 区块链Web控制器
 * 提供区块链相关的页面访问
 *
 * @author DecentralizedGamingPlatform
 */
@Controller
@RequestMapping("/blockchain")
public class BlockchainWebController {

    /**
     * 智能合约部署页面
     */
    @GetMapping("/deployment")
    public String deploymentPage(Model model) {
        model.addAttribute("pageTitle", "智能合约部署 - 去中心化游戏平台");
        return "blockchain/deployment";
    }
}
