package com.decentralized.gaming.platform.service.blockchain.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * 区块链服务降级处理器
 * 当区块链连接不可用时，提供默认的响应和错误处理
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
public class BlockchainFallbackService {

    /**
     * 检查区块链服务是否可用
     */
    public boolean isBlockchainAvailable() {
        // 这里可以添加更复杂的检查逻辑
        // 例如检查Web3j Bean是否为null，或者检查网络连接状态
        return false; // 默认返回false，表示区块链服务不可用
    }

    /**
     * 获取默认的游戏NFT铸造费用
     */
    public BigInteger getDefaultGameMintFee() {
        log.warn("⚠️  区块链服务不可用，返回默认游戏NFT铸造费用");
        return BigInteger.valueOf(2000000000000000000L); // 2 ETH
    }

    /**
     * 获取默认的智能体NFT铸造费用
     */
    public BigInteger getDefaultAgentMintFee() {
        log.warn("⚠️  区块链服务不可用，返回默认智能体NFT铸造费用");
        return BigInteger.valueOf(1000000000000000000L); // 1 ETH
    }

    /**
     * 获取默认的费用接收者地址
     */
    public String getDefaultFeeRecipient() {
        log.warn("⚠️  区块链服务不可用，返回默认费用接收者地址");
        return "0x0000000000000000000000000000000000000000";
    }

    /**
     * 获取默认的平台代币地址
     */
    public String getDefaultPlatformToken() {
        log.warn("⚠️  区块链服务不可用，返回默认平台代币地址");
        return "0x0000000000000000000000000000000000000000";
    }

    /**
     * 抛出区块链服务不可用的异常
     */
    public void throwBlockchainUnavailableException(String operation) {
        String message = String.format("区块链服务不可用，无法执行操作: %s", operation);
        log.error("❌ {}", message);
        throw new RuntimeException(message);
    }

    /**
     * 记录区块链服务不可用的警告
     */
    public void logBlockchainUnavailable(String operation) {
        log.warn("⚠️  区块链服务不可用，操作 {} 将被跳过", operation);
    }
}
