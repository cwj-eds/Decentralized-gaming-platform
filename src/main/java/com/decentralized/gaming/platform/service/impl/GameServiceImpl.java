//package com.decentralized.gaming.platform.service.impl;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.decentralized.gaming.platform.entity.Game;
//import com.decentralized.gaming.platform.mapper.GameMapper;
//import com.decentralized.gaming.platform.service.GameService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.UUID;
//
///**
// * 游戏服务实现类
// *
// * @author DecentralizedGamingPlatform
// */
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class GameServiceImpl extends ServiceImpl<GameMapper, Game> implements GameService {
//
//    @Override
//    public Game createGame(String title, String description, Long creatorId, String gameCode) {
//        Game game = new Game();
//        game.setTitle(title);
//        game.setDescription(description);
//        game.setCreatorId(creatorId);
//        game.setGameCode(gameCode);
//        game.setStatus(Game.GameStatus.DRAFT.getCode());
//        game.setPlayCount(0);
//        game.setCreatedAt(LocalDateTime.now());
//        game.setUpdatedAt(LocalDateTime.now());
//
//        baseMapper.insert(game);
//        log.info("游戏创建成功: {}", title);
//
//        return game;
//    }
//
//    @Override
//    public Game publishGame(Long gameId, Long userId) {
//        Game game = baseMapper.selectById(gameId);
//        if (game == null || !game.getCreatorId().equals(userId)) {
//            throw new RuntimeException("游戏不存在或无权限");
//        }
//
//        game.setStatus(Game.GameStatus.PUBLISHED.getCode());
//        game.setUpdatedAt(LocalDateTime.now());
//        baseMapper.updateById(game);
//
//        log.info("游戏发布成功: {}", game.getTitle());
//        return game;
//    }
//
//    @Override
//    public List<Game> getUserGames(Long userId) {
//        return baseMapper.selectList(
//            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Game>()
//                .eq("creator_id", userId)
//        );
//    }
//
//    @Override
//    public Page<Game> getPopularGames(int page, int size) {
//        Page<Game> pageParam = new Page<>(page, size);
//        return baseMapper.selectPage(pageParam,
//            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Game>()
//                .eq("status", Game.GameStatus.PUBLISHED.getCode())
//                .orderByDesc("play_count")
//        );
//    }
//
//    @Override
//    public String startGame(Long gameId, Long userId) {
//        Game game = baseMapper.selectById(gameId);
//        if (game == null) {
//            throw new RuntimeException("游戏不存在");
//        }
//
//        // 增加游戏次数
//        incrementPlayCount(gameId);
//
//        // 生成游戏会话令牌
//        String sessionToken = UUID.randomUUID().toString();
//
//        // TODO: 创建游戏会话记录
//
//        log.info("用户 {} 开始游戏: {}", userId, game.getTitle());
//        return sessionToken;
//    }
//
//    @Override
//    public void incrementPlayCount(Long gameId) {
//        Game game = baseMapper.selectById(gameId);
//        if (game != null) {
//            game.setPlayCount(game.getPlayCount() + 1);
//            game.setUpdatedAt(LocalDateTime.now());
//            baseMapper.updateById(game);
//        }
//    }
//}
