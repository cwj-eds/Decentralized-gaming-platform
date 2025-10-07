package com.decentralized.gaming.platform.service.blockchain.impl;

import com.decentralized.gaming.platform.exception.BlockchainException;
import com.decentralized.gaming.platform.service.blockchain.BlockchainService;
import com.decentralized.gaming.platform.service.blockchain.BlockchainRetryService;
import com.decentralized.gaming.platform.service.blockchain.BlockchainCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * 区块链基础服务实现类
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
public class BlockchainServiceImpl implements BlockchainService {

    @Autowired(required = false)
    private Web3j web3j;

    @Autowired
    private BlockchainRetryService retryService;

    @Autowired
    private BlockchainCacheService cacheService;

    @Value("${app.blockchain.gas.price:20000000000}")
    private Long defaultGasPrice;

    /**
     * 验证钱包地址签名
     *
     * @param message 原始消息
     * @param signature 签名
     * @param address 钱包地址
     * @return 验证结果
     */
    @Override
    public boolean verifySignature(String message, String signature, String address) {
        try {
            // 将消息转换为字节数组
            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);

            // 解析签名
            byte[] signatureBytes = Numeric.hexStringToByteArray(signature);

            // 恢复签名者地址
            Sign.SignatureData signatureData = new Sign.SignatureData(
                    signatureBytes[64],
                    java.util.Arrays.copyOfRange(signatureBytes, 0, 32),
                    java.util.Arrays.copyOfRange(signatureBytes, 32, 64)
            );

            // 验证签名
            BigInteger recoveredKey = Sign.signedMessageToKey(messageBytes, signatureData);
            String recoveredAddress = "0x" + Keys.getAddress(recoveredKey);

            return recoveredAddress.equalsIgnoreCase(address);
        } catch (Exception e) {
            log.error("签名验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取账户余额
     *
     * @param address 钱包地址
     * @return 余额(ETH)
     */
    @Override
    public BigDecimal getBalance(String address) {
        // 先尝试从缓存获取
        BigDecimal cachedBalance = cacheService.getCachedBalance(address);
        if (cachedBalance != null) {
            return cachedBalance;
        }

        // 从区块链获取余额
        return retryService.executeWithRetry(() -> {
            try {
                // 检查web3j实例是否为空
                if (web3j == null) {
                    throw new RuntimeException("Web3j实例未初始化");
                }
                
                EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
                BigInteger balance = ethGetBalance.getBalance();
                BigDecimal ethBalance = Convert.fromWei(balance.toString(), Convert.Unit.ETHER);
                
                // 缓存结果
                cacheService.cacheBalance(address, ethBalance);
                
                return ethBalance;
            } catch (Exception e) {
                log.error("获取余额失败: {}", e.getMessage());
                throw new RuntimeException("获取余额失败", e);
            }
        }, "获取余额");
    }

    /**
     * 等待交易确认
     *
     * @param transactionHash 交易哈希
     * @param maxWaitTime 最大等待时间(秒)
     * @return 交易收据
     */
    @Override
    public TransactionReceipt waitForTransactionReceipt(String transactionHash, int maxWaitTime) {
        try {
            // 检查web3j实例是否为空
            if (web3j == null) {
                throw new RuntimeException("Web3j实例未初始化");
            }
            
            return web3j.ethGetTransactionReceipt(transactionHash)
                    .send()
                    .getTransactionReceipt()
                    .orElse(null);
        } catch (Exception e) {
            log.error("等待交易确认失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 检查交易是否成功
     *
     * @param transactionHash 交易哈希
     * @return 是否成功
     */
    @Override
    public boolean isTransactionSuccessful(String transactionHash) {
        TransactionReceipt receipt = waitForTransactionReceipt(transactionHash, 30);
        return receipt != null && receipt.isStatusOK();
    }

    /**
     * 获取当前区块号
     *
     * @return 区块号
     */
    @Override
    public BigInteger getCurrentBlockNumber() {
        // 先尝试从缓存获取
        BigInteger cachedBlockNumber = cacheService.getCachedBlockNumber();
        if (cachedBlockNumber != null) {
            return cachedBlockNumber;
        }

        // 从区块链获取区块号
        return retryService.executeWithRetry(() -> {
            try {
                // 检查web3j实例是否为空
                if (web3j == null) {
                    log.error("Web3j实例未初始化，可能是因为区块链节点URL配置错误");
                    throw new RuntimeException("Web3j实例未初始化，可能是因为区块链节点URL配置错误");
                }
                
                // 检查web3j服务是否正确初始化
                if (web3j.ethBlockNumber() == null) {
                    log.error("无法创建区块号请求，可能是因为区块链节点URL配置错误");
                    throw new RuntimeException("无法创建区块号请求，可能是因为区块链节点URL配置错误");
                }
                
                BigInteger blockNumber = web3j.ethBlockNumber().send().getBlockNumber();
                
                // 缓存结果
                cacheService.cacheBlockNumber(blockNumber);
                
                return blockNumber;
            } catch (Exception e) {
                log.error("获取当前区块号失败: {}", e.getMessage(), e);
                throw new RuntimeException("获取当前区块号失败", e);
            }
        }, "获取当前区块号");
    }

    /**
     * 检查网络连接
     *
     * @return 是否连接成功
     */
    @Override
    public boolean isConnected() {
        try {
            // 检查web3j实例是否为空
            if (web3j == null) {
                log.error("Web3j实例未初始化，可能是因为区块链节点URL配置错误");
                return false;
            }
            
            return web3j.ethBlockNumber().send().getBlockNumber().compareTo(BigInteger.ZERO) >= 0;
        } catch (Exception e) {
            log.error("网络连接检查失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取当前Gas价格
     *
     * @return Gas价格(wei)
     */
    @Override
    public BigInteger getGasPrice() {
        // 先尝试从缓存获取
        BigInteger cachedGasPrice = cacheService.getCachedGasPrice();
        if (cachedGasPrice != null) {
            return cachedGasPrice;
        }

        // 从区块链获取Gas价格，失败时使用默认值
        return retryService.executeWithFallback(
            () -> {
                try {
                    // 检查web3j实例是否为空
                    if (web3j == null) {
                        log.error("Web3j实例未初始化，可能是因为区块链节点URL配置错误");
                        throw new RuntimeException("Web3j实例未初始化，可能是因为区块链节点URL配置错误");
                    }
                    
                    BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
                    
                    // 缓存结果
                    cacheService.cacheGasPrice(gasPrice);
                    
                    return gasPrice;
                } catch (Exception e) {
                    log.error("获取Gas价格失败: {}", e.getMessage());
                    throw new RuntimeException("获取Gas价格失败", e);
                }
            },
            () -> {
                log.warn("使用默认Gas价格: {}", defaultGasPrice);
                return BigInteger.valueOf(defaultGasPrice);
            },
            "获取Gas价格"
        );
    }
}