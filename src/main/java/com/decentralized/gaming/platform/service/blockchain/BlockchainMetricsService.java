package com.decentralized.gaming.platform.service.blockchain;

import java.math.BigInteger;
import java.util.Map;

/**
 * 区块链指标服务接口
 * 收集和管理区块链相关的性能指标
 *
 * @author DecentralizedGamingPlatform
 */
public interface BlockchainMetricsService {

    /**
     * 记录API请求
     *
     * @param endpoint 端点
     * @param success 是否成功
     */
    void recordApiRequest(String endpoint, boolean success);

    /**
     * 记录API错误
     *
     * @param endpoint 端点
     * @param errorType 错误类型
     */
    void recordApiError(String endpoint, String errorType);

    /**
     * 记录响应时间
     *
     * @param endpoint 端点
     * @param responseTime 响应时间(毫秒)
     */
    void recordResponseTime(String endpoint, long responseTime);

    /**
     * 记录Gas使用量
     *
     * @param operation 操作类型
     * @param gasUsed Gas使用量
     */
    void recordGasUsage(String operation, BigInteger gasUsed);

    /**
     * 获取指标数据
     *
     * @return 指标数据映射
     */
    Map<String, Object> getMetrics();

    /**
     * 计算平均响应时间
     *
     * @return 平均响应时间映射
     */
    Map<String, Double> calculateAverageResponseTimes();

    /**
     * 计算平均Gas使用量
     *
     * @return 平均Gas使用量映射
     */
    Map<String, Double> calculateAverageGasUsage();

    /**
     * 重置指标数据
     */
    void resetMetrics();

    /**
     * 获取健康状态
     *
     * @return 健康状态映射
     */
    Map<String, Object> getHealthStatus();
}
