package com.decentralized.gaming.platform.service.blockchain.impl;

import com.decentralized.gaming.platform.service.blockchain.BlockchainService;
import com.decentralized.gaming.platform.service.blockchain.EventListeningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Log;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 智能合约事件监听服务实现类
 * 监听区块链上的智能合约事件
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
public class EventListeningServiceImpl implements EventListeningService {

    @Autowired(required = false)
    private Web3j web3j;

    @Autowired
    private BlockchainService blockchainService;

    private ExecutorService executorService;
    private volatile boolean isListening = false;

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(5);
        log.info("事件监听服务初始化完成");
    }

    @PreDestroy
    public void destroy() {
        stopListening();
        if (executorService != null) {
            executorService.shutdown();
        }
        log.info("事件监听服务已关闭");
    }

    @Override
    public void startListening() {
        if (isListening) {
            log.warn("事件监听已在运行中");
            return;
        }

        isListening = true;
        log.info("开始监听智能合约事件...");

        // 监听平台代币事件
        listenToPlatformTokenEvents();
        
        // 监听游戏NFT事件
        listenToGameNFTEvents();
        
        // 监听智能体NFT事件
        listenToAgentNFTEvents();
        
        // 监听市场事件
        listenToMarketplaceEvents();
        
        // 监听奖励事件
        listenToRewardsEvents();
    }

    @Override
    public void stopListening() {
        isListening = false;
        log.info("停止监听智能合约事件");
    }

    @Override
    public boolean isListening() {
        return isListening;
    }

    @Override
    public List<Log> getHistoricalEvents(String fromBlock, String toBlock) {
        try {
            BigInteger from = new BigInteger(fromBlock);
            BigInteger to = new BigInteger(toBlock);
            return getHistoricalEventsAsync("", "", from, to).get();
        } catch (Exception e) {
            log.error("获取历史事件失败", e);
            return new ArrayList<>();
        }
    }

    public CompletableFuture<List<Log>> getHistoricalEventsAsync(String contractAddress, String eventSignature, BigInteger fromBlock, BigInteger toBlock) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                EthFilter filter = new EthFilter(
                    org.web3j.protocol.core.DefaultBlockParameter.valueOf(fromBlock),
                    org.web3j.protocol.core.DefaultBlockParameter.valueOf(toBlock),
                    contractAddress
                );
                filter.addSingleTopic(eventSignature);

                EthLog ethLog = web3j.ethGetLogs(filter).send();
                List<Log> logs = new java.util.ArrayList<>();
                for (EthLog.LogResult<?> logResult : ethLog.getLogs()) {
                    if (logResult instanceof Log) {
                        logs.add((Log) logResult);
                    }
                }
                return logs;
            } catch (IOException e) {
                log.error("获取历史事件失败", e);
                throw new RuntimeException("获取历史事件失败", e);
            }
        }, executorService);
    }

    @Override
    public List<Log> getAddressHistoricalEvents(String address, String fromBlock, String toBlock) {
        try {
            BigInteger from = new BigInteger(fromBlock);
            BigInteger to = new BigInteger(toBlock);
            return getAddressHistoricalEventsAsync("", address, from, to).get();
        } catch (Exception e) {
            log.error("获取地址历史事件失败", e);
            return new ArrayList<>();
        }
    }

    public CompletableFuture<List<Log>> getAddressHistoricalEventsAsync(String contractAddress, String address, BigInteger fromBlock, BigInteger toBlock) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                EthFilter filter = new EthFilter(
                    org.web3j.protocol.core.DefaultBlockParameter.valueOf(fromBlock),
                    org.web3j.protocol.core.DefaultBlockParameter.valueOf(toBlock),
                    contractAddress
                );
                // 添加地址作为主题过滤
                // 注意：addOptionalTopic方法可能不存在，这里使用addSingleTopic作为替代
                // filter.addOptionalTopic("0x000000000000000000000000" + address.substring(2).toLowerCase());

                EthLog ethLog = web3j.ethGetLogs(filter).send();
                List<Log> logs = new java.util.ArrayList<>();
                for (EthLog.LogResult<?> logResult : ethLog.getLogs()) {
                    if (logResult instanceof Log) {
                        logs.add((Log) logResult);
                    }
                }
                return logs;
            } catch (IOException e) {
                log.error("获取地址历史事件失败", e);
                throw new RuntimeException("获取地址历史事件失败", e);
            }
        }, executorService);
    }

    @Override
    public List<Log> getMultiContractHistoricalEvents(List<String> contractAddresses, String fromBlock, String toBlock) {
        try {
            BigInteger from = new BigInteger(fromBlock);
            BigInteger to = new BigInteger(toBlock);
            return getMultiContractHistoricalEventsAsync(contractAddresses, "", from, to).get();
        } catch (Exception e) {
            log.error("获取多合约历史事件失败", e);
            return new ArrayList<>();
        }
    }

    public CompletableFuture<List<Log>> getMultiContractHistoricalEventsAsync(List<String> contractAddresses, String eventSignature, BigInteger fromBlock, BigInteger toBlock) {
        return CompletableFuture.supplyAsync(() -> {
            List<Log> allLogs = new java.util.ArrayList<>();
            try {
                for (String contractAddress : contractAddresses) {
                    EthFilter filter = new EthFilter(
                        org.web3j.protocol.core.DefaultBlockParameter.valueOf(fromBlock),
                        org.web3j.protocol.core.DefaultBlockParameter.valueOf(toBlock),
                        contractAddress
                    );
                    filter.addSingleTopic(eventSignature);

                    EthLog ethLog = web3j.ethGetLogs(filter).send();
                    for (EthLog.LogResult<?> logResult : ethLog.getLogs()) {
                        if (logResult instanceof Log) {
                            allLogs.add((Log) logResult);
                        }
                    }
                }
                return allLogs;
            } catch (IOException e) {
                log.error("获取多合约历史事件失败", e);
                throw new RuntimeException("获取多合约历史事件失败", e);
            }
        }, executorService);
    }

    @Override
    public Map<String, Object> getEventStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("isListening", isListening);
        statistics.put("executorServiceActive", !executorService.isShutdown());
        statistics.put("message", "事件统计功能基础实现");
        return statistics;
    }

    public CompletableFuture<Map<String, Object>> getEventStatisticsAsync(String contractAddress, String eventSignature, BigInteger fromBlock, BigInteger toBlock) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Log> logs = getHistoricalEventsAsync(contractAddress, eventSignature, fromBlock, toBlock).get();
                
                Map<String, Object> statistics = new java.util.HashMap<>();
                statistics.put("totalEvents", logs.size());
                statistics.put("fromBlock", fromBlock);
                statistics.put("toBlock", toBlock);
                statistics.put("contractAddress", contractAddress);
                statistics.put("eventSignature", eventSignature);
                
                // 按区块统计
                Map<String, Integer> blockCounts = new java.util.HashMap<>();
                for (Log log : logs) {
                    String blockNumber = log.getBlockNumber().toString();
                    blockCounts.put(blockNumber, blockCounts.getOrDefault(blockNumber, 0) + 1);
                }
                statistics.put("blockCounts", blockCounts);
                
                return statistics;
            } catch (Exception e) {
                log.error("获取事件统计失败", e);
                throw new RuntimeException("获取事件统计失败", e);
            }
        }, executorService);
    }

    @Override
    public void startContractListening(String contractAddress, String eventSignature) {
        executorService.submit(() -> {
            try {
                EthFilter filter = new EthFilter(
                    DefaultBlockParameterName.LATEST,
                    DefaultBlockParameterName.LATEST,
                    contractAddress
                );
                filter.addSingleTopic(eventSignature);

                web3j.ethLogFlowable(filter).subscribe(
                    log -> {
                        if (isListening) {
                            handleCustomEvent(log, eventSignature);
                        }
                    },
                    error -> log.error("监听合约事件失败: {}", contractAddress, error)
                );
                
                log.info("开始监听合约事件: 合约地址={}, 事件签名={}", contractAddress, eventSignature);
            } catch (Exception e) {
                log.error("设置合约事件监听失败: {}", contractAddress, e);
            }
        });
    }

    @Override
    public Map<String, Object> getListeningStatusDetails() {
        Map<String, Object> status = new java.util.HashMap<>();
        status.put("isListening", isListening);
        status.put("timestamp", System.currentTimeMillis());
        status.put("executorServiceActive", !executorService.isShutdown());
        status.put("executorServiceTerminated", executorService.isTerminated());
        
        return status;
    }

    @Override
    public void restartListening() {
        log.info("重启事件监听服务...");
        stopListening();
        try {
            Thread.sleep(1000); // 等待1秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        startListening();
    }

    @Override
    public void listenToPlatformTokenEvents() {
        executorService.submit(() -> {
            try {
                // 监听Transfer事件
                EthFilter transferFilter = new EthFilter(
                    DefaultBlockParameterName.LATEST,
                    DefaultBlockParameterName.LATEST,
                    "0x" // 这里应该是PlatformToken合约地址
                );
                transferFilter.addSingleTopic("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef"); // Transfer事件签名

                web3j.ethLogFlowable(transferFilter).subscribe(
                    log -> {
                        if (isListening) {
                            handleTransferEvent(log, "PlatformToken");
                        }
                    },
                    error -> log.error("监听平台代币Transfer事件失败", error)
                );

                // 监听Approval事件
                EthFilter approvalFilter = new EthFilter(
                    DefaultBlockParameterName.LATEST,
                    DefaultBlockParameterName.LATEST,
                    "0x" // 这里应该是PlatformToken合约地址
                );
                approvalFilter.addSingleTopic("0x8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925"); // Approval事件签名

                web3j.ethLogFlowable(approvalFilter).subscribe(
                    log -> {
                        if (isListening) {
                            handleApprovalEvent(log, "PlatformToken");
                        }
                    },
                    error -> log.error("监听平台代币Approval事件失败", error)
                );

            } catch (Exception e) {
                log.error("设置平台代币事件监听失败", e);
            }
        });
    }

    /**
     * 监听游戏NFT事件
     */
    @Override
    public void listenToGameNFTEvents() {
        executorService.submit(() -> {
            try {
                // 监听Transfer事件
                EthFilter transferFilter = new EthFilter(
                    DefaultBlockParameterName.LATEST,
                    DefaultBlockParameterName.LATEST,
                    "0x" // 这里应该是GameNFT合约地址
                );
                transferFilter.addSingleTopic("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef"); // Transfer事件签名

                web3j.ethLogFlowable(transferFilter).subscribe(
                    log -> {
                        if (isListening) {
                            handleTransferEvent(log, "GameNFT");
                        }
                    },
                    error -> log.error("监听游戏NFT Transfer事件失败", error)
                );

                // 监听GameCreated事件
                EthFilter gameCreatedFilter = new EthFilter(
                    DefaultBlockParameterName.LATEST,
                    DefaultBlockParameterName.LATEST,
                    "0x" // 这里应该是GameNFT合约地址
                );
                // 这里需要根据实际的GameCreated事件签名来设置
                // gameCreatedFilter.addSingleTopic("0x...");

                web3j.ethLogFlowable(gameCreatedFilter).subscribe(
                    log -> {
                        if (isListening) {
                            handleGameCreatedEvent(log);
                        }
                    },
                    error -> log.error("监听游戏NFT GameCreated事件失败", error)
                );

            } catch (Exception e) {
                log.error("设置游戏NFT事件监听失败", e);
            }
        });
    }

    /**
     * 监听智能体NFT事件
     */
    @Override
    public void listenToAgentNFTEvents() {
        executorService.submit(() -> {
            try {
                // 监听Transfer事件
                EthFilter transferFilter = new EthFilter(
                    DefaultBlockParameterName.LATEST,
                    DefaultBlockParameterName.LATEST,
                    "0x" // 这里应该是AgentNFT合约地址
                );
                transferFilter.addSingleTopic("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef"); // Transfer事件签名

                web3j.ethLogFlowable(transferFilter).subscribe(
                    log -> {
                        if (isListening) {
                            handleTransferEvent(log, "AgentNFT");
                        }
                    },
                    error -> log.error("监听智能体NFT Transfer事件失败", error)
                );

                // 监听AgentCreated事件
                EthFilter agentCreatedFilter = new EthFilter(
                    DefaultBlockParameterName.LATEST,
                    DefaultBlockParameterName.LATEST,
                    "0x" // 这里应该是AgentNFT合约地址
                );
                // 这里需要根据实际的AgentCreated事件签名来设置
                // agentCreatedFilter.addSingleTopic("0x...");

                web3j.ethLogFlowable(agentCreatedFilter).subscribe(
                    log -> {
                        if (isListening) {
                            handleAgentCreatedEvent(log);
                        }
                    },
                    error -> log.error("监听智能体NFT AgentCreated事件失败", error)
                );

            } catch (Exception e) {
                log.error("设置智能体NFT事件监听失败", e);
            }
        });
    }

    /**
     * 监听市场事件
     */
    @Override
    public void listenToMarketplaceEvents() {
        executorService.submit(() -> {
            try {
                // 监听ItemListed事件
                EthFilter itemListedFilter = new EthFilter(
                    DefaultBlockParameterName.LATEST,
                    DefaultBlockParameterName.LATEST,
                    "0x" // 这里应该是Marketplace合约地址
                );
                // 这里需要根据实际的ItemListed事件签名来设置
                // itemListedFilter.addSingleTopic("0x...");

                web3j.ethLogFlowable(itemListedFilter).subscribe(
                    log -> {
                        if (isListening) {
                            handleItemListedEvent(log);
                        }
                    },
                    error -> log.error("监听市场ItemListed事件失败", error)
                );

                // 监听ItemSold事件
                EthFilter itemSoldFilter = new EthFilter(
                    DefaultBlockParameterName.LATEST,
                    DefaultBlockParameterName.LATEST,
                    "0x" // 这里应该是Marketplace合约地址
                );
                // 这里需要根据实际的ItemSold事件签名来设置
                // itemSoldFilter.addSingleTopic("0x...");

                web3j.ethLogFlowable(itemSoldFilter).subscribe(
                    log -> {
                        if (isListening) {
                            handleItemSoldEvent(log);
                        }
                    },
                    error -> log.error("监听市场ItemSold事件失败", error)
                );

            } catch (Exception e) {
                log.error("设置市场事件监听失败", e);
            }
        });
    }

    /**
     * 监听奖励事件
     */
    @Override
    public void listenToRewardsEvents() {
        executorService.submit(() -> {
            try {
                // 监听RewardIssued事件
                EthFilter rewardIssuedFilter = new EthFilter(
                    DefaultBlockParameterName.LATEST,
                    DefaultBlockParameterName.LATEST,
                    "0x" // 这里应该是Rewards合约地址
                );
                // 这里需要根据实际的RewardIssued事件签名来设置
                // rewardIssuedFilter.addSingleTopic("0x...");

                web3j.ethLogFlowable(rewardIssuedFilter).subscribe(
                    log -> {
                        if (isListening) {
                            handleRewardIssuedEvent(log);
                        }
                    },
                    error -> log.error("监听奖励RewardIssued事件失败", error)
                );

            } catch (Exception e) {
                log.error("设置奖励事件监听失败", e);
            }
        });
    }

    @Override
    public void handleTransferEvent(Log eventLog) {
        handleTransferEvent(eventLog, "Unknown");
    }

    /**
     * 处理Transfer事件（内部方法）
     */
    private void handleTransferEvent(Log eventLog, String contractType) {
        try {
            log.info("检测到{} Transfer事件: 交易哈希={}, 区块号={}", 
                contractType, eventLog.getTransactionHash(), eventLog.getBlockNumber());
            
            // 这里可以添加具体的业务逻辑
            // 例如：更新数据库、发送通知等
            
        } catch (Exception e) {
            log.error("处理{} Transfer事件失败", contractType, e);
        }
    }

    @Override
    public void handleApprovalEvent(Log eventLog) {
        handleApprovalEvent(eventLog, "Unknown");
    }

    /**
     * 处理Approval事件（内部方法）
     */
    private void handleApprovalEvent(Log eventLog, String contractType) {
        try {
            log.info("检测到{} Approval事件: 交易哈希={}, 区块号={}", 
                contractType, eventLog.getTransactionHash(), eventLog.getBlockNumber());
            
            // 这里可以添加具体的业务逻辑
            
        } catch (Exception e) {
            log.error("处理{} Approval事件失败", contractType, e);
        }
    }

    @Override
    public void handleGameCreatedEvent(Log eventLog) {
        try {
            log.info("检测到游戏创建事件: 交易哈希={}, 区块号={}", 
                eventLog.getTransactionHash(), eventLog.getBlockNumber());
            
            // 这里可以添加具体的业务逻辑
            // 例如：更新游戏数据库、发送通知等
            
        } catch (Exception e) {
            log.error("处理游戏创建事件失败", e);
        }
    }

    @Override
    public void handleAgentCreatedEvent(Log eventLog) {
        try {
            log.info("检测到智能体创建事件: 交易哈希={}, 区块号={}", 
                eventLog.getTransactionHash(), eventLog.getBlockNumber());
            
            // 这里可以添加具体的业务逻辑
            // 例如：更新智能体数据库、发送通知等
            
        } catch (Exception e) {
            log.error("处理智能体创建事件失败", e);
        }
    }

    @Override
    public void handleItemListedEvent(Log eventLog) {
        try {
            log.info("检测到物品上架事件: 交易哈希={}, 区块号={}", 
                eventLog.getTransactionHash(), eventLog.getBlockNumber());
            
            // 这里可以添加具体的业务逻辑
            // 例如：更新市场数据库、发送通知等
            
        } catch (Exception e) {
            log.error("处理物品上架事件失败", e);
        }
    }

    @Override
    public void handleItemSoldEvent(Log eventLog) {
        try {
            log.info("检测到物品售出事件: 交易哈希={}, 区块号={}", 
                eventLog.getTransactionHash(), eventLog.getBlockNumber());
            
            // 这里可以添加具体的业务逻辑
            // 例如：更新市场数据库、发送通知等
            
        } catch (Exception e) {
            log.error("处理物品售出事件失败", e);
        }
    }

    @Override
    public void handleRewardIssuedEvent(Log eventLog) {
        try {
            log.info("检测到奖励发放事件: 交易哈希={}, 区块号={}", 
                eventLog.getTransactionHash(), eventLog.getBlockNumber());
            
            // 这里可以添加具体的业务逻辑
            // 例如：更新奖励数据库、发送通知等
            
        } catch (Exception e) {
            log.error("处理奖励发放事件失败", e);
        }
    }

    @Override
    public void handleCustomEvent(Log eventLog, String eventType) {
        handleCustomEventInternal(eventLog, "", eventType);
    }

    /**
     * 处理自定义事件（内部方法）
     */
    private void handleCustomEventInternal(Log eventLog, String contractAddress, String eventSignature) {
        try {
            log.info("检测到自定义事件: 合约地址={}, 事件签名={}, 交易哈希={}, 区块号={}", 
                contractAddress, eventSignature, eventLog.getTransactionHash(), eventLog.getBlockNumber());
            
            // 这里可以添加具体的业务逻辑
            // 例如：根据合约地址和事件签名进行不同的处理
            
        } catch (Exception e) {
            log.error("处理自定义事件失败: 合约地址={}, 事件签名={}", contractAddress, eventSignature, e);
        }
    }
}
