package com.decentralized.gaming.platform.service.blockchain;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 奖励服务接口
 * 封装奖励合约的所有操作
 *
 * @author DecentralizedGamingPlatform
 */
public interface RewardsService {

    /**
     * 获取合约拥有者
     *
     * @return 拥有者地址
     */
    String getOwner();

    /**
     * 获取代币地址
     *
     * @return 代币地址
     */
    String getToken();

    /**
     * 发放奖励
     *
     * @param credentials 发放者凭证
     * @param recipient 接收者地址
     * @param amount 奖励金额
     * @return 交易收据
     */
    TransactionReceipt issueReward(Credentials credentials, String recipient, BigInteger amount);

    /**
     * 批量发放奖励
     *
     * @param credentials 发放者凭证
     * @param recipients 接收者地址列表
     * @param amounts 奖励金额列表
     * @return 交易收据
     */
    TransactionReceipt batchIssue(Credentials credentials, List<String> recipients, List<BigInteger> amounts);

    /**
     * 转移所有权
     *
     * @param credentials 当前拥有者凭证
     * @param newOwner 新拥有者地址
     * @return 交易收据
     */
    TransactionReceipt transferOwnership(Credentials credentials, String newOwner);

    /**
     * 放弃所有权
     *
     * @param credentials 当前拥有者凭证
     * @return 交易收据
     */
    TransactionReceipt renounceOwnership(Credentials credentials);

    /**
     * 异步发放奖励
     *
     * @param credentials 发放者凭证
     * @param recipient 接收者地址
     * @param amount 奖励金额
     * @return 异步交易结果
     */
    CompletableFuture<TransactionReceipt> issueRewardAsync(Credentials credentials, String recipient, BigInteger amount);

    /**
     * 异步批量发放奖励
     *
     * @param credentials 发放者凭证
     * @param recipients 接收者地址列表
     * @param amounts 奖励金额列表
     * @return 异步交易结果
     */
    CompletableFuture<TransactionReceipt> batchIssueAsync(Credentials credentials, List<String> recipients, List<BigInteger> amounts);

    /**
     * 检查交易是否成功
     *
     * @param transactionHash 交易哈希
     * @return 是否成功
     */
    boolean isTransactionSuccessful(String transactionHash);

    /**
     * 等待交易确认
     *
     * @param transactionHash 交易哈希
     * @param maxWaitTime 最大等待时间(秒)
     * @return 交易收据
     */
    TransactionReceipt waitForTransactionReceipt(String transactionHash, int maxWaitTime);
}
