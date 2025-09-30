package com.decentralized.gaming.platform.service.blockchain.impl;

import com.decentralized.gaming.platform.service.blockchain.BlockchainMetricsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 区块链指标服务实现类
 * 收集和统计区块链相关的性能指标
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
public class BlockchainMetricsServiceImpl implements BlockchainMetricsService {

    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong totalErrors = new AtomicLong(0);
    private final Map<String, AtomicLong> requestCounts = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> errorCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> responseTimes = new ConcurrentHashMap<>();
    private final Map<String, Long> gasUsage = new ConcurrentHashMap<>();

    @Override
    public void recordApiRequest(String endpoint, boolean success) {
        totalRequests.incrementAndGet();
        requestCounts.computeIfAbsent(endpoint, k -> new AtomicLong(0)).incrementAndGet();
        if (!success) {
            totalErrors.incrementAndGet();
        }
        log.debug("记录API请求: {}, 成功: {}", endpoint, success);
    }

    @Override
    public void recordApiError(String endpoint, String errorType) {
        totalErrors.incrementAndGet();
        String errorKey = endpoint + ":" + errorType;
        errorCounts.computeIfAbsent(errorKey, k -> new AtomicLong(0)).incrementAndGet();
        log.debug("记录API错误: {} - {}", endpoint, errorType);
    }

    @Override
    public void recordResponseTime(String endpoint, long responseTimeMs) {
        responseTimes.put(endpoint + ":" + System.currentTimeMillis(), responseTimeMs);
        log.debug("记录响应时间: {} - {}ms", endpoint, responseTimeMs);
    }

    @Override
    public void recordGasUsage(String operation, BigInteger gasUsed) {
        gasUsage.put(operation + ":" + System.currentTimeMillis(), gasUsed.longValue());
        log.debug("记录Gas使用: {} - {}", operation, gasUsed);
    }

    @Override
    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        try {
            // 基本统计
            long requests = totalRequests.get();
            long errors = totalErrors.get();
            metrics.put("totalRequests", requests);
            metrics.put("totalErrors", errors);
            
            // 计算错误率
            double errorRate = requests > 0 ? (double) errors / requests * 100 : 0;
            metrics.put("errorRate", String.format("%.2f%%", errorRate));
            
            // 请求统计
            Map<String, Long> endpointRequests = new HashMap<>();
            requestCounts.forEach((endpoint, count) -> 
                endpointRequests.put(endpoint, count.get()));
            metrics.put("endpointRequests", endpointRequests);
            
            // 错误统计
            Map<String, Long> endpointErrors = new HashMap<>();
            errorCounts.forEach((errorKey, count) -> 
                endpointErrors.put(errorKey, count.get()));
            metrics.put("endpointErrors", endpointErrors);
            
            // 平均响应时间
            Map<String, Double> avgResponseTimes = calculateAverageResponseTimes();
            metrics.put("averageResponseTimes", avgResponseTimes);
            
            // 平均Gas使用量
            Map<String, Double> avgGasUsage = calculateAverageGasUsage();
            metrics.put("averageGasUsage", avgGasUsage);
            
            metrics.put("timestamp", LocalDateTime.now());
            
        } catch (Exception e) {
            log.error("获取指标统计失败", e);
            metrics.put("error", "获取指标失败: " + e.getMessage());
        }
        
        return metrics;
    }

    @Override
    public Map<String, Double> calculateAverageResponseTimes() {
        Map<String, Double> averages = new HashMap<>();
        Map<String, Long> endpointTotals = new HashMap<>();
        Map<String, Integer> endpointCounts = new HashMap<>();
        
        responseTimes.forEach((key, time) -> {
            String endpoint = key.split(":")[0];
            endpointTotals.merge(endpoint, time, Long::sum);
            endpointCounts.merge(endpoint, 1, Integer::sum);
        });
        
        endpointTotals.forEach((endpoint, total) -> {
            int count = endpointCounts.get(endpoint);
            averages.put(endpoint, (double) total / count);
        });
        
        return averages;
    }

    @Override
    public Map<String, Double> calculateAverageGasUsage() {
        Map<String, Double> averages = new HashMap<>();
        Map<String, Long> operationTotals = new HashMap<>();
        Map<String, Integer> operationCounts = new HashMap<>();
        
        gasUsage.forEach((key, gas) -> {
            String operation = key.split(":")[0];
            operationTotals.merge(operation, gas, Long::sum);
            operationCounts.merge(operation, 1, Integer::sum);
        });
        
        operationTotals.forEach((operation, total) -> {
            int count = operationCounts.get(operation);
            averages.put(operation, (double) total / count);
        });
        
        return averages;
    }

    @Override
    public void resetMetrics() {
        totalRequests.set(0);
        totalErrors.set(0);
        requestCounts.clear();
        errorCounts.clear();
        responseTimes.clear();
        gasUsage.clear();
        log.info("重置所有区块链指标");
    }

    @Override
    public Map<String, Object> getHealthStatus() {
        Map<String, Object> health = new HashMap<>();
        
        long requests = totalRequests.get();
        long errors = totalErrors.get();
        double errorRate = requests > 0 ? (double) errors / requests * 100 : 0;
        
        health.put("status", errorRate < 10 ? "HEALTHY" : errorRate < 30 ? "WARNING" : "CRITICAL");
        health.put("errorRate", errorRate);
        health.put("totalRequests", requests);
        health.put("totalErrors", errors);
        health.put("timestamp", LocalDateTime.now());
        
        return health;
    }
}
