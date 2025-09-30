package com.decentralized.gaming.platform.controller.blockchain;

import com.decentralized.gaming.platform.common.Result;
import com.decentralized.gaming.platform.service.blockchain.BlockchainService;
import com.decentralized.gaming.platform.service.blockchain.TransactionMonitoringService;
import com.decentralized.gaming.platform.service.blockchain.TransactionHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 交易状态监控控制器
 * 提供交易状态查询和监控相关的API接口
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@RestController
@RequestMapping("/api/transaction")
@Tag(name = "交易状态监控", description = "交易状态查询和监控相关的API接口")
public class TransactionStatusController {

    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private TransactionMonitoringService transactionMonitoringService;

    @Autowired
    private TransactionHistoryService transactionHistoryService;

    // ==================== 交易状态查询 ====================

    @GetMapping("/status/{txHash}")
    @Operation(summary = "查询交易状态", description = "查询指定交易哈希的状态")
    public Result<Object> getTransactionStatus(
            @Parameter(description = "交易哈希") @PathVariable String txHash) {
        try {
            boolean isSuccessful = blockchainService.isTransactionSuccessful(txHash);
            TransactionReceipt receipt = blockchainService.waitForTransactionReceipt(txHash, 30);
            
            Map<String, Object> status = new HashMap<>();
            status.put("txHash", txHash);
            status.put("isSuccessful", isSuccessful);
            status.put("blockNumber", receipt != null ? receipt.getBlockNumber() : null);
            status.put("gasUsed", receipt != null ? receipt.getGasUsed() : null);
            status.put("status", receipt != null ? receipt.getStatus() : null);
            status.put("from", receipt != null ? receipt.getFrom() : null);
            status.put("to", receipt != null ? receipt.getTo() : null);
            status.put("timestamp", System.currentTimeMillis());
            
            return Result.success(status, "查询交易状态成功");
        } catch (Exception e) {
            log.error("查询交易状态失败", e);
            return Result.error("查询交易状态失败: " + e.getMessage());
        }
    }

    @GetMapping("/receipt/{txHash}")
    @Operation(summary = "获取交易收据", description = "获取指定交易哈希的详细收据")
    public Result<Object> getTransactionReceipt(
            @Parameter(description = "交易哈希") @PathVariable String txHash) {
        try {
            TransactionReceipt receipt = blockchainService.waitForTransactionReceipt(txHash, 30);
            
            if (receipt != null) {
                Map<String, Object> receiptInfo = new HashMap<>();
                receiptInfo.put("txHash", receipt.getTransactionHash());
                receiptInfo.put("blockNumber", receipt.getBlockNumber());
                receiptInfo.put("blockHash", receipt.getBlockHash());
                receiptInfo.put("gasUsed", receipt.getGasUsed());
                receiptInfo.put("status", receipt.getStatus());
                receiptInfo.put("from", receipt.getFrom());
                receiptInfo.put("to", receipt.getTo());
                receiptInfo.put("logs", receipt.getLogs());
                receiptInfo.put("timestamp", System.currentTimeMillis());
                
                return Result.success(receiptInfo, "获取交易收据成功");
            } else {
                return Result.error("交易收据未找到");
            }
        } catch (Exception e) {
            log.error("获取交易收据失败", e);
            return Result.error("获取交易收据失败: " + e.getMessage());
        }
    }

    @GetMapping("/details/{txHash}")
    @Operation(summary = "获取交易详情", description = "获取指定交易哈希的详细信息")
    public Result<Object> getTransactionDetails(
            @Parameter(description = "交易哈希") @PathVariable String txHash) {
        try {
            // 这里可以扩展获取更详细的交易信息
            TransactionReceipt receipt = blockchainService.waitForTransactionReceipt(txHash, 30);
            
            Map<String, Object> details = new HashMap<>();
            details.put("txHash", txHash);
            details.put("receipt", receipt);
            details.put("timestamp", System.currentTimeMillis());
            details.put("note", "交易详情查询功能可以进一步扩展");
            
            return Result.success(details, "获取交易详情成功");
        } catch (Exception e) {
            log.error("获取交易详情失败", e);
            return Result.error("获取交易详情失败: " + e.getMessage());
        }
    }

    // ==================== 交易确认监控 ====================

    @GetMapping("/confirmations/{txHash}")
    @Operation(summary = "查询交易确认数", description = "查询指定交易的确认数")
    public Result<Object> getTransactionConfirmations(
            @Parameter(description = "交易哈希") @PathVariable String txHash) {
        try {
            TransactionReceipt receipt = blockchainService.waitForTransactionReceipt(txHash, 30);
            BigInteger currentBlockNumber = blockchainService.getCurrentBlockNumber();
            
            Map<String, Object> confirmations = new HashMap<>();
            if (receipt != null) {
                BigInteger confirmationsCount = currentBlockNumber.subtract(receipt.getBlockNumber());
                confirmations.put("txHash", txHash);
                confirmations.put("blockNumber", receipt.getBlockNumber());
                confirmations.put("currentBlockNumber", currentBlockNumber);
                confirmations.put("confirmations", confirmationsCount);
                confirmations.put("isConfirmed", confirmationsCount.compareTo(BigInteger.valueOf(12)) >= 0); // 12个确认
            } else {
                confirmations.put("txHash", txHash);
                confirmations.put("confirmations", BigInteger.ZERO);
                confirmations.put("isConfirmed", false);
                confirmations.put("status", "交易未找到或未确认");
            }
            
            return Result.success(confirmations, "查询交易确认数成功");
        } catch (Exception e) {
            log.error("查询交易确认数失败", e);
            return Result.error("查询交易确认数失败: " + e.getMessage());
        }
    }

    @PostMapping("/wait-confirmation/{txHash}")
    @Operation(summary = "等待交易确认", description = "等待指定交易达到指定确认数")
    public Result<Object> waitForTransactionConfirmation(
            @Parameter(description = "交易哈希") @PathVariable String txHash,
            @Parameter(description = "目标确认数") @RequestParam(defaultValue = "1") int targetConfirmations,
            @Parameter(description = "最大等待时间(秒)") @RequestParam(defaultValue = "300") int maxWaitTime) {
        try {
            CompletableFuture<Map<String, Object>> future = CompletableFuture.supplyAsync(() -> {
                Map<String, Object> result = new HashMap<>();
                long startTime = System.currentTimeMillis();
                
                while (System.currentTimeMillis() - startTime < maxWaitTime * 1000) {
                    try {
                        TransactionReceipt receipt = blockchainService.waitForTransactionReceipt(txHash, 5);
                        if (receipt != null) {
                            BigInteger currentBlockNumber = blockchainService.getCurrentBlockNumber();
                            BigInteger confirmations = currentBlockNumber.subtract(receipt.getBlockNumber());
                            
                            result.put("txHash", txHash);
                            result.put("blockNumber", receipt.getBlockNumber());
                            result.put("currentBlockNumber", currentBlockNumber);
                            result.put("confirmations", confirmations);
                            result.put("targetConfirmations", targetConfirmations);
                            result.put("isConfirmed", confirmations.compareTo(BigInteger.valueOf(targetConfirmations)) >= 0);
                            result.put("waitTime", System.currentTimeMillis() - startTime);
                            
                            if (confirmations.compareTo(BigInteger.valueOf(targetConfirmations)) >= 0) {
                                result.put("status", "交易已确认");
                                return result;
                            }
                        }
                        
                        Thread.sleep(5000); // 等待5秒后重试
                    } catch (Exception e) {
                        log.error("等待交易确认时出错", e);
                        result.put("error", e.getMessage());
                        return result;
                    }
                }
                
                result.put("status", "等待超时");
                result.put("waitTime", maxWaitTime * 1000);
                return result;
            });
            
            Map<String, Object> result = future.get();
            return Result.success(result, "等待交易确认完成");
        } catch (Exception e) {
            log.error("等待交易确认失败", e);
            return Result.error("等待交易确认失败: " + e.getMessage());
        }
    }

    // ==================== 批量交易状态查询 ====================

    @PostMapping("/batch/status")
    @Operation(summary = "批量查询交易状态", description = "批量查询多个交易的状态")
    public Result<Object> getBatchTransactionStatus(
            @Parameter(description = "交易哈希列表") @RequestParam java.util.List<String> txHashes) {
        try {
            java.util.List<Map<String, Object>> results = new java.util.ArrayList<>();
            int successCount = 0;
            
            for (String txHash : txHashes) {
                try {
                    boolean isSuccessful = blockchainService.isTransactionSuccessful(txHash);
                    TransactionReceipt receipt = blockchainService.waitForTransactionReceipt(txHash, 10);
                    
                    Map<String, Object> status = new HashMap<>();
                    status.put("txHash", txHash);
                    status.put("isSuccessful", isSuccessful);
                    status.put("blockNumber", receipt != null ? receipt.getBlockNumber() : null);
                    status.put("gasUsed", receipt != null ? receipt.getGasUsed() : null);
                    status.put("status", receipt != null ? receipt.getStatus() : null);
                    status.put("found", receipt != null);
                    
                    results.add(status);
                    if (isSuccessful) successCount++;
                } catch (Exception e) {
                    log.error("查询交易状态失败，交易哈希: {}", txHash, e);
                    Map<String, Object> status = new HashMap<>();
                    status.put("txHash", txHash);
                    status.put("error", e.getMessage());
                    status.put("found", false);
                    results.add(status);
                }
            }
            
            Map<String, Object> batchResult = new HashMap<>();
            batchResult.put("totalCount", txHashes.size());
            batchResult.put("successCount", successCount);
            batchResult.put("failureCount", txHashes.size() - successCount);
            batchResult.put("results", results);
            
            return Result.success(batchResult, "批量查询交易状态完成");
        } catch (Exception e) {
            log.error("批量查询交易状态失败", e);
            return Result.error("批量查询交易状态失败: " + e.getMessage());
        }
    }

    // ==================== 交易历史查询 ====================

    @GetMapping("/history/{address}")
    @Operation(summary = "查询地址交易历史", description = "查询指定地址的交易历史")
    public Result<Object> getAddressTransactionHistory(
            @Parameter(description = "钱包地址") @PathVariable String address,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        try {
            // 计算区块范围
            BigInteger currentBlock = BigInteger.valueOf(System.currentTimeMillis() / 1000 / 12);
            BigInteger fromBlock = currentBlock.subtract(BigInteger.valueOf(1000));
            
            List<Transaction> transactions = transactionHistoryService.getAddressTransactionHistory(address, fromBlock.longValue(), currentBlock.longValue());
            
            // 分页处理
            int startIndex = (page - 1) * size;
            int endIndex = Math.min(startIndex + size, transactions.size());
            List<Transaction> pagedTransactions = transactions.subList(startIndex, endIndex);
            
            Map<String, Object> result = new HashMap<>();
            result.put("transactions", pagedTransactions);
            result.put("total", transactions.size());
            result.put("page", page);
            result.put("size", size);
            
            return Result.success(result, "查询地址交易历史成功");
        } catch (Exception e) {
            log.error("查询地址交易历史失败", e);
            return Result.error("查询地址交易历史失败: " + e.getMessage());
        }
    }

    @GetMapping("/history/{address}/contract/{contractAddress}")
    @Operation(summary = "查询地址与合约的交互历史", description = "查询指定地址与特定合约的交互历史")
    public Result<Object> getContractInteractionHistory(
            @Parameter(description = "钱包地址") @PathVariable String address,
            @Parameter(description = "合约地址") @PathVariable String contractAddress) {
        try {
            // 计算区块范围
            BigInteger currentBlock = BigInteger.valueOf(System.currentTimeMillis() / 1000 / 12);
            BigInteger fromBlock = currentBlock.subtract(BigInteger.valueOf(1000));
            
            List<Transaction> transactions = transactionHistoryService.getContractInteractionHistory(contractAddress, fromBlock.longValue(), currentBlock.longValue());
            
            Map<String, Object> result = new HashMap<>();
            result.put("transactions", transactions);
            result.put("total", transactions.size());
            result.put("contractAddress", contractAddress);
            result.put("address", address);
            
            return Result.success(result, "查询合约交互历史成功");
        } catch (Exception e) {
            log.error("查询合约交互历史失败", e);
            return Result.error("查询合约交互历史失败: " + e.getMessage());
        }
    }

    @GetMapping("/history/{address}/nft")
    @Operation(summary = "查询地址的NFT交易历史", description = "查询指定地址的NFT相关交易历史")
    public Result<Object> getNFTTransactionHistory(
            @Parameter(description = "钱包地址") @PathVariable String address) {
        try {
            // 这里需要指定NFT合约地址，暂时使用空字符串
            List<Transaction> transactions = transactionHistoryService.getNFTTransactionHistory("", address);
            
            Map<String, Object> result = new HashMap<>();
            result.put("transactions", transactions);
            result.put("total", transactions.size());
            result.put("address", address);
            
            return Result.success(result, "查询NFT交易历史成功");
        } catch (Exception e) {
            log.error("查询NFT交易历史失败", e);
            return Result.error("查询NFT交易历史失败: " + e.getMessage());
        }
    }

    // ==================== 交易监控设置 ====================

    @PostMapping("/monitor/start")
    @Operation(summary = "开始交易监控", description = "开始监控指定交易的状态变化")
    public Result<Object> startTransactionMonitoring(
            @Parameter(description = "交易哈希") @RequestParam String txHash,
            @Parameter(description = "监控间隔(秒)") @RequestParam(defaultValue = "10") int interval) {
        try {
            // 开始监控交易
            transactionMonitoringService.startMonitoring(txHash, new TransactionMonitoringService.MonitoringCallback() {
                @Override
                public void onConfirmed(String transactionHash, long blockNumber) {
                    log.info("交易 {} 已确认，区块号: {}", transactionHash, blockNumber);
                }

                @Override
                public void onFailed(String transactionHash, String error) {
                    log.error("交易 {} 失败: {}", transactionHash, error);
                }

                @Override
                public void onTimeout(String transactionHash) {
                    log.warn("交易 {} 监控超时", transactionHash);
                }
            });

            Map<String, Object> result = new HashMap<>();
            result.put("txHash", txHash);
            result.put("interval", interval);
            result.put("status", "监控已启动");
            result.put("timestamp", System.currentTimeMillis());
            result.put("message", "交易监控已启动，将定期检查交易状态");
            
            return Result.success(result, "开始交易监控成功");
        } catch (Exception e) {
            log.error("开始交易监控失败", e);
            return Result.error("开始交易监控失败: " + e.getMessage());
        }
    }

    @PostMapping("/monitor/stop")
    @Operation(summary = "停止交易监控", description = "停止监控指定交易")
    public Result<Object> stopTransactionMonitoring(
            @Parameter(description = "交易哈希") @RequestParam String txHash) {
        try {
            // 停止监控交易
            transactionMonitoringService.stopMonitoring(txHash);

            Map<String, Object> result = new HashMap<>();
            result.put("txHash", txHash);
            result.put("status", "监控已停止");
            result.put("timestamp", System.currentTimeMillis());
            result.put("message", "交易监控已停止");
            
            return Result.success(result, "停止交易监控成功");
        } catch (Exception e) {
            log.error("停止交易监控失败", e);
            return Result.error("停止交易监控失败: " + e.getMessage());
        }
    }

    // ==================== 网络状态查询 ====================

    @GetMapping("/network/status")
    @Operation(summary = "查询网络状态", description = "查询区块链网络的状态信息")
    public Result<Object> getNetworkStatus() {
        try {
            BigInteger blockNumber = blockchainService.getCurrentBlockNumber();
            boolean isConnected = blockchainService.isConnected();
            
            Map<String, Object> networkStatus = new HashMap<>();
            networkStatus.put("blockNumber", blockNumber);
            networkStatus.put("isConnected", isConnected);
            networkStatus.put("status", isConnected ? "网络连接正常" : "网络连接异常");
            networkStatus.put("timestamp", System.currentTimeMillis());
            
            return Result.success(networkStatus, "查询网络状态成功");
        } catch (Exception e) {
            log.error("查询网络状态失败", e);
            return Result.error("查询网络状态失败: " + e.getMessage());
        }
    }

    @GetMapping("/network/gas-price")
    @Operation(summary = "查询Gas价格", description = "查询当前网络的Gas价格")
    public Result<Object> getGasPrice() {
        try {
            Map<String, Object> gasInfo = new HashMap<>();
            
            // 获取当前Gas价格
            try {
                BigInteger gasPrice = blockchainService.getGasPrice();
                gasInfo.put("gasPrice", gasPrice);
                gasInfo.put("gasPriceGwei", gasPrice.divide(BigInteger.valueOf(1000000000))); // 转换为Gwei
                gasInfo.put("gasPriceWei", gasPrice.toString());
            } catch (Exception e) {
                log.warn("获取Gas价格失败: {}", e.getMessage());
                gasInfo.put("gasPriceError", e.getMessage());
                // 提供默认值
                gasInfo.put("gasPrice", BigInteger.valueOf(20000000000L)); // 20 Gwei
                gasInfo.put("gasPriceGwei", 20);
                gasInfo.put("gasPriceWei", "20000000000");
            }
            
            // 获取网络信息
            try {
                BigInteger blockNumber = blockchainService.getCurrentBlockNumber();
                boolean isConnected = blockchainService.isConnected();
                gasInfo.put("blockNumber", blockNumber);
                gasInfo.put("isConnected", isConnected);
            } catch (Exception e) {
                log.warn("获取网络信息失败: {}", e.getMessage());
                gasInfo.put("networkError", e.getMessage());
            }
            
            gasInfo.put("timestamp", System.currentTimeMillis());
            gasInfo.put("unit", "wei");
            gasInfo.put("note", "Gas价格实时获取，建议根据网络拥堵情况调整");
            
            return Result.success(gasInfo, "查询Gas价格成功");
        } catch (Exception e) {
            log.error("查询Gas价格失败", e);
            return Result.error("查询Gas价格失败: " + e.getMessage());
        }
    }
}
