package com.decentralized.gaming.platform.service.blockchain.impl;

import com.decentralized.gaming.platform.service.blockchain.BlockchainService;
import com.decentralized.gaming.platform.service.blockchain.TransactionHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * 交易历史服务实现类
 * 提供区块链交易历史查询功能
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
public class TransactionHistoryServiceImpl implements TransactionHistoryService {

    @Autowired(required = false)
    private Web3j web3j;

    @Autowired
    private BlockchainService blockchainService;

    @Override
    public List<Transaction> getAddressTransactionHistory(String address, long startBlock, long endBlock) {
        List<Transaction> transactions = new ArrayList<>();
        
        try {
            // 遍历区块范围
            for (long i = startBlock; i <= endBlock; i++) {
                try {
                    EthBlock ethBlock = web3j.ethGetBlockByNumber(
                        DefaultBlockParameter.valueOf(BigInteger.valueOf(i)), 
                        true
                    ).send();
                    
                    if (ethBlock.getBlock() != null) {
                        List<EthBlock.TransactionResult> txResults = ethBlock.getBlock().getTransactions();
                        for (EthBlock.TransactionResult txResult : txResults) {
                            Transaction tx = (Transaction) txResult.get();
                            // 检查交易的发送者或接收者是否是目标地址
                            if (tx.getFrom().equalsIgnoreCase(address) || 
                                (tx.getTo() != null && tx.getTo().equalsIgnoreCase(address))) {
                                transactions.add(tx);
                            }
                        }
                    }
                } catch (Exception e) {
                    log.warn("获取区块 {} 失败: {}", i, e.getMessage());
                }
            }
            
            log.info("获取地址 {} 的交易历史，从区块 {} 到 {}, 共 {} 笔交易", 
                address, startBlock, endBlock, transactions.size());
            
        } catch (Exception e) {
            log.error("获取地址交易历史失败", e);
        }
        
        return transactions;
    }

    @Override
    public List<Transaction> getRecentTransactions(int limit) {
        List<Transaction> transactions = new ArrayList<>();
        
        try {
            BigInteger currentBlock = blockchainService.getCurrentBlockNumber();
            BigInteger startBlock = currentBlock.subtract(BigInteger.valueOf(limit));
            
            for (long i = startBlock.longValue(); i <= currentBlock.longValue() && transactions.size() < limit; i++) {
                try {
                    EthBlock ethBlock = web3j.ethGetBlockByNumber(
                        DefaultBlockParameter.valueOf(BigInteger.valueOf(i)), 
                        true
                    ).send();
                    
                    if (ethBlock.getBlock() != null) {
                        List<EthBlock.TransactionResult> txResults = ethBlock.getBlock().getTransactions();
                        for (EthBlock.TransactionResult txResult : txResults) {
                            if (transactions.size() >= limit) {
                                break;
                            }
                            transactions.add((Transaction) txResult.get());
                        }
                    }
                } catch (Exception e) {
                    log.warn("获取区块 {} 失败: {}", i, e.getMessage());
                }
            }
            
            log.info("获取最近 {} 笔交易", transactions.size());
            
        } catch (Exception e) {
            log.error("获取最近交易失败", e);
        }
        
        return transactions;
    }

    @Override
    public List<Transaction> getContractInteractionHistory(String contractAddress, long startBlock, long endBlock) {
        List<Transaction> transactions = new ArrayList<>();
        
        try {
            // 遍历区块范围
            for (long i = startBlock; i <= endBlock; i++) {
                try {
                    EthBlock ethBlock = web3j.ethGetBlockByNumber(
                        DefaultBlockParameter.valueOf(BigInteger.valueOf(i)), 
                        true
                    ).send();
                    
                    if (ethBlock.getBlock() != null) {
                        List<EthBlock.TransactionResult> txResults = ethBlock.getBlock().getTransactions();
                        for (EthBlock.TransactionResult txResult : txResults) {
                            Transaction tx = (Transaction) txResult.get();
                            // 检查交易的接收者是否是目标合约
                            if (tx.getTo() != null && tx.getTo().equalsIgnoreCase(contractAddress)) {
                                transactions.add(tx);
                            }
                        }
                    }
                } catch (Exception e) {
                    log.warn("获取区块 {} 失败: {}", i, e.getMessage());
                }
            }
            
            log.info("获取合约 {} 的交互历史，从区块 {} 到 {}, 共 {} 笔交易", 
                contractAddress, startBlock, endBlock, transactions.size());
            
        } catch (Exception e) {
            log.error("获取合约交互历史失败", e);
        }
        
        return transactions;
    }

    @Override
    public List<Transaction> getNFTTransactionHistory(String nftContract, String tokenId) {
        List<Transaction> transactions = new ArrayList<>();
        
        try {
            // 获取最近1000个区块的交易
            BigInteger currentBlock = blockchainService.getCurrentBlockNumber();
            BigInteger startBlock = currentBlock.subtract(BigInteger.valueOf(1000));
            
            transactions = getContractInteractionHistory(nftContract, startBlock.longValue(), currentBlock.longValue());
            
            log.info("获取NFT合约 {} 代币 {} 的交易历史，共 {} 笔交易", 
                nftContract, tokenId, transactions.size());
            
        } catch (Exception e) {
            log.error("获取NFT交易历史失败", e);
        }
        
        return transactions;
    }
}