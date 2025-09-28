//package com.decentralized.gaming.platform.controller;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.decentralized.gaming.platform.common.PageResult;
//import com.decentralized.gaming.platform.common.Result;
//import com.decentralized.gaming.platform.entity.Game;
//import com.decentralized.gaming.platform.service.GameService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * 游戏控制器
// *
// * @author DecentralizedGamingPlatform
// */
//@Tag(name = "游戏管理", description = "游戏相关接口")
//@RestController
//@RequestMapping("/api/games")
//@RequiredArgsConstructor
//public class GameController {
//
//    private final GameService gameService;
//
//    @Operation(summary = "获取所有游戏")
//    @GetMapping
//    public Result<PageResult<Game>> getGames(@RequestParam(defaultValue = "1") int page,
//                                           @RequestParam(defaultValue = "10") int size) {
//        Page<Game> gamePage = gameService.getPopularGames(page, size);
//        PageResult<Game> pageResult = PageResult.of(gamePage.getRecords(),
//                                                   gamePage.getTotal(),
//                                                   gamePage.getCurrent(),
//                                                   gamePage.getSize());
//        return Result.success(pageResult);
//    }
//
//    @Operation(summary = "创建游戏")
//    @PostMapping
//    public Result<Game> createGame(@RequestBody CreateGameRequest request) {
//        Game game = gameService.createGame(request.getTitle(),
//                                         request.getDescription(),
//                                         request.getCreatorId(),
//                                         request.getGameCode());
//        return Result.success(game);
//    }
//
//    @Operation(summary = "发布游戏")
//    @PostMapping("/{gameId}/publish")
//    public Result<Void> publishGame(@PathVariable Long gameId,
//                                   @RequestParam Long userId) {
//        gameService.publishGame(gameId, userId);
//        return Result.success();
//    }
//
//    @Operation(summary = "开始游戏")
//    @PostMapping("/{gameId}/play")
//    public Result<String> startGame(@PathVariable Long gameId,
//                                   @RequestParam Long userId) {
//        String sessionToken = gameService.startGame(gameId, userId);
//        return Result.success(sessionToken);
//    }
//
//    @Operation(summary = "获取用户游戏")
//    @GetMapping("/user/{userId}")
//    public Result<List<Game>> getUserGames(@PathVariable Long userId) {
//        List<Game> games = gameService.getUserGames(userId);
//        return Result.success(games);
//    }
//
//    /**
//     * 创建游戏请求对象
//     */
//    public static class CreateGameRequest {
//        private String title;
//        private String description;
//        private Long creatorId;
//        private String gameCode;
//
//        // getters and setters
//        public String getTitle() {
//            return title;
//        }
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public String getDescription() {
//            return description;
//        }
//
//        public void setDescription(String description) {
//            this.description = description;
//        }
//
//        public Long getCreatorId() {
//            return creatorId;
//        }
//
//        public void setCreatorId(Long creatorId) {
//            this.creatorId = creatorId;
//        }
//
//        public String getGameCode() {
//            return gameCode;
//        }
//
//        public void setGameCode(String gameCode) {
//            this.gameCode = gameCode;
//        }
//    }
//}
