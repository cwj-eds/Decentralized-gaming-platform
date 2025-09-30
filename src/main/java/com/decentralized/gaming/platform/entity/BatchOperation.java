package com.decentralized.gaming.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 批量操作实体
 *
 * @author DecentralizedGamingPlatform
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("batch_operations")
public class BatchOperation {

    /**
     * 批量操作ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String batchId;

    /**
     * 操作类型：NFT_TRANSFER, NFT_APPROVE, TOKEN_TRANSFER等
     */
    private String operationType;

    /**
     * 操作状态：PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED
     */
    private String status;

    /**
     * 总操作数
     */
    private Integer totalOperations;

    /**
     * 已完成操作数
     */
    private Integer completedOperations;

    /**
     * 失败操作数
     */
    private Integer failedOperations;

    /**
     * 进度百分比 (0-100)
     */
    private Integer progress;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 操作详情（JSON格式）
     */
    private String operationDetails;

    /**
     * 创建者用户ID
     */
    private Long createdBy;
}
