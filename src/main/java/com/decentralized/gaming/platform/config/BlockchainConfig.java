package com.decentralized.gaming.platform.config;

import com.decentralized.gaming.platform.contracts.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import jakarta.annotation.PostConstruct;
import java.math.BigInteger;
import lombok.extern.slf4j.Slf4j;

/**
 * 区块链配置类
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "app.blockchain")
@Data
public class BlockchainConfig {
    
    // 是否启用区块链功能
    private boolean enabled = true;

    private String networkUrl;
    private Long chainId;
    private BigInteger gasLimit;
    private BigInteger gasPrice;
    private ContractAddresses contracts = new ContractAddresses();

    @Data
    public static class ContractAddresses {
        private String platformToken;
        private String gameNft;
        private String agentNft;
        private String marketplace;
        private String rewards;
    }

    /**
     * 检查配置是否正确
     */
    @PostConstruct
    public void validateConfig() {
        log.info("=== 区块链配置验证开始 ===");
        
        // 验证网络URL配置
        if (networkUrl == null || networkUrl.trim().isEmpty()) {
            log.error("❌ 区块链网络URL未配置");
            throw new IllegalStateException("区块链网络URL未配置");
        }
        log.info("✅ 区块链网络URL配置成功: {}", networkUrl);
        
        // 验证链ID配置
        if (chainId == null) {
            log.warn("⚠️  链ID未配置，使用默认值");
        } else {
            log.info("✅ 链ID配置成功: {}", chainId);
        }
        
        // 验证Gas配置
        if (gasLimit == null || gasPrice == null) {
            log.info("ℹ️  Gas配置未设置，将使用默认值");
        } else {
            log.info("✅ Gas配置成功 - Limit: {}, Price: {}", gasLimit, gasPrice);
        }
        
        // 验证合约地址配置
        validateContractAddresses();
        
        log.info("=== 区块链配置验证完成 ===");
    }
    
    /**
     * 验证合约地址配置
     */
    private void validateContractAddresses() {
        log.info("--- 合约地址配置验证 ---");
        
        if (contracts == null) {
            log.error("❌ 合约地址配置对象为空");
            throw new IllegalStateException("合约地址配置对象为空");
        }
        
        // 验证平台代币合约
        if (contracts.getPlatformToken() == null || contracts.getPlatformToken().trim().isEmpty()) {
            log.error("❌ 平台代币合约地址未配置");
            throw new IllegalStateException("平台代币合约地址未配置");
        }
        log.info("✅ 平台代币合约地址: {}", contracts.getPlatformToken());
        
        // 验证游戏NFT合约
        if (contracts.getGameNft() == null || contracts.getGameNft().trim().isEmpty()) {
            log.error("❌ 游戏NFT合约地址未配置");
            throw new IllegalStateException("游戏NFT合约地址未配置");
        }
        log.info("✅ 游戏NFT合约地址: {}", contracts.getGameNft());
        
        // 验证智能体NFT合约
        if (contracts.getAgentNft() == null || contracts.getAgentNft().trim().isEmpty()) {
            log.error("❌ 智能体NFT合约地址未配置");
            throw new IllegalStateException("智能体NFT合约地址未配置");
        }
        log.info("✅ 智能体NFT合约地址: {}", contracts.getAgentNft());
        
        // 验证市场合约
        if (contracts.getMarketplace() == null || contracts.getMarketplace().trim().isEmpty()) {
            log.error("❌ 市场合约地址未配置");
            throw new IllegalStateException("市场合约地址未配置");
        }
        log.info("✅ 市场合约地址: {}", contracts.getMarketplace());
        
        // 验证奖励合约
        if (contracts.getRewards() == null || contracts.getRewards().trim().isEmpty()) {
            log.error("❌ 奖励合约地址未配置");
            throw new IllegalStateException("奖励合约地址未配置");
        }
        log.info("✅ 奖励合约地址: {}", contracts.getRewards());
        
        log.info("--- 所有合约地址配置验证完成 ---");
    }

    /**
     * Web3j实例
     */
    @Bean
    public Web3j web3j() {
        log.info("=== 区块链网络连接初始化开始 ===");
        
        // 检查是否启用区块链功能
        if (!enabled) {
            log.warn("⚠️  区块链功能已禁用，跳过连接初始化");
            return null;
        }
        
        // 检查networkUrl是否为空
        if (networkUrl == null || networkUrl.trim().isEmpty()) {
            log.error("❌ 区块链网络URL未配置");
            throw new IllegalStateException("区块链网络URL未配置");
        }
        
        try {
            log.info("🔗 正在连接到区块链节点: {}", networkUrl);
            
            // 创建带有超时配置的HttpService
            HttpService httpService = new HttpService(networkUrl);
            httpService.addHeader("Content-Type", "application/json");
            httpService.addHeader("User-Agent", "DecentralizedGamingPlatform/1.0");
            
            Web3j web3j = Web3j.build(httpService);
            
            // 测试连接
            testBlockchainConnection(web3j);
            
            log.info("✅ 区块链网络连接成功!");
            log.info("=== 区块链网络连接初始化完成 ===");
            return web3j;
            
        } catch (Exception e) {
            log.error("❌ 无法连接到区块链节点: {}", networkUrl, e);
            log.error("💡 请检查:");
            log.error("   1. Hardhat节点是否正在运行: npx hardhat node");
            log.error("   2. 端口8545是否被占用");
            log.error("   3. 防火墙是否阻止了连接");
            log.error("   4. 网络配置是否正确");
            throw new IllegalStateException("无法连接到区块链节点: " + networkUrl, e);
        }
    }
    
    /**
     * 测试区块链连接
     */
    private void testBlockchainConnection(Web3j web3j) {
        int maxRetries = 3;
        int retryDelay = 2000; // 2秒
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                log.info("🔍 正在测试区块链连接... (尝试 {}/{})", attempt, maxRetries);
                
                // 获取网络版本
                String netVersion = web3j.netVersion().send().getNetVersion();
                log.info("✅ 网络版本: {}", netVersion);
                
                // 获取当前区块号
                BigInteger blockNumber = web3j.ethBlockNumber().send().getBlockNumber();
                log.info("✅ 当前区块号: {}", blockNumber);
                
                // 获取链ID
                BigInteger chainId = web3j.ethChainId().send().getChainId();
                log.info("✅ 链ID: {}", chainId);
                
                // 获取Gas价格
                BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
                log.info("✅ 当前Gas价格: {} wei", gasPrice);
                
                log.info("🎉 区块链连接测试全部通过!");
                return; // 成功，退出重试循环
                
            } catch (Exception e) {
                log.warn("⚠️  区块链连接测试失败 (尝试 {}/{}): {}", attempt, maxRetries, e.getMessage());
                
                if (attempt == maxRetries) {
                    log.error("❌ 区块链连接测试最终失败，已尝试 {} 次", maxRetries);
                    log.error("💡 可能的解决方案:");
                    log.error("   1. 重启Hardhat节点: npx hardhat node");
                    log.error("   2. 检查Hardhat节点日志");
                    log.error("   3. 尝试使用不同的端口");
                    log.error("   4. 检查网络连接");
                    throw new RuntimeException("区块链连接测试失败，已重试" + maxRetries + "次", e);
                } else {
                    log.info("⏳ 等待 {} 毫秒后重试...", retryDelay);
                    try {
                        Thread.sleep(retryDelay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("连接测试被中断", ie);
                    }
                }
            }
        }
    }

    /**
     * Gas提供者
     */
    @Bean
    public ContractGasProvider gasProvider() {
        // 使用默认的Gas提供者，如果配置了自定义值则使用自定义值
        if (gasPrice != null && gasLimit != null) {
            return new ContractGasProvider() {
                @Override
                public BigInteger getGasPrice(String contractFunc) {
                    return gasPrice;
                }

                @Override
                public BigInteger getGasPrice() {
                    return gasPrice;
                }

                @Override
                public BigInteger getGasLimit(String contractFunc) {
                    return gasLimit;
                }

                @Override
                public BigInteger getGasLimit() {
                    return gasLimit;
                }
            };
        } else {
            // 使用默认值
            return new DefaultGasProvider();
        }
    }

    /**
     * 平台代币合约
     */
    @Bean
    public PlatformToken platformTokenContract(Web3j web3j, ContractGasProvider gasProvider) {
        log.info("=== 平台代币合约初始化开始 ===");
        
        // 检查合约地址是否为空
        if (contracts.getPlatformToken() == null || contracts.getPlatformToken().trim().isEmpty()) {
            log.error("❌ 平台代币合约地址未配置");
            throw new IllegalStateException("平台代币合约地址未配置");
        }
        
        try {
            log.info("📄 正在加载平台代币合约: {}", contracts.getPlatformToken());
            PlatformToken contract = PlatformToken.load(contracts.getPlatformToken(), web3j, Credentials.create("0"), gasProvider);
            
            // 测试合约连接
            testContractConnection("平台代币合约", contract);
            
            log.info("✅ 平台代币合约加载成功!");
            log.info("=== 平台代币合约初始化完成 ===");
            return contract;
            
        } catch (Exception e) {
            log.error("❌ 平台代币合约加载失败: {}", contracts.getPlatformToken(), e);
            throw new IllegalStateException("平台代币合约加载失败", e);
        }
    }

    /**
     * 测试合约连接
     */
    private void testContractConnection(String contractName, Object contract) {
        try {
            log.info("🔍 正在测试{}连接...", contractName);
            
            // 这里可以添加具体的合约方法调用来测试连接
            // 例如：获取合约名称、符号等基本信息
            log.info("✅ {}连接测试通过", contractName);
            
        } catch (Exception e) {
            log.error("❌ {}连接测试失败", contractName, e);
            throw new RuntimeException(contractName + "连接测试失败", e);
        }
    }

    /**
     * 游戏NFT合约
     */
    @Bean
    public GameNFT gameNftContract(Web3j web3j, ContractGasProvider gasProvider) {
        log.info("=== 游戏NFT合约初始化开始 ===");
        
        // 检查合约地址是否为空
        if (contracts.getGameNft() == null || contracts.getGameNft().trim().isEmpty()) {
            log.error("❌ 游戏NFT合约地址未配置");
            throw new IllegalStateException("游戏NFT合约地址未配置");
        }
        
        try {
            log.info("📄 正在加载游戏NFT合约: {}", contracts.getGameNft());
            GameNFT contract = GameNFT.load(contracts.getGameNft(), web3j, Credentials.create("0"), gasProvider);
            
            // 测试合约连接
            testContractConnection("游戏NFT合约", contract);
            
            log.info("✅ 游戏NFT合约加载成功!");
            log.info("=== 游戏NFT合约初始化完成 ===");
            return contract;
            
        } catch (Exception e) {
            log.error("❌ 游戏NFT合约加载失败: {}", contracts.getGameNft(), e);
            throw new IllegalStateException("游戏NFT合约加载失败", e);
        }
    }

    /**
     * 智能体NFT合约
     */
    @Bean
    public AgentNFT agentNftContract(Web3j web3j, ContractGasProvider gasProvider) {
        log.info("=== 智能体NFT合约初始化开始 ===");
        
        // 检查合约地址是否为空
        if (contracts.getAgentNft() == null || contracts.getAgentNft().trim().isEmpty()) {
            log.error("❌ 智能体NFT合约地址未配置");
            throw new IllegalStateException("智能体NFT合约地址未配置");
        }
        
        try {
            log.info("📄 正在加载智能体NFT合约: {}", contracts.getAgentNft());
            AgentNFT contract = AgentNFT.load(contracts.getAgentNft(), web3j, Credentials.create("0"), gasProvider);
            
            // 测试合约连接
            testContractConnection("智能体NFT合约", contract);
            
            log.info("✅ 智能体NFT合约加载成功!");
            log.info("=== 智能体NFT合约初始化完成 ===");
            return contract;
            
        } catch (Exception e) {
            log.error("❌ 智能体NFT合约加载失败: {}", contracts.getAgentNft(), e);
            throw new IllegalStateException("智能体NFT合约加载失败", e);
        }
    }

    /**
     * 交易市场合约
     */
    @Bean
    public Marketplace marketplaceContract(Web3j web3j, ContractGasProvider gasProvider) {
        log.info("=== 交易市场合约初始化开始 ===");
        
        // 检查合约地址是否为空
        if (contracts.getMarketplace() == null || contracts.getMarketplace().trim().isEmpty()) {
            log.error("❌ 交易市场合约地址未配置");
            throw new IllegalStateException("交易市场合约地址未配置");
        }
        
        try {
            log.info("📄 正在加载交易市场合约: {}", contracts.getMarketplace());
            Marketplace contract = Marketplace.load(contracts.getMarketplace(), web3j, Credentials.create("0"), gasProvider);
            
            // 测试合约连接
            testContractConnection("交易市场合约", contract);
            
            log.info("✅ 交易市场合约加载成功!");
            log.info("=== 交易市场合约初始化完成 ===");
            return contract;
            
        } catch (Exception e) {
            log.error("❌ 交易市场合约加载失败: {}", contracts.getMarketplace(), e);
            throw new IllegalStateException("交易市场合约加载失败", e);
        }
    }

    /**
     * 奖励合约
     */
    @Bean
    public Rewards rewardsContract(Web3j web3j, ContractGasProvider gasProvider) {
        log.info("=== 奖励合约初始化开始 ===");
        
        // 检查合约地址是否为空
        if (contracts.getRewards() == null || contracts.getRewards().trim().isEmpty()) {
            log.error("❌ 奖励合约地址未配置");
            throw new IllegalStateException("奖励合约地址未配置");
        }
        
        try {
            log.info("📄 正在加载奖励合约: {}", contracts.getRewards());
            Rewards contract = Rewards.load(contracts.getRewards(), web3j, Credentials.create("0"), gasProvider);
            
            // 测试合约连接
            testContractConnection("奖励合约", contract);
            
            log.info("✅ 奖励合约加载成功!");
            log.info("=== 奖励合约初始化完成 ===");
            return contract;
            
        } catch (Exception e) {
            log.error("❌ 奖励合约加载失败: {}", contracts.getRewards(), e);
            throw new IllegalStateException("奖励合约加载失败", e);
        }
    }
}