package com.decentralized.gaming.platform.service.blockchain;

import com.decentralized.gaming.platform.entity.EventSubscription;

import java.util.List;
import java.util.Optional;

/**
 * 事件订阅服务接口
 *
 * @author DecentralizedGamingPlatform
 */
public interface EventSubscriptionService {

    /**
     * 创建事件订阅
     *
     * @param contractType 合约类型
     * @param contractAddress 合约地址
     * @param eventType 事件类型
     * @param eventSignature 事件签名
     * @return 订阅ID
     */
    String createSubscription(String contractType, String contractAddress, String eventType, String eventSignature);

    /**
     * 取消事件订阅
     *
     * @param subscriptionId 订阅ID
     * @return 是否成功
     */
    boolean cancelSubscription(String subscriptionId);

    /**
     * 暂停事件订阅
     *
     * @param subscriptionId 订阅ID
     * @return 是否成功
     */
    boolean pauseSubscription(String subscriptionId);

    /**
     * 恢复事件订阅
     *
     * @param subscriptionId 订阅ID
     * @return 是否成功
     */
    boolean resumeSubscription(String subscriptionId);

    /**
     * 根据ID获取订阅信息
     *
     * @param subscriptionId 订阅ID
     * @return 订阅信息
     */
    Optional<EventSubscription> getSubscriptionById(String subscriptionId);

    /**
     * 获取所有活跃的订阅
     *
     * @return 活跃订阅列表
     */
    List<EventSubscription> getActiveSubscriptions();

    /**
     * 根据合约类型获取订阅
     *
     * @param contractType 合约类型
     * @return 订阅列表
     */
    List<EventSubscription> getSubscriptionsByContractType(String contractType);

    /**
     * 更新订阅状态
     *
     * @param subscriptionId 订阅ID
     * @param status 新状态
     * @return 是否成功
     */
    boolean updateSubscriptionStatus(String subscriptionId, String status);

    /**
     * 更新事件计数
     *
     * @param subscriptionId 订阅ID
     * @return 是否成功
     */
    boolean updateEventCount(String subscriptionId);
}
