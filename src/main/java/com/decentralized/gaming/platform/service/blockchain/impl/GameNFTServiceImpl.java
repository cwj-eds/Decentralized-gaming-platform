package com.decentralized.gaming.platform.service.blockchain.impl;

import com.decentralized.gaming.platform.contracts.GameNFT;
import com.decentralized.gaming.platform.exception.BlockchainException;
import com.decentralized.gaming.platform.service.blockchain.BlockchainService;
import com.decentralized.gaming.platform.service.blockchain.GameNFTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

/**
 * 游戏NFT服务实现类
 * 封装游戏NFT合约的所有操作
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
public class GameNFTServiceImpl implements GameNFTService {

    @Autowired(required = false)
    private GameNFT gameNftContract;

    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private ContractGasProvider gasProvider;

    @Autowired
    private org.web3j.protocol.Web3j web3j;

    @Override
    public String getName() {
        try {
            return gameNftContract.name().send();
        } catch (Exception e) {
            log.error("获取NFT名称失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "getName", "获取NFT名称失败", e);
        }
    }

    @Override
    public String getSymbol() {
        try {
            return gameNftContract.symbol().send();
        } catch (Exception e) {
            log.error("获取NFT符号失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "getSymbol", "获取NFT符号失败", e);
        }
    }

    @Override
    public String getOwnerOf(BigInteger tokenId) {
        try {
            return gameNftContract.ownerOf(tokenId).send();
        } catch (Exception e) {
            log.error("获取NFT拥有者失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "getOwner", "获取NFT拥有者失败", e);
        }
    }

    @Override
    public BigInteger getBalanceOf(String address) {
        try {
            return gameNftContract.balanceOf(address).send();
        } catch (Exception e) {
            log.error("获取账户NFT余额失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "balanceOf", "获取账户NFT余额失败", e);
        }
    }

    @Override
    public String getTokenURI(BigInteger tokenId) {
        try {
            return gameNftContract.tokenURI(tokenId).send();
        } catch (Exception e) {
            log.error("获取代币URI失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "tokenURI", "获取代币URI失败", e);
        }
    }

    @Override
    public String getApproved(BigInteger tokenId) {
        try {
            return gameNftContract.getApproved(tokenId).send();
        } catch (Exception e) {
            log.error("获取授权地址失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "getApproved", "获取授权地址失败", e);
        }
    }

    @Override
    public boolean isApprovedForAll(String owner, String operator) {
        try {
            return gameNftContract.isApprovedForAll(owner, operator).send();
        } catch (Exception e) {
            log.error("检查授权状态失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "isApprovedForAll", "检查授权状态失败", e);
        }
    }

    @Override
    public TransactionReceipt createGame(Credentials credentials, String gameName, String gameDescription, 
                                       String gameImageUrl, String gameUrl, BigInteger creationFee) {
        try {
            GameNFT contract = GameNFT.load(
                gameNftContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.createGame(
                gameName, gameDescription, gameImageUrl, gameUrl
            ).send();
            
            log.info("创建游戏成功: 游戏名称: {}, 创建者: {}, 交易哈希: {}", 
                gameName, credentials.getAddress(), receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("创建游戏失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "createGame", "创建游戏失败", e);
        }
    }

    @Override
    public TransactionReceipt approve(Credentials credentials, String to, BigInteger tokenId) {
        try {
            GameNFT contract = GameNFT.load(
                gameNftContract.getContractAddress(),
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
            GameNFT contract = GameNFT.load(
                gameNftContract.getContractAddress(),
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
            GameNFT contract = GameNFT.load(
                gameNftContract.getContractAddress(),
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
            GameNFT contract = GameNFT.load(
                gameNftContract.getContractAddress(),
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
    public TransactionReceipt adminMint(Credentials credentials, String to, String title, String description, String codeHash, String tokenURI) {
        try {
            GameNFT contract = GameNFT.load(
                gameNftContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.adminMint(to, title, description, codeHash, tokenURI).send();
            log.info("管理员铸造NFT成功: 接收者: {}, 交易哈希: {}", 
                to, receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("管理员铸造NFT失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "mint", "管理员铸造NFT失败", e);
        }
    }

    @Override
    public TransactionReceipt setCreationFee(Credentials credentials, BigInteger newCreationFee) {
        try {
            GameNFT contract = GameNFT.load(
                gameNftContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.setCreationFee(newCreationFee).send();
            log.info("设置创建费用成功: 新费用: {}, 交易哈希: {}", 
                newCreationFee, receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("设置创建费用失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "setCreationFee", "设置创建费用失败", e);
        }
    }

    @Override
    public TransactionReceipt setFeeRecipient(Credentials credentials, String newFeeRecipient) {
        try {
            GameNFT contract = GameNFT.load(
                gameNftContract.getContractAddress(),
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
            GameNFT contract = GameNFT.load(
                gameNftContract.getContractAddress(),
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
            GameNFT contract = GameNFT.load(
                gameNftContract.getContractAddress(),
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
            GameNFT contract = GameNFT.load(
                gameNftContract.getContractAddress(),
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
            return gameNftContract.owner().send();
        } catch (Exception e) {
            log.error("获取合约拥有者失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "owner", "获取合约拥有者失败", e);
        }
    }

    @Override
    public BigInteger getCreationFee() {
        try {
            return gameNftContract.creationFee().send();
        } catch (Exception e) {
            log.error("获取创建费用失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "creationFee", "获取创建费用失败", e);
        }
    }

    @Override
    public String getFeeRecipient() {
        try {
            return gameNftContract.feeRecipient().send();
        } catch (Exception e) {
            log.error("获取费用接收者失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "feeRecipient", "获取费用接收者失败", e);
        }
    }

    @Override
    public String getPlatformToken() {
        try {
            return gameNftContract.platformToken().send();
        } catch (Exception e) {
            log.error("获取平台代币地址失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "platformToken", "获取平台代币地址失败", e);
        }
    }

    @Override
    public boolean supportsInterface(byte[] interfaceId) {
        try {
            return gameNftContract.supportsInterface(interfaceId).send();
        } catch (Exception e) {
            log.error("检查接口支持失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "supportsInterface", "检查接口支持失败", e);
        }
    }

    @Override
    public CompletableFuture<TransactionReceipt> createGameAsync(Credentials credentials, String gameName, 
                                                               String gameDescription, String gameImageUrl, 
                                                               String gameUrl, BigInteger creationFee) {
        return CompletableFuture.supplyAsync(() -> 
            createGame(credentials, gameName, gameDescription, gameImageUrl, gameUrl, creationFee));
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
