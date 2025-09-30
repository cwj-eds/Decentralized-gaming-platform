package com.decentralized.gaming.platform.controller.blockchain;

import com.decentralized.gaming.platform.common.Result;
import com.decentralized.gaming.platform.service.blockchain.AgentNFTService;
import com.decentralized.gaming.platform.service.blockchain.ContractConfigService;
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
 * NFT授权控制器
 * 提供NFT授权相关的API接口
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@RestController
@RequestMapping("/api/nft/approval")
@Tag(name = "NFT授权", description = "NFT授权相关的API接口")
public class NFTApprovalController {

    @Autowired
    private GameNFTService gameNFTService;

    @Autowired
    private AgentNFTService agentNFTService;

    @Autowired
    private ContractConfigService contractConfigService;

    // ==================== 游戏NFT授权 ====================

    @PostMapping("/game/approve")
    @Operation(summary = "授权游戏NFT", description = "授权指定地址使用游戏NFT")
    public Result<Object> approveGameNFT(
            @Parameter(description = "被授权者地址") @RequestParam String to,
            @Parameter(description = "NFT ID") @RequestParam BigInteger tokenId,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            TransactionReceipt receipt = gameNFTService.approve(credentials, to, tokenId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("txHash", receipt.getTransactionHash());
            result.put("owner", credentials.getAddress());
            result.put("approved", to);
            result.put("tokenId", tokenId);
            result.put("blockNumber", receipt.getBlockNumber());
            result.put("gasUsed", receipt.getGasUsed());
            
            return Result.success(result, "游戏NFT授权成功");
        } catch (Exception e) {
            log.error("授权游戏NFT失败", e);
            return Result.error("授权游戏NFT失败: " + e.getMessage());
        }
    }

    @PostMapping("/game/approve-all")
    @Operation(summary = "批量授权游戏NFT", description = "授权指定地址使用所有游戏NFT")
    public Result<Object> setApprovalForAllGameNFT(
            @Parameter(description = "操作者地址") @RequestParam String operator,
            @Parameter(description = "是否授权") @RequestParam boolean approved,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            TransactionReceipt receipt = gameNFTService.setApprovalForAll(credentials, operator, approved);
            
            Map<String,Object> result = new HashMap<>();
            result.put("txHash", receipt.getTransactionHash());
            result.put("owner", credentials.getAddress());
            result.put("operator", operator);
            result.put("approved", approved);
            result.put("blockNumber", receipt.getBlockNumber());
            result.put("gasUsed", receipt.getGasUsed());
            
            return Result.success(result, "批量授权游戏NFT成功");
        } catch (Exception e) {
            log.error("批量授权游戏NFT失败", e);
            return Result.error("批量授权游戏NFT失败: " + e.getMessage());
        }
    }

    @GetMapping("/game/approved/{tokenId}")
    @Operation(summary = "查询游戏NFT授权地址", description = "查询指定游戏NFT的授权地址")
    public Result<String> getGameNFTApproved(
            @Parameter(description = "NFT ID") @PathVariable BigInteger tokenId) {
        try {
            String approved = gameNFTService.getApproved(tokenId);
            return Result.success(approved, "查询游戏NFT授权地址成功");
        } catch (Exception e) {
            log.error("查询游戏NFT授权地址失败", e);
            return Result.error("查询游戏NFT授权地址失败: " + e.getMessage());
        }
    }

    @GetMapping("/game/approved-for-all")
    @Operation(summary = "查询游戏NFT批量授权状态", description = "查询指定操作者是否被授权使用所有游戏NFT")
    public Result<Object> isGameNFTApprovedForAll(
            @Parameter(description = "拥有者地址") @RequestParam String owner,
            @Parameter(description = "操作者地址") @RequestParam String operator) {
        try {
            boolean approved = gameNFTService.isApprovedForAll(owner, operator);
            Map<String, Object> result = new HashMap<>();
            result.put("owner", owner);
            result.put("operator", operator);
            result.put("approved", approved);
            
            return Result.success(result, "查询游戏NFT批量授权状态成功");
        } catch (Exception e) {
            log.error("查询游戏NFT批量授权状态失败", e);
            return Result.error("查询游戏NFT批量授权状态失败: " + e.getMessage());
        }
    }

    // ==================== 智能体NFT授权 ====================

    @PostMapping("/agent/approve")
    @Operation(summary = "授权智能体NFT", description = "授权指定地址使用智能体NFT")
    public Result<Object> approveAgentNFT(
            @Parameter(description = "被授权者地址") @RequestParam String to,
            @Parameter(description = "NFT ID") @RequestParam BigInteger tokenId,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            TransactionReceipt receipt = agentNFTService.approve(credentials, to, tokenId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("txHash", receipt.getTransactionHash());
            result.put("owner", credentials.getAddress());
            result.put("approved", to);
            result.put("tokenId", tokenId);
            result.put("blockNumber", receipt.getBlockNumber());
            result.put("gasUsed", receipt.getGasUsed());
            
            return Result.success(result, "智能体NFT授权成功");
        } catch (Exception e) {
            log.error("授权智能体NFT失败", e);
            return Result.error("授权智能体NFT失败: " + e.getMessage());
        }
    }

    @PostMapping("/agent/approve-all")
    @Operation(summary = "批量授权智能体NFT", description = "授权指定地址使用所有智能体NFT")
    public Result<Object> setApprovalForAllAgentNFT(
            @Parameter(description = "操作者地址") @RequestParam String operator,
            @Parameter(description = "是否授权") @RequestParam boolean approved,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            TransactionReceipt receipt = agentNFTService.setApprovalForAll(credentials, operator, approved);
            
            Map<String, Object> result = new HashMap<>();
            result.put("txHash", receipt.getTransactionHash());
            result.put("owner", credentials.getAddress());
            result.put("operator", operator);
            result.put("approved", approved);
            result.put("blockNumber", receipt.getBlockNumber());
            result.put("gasUsed", receipt.getGasUsed());
            
            return Result.success(result, "批量授权智能体NFT成功");
        } catch (Exception e) {
            log.error("批量授权智能体NFT失败", e);
            return Result.error("批量授权智能体NFT失败: " + e.getMessage());
        }
    }

    @GetMapping("/agent/approved/{tokenId}")
    @Operation(summary = "查询智能体NFT授权地址", description = "查询指定智能体NFT的授权地址")
    public Result<String> getAgentNFTApproved(
            @Parameter(description = "NFT ID") @PathVariable BigInteger tokenId) {
        try {
            String approved = agentNFTService.getApproved(tokenId);
            return Result.success(approved, "查询智能体NFT授权地址成功");
        } catch (Exception e) {
            log.error("查询智能体NFT授权地址失败", e);
            return Result.error("查询智能体NFT授权地址失败: " + e.getMessage());
        }
    }

    @GetMapping("/agent/approved-for-all")
    @Operation(summary = "查询智能体NFT批量授权状态", description = "查询指定操作者是否被授权使用所有智能体NFT")
    public Result<Object> isAgentNFTApprovedForAll(
            @Parameter(description = "拥有者地址") @RequestParam String owner,
            @Parameter(description = "操作者地址") @RequestParam String operator) {
        try {
            boolean approved = agentNFTService.isApprovedForAll(owner, operator);
            Map<String, Object> result = new HashMap<>();
            result.put("owner", owner);
            result.put("operator", operator);
            result.put("approved", approved);
            
            return Result.success(result, "查询智能体NFT批量授权状态成功");
        } catch (Exception e) {
            log.error("查询智能体NFT批量授权状态失败", e);
            return Result.error("查询智能体NFT批量授权状态失败: " + e.getMessage());
        }
    }

    // ==================== 授权管理功能 ====================

    @PostMapping("/game/revoke")
    @Operation(summary = "撤销游戏NFT授权", description = "撤销指定游戏NFT的授权")
    public Result<Object> revokeGameNFTApproval(
            @Parameter(description = "NFT ID") @RequestParam BigInteger tokenId,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            // 撤销授权就是授权给零地址
            TransactionReceipt receipt = gameNFTService.approve(credentials, "0x0000000000000000000000000000000000000000", tokenId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("txHash", receipt.getTransactionHash());
            result.put("owner", credentials.getAddress());
            result.put("tokenId", tokenId);
            result.put("revoked", true);
            result.put("blockNumber", receipt.getBlockNumber());
            result.put("gasUsed", receipt.getGasUsed());
            
            return Result.success(result, "撤销游戏NFT授权成功");
        } catch (Exception e) {
            log.error("撤销游戏NFT授权失败", e);
            return Result.error("撤销游戏NFT授权失败: " + e.getMessage());
        }
    }

    @PostMapping("/agent/revoke")
    @Operation(summary = "撤销智能体NFT授权", description = "撤销指定智能体NFT的授权")
    public Result<Object> revokeAgentNFTApproval(
            @Parameter(description = "NFT ID") @RequestParam BigInteger tokenId,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            // 撤销授权就是授权给零地址
            TransactionReceipt receipt = agentNFTService.approve(credentials, "0x0000000000000000000000000000000000000000", tokenId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("txHash", receipt.getTransactionHash());
            result.put("owner", credentials.getAddress());
            result.put("tokenId", tokenId);
            result.put("revoked", true);
            result.put("blockNumber", receipt.getBlockNumber());
            result.put("gasUsed", receipt.getGasUsed());
            
            return Result.success(result, "撤销智能体NFT授权成功");
        } catch (Exception e) {
            log.error("撤销智能体NFT授权失败", e);
            return Result.error("撤销智能体NFT授权失败: " + e.getMessage());
        }
    }

    @PostMapping("/game/revoke-all")
    @Operation(summary = "撤销所有游戏NFT授权", description = "撤销指定操作者的所有游戏NFT授权")
    public Result<Object> revokeAllGameNFTApproval(
            @Parameter(description = "操作者地址") @RequestParam String operator,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            TransactionReceipt receipt = gameNFTService.setApprovalForAll(credentials, operator, false);
            
            Map<String, Object> result = new HashMap<>();
            result.put("txHash", receipt.getTransactionHash());
            result.put("owner", credentials.getAddress());
            result.put("operator", operator);
            result.put("revoked", true);
            result.put("blockNumber", receipt.getBlockNumber());
            result.put("gasUsed", receipt.getGasUsed());
            
            return Result.success(result, "撤销所有游戏NFT授权成功");
        } catch (Exception e) {
            log.error("撤销所有游戏NFT授权失败", e);
            return Result.error("撤销所有游戏NFT授权失败: " + e.getMessage());
        }
    }

    @PostMapping("/agent/revoke-all")
    @Operation(summary = "撤销所有智能体NFT授权", description = "撤销指定操作者的所有智能体NFT授权")
    public Result<Object> revokeAllAgentNFTApproval(
            @Parameter(description = "操作者地址") @RequestParam String operator,
            @Parameter(description = "私钥") @RequestParam String privateKey) {
        try {
            Credentials credentials = getCredentialsFromPrivateKey(privateKey);
            TransactionReceipt receipt = agentNFTService.setApprovalForAll(credentials, operator, false);
            
            Map<String, Object> result = new HashMap<>();
            result.put("txHash", receipt.getTransactionHash());
            result.put("owner", credentials.getAddress());
            result.put("operator", operator);
            result.put("revoked", true);
            result.put("blockNumber", receipt.getBlockNumber());
            result.put("gasUsed", receipt.getGasUsed());
            
            return Result.success(result, "撤销所有智能体NFT授权成功");
        } catch (Exception e) {
            log.error("撤销所有智能体NFT授权失败", e);
            return Result.error("撤销所有智能体NFT授权失败: " + e.getMessage());
        }
    }

    // ==================== 授权状态查询 ====================

    @GetMapping("/game/approval-status/{address}")
    @Operation(summary = "查询游戏NFT授权状态", description = "查询指定地址的游戏NFT授权状态")
    public Result<Object> getGameNFTApprovalStatus(
            @Parameter(description = "钱包地址") @PathVariable String address) {
        try {
            String contractAddress = contractConfigService.getContractAddress("game-nft");
            if (contractAddress.isEmpty()) {
                return Result.error("游戏NFT合约地址未配置");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("address", address);
            result.put("contractAddress", contractAddress);
            result.put("contractType", "GameNFT");
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
            
            // 获取授权历史（这里可以集成事件监听服务）
            result.put("approvalHistory", "授权历史查询功能需要集成事件监听服务");
            result.put("note", "可以通过事件监听服务获取历史授权记录");
            
            return Result.success(result, "查询游戏NFT授权状态成功");
        } catch (Exception e) {
            log.error("查询游戏NFT授权状态失败", e);
            return Result.error("查询游戏NFT授权状态失败: " + e.getMessage());
        }
    }

    @GetMapping("/agent/approval-status/{address}")
    @Operation(summary = "查询智能体NFT授权状态", description = "查询指定地址的智能体NFT授权状态")
    public Result<Object> getAgentNFTApprovalStatus(
            @Parameter(description = "钱包地址") @PathVariable String address) {
        try {
            String contractAddress = contractConfigService.getContractAddress("agent-nft");
            if (contractAddress.isEmpty()) {
                return Result.error("智能体NFT合约地址未配置");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("address", address);
            result.put("contractAddress", contractAddress);
            result.put("contractType", "AgentNFT");
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
            
            // 获取授权历史（这里可以集成事件监听服务）
            result.put("approvalHistory", "授权历史查询功能需要集成事件监听服务");
            result.put("note", "可以通过事件监听服务获取历史授权记录");
            
            return Result.success(result, "查询智能体NFT授权状态成功");
        } catch (Exception e) {
            log.error("查询智能体NFT授权状态失败", e);
            return Result.error("查询智能体NFT授权状态失败: " + e.getMessage());
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
