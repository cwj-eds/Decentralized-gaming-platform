package com.decentralized.gaming.platform.service.blockchain.impl;

import com.decentralized.gaming.platform.service.blockchain.ContractConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 合约配置服务实现类
 * 管理智能合约的地址和基本信息
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
public class ContractConfigServiceImpl implements ContractConfigService {

    @Value("${app.blockchain.contracts.platformToken:}")
    private String platformTokenAddress;

    @Value("${app.blockchain.contracts.gameNft:}")
    private String gameNFTAddress;

    @Value("${app.blockchain.contracts.agentNft:}")
    private String agentNFTAddress;

    @Value("${app.blockchain.contracts.marketplace:}")
    private String marketplaceAddress;

    @Value("${app.blockchain.contracts.rewards:}")
    private String rewardsAddress;

    @Value("${app.blockchain.networkUrl:http://localhost:8545}")
    private String networkUrl;

    @Value("${app.blockchain.chainId:31337}")
    private String chainId;

    // 合约信息配置
    @Value("${app.blockchain.contract-info.platform-token.name:Platform Token}")
    private String platformTokenName;

    @Value("${app.blockchain.contract-info.platform-token.symbol:PLT}")
    private String platformTokenSymbol;

    @Value("${app.blockchain.contract-info.platform-token.decimals:18}")
    private Integer platformTokenDecimals;

    @Value("${app.blockchain.contract-info.platform-token.type:ERC20}")
    private String platformTokenType;

    @Value("${app.blockchain.contract-info.platform-token.total-supply:1000000000000000000000000000}")
    private String platformTokenTotalSupply;

    @Value("${app.blockchain.contract-info.platform-token.base-uri:https://api.gaming-platform.com/metadata/token/}")
    private String platformTokenBaseUri;

    @Value("${app.blockchain.contract-info.game-nft.name:Game NFT}")
    private String gameNFTName;

    @Value("${app.blockchain.contract-info.game-nft.symbol:GAME}")
    private String gameNFTSymbol;

    @Value("${app.blockchain.contract-info.game-nft.type:ERC721}")
    private String gameNFTType;

    @Value("${app.blockchain.contract-info.game-nft.base-uri:https://api.gaming-platform.com/metadata/game/}")
    private String gameNFTBaseUri;

    @Value("${app.blockchain.contract-info.agent-nft.name:Agent NFT}")
    private String agentNFTName;

    @Value("${app.blockchain.contract-info.agent-nft.symbol:AGENT}")
    private String agentNFTSymbol;

    @Value("${app.blockchain.contract-info.agent-nft.type:ERC721}")
    private String agentNFTType;

    @Value("${app.blockchain.contract-info.agent-nft.base-uri:https://api.gaming-platform.com/metadata/agent/}")
    private String agentNFTBaseUri;

    @Value("${app.blockchain.contract-info.marketplace.name:Marketplace}")
    private String marketplaceName;

    @Value("${app.blockchain.contract-info.marketplace.type:Marketplace}")
    private String marketplaceType;

    @Value("${app.blockchain.contract-info.marketplace.fee-rate:250}")
    private String marketplaceFeeRate;

    @Value("${app.blockchain.contract-info.marketplace.fee-recipient:0x0000000000000000000000000000000000000000}")
    private String marketplaceFeeRecipient;

    @Value("${app.blockchain.contract-info.rewards.name:Rewards}")
    private String rewardsName;

    @Value("${app.blockchain.contract-info.rewards.type:Rewards}")
    private String rewardsType;

    @Override
    public Map<String, Object> getAllContractsInfo() {
        Map<String, Object> contractsInfo = new HashMap<>();
        
        // 平台代币合约信息
        Map<String, Object> platformTokenInfo = new HashMap<>();
        platformTokenInfo.put("address", platformTokenAddress);
        platformTokenInfo.put("name", platformTokenName);
        platformTokenInfo.put("symbol", platformTokenSymbol);
        platformTokenInfo.put("decimals", platformTokenDecimals);
        platformTokenInfo.put("type", platformTokenType);
        platformTokenInfo.put("deployed", !platformTokenAddress.isEmpty());
        contractsInfo.put("platformToken", platformTokenInfo);

        // 游戏NFT合约信息
        Map<String, Object> gameNFTInfo = new HashMap<>();
        gameNFTInfo.put("address", gameNFTAddress);
        gameNFTInfo.put("name", gameNFTName);
        gameNFTInfo.put("symbol", gameNFTSymbol);
        gameNFTInfo.put("type", gameNFTType);
        gameNFTInfo.put("deployed", !gameNFTAddress.isEmpty());
        contractsInfo.put("gameNFT", gameNFTInfo);

        // 智能体NFT合约信息
        Map<String, Object> agentNFTInfo = new HashMap<>();
        agentNFTInfo.put("address", agentNFTAddress);
        agentNFTInfo.put("name", agentNFTName);
        agentNFTInfo.put("symbol", agentNFTSymbol);
        agentNFTInfo.put("type", agentNFTType);
        agentNFTInfo.put("deployed", !agentNFTAddress.isEmpty());
        contractsInfo.put("agentNFT", agentNFTInfo);

        // 市场合约信息
        Map<String, Object> marketplaceInfo = new HashMap<>();
        marketplaceInfo.put("address", marketplaceAddress);
        marketplaceInfo.put("name", marketplaceName);
        marketplaceInfo.put("type", marketplaceType);
        marketplaceInfo.put("deployed", !marketplaceAddress.isEmpty());
        contractsInfo.put("marketplace", marketplaceInfo);

        // 奖励合约信息
        Map<String, Object> rewardsInfo = new HashMap<>();
        rewardsInfo.put("address", rewardsAddress);
        rewardsInfo.put("name", rewardsName);
        rewardsInfo.put("type", rewardsType);
        rewardsInfo.put("deployed", !rewardsAddress.isEmpty());
        contractsInfo.put("rewards", rewardsInfo);

        // 网络信息
        Map<String, Object> networkInfo = new HashMap<>();
        networkInfo.put("url", networkUrl);
        networkInfo.put("chainId", chainId);
        contractsInfo.put("network", networkInfo);

        return contractsInfo;
    }

    @Override
    public Map<String, Object> getPlatformTokenInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("address", platformTokenAddress);
        info.put("name", platformTokenName);
        info.put("symbol", platformTokenSymbol);
        info.put("decimals", platformTokenDecimals);
        info.put("type", platformTokenType);
        info.put("deployed", !platformTokenAddress.isEmpty());
        info.put("totalSupply", platformTokenTotalSupply);
        info.put("baseUri", platformTokenBaseUri);
        return info;
    }

    @Override
    public Map<String, Object> getGameNFTInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("address", gameNFTAddress);
        info.put("name", gameNFTName);
        info.put("symbol", gameNFTSymbol);
        info.put("type", gameNFTType);
        info.put("deployed", !gameNFTAddress.isEmpty());
        info.put("baseURI", gameNFTBaseUri);
        return info;
    }

    @Override
    public Map<String, Object> getAgentNFTInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("address", agentNFTAddress);
        info.put("name", agentNFTName);
        info.put("symbol", agentNFTSymbol);
        info.put("type", agentNFTType);
        info.put("deployed", !agentNFTAddress.isEmpty());
        info.put("baseURI", agentNFTBaseUri);
        return info;
    }

    @Override
    public Map<String, Object> getMarketplaceInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("address", marketplaceAddress);
        info.put("name", marketplaceName);
        info.put("type", marketplaceType);
        info.put("deployed", !marketplaceAddress.isEmpty());
        info.put("feeRate", marketplaceFeeRate);
        info.put("feeRecipient", marketplaceFeeRecipient);
        return info;
    }

    @Override
    public Map<String, Object> getRewardsInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("address", rewardsAddress);
        info.put("name", rewardsName);
        info.put("type", rewardsType);
        info.put("deployed", !rewardsAddress.isEmpty());
        info.put("tokenAddress", platformTokenAddress);
        return info;
    }

    @Override
    public String getContractAddress(String contractType) {
        switch (contractType.toLowerCase()) {
            case "platform-token":
            case "platformtoken":
                return platformTokenAddress;
            case "game-nft":
            case "gamenft":
                return gameNFTAddress;
            case "agent-nft":
            case "agentnft":
                return agentNFTAddress;
            case "marketplace":
                return marketplaceAddress;
            case "rewards":
                return rewardsAddress;
            default:
                return "";
        }
    }

    @Override
    public boolean isContractDeployed(String contractType) {
        String address = getContractAddress(contractType);
        return !address.isEmpty() && !address.equals("0x0000000000000000000000000000000000000000");
    }
}
