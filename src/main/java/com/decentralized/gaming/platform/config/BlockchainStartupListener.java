package com.decentralized.gaming.platform.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;

/**
 * 区块链启动监听器
 * 在应用启动完成后显示区块链服务的状态
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Component
public class BlockchainStartupListener {

    @Autowired(required = false)
    private Web3j web3j;

    @Autowired
    private BlockchainConfig blockchainConfig;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("🚀 ==========================================");
        log.info("🚀 应用启动完成 - 区块链服务状态检查");
        log.info("🚀 ==========================================");
        
        // 检查区块链功能是否启用
        if (!blockchainConfig.isEnabled()) {
            log.warn("⚠️  区块链功能已禁用");
            log.info("💡 如需启用区块链功能，请设置 app.blockchain.enabled=true");
            return;
        }
        
        // 检查Web3j连接状态
        if (web3j == null) {
            log.error("❌ 区块链服务不可用");
            log.error("💡 可能的原因:");
            log.error("   1. Hardhat节点未启动: npx hardhat node");
            log.error("   2. 网络连接问题");
            log.error("   3. 配置错误");
            log.warn("⚠️  应用将以降级模式运行，区块链相关功能将被禁用");
        } else {
            log.info("✅ 区块链服务已就绪");
            log.info("🌐 网络URL: {}", blockchainConfig.getNetworkUrl());
            log.info("🔗 链ID: {}", blockchainConfig.getChainId());
            
            // 显示合约地址信息
            if (blockchainConfig.getContracts() != null) {
                log.info("📄 已配置的合约地址:");
                if (blockchainConfig.getContracts().getPlatformToken() != null) {
                    log.info("   💰 平台代币: {}", blockchainConfig.getContracts().getPlatformToken());
                }
                if (blockchainConfig.getContracts().getGameNft() != null) {
                    log.info("   🎮 游戏NFT: {}", blockchainConfig.getContracts().getGameNft());
                }
                if (blockchainConfig.getContracts().getAgentNft() != null) {
                    log.info("   🤖 智能体NFT: {}", blockchainConfig.getContracts().getAgentNft());
                }
                if (blockchainConfig.getContracts().getMarketplace() != null) {
                    log.info("   🛒 市场合约: {}", blockchainConfig.getContracts().getMarketplace());
                }
                if (blockchainConfig.getContracts().getRewards() != null) {
                    log.info("   🎁 奖励合约: {}", blockchainConfig.getContracts().getRewards());
                }
            }
        }
        
        log.info("🚀 ==========================================");
        log.info("🚀 区块链服务状态检查完成");
        log.info("🚀 ==========================================");
    }
}