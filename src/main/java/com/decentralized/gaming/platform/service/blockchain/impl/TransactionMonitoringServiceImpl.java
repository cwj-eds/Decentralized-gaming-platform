package com.decentralized.gaming.platform.service.blockchain.impl;

import com.decentralized.gaming.platform.service.blockchain.BlockchainService;
import com.decentralized.gaming.platform.service.blockchain.TransactionMonitoringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 交易监控服务实现类
 * 提供交易状态监控功能
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
public class TransactionMonitoringServiceImpl implements TransactionMonitoringService {

    @Autowired
    private BlockchainService blockchainService;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private final Map<String, MonitoringTask> monitoringTasks = new ConcurrentHashMap<>();

    @Override
    public void startMonitoring(String txHash, MonitoringCallback callback) {
        startMonitoring(txHash, 3, callback); // 默认3秒间隔
    }

    public void startMonitoring(String txHash, int intervalSeconds, MonitoringCallback callback) {
        if (monitoringTasks.containsKey(txHash)) {
            log.warn("交易 {} 已在监控中", txHash);
            return;
        }

        MonitoringTask task = new MonitoringTask(txHash, intervalSeconds, callback);
        monitoringTasks.put(txHash, task);
        
        scheduler.scheduleAtFixedRate(task, 0, intervalSeconds, TimeUnit.SECONDS);
        log.info("开始监控交易: {}, 监控间隔: {}秒", txHash, intervalSeconds);
    }

    @Override
    public void stopMonitoring(String txHash) {
        MonitoringTask task = monitoringTasks.remove(txHash);
        if (task != null) {
            task.stop();
            log.info("停止监控交易: {}", txHash);
        } else {
            log.warn("交易 {} 未在监控中", txHash);
        }
    }

    @Override
    public MonitoringStatus getMonitoringStatus(String txHash) {
        MonitoringTask task = monitoringTasks.get(txHash);
        if (task != null) {
            return task.getStatusEnum();
        } else {
            return MonitoringStatus.PENDING;
        }
    }

    public Map<String, Object> getMonitoringStatusDetails(String txHash) {
        MonitoringTask task = monitoringTasks.get(txHash);
        if (task != null) {
            return task.getStatus();
        } else {
            return Map.of("monitoring", false, "message", "交易未在监控中");
        }
    }

    @Override
    public Map<String, MonitoringStatus> getAllMonitoringStatus() {
        Map<String, MonitoringStatus> status = new ConcurrentHashMap<>();
        for (Map.Entry<String, MonitoringTask> entry : monitoringTasks.entrySet()) {
            status.put(entry.getKey(), entry.getValue().getStatusEnum());
        }
        return status;
    }

    public Map<String, Object> getAllMonitoringStatusDetails() {
        Map<String, Object> status = new ConcurrentHashMap<>();
        status.put("totalTasks", monitoringTasks.size());
        status.put("activeTasks", monitoringTasks.keySet());
        
        Map<String, Object> taskStatuses = new ConcurrentHashMap<>();
        for (Map.Entry<String, MonitoringTask> entry : monitoringTasks.entrySet()) {
            taskStatuses.put(entry.getKey(), entry.getValue().getStatus());
        }
        status.put("taskStatuses", taskStatuses);
        
        return status;
    }

    @Override
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(30, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        log.info("交易监控服务已关闭");
    }

    /**
     * 监控任务
     */
    private class MonitoringTask implements Runnable {
        private final String txHash;
        private final int intervalSeconds;
        private final MonitoringCallback callback;
        private volatile boolean running = true;
        private int checkCount = 0;
        private long startTime = System.currentTimeMillis();
        private volatile MonitoringStatus status = MonitoringStatus.PENDING;

        public MonitoringTask(String txHash, int intervalSeconds, MonitoringCallback callback) {
            this.txHash = txHash;
            this.intervalSeconds = intervalSeconds;
            this.callback = callback;
        }

        @Override
        public void run() {
            if (!running) {
                return;
            }

            try {
                checkCount++;
                
                // 检查交易状态
                TransactionReceipt receipt = blockchainService.waitForTransactionReceipt(txHash, 5);
                boolean isSuccessful = blockchainService.isTransactionSuccessful(txHash);
                BigInteger currentBlockNumber = blockchainService.getCurrentBlockNumber();
                
                // 如果交易已确认，调用回调并停止监控
                if (receipt != null && isSuccessful) {
                    status = MonitoringStatus.CONFIRMED;
                    if (callback != null) {
                        callback.onConfirmed(txHash, receipt.getBlockNumber().longValue());
                    }
                    log.info("交易 {} 已确认，停止监控", txHash);
                    stop();
                    monitoringTasks.remove(txHash);
                } else if (receipt != null && !isSuccessful) {
                    status = MonitoringStatus.FAILED;
                    if (callback != null) {
                        callback.onFailed(txHash, "交易执行失败");
                    }
                    stop();
                    monitoringTasks.remove(txHash);
                } else if (checkCount > 100) {
                    // 超时检查
                    status = MonitoringStatus.TIMEOUT;
                    if (callback != null) {
                        callback.onTimeout(txHash);
                    }
                    stop();
                    monitoringTasks.remove(txHash);
                }

            } catch (Exception e) {
                log.error("监控交易 {} 时出错", txHash, e);
                status = MonitoringStatus.FAILED;
                if (callback != null) {
                    callback.onFailed(txHash, e.getMessage());
                }
            }
        }

        public void stop() {
            running = false;
        }

        public MonitoringStatus getStatusEnum() {
            return status;
        }

        public Map<String, Object> getStatus() {
            return Map.of(
                "monitoring", running,
                "txHash", txHash,
                "intervalSeconds", intervalSeconds,
                "checkCount", checkCount,
                "runningTime", System.currentTimeMillis() - startTime,
                "startTime", startTime,
                "status", status.name()
            );
        }
    }
}
