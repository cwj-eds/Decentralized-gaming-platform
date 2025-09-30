package com.decentralized.gaming.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 事件订阅实体
 *
 * @author DecentralizedGamingPlatform
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("event_subscriptions")
public class EventSubscription {

    /**
     * 订阅ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String subscriptionId;

    /**
     * 合约类型
     */
    private String contractType;

    /**
     * 合约地址
     */
    private String contractAddress;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 事件签名
     */
    private String eventSignature;

    /**
     * 订阅状态：ACTIVE, PAUSED, CANCELLED
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 最后事件时间
     */
    private LocalDateTime lastEventTime;

    /**
     * 事件计数
     */
    private Long eventCount;
}
