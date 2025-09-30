package com.decentralized.gaming.platform.service.blockchain;

import com.decentralized.gaming.platform.entity.BatchOperation;

import java.util.List;
import java.util.Optional;

/**
 * 批量操作服务接口
 *
 * @author DecentralizedGamingPlatform
 */
public interface BatchOperationService {

    /**
     * 创建批量操作
     *
     * @param operationType 操作类型
     * @param totalOperations 总操作数
     * @param operationDetails 操作详情
     * @param createdBy 创建者用户ID
     * @return 批量操作ID
     */
    String createBatchOperation(String operationType, Integer totalOperations, String operationDetails, Long createdBy);

    /**
     * 开始批量操作
     *
     * @param batchId 批量操作ID
     * @return 是否成功
     */
    boolean startBatchOperation(String batchId);

    /**
     * 更新批量操作进度
     *
     * @param batchId 批量操作ID
     * @param completedOperations 已完成操作数
     * @param failedOperations 失败操作数
     * @return 是否成功
     */
    boolean updateBatchProgress(String batchId, Integer completedOperations, Integer failedOperations);

    /**
     * 完成批量操作
     *
     * @param batchId 批量操作ID
     * @return 是否成功
     */
    boolean completeBatchOperation(String batchId);

    /**
     * 失败批量操作
     *
     * @param batchId 批量操作ID
     * @param errorMessage 错误信息
     * @return 是否成功
     */
    boolean failBatchOperation(String batchId, String errorMessage);

    /**
     * 取消批量操作
     *
     * @param batchId 批量操作ID
     * @return 是否成功
     */
    boolean cancelBatchOperation(String batchId);

    /**
     * 根据ID获取批量操作
     *
     * @param batchId 批量操作ID
     * @return 批量操作信息
     */
    Optional<BatchOperation> getBatchOperationById(String batchId);

    /**
     * 获取用户的批量操作列表
     *
     * @param userId 用户ID
     * @return 批量操作列表
     */
    List<BatchOperation> getBatchOperationsByUser(Long userId);

    /**
     * 获取所有批量操作列表
     *
     * @return 批量操作列表
     */
    List<BatchOperation> getAllBatchOperations();

    /**
     * 获取进行中的批量操作
     *
     * @return 进行中的批量操作列表
     */
    List<BatchOperation> getActiveBatchOperations();
}
