package com.decentralized.gaming.platform.controller.blockchain;

import com.decentralized.gaming.platform.common.Result;
import com.decentralized.gaming.platform.service.blockchain.AgentNFTService;
import com.decentralized.gaming.platform.service.blockchain.ContractConfigService;
import com.decentralized.gaming.platform.service.blockchain.EventListeningService;
import com.decentralized.gaming.platform.service.blockchain.GameNFTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * NFT转账控制器
 * 提供游戏NFT和智能体NFT的转账相关API
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@RestController
@RequestMapping("/api/nft/transfer")
@Tag(name = "NFT转账", description = "NFT转账相关的API接口")
public class NFTTransferController {

    @Autowired
    private GameNFTService gameNFTService;

    @Autowired
    private AgentNFTService agentNFTService;

    @Autowired
    private EventListeningService eventListeningService;

    @Autowired
    private ContractConfigService contractConfigService;

    // ==================== 游戏NFT转账 ====================

    @PostMapping("/game/transfer")
    @Operation(summary = "转账游戏NFT", description = "将游戏NFT从发送方转移到接收方")
    public Result<Object> transferGameNFT(
            @Parameter(description = "发送方地址") @RequestParam String from,
            @Parameter(description = "接收方地址") @RequestParam String to,
            @Parameter(description = "NFT ID") @RequestParam BigInteger tokenId,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            TransactionReceipt receipt = gameNFTService.transferFrom(credentials, from, to, tokenId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("txHash", receipt.getTransactionHash());
            result.put("from", from);
            result.put("to", to);
            result.put("tokenId", tokenId);
            result.put("blockNumber", receipt.getBlockNumber());
            result.put("gasUsed", receipt.getGasUsed());
            
            return Result.success(result, "游戏NFT转账成功");
        } catch (Exception e) {
            log.error("转账游戏NFT失败", e);
            return Result.error("转账游戏NFT失败: " + e.getMessage());
        }
    }

    @PostMapping("/game/safe-transfer")
    @Operation(summary = "安全转账游戏NFT", description = "安全地将游戏NFT从发送方转移到接收方")
    public Result<Object> safeTransferGameNFT(
            @Parameter(description = "发送方地址") @RequestParam String from,
            @Parameter(description = "接收方地址") @RequestParam String to,
            @Parameter(description = "NFT ID") @RequestParam BigInteger tokenId,
            @Parameter(description = "附加数据") @RequestParam(required = false) String data,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            byte[] dataBytes = data != null ? data.getBytes() : new byte[0];
            TransactionReceipt receipt = gameNFTService.safeTransferFrom(credentials, from, to, tokenId, dataBytes);
            
            Map<String, Object> result = new HashMap<>();
            result.put("txHash", receipt.getTransactionHash());
            result.put("from", from);
            result.put("to", to);
            result.put("tokenId", tokenId);
            result.put("data", data);
            result.put("blockNumber", receipt.getBlockNumber());
            result.put("gasUsed", receipt.getGasUsed());
            
            return Result.success(result, "游戏NFT安全转账成功");
        } catch (Exception e) {
            log.error("安全转账游戏NFT失败", e);
            return Result.error("安全转账游戏NFT失败: " + e.getMessage());
        }
    }

    @GetMapping("/game/owner/{tokenId}")
    @Operation(summary = "查询游戏NFT拥有者", description = "查询指定游戏NFT的拥有者地址")
    public Result<String> getGameNFTOwner(
            @Parameter(description = "NFT ID") @PathVariable BigInteger tokenId) {
        try {
            String owner = gameNFTService.getOwnerOf(tokenId);
            return Result.success(owner, "查询游戏NFT拥有者成功");
        } catch (Exception e) {
            log.error("查询游戏NFT拥有者失败", e);
            return Result.error("查询游戏NFT拥有者失败: " + e.getMessage());
        }
    }

    @GetMapping("/game/balance/{address}")
    @Operation(summary = "查询游戏NFT余额", description = "查询指定地址的游戏NFT余额")
    public Result<Object> getGameNFTBalance(
            @Parameter(description = "钱包地址") @PathVariable String address) {
        try {
            BigInteger balance = gameNFTService.getBalanceOf(address);
            Map<String, Object> result = new HashMap<>();
            result.put("address", address);
            result.put("balance", balance);
            result.put("balanceString", balance.toString());
            
            return Result.success(result, "查询游戏NFT余额成功");
        } catch (Exception e) {
            log.error("查询游戏NFT余额失败", e);
            return Result.error("查询游戏NFT余额失败: " + e.getMessage());
        }
    }

    // ==================== 智能体NFT转账 ====================

    @PostMapping("/agent/transfer")
    @Operation(summary = "转账智能体NFT", description = "将智能体NFT从发送方转移到接收方")
    public Result<Object> transferAgentNFT(
            @Parameter(description = "发送方地址") @RequestParam String from,
            @Parameter(description = "接收方地址") @RequestParam String to,
            @Parameter(description = "NFT ID") @RequestParam BigInteger tokenId,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            TransactionReceipt receipt = agentNFTService.transferFrom(credentials, from, to, tokenId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("txHash", receipt.getTransactionHash());
            result.put("from", from);
            result.put("to", to);
            result.put("tokenId", tokenId);
            result.put("blockNumber", receipt.getBlockNumber());
            result.put("gasUsed", receipt.getGasUsed());
            
            return Result.success(result, "智能体NFT转账成功");
        } catch (Exception e) {
            log.error("转账智能体NFT失败", e);
            return Result.error("转账智能体NFT失败: " + e.getMessage());
        }
    }

    @PostMapping("/agent/safe-transfer")
    @Operation(summary = "安全转账智能体NFT", description = "安全地将智能体NFT从发送方转移到接收方")
    public Result<Object> safeTransferAgentNFT(
            @Parameter(description = "发送方地址") @RequestParam String from,
            @Parameter(description = "接收方地址") @RequestParam String to,
            @Parameter(description = "NFT ID") @RequestParam BigInteger tokenId,
            @Parameter(description = "附加数据") @RequestParam(required = false) String data,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            byte[] dataBytes = data != null ? data.getBytes() : new byte[0];
            TransactionReceipt receipt = agentNFTService.safeTransferFrom(credentials, from, to, tokenId, dataBytes);
            
            Map<String, Object> result = new HashMap<>();
            result.put("txHash", receipt.getTransactionHash());
            result.put("from", from);
            result.put("to", to);
            result.put("tokenId", tokenId);
            result.put("data", data);
            result.put("blockNumber", receipt.getBlockNumber());
            result.put("gasUsed", receipt.getGasUsed());
            
            return Result.success(result, "智能体NFT安全转账成功");
        } catch (Exception e) {
            log.error("安全转账智能体NFT失败", e);
            return Result.error("安全转账智能体NFT失败: " + e.getMessage());
        }
    }

    @GetMapping("/agent/owner/{tokenId}")
    @Operation(summary = "查询智能体NFT拥有者", description = "查询指定智能体NFT的拥有者地址")
    public Result<String> getAgentNFTOwner(
            @Parameter(description = "NFT ID") @PathVariable BigInteger tokenId) {
        try {
            String owner = agentNFTService.getOwnerOf(tokenId);
            return Result.success(owner, "查询智能体NFT拥有者成功");
        } catch (Exception e) {
            log.error("查询智能体NFT拥有者失败", e);
            return Result.error("查询智能体NFT拥有者失败: " + e.getMessage());
        }
    }

    @GetMapping("/agent/balance/{address}")
    @Operation(summary = "查询智能体NFT余额", description = "查询指定地址的智能体NFT余额")
    public Result<Object> getAgentNFTBalance(
            @Parameter(description = "钱包地址") @PathVariable String address) {
        try {
            BigInteger balance = agentNFTService.getBalanceOf(address);
            Map<String, Object> result = new HashMap<>();
            result.put("address", address);
            result.put("balance", balance);
            result.put("balanceString", balance.toString());
            
            return Result.success(result, "查询智能体NFT余额成功");
        } catch (Exception e) {
            log.error("查询智能体NFT余额失败", e);
            return Result.error("查询智能体NFT余额失败: " + e.getMessage());
        }
    }

    // ==================== 批量转账功能 ====================

    @PostMapping("/game/batch-transfer")
    @Operation(summary = "批量转账游戏NFT", description = "批量将多个游戏NFT从发送方转移到接收方")
    public Result<Object> batchTransferGameNFT(
            @Parameter(description = "发送方地址") @RequestParam String from,
            @Parameter(description = "接收方地址") @RequestParam String to,
            @Parameter(description = "NFT ID列表") @RequestParam java.util.List<BigInteger> tokenIds,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            java.util.List<String> txHashes = new java.util.ArrayList<>();
            java.util.List<Map<String, Object>> results = new java.util.ArrayList<>();
            
            for (BigInteger tokenId : tokenIds) {
                try {
                    TransactionReceipt receipt = gameNFTService.transferFrom(credentials, from, to, tokenId);
                    txHashes.add(receipt.getTransactionHash());
                    
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
            batchResult.put("successCount", txHashes.size());
            batchResult.put("txHashes", txHashes);
            batchResult.put("results", results);
            
            return Result.success(batchResult, "批量转账游戏NFT完成");
        } catch (Exception e) {
            log.error("批量转账游戏NFT失败", e);
            return Result.error("批量转账游戏NFT失败: " + e.getMessage());
        }
    }

    @PostMapping("/agent/batch-transfer")
    @Operation(summary = "批量转账智能体NFT", description = "批量将多个智能体NFT从发送方转移到接收方")
    public Result<Object> batchTransferAgentNFT(
            @Parameter(description = "发送方地址") @RequestParam String from,
            @Parameter(description = "接收方地址") @RequestParam String to,
            @Parameter(description = "NFT ID列表") @RequestParam java.util.List<BigInteger> tokenIds,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            java.util.List<String> txHashes = new java.util.ArrayList<>();
            java.util.List<Map<String, Object>> results = new java.util.ArrayList<>();
            
            for (BigInteger tokenId : tokenIds) {
                try {
                    TransactionReceipt receipt = agentNFTService.transferFrom(credentials, from, to, tokenId);
                    txHashes.add(receipt.getTransactionHash());
                    
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
            batchResult.put("successCount", txHashes.size());
            batchResult.put("txHashes", txHashes);
            batchResult.put("results", results);
            
            return Result.success(batchResult, "批量转账智能体NFT完成");
        } catch (Exception e) {
            log.error("批量转账智能体NFT失败", e);
            return Result.error("批量转账智能体NFT失败: " + e.getMessage());
        }
    }

    // ==================== 转账历史查询 ====================

    @GetMapping("/game/transfer-history/{address}")
    @Operation(summary = "查询游戏NFT转账历史", description = "查询指定地址的游戏NFT转账历史")
    public Result<Object> getGameNFTTransferHistory(
            @Parameter(description = "钱包地址") @PathVariable String address,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        try {
            String contractAddress = contractConfigService.getContractAddress("game-nft");
            if (contractAddress.isEmpty()) {
                return Result.error("游戏NFT合约地址未配置");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("address", address);
            result.put("contractAddress", contractAddress);
            result.put("contractType", "GameNFT");
            result.put("page", page);
            result.put("size", size);
            result.put("deployed", contractConfigService.isContractDeployed("game-nft"));
            
            // 获取NFT余额
            try {
                BigInteger balance = gameNFTService.getBalanceOf(address);
                result.put("nftBalance", balance);
                result.put("nftBalanceString", balance.toString());
            } catch (Exception e) {
                log.warn("获取游戏NFT余额失败: {}", e.getMessage());
                result.put("balanceError", e.getMessage());
            }
            
            // 尝试获取历史转账事件
            try {
                // 估算区块范围（最近1000个区块）
                BigInteger currentBlock = BigInteger.valueOf(System.currentTimeMillis() / 1000 / 12);
                BigInteger fromBlock = currentBlock.subtract(BigInteger.valueOf(1000));
                
                String transferSignature = "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";
                java.util.List<org.web3j.protocol.core.methods.response.Log> logs = 
                    eventListeningService.getHistoricalEvents(fromBlock.toString(), currentBlock.toString());
                
                // 过滤与指定地址相关的事件
                java.util.List<org.web3j.protocol.core.methods.response.Log> relevantLogs = new java.util.ArrayList<>();
                for (org.web3j.protocol.core.methods.response.Log log : logs) {
                    if (log.getTopics().size() >= 3) {
                        String from = "0x" + log.getTopics().get(1).substring(26);
                        String to = "0x" + log.getTopics().get(2).substring(26);
                        if (from.equalsIgnoreCase(address) || to.equalsIgnoreCase(address)) {
                            relevantLogs.add(log);
                        }
                    }
                }
                
                // 分页处理
                int startIndex = (page - 1) * size;
                int endIndex = Math.min(startIndex + size, relevantLogs.size());
                java.util.List<org.web3j.protocol.core.methods.response.Log> pagedLogs = 
                    relevantLogs.subList(startIndex, endIndex);
                
                result.put("transferEvents", pagedLogs);
                result.put("totalCount", relevantLogs.size());
                result.put("hasMore", endIndex < relevantLogs.size());
                
            } catch (Exception e) {
                log.warn("获取游戏NFT转账历史失败: {}", e.getMessage());
                result.put("historyError", e.getMessage());
                result.put("note", "转账历史查询需要区块链网络连接");
            }
            
            return Result.success(result, "查询游戏NFT转账历史成功");
        } catch (Exception e) {
            log.error("查询游戏NFT转账历史失败", e);
            return Result.error("查询游戏NFT转账历史失败: " + e.getMessage());
        }
    }

    @GetMapping("/agent/transfer-history/{address}")
    @Operation(summary = "查询智能体NFT转账历史", description = "查询指定地址的智能体NFT转账历史")
    public Result<Object> getAgentNFTTransferHistory(
            @Parameter(description = "钱包地址") @PathVariable String address,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        try {
            String contractAddress = contractConfigService.getContractAddress("agent-nft");
            if (contractAddress.isEmpty()) {
                return Result.error("智能体NFT合约地址未配置");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("address", address);
            result.put("contractAddress", contractAddress);
            result.put("contractType", "AgentNFT");
            result.put("page", page);
            result.put("size", size);
            result.put("deployed", contractConfigService.isContractDeployed("agent-nft"));
            
            // 获取NFT余额
            try {
                BigInteger balance = agentNFTService.getBalanceOf(address);
                result.put("nftBalance", balance);
                result.put("nftBalanceString", balance.toString());
            } catch (Exception e) {
                log.warn("获取智能体NFT余额失败: {}", e.getMessage());
                result.put("balanceError", e.getMessage());
            }
            
            // 尝试获取历史转账事件
            try {
                // 估算区块范围（最近1000个区块）
                BigInteger currentBlock = BigInteger.valueOf(System.currentTimeMillis() / 1000 / 12);
                BigInteger fromBlock = currentBlock.subtract(BigInteger.valueOf(1000));
                
                String transferSignature = "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";
                java.util.List<org.web3j.protocol.core.methods.response.Log> logs = 
                    eventListeningService.getHistoricalEvents(fromBlock.toString(), currentBlock.toString());
                
                // 过滤与指定地址相关的事件
                java.util.List<org.web3j.protocol.core.methods.response.Log> relevantLogs = new java.util.ArrayList<>();
                for (org.web3j.protocol.core.methods.response.Log log : logs) {
                    if (log.getTopics().size() >= 3) {
                        String from = "0x" + log.getTopics().get(1).substring(26);
                        String to = "0x" + log.getTopics().get(2).substring(26);
                        if (from.equalsIgnoreCase(address) || to.equalsIgnoreCase(address)) {
                            relevantLogs.add(log);
                        }
                    }
                }
                
                // 分页处理
                int startIndex = (page - 1) * size;
                int endIndex = Math.min(startIndex + size, relevantLogs.size());
                java.util.List<org.web3j.protocol.core.methods.response.Log> pagedLogs = 
                    relevantLogs.subList(startIndex, endIndex);
                
                result.put("transferEvents", pagedLogs);
                result.put("totalCount", relevantLogs.size());
                result.put("hasMore", endIndex < relevantLogs.size());
                
            } catch (Exception e) {
                log.warn("获取智能体NFT转账历史失败: {}", e.getMessage());
                result.put("historyError", e.getMessage());
                result.put("note", "转账历史查询需要区块链网络连接");
            }
            
            return Result.success(result, "查询智能体NFT转账历史成功");
        } catch (Exception e) {
            log.error("查询智能体NFT转账历史失败", e);
            return Result.error("查询智能体NFT转账历史失败: " + e.getMessage());
        }
    }

    /**
     * 将私钥字符串转换为Credentials对象
     */
    private Credentials getCredentialsFromPrivateKey(String privateKey) {
        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        return Credentials.create(privateKey);
    }
}
