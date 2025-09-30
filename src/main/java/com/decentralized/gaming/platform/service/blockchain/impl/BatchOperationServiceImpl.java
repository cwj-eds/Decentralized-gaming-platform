package com.decentralized.gaming.platform.service.blockchain.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.decentralized.gaming.platform.entity.BatchOperation;
import com.decentralized.gaming.platform.mapper.BatchOperationMapper;
import com.decentralized.gaming.platform.service.blockchain.BatchOperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 批量操作服务实现类
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
public class BatchOperationServiceImpl implements BatchOperationService {

    @Autowired
    private BatchOperationMapper batchOperationMapper;

    @Override
    @Transactional
    public String createBatchOperation(String operationType, Integer totalOperations, String operationDetails, Long createdBy) {
        try {
            // 生成批量操作ID
            String batchId = "batch_" + UUID.randomUUID().toString().replace("-", "");

            // 创建批量操作记录
            BatchOperation batchOperation = new BatchOperation();
            batchOperation.setBatchId(batchId);
            batchOperation.setOperationType(operationType);
            batchOperation.setStatus("PENDING");
            batchOperation.setTotalOperations(totalOperations);
            batchOperation.setCompletedOperations(0);
            batchOperation.setFailedOperations(0);
            batchOperation.setProgress(0);
            batchOperation.setCreatedAt(LocalDateTime.now());
            batchOperation.setUpdatedAt(LocalDateTime.now());
            batchOperation.setOperationDetails(operationDetails);
            batchOperation.setCreatedBy(createdBy);

            // 保存到数据库
            int result = batchOperationMapper.insert(batchOperation);
            if (result > 0) {
                log.info("创建批量操作成功，批量操作ID: {}, 操作类型: {}, 总操作数: {}", 
                        batchId, operationType, totalOperations);
                return batchId;
            } else {
                log.error("创建批量操作失败，批量操作ID: {}", batchId);
                return null;
            }
        } catch (Exception e) {
            log.error("创建批量操作异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public boolean startBatchOperation(String batchId) {
        try {
            LambdaUpdateWrapper<BatchOperation> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(BatchOperation::getBatchId, batchId)
                        .set(BatchOperation::getStatus, "PROCESSING")
                        .set(BatchOperation::getStartTime, LocalDateTime.now())
                        .set(BatchOperation::getUpdatedAt, LocalDateTime.now());

            int result = batchOperationMapper.update(null, updateWrapper);
            if (result > 0) {
                log.info("开始批量操作成功，批量操作ID: {}", batchId);
                return true;
            } else {
                log.warn("开始批量操作失败，批量操作ID: {}", batchId);
                return false;
            }
        } catch (Exception e) {
            log.error("开始批量操作异常，批量操作ID: {}", batchId, e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean updateBatchProgress(String batchId, Integer completedOperations, Integer failedOperations) {
        try {
            // 获取当前批量操作信息
            Optional<BatchOperation> optional = getBatchOperationById(batchId);
            if (!optional.isPresent()) {
                log.warn("批量操作不存在，批量操作ID: {}", batchId);
                return false;
            }

            BatchOperation batchOperation = optional.get();
            Integer totalOperations = batchOperation.getTotalOperations();
            
            // 计算进度
            int progress = (int) Math.round((double) (completedOperations + failedOperations) / totalOperations * 100);
            progress = Math.min(progress, 100); // 确保不超过100%

            LambdaUpdateWrapper<BatchOperation> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(BatchOperation::getBatchId, batchId)
                        .set(BatchOperation::getCompletedOperations, completedOperations)
                        .set(BatchOperation::getFailedOperations, failedOperations)
                        .set(BatchOperation::getProgress, progress)
                        .set(BatchOperation::getUpdatedAt, LocalDateTime.now());

            int result = batchOperationMapper.update(null, updateWrapper);
            if (result > 0) {
                log.debug("更新批量操作进度成功，批量操作ID: {}, 进度: {}%", batchId, progress);
                return true;
            } else {
                log.warn("更新批量操作进度失败，批量操作ID: {}", batchId);
                return false;
            }
        } catch (Exception e) {
            log.error("更新批量操作进度异常，批量操作ID: {}", batchId, e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean completeBatchOperation(String batchId) {
        try {
            LambdaUpdateWrapper<BatchOperation> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(BatchOperation::getBatchId, batchId)
                        .set(BatchOperation::getStatus, "COMPLETED")
                        .set(BatchOperation::getEndTime, LocalDateTime.now())
                        .set(BatchOperation::getProgress, 100)
                        .set(BatchOperation::getUpdatedAt, LocalDateTime.now());

            int result = batchOperationMapper.update(null, updateWrapper);
            if (result > 0) {
                log.info("完成批量操作成功，批量操作ID: {}", batchId);
                return true;
            } else {
                log.warn("完成批量操作失败，批量操作ID: {}", batchId);
                return false;
            }
        } catch (Exception e) {
            log.error("完成批量操作异常，批量操作ID: {}", batchId, e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean failBatchOperation(String batchId, String errorMessage) {
        try {
            LambdaUpdateWrapper<BatchOperation> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(BatchOperation::getBatchId, batchId)
                        .set(BatchOperation::getStatus, "FAILED")
                        .set(BatchOperation::getEndTime, LocalDateTime.now())
                        .set(BatchOperation::getErrorMessage, errorMessage)
                        .set(BatchOperation::getUpdatedAt, LocalDateTime.now());

            int result = batchOperationMapper.update(null, updateWrapper);
            if (result > 0) {
                log.info("批量操作失败，批量操作ID: {}, 错误信息: {}", batchId, errorMessage);
                return true;
            } else {
                log.warn("设置批量操作失败状态失败，批量操作ID: {}", batchId);
                return false;
            }
        } catch (Exception e) {
            log.error("设置批量操作失败状态异常，批量操作ID: {}", batchId, e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean cancelBatchOperation(String batchId) {
        try {
            LambdaUpdateWrapper<BatchOperation> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(BatchOperation::getBatchId, batchId)
                        .set(BatchOperation::getStatus, "CANCELLED")
                        .set(BatchOperation::getEndTime, LocalDateTime.now())
                        .set(BatchOperation::getUpdatedAt, LocalDateTime.now());

            int result = batchOperationMapper.update(null, updateWrapper);
            if (result > 0) {
                log.info("取消批量操作成功，批量操作ID: {}", batchId);
                return true;
            } else {
                log.warn("取消批量操作失败，批量操作ID: {}", batchId);
                return false;
            }
        } catch (Exception e) {
            log.error("取消批量操作异常，批量操作ID: {}", batchId, e);
            return false;
        }
    }

    @Override
    public Optional<BatchOperation> getBatchOperationById(String batchId) {
        try {
            LambdaQueryWrapper<BatchOperation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BatchOperation::getBatchId, batchId);
            
            BatchOperation batchOperation = batchOperationMapper.selectOne(queryWrapper);
            return Optional.ofNullable(batchOperation);
        } catch (Exception e) {
            log.error("获取批量操作信息异常，批量操作ID: {}", batchId, e);
            return Optional.empty();
        }
    }

    @Override
    public List<BatchOperation> getBatchOperationsByUser(Long userId) {
        try {
            LambdaQueryWrapper<BatchOperation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BatchOperation::getCreatedBy, userId)
                       .orderByDesc(BatchOperation::getCreatedAt);
            
            return batchOperationMapper.selectList(queryWrapper);
        } catch (Exception e) {
            log.error("获取用户批量操作列表异常，用户ID: {}", userId, e);
            return List.of();
        }
    }

    @Override
    public List<BatchOperation> getAllBatchOperations() {
        try {
            LambdaQueryWrapper<BatchOperation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.orderByDesc(BatchOperation::getCreatedAt);
            
            return batchOperationMapper.selectList(queryWrapper);
        } catch (Exception e) {
            log.error("获取所有批量操作列表异常", e);
            return List.of();
        }
    }

    @Override
    public List<BatchOperation> getActiveBatchOperations() {
        try {
            LambdaQueryWrapper<BatchOperation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(BatchOperation::getStatus, "PENDING", "PROCESSING")
                       .orderByDesc(BatchOperation::getCreatedAt);
            
            return batchOperationMapper.selectList(queryWrapper);
        } catch (Exception e) {
            log.error("获取活跃批量操作列表异常", e);
            return List.of();
        }
    }
}
