package com.decentralized.gaming.platform.service.blockchain;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

/**
 * 市场服务接口
 * 封装市场合约的所有操作
 *
 * @author DecentralizedGamingPlatform
 */
public interface MarketplaceService {

    /**
     * 获取合约拥有者
     *
     * @return 拥有者地址
     */
    String getOwner();

    /**
     * 获取平台代币地址
     *
     * @return 平台代币地址
     */
    String getPlatformToken();

    /**
     * 获取费用接收者
     *
     * @return 费用接收者地址
     */
    String getFeeRecipient();

    /**
     * 获取费用基点
     *
     * @return 费用基点
     */
    BigInteger getFeeBasisPoints();

    /**
     * 获取上架计数器
     *
     * @return 上架计数器
     */
    BigInteger getListingCounter();

    /**
     * 上架ERC721代币
     *
     * @param credentials 上架者凭证
     * @param nftContract NFT合约地址
     * @param tokenId 代币ID
     * @param price 价格
     * @return 交易收据
     */
    TransactionReceipt listERC721(Credentials credentials, String nftContract, BigInteger tokenId, BigInteger price);

    /**
     * 上架ERC1155代币
     *
     * @param credentials 上架者凭证
     * @param nftContract NFT合约地址
     * @param tokenId 代币ID
     * @param quantity 数量
     * @param price 价格
     * @return 交易收据
     */
    TransactionReceipt listERC1155(Credentials credentials, String nftContract, BigInteger tokenId, BigInteger quantity, BigInteger price);

    /**
     * 购买代币
     *
     * @param credentials 购买者凭证
     * @param listingId 上架ID
     * @return 交易收据
     */
    TransactionReceipt buy(Credentials credentials, BigInteger listingId);

    /**
     * 取消上架
     *
     * @param credentials 上架者凭证
     * @param listingId 上架ID
     * @return 交易收据
     */
    TransactionReceipt cancelListing(Credentials credentials, BigInteger listingId);

    /**
     * 设置费用基点
     *
     * @param credentials 管理员凭证
     * @param newFeeBasisPoints 新费用基点
     * @return 交易收据
     */
    TransactionReceipt setFeeBasisPoints(Credentials credentials, BigInteger newFeeBasisPoints);

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
     * ERC1155接收回调
     *
     * @param operator 操作者地址
     * @param from 发送者地址
     * @param id 代币ID
     * @param value 数量
     * @param data 附加数据
     * @return 选择器
     */
    byte[] onERC1155Received(String operator, String from, BigInteger id, BigInteger value, byte[] data);

    /**
     * ERC1155批量接收回调
     *
     * @param operator 操作者地址
     * @param from 发送者地址
     * @param ids 代币ID数组
     * @param values 数量数组
     * @param data 附加数据
     * @return 选择器
     */
    byte[] onERC1155BatchReceived(String operator, String from, BigInteger[] ids, BigInteger[] values, byte[] data);

    /**
     * 异步上架ERC721代币
     *
     * @param credentials 上架者凭证
     * @param nftContract NFT合约地址
     * @param tokenId 代币ID
     * @param price 价格
     * @return 异步交易结果
     */
    CompletableFuture<TransactionReceipt> listERC721Async(Credentials credentials, String nftContract, BigInteger tokenId, BigInteger price);

    /**
     * 异步购买代币
     *
     * @param credentials 购买者凭证
     * @param listingId 上架ID
     * @return 异步交易结果
     */
    CompletableFuture<TransactionReceipt> buyAsync(Credentials credentials, BigInteger listingId);

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
