package com.decentralized.gaming.platform.service.blockchain.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.decentralized.gaming.platform.entity.EventSubscription;
import com.decentralized.gaming.platform.mapper.EventSubscriptionMapper;
import com.decentralized.gaming.platform.service.blockchain.EventSubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 事件订阅服务实现类
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
public class EventSubscriptionServiceImpl implements EventSubscriptionService {

    @Autowired
    private EventSubscriptionMapper subscriptionMapper;

    @Override
    @Transactional
    public String createSubscription(String contractType, String contractAddress, String eventType, String eventSignature) {
        try {
            // 生成订阅ID
            String subscriptionId = UUID.randomUUID().toString().replace("-", "");

            // 创建订阅记录
            EventSubscription subscription = new EventSubscription();
            subscription.setSubscriptionId(subscriptionId);
            subscription.setContractType(contractType);
            subscription.setContractAddress(contractAddress);
            subscription.setEventType(eventType);
            subscription.setEventSignature(eventSignature);
            subscription.setStatus("ACTIVE");
            subscription.setCreatedAt(LocalDateTime.now());
            subscription.setUpdatedAt(LocalDateTime.now());
            subscription.setEventCount(0L);

            // 保存到数据库
            int result = subscriptionMapper.insert(subscription);
            if (result > 0) {
                log.info("创建事件订阅成功，订阅ID: {}, 合约类型: {}, 事件类型: {}", 
                        subscriptionId, contractType, eventType);
                return subscriptionId;
            } else {
                log.error("创建事件订阅失败，订阅ID: {}", subscriptionId);
                return null;
            }
        } catch (Exception e) {
            log.error("创建事件订阅异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public boolean cancelSubscription(String subscriptionId) {
        try {
            LambdaUpdateWrapper<EventSubscription> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(EventSubscription::getSubscriptionId, subscriptionId)
                        .set(EventSubscription::getStatus, "CANCELLED")
                        .set(EventSubscription::getUpdatedAt, LocalDateTime.now());

            int result = subscriptionMapper.update(null, updateWrapper);
            if (result > 0) {
                log.info("取消事件订阅成功，订阅ID: {}", subscriptionId);
                return true;
            } else {
                log.warn("取消事件订阅失败，订阅ID: {}", subscriptionId);
                return false;
            }
        } catch (Exception e) {
            log.error("取消事件订阅异常，订阅ID: {}", subscriptionId, e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean pauseSubscription(String subscriptionId) {
        try {
            LambdaUpdateWrapper<EventSubscription> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(EventSubscription::getSubscriptionId, subscriptionId)
                        .set(EventSubscription::getStatus, "PAUSED")
                        .set(EventSubscription::getUpdatedAt, LocalDateTime.now());

            int result = subscriptionMapper.update(null, updateWrapper);
            if (result > 0) {
                log.info("暂停事件订阅成功，订阅ID: {}", subscriptionId);
                return true;
            } else {
                log.warn("暂停事件订阅失败，订阅ID: {}", subscriptionId);
                return false;
            }
        } catch (Exception e) {
            log.error("暂停事件订阅异常，订阅ID: {}", subscriptionId, e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean resumeSubscription(String subscriptionId) {
        try {
            LambdaUpdateWrapper<EventSubscription> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(EventSubscription::getSubscriptionId, subscriptionId)
                        .set(EventSubscription::getStatus, "ACTIVE")
                        .set(EventSubscription::getUpdatedAt, LocalDateTime.now());

            int result = subscriptionMapper.update(null, updateWrapper);
            if (result > 0) {
                log.info("恢复事件订阅成功，订阅ID: {}", subscriptionId);
                return true;
            } else {
                log.warn("恢复事件订阅失败，订阅ID: {}", subscriptionId);
                return false;
            }
        } catch (Exception e) {
            log.error("恢复事件订阅异常，订阅ID: {}", subscriptionId, e);
            return false;
        }
    }

    @Override
    public Optional<EventSubscription> getSubscriptionById(String subscriptionId) {
        try {
            LambdaQueryWrapper<EventSubscription> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(EventSubscription::getSubscriptionId, subscriptionId);
            
            EventSubscription subscription = subscriptionMapper.selectOne(queryWrapper);
            return Optional.ofNullable(subscription);
        } catch (Exception e) {
            log.error("获取订阅信息异常，订阅ID: {}", subscriptionId, e);
            return Optional.empty();
        }
    }

    @Override
    public List<EventSubscription> getActiveSubscriptions() {
        try {
            LambdaQueryWrapper<EventSubscription> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(EventSubscription::getStatus, "ACTIVE")
                       .orderByDesc(EventSubscription::getCreatedAt);
            
            return subscriptionMapper.selectList(queryWrapper);
        } catch (Exception e) {
            log.error("获取活跃订阅列表异常", e);
            return List.of();
        }
    }

    @Override
    public List<EventSubscription> getSubscriptionsByContractType(String contractType) {
        try {
            LambdaQueryWrapper<EventSubscription> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(EventSubscription::getContractType, contractType)
                       .orderByDesc(EventSubscription::getCreatedAt);
            
            return subscriptionMapper.selectList(queryWrapper);
        } catch (Exception e) {
            log.error("根据合约类型获取订阅列表异常，合约类型: {}", contractType, e);
            return List.of();
        }
    }

    @Override
    @Transactional
    public boolean updateSubscriptionStatus(String subscriptionId, String status) {
        try {
            LambdaUpdateWrapper<EventSubscription> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(EventSubscription::getSubscriptionId, subscriptionId)
                        .set(EventSubscription::getStatus, status)
                        .set(EventSubscription::getUpdatedAt, LocalDateTime.now());

            int result = subscriptionMapper.update(null, updateWrapper);
            return result > 0;
        } catch (Exception e) {
            log.error("更新订阅状态异常，订阅ID: {}, 状态: {}", subscriptionId, status, e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean updateEventCount(String subscriptionId) {
        try {
            LambdaUpdateWrapper<EventSubscription> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(EventSubscription::getSubscriptionId, subscriptionId)
                        .setSql("event_count = event_count + 1")
                        .set(EventSubscription::getLastEventTime, LocalDateTime.now())
                        .set(EventSubscription::getUpdatedAt, LocalDateTime.now());

            int result = subscriptionMapper.update(null, updateWrapper);
            return result > 0;
        } catch (Exception e) {
            log.error("更新事件计数异常，订阅ID: {}", subscriptionId, e);
            return false;
        }
    }
}
