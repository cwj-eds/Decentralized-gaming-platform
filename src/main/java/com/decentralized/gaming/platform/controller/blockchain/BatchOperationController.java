package com.decentralized.gaming.platform.controller.blockchain;

import com.decentralized.gaming.platform.common.Result;
import com.decentralized.gaming.platform.entity.BatchOperation;
import com.decentralized.gaming.platform.service.blockchain.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 批量操作控制器
 * 提供批量处理相关的API接口
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@RestController
@RequestMapping("/api/batch")
@Tag(name = "批量操作", description = "批量处理相关的API接口")
public class BatchOperationController {

    @Autowired
    private PlatformTokenService platformTokenService;

    @Autowired
    private GameNFTService gameNFTService;

    @Autowired
    private AgentNFTService agentNFTService;

    @Autowired
    private MarketplaceService marketplaceService;

    @Autowired
    private BatchOperationService batchOperationService;

    // @Autowired
    // private RewardsService rewardsService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    // ==================== 代币批量操作 ====================

    @PostMapping("/token/transfer")
    @Operation(summary = "批量转账代币", description = "批量转账平台代币到多个地址")
    public Result<Object> batchTransferTokens(
            @Parameter(description = "接收地址列表") @RequestParam List<String> recipients,
            @Parameter(description = "转账数量列表") @RequestParam List<BigInteger> amounts,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            if (recipients.size() != amounts.size()) {
                return Result.error("接收地址数量与转账数量不匹配");
            }

            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            List<Map<String, Object>> results = new ArrayList<>();
            List<String> txHashes = new ArrayList<>();
            int successCount = 0;

            for (int i = 0; i < recipients.size(); i++) {
                try {
                    TransactionReceipt receipt = platformTokenService.transfer(credentials, recipients.get(i), amounts.get(i));
                    txHashes.add(receipt.getTransactionHash());
                    successCount++;

                    Map<String, Object> result = new HashMap<>();
                    result.put("recipient", recipients.get(i));
                    result.put("amount", amounts.get(i));
                    result.put("txHash", receipt.getTransactionHash());
                    result.put("success", true);
                    results.add(result);
                } catch (Exception e) {
                    log.error("批量转账代币失败，接收者: {}", recipients.get(i), e);
                    Map<String, Object> result = new HashMap<>();
                    result.put("recipient", recipients.get(i));
                    result.put("amount", amounts.get(i));
                    result.put("success", false);
                    result.put("error", e.getMessage());
                    results.add(result);
                }
            }

            Map<String, Object> batchResult = new HashMap<>();
            batchResult.put("totalCount", recipients.size());
            batchResult.put("successCount", successCount);
            batchResult.put("failureCount", recipients.size() - successCount);
            batchResult.put("txHashes", txHashes);
            batchResult.put("results", results);

            return Result.success(batchResult, "批量转账代币完成");
        } catch (Exception e) {
            log.error("批量转账代币失败", e);
            return Result.error("批量转账代币失败: " + e.getMessage());
        }
    }

    @PostMapping("/token/approve")
    @Operation(summary = "批量授权代币", description = "批量授权平台代币给多个地址")
    public Result<Object> batchApproveTokens(
            @Parameter(description = "被授权者地址列表") @RequestParam List<String> spenders,
            @Parameter(description = "授权数量列表") @RequestParam List<BigInteger> amounts,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            if (spenders.size() != amounts.size()) {
                return Result.error("被授权者地址数量与授权数量不匹配");
            }

            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            List<Map<String, Object>> results = new ArrayList<>();
            List<String> txHashes = new ArrayList<>();
            int successCount = 0;

            for (int i = 0; i < spenders.size(); i++) {
                try {
                    TransactionReceipt receipt = platformTokenService.approve(credentials, spenders.get(i), amounts.get(i));
                    txHashes.add(receipt.getTransactionHash());
                    successCount++;

                    Map<String, Object> result = new HashMap<>();
                    result.put("spender", spenders.get(i));
                    result.put("amount", amounts.get(i));
                    result.put("txHash", receipt.getTransactionHash());
                    result.put("success", true);
                    results.add(result);
                } catch (Exception e) {
                    log.error("批量授权代币失败，被授权者: {}", spenders.get(i), e);
                    Map<String, Object> result = new HashMap<>();
                    result.put("spender", spenders.get(i));
                    result.put("amount", amounts.get(i));
                    result.put("success", false);
                    result.put("error", e.getMessage());
                    results.add(result);
                }
            }

            Map<String, Object> batchResult = new HashMap<>();
            batchResult.put("totalCount", spenders.size());
            batchResult.put("successCount", successCount);
            batchResult.put("failureCount", spenders.size() - successCount);
            batchResult.put("txHashes", txHashes);
            batchResult.put("results", results);

            return Result.success(batchResult, "批量授权代币完成");
        } catch (Exception e) {
            log.error("批量授权代币失败", e);
            return Result.error("批量授权代币失败: " + e.getMessage());
        }
    }

    // ==================== NFT批量操作 ====================

    @PostMapping("/nft/game/transfer")
    @Operation(summary = "批量转账游戏NFT", description = "批量将游戏NFT从发送方转移到接收方")
    public Result<Object> batchTransferGameNFTs(
            @Parameter(description = "发送方地址") @RequestParam String from,
            @Parameter(description = "接收方地址") @RequestParam String to,
            @Parameter(description = "NFT ID列表") @RequestParam List<BigInteger> tokenIds,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            List<Map<String, Object>> results = new ArrayList<>();
            List<String> txHashes = new ArrayList<>();
            int successCount = 0;

            for (BigInteger tokenId : tokenIds) {
                try {
                    TransactionReceipt receipt = gameNFTService.transferFrom(credentials, from, to, tokenId);
                    txHashes.add(receipt.getTransactionHash());
                    successCount++;

                    Map<String, Object> result = new HashMap<>();
                    result.put("tokenId", tokenId);
                    result.put("txHash", receipt.getTransactionHash());
                    result.put("success", true);
                    results.add(result);
                } catch (Exception e) {
                    log.error("批量转账游戏NFT失败，代币ID: {}", tokenId, e);
                    Map<String, Object> result = new HashMap<>();
                    result.put("tokenId", tokenId);
                    result.put("success", false);
                    result.put("error", e.getMessage());
                    results.add(result);
                }
            }

            Map<String, Object> batchResult = new HashMap<>();
            batchResult.put("from", from);
            batchResult.put("to", to);
            batchResult.put("totalCount", tokenIds.size());
            batchResult.put("successCount", successCount);
            batchResult.put("failureCount", tokenIds.size() - successCount);
            batchResult.put("txHashes", txHashes);
            batchResult.put("results", results);

            return Result.success(batchResult, "批量转账游戏NFT完成");
        } catch (Exception e) {
            log.error("批量转账游戏NFT失败", e);
            return Result.error("批量转账游戏NFT失败: " + e.getMessage());
        }
    }

    @PostMapping("/nft/agent/transfer")
    @Operation(summary = "批量转账智能体NFT", description = "批量将智能体NFT从发送方转移到接收方")
    public Result<Object> batchTransferAgentNFTs(
            @Parameter(description = "发送方地址") @RequestParam String from,
            @Parameter(description = "接收方地址") @RequestParam String to,
            @Parameter(description = "NFT ID列表") @RequestParam List<BigInteger> tokenIds,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            List<Map<String, Object>> results = new ArrayList<>();
            List<String> txHashes = new ArrayList<>();
            int successCount = 0;

            for (BigInteger tokenId : tokenIds) {
                try {
                    TransactionReceipt receipt = agentNFTService.transferFrom(credentials, from, to, tokenId);
                    txHashes.add(receipt.getTransactionHash());
                    successCount++;

                    Map<String, Object> result = new HashMap<>();
                    result.put("tokenId", tokenId);
                    result.put("txHash", receipt.getTransactionHash());
                    result.put("success", true);
                    results.add(result);
                } catch (Exception e) {
                    log.error("批量转账智能体NFT失败，代币ID: {}", tokenId, e);
                    Map<String, Object> result = new HashMap<>();
                    result.put("tokenId", tokenId);
                    result.put("success", false);
                    result.put("error", e.getMessage());
                    results.add(result);
                }
            }

            Map<String, Object> batchResult = new HashMap<>();
            batchResult.put("from", from);
            batchResult.put("to", to);
            batchResult.put("totalCount", tokenIds.size());
            batchResult.put("successCount", successCount);
            batchResult.put("failureCount", tokenIds.size() - successCount);
            batchResult.put("txHashes", txHashes);
            batchResult.put("results", results);

            return Result.success(batchResult, "批量转账智能体NFT完成");
        } catch (Exception e) {
            log.error("批量转账智能体NFT失败", e);
            return Result.error("批量转账智能体NFT失败: " + e.getMessage());
        }
    }

    @PostMapping("/nft/game/approve")
    @Operation(summary = "批量授权游戏NFT", description = "批量授权游戏NFT给指定地址")
    public Result<Object> batchApproveGameNFTs(
            @Parameter(description = "被授权者地址") @RequestParam String to,
            @Parameter(description = "NFT ID列表") @RequestParam List<BigInteger> tokenIds,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            List<Map<String, Object>> results = new ArrayList<>();
            List<String> txHashes = new ArrayList<>();
            int successCount = 0;

            for (BigInteger tokenId : tokenIds) {
                try {
                    TransactionReceipt receipt = gameNFTService.approve(credentials, to, tokenId);
                    txHashes.add(receipt.getTransactionHash());
                    successCount++;

                    Map<String, Object> result = new HashMap<>();
                    result.put("tokenId", tokenId);
                    result.put("txHash", receipt.getTransactionHash());
                    result.put("success", true);
                    results.add(result);
                } catch (Exception e) {
                    log.error("批量授权游戏NFT失败，代币ID: {}", tokenId, e);
                    Map<String, Object> result = new HashMap<>();
                    result.put("tokenId", tokenId);
                    result.put("success", false);
                    result.put("error", e.getMessage());
                    results.add(result);
                }
            }

            Map<String, Object> batchResult = new HashMap<>();
            batchResult.put("approved", to);
            batchResult.put("totalCount", tokenIds.size());
            batchResult.put("successCount", successCount);
            batchResult.put("failureCount", tokenIds.size() - successCount);
            batchResult.put("txHashes", txHashes);
            batchResult.put("results", results);

            return Result.success(batchResult, "批量授权游戏NFT完成");
        } catch (Exception e) {
            log.error("批量授权游戏NFT失败", e);
            return Result.error("批量授权游戏NFT失败: " + e.getMessage());
        }
    }

    @PostMapping("/nft/agent/approve")
    @Operation(summary = "批量授权智能体NFT", description = "批量授权智能体NFT给指定地址")
    public Result<Object> batchApproveAgentNFTs(
            @Parameter(description = "被授权者地址") @RequestParam String to,
            @Parameter(description = "NFT ID列表") @RequestParam List<BigInteger> tokenIds,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            List<Map<String, Object>> results = new ArrayList<>();
            List<String> txHashes = new ArrayList<>();
            int successCount = 0;

            for (BigInteger tokenId : tokenIds) {
                try {
                    TransactionReceipt receipt = agentNFTService.approve(credentials, to, tokenId);
                    txHashes.add(receipt.getTransactionHash());
                    successCount++;

                    Map<String, Object> result = new HashMap<>();
                    result.put("tokenId", tokenId);
                    result.put("txHash", receipt.getTransactionHash());
                    result.put("success", true);
                    results.add(result);
                } catch (Exception e) {
                    log.error("批量授权智能体NFT失败，代币ID: {}", tokenId, e);
                    Map<String, Object> result = new HashMap<>();
                    result.put("tokenId", tokenId);
                    result.put("success", false);
                    result.put("error", e.getMessage());
                    results.add(result);
                }
            }

            Map<String, Object> batchResult = new HashMap<>();
            batchResult.put("approved", to);
            batchResult.put("totalCount", tokenIds.size());
            batchResult.put("successCount", successCount);
            batchResult.put("failureCount", tokenIds.size() - successCount);
            batchResult.put("txHashes", txHashes);
            batchResult.put("results", results);

            return Result.success(batchResult, "批量授权智能体NFT完成");
        } catch (Exception e) {
            log.error("批量授权智能体NFT失败", e);
            return Result.error("批量授权智能体NFT失败: " + e.getMessage());
        }
    }

    // ==================== 市场批量操作 ====================

    @PostMapping("/marketplace/list")
    @Operation(summary = "批量上架NFT", description = "批量上架NFT到市场")
    public Result<Object> batchListNFTs(
            @Parameter(description = "NFT合约地址列表") @RequestParam List<String> nftContracts,
            @Parameter(description = "NFT ID列表") @RequestParam List<BigInteger> tokenIds,
            @Parameter(description = "价格列表") @RequestParam List<BigInteger> prices,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            if (nftContracts.size() != tokenIds.size() || tokenIds.size() != prices.size()) {
                return Result.error("NFT合约地址、ID和价格数量不匹配");
            }

            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            List<Map<String, Object>> results = new ArrayList<>();
            List<String> txHashes = new ArrayList<>();
            int successCount = 0;

            for (int i = 0; i < nftContracts.size(); i++) {
                try {
                    TransactionReceipt receipt = marketplaceService.listERC721(credentials, nftContracts.get(i), tokenIds.get(i), prices.get(i));
                    txHashes.add(receipt.getTransactionHash());
                    successCount++;

                    Map<String, Object> result = new HashMap<>();
                    result.put("nftContract", nftContracts.get(i));
                    result.put("tokenId", tokenIds.get(i));
                    result.put("price", prices.get(i));
                    result.put("txHash", receipt.getTransactionHash());
                    result.put("success", true);
                    results.add(result);
                } catch (Exception e) {
                    log.error("批量上架NFT失败，合约: {}, 代币ID: {}", nftContracts.get(i), tokenIds.get(i), e);
                    Map<String, Object> result = new HashMap<>();
                    result.put("nftContract", nftContracts.get(i));
                    result.put("tokenId", tokenIds.get(i));
                    result.put("price", prices.get(i));
                    result.put("success", false);
                    result.put("error", e.getMessage());
                    results.add(result);
                }
            }

            Map<String, Object> batchResult = new HashMap<>();
            batchResult.put("totalCount", nftContracts.size());
            batchResult.put("successCount", successCount);
            batchResult.put("failureCount", nftContracts.size() - successCount);
            batchResult.put("txHashes", txHashes);
            batchResult.put("results", results);

            return Result.success(batchResult, "批量上架NFT完成");
        } catch (Exception e) {
            log.error("批量上架NFT失败", e);
            return Result.error("批量上架NFT失败: " + e.getMessage());
        }
    }

    @PostMapping("/marketplace/cancel")
    @Operation(summary = "批量取消上架", description = "批量取消NFT上架")
    public Result<Object> batchCancelListings(
            @Parameter(description = "上架ID列表") @RequestParam List<BigInteger> listingIds,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            List<Map<String, Object>> results = new ArrayList<>();
            List<String> txHashes = new ArrayList<>();
            int successCount = 0;

            for (BigInteger listingId : listingIds) {
                try {
                    TransactionReceipt receipt = marketplaceService.cancelListing(credentials, listingId);
                    txHashes.add(receipt.getTransactionHash());
                    successCount++;

                    Map<String, Object> result = new HashMap<>();
                    result.put("listingId", listingId);
                    result.put("txHash", receipt.getTransactionHash());
                    result.put("success", true);
                    results.add(result);
                } catch (Exception e) {
                    log.error("批量取消上架失败，上架ID: {}", listingId, e);
                    Map<String, Object> result = new HashMap<>();
                    result.put("listingId", listingId);
                    result.put("success", false);
                    result.put("error", e.getMessage());
                    results.add(result);
                }
            }

            Map<String, Object> batchResult = new HashMap<>();
            batchResult.put("totalCount", listingIds.size());
            batchResult.put("successCount", successCount);
            batchResult.put("failureCount", listingIds.size() - successCount);
            batchResult.put("txHashes", txHashes);
            batchResult.put("results", results);

            return Result.success(batchResult, "批量取消上架完成");
        } catch (Exception e) {
            log.error("批量取消上架失败", e);
            return Result.error("批量取消上架失败: " + e.getMessage());
        }
    }

    // ==================== 异步批量操作 ====================

    @PostMapping("/async/token/transfer")
    @Operation(summary = "异步批量转账代币", description = "异步批量转账平台代币到多个地址")
    public Result<Object> asyncBatchTransferTokens(
            @Parameter(description = "接收地址列表") @RequestParam List<String> recipients,
            @Parameter(description = "转账数量列表") @RequestParam List<BigInteger> amounts,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            if (recipients.size() != amounts.size()) {
                return Result.error("接收地址数量与转账数量不匹配");
            }

            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            List<CompletableFuture<Map<String, Object>>> futures = new ArrayList<>();

            for (int i = 0; i < recipients.size(); i++) {
                final int index = i;
                CompletableFuture<Map<String, Object>> future = CompletableFuture.supplyAsync(() -> {
                    Map<String, Object> result = new HashMap<>();
                    try {
                        TransactionReceipt receipt = platformTokenService.transfer(credentials, recipients.get(index), amounts.get(index));
                        result.put("recipient", recipients.get(index));
                        result.put("amount", amounts.get(index));
                        result.put("txHash", receipt.getTransactionHash());
                        result.put("success", true);
                    } catch (Exception e) {
                        log.error("异步批量转账代币失败，接收者: {}", recipients.get(index), e);
                        result.put("recipient", recipients.get(index));
                        result.put("amount", amounts.get(index));
                        result.put("success", false);
                        result.put("error", e.getMessage());
                    }
                    return result;
                }, executorService);
                futures.add(future);
            }

            // 等待所有异步操作完成
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            allFutures.get(); // 等待完成

            List<Map<String, Object>> results = new ArrayList<>();
            List<String> txHashes = new ArrayList<>();
            int successCount = 0;

            for (CompletableFuture<Map<String, Object>> future : futures) {
                Map<String, Object> result = future.get();
                results.add(result);
                if ((Boolean) result.get("success")) {
                    txHashes.add((String) result.get("txHash"));
                    successCount++;
                }
            }

            Map<String, Object> batchResult = new HashMap<>();
            batchResult.put("totalCount", recipients.size());
            batchResult.put("successCount", successCount);
            batchResult.put("failureCount", recipients.size() - successCount);
            batchResult.put("txHashes", txHashes);
            batchResult.put("results", results);

            return Result.success(batchResult, "异步批量转账代币完成");
        } catch (Exception e) {
            log.error("异步批量转账代币失败", e);
            return Result.error("异步批量转账代币失败: " + e.getMessage());
        }
    }

    // ==================== 批量操作状态查询 ====================

    @GetMapping("/status/{batchId}")
    @Operation(summary = "查询批量操作状态", description = "查询指定批量操作的状态")
    public Result<Object> getBatchOperationStatus(
            @Parameter(description = "批量操作ID") @PathVariable String batchId) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("batchId", batchId);
            result.put("timestamp", System.currentTimeMillis());
            
            // 从数据库获取真实的批量操作状态
            var batchOperation = batchOperationService.getBatchOperationById(batchId);
            if (batchOperation.isPresent()) {
                BatchOperation operation = batchOperation.get();
                
                Map<String, Object> batchStatus = new HashMap<>();
                batchStatus.put("status", operation.getStatus());
                batchStatus.put("progress", operation.getProgress());
                batchStatus.put("totalOperations", operation.getTotalOperations());
                batchStatus.put("completedOperations", operation.getCompletedOperations());
                batchStatus.put("failedOperations", operation.getFailedOperations());
                batchStatus.put("operationType", operation.getOperationType());
                batchStatus.put("createdAt", operation.getCreatedAt());
                batchStatus.put("startTime", operation.getStartTime());
                batchStatus.put("endTime", operation.getEndTime());
                batchStatus.put("updatedAt", operation.getUpdatedAt());
                
                if (operation.getErrorMessage() != null) {
                    batchStatus.put("errorMessage", operation.getErrorMessage());
                }
                
                // 计算持续时间
                if (operation.getStartTime() != null) {
                    LocalDateTime endTime = operation.getEndTime() != null ? 
                        operation.getEndTime() : LocalDateTime.now();
                    long duration = java.time.Duration.between(operation.getStartTime(), endTime).toMillis();
                    batchStatus.put("duration", duration);
                }
                
                result.put("batchStatus", batchStatus);
                result.put("message", "批量操作状态查询成功");
                result.put("note", "状态信息来自数据库存储");
            } else {
                result.put("batchStatus", null);
                result.put("message", "批量操作不存在");
                result.put("note", "请检查批量操作ID是否正确");
            }
            
            return Result.success(result, "查询批量操作状态成功");
        } catch (Exception e) {
            log.error("查询批量操作状态失败", e);
            return Result.error("查询批量操作状态失败: " + e.getMessage());
        }
    }

    /**
     * 将私钥字符串转换为Credentials对象
     */
    @GetMapping("/operations")
    @Operation(summary = "获取批量操作列表", description = "获取所有批量操作的列表")
    public Result<Object> getBatchOperations(
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId) {
        try {
            List<BatchOperation> operations;
            if (userId != null) {
                operations = batchOperationService.getBatchOperationsByUser(userId);
            } else {
                operations = batchOperationService.getAllBatchOperations();
            }

            Map<String, Object> result = new HashMap<>();
            result.put("operations", operations);
            result.put("total", operations.size());
            result.put("timestamp", System.currentTimeMillis());

            return Result.success(result, "获取批量操作列表成功");
        } catch (Exception e) {
            log.error("获取批量操作列表失败", e);
            return Result.error("获取批量操作列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/operations/active")
    @Operation(summary = "获取活跃批量操作", description = "获取所有进行中的批量操作")
    public Result<Object> getActiveBatchOperations() {
        try {
            List<BatchOperation> operations = batchOperationService.getActiveBatchOperations();

            Map<String, Object> result = new HashMap<>();
            result.put("operations", operations);
            result.put("total", operations.size());
            result.put("timestamp", System.currentTimeMillis());

            return Result.success(result, "获取活跃批量操作成功");
        } catch (Exception e) {
            log.error("获取活跃批量操作失败", e);
            return Result.error("获取活跃批量操作失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/operations/{batchId}")
    @Operation(summary = "取消批量操作", description = "取消指定的批量操作")
    public Result<Object> cancelBatchOperation(
            @Parameter(description = "批量操作ID") @PathVariable String batchId) {
        try {
            boolean cancelled = batchOperationService.cancelBatchOperation(batchId);
            if (cancelled) {
                return Result.success("批量操作已取消", "取消批量操作成功");
            } else {
                return Result.error("取消批量操作失败");
            }
        } catch (Exception e) {
            log.error("取消批量操作失败", e);
            return Result.error("取消批量操作失败: " + e.getMessage());
        }
    }

    private Credentials getCredentialsFromPrivateKey(String privateKey) {
        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        return Credentials.create(privateKey);
    }
}
