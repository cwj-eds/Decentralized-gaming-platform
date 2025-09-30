package com.decentralized.gaming.platform.service.blockchain;

import org.web3j.protocol.core.methods.response.Transaction;

import java.util.List;

/**
 * 交易历史服务接口
 * 提供交易历史查询功能
 *
 * @author DecentralizedGamingPlatform
 */
public interface TransactionHistoryService {

    /**
     * 获取地址交易历史
     *
     * @param address 地址
     * @param startBlock 起始区块
     * @param endBlock 结束区块
     * @return 交易列表
     */
    List<Transaction> getAddressTransactionHistory(String address, long startBlock, long endBlock);

    /**
     * 获取最近交易
     *
     * @param limit 限制数量
     * @return 交易列表
     */
    List<Transaction> getRecentTransactions(int limit);

    /**
     * 获取合约交互历史
     *
     * @param contractAddress 合约地址
     * @param startBlock 起始区块
     * @param endBlock 结束区块
     * @return 交易列表
     */
    List<Transaction> getContractInteractionHistory(String contractAddress, long startBlock, long endBlock);

    /**
     * 获取NFT交易历史
     *
     * @param nftContract NFT合约地址
     * @param tokenId 代币ID
     * @return 交易列表
     */
    List<Transaction> getNFTTransactionHistory(String nftContract, String tokenId);
}
