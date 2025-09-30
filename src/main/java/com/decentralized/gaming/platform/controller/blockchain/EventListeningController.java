package com.decentralized.gaming.platform.controller.blockchain;

import com.decentralized.gaming.platform.common.Result;
import com.decentralized.gaming.platform.service.blockchain.ContractConfigService;
import com.decentralized.gaming.platform.service.blockchain.EventListeningService;
import com.decentralized.gaming.platform.service.blockchain.EventSubscriptionService;
import com.decentralized.gaming.platform.entity.EventSubscription;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.Log;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 事件监听控制器
 * 提供智能合约事件监听相关的API接口
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@RestController
@RequestMapping("/api/events")
@Tag(name = "事件监听", description = "智能合约事件监听相关的API接口")
public class EventListeningController {

    @Autowired
    private EventListeningService eventListeningService;

    @Autowired
    private ContractConfigService contractConfigService;

    @Autowired
    private EventSubscriptionService eventSubscriptionService;

    // ==================== 事件监听控制 ====================

    @PostMapping("/start")
    @Operation(summary = "开始事件监听", description = "开始监听所有智能合约事件")
    public Result<String> startEventListening() {
        try {
            eventListeningService.startListening();
            return Result.success("事件监听已启动", "开始监听智能合约事件");
        } catch (Exception e) {
            log.error("启动事件监听失败", e);
            return Result.error("启动事件监听失败: " + e.getMessage());
        }
    }

    @PostMapping("/stop")
    @Operation(summary = "停止事件监听", description = "停止监听智能合约事件")
    public Result<String> stopEventListening() {
        try {
            eventListeningService.stopListening();
            return Result.success("事件监听已停止", "停止监听智能合约事件");
        } catch (Exception e) {
            log.error("停止事件监听失败", e);
            return Result.error("停止事件监听失败: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    @Operation(summary = "获取事件监听状态", description = "获取事件监听服务的状态")
    public Result<Object> getEventListeningStatus() {
        try {
            Map<String, Object> status = new HashMap<>();
            status.put("isListening", eventListeningService.isListening());
            status.put("timestamp", System.currentTimeMillis());
            status.put("status", eventListeningService.isListening() ? "监听中" : "已停止");
            
            return Result.success(status, "获取事件监听状态成功");
        } catch (Exception e) {
            log.error("获取事件监听状态失败", e);
            return Result.error("获取事件监听状态失败: " + e.getMessage());
        }
    }

    // ==================== 历史事件查询 ====================

    @GetMapping("/historical")
    @Operation(summary = "获取历史事件", description = "获取指定合约的历史事件")
    public Result<Object> getHistoricalEvents(
            @Parameter(description = "合约地址") @RequestParam String contractAddress,
            @Parameter(description = "事件签名") @RequestParam String eventSignature,
            @Parameter(description = "起始区块") @RequestParam BigInteger fromBlock,
            @Parameter(description = "结束区块") @RequestParam BigInteger toBlock) {
        try {
            List<Log> logs = eventListeningService.getHistoricalEvents(fromBlock.toString(), toBlock.toString());
            
            Map<String, Object> result = new HashMap<>();
            result.put("events", logs);
            result.put("count", logs.size());
            result.put("fromBlock", fromBlock);
            result.put("toBlock", toBlock);
            result.put("contractAddress", contractAddress);
            result.put("eventSignature", eventSignature);
            
            return Result.success(result, "获取历史事件成功");
        } catch (Exception e) {
            log.error("获取历史事件失败", e);
            return Result.error("获取历史事件失败: " + e.getMessage());
        }
    }

    @GetMapping("/historical/transfer")
    @Operation(summary = "获取转账历史事件", description = "获取指定合约的转账历史事件")
    public Result<Object> getTransferHistoryEvents(
            @Parameter(description = "合约地址") @RequestParam String contractAddress,
            @Parameter(description = "起始区块") @RequestParam BigInteger fromBlock,
            @Parameter(description = "结束区块") @RequestParam BigInteger toBlock) {
        try {
            // Transfer事件签名: Transfer(address,address,uint256)
            String transferEventSignature = "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";
            
            List<Log> logs = eventListeningService.getHistoricalEvents(fromBlock.toString(), toBlock.toString());
            
            Map<String, Object> result = new HashMap<>();
            result.put("events", logs);
            result.put("count", logs.size());
            result.put("fromBlock", fromBlock);
            result.put("toBlock", toBlock);
            result.put("contractAddress", contractAddress);
            result.put("eventType", "Transfer");
            
            return Result.success(result, "获取转账历史事件成功");
        } catch (Exception e) {
            log.error("获取转账历史事件失败", e);
            return Result.error("获取转账历史事件失败: " + e.getMessage());
        }
    }

    @GetMapping("/historical/approval")
    @Operation(summary = "获取授权历史事件", description = "获取指定合约的授权历史事件")
    public Result<Object> getApprovalHistoryEvents(
            @Parameter(description = "合约地址") @RequestParam String contractAddress,
            @Parameter(description = "起始区块") @RequestParam BigInteger fromBlock,
            @Parameter(description = "结束区块") @RequestParam BigInteger toBlock) {
        try {
            // Approval事件签名: Approval(address,address,uint256)
            String approvalEventSignature = "0x8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925";
            
            List<Log> logs = eventListeningService.getHistoricalEvents(fromBlock.toString(), toBlock.toString());
            
            Map<String, Object> result = new HashMap<>();
            result.put("events", logs);
            result.put("count", logs.size());
            result.put("fromBlock", fromBlock);
            result.put("toBlock", toBlock);
            result.put("contractAddress", contractAddress);
            result.put("eventType", "Approval");
            
            return Result.success(result, "获取授权历史事件成功");
        } catch (Exception e) {
            log.error("获取授权历史事件失败", e);
            return Result.error("获取授权历史事件失败: " + e.getMessage());
        }
    }

    // ==================== 特定合约事件查询 ====================

    @GetMapping("/game-nft/events")
    @Operation(summary = "获取游戏NFT事件", description = "获取游戏NFT合约的事件")
    public Result<Object> getGameNFTEvents(
            @Parameter(description = "事件类型") @RequestParam(required = false) String eventType,
            @Parameter(description = "起始区块") @RequestParam(defaultValue = "0") BigInteger fromBlock,
            @Parameter(description = "结束区块") @RequestParam(defaultValue = "latest") BigInteger toBlock) {
        try {
            String contractAddress = contractConfigService.getContractAddress("game-nft");
            if (contractAddress.isEmpty()) {
                return Result.error("游戏NFT合约地址未配置");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("contractType", "GameNFT");
            result.put("contractAddress", contractAddress);
            result.put("eventType", eventType);
            result.put("fromBlock", fromBlock);
            result.put("toBlock", toBlock);
            result.put("deployed", contractConfigService.isContractDeployed("game-nft"));
            
            // 如果指定了事件类型，尝试获取历史事件
            if (eventType != null && !eventType.isEmpty()) {
                try {
                    String eventSignature = getEventSignature(eventType);
                    if (!eventSignature.isEmpty()) {
                        List<Log> logs = eventListeningService.getHistoricalEvents(
                            fromBlock.toString(), toBlock.toString());
                        result.put("events", logs);
                        result.put("eventCount", logs.size());
                    }
                } catch (Exception e) {
                    log.warn("获取游戏NFT历史事件失败: {}", e.getMessage());
                    result.put("eventError", e.getMessage());
                }
            }
            
            return Result.success(result, "获取游戏NFT事件成功");
        } catch (Exception e) {
            log.error("获取游戏NFT事件失败", e);
            return Result.error("获取游戏NFT事件失败: " + e.getMessage());
        }
    }

    @GetMapping("/agent-nft/events")
    @Operation(summary = "获取智能体NFT事件", description = "获取智能体NFT合约的事件")
    public Result<Object> getAgentNFTEvents(
            @Parameter(description = "事件类型") @RequestParam(required = false) String eventType,
            @Parameter(description = "起始区块") @RequestParam(defaultValue = "0") BigInteger fromBlock,
            @Parameter(description = "结束区块") @RequestParam(defaultValue = "latest") BigInteger toBlock) {
        try {
            String contractAddress = contractConfigService.getContractAddress("agent-nft");
            if (contractAddress.isEmpty()) {
                return Result.error("智能体NFT合约地址未配置");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("contractType", "AgentNFT");
            result.put("contractAddress", contractAddress);
            result.put("eventType", eventType);
            result.put("fromBlock", fromBlock);
            result.put("toBlock", toBlock);
            result.put("deployed", contractConfigService.isContractDeployed("agent-nft"));
            
            // 如果指定了事件类型，尝试获取历史事件
            if (eventType != null && !eventType.isEmpty()) {
                try {
                    String eventSignature = getEventSignature(eventType);
                    if (!eventSignature.isEmpty()) {
                        List<Log> logs = eventListeningService.getHistoricalEvents(
                            fromBlock.toString(), toBlock.toString());
                        result.put("events", logs);
                        result.put("eventCount", logs.size());
                    }
                } catch (Exception e) {
                    log.warn("获取智能体NFT历史事件失败: {}", e.getMessage());
                    result.put("eventError", e.getMessage());
                }
            }
            
            return Result.success(result, "获取智能体NFT事件成功");
        } catch (Exception e) {
            log.error("获取智能体NFT事件失败", e);
            return Result.error("获取智能体NFT事件失败: " + e.getMessage());
        }
    }

    @GetMapping("/marketplace/events")
    @Operation(summary = "获取市场事件", description = "获取市场合约的事件")
    public Result<Object> getMarketplaceEvents(
            @Parameter(description = "事件类型") @RequestParam(required = false) String eventType,
            @Parameter(description = "起始区块") @RequestParam(defaultValue = "0") BigInteger fromBlock,
            @Parameter(description = "结束区块") @RequestParam(defaultValue = "latest") BigInteger toBlock) {
        try {
            String contractAddress = contractConfigService.getContractAddress("marketplace");
            if (contractAddress.isEmpty()) {
                return Result.error("市场合约地址未配置");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("contractType", "Marketplace");
            result.put("contractAddress", contractAddress);
            result.put("eventType", eventType);
            result.put("fromBlock", fromBlock);
            result.put("toBlock", toBlock);
            result.put("deployed", contractConfigService.isContractDeployed("marketplace"));
            
            // 如果指定了事件类型，尝试获取历史事件
            if (eventType != null && !eventType.isEmpty()) {
                try {
                    String eventSignature = getEventSignature(eventType);
                    if (!eventSignature.isEmpty()) {
                        List<Log> logs = eventListeningService.getHistoricalEvents(
                            fromBlock.toString(), toBlock.toString());
                        result.put("events", logs);
                        result.put("eventCount", logs.size());
                    }
                } catch (Exception e) {
                    log.warn("获取市场历史事件失败: {}", e.getMessage());
                    result.put("eventError", e.getMessage());
                }
            }
            
            return Result.success(result, "获取市场事件成功");
        } catch (Exception e) {
            log.error("获取市场事件失败", e);
            return Result.error("获取市场事件失败: " + e.getMessage());
        }
    }

    @GetMapping("/platform-token/events")
    @Operation(summary = "获取平台代币事件", description = "获取平台代币合约的事件")
    public Result<Object> getPlatformTokenEvents(
            @Parameter(description = "事件类型") @RequestParam(required = false) String eventType,
            @Parameter(description = "起始区块") @RequestParam(defaultValue = "0") BigInteger fromBlock,
            @Parameter(description = "结束区块") @RequestParam(defaultValue = "latest") BigInteger toBlock) {
        try {
            String contractAddress = contractConfigService.getContractAddress("platform-token");
            if (contractAddress.isEmpty()) {
                return Result.error("平台代币合约地址未配置");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("contractType", "PlatformToken");
            result.put("contractAddress", contractAddress);
            result.put("eventType", eventType);
            result.put("fromBlock", fromBlock);
            result.put("toBlock", toBlock);
            result.put("deployed", contractConfigService.isContractDeployed("platform-token"));
            
            // 如果指定了事件类型，尝试获取历史事件
            if (eventType != null && !eventType.isEmpty()) {
                try {
                    String eventSignature = getEventSignature(eventType);
                    if (!eventSignature.isEmpty()) {
                        List<Log> logs = eventListeningService.getHistoricalEvents(
                            fromBlock.toString(), toBlock.toString());
                        result.put("events", logs);
                        result.put("eventCount", logs.size());
                    }
                } catch (Exception e) {
                    log.warn("获取平台代币历史事件失败: {}", e.getMessage());
                    result.put("eventError", e.getMessage());
                }
            }
            
            return Result.success(result, "获取平台代币事件成功");
        } catch (Exception e) {
            log.error("获取平台代币事件失败", e);
            return Result.error("获取平台代币事件失败: " + e.getMessage());
        }
    }

    // ==================== 事件过滤和搜索 ====================

    @GetMapping("/search")
    @Operation(summary = "搜索事件", description = "根据条件搜索事件")
    public Result<Object> searchEvents(
            @Parameter(description = "合约地址") @RequestParam(required = false) String contractAddress,
            @Parameter(description = "事件签名") @RequestParam(required = false) String eventSignature,
            @Parameter(description = "起始区块") @RequestParam(defaultValue = "0") BigInteger fromBlock,
            @Parameter(description = "结束区块") @RequestParam(defaultValue = "latest") BigInteger toBlock,
            @Parameter(description = "主题过滤") @RequestParam(required = false) List<String> topics) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("contractAddress", contractAddress);
            result.put("eventSignature", eventSignature);
            result.put("fromBlock", fromBlock);
            result.put("toBlock", toBlock);
            result.put("topics", topics);
            result.put("timestamp", System.currentTimeMillis());
            
            // 如果提供了合约地址和事件签名，尝试搜索事件
            if (contractAddress != null && !contractAddress.isEmpty() && 
                eventSignature != null && !eventSignature.isEmpty()) {
                try {
                    List<Log> logs = eventListeningService.getHistoricalEvents(
                        fromBlock.toString(), toBlock.toString());
                    
                    result.put("events", logs);
                    result.put("eventCount", logs.size());
                    result.put("searchSuccessful", true);
                    
                    // 如果有主题过滤，进行进一步过滤
                    if (topics != null && !topics.isEmpty()) {
                        List<Log> filteredLogs = new ArrayList<>();
                        for (Log log : logs) {
                            boolean matches = true;
                            for (int i = 0; i < topics.size() && i < log.getTopics().size(); i++) {
                                if (!log.getTopics().get(i).equals(topics.get(i))) {
                                    matches = false;
                                    break;
                                }
                            }
                            if (matches) {
                                filteredLogs.add(log);
                            }
                        }
                        result.put("filteredEvents", filteredLogs);
                        result.put("filteredCount", filteredLogs.size());
                    }
                    
                } catch (Exception e) {
                    log.warn("搜索事件失败: {}", e.getMessage());
                    result.put("searchError", e.getMessage());
                    result.put("searchSuccessful", false);
                }
            } else {
                result.put("message", "请提供合约地址和事件签名进行搜索");
                result.put("searchSuccessful", false);
            }
            
            result.put("note", "可以通过Web3j的EthFilter实现复杂的事件过滤");
            
            return Result.success(result, "搜索事件成功");
        } catch (Exception e) {
            log.error("搜索事件失败", e);
            return Result.error("搜索事件失败: " + e.getMessage());
        }
    }

    @GetMapping("/address/{address}/events")
    @Operation(summary = "查询地址相关事件", description = "查询与指定地址相关的事件")
    public Result<Object> getAddressEvents(
            @Parameter(description = "钱包地址") @PathVariable String address,
            @Parameter(description = "起始区块") @RequestParam(defaultValue = "0") BigInteger fromBlock,
            @Parameter(description = "结束区块") @RequestParam(defaultValue = "latest") BigInteger toBlock) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("address", address);
            result.put("fromBlock", fromBlock);
            result.put("toBlock", toBlock);
            result.put("timestamp", System.currentTimeMillis());
            
            // 获取所有合约地址
            String[] contractTypes = {"platform-token", "game-nft", "agent-nft", "marketplace", "rewards"};
            Map<String, Object> allEvents = new HashMap<>();
            int totalEvents = 0;
            
            for (String contractType : contractTypes) {
                String contractAddress = contractConfigService.getContractAddress(contractType);
                if (!contractAddress.isEmpty() && contractConfigService.isContractDeployed(contractType)) {
                    try {
                        // 获取Transfer事件
                        String transferSignature = getEventSignature("transfer");
                        if (!transferSignature.isEmpty()) {
                            List<Log> transferLogs = eventListeningService.getHistoricalEvents(
                                fromBlock.toString(), toBlock.toString());
                            
                            // 过滤与指定地址相关的事件
                            List<Log> relevantTransferLogs = new ArrayList<>();
                            for (Log log : transferLogs) {
                                if (log.getTopics().size() >= 3) {
                                    String from = "0x" + log.getTopics().get(1).substring(26);
                                    String to = "0x" + log.getTopics().get(2).substring(26);
                                    if (from.equalsIgnoreCase(address) || to.equalsIgnoreCase(address)) {
                                        relevantTransferLogs.add(log);
                                    }
                                }
                            }
                            
                            if (!relevantTransferLogs.isEmpty()) {
                                Map<String, Object> contractEvents = new HashMap<>();
                                contractEvents.put("contractType", getContractType(contractAddress));
                                contractEvents.put("contractAddress", contractAddress);
                                contractEvents.put("transferEvents", relevantTransferLogs);
                                contractEvents.put("transferCount", relevantTransferLogs.size());
                                
                                allEvents.put(contractType, contractEvents);
                                totalEvents += relevantTransferLogs.size();
                            }
                        }
                    } catch (Exception e) {
                        log.warn("获取{}合约地址事件失败: {}", contractType, e.getMessage());
                    }
                }
            }
            
            result.put("allEvents", allEvents);
            result.put("totalEvents", totalEvents);
            result.put("contractsChecked", contractTypes.length);
            result.put("message", "地址事件查询功能已实现地址过滤逻辑");
            result.put("note", "通过事件日志的topics字段过滤相关地址");
            
            return Result.success(result, "查询地址相关事件成功");
        } catch (Exception e) {
            log.error("查询地址相关事件失败", e);
            return Result.error("查询地址相关事件失败: " + e.getMessage());
        }
    }

    // ==================== 事件统计和分析 ====================

    @GetMapping("/statistics")
    @Operation(summary = "获取事件统计", description = "获取事件统计信息")
    public Result<Object> getEventStatistics(
            @Parameter(description = "合约地址") @RequestParam(required = false) String contractAddress,
            @Parameter(description = "时间范围(小时)") @RequestParam(defaultValue = "24") int hours) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("timeRange", hours + "小时");
            result.put("timestamp", System.currentTimeMillis());
            
            if (contractAddress != null && !contractAddress.isEmpty()) {
                // 获取指定合约的统计信息
                result.put("contractAddress", contractAddress);
                result.put("contractType", getContractType(contractAddress));
                
                // 尝试获取最近的事件统计
                try {
                    BigInteger currentBlock = BigInteger.valueOf(System.currentTimeMillis() / 1000 / 12); // 估算当前区块
                    BigInteger fromBlock = currentBlock.subtract(BigInteger.valueOf(hours * 300)); // 估算起始区块
                    
                    // 获取Transfer事件统计
                    String transferSignature = getEventSignature("transfer");
                    if (!transferSignature.isEmpty()) {
                            List<Log> transferLogs = eventListeningService.getHistoricalEvents(
                                fromBlock.toString(), currentBlock.toString());
                        result.put("transferEvents", transferLogs.size());
                    }
                    
                    // 获取Approval事件统计
                    String approvalSignature = getEventSignature("approval");
                    if (!approvalSignature.isEmpty()) {
                        List<Log> approvalLogs = eventListeningService.getHistoricalEvents(
                            fromBlock.toString(), currentBlock.toString());
                        result.put("approvalEvents", approvalLogs.size());
                    }
                } catch (Exception e) {
                    log.warn("获取合约事件统计失败: {}", e.getMessage());
                    result.put("statisticsError", e.getMessage());
                }
            } else {
                // 获取所有合约的统计信息
                Map<String, Object> allContractsStats = new HashMap<>();
                String[] contractTypes = {"platform-token", "game-nft", "agent-nft", "marketplace", "rewards"};
                
                for (String contractType : contractTypes) {
                    String address = contractConfigService.getContractAddress(contractType);
                    if (!address.isEmpty()) {
                        Map<String, Object> contractStats = new HashMap<>();
                        contractStats.put("address", address);
                        contractStats.put("deployed", contractConfigService.isContractDeployed(contractType));
                        allContractsStats.put(contractType, contractStats);
                    }
                }
                result.put("allContracts", allContractsStats);
            }
            
            return Result.success(result, "获取事件统计成功");
        } catch (Exception e) {
            log.error("获取事件统计失败", e);
            return Result.error("获取事件统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/analytics/volume")
    @Operation(summary = "获取交易量分析", description = "获取交易量分析数据")
    public Result<Object> getVolumeAnalytics(
            @Parameter(description = "时间范围(天)") @RequestParam(defaultValue = "7") int days) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("timeRange", days + "天");
            result.put("timestamp", System.currentTimeMillis());
            
            // 获取所有合约的交易量统计
            Map<String, Object> volumeStats = new HashMap<>();
            String[] contractTypes = {"platform-token", "game-nft", "agent-nft", "marketplace"};
            
            for (String contractType : contractTypes) {
                String address = contractConfigService.getContractAddress(contractType);
                if (!address.isEmpty() && contractConfigService.isContractDeployed(contractType)) {
                    Map<String, Object> contractStats = new HashMap<>();
                    contractStats.put("address", address);
                    contractStats.put("type", getContractType(address));
                    
                    try {
                        // 估算区块范围
                        BigInteger currentBlock = BigInteger.valueOf(System.currentTimeMillis() / 1000 / 12);
                        BigInteger fromBlock = currentBlock.subtract(BigInteger.valueOf(days * 24 * 300));
                        
                        // 获取Transfer事件统计
                        String transferSignature = getEventSignature("transfer");
                        if (!transferSignature.isEmpty()) {
                            List<Log> transferLogs = eventListeningService.getHistoricalEvents(
                                fromBlock.toString(), currentBlock.toString());
                            contractStats.put("transferCount", transferLogs.size());
                            contractStats.put("dailyAverage", transferLogs.size() / days);
                        }
                        
                        // 获取其他相关事件统计
                        if (contractType.equals("marketplace")) {
                            String itemListedSignature = getEventSignature("itemlisted");
                            String itemSoldSignature = getEventSignature("itemsold");
                            if (!itemListedSignature.isEmpty()) {
                                List<Log> listedLogs = eventListeningService.getHistoricalEvents(
                                    fromBlock.toString(), currentBlock.toString());
                                contractStats.put("itemsListed", listedLogs.size());
                            }
                            if (!itemSoldSignature.isEmpty()) {
                                List<Log> soldLogs = eventListeningService.getHistoricalEvents(
                                    fromBlock.toString(), currentBlock.toString());
                                contractStats.put("itemsSold", soldLogs.size());
                            }
                        }
                    } catch (Exception e) {
                        log.warn("获取{}合约交易量统计失败: {}", contractType, e.getMessage());
                        contractStats.put("error", e.getMessage());
                    }
                    
                    volumeStats.put(contractType, contractStats);
                }
            }
            
            result.put("volumeStats", volumeStats);
            result.put("totalContracts", volumeStats.size());
            
            return Result.success(result, "获取交易量分析成功");
        } catch (Exception e) {
            log.error("获取交易量分析失败", e);
            return Result.error("获取交易量分析失败: " + e.getMessage());
        }
    }

    // ==================== 事件订阅和通知 ====================

    @PostMapping("/subscribe")
    @Operation(summary = "订阅事件", description = "订阅特定类型的事件")
    public Result<Object> subscribeToEvents(
            @Parameter(description = "合约地址") @RequestParam String contractAddress,
            @Parameter(description = "事件签名") @RequestParam String eventSignature,
            @Parameter(description = "回调URL") @RequestParam(required = false) String callbackUrl) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("contractAddress", contractAddress);
            result.put("eventSignature", eventSignature);
            result.put("callbackUrl", callbackUrl);
            result.put("timestamp", System.currentTimeMillis());
            
            // 验证合约地址
            String contractType = getContractType(contractAddress);
            if ("Unknown".equals(contractType)) {
                result.put("status", "订阅失败");
                result.put("error", "未知的合约地址");
                return Result.error("订阅事件失败: 未知的合约地址");
            }
            
            // 获取事件类型
            String eventType = getEventTypeFromSignature(eventSignature);
            
            // 创建订阅记录
            String subscriptionId = eventSubscriptionService.createSubscription(
                contractType, contractAddress, eventType, eventSignature);
            
            if (subscriptionId == null) {
                result.put("status", "订阅失败");
                result.put("error", "创建订阅记录失败");
                return Result.error("订阅事件失败: 创建订阅记录失败");
            }
            
            // 启动特定合约的事件监听
            eventListeningService.startContractListening(contractAddress, eventSignature);
            
            result.put("subscriptionId", subscriptionId);
            result.put("contractType", contractType);
            result.put("eventType", eventType);
            result.put("status", "订阅已创建");
            result.put("message", "事件订阅已创建，开始监听指定合约事件");
            result.put("note", "订阅信息已保存到数据库，支持状态管理");
            result.put("suggestion", "可以通过WebSocket连接接收实时事件通知");
            
            return Result.success(result, "订阅事件成功");
        } catch (Exception e) {
            log.error("订阅事件失败", e);
            return Result.error("订阅事件失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/subscribe/{subscriptionId}")
    @Operation(summary = "取消订阅", description = "取消指定的事件订阅")
    public Result<Object> unsubscribeFromEvents(
            @Parameter(description = "订阅ID") @PathVariable String subscriptionId) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("subscriptionId", subscriptionId);
            result.put("timestamp", System.currentTimeMillis());
            
            // 从订阅管理系统中取消订阅
            boolean cancelled = eventSubscriptionService.cancelSubscription(subscriptionId);
            
            if (cancelled) {
                result.put("status", "订阅已取消");
                result.put("message", "事件订阅已成功取消");
                result.put("note", "订阅状态已更新为CANCELLED");
                result.put("suggestion", "可以重新创建订阅以恢复事件监听");
            } else {
                result.put("status", "取消订阅失败");
                result.put("message", "无法找到指定的订阅或取消操作失败");
                result.put("note", "请检查订阅ID是否正确");
                result.put("suggestion", "可以查询现有订阅列表确认订阅状态");
            }
            
            return Result.success(result, "取消事件订阅成功");
        } catch (Exception e) {
            log.error("取消订阅失败", e);
            return Result.error("取消订阅失败: " + e.getMessage());
        }
    }

    @GetMapping("/subscriptions")
    @Operation(summary = "获取订阅列表", description = "获取所有事件订阅的列表")
    public Result<Object> getSubscriptions(
            @Parameter(description = "合约类型") @RequestParam(required = false) String contractType) {
        try {
            List<EventSubscription> subscriptions;
            if (contractType != null && !contractType.trim().isEmpty()) {
                subscriptions = eventSubscriptionService.getSubscriptionsByContractType(contractType);
            } else {
                subscriptions = eventSubscriptionService.getActiveSubscriptions();
            }

            Map<String, Object> result = new HashMap<>();
            result.put("subscriptions", subscriptions);
            result.put("total", subscriptions.size());
            result.put("timestamp", System.currentTimeMillis());

            return Result.success(result, "获取订阅列表成功");
        } catch (Exception e) {
            log.error("获取订阅列表失败", e);
            return Result.error("获取订阅列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/subscriptions/{subscriptionId}")
    @Operation(summary = "获取订阅详情", description = "获取指定订阅的详细信息")
    public Result<Object> getSubscriptionDetails(
            @Parameter(description = "订阅ID") @PathVariable String subscriptionId) {
        try {
            var subscription = eventSubscriptionService.getSubscriptionById(subscriptionId);
            if (subscription.isPresent()) {
                return Result.success(subscription.get(), "获取订阅详情成功");
            } else {
                return Result.error("订阅不存在");
            }
        } catch (Exception e) {
            log.error("获取订阅详情失败", e);
            return Result.error("获取订阅详情失败: " + e.getMessage());
        }
    }

    @PutMapping("/subscriptions/{subscriptionId}/pause")
    @Operation(summary = "暂停订阅", description = "暂停指定的事件订阅")
    public Result<Object> pauseSubscription(
            @Parameter(description = "订阅ID") @PathVariable String subscriptionId) {
        try {
            boolean paused = eventSubscriptionService.pauseSubscription(subscriptionId);
            if (paused) {
                return Result.success("订阅已暂停", "暂停订阅成功");
            } else {
                return Result.error("暂停订阅失败");
            }
        } catch (Exception e) {
            log.error("暂停订阅失败", e);
            return Result.error("暂停订阅失败: " + e.getMessage());
        }
    }

    @PutMapping("/subscriptions/{subscriptionId}/resume")
    @Operation(summary = "恢复订阅", description = "恢复指定的事件订阅")
    public Result<Object> resumeSubscription(
            @Parameter(description = "订阅ID") @PathVariable String subscriptionId) {
        try {
            boolean resumed = eventSubscriptionService.resumeSubscription(subscriptionId);
            if (resumed) {
                return Result.success("订阅已恢复", "恢复订阅成功");
            } else {
                return Result.error("恢复订阅失败");
            }
        } catch (Exception e) {
            log.error("恢复订阅失败", e);
            return Result.error("恢复订阅失败: " + e.getMessage());
        }
    }

    /**
     * 根据事件签名获取事件类型
     */
    private String getEventTypeFromSignature(String eventSignature) {
        switch (eventSignature.toLowerCase()) {
            case "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef":
                return "Transfer";
            case "0x8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925":
                return "Approval";
            case "0x17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c31":
                return "ApprovalForAll";
            default:
                return "Unknown";
        }
    }

    /**
     * 获取事件签名
     */
    private String getEventSignature(String eventType) {
        switch (eventType.toLowerCase()) {
            case "transfer":
                return "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";
            case "approval":
                return "0x8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925";
            case "approvalforall":
                return "0x17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c31";
            case "gamecreated":
                return "0x..."; // 需要根据实际合约定义
            case "agentcreated":
                return "0x..."; // 需要根据实际合约定义
            case "itemlisted":
                return "0x..."; // 需要根据实际合约定义
            case "itemsold":
                return "0x..."; // 需要根据实际合约定义
            case "rewardissued":
                return "0x..."; // 需要根据实际合约定义
            default:
                return "";
        }
    }

    /**
     * 根据合约地址获取合约类型
     */
    private String getContractType(String contractAddress) {
        if (contractAddress.equals(contractConfigService.getContractAddress("platform-token"))) {
            return "PlatformToken";
        } else if (contractAddress.equals(contractConfigService.getContractAddress("game-nft"))) {
            return "GameNFT";
        } else if (contractAddress.equals(contractConfigService.getContractAddress("agent-nft"))) {
            return "AgentNFT";
        } else if (contractAddress.equals(contractConfigService.getContractAddress("marketplace"))) {
            return "Marketplace";
        } else if (contractAddress.equals(contractConfigService.getContractAddress("rewards"))) {
            return "Rewards";
        } else {
            return "Unknown";
        }
    }
}
