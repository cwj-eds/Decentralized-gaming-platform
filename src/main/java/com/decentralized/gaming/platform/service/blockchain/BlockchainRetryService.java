package com.decentralized.gaming.platform.service.blockchain;

import java.util.function.Supplier;

/**
 * 区块链重试服务接口
 * 提供区块链操作的重试机制和降级策略
 *
 * @author DecentralizedGamingPlatform
 */
public interface BlockchainRetryService {

    /**
     * 执行带重试的区块链操作
     *
     * @param operation 操作函数
     * @param operationName 操作名称（用于日志）
     * @return 操作结果
     */
    <T> T executeWithRetry(Supplier<T> operation, String operationName);

    /**
     * 执行带重试的区块链操作（无返回值）
     *
     * @param operation 操作函数
     * @param operationName 操作名称（用于日志）
     */
    void executeWithRetry(Runnable operation, String operationName);

    /**
     * 执行带降级策略的操作
     *
     * @param primaryOperation 主要操作
     * @param fallbackOperation 降级操作
     * @param operationName 操作名称
     * @return 操作结果
     */
    <T> T executeWithFallback(Supplier<T> primaryOperation, 
                             Supplier<T> fallbackOperation, 
                             String operationName);
}
