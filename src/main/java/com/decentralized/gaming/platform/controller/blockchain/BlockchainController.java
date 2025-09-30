package com.decentralized.gaming.platform.controller.blockchain;

import com.decentralized.gaming.platform.common.Result;
import com.decentralized.gaming.platform.service.blockchain.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * 区块链功能控制器
 * 提供区块链相关的API接口
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@RestController
@RequestMapping("/api/blockchain")
@Tag(name = "区块链功能", description = "区块链相关的API接口")
public class BlockchainController {

    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private PlatformTokenService platformTokenService;

    @Autowired
    private GameNFTService gameNFTService;

    @Autowired
    private AgentNFTService agentNFTService;

    @Autowired
    private MarketplaceService marketplaceService;

    @Autowired
    private RewardsService rewardsService;

    @Autowired
    private EventListeningService eventListeningService;

    @Autowired
    private ContractConfigService contractConfigService;

    @Autowired
    private BlockchainMetricsService metricsService;

    @Autowired
    private BlockchainCacheService cacheService;

    // ==================== 基础功能 ====================

    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查区块链服务是否正常运行")
    public Result<Object> healthCheck() {
        long startTime = System.currentTimeMillis();
        try {
            metricsService.recordApiRequest("blockchain/health", true);
            
            Map<String, Object> healthInfo = new HashMap<>();
            healthInfo.put("status", "OK");
            healthInfo.put("message", "区块链服务运行正常");
            healthInfo.put("networkConnected", blockchainService.isConnected());
            healthInfo.put("currentBlockNumber", blockchainService.getCurrentBlockNumber());
            healthInfo.put("gasPrice", blockchainService.getGasPrice());
            healthInfo.put("cacheStats", cacheService.getCacheStats());
            healthInfo.put("metrics", metricsService.getHealthStatus());
            healthInfo.put("timestamp", System.currentTimeMillis());
            
            long responseTime = System.currentTimeMillis() - startTime;
            metricsService.recordResponseTime("blockchain/health", responseTime);
            
            return Result.success(healthInfo, "区块链服务运行正常");
        } catch (Exception e) {
            log.error("健康检查失败", e);
            metricsService.recordApiError("blockchain/health", "HEALTH_CHECK_FAILED");
            return Result.error("区块链服务异常: " + e.getMessage());
        }
    }

    @GetMapping("/network/info")
    @Operation(summary = "获取区块链网络信息", description = "获取当前连接的区块链网络信息")
    public Result<Object> getNetworkInfo() {
        try {
            BigInteger blockNumber = blockchainService.getCurrentBlockNumber();
            boolean isConnected = blockchainService.isConnected();
            
            java.util.Map<String, Object> networkInfo = new java.util.HashMap<>();
            networkInfo.put("blockNumber", blockNumber);
            networkInfo.put("isConnected", isConnected);
            networkInfo.put("status", "区块链网络连接正常");
            
            return Result.success(networkInfo, "获取网络信息成功");
        } catch (Exception e) {
            log.error("获取网络信息失败", e);
            return Result.error("获取网络信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/account/{address}/balance")
    @Operation(summary = "获取账户余额", description = "获取指定地址的ETH余额")
    public Result<Object> getAccountBalance(
            @Parameter(description = "钱包地址") @PathVariable String address) {
        try {
            java.math.BigDecimal balance = blockchainService.getBalance(address);
            return Result.success(balance, "获取余额成功");
        } catch (Exception e) {
            log.error("获取账户余额失败", e);
            return Result.error("获取账户余额失败: " + e.getMessage());
        }
    }

    @PostMapping("/signature/verify")
    @Operation(summary = "验证签名", description = "验证钱包地址的签名")
    public Result<Boolean> verifySignature(
            @Parameter(description = "原始消息") @RequestParam String message,
            @Parameter(description = "签名") @RequestParam String signature,
            @Parameter(description = "钱包地址") @RequestParam String address) {
        try {
            boolean isValid = blockchainService.verifySignature(message, signature, address);
            return Result.success(isValid, "签名验证完成");
        } catch (Exception e) {
            log.error("签名验证失败", e);
            return Result.error("签名验证失败: " + e.getMessage());
        }
    }

    @GetMapping("/contracts/info")
    @Operation(summary = "获取合约信息", description = "获取所有智能合约的基本信息")
    public Result<Object> getContractsInfo() {
        try {
            java.util.Map<String, Object> contractsInfo = contractConfigService.getAllContractsInfo();
            return Result.success(contractsInfo, "获取合约信息成功");
        } catch (Exception e) {
            log.error("获取合约信息失败", e);
            return Result.error("获取合约信息失败: " + e.getMessage());
        }
    }

    // ==================== 平台代币功能 ====================

    @GetMapping("/token/info")
    @Operation(summary = "获取代币信息", description = "获取平台代币的基本信息")
    public Result<Object> getTokenInfo() {
        try {
            java.util.Map<String, Object> tokenInfo = contractConfigService.getPlatformTokenInfo();
            return Result.success(tokenInfo, "获取代币信息成功");
        } catch (Exception e) {
            log.error("获取代币信息失败", e);
            return Result.error("获取代币信息失败: " + e.getMessage());
        }
    }

    // ==================== 游戏NFT功能 ====================

    @GetMapping("/game/info")
    @Operation(summary = "获取游戏NFT信息", description = "获取游戏NFT合约的基本信息")
    public Result<Object> getGameInfo() {
        try {
            java.util.Map<String, Object> gameInfo = contractConfigService.getGameNFTInfo();
            return Result.success(gameInfo, "获取游戏NFT信息成功");
        } catch (Exception e) {
            log.error("获取游戏NFT信息失败", e);
            return Result.error("获取游戏NFT信息失败: " + e.getMessage());
        }
    }

    // ==================== 智能体NFT功能 ====================

    @GetMapping("/agent/info")
    @Operation(summary = "获取智能体NFT信息", description = "获取智能体NFT合约的基本信息")
    public Result<Object> getAgentInfo() {
        try {
            java.util.Map<String, Object> agentInfo = contractConfigService.getAgentNFTInfo();
            return Result.success(agentInfo, "获取智能体NFT信息成功");
        } catch (Exception e) {
            log.error("获取智能体NFT信息失败", e);
            return Result.error("获取智能体NFT信息失败: " + e.getMessage());
        }
    }

    // ==================== 市场功能 ====================

    @GetMapping("/marketplace/info")
    @Operation(summary = "获取市场信息", description = "获取市场合约的基本信息")
    public Result<Object> getMarketplaceInfo() {
        try {
            java.util.Map<String, Object> marketplaceInfo = contractConfigService.getMarketplaceInfo();
            return Result.success(marketplaceInfo, "获取市场信息成功");
        } catch (Exception e) {
            log.error("获取市场信息失败", e);
            return Result.error("获取市场信息失败: " + e.getMessage());
        }
    }

    // ==================== 奖励功能 ====================

    @GetMapping("/rewards/info")
    @Operation(summary = "获取奖励信息", description = "获取奖励合约的基本信息")
    public Result<Object> getRewardsInfo() {
        try {
            java.util.Map<String, Object> rewardsInfo = contractConfigService.getRewardsInfo();
            return Result.success(rewardsInfo, "获取奖励信息成功");
        } catch (Exception e) {
            log.error("获取奖励信息失败", e);
            return Result.error("获取奖励信息失败: " + e.getMessage());
        }
    }

    // ==================== NFT转账功能 ====================

    @PostMapping("/game/transfer")
    @Operation(summary = "转账游戏NFT", description = "转账游戏NFT到指定地址")
    public Result<String> transferGameNFT(
            @Parameter(description = "发送方地址") @RequestParam String from,
            @Parameter(description = "接收方地址") @RequestParam String to,
            @Parameter(description = "NFT ID") @RequestParam BigInteger tokenId,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            org.web3j.crypto.Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            org.web3j.protocol.core.methods.response.TransactionReceipt receipt = gameNFTService.transferFrom(credentials, from, to, tokenId);
            return Result.success(receipt.getTransactionHash(), "转账游戏NFT成功");
        } catch (Exception e) {
            log.error("转账游戏NFT失败", e);
            return Result.error("转账游戏NFT失败: " + e.getMessage());
        }
    }

    @PostMapping("/agent/transfer")
    @Operation(summary = "转账智能体NFT", description = "转账智能体NFT到指定地址")
    public Result<String> transferAgentNFT(
            @Parameter(description = "发送方地址") @RequestParam String from,
            @Parameter(description = "接收方地址") @RequestParam String to,
            @Parameter(description = "NFT ID") @RequestParam BigInteger tokenId,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            org.web3j.crypto.Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            org.web3j.protocol.core.methods.response.TransactionReceipt receipt = agentNFTService.transferFrom(credentials, from, to, tokenId);
            return Result.success(receipt.getTransactionHash(), "转账智能体NFT成功");
        } catch (Exception e) {
            log.error("转账智能体NFT失败", e);
            return Result.error("转账智能体NFT失败: " + e.getMessage());
        }
    }

    // ==================== NFT授权功能 ====================

    @PostMapping("/game/approve")
    @Operation(summary = "授权游戏NFT", description = "授权指定地址使用游戏NFT")
    public Result<String> approveGameNFT(
            @Parameter(description = "授权地址") @RequestParam String to,
            @Parameter(description = "NFT ID") @RequestParam BigInteger tokenId,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            org.web3j.crypto.Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            org.web3j.protocol.core.methods.response.TransactionReceipt receipt = gameNFTService.approve(credentials, to, tokenId);
            return Result.success(receipt.getTransactionHash(), "授权游戏NFT成功");
        } catch (Exception e) {
            log.error("授权游戏NFT失败", e);
            return Result.error("授权游戏NFT失败: " + e.getMessage());
        }
    }

    @PostMapping("/agent/approve")
    @Operation(summary = "授权智能体NFT", description = "授权指定地址使用智能体NFT")
    public Result<String> approveAgentNFT(
            @Parameter(description = "授权地址") @RequestParam String to,
            @Parameter(description = "NFT ID") @RequestParam BigInteger tokenId,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            org.web3j.crypto.Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            org.web3j.protocol.core.methods.response.TransactionReceipt receipt = agentNFTService.approve(credentials, to, tokenId);
            return Result.success(receipt.getTransactionHash(), "授权智能体NFT成功");
        } catch (Exception e) {
            log.error("授权智能体NFT失败", e);
            return Result.error("授权智能体NFT失败: " + e.getMessage());
        }
    }

    @PostMapping("/game/approve-all")
    @Operation(summary = "批量授权游戏NFT", description = "授权指定地址使用所有游戏NFT")
    public Result<String> setApprovalForAllGameNFT(
            @Parameter(description = "授权地址") @RequestParam String operator,
            @Parameter(description = "是否授权") @RequestParam boolean approved,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            org.web3j.crypto.Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            org.web3j.protocol.core.methods.response.TransactionReceipt receipt = gameNFTService.setApprovalForAll(credentials, operator, approved);
            return Result.success(receipt.getTransactionHash(), "批量授权游戏NFT成功");
        } catch (Exception e) {
            log.error("批量授权游戏NFT失败", e);
            return Result.error("批量授权游戏NFT失败: " + e.getMessage());
        }
    }

    @PostMapping("/agent/approve-all")
    @Operation(summary = "批量授权智能体NFT", description = "授权指定地址使用所有智能体NFT")
    public Result<String> setApprovalForAllAgentNFT(
            @Parameter(description = "授权地址") @RequestParam String operator,
            @Parameter(description = "是否授权") @RequestParam boolean approved,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            org.web3j.crypto.Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            org.web3j.protocol.core.methods.response.TransactionReceipt receipt = agentNFTService.setApprovalForAll(credentials, operator, approved);
            return Result.success(receipt.getTransactionHash(), "批量授权智能体NFT成功");
        } catch (Exception e) {
            log.error("批量授权智能体NFT失败", e);
            return Result.error("批量授权智能体NFT失败: " + e.getMessage());
        }
    }

    // ==================== 批量操作功能 ====================

    @PostMapping("/batch/transfer-tokens")
    @Operation(summary = "批量转账代币", description = "批量转账平台代币到多个地址")
    public Result<Object> batchTransferTokens(
            @Parameter(description = "接收地址列表") @RequestParam java.util.List<String> recipients,
            @Parameter(description = "转账数量列表") @RequestParam java.util.List<BigInteger> amounts,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            org.web3j.crypto.Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            java.util.List<String> txHashes = new java.util.ArrayList<>();
            
            for (int i = 0; i < recipients.size() && i < amounts.size(); i++) {
                org.web3j.protocol.core.methods.response.TransactionReceipt receipt = 
                    platformTokenService.transfer(credentials, recipients.get(i), amounts.get(i));
                txHashes.add(receipt.getTransactionHash());
            }
            
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("txHashes", txHashes);
            result.put("count", txHashes.size());
            
            return Result.success(result, "批量转账代币成功");
        } catch (Exception e) {
            log.error("批量转账代币失败", e);
            return Result.error("批量转账代币失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/issue-rewards")
    @Operation(summary = "批量发放奖励", description = "向多个地址批量发放平台代币奖励")
    public Result<String> batchIssueRewards(
            @Parameter(description = "接收地址列表") @RequestParam java.util.List<String> recipients,
            @Parameter(description = "奖励数量列表") @RequestParam java.util.List<BigInteger> amounts,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            org.web3j.crypto.Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            org.web3j.protocol.core.methods.response.TransactionReceipt receipt = 
                rewardsService.batchIssue(credentials, recipients, amounts);
            return Result.success(receipt.getTransactionHash(), "批量发放奖励成功");
        } catch (Exception e) {
            log.error("批量发放奖励失败", e);
            return Result.error("批量发放奖励失败: " + e.getMessage());
        }
    }

    // ==================== 交易状态查询 ====================

    @GetMapping("/transaction/{txHash}/status")
    @Operation(summary = "查询交易状态", description = "查询指定交易哈希的状态")
    public Result<Object> getTransactionStatus(
            @Parameter(description = "交易哈希") @PathVariable String txHash) {
        try {
            boolean isSuccessful = blockchainService.isTransactionSuccessful(txHash);
            org.web3j.protocol.core.methods.response.TransactionReceipt receipt = 
                blockchainService.waitForTransactionReceipt(txHash, 30);
            
            java.util.Map<String, Object> status = new java.util.HashMap<>();
            status.put("txHash", txHash);
            status.put("isSuccessful", isSuccessful);
            status.put("blockNumber", receipt != null ? receipt.getBlockNumber() : null);
            status.put("gasUsed", receipt != null ? receipt.getGasUsed() : null);
            status.put("status", receipt != null ? receipt.getStatus() : null);
            
            return Result.success(status, "查询交易状态成功");
        } catch (Exception e) {
            log.error("查询交易状态失败", e);
            return Result.error("查询交易状态失败: " + e.getMessage());
        }
    }

    @GetMapping("/transaction/{txHash}/receipt")
    @Operation(summary = "获取交易收据", description = "获取指定交易哈希的详细收据")
    public Result<Object> getTransactionReceipt(
            @Parameter(description = "交易哈希") @PathVariable String txHash) {
        try {
            org.web3j.protocol.core.methods.response.TransactionReceipt receipt = 
                blockchainService.waitForTransactionReceipt(txHash, 30);
            
            if (receipt != null) {
                java.util.Map<String, Object> receiptInfo = new java.util.HashMap<>();
                receiptInfo.put("txHash", receipt.getTransactionHash());
                receiptInfo.put("blockNumber", receipt.getBlockNumber());
                receiptInfo.put("gasUsed", receipt.getGasUsed());
                receiptInfo.put("status", receipt.getStatus());
                receiptInfo.put("from", receipt.getFrom());
                receiptInfo.put("to", receipt.getTo());
                receiptInfo.put("logs", receipt.getLogs());
                
                return Result.success(receiptInfo, "获取交易收据成功");
            } else {
                return Result.error("交易收据未找到");
            }
        } catch (Exception e) {
            log.error("获取交易收据失败", e);
            return Result.error("获取交易收据失败: " + e.getMessage());
        }
    }

    // ==================== 事件监听功能 ====================

    @PostMapping("/events/start")
    @Operation(summary = "开始事件监听", description = "开始监听智能合约事件")
    public Result<String> startEventListening() {
        try {
            eventListeningService.startListening();
            return Result.success("事件监听已启动", "开始监听智能合约事件");
        } catch (Exception e) {
            log.error("启动事件监听失败", e);
            return Result.error("启动事件监听失败: " + e.getMessage());
        }
    }

    @PostMapping("/events/stop")
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

    @GetMapping("/events/status")
    @Operation(summary = "获取事件监听状态", description = "获取事件监听服务的状态")
    public Result<Object> getEventListeningStatus() {
        try {
            java.util.Map<String, Object> status = new java.util.HashMap<>();
            status.put("isListening", eventListeningService.isListening());
            status.put("timestamp", System.currentTimeMillis());
            
            return Result.success(status, "获取事件监听状态成功");
        } catch (Exception e) {
            log.error("获取事件监听状态失败", e);
            return Result.error("获取事件监听状态失败: " + e.getMessage());
        }
    }

    @GetMapping("/events/historical")
    @Operation(summary = "获取历史事件", description = "获取指定合约的历史事件")
    public Result<Object> getHistoricalEvents(
            @Parameter(description = "合约地址") @RequestParam String contractAddress,
            @Parameter(description = "事件签名") @RequestParam String eventSignature,
            @Parameter(description = "起始区块") @RequestParam BigInteger fromBlock,
            @Parameter(description = "结束区块") @RequestParam BigInteger toBlock) {
        try {
            java.util.List<org.web3j.protocol.core.methods.response.Log> logs = 
                eventListeningService.getHistoricalEvents(fromBlock.toString(), toBlock.toString());
            
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("events", logs);
            result.put("count", logs.size());
            result.put("fromBlock", fromBlock);
            result.put("toBlock", toBlock);
            
            return Result.success(result, "获取历史事件成功");
        } catch (Exception e) {
            log.error("获取历史事件失败", e);
            return Result.error("获取历史事件失败: " + e.getMessage());
        }
    }

    // ==================== 服务状态 ====================

    @GetMapping("/services/status")
    @Operation(summary = "获取服务状态", description = "获取所有区块链服务的状态")
    public Result<Object> getServicesStatus() {
        try {
            java.util.Map<String, Object> servicesStatus = new java.util.HashMap<>();
            
            // 检查区块链服务连接状态
            boolean blockchainConnected = blockchainService.isConnected();
            servicesStatus.put("blockchainService", blockchainConnected ? "运行正常" : "连接异常");
            
            // 检查合约部署状态
            servicesStatus.put("platformTokenService", contractConfigService.isContractDeployed("platform-token") ? "运行正常" : "合约未部署");
            servicesStatus.put("gameNFTService", contractConfigService.isContractDeployed("game-nft") ? "运行正常" : "合约未部署");
            servicesStatus.put("agentNFTService", contractConfigService.isContractDeployed("agent-nft") ? "运行正常" : "合约未部署");
            servicesStatus.put("marketplaceService", contractConfigService.isContractDeployed("marketplace") ? "运行正常" : "合约未部署");
            servicesStatus.put("rewardsService", contractConfigService.isContractDeployed("rewards") ? "运行正常" : "合约未部署");
            
            // 事件监听服务状态
            servicesStatus.put("eventListeningService", eventListeningService.isListening() ? "监听中" : "已停止");
            
            // 合约配置服务状态
            servicesStatus.put("contractConfigService", "运行正常");
            
            // 系统信息
            servicesStatus.put("timestamp", System.currentTimeMillis());
            servicesStatus.put("networkName", contractConfigService.getAllContractsInfo().get("network"));
            
            return Result.success(servicesStatus, "获取服务状态成功");
        } catch (Exception e) {
            log.error("获取服务状态失败", e);
            return Result.error("获取服务状态失败: " + e.getMessage());
        }
    }

    // ==================== 监控和指标 ====================

    @GetMapping("/metrics")
    @Operation(summary = "获取区块链指标", description = "获取区块链服务的性能指标和统计信息")
    public Result<Object> getMetrics() {
        try {
            Map<String, Object> metrics = metricsService.getMetrics();
            return Result.success(metrics, "获取指标成功");
        } catch (Exception e) {
            log.error("获取指标失败", e);
            return Result.error("获取指标失败: " + e.getMessage());
        }
    }

    @GetMapping("/cache/stats")
    @Operation(summary = "获取缓存统计", description = "获取区块链缓存的使用统计")
    public Result<Object> getCacheStats() {
        try {
            BlockchainCacheService.CacheStats stats = cacheService.getCacheStats();
            return Result.success(stats, "获取缓存统计成功");
        } catch (Exception e) {
            log.error("获取缓存统计失败", e);
            return Result.error("获取缓存统计失败: " + e.getMessage());
        }
    }

    @PostMapping("/cache/clear")
    @Operation(summary = "清除缓存", description = "清除所有区块链缓存")
    public Result<String> clearCache() {
        try {
            cacheService.clearAllCache();
            return Result.success("缓存清除成功", "所有区块链缓存已清除");
        } catch (Exception e) {
            log.error("清除缓存失败", e);
            return Result.error("清除缓存失败: " + e.getMessage());
        }
    }

    @PostMapping("/metrics/reset")
    @Operation(summary = "重置指标", description = "重置所有区块链指标统计")
    public Result<String> resetMetrics() {
        try {
            metricsService.resetMetrics();
            return Result.success("指标重置成功", "所有区块链指标已重置");
        } catch (Exception e) {
            log.error("重置指标失败", e);
            return Result.error("重置指标失败: " + e.getMessage());
        }
    }

    /**
     * 将私钥字符串转换为Credentials对象
     */
    private org.web3j.crypto.Credentials getCredentialsFromPrivateKey(String privateKey) {
        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        return org.web3j.crypto.Credentials.create(privateKey);
    }
}