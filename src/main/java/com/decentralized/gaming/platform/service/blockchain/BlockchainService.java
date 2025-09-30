package com.decentralized.gaming.platform.service.blockchain;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 区块链基础服务接口
 *
 * @author DecentralizedGamingPlatform
 */
public interface BlockchainService {

    /**
     * 验证钱包地址签名
     *
     * @param message 原始消息
     * @param signature 签名
     * @param address 钱包地址
     * @return 验证结果
     */
    boolean verifySignature(String message, String signature, String address);

    /**
     * 获取账户余额
     *
     * @param address 钱包地址
     * @return 余额(ETH)
     */
    BigDecimal getBalance(String address);

    /**
     * 等待交易确认
     *
     * @param transactionHash 交易哈希
     * @param maxWaitTime 最大等待时间(秒)
     * @return 交易收据
     */
    TransactionReceipt waitForTransactionReceipt(String transactionHash, int maxWaitTime);

    /**
     * 检查交易是否成功
     *
     * @param transactionHash 交易哈希
     * @return 是否成功
     */
    boolean isTransactionSuccessful(String transactionHash);

    /**
     * 获取当前区块号
     *
     * @return 区块号
     */
    BigInteger getCurrentBlockNumber();

    /**
     * 检查网络连接
     *
     * @return 是否连接成功
     */
    boolean isConnected();

    /**
     * 获取当前Gas价格
     *
     * @return Gas价格(wei)
     */
    BigInteger getGasPrice();
}
