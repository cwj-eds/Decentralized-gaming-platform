package com.decentralized.gaming.platform.service.blockchain.impl;

import com.decentralized.gaming.platform.contracts.AgentNFT;
import com.decentralized.gaming.platform.exception.BlockchainException;
import com.decentralized.gaming.platform.service.blockchain.AgentNFTService;
import com.decentralized.gaming.platform.service.blockchain.BlockchainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

/**
 * 智能体NFT服务实现类
 * 封装智能体NFT合约的所有操作
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
public class AgentNFTServiceImpl implements AgentNFTService {

    @Autowired
    private AgentNFT agentNftContract;

    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private ContractGasProvider gasProvider;

    @Autowired
    private org.web3j.protocol.Web3j web3j;

    @Override
    public String getName() {
        try {
            return agentNftContract.name().send();
        } catch (Exception e) {
            log.error("获取NFT名称失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "getName", "获取NFT名称失败", e);
        }
    }

    @Override
    public String getSymbol() {
        try {
            return agentNftContract.symbol().send();
        } catch (Exception e) {
            log.error("获取NFT符号失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "getSymbol", "获取NFT符号失败", e);
        }
    }

    @Override
    public String getOwnerOf(BigInteger tokenId) {
        try {
            return agentNftContract.ownerOf(tokenId).send();
        } catch (Exception e) {
            log.error("获取NFT拥有者失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "getOwner", "获取NFT拥有者失败", e);
        }
    }

    @Override
    public BigInteger getBalanceOf(String address) {
        try {
            return agentNftContract.balanceOf(address).send();
        } catch (Exception e) {
            log.error("获取账户NFT余额失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "balanceOf", "获取账户NFT余额失败", e);
        }
    }

    @Override
    public String getTokenURI(BigInteger tokenId) {
        try {
            return agentNftContract.tokenURI(tokenId).send();
        } catch (Exception e) {
            log.error("获取代币URI失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "tokenURI", "获取代币URI失败", e);
        }
    }

    @Override
    public String getApproved(BigInteger tokenId) {
        try {
            return agentNftContract.getApproved(tokenId).send();
        } catch (Exception e) {
            log.error("获取授权地址失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "getApproved", "获取授权地址失败", e);
        }
    }

    @Override
    public boolean isApprovedForAll(String owner, String operator) {
        try {
            return agentNftContract.isApprovedForAll(owner, operator).send();
        } catch (Exception e) {
            log.error("检查授权状态失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "isApprovedForAll", "检查授权状态失败", e);
        }
    }

    @Override
    public TransactionReceipt createAgent(Credentials credentials, String agentName, String agentDescription, 
                                        String agentImageUrl, String agentUrl, BigInteger uploadFee) {
        try {
            AgentNFT contract = AgentNFT.load(
                agentNftContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.createAgent(
                agentName, agentDescription, "AI_AGENT", agentImageUrl, agentUrl, uploadFee, ""
            ).send();
            
            log.info("创建智能体成功: 智能体名称: {}, 创建者: {}, 交易哈希: {}", 
                agentName, credentials.getAddress(), receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("创建智能体失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "createAgent", "创建智能体失败", e);
        }
    }

    @Override
    public TransactionReceipt approve(Credentials credentials, String to, BigInteger tokenId) {
        try {
            AgentNFT contract = AgentNFT.load(
                agentNftContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.approve(to, tokenId).send();
            log.info("授权NFT成功: 代币ID: {}, 被授权者: {}, 交易哈希: {}", 
                tokenId, to, receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("授权NFT失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "approve", "授权NFT失败", e);
        }
    }

    @Override
    public TransactionReceipt setApprovalForAll(Credentials credentials, String operator, boolean approved) {
        try {
            AgentNFT contract = AgentNFT.load(
                agentNftContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.setApprovalForAll(operator, approved).send();
            log.info("设置操作者授权成功: 操作者: {}, 授权状态: {}, 交易哈希: {}", 
                operator, approved, receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("设置操作者授权失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "setApprovalForAll", "设置操作者授权失败", e);
        }
    }

    @Override
    public TransactionReceipt transferFrom(Credentials credentials, String from, String to, BigInteger tokenId) {
        try {
            AgentNFT contract = AgentNFT.load(
                agentNftContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.transferFrom(from, to, tokenId).send();
            log.info("转移NFT成功: 代币ID: {}, {} -> {}, 交易哈希: {}", 
                tokenId, from, to, receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("转移NFT失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "transferFrom", "转移NFT失败", e);
        }
    }

    @Override
    public TransactionReceipt safeTransferFrom(Credentials credentials, String from, String to, 
                                             BigInteger tokenId, byte[] data) {
        try {
            AgentNFT contract = AgentNFT.load(
                agentNftContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.safeTransferFrom(from, to, tokenId, data).send();
            log.info("安全转移NFT成功: 代币ID: {}, {} -> {}, 交易哈希: {}", 
                tokenId, from, to, receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("安全转移NFT失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "safeTransferFrom", "安全转移NFT失败", e);
        }
    }

    @Override
    public TransactionReceipt adminMintAgent(Credentials credentials, String to, String agentName, String agentDescription, 
                                            String agentImageUrl, String agentUrl) {
        try {
            AgentNFT contract = AgentNFT.load(
                agentNftContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.adminMintAgent(to, agentName, agentDescription, "AI_AGENT", agentImageUrl, agentUrl, BigInteger.ZERO, "").send();
            log.info("管理员铸造智能体NFT成功: 接收者: {}, 交易哈希: {}", 
                to, receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("管理员铸造智能体NFT失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "mint", "管理员铸造智能体NFT失败", e);
        }
    }

    @Override
    public TransactionReceipt setUploadFee(Credentials credentials, BigInteger newUploadFee) {
        try {
            AgentNFT contract = AgentNFT.load(
                agentNftContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.setUploadFee(newUploadFee).send();
            log.info("设置上传费用成功: 新费用: {}, 交易哈希: {}", 
                newUploadFee, receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("设置上传费用失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "setUploadFee", "设置上传费用失败", e);
        }
    }

    @Override
    public TransactionReceipt setFeeRecipient(Credentials credentials, String newFeeRecipient) {
        try {
            AgentNFT contract = AgentNFT.load(
                agentNftContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.setFeeRecipient(newFeeRecipient).send();
            log.info("设置费用接收者成功: 新接收者: {}, 交易哈希: {}", 
                newFeeRecipient, receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("设置费用接收者失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "setFeeRecipient", "设置费用接收者失败", e);
        }
    }

    @Override
    public TransactionReceipt setPlatformToken(Credentials credentials, String newPlatformToken) {
        try {
            AgentNFT contract = AgentNFT.load(
                agentNftContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.setPlatformToken(newPlatformToken).send();
            log.info("设置平台代币成功: 新代币地址: {}, 交易哈希: {}", 
                newPlatformToken, receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("设置平台代币失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "setPlatformToken", "设置平台代币失败", e);
        }
    }

    @Override
    public TransactionReceipt transferOwnership(Credentials credentials, String newOwner) {
        try {
            AgentNFT contract = AgentNFT.load(
                agentNftContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.transferOwnership(newOwner).send();
            log.info("转移所有权成功: {} -> {}, 交易哈希: {}", 
                credentials.getAddress(), newOwner, receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("转移所有权失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "transferOwnership", "转移所有权失败", e);
        }
    }

    @Override
    public TransactionReceipt renounceOwnership(Credentials credentials) {
        try {
            AgentNFT contract = AgentNFT.load(
                agentNftContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.renounceOwnership().send();
            log.info("放弃所有权成功: 操作者: {}, 交易哈希: {}", 
                credentials.getAddress(), receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("放弃所有权失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "renounceOwnership", "放弃所有权失败", e);
        }
    }

    @Override
    public String getOwner() {
        try {
            return agentNftContract.owner().send();
        } catch (Exception e) {
            log.error("获取合约拥有者失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "owner", "获取合约拥有者失败", e);
        }
    }

    @Override
    public BigInteger getUploadFee() {
        try {
            return agentNftContract.uploadFee().send();
        } catch (Exception e) {
            log.error("获取上传费用失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "uploadFee", "获取上传费用失败", e);
        }
    }

    @Override
    public String getFeeRecipient() {
        try {
            return agentNftContract.feeRecipient().send();
        } catch (Exception e) {
            log.error("获取费用接收者失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "feeRecipient", "获取费用接收者失败", e);
        }
    }

    @Override
    public String getPlatformToken() {
        try {
            return agentNftContract.platformToken().send();
        } catch (Exception e) {
            log.error("获取平台代币地址失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "platformToken", "获取平台代币地址失败", e);
        }
    }

    @Override
    public boolean supportsInterface(byte[] interfaceId) {
        try {
            return agentNftContract.supportsInterface(interfaceId).send();
        } catch (Exception e) {
            log.error("检查接口支持失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "supportsInterface", "检查接口支持失败", e);
        }
    }

    @Override
    public CompletableFuture<TransactionReceipt> createAgentAsync(Credentials credentials, String agentName, 
                                                                String agentDescription, String agentImageUrl, 
                                                                String agentUrl, BigInteger uploadFee) {
        return CompletableFuture.supplyAsync(() -> 
            createAgent(credentials, agentName, agentDescription, agentImageUrl, agentUrl, uploadFee));
    }

    @Override
    public boolean isTransactionSuccessful(String transactionHash) {
        return blockchainService.isTransactionSuccessful(transactionHash);
    }

    @Override
    public TransactionReceipt waitForTransactionReceipt(String transactionHash, int maxWaitTime) {
        return blockchainService.waitForTransactionReceipt(transactionHash, maxWaitTime);
    }
}
