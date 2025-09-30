package com.decentralized.gaming.platform.service.blockchain;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

/**
 * 平台代币服务接口
 * 封装平台代币合约的所有操作
 *
 * @author DecentralizedGamingPlatform
 */
public interface PlatformTokenService {

    /**
     * 获取代币名称
     *
     * @return 代币名称
     */
    String getName();

    /**
     * 获取代币符号
     *
     * @return 代币符号
     */
    String getSymbol();

    /**
     * 获取代币精度
     *
     * @return 代币精度
     */
    BigInteger getDecimals();

    /**
     * 获取代币总供应量
     *
     * @return 总供应量
     */
    BigInteger getTotalSupply();

    /**
     * 获取账户余额
     *
     * @param address 账户地址
     * @return 余额
     */
    BigInteger getBalance(String address);

    /**
     * 获取授权额度
     *
     * @param owner 拥有者地址
     * @param spender 被授权者地址
     * @return 授权额度
     */
    BigInteger getAllowance(String owner, String spender);

    /**
     * 转账
     *
     * @param credentials 发送者凭证
     * @param to 接收者地址
     * @param amount 转账金额
     * @return 交易收据
     */
    TransactionReceipt transfer(Credentials credentials, String to, BigInteger amount);

    /**
     * 授权转账
     *
     * @param credentials 授权者凭证
     * @param spender 被授权者地址
     * @param amount 授权金额
     * @return 交易收据
     */
    TransactionReceipt approve(Credentials credentials, String spender, BigInteger amount);

    /**
     * 代理转账
     *
     * @param credentials 代理者凭证
     * @param from 发送者地址
     * @param to 接收者地址
     * @param amount 转账金额
     * @return 交易收据
     */
    TransactionReceipt transferFrom(Credentials credentials, String from, String to, BigInteger amount);

    /**
     * 铸造代币
     *
     * @param credentials 操作者凭证
     * @param to 接收者地址
     * @param amount 铸造金额
     * @return 交易收据
     */
    TransactionReceipt mint(Credentials credentials, String to, BigInteger amount);

    /**
     * 销毁代币
     *
     * @param credentials 操作者凭证
     * @param amount 销毁金额
     * @return 交易收据
     */
    TransactionReceipt burn(Credentials credentials, BigInteger amount);

    /**
     * 管理员销毁代币
     *
     * @param credentials 管理员凭证
     * @param account 销毁者地址
     * @param amount 销毁金额
     * @return 交易收据
     */
    TransactionReceipt adminBurn(Credentials credentials, String account, BigInteger amount);

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
     * 获取合约拥有者
     *
     * @return 拥有者地址
     */
    String getOwner();

    /**
     * 异步转账
     *
     * @param credentials 发送者凭证
     * @param to 接收者地址
     * @param amount 转账金额
     * @return 异步交易结果
     */
    CompletableFuture<TransactionReceipt> transferAsync(Credentials credentials, String to, BigInteger amount);

    /**
     * 异步授权
     *
     * @param credentials 授权者凭证
     * @param spender 被授权者地址
     * @param amount 授权金额
     * @return 异步交易结果
     */
    CompletableFuture<TransactionReceipt> approveAsync(Credentials credentials, String spender, BigInteger amount);

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
