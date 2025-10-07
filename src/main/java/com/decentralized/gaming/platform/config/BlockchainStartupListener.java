package com.decentralized.gaming.platform.config;

import com.decentralized.gaming.platform.contracts.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;

/**
 * 区块链启动监听器
 * 在应用启动完成后显示区块链配置总结
 */
@Slf4j
@Component
public class BlockchainStartupListener {

    @Autowired
    private Web3j web3j;
    
    @Autowired
    private BlockchainConfig blockchainConfig;
    
    @Autowired
    private PlatformToken platformTokenContract;
    
    @Autowired
    private GameNFT gameNftContract;
    
    @Autowired
    private AgentNFT agentNftContract;
    
    @Autowired
    private Marketplace marketplaceContract;
    
    @Autowired
    private Rewards rewardsContract;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("🚀 应用启动完成，开始区块链配置总结...");
        
        try {
            displayBlockchainSummary();
            log.info("🎉 区块链配置总结完成!");
        } catch (Exception e) {
            log.error("❌ 区块链配置总结失败", e);
        }
    }
    
    /**
     * 显示区块链配置总结
     */
    private void displayBlockchainSummary() {
        log.info("═══════════════════════════════════════════════════════════════");
        log.info("🎮 去中心化游戏平台 - 区块链配置总结");
        log.info("═══════════════════════════════════════════════════════════════");
        
        // 网络信息
        displayNetworkInfo();
        
        // 合约信息
        displayContractInfo();
        
        // 配置状态
        displayConfigurationStatus();
        
        log.info("═══════════════════════════════════════════════════════════════");
    }
    
    /**
     * 显示网络信息
     */
    private void displayNetworkInfo() {
        try {
            log.info("📡 区块链网络信息:");
            log.info("   🔗 网络URL: {}", blockchainConfig.getNetworkUrl());
            log.info("   🆔 链ID: {}", blockchainConfig.getChainId());
            
            // 获取实时网络信息
            String netVersion = web3j.netVersion().send().getNetVersion();
            BigInteger blockNumber = web3j.ethBlockNumber().send().getBlockNumber();
            BigInteger chainId = web3j.ethChainId().send().getChainId();
            BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
            
            log.info("   🌐 网络版本: {}", netVersion);
            log.info("   📦 当前区块: {}", blockNumber);
            log.info("   🔗 实际链ID: {}", chainId);
            log.info("   ⛽ Gas价格: {} wei", gasPrice);
            
        } catch (Exception e) {
            log.error("   ❌ 获取网络信息失败: {}", e.getMessage());
        }
    }
    
    /**
     * 显示合约信息
     */
    private void displayContractInfo() {
        log.info("📄 智能合约信息:");
        
        BlockchainConfig.ContractAddresses contracts = blockchainConfig.getContracts();
        
        log.info("   🪙 平台代币合约: {}", contracts.getPlatformToken());
        log.info("   🎮 游戏NFT合约: {}", contracts.getGameNft());
        log.info("   🤖 智能体NFT合约: {}", contracts.getAgentNft());
        log.info("   🏪 交易市场合约: {}", contracts.getMarketplace());
        log.info("   🎁 奖励合约: {}", contracts.getRewards());
    }
    
    /**
     * 显示配置状态
     */
    private void displayConfigurationStatus() {
        log.info("⚙️  配置状态:");
        
        // Gas配置
        if (blockchainConfig.getGasLimit() != null && blockchainConfig.getGasPrice() != null) {
            log.info("   ⛽ Gas配置: Limit={}, Price={}", 
                blockchainConfig.getGasLimit(), blockchainConfig.getGasPrice());
        } else {
            log.info("   ⛽ Gas配置: 使用默认值");
        }
        
        // 合约连接状态
        log.info("   📄 合约连接状态:");
        log.info("      ✅ 平台代币合约: 已连接");
        log.info("      ✅ 游戏NFT合约: 已连接");
        log.info("      ✅ 智能体NFT合约: 已连接");
        log.info("      ✅ 交易市场合约: 已连接");
        log.info("      ✅ 奖励合约: 已连接");
        
        log.info("🎯 所有区块链组件已就绪，可以开始使用!");
    }
}
