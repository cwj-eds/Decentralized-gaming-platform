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

import java.math.BigInteger;

/**
 * 区块链配置类
 *
 * @author DecentralizedGamingPlatform
 */
@Configuration
@ConfigurationProperties(prefix = "app.blockchain")
@Data
public class BlockchainConfig {

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
        private String gameItemNft;
        private String marketplace;
    }

    /**
     * Web3j实例
     */
    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(networkUrl));
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
        return PlatformToken.load(contracts.getPlatformToken(), web3j, Credentials.create("0"), gasProvider);
    }

    /**
     * 游戏NFT合约
     */
    @Bean
    public GameNFT gameNftContract(Web3j web3j, ContractGasProvider gasProvider) {
        return GameNFT.load(contracts.getGameNft(), web3j, Credentials.create("0"), gasProvider);
    }

    /**
     * 智能体NFT合约
     */
    @Bean
    public AgentNFT agentNftContract(Web3j web3j, ContractGasProvider gasProvider) {
        return AgentNFT.load(contracts.getAgentNft(), web3j, Credentials.create("0"), gasProvider);
    }

    /**
     * 游戏道具NFT合约
     */
    @Bean
    public GameItemNFT gameItemNftContract(Web3j web3j, ContractGasProvider gasProvider) {
        return GameItemNFT.load(contracts.getGameItemNft(), web3j, Credentials.create("0"), gasProvider);
    }

    /**
     * 交易市场合约
     */
    @Bean
    public Marketplace marketplaceContract(Web3j web3j, ContractGasProvider gasProvider) {
        return Marketplace.load(contracts.getMarketplace(), web3j, Credentials.create("0"), gasProvider);
    }
}