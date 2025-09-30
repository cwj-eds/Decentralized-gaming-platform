package com.decentralized.gaming.platform.service.blockchain.impl;

import com.decentralized.gaming.platform.exception.BlockchainException;
import com.decentralized.gaming.platform.service.blockchain.BlockchainRetryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

/**
 * 区块链重试服务实现类
 * 提供区块链操作的重试机制和降级策略
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
public class BlockchainRetryServiceImpl implements BlockchainRetryService {

    @Value("${app.blockchain.retry.max-attempts:3}")
    private int maxAttempts;

    @Value("${app.blockchain.retry.delay-ms:1000}")
    private long delayMs;

    @Value("${app.blockchain.retry.backoff-multiplier:2}")
    private double backoffMultiplier;

    /**
     * 执行带重试的区块链操作
     *
     * @param operation 操作函数
     * @param operationName 操作名称（用于日志）
     * @return 操作结果
     */
    @Override
    public <T> T executeWithRetry(Supplier<T> operation, String operationName) {
        Exception lastException = null;
        
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                log.debug("执行区块链操作: {}, 尝试次数: {}/{}", operationName, attempt, maxAttempts);
                T result = operation.get();
                log.debug("区块链操作成功: {}, 尝试次数: {}", operationName, attempt);
                return result;
            } catch (Exception e) {
                lastException = e;
                log.warn("区块链操作失败: {}, 尝试次数: {}/{}, 错误: {}", 
                        operationName, attempt, maxAttempts, e.getMessage());
                
                if (attempt < maxAttempts) {
                    long delay = (long) (delayMs * Math.pow(backoffMultiplier, attempt - 1));
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new BlockchainException(
                            BlockchainException.ErrorCodes.TIMEOUT,
                            operationName,
                            "重试被中断"
                        );
                    }
                }
            }
        }
        
        log.error("区块链操作最终失败: {}, 已尝试 {} 次", operationName, maxAttempts);
        throw new BlockchainException(
            BlockchainException.ErrorCodes.NETWORK_ERROR,
            operationName,
            "区块链操作失败，已重试" + maxAttempts + "次",
            lastException
        );
    }

    /**
     * 执行带重试的区块链操作（无返回值）
     *
     * @param operation 操作函数
     * @param operationName 操作名称（用于日志）
     */
    @Override
    public void executeWithRetry(Runnable operation, String operationName) {
        executeWithRetry(() -> {
            operation.run();
            return null;
        }, operationName);
    }

    /**
     * 执行带降级策略的操作
     *
     * @param primaryOperation 主要操作
     * @param fallbackOperation 降级操作
     * @param operationName 操作名称
     * @return 操作结果
     */
    @Override
    public <T> T executeWithFallback(Supplier<T> primaryOperation, 
                                   Supplier<T> fallbackOperation, 
                                   String operationName) {
        try {
            return executeWithRetry(primaryOperation, operationName);
        } catch (BlockchainException e) {
            log.warn("主要操作失败，尝试降级操作: {}", operationName);
            try {
                return fallbackOperation.get();
            } catch (Exception fallbackException) {
                log.error("降级操作也失败: {}", operationName, fallbackException);
                throw new BlockchainException(
                    BlockchainException.ErrorCodes.NETWORK_ERROR,
                    operationName,
                    "主要操作和降级操作都失败",
                    fallbackException
                );
            }
        }
    }
}
