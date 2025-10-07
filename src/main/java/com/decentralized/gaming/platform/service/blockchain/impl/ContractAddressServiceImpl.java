package com.decentralized.gaming.platform.service.blockchain.impl;

import com.decentralized.gaming.platform.service.blockchain.ContractAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 合约地址管理服务实现类
 * 负责合约地址的动态配置和管理
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
public class ContractAddressServiceImpl implements ContractAddressService {

    // 内存中的合约地址缓存
    private final Map<String, ContractConfig> contractConfigs = new ConcurrentHashMap<>();

    // 从配置文件读取的默认合约地址
    @Value("${app.blockchain.contracts.platformToken:}")
    private String defaultPlatformTokenAddress;

    @Value("${app.blockchain.contracts.gameNft:}")
    private String defaultGameNFTAddress;

    @Value("${app.blockchain.contracts.agentNft:}")
    private String defaultAgentNFTAddress;

    @Value("${app.blockchain.contracts.marketplace:}")
    private String defaultMarketplaceAddress;

    @Value("${app.blockchain.contracts.rewards:}")
    private String defaultRewardsAddress;

    @Value("${app.blockchain.networkUrl:http://localhost:8545}")
    private String networkUrl;

    @Value("${app.blockchain.chainId:31337}")
    private String chainId;

    @Override
    public String getContractAddress(String contractType) {
        // 首先从内存缓存中获取
        ContractConfig config = contractConfigs.get(contractType);
        if (config != null && config.isActive()) {
            return config.getContractAddress();
        }

        // 如果内存中没有，从配置文件获取
        String address = getDefaultContractAddress(contractType);
        if (address != null && !address.isEmpty()) {
            // 将配置文件的地址加载到内存中
            ContractConfig defaultConfig = new ContractConfig(contractType, address);
            defaultConfig.setNetworkName(networkUrl);
            defaultConfig.setChainId(chainId);
            contractConfigs.put(contractType, defaultConfig);
            return address;
        }

        return null;
    }

    @Override
    public boolean setContractAddress(String contractType, String contractAddress) {
        try {
            // 验证合约地址格式
            if (!isValidContractAddress(contractAddress)) {
                log.error("无效的合约地址格式: {}", contractAddress);
                return false;
            }

            // 创建或更新合约配置
            ContractConfig config = contractConfigs.get(contractType);
            if (config == null) {
                config = new ContractConfig(contractType, contractAddress);
            } else {
                config.setContractAddress(contractAddress);
                config.setDeployedAt(System.currentTimeMillis());
            }

            config.setNetworkName(networkUrl);
            config.setChainId(chainId);
            config.setActive(true);

            contractConfigs.put(contractType, config);
            
            log.info("合约地址已更新: type={}, address={}", contractType, contractAddress);
            return true;

        } catch (Exception e) {
            log.error("设置合约地址失败: type={}, address={}", contractType, contractAddress, e);
            return false;
        }
    }

    @Override
    public Map<String, String> getAllContractAddresses() {
        Map<String, String> addresses = new HashMap<>();
        
        // 从内存缓存获取
        for (Map.Entry<String, ContractConfig> entry : contractConfigs.entrySet()) {
            if (entry.getValue().isActive()) {
                addresses.put(entry.getKey(), entry.getValue().getContractAddress());
            }
        }

        // 补充配置文件中的地址
        String[] contractTypes = {"platform-token", "game-nft", "agent-nft", "marketplace", "rewards"};
        for (String contractType : contractTypes) {
            if (!addresses.containsKey(contractType)) {
                String address = getDefaultContractAddress(contractType);
                if (address != null && !address.isEmpty()) {
                    addresses.put(contractType, address);
                }
            }
        }

        return addresses;
    }

    @Override
    public boolean isValidContractAddress(String contractAddress) {
        if (contractAddress == null || contractAddress.isEmpty()) {
            return false;
        }

        // 检查以太坊地址格式
        if (!contractAddress.startsWith("0x") || contractAddress.length() != 42) {
            return false;
        }

        // 检查是否为有效的十六进制字符串
        try {
            Long.parseLong(contractAddress.substring(2), 16);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean isContractDeployed(String contractType) {
        String address = getContractAddress(contractType);
        return address != null && !address.isEmpty() && isValidContractAddress(address);
    }

    @Override
    public boolean clearContractAddress(String contractType) {
        try {
            ContractConfig config = contractConfigs.get(contractType);
            if (config != null) {
                config.setActive(false);
                log.info("合约地址已清除: type={}", contractType);
            }
            return true;
        } catch (Exception e) {
            log.error("清除合约地址失败: type={}", contractType, e);
            return false;
        }
    }

    @Override
    public boolean setAllContractAddresses(Map<String, String> contractAddresses) {
        try {
            for (Map.Entry<String, String> entry : contractAddresses.entrySet()) {
                if (!setContractAddress(entry.getKey(), entry.getValue())) {
                    log.error("设置合约地址失败: type={}, address={}", entry.getKey(), entry.getValue());
                    return false;
                }
            }
            log.info("批量设置合约地址成功，共设置 {} 个合约", contractAddresses.size());
            return true;
        } catch (Exception e) {
            log.error("批量设置合约地址失败", e);
            return false;
        }
    }

    @Override
    public ContractConfig getContractConfig(String contractType) {
        ContractConfig config = contractConfigs.get(contractType);
        if (config != null) {
            return config;
        }

        // 如果内存中没有，从配置文件创建
        String address = getDefaultContractAddress(contractType);
        if (address != null && !address.isEmpty()) {
            config = new ContractConfig(contractType, address);
            config.setNetworkName(networkUrl);
            config.setChainId(chainId);
            contractConfigs.put(contractType, config);
            return config;
        }

        return null;
    }

    @Override
    public boolean updateContractConfig(String contractType, ContractConfig config) {
        try {
            if (config == null) {
                return false;
            }

            config.setContractType(contractType);
            config.setNetworkName(networkUrl);
            config.setChainId(chainId);
            
            contractConfigs.put(contractType, config);
            
            log.info("合约配置已更新: type={}", contractType);
            return true;

        } catch (Exception e) {
            log.error("更新合约配置失败: type={}", contractType, e);
            return false;
        }
    }

    /**
     * 获取默认合约地址
     */
    private String getDefaultContractAddress(String contractType) {
        switch (contractType.toLowerCase()) {
            case "platform-token":
            case "platformtoken":
                return defaultPlatformTokenAddress;
            case "game-nft":
            case "gamenft":
                return defaultGameNFTAddress;
            case "agent-nft":
            case "agentnft":
                return defaultAgentNFTAddress;
            case "marketplace":
                return defaultMarketplaceAddress;
            case "rewards":
                return defaultRewardsAddress;
            default:
                return null;
        }
    }

    /**
     * 初始化默认配置
     */
    public void initializeDefaultConfigs() {
        log.info("初始化默认合约配置");
        
        String[] contractTypes = {"platform-token", "game-nft", "agent-nft", "marketplace", "rewards"};
        for (String contractType : contractTypes) {
            String address = getDefaultContractAddress(contractType);
            if (address != null && !address.isEmpty()) {
                ContractConfig config = new ContractConfig(contractType, address);
                config.setNetworkName(networkUrl);
                config.setChainId(chainId);
                contractConfigs.put(contractType, config);
                log.info("加载默认合约配置: type={}, address={}", contractType, address);
            }
        }
    }

    /**
     * 获取合约配置统计信息
     */
    public Map<String, Object> getContractConfigStats() {
        Map<String, Object> stats = new HashMap<>();
        
        int totalContracts = contractConfigs.size();
        int activeContracts = 0;
        int deployedContracts = 0;
        
        for (ContractConfig config : contractConfigs.values()) {
            if (config.isActive()) {
                activeContracts++;
            }
            if (isValidContractAddress(config.getContractAddress())) {
                deployedContracts++;
            }
        }
        
        stats.put("totalContracts", totalContracts);
        stats.put("activeContracts", activeContracts);
        stats.put("deployedContracts", deployedContracts);
        stats.put("networkUrl", networkUrl);
        stats.put("chainId", chainId);
        stats.put("lastUpdateTime", System.currentTimeMillis());
        
        return stats;
    }
}
