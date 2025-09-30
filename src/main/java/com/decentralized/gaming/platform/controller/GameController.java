package com.decentralized.gaming.platform.controller;

import com.decentralized.gaming.platform.common.PageResult;
import com.decentralized.gaming.platform.service.GameService;
import com.decentralized.gaming.platform.vo.GameVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/games")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GameController {

    private final GameService gameService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "12") int size,
                       Model model) {
        PageResult<GameVO> games = gameService.getPublishedGames(page, size);
        model.addAttribute("games", games);
        model.addAttribute("pageTitle", "游戏中心");
        return "games/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        GameVO game = gameService.getGameDetail(id);
        model.addAttribute("game", game);
        model.addAttribute("pageTitle", game.getTitle());
        return "games/detail";
    }

    @GetMapping("/{id}/play")
    public String play(@PathVariable Long id, Model model) {
        GameVO game = gameService.getGameDetail(id);
        model.addAttribute("game", game);
        model.addAttribute("pageTitle", game.getTitle() + " - 试玩");
        return "games/play";
    }
}



