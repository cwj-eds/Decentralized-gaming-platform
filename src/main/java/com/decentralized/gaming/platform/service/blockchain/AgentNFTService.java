package com.decentralized.gaming.platform.service.blockchain;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

/**
 * 代理NFT服务接口
 * 封装代理NFT合约的所有操作
 *
 * @author DecentralizedGamingPlatform
 */
public interface AgentNFTService {

    /**
     * 获取NFT名称
     *
     * @return NFT名称
     */
    String getName();

    /**
     * 获取NFT符号
     *
     * @return NFT符号
     */
    String getSymbol();

    /**
     * 获取NFT拥有者
     *
     * @param tokenId 代币ID
     * @return 拥有者地址
     */
    String getOwnerOf(BigInteger tokenId);

    /**
     * 获取账户NFT余额
     *
     * @param owner 拥有者地址
     * @return 余额
     */
    BigInteger getBalanceOf(String owner);

    /**
     * 获取代币URI
     *
     * @param tokenId 代币ID
     * @return 代币URI
     */
    String getTokenURI(BigInteger tokenId);

    /**
     * 获取授权地址
     *
     * @param tokenId 代币ID
     * @return 授权地址
     */
    String getApproved(BigInteger tokenId);

    /**
     * 检查是否授权给操作者
     *
     * @param owner 拥有者地址
     * @param operator 操作者地址
     * @return 是否授权
     */
    boolean isApprovedForAll(String owner, String operator);

    /**
     * 创建代理
     *
     * @param credentials 创建者凭证
     * @param agentName 代理名称
     * @param agentDescription 代理描述
     * @param agentImageUrl 代理图片URL
     * @param agentUrl 代理URL
     * @param uploadFee 上传费用
     * @return 交易收据
     */
    TransactionReceipt createAgent(Credentials credentials, String agentName, String agentDescription, 
                                  String agentImageUrl, String agentUrl, BigInteger uploadFee);

    /**
     * 授权代币
     *
     * @param credentials 拥有者凭证
     * @param to 被授权者地址
     * @param tokenId 代币ID
     * @return 交易收据
     */
    TransactionReceipt approve(Credentials credentials, String to, BigInteger tokenId);

    /**
     * 设置操作者授权
     *
     * @param credentials 拥有者凭证
     * @param operator 操作者地址
     * @param approved 是否授权
     * @return 交易收据
     */
    TransactionReceipt setApprovalForAll(Credentials credentials, String operator, boolean approved);

    /**
     * 转账代币
     *
     * @param credentials 发送者凭证
     * @param from 发送者地址
     * @param to 接收者地址
     * @param tokenId 代币ID
     * @return 交易收据
     */
    TransactionReceipt transferFrom(Credentials credentials, String from, String to, BigInteger tokenId);

    /**
     * 安全转账代币
     *
     * @param credentials 发送者凭证
     * @param from 发送者地址
     * @param to 接收者地址
     * @param tokenId 代币ID
     * @param data 附加数据
     * @return 交易收据
     */
    TransactionReceipt safeTransferFrom(Credentials credentials, String from, String to, BigInteger tokenId, byte[] data);

    /**
     * 管理员铸造代理
     *
     * @param credentials 管理员凭证
     * @param to 接收者地址
     * @param agentName 代理名称
     * @param agentDescription 代理描述
     * @param agentImageUrl 代理图片URL
     * @param agentUrl 代理URL
     * @return 交易收据
     */
    TransactionReceipt adminMintAgent(Credentials credentials, String to, String agentName, String agentDescription, 
                                     String agentImageUrl, String agentUrl);

    /**
     * 设置上传费用
     *
     * @param credentials 管理员凭证
     * @param newUploadFee 新上传费用
     * @return 交易收据
     */
    TransactionReceipt setUploadFee(Credentials credentials, BigInteger newUploadFee);

    /**
     * 设置费用接收者
     *
     * @param credentials 管理员凭证
     * @param newFeeRecipient 新费用接收者地址
     * @return 交易收据
     */
    TransactionReceipt setFeeRecipient(Credentials credentials, String newFeeRecipient);

    /**
     * 设置平台代币
     *
     * @param credentials 管理员凭证
     * @param newPlatformToken 新平台代币地址
     * @return 交易收据
     */
    TransactionReceipt setPlatformToken(Credentials credentials, String newPlatformToken);

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
     * 获取上传费用
     *
     * @return 上传费用
     */
    BigInteger getUploadFee();

    /**
     * 获取费用接收者
     *
     * @return 费用接收者地址
     */
    String getFeeRecipient();

    /**
     * 获取平台代币地址
     *
     * @return 平台代币地址
     */
    String getPlatformToken();

    /**
     * 检查是否支持接口
     *
     * @param interfaceId 接口ID
     * @return 是否支持
     */
    boolean supportsInterface(byte[] interfaceId);

    /**
     * 异步创建代理
     *
     * @param credentials 创建者凭证
     * @param agentName 代理名称
     * @param agentDescription 代理描述
     * @param agentImageUrl 代理图片URL
     * @param agentUrl 代理URL
     * @param uploadFee 上传费用
     * @return 异步交易结果
     */
    CompletableFuture<TransactionReceipt> createAgentAsync(Credentials credentials, String agentName, String agentDescription, 
                                                          String agentImageUrl, String agentUrl, BigInteger uploadFee);

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
