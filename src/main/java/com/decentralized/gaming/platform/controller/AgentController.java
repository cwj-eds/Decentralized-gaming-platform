package com.decentralized.gaming.platform.controller;

import com.decentralized.gaming.platform.common.PageResult;
import com.decentralized.gaming.platform.common.Result;
import com.decentralized.gaming.platform.dto.CreateAgentRequest;
import com.decentralized.gaming.platform.dto.GameGenerationRequest;
import com.decentralized.gaming.platform.service.AgentService;
import com.decentralized.gaming.platform.vo.AgentStatistics;
import com.decentralized.gaming.platform.vo.AgentVO;
import com.decentralized.gaming.platform.vo.GameGenerationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 智能体控制器
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Controller
@RequestMapping("/agents")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AgentController {

    private final AgentService agentService;

    /**
     * 智能体平台首页
     */
    @GetMapping
    public String index(Model model) {
        // 获取热门智能体
        List<AgentVO> popularAgents = agentService.getPopularAgents(6);
        
        // 获取智能体统计信息
        AgentStatistics statistics = agentService.getAgentStatistics();
        
        model.addAttribute("popularAgents", popularAgents);
        model.addAttribute("statistics", statistics);
        model.addAttribute("pageTitle", "智能体平台");
        
        return "agents/index";
    }

    /**
     * 智能体列表页面
     */
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "12") int size,
                       @RequestParam(required = false) String type,
                       Model model) {
        PageResult<AgentVO> agents;
        
        if (type != null && !type.isEmpty()) {
            agents = agentService.getAgentsByType(type, page, size);
        } else {
            agents = agentService.getActiveAgents(page, size);
        }
        
        model.addAttribute("agents", agents);
        model.addAttribute("currentType", type);
        model.addAttribute("pageTitle", "智能体列表");
        
        return "agents/list";
    }

    /**
     * 智能体详情页面
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        AgentVO agent = agentService.getAgentDetail(id);
        model.addAttribute("agent", agent);
        model.addAttribute("pageTitle", agent.getName() + " - 智能体详情");
        
        return "agents/detail";
    }

    /**
     * 游戏制作智能体页面
     */
    @GetMapping("/game-maker")
    public String gameMaker(Model model) {
        // 获取游戏制作智能体
        List<AgentVO> gameMakerAgents = agentService.getAgentsByType("GAME_MAKER", 1, 10).getRecords();
        
        model.addAttribute("gameMakerAgents", gameMakerAgents);
        model.addAttribute("pageTitle", "游戏制作智能体");
        
        return "agents/game-maker";
    }

    /**
     * 智能体上传页面
     */
    @GetMapping("/upload")
    public String upload(Model model) {
        model.addAttribute("pageTitle", "上传智能体");
        return "agents/upload";
    }

    /**
     * 我的智能体页面
     */
    @GetMapping("/my-agents")
    public String myAgents(@RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "12") int size,
                           @RequestParam(required = false) Long userId,
                           Model model) {
        // 这里应该从session或token中获取当前用户ID
        // 暂时使用参数传递，实际项目中应该从认证信息中获取
        if (userId == null) {
            userId = 1L; // 默认用户ID，实际应该从认证中获取
        }
        
        PageResult<AgentVO> agents = agentService.getAgentsByCreator(userId, page, size);
        model.addAttribute("agents", agents);
        model.addAttribute("pageTitle", "我的智能体");
        
        return "agents/my-agents";
    }

    // ========== API接口 ==========

    /**
     * 创建智能体API
     */
    @PostMapping("/api/create")
    @ResponseBody
    public Result<AgentVO> createAgent(@Valid @RequestBody CreateAgentRequest request,
                                       @RequestParam Long userId) {
        try {
            AgentVO agent = agentService.createAgent(request, userId);
            return Result.success(agent);
        } catch (Exception e) {
            log.error("创建智能体失败", e);
            return Result.error("创建智能体失败: " + e.getMessage());
        }
    }

    /**
     * 生成游戏API
     */
    @PostMapping("/api/game-maker/generate")
    @ResponseBody
    public Result<GameGenerationResult> generateGame(@Valid @RequestBody GameGenerationRequest request,
                                                     @RequestParam Long userId) {
        try {
            GameGenerationResult result = agentService.generateGame(request, userId);
            return Result.success(result);
        } catch (Exception e) {
            log.error("生成游戏失败", e);
            return Result.error("生成游戏失败: " + e.getMessage());
        }
    }

    /**
     * 使用智能体API
     */
    @PostMapping("/api/{agentId}/use")
    @ResponseBody
    public Result<Boolean> useAgent(@PathVariable Long agentId,
                                    @RequestParam Long userId) {
        try {
            boolean success = agentService.useAgent(agentId, userId);
            return Result.success(success);
        } catch (Exception e) {
            log.error("使用智能体失败", e);
            return Result.error("使用智能体失败: " + e.getMessage());
        }
    }

    /**
     * 获取智能体列表API
     */
    @GetMapping("/api/list")
    @ResponseBody
    public Result<PageResult<AgentVO>> getAgents(@RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "12") int size,
                                                  @RequestParam(required = false) String type) {
        try {
            PageResult<AgentVO> agents;
            if (type != null && !type.isEmpty()) {
                agents = agentService.getAgentsByType(type, page, size);
            } else {
                agents = agentService.getActiveAgents(page, size);
            }
            return Result.success(agents);
        } catch (Exception e) {
            log.error("获取智能体列表失败", e);
            return Result.error("获取智能体列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取智能体详情API
     */
    @GetMapping("/api/{id}")
    @ResponseBody
    public Result<AgentVO> getAgentDetail(@PathVariable Long id) {
        try {
            AgentVO agent = agentService.getAgentDetail(id);
            return Result.success(agent);
        } catch (Exception e) {
            log.error("获取智能体详情失败", e);
            return Result.error("获取智能体详情失败: " + e.getMessage());
        }
    }

    /**
     * 获取热门智能体API
     */
    @GetMapping("/api/popular")
    @ResponseBody
    public Result<List<AgentVO>> getPopularAgents(@RequestParam(defaultValue = "6") int limit) {
        try {
            List<AgentVO> agents = agentService.getPopularAgents(limit);
            return Result.success(agents);
        } catch (Exception e) {
            log.error("获取热门智能体失败", e);
            return Result.error("获取热门智能体失败: " + e.getMessage());
        }
    }

    /**
     * 获取智能体统计信息API
     */
    @GetMapping("/api/statistics")
    @ResponseBody
    public Result<AgentStatistics> getStatistics() {
        try {
            AgentStatistics statistics = agentService.getAgentStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取智能体统计信息失败", e);
            return Result.error("获取智能体统计信息失败: " + e.getMessage());
        }
    }
}
