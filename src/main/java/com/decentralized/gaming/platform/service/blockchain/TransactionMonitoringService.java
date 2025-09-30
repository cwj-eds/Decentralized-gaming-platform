package com.decentralized.gaming.platform.service.blockchain;

import java.util.List;
import java.util.Map;

/**
 * 交易监控服务接口
 * 监控区块链交易状态
 *
 * @author DecentralizedGamingPlatform
 */
public interface TransactionMonitoringService {

    /**
     * 开始监控交易
     *
     * @param transactionHash 交易哈希
     * @param callback 回调函数
     */
    void startMonitoring(String transactionHash, MonitoringCallback callback);

    /**
     * 停止监控交易
     *
     * @param transactionHash 交易哈希
     */
    void stopMonitoring(String transactionHash);

    /**
     * 获取监控状态
     *
     * @param transactionHash 交易哈希
     * @return 监控状态
     */
    MonitoringStatus getMonitoringStatus(String transactionHash);

    /**
     * 获取所有监控状态
     *
     * @return 所有监控状态
     */
    Map<String, MonitoringStatus> getAllMonitoringStatus();

    /**
     * 关闭监控服务
     */
    void shutdown();

    /**
     * 监控任务接口
     */
    interface MonitoringTask {
        /**
         * 执行监控任务
         */
        void run();
    }

    /**
     * 监控回调接口
     */
    interface MonitoringCallback {
        /**
         * 交易确认回调
         *
         * @param transactionHash 交易哈希
         * @param blockNumber 区块号
         */
        void onConfirmed(String transactionHash, long blockNumber);

        /**
         * 交易失败回调
         *
         * @param transactionHash 交易哈希
         * @param error 错误信息
         */
        void onFailed(String transactionHash, String error);

        /**
         * 监控超时回调
         *
         * @param transactionHash 交易哈希
         */
        void onTimeout(String transactionHash);
    }

    /**
     * 监控状态枚举
     */
    enum MonitoringStatus {
        PENDING,
        CONFIRMED,
        FAILED,
        TIMEOUT
    }
}
