package com.decentralized.gaming.platform.service.blockchain.impl;

import com.decentralized.gaming.platform.contracts.Marketplace;
import com.decentralized.gaming.platform.exception.BlockchainException;
import com.decentralized.gaming.platform.service.blockchain.BlockchainService;
import com.decentralized.gaming.platform.service.blockchain.MarketplaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 交易市场服务实现类
 * 封装交易市场合约的所有操作
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
public class MarketplaceServiceImpl implements MarketplaceService {

    @Autowired(required = false)
    private Marketplace marketplaceContract;

    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private ContractGasProvider gasProvider;

    @Autowired(required = false)
    private org.web3j.protocol.Web3j web3j;

    @Override
    public String getOwner() {
        try {
            return marketplaceContract.owner().send();
        } catch (Exception e) {
            log.error("获取合约拥有者失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "owner", "获取合约拥有者失败", e);
        }
    }

    @Override
    public String getPlatformToken() {
        try {
            return marketplaceContract.platformToken().send();
        } catch (Exception e) {
            log.error("获取平台代币地址失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "platformToken", "获取平台代币地址失败", e);
        }
    }

    @Override
    public String getFeeRecipient() {
        try {
            return marketplaceContract.feeRecipient().send();
        } catch (Exception e) {
            log.error("获取费用接收者失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "feeRecipient", "获取费用接收者失败", e);
        }
    }

    @Override
    public BigInteger getFeeBasisPoints() {
        try {
            return marketplaceContract.feeBasisPoints().send();
        } catch (Exception e) {
            log.error("获取费用基点失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "feeBasisPoints", "获取费用基点失败", e);
        }
    }

    @Override
    public BigInteger getListingCounter() {
        try {
            return marketplaceContract.listingCounter().send();
        } catch (Exception e) {
            log.error("获取上架计数器失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "listingCounter", "获取上架计数器失败", e);
        }
    }

    @Override
    public TransactionReceipt listERC721(Credentials credentials, String nftContract, BigInteger tokenId, BigInteger price) {
        try {
            Marketplace contract = Marketplace.load(
                marketplaceContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.listERC721(nftContract, tokenId, price).send();
            log.info("上架ERC721 NFT成功: 合约地址: {}, 代币ID: {}, 价格: {}, 交易哈希: {}", 
                nftContract, tokenId, price, receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("上架ERC721 NFT失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "listERC721", "上架ERC721 NFT失败", e);
        }
    }

    @Override
    public TransactionReceipt listERC1155(Credentials credentials, String nftContract, BigInteger tokenId, 
                                        BigInteger quantity, BigInteger price) {
        try {
            Marketplace contract = Marketplace.load(
                marketplaceContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.listERC1155(nftContract, tokenId, quantity, price).send();
            log.info("上架ERC1155 NFT成功: 合约地址: {}, 代币ID: {}, 数量: {}, 价格: {}, 交易哈希: {}", 
                nftContract, tokenId, quantity, price, receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("上架ERC1155 NFT失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "listERC1155", "上架ERC1155 NFT失败", e);
        }
    }

    @Override
    public TransactionReceipt buy(Credentials credentials, BigInteger listingId) {
        try {
            Marketplace contract = Marketplace.load(
                marketplaceContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.buy(listingId).send();
            log.info("购买NFT成功: 上架ID: {}, 购买者: {}, 交易哈希: {}", 
                listingId, credentials.getAddress(), receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("购买NFT失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "buyItem", "购买NFT失败", e);
        }
    }

    @Override
    public TransactionReceipt cancelListing(Credentials credentials, BigInteger listingId) {
        try {
            Marketplace contract = Marketplace.load(
                marketplaceContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.cancelListing(listingId).send();
            log.info("取消上架成功: 上架ID: {}, 操作者: {}, 交易哈希: {}", 
                listingId, credentials.getAddress(), receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("取消上架失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "cancelListing", "取消上架失败", e);
        }
    }

    @Override
    public TransactionReceipt setFeeBasisPoints(Credentials credentials, BigInteger newFeeBasisPoints) {
        try {
            Marketplace contract = Marketplace.load(
                marketplaceContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.setFeeBasisPoints(newFeeBasisPoints).send();
            log.info("设置费用基点成功: 新费用基点: {}, 交易哈希: {}", 
                newFeeBasisPoints, receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("设置费用基点失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "setFeeBasisPoints", "设置费用基点失败", e);
        }
    }

    @Override
    public TransactionReceipt setFeeRecipient(Credentials credentials, String newFeeRecipient) {
        try {
            Marketplace contract = Marketplace.load(
                marketplaceContract.getContractAddress(),
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
            Marketplace contract = Marketplace.load(
                marketplaceContract.getContractAddress(),
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
            Marketplace contract = Marketplace.load(
                marketplaceContract.getContractAddress(),
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
            Marketplace contract = Marketplace.load(
                marketplaceContract.getContractAddress(),
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
    public byte[] onERC1155Received(String operator, String from, 
                                  BigInteger id, BigInteger value, byte[] data) {
        log.info("处理ERC1155接收: 操作者={}, 发送者={}, 代币ID={}, 数量={}", 
            operator, from, id, value);
        // 返回ERC1155接收函数的选择器
        return new byte[]{0x01, 0x02, 0x03, 0x04};
    }

    @Override
    public byte[] onERC1155BatchReceived(String operator, String from, 
                                       BigInteger[] ids, BigInteger[] values, byte[] data) {
        log.info("处理ERC1155批量接收: 操作者={}, 发送者={}, 代币数量={}", 
            operator, from, ids.length);
        // 返回ERC1155批量接收函数的选择器
        return new byte[]{0x01, 0x02, 0x03, 0x04};
    }

    @Override
    public CompletableFuture<TransactionReceipt> listERC721Async(Credentials credentials, String nftContract, 
                                                               BigInteger tokenId, BigInteger price) {
        return CompletableFuture.supplyAsync(() -> 
            listERC721(credentials, nftContract, tokenId, price));
    }

    @Override
    public CompletableFuture<TransactionReceipt> buyAsync(Credentials credentials, BigInteger listingId) {
        return CompletableFuture.supplyAsync(() -> buy(credentials, listingId));
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
