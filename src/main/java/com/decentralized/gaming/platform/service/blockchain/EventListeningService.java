package com.decentralized.gaming.platform.service.blockchain;

import org.web3j.protocol.core.methods.response.Log;

import java.util.List;
import java.util.Map;

/**
 * 事件监听服务接口
 * 负责监听区块链事件并处理
 *
 * @author DecentralizedGamingPlatform
 */
public interface EventListeningService {

    /**
     * 初始化事件监听服务
     */
    void init();

    /**
     * 销毁事件监听服务
     */
    void destroy();

    /**
     * 开始监听事件
     */
    void startListening();

    /**
     * 停止监听事件
     */
    void stopListening();

    /**
     * 监听平台代币事件
     */
    void listenToPlatformTokenEvents();

    /**
     * 监听游戏NFT事件
     */
    void listenToGameNFTEvents();

    /**
     * 监听代理NFT事件
     */
    void listenToAgentNFTEvents();

    /**
     * 监听市场事件
     */
    void listenToMarketplaceEvents();

    /**
     * 监听奖励事件
     */
    void listenToRewardsEvents();

    /**
     * 处理转账事件
     *
     * @param log 事件日志
     */
    void handleTransferEvent(Log log);

    /**
     * 处理授权事件
     *
     * @param log 事件日志
     */
    void handleApprovalEvent(Log log);

    /**
     * 处理游戏创建事件
     *
     * @param log 事件日志
     */
    void handleGameCreatedEvent(Log log);

    /**
     * 处理代理创建事件
     *
     * @param log 事件日志
     */
    void handleAgentCreatedEvent(Log log);

    /**
     * 处理物品上架事件
     *
     * @param log 事件日志
     */
    void handleItemListedEvent(Log log);

    /**
     * 处理物品售出事件
     *
     * @param log 事件日志
     */
    void handleItemSoldEvent(Log log);

    /**
     * 处理奖励发放事件
     *
     * @param log 事件日志
     */
    void handleRewardIssuedEvent(Log log);

    /**
     * 检查是否正在监听
     *
     * @return 是否正在监听
     */
    boolean isListening();

    /**
     * 获取历史事件
     *
     * @param fromBlock 起始区块
     * @param toBlock 结束区块
     * @return 历史事件列表
     */
    List<Log> getHistoricalEvents(String fromBlock, String toBlock);

    /**
     * 获取地址历史事件
     *
     * @param address 地址
     * @param fromBlock 起始区块
     * @param toBlock 结束区块
     * @return 历史事件列表
     */
    List<Log> getAddressHistoricalEvents(String address, String fromBlock, String toBlock);

    /**
     * 获取多合约历史事件
     *
     * @param contractAddresses 合约地址列表
     * @param fromBlock 起始区块
     * @param toBlock 结束区块
     * @return 历史事件列表
     */
    List<Log> getMultiContractHistoricalEvents(List<String> contractAddresses, String fromBlock, String toBlock);

    /**
     * 获取事件统计信息
     *
     * @return 事件统计信息
     */
    Map<String, Object> getEventStatistics();

    /**
     * 开始监听特定合约事件
     *
     * @param contractAddress 合约地址
     * @param eventName 事件名称
     */
    void startContractListening(String contractAddress, String eventName);

    /**
     * 处理自定义事件
     *
     * @param log 事件日志
     * @param eventType 事件类型
     */
    void handleCustomEvent(Log log, String eventType);

    /**
     * 获取监听状态详情
     *
     * @return 监听状态详情
     */
    Map<String, Object> getListeningStatusDetails();

    /**
     * 重启监听
     */
    void restartListening();
}
