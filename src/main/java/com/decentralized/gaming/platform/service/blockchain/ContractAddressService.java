package com.decentralized.gaming.platform.service.blockchain;

import java.util.Map;

/**
 * 合约地址管理服务接口
 * 负责合约地址的动态配置和管理
 *
 * @author DecentralizedGamingPlatform
 */
public interface ContractAddressService {

    /**
     * 获取合约地址
     *
     * @param contractType 合约类型
     * @return 合约地址
     */
    String getContractAddress(String contractType);

    /**
     * 设置合约地址
     *
     * @param contractType 合约类型
     * @param contractAddress 合约地址
     * @return 是否成功
     */
    boolean setContractAddress(String contractType, String contractAddress);

    /**
     * 获取所有合约地址
     *
     * @return 合约地址映射
     */
    Map<String, String> getAllContractAddresses();

    /**
     * 验证合约地址格式
     *
     * @param contractAddress 合约地址
     * @return 是否有效
     */
    boolean isValidContractAddress(String contractAddress);

    /**
     * 检查合约是否已部署
     *
     * @param contractType 合约类型
     * @return 是否已部署
     */
    boolean isContractDeployed(String contractType);

    /**
     * 清除合约地址
     *
     * @param contractType 合约类型
     * @return 是否成功
     */
    boolean clearContractAddress(String contractType);

    /**
     * 批量设置合约地址
     *
     * @param contractAddresses 合约地址映射
     * @return 是否成功
     */
    boolean setAllContractAddresses(Map<String, String> contractAddresses);

    /**
     * 获取合约配置信息
     *
     * @param contractType 合约类型
     * @return 合约配置信息
     */
    ContractConfig getContractConfig(String contractType);

    /**
     * 更新合约配置
     *
     * @param contractType 合约类型
     * @param config 合约配置
     * @return 是否成功
     */
    boolean updateContractConfig(String contractType, ContractConfig config);

    /**
     * 合约配置类
     */
    class ContractConfig {
        private String contractType;
        private String contractAddress;
        private String contractName;
        private String contractSymbol;
        private String contractVersion;
        private String networkName;
        private String chainId;
        private long deployedAt;
        private String deployerAddress;
        private String transactionHash;
        private long gasUsed;
        private boolean isActive;

        // 构造函数
        public ContractConfig() {}

        public ContractConfig(String contractType, String contractAddress) {
            this.contractType = contractType;
            this.contractAddress = contractAddress;
            this.isActive = true;
            this.deployedAt = System.currentTimeMillis();
        }

        // Getters and Setters
        public String getContractType() { return contractType; }
        public void setContractType(String contractType) { this.contractType = contractType; }
        
        public String getContractAddress() { return contractAddress; }
        public void setContractAddress(String contractAddress) { this.contractAddress = contractAddress; }
        
        public String getContractName() { return contractName; }
        public void setContractName(String contractName) { this.contractName = contractName; }
        
        public String getContractSymbol() { return contractSymbol; }
        public void setContractSymbol(String contractSymbol) { this.contractSymbol = contractSymbol; }
        
        public String getContractVersion() { return contractVersion; }
        public void setContractVersion(String contractVersion) { this.contractVersion = contractVersion; }
        
        public String getNetworkName() { return networkName; }
        public void setNetworkName(String networkName) { this.networkName = networkName; }
        
        public String getChainId() { return chainId; }
        public void setChainId(String chainId) { this.chainId = chainId; }
        
        public long getDeployedAt() { return deployedAt; }
        public void setDeployedAt(long deployedAt) { this.deployedAt = deployedAt; }
        
        public String getDeployerAddress() { return deployerAddress; }
        public void setDeployerAddress(String deployerAddress) { this.deployerAddress = deployerAddress; }
        
        public String getTransactionHash() { return transactionHash; }
        public void setTransactionHash(String transactionHash) { this.transactionHash = transactionHash; }
        
        public long getGasUsed() { return gasUsed; }
        public void setGasUsed(long gasUsed) { this.gasUsed = gasUsed; }
        
        public boolean isActive() { return isActive; }
        public void setActive(boolean active) { isActive = active; }
    }
}
