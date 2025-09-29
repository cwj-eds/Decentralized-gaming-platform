package com.decentralized.gaming.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.decentralized.gaming.platform.common.PageResult;
import com.decentralized.gaming.platform.entity.Game;
import com.decentralized.gaming.platform.exception.BusinessException;
import com.decentralized.gaming.platform.mapper.GameMapper;
import com.decentralized.gaming.platform.service.GameService;
import com.decentralized.gaming.platform.vo.GameVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GameServiceImpl implements GameService {

    private final GameMapper gameMapper;

    @Override
    public PageResult<GameVO> getPublishedGames(int page, int size) {
        Page<Game> p = new Page<>(page, size);
        LambdaQueryWrapper<Game> qw = new LambdaQueryWrapper<>();
        qw.eq(Game::getStatus, Game.GameStatus.PUBLISHED)
          .orderByDesc(Game::getCreatedAt);
        gameMapper.selectPage(p, qw);
        List<GameVO> list = p.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return PageResult.of(list, p.getTotal(), p.getCurrent(), p.getSize());
    }

    @Override
    public GameVO getGameDetail(Long gameId) {
        Game game = gameMapper.selectById(gameId);
        if (game == null || game.getStatus() == null) {
            throw new BusinessException("游戏不存在");
        }
        return toVO(game);
    }

    @Override
    public Long countPublishedGames() {
        return gameMapper.selectCount(new LambdaQueryWrapper<Game>()
                .eq(Game::getStatus, Game.GameStatus.PUBLISHED));
    }

    private GameVO toVO(Game game) {
        GameVO vo = new GameVO();
        BeanUtils.copyProperties(game, vo);
        return vo;
    }
}


