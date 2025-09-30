package com.decentralized.gaming.platform.service.blockchain.impl;

import com.decentralized.gaming.platform.contracts.Rewards;
import com.decentralized.gaming.platform.exception.BlockchainException;
import com.decentralized.gaming.platform.service.blockchain.BlockchainService;
import com.decentralized.gaming.platform.service.blockchain.RewardsService;
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
 * 奖励服务实现类
 * 封装奖励合约的所有操作
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
public class RewardsServiceImpl implements RewardsService {

    @Autowired
    private Rewards rewardsContract;

    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private ContractGasProvider gasProvider;

    @Autowired
    private org.web3j.protocol.Web3j web3j;

    @Override
    public String getOwner() {
        try {
            return rewardsContract.owner().send();
        } catch (Exception e) {
            log.error("获取合约拥有者失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "owner", "获取合约拥有者失败", e);
        }
    }

    @Override
    public String getToken() {
        try {
            return rewardsContract.token().send();
        } catch (Exception e) {
            log.error("获取代币地址失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "token", "获取代币地址失败", e);
        }
    }

    @Override
    public TransactionReceipt issueReward(Credentials credentials, String recipient, BigInteger amount) {
        try {
            Rewards contract = Rewards.load(
                rewardsContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.issueReward(recipient, amount, "Platform reward").send();
            log.info("发放奖励成功: 接收者: {}, 金额: {}, 交易哈希: {}", 
                recipient, amount, receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("发放奖励失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "issueReward", "发放奖励失败", e);
        }
    }

    @Override
    public TransactionReceipt batchIssue(Credentials credentials, List<String> recipients, List<BigInteger> amounts) {
        try {
            Rewards contract = Rewards.load(
                rewardsContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.batchIssue(recipients, amounts, "Batch platform reward").send();
            log.info("批量发放奖励成功: 接收者数量: {}, 交易哈希: {}", 
                recipients.size(), receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("批量发放奖励失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "batchIssueReward", "批量发放奖励失败", e);
        }
    }

    @Override
    public TransactionReceipt transferOwnership(Credentials credentials, String newOwner) {
        try {
            Rewards contract = Rewards.load(
                rewardsContract.getContractAddress(),
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
            Rewards contract = Rewards.load(
                rewardsContract.getContractAddress(),
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
    public CompletableFuture<TransactionReceipt> issueRewardAsync(Credentials credentials, String recipient, BigInteger amount) {
        return CompletableFuture.supplyAsync(() -> issueReward(credentials, recipient, amount));
    }

    @Override
    public CompletableFuture<TransactionReceipt> batchIssueAsync(Credentials credentials, List<String> recipients, List<BigInteger> amounts) {
        return CompletableFuture.supplyAsync(() -> batchIssue(credentials, recipients, amounts));
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
