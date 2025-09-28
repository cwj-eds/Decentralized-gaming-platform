package com.decentralized.gaming.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.decentralized.gaming.platform.entity.Game;

import java.util.List;

/**
 * 游戏服务接口
 *
 * @author DecentralizedGamingPlatform
 */
public interface GameService extends IService<Game> {

    /**
     * 创建游戏
     * 
     * @param title 游戏标题
     * @param description 游戏描述
     * @param creatorId 创建者ID
     * @param gameCode 游戏代码
     * @return 游戏信息
     */
    Game createGame(String title, String description, Long creatorId, String gameCode);

    /**
     * 发布游戏
     * 
     * @param gameId 游戏ID
     * @param userId 用户ID
     * @return 游戏信息
     */
    Game publishGame(Long gameId, Long userId);

    /**
     * 获取用户游戏列表
     * 
     * @param userId 用户ID
     * @return 游戏列表
     */
    List<Game> getUserGames(Long userId);

    /**
     * 获取热门游戏
     * 
     * @param page 页码
     * @param size 页面大小
     * @return 游戏分页列表
     */
    Page<Game> getPopularGames(int page, int size);

    /**
     * 开始游戏
     * 
     * @param gameId 游戏ID
     * @param userId 用户ID
     * @return 游戏会话令牌
     */
    String startGame(Long gameId, Long userId);

    /**
     * 增加游戏播放次数
     * 
     * @param gameId 游戏ID
     */
    void incrementPlayCount(Long gameId);
}
