package com.decentralized.gaming.platform.service.blockchain;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.Map;

/**
 * 智能合约部署服务接口
 * 负责智能合约的部署、配置和管理
 *
 * @author DecentralizedGamingPlatform
 */
public interface ContractDeploymentService {

    /**
     * 部署平台代币合约
     *
     * @param deployerPrivateKey 部署者私钥
     * @param name 代币名称
     * @param symbol 代币符号
     * @return 部署结果
     */
    DeploymentResult deployPlatformToken(String deployerPrivateKey, String name, String symbol);

    /**
     * 部署游戏NFT合约
     *
     * @param deployerPrivateKey 部署者私钥
     * @param name NFT名称
     * @param symbol NFT符号
     * @param platformTokenAddress 平台代币地址
     * @param feeRecipient 费用接收者地址
     * @param creationFee 创建费用
     * @return 部署结果
     */
    DeploymentResult deployGameNFT(String deployerPrivateKey, String name, String symbol, 
                                   String platformTokenAddress, String feeRecipient, BigInteger creationFee);

    /**
     * 部署智能体NFT合约
     *
     * @param deployerPrivateKey 部署者私钥
     * @param name NFT名称
     * @param symbol NFT符号
     * @param platformTokenAddress 平台代币地址
     * @param feeRecipient 费用接收者地址
     * @param creationFee 创建费用
     * @return 部署结果
     */
    DeploymentResult deployAgentNFT(String deployerPrivateKey, String name, String symbol,
                                    String platformTokenAddress, String feeRecipient, BigInteger creationFee);

    /**
     * 部署交易市场合约
     *
     * @param deployerPrivateKey 部署者私钥
     * @param platformTokenAddress 平台代币地址
     * @param feeRecipient 费用接收者地址
     * @param feeBasisPoints 费用基点
     * @return 部署结果
     */
    DeploymentResult deployMarketplace(String deployerPrivateKey, String platformTokenAddress,
                                       String feeRecipient, BigInteger feeBasisPoints);

    /**
     * 部署奖励合约
     *
     * @param deployerPrivateKey 部署者私钥
     * @param platformTokenAddress 平台代币地址
     * @return 部署结果
     */
    DeploymentResult deployRewards(String deployerPrivateKey, String platformTokenAddress);

    /**
     * 批量部署所有合约
     *
     * @param deployerPrivateKey 部署者私钥
     * @param config 部署配置
     * @return 部署结果映射
     */
    Map<String, DeploymentResult> deployAllContracts(String deployerPrivateKey, DeploymentConfig config);

    /**
     * 验证合约部署
     *
     * @param contractAddress 合约地址
     * @param contractType 合约类型
     * @return 验证结果
     */
    boolean verifyContractDeployment(String contractAddress, String contractType);

    /**
     * 获取部署状态
     *
     * @return 部署状态信息
     */
    DeploymentStatus getDeploymentStatus();

    /**
     * 更新合约地址配置
     *
     * @param contractType 合约类型
     * @param contractAddress 合约地址
     * @return 更新结果
     */
    boolean updateContractAddress(String contractType, String contractAddress);

    /**
     * 部署结果类
     */
    class DeploymentResult {
        private boolean success;
        private String contractAddress;
        private String transactionHash;
        private String errorMessage;
        private TransactionReceipt receipt;
        private long gasUsed;
        private long deploymentTime;

        // 构造函数
        public DeploymentResult(boolean success, String contractAddress, String transactionHash) {
            this.success = success;
            this.contractAddress = contractAddress;
            this.transactionHash = transactionHash;
            this.deploymentTime = System.currentTimeMillis();
        }

        public DeploymentResult(boolean success, String errorMessage) {
            this.success = success;
            this.errorMessage = errorMessage;
            this.deploymentTime = System.currentTimeMillis();
        }

        // Getters and Setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public String getContractAddress() { return contractAddress; }
        public void setContractAddress(String contractAddress) { this.contractAddress = contractAddress; }
        
        public String getTransactionHash() { return transactionHash; }
        public void setTransactionHash(String transactionHash) { this.transactionHash = transactionHash; }
        
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        
        public TransactionReceipt getReceipt() { return receipt; }
        public void setReceipt(TransactionReceipt receipt) { this.receipt = receipt; }
        
        public long getGasUsed() { return gasUsed; }
        public void setGasUsed(long gasUsed) { this.gasUsed = gasUsed; }
        
        public long getDeploymentTime() { return deploymentTime; }
        public void setDeploymentTime(long deploymentTime) { this.deploymentTime = deploymentTime; }
    }

    /**
     * 部署配置类
     */
    class DeploymentConfig {
        private String platformTokenName = "Platform Token";
        private String platformTokenSymbol = "PLT";
        private String gameNFTName = "Game NFT";
        private String gameNFTSymbol = "GAME";
        private String agentNFTName = "Agent NFT";
        private String agentNFTSymbol = "AGENT";
        private String feeRecipient;
        private BigInteger gameCreationFee = BigInteger.valueOf(100).multiply(BigInteger.TEN.pow(18)); // 100 PLT
        private BigInteger agentCreationFee = BigInteger.valueOf(50).multiply(BigInteger.TEN.pow(18)); // 50 PLT
        private BigInteger marketplaceFeeBasisPoints = BigInteger.valueOf(250); // 2.5%

        // Getters and Setters
        public String getPlatformTokenName() { return platformTokenName; }
        public void setPlatformTokenName(String platformTokenName) { this.platformTokenName = platformTokenName; }
        
        public String getPlatformTokenSymbol() { return platformTokenSymbol; }
        public void setPlatformTokenSymbol(String platformTokenSymbol) { this.platformTokenSymbol = platformTokenSymbol; }
        
        public String getGameNFTName() { return gameNFTName; }
        public void setGameNFTName(String gameNFTName) { this.gameNFTName = gameNFTName; }
        
        public String getGameNFTSymbol() { return gameNFTSymbol; }
        public void setGameNFTSymbol(String gameNFTSymbol) { this.gameNFTSymbol = gameNFTSymbol; }
        
        public String getAgentNFTName() { return agentNFTName; }
        public void setAgentNFTName(String agentNFTName) { this.agentNFTName = agentNFTName; }
        
        public String getAgentNFTSymbol() { return agentNFTSymbol; }
        public void setAgentNFTSymbol(String agentNFTSymbol) { this.agentNFTSymbol = agentNFTSymbol; }
        
        public String getFeeRecipient() { return feeRecipient; }
        public void setFeeRecipient(String feeRecipient) { this.feeRecipient = feeRecipient; }
        
        public BigInteger getGameCreationFee() { return gameCreationFee; }
        public void setGameCreationFee(BigInteger gameCreationFee) { this.gameCreationFee = gameCreationFee; }
        
        public BigInteger getAgentCreationFee() { return agentCreationFee; }
        public void setAgentCreationFee(BigInteger agentCreationFee) { this.agentCreationFee = agentCreationFee; }
        
        public BigInteger getMarketplaceFeeBasisPoints() { return marketplaceFeeBasisPoints; }
        public void setMarketplaceFeeBasisPoints(BigInteger marketplaceFeeBasisPoints) { this.marketplaceFeeBasisPoints = marketplaceFeeBasisPoints; }
    }

    /**
     * 部署状态类
     */
    class DeploymentStatus {
        private boolean platformTokenDeployed;
        private boolean gameNFTDeployed;
        private boolean agentNFTDeployed;
        private boolean marketplaceDeployed;
        private boolean rewardsDeployed;
        private String platformTokenAddress;
        private String gameNFTAddress;
        private String agentNFTAddress;
        private String marketplaceAddress;
        private String rewardsAddress;
        private long lastUpdateTime;

        public DeploymentStatus() {
            this.lastUpdateTime = System.currentTimeMillis();
        }

        // Getters and Setters
        public boolean isPlatformTokenDeployed() { return platformTokenDeployed; }
        public void setPlatformTokenDeployed(boolean platformTokenDeployed) { this.platformTokenDeployed = platformTokenDeployed; }
        
        public boolean isGameNFTDeployed() { return gameNFTDeployed; }
        public void setGameNFTDeployed(boolean gameNFTDeployed) { this.gameNFTDeployed = gameNFTDeployed; }
        
        public boolean isAgentNFTDeployed() { return agentNFTDeployed; }
        public void setAgentNFTDeployed(boolean agentNFTDeployed) { this.agentNFTDeployed = agentNFTDeployed; }
        
        public boolean isMarketplaceDeployed() { return marketplaceDeployed; }
        public void setMarketplaceDeployed(boolean marketplaceDeployed) { this.marketplaceDeployed = marketplaceDeployed; }
        
        public boolean isRewardsDeployed() { return rewardsDeployed; }
        public void setRewardsDeployed(boolean rewardsDeployed) { this.rewardsDeployed = rewardsDeployed; }
        
        public String getPlatformTokenAddress() { return platformTokenAddress; }
        public void setPlatformTokenAddress(String platformTokenAddress) { this.platformTokenAddress = platformTokenAddress; }
        
        public String getGameNFTAddress() { return gameNFTAddress; }
        public void setGameNFTAddress(String gameNFTAddress) { this.gameNFTAddress = gameNFTAddress; }
        
        public String getAgentNFTAddress() { return agentNFTAddress; }
        public void setAgentNFTAddress(String agentNFTAddress) { this.agentNFTAddress = agentNFTAddress; }
        
        public String getMarketplaceAddress() { return marketplaceAddress; }
        public void setMarketplaceAddress(String marketplaceAddress) { this.marketplaceAddress = marketplaceAddress; }
        
        public String getRewardsAddress() { return rewardsAddress; }
        public void setRewardsAddress(String rewardsAddress) { this.rewardsAddress = rewardsAddress; }
        
        public long getLastUpdateTime() { return lastUpdateTime; }
        public void setLastUpdateTime(long lastUpdateTime) { this.lastUpdateTime = lastUpdateTime; }

        /**
         * 检查是否所有合约都已部署
         */
        public boolean isAllDeployed() {
            return platformTokenDeployed && gameNFTDeployed && agentNFTDeployed && 
                   marketplaceDeployed && rewardsDeployed;
        }

        /**
         * 获取部署进度百分比
         */
        public int getDeploymentProgress() {
            int deployed = 0;
            if (platformTokenDeployed) deployed++;
            if (gameNFTDeployed) deployed++;
            if (agentNFTDeployed) deployed++;
            if (marketplaceDeployed) deployed++;
            if (rewardsDeployed) deployed++;
            return (deployed * 100) / 5;
        }
    }
}
