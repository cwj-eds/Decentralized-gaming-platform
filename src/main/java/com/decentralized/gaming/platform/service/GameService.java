package com.decentralized.gaming.platform.service;

import com.decentralized.gaming.platform.common.PageResult;
import com.decentralized.gaming.platform.vo.GameVO;

public interface GameService {
    PageResult<GameVO> getPublishedGames(int page, int size);
    GameVO getGameDetail(Long gameId);
    Long countPublishedGames();
}



