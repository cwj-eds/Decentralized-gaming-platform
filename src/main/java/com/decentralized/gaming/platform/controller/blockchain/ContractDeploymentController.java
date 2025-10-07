package com.decentralized.gaming.platform.controller.blockchain;

import com.decentralized.gaming.platform.common.Result;
import com.decentralized.gaming.platform.service.blockchain.ContractDeploymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;

/**
 * 智能合约部署控制器
 * 提供合约部署相关的API接口
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@RestController
@RequestMapping("/api/blockchain/deployment")
public class ContractDeploymentController {

    @Autowired
    private ContractDeploymentService contractDeploymentService;

    /**
     * 获取部署状态
     */
    @GetMapping("/status")
    public Result<ContractDeploymentService.DeploymentStatus> getDeploymentStatus() {
        try {
            ContractDeploymentService.DeploymentStatus status = contractDeploymentService.getDeploymentStatus();
            return Result.success(status);
        } catch (Exception e) {
            log.error("获取部署状态失败", e);
            return Result.error("获取部署状态失败: " + e.getMessage());
        }
    }

    /**
     * 部署平台代币合约
     */
    @PostMapping("/platform-token")
    public Result<ContractDeploymentService.DeploymentResult> deployPlatformToken(
            @RequestBody DeployPlatformTokenRequest request) {
        try {
            log.info("收到部署平台代币合约请求: {}", request);
            
            // 验证请求参数
            if (request.getDeployerPrivateKey() == null || request.getDeployerPrivateKey().isEmpty()) {
                return Result.error("部署者私钥不能为空");
            }
            if (request.getName() == null || request.getName().isEmpty()) {
                return Result.error("代币名称不能为空");
            }
            if (request.getSymbol() == null || request.getSymbol().isEmpty()) {
                return Result.error("代币符号不能为空");
            }

            ContractDeploymentService.DeploymentResult result = contractDeploymentService.deployPlatformToken(
                    request.getDeployerPrivateKey(), request.getName(), request.getSymbol());

            if (result.isSuccess()) {
                return Result.success(result);
            } else {
                return Result.error(result.getErrorMessage());
            }

        } catch (Exception e) {
            log.error("部署平台代币合约失败", e);
            return Result.error("部署失败: " + e.getMessage());
        }
    }

    /**
     * 部署游戏NFT合约
     */
    @PostMapping("/game-nft")
    public Result<ContractDeploymentService.DeploymentResult> deployGameNFT(
            @RequestBody DeployGameNFTRequest request) {
        try {
            log.info("收到部署游戏NFT合约请求: {}", request);
            
            // 验证请求参数
            if (request.getDeployerPrivateKey() == null || request.getDeployerPrivateKey().isEmpty()) {
                return Result.error("部署者私钥不能为空");
            }
            if (request.getPlatformTokenAddress() == null || request.getPlatformTokenAddress().isEmpty()) {
                return Result.error("平台代币地址不能为空");
            }
            if (request.getFeeRecipient() == null || request.getFeeRecipient().isEmpty()) {
                return Result.error("费用接收者地址不能为空");
            }

            ContractDeploymentService.DeploymentResult result = contractDeploymentService.deployGameNFT(
                    request.getDeployerPrivateKey(), request.getName(), request.getSymbol(),
                    request.getPlatformTokenAddress(), request.getFeeRecipient(), request.getCreationFee());

            if (result.isSuccess()) {
                return Result.success(result);
            } else {
                return Result.error(result.getErrorMessage());
            }

        } catch (Exception e) {
            log.error("部署游戏NFT合约失败", e);
            return Result.error("部署失败: " + e.getMessage());
        }
    }

    /**
     * 部署智能体NFT合约
     */
    @PostMapping("/agent-nft")
    public Result<ContractDeploymentService.DeploymentResult> deployAgentNFT(
            @RequestBody DeployAgentNFTRequest request) {
        try {
            log.info("收到部署智能体NFT合约请求: {}", request);
            
            // 验证请求参数
            if (request.getDeployerPrivateKey() == null || request.getDeployerPrivateKey().isEmpty()) {
                return Result.error("部署者私钥不能为空");
            }
            if (request.getPlatformTokenAddress() == null || request.getPlatformTokenAddress().isEmpty()) {
                return Result.error("平台代币地址不能为空");
            }
            if (request.getFeeRecipient() == null || request.getFeeRecipient().isEmpty()) {
                return Result.error("费用接收者地址不能为空");
            }

            ContractDeploymentService.DeploymentResult result = contractDeploymentService.deployAgentNFT(
                    request.getDeployerPrivateKey(), request.getName(), request.getSymbol(),
                    request.getPlatformTokenAddress(), request.getFeeRecipient(), request.getCreationFee());

            if (result.isSuccess()) {
                return Result.success(result);
            } else {
                return Result.error(result.getErrorMessage());
            }

        } catch (Exception e) {
            log.error("部署智能体NFT合约失败", e);
            return Result.error("部署失败: " + e.getMessage());
        }
    }

    /**
     * 部署交易市场合约
     */
    @PostMapping("/marketplace")
    public Result<ContractDeploymentService.DeploymentResult> deployMarketplace(
            @RequestBody DeployMarketplaceRequest request) {
        try {
            log.info("收到部署交易市场合约请求: {}", request);
            
            // 验证请求参数
            if (request.getDeployerPrivateKey() == null || request.getDeployerPrivateKey().isEmpty()) {
                return Result.error("部署者私钥不能为空");
            }
            if (request.getPlatformTokenAddress() == null || request.getPlatformTokenAddress().isEmpty()) {
                return Result.error("平台代币地址不能为空");
            }
            if (request.getFeeRecipient() == null || request.getFeeRecipient().isEmpty()) {
                return Result.error("费用接收者地址不能为空");
            }

            ContractDeploymentService.DeploymentResult result = contractDeploymentService.deployMarketplace(
                    request.getDeployerPrivateKey(), request.getPlatformTokenAddress(),
                    request.getFeeRecipient(), request.getFeeBasisPoints());

            if (result.isSuccess()) {
                return Result.success(result);
            } else {
                return Result.error(result.getErrorMessage());
            }

        } catch (Exception e) {
            log.error("部署交易市场合约失败", e);
            return Result.error("部署失败: " + e.getMessage());
        }
    }

    /**
     * 部署奖励合约
     */
    @PostMapping("/rewards")
    public Result<ContractDeploymentService.DeploymentResult> deployRewards(
            @RequestBody DeployRewardsRequest request) {
        try {
            log.info("收到部署奖励合约请求: {}", request);
            
            // 验证请求参数
            if (request.getDeployerPrivateKey() == null || request.getDeployerPrivateKey().isEmpty()) {
                return Result.error("部署者私钥不能为空");
            }
            if (request.getPlatformTokenAddress() == null || request.getPlatformTokenAddress().isEmpty()) {
                return Result.error("平台代币地址不能为空");
            }

            ContractDeploymentService.DeploymentResult result = contractDeploymentService.deployRewards(
                    request.getDeployerPrivateKey(), request.getPlatformTokenAddress());

            if (result.isSuccess()) {
                return Result.success(result);
            } else {
                return Result.error(result.getErrorMessage());
            }

        } catch (Exception e) {
            log.error("部署奖励合约失败", e);
            return Result.error("部署失败: " + e.getMessage());
        }
    }

    /**
     * 批量部署所有合约
     */
    @PostMapping("/all")
    public Result<Map<String, ContractDeploymentService.DeploymentResult>> deployAllContracts(
            @RequestBody DeployAllContractsRequest request) {
        try {
            log.info("收到批量部署所有合约请求: {}", request);
            
            // 验证请求参数
            if (request.getDeployerPrivateKey() == null || request.getDeployerPrivateKey().isEmpty()) {
                return Result.error("部署者私钥不能为空");
            }
            if (request.getFeeRecipient() == null || request.getFeeRecipient().isEmpty()) {
                return Result.error("费用接收者地址不能为空");
            }

            // 构建部署配置
            ContractDeploymentService.DeploymentConfig config = new ContractDeploymentService.DeploymentConfig();
            config.setPlatformTokenName(request.getPlatformTokenName());
            config.setPlatformTokenSymbol(request.getPlatformTokenSymbol());
            config.setGameNFTName(request.getGameNFTName());
            config.setGameNFTSymbol(request.getGameNFTSymbol());
            config.setAgentNFTName(request.getAgentNFTName());
            config.setAgentNFTSymbol(request.getAgentNFTSymbol());
            config.setFeeRecipient(request.getFeeRecipient());
            config.setGameCreationFee(request.getGameCreationFee());
            config.setAgentCreationFee(request.getAgentCreationFee());
            config.setMarketplaceFeeBasisPoints(request.getMarketplaceFeeBasisPoints());

            Map<String, ContractDeploymentService.DeploymentResult> results = 
                    contractDeploymentService.deployAllContracts(request.getDeployerPrivateKey(), config);

            return Result.success(results);

        } catch (Exception e) {
            log.error("批量部署所有合约失败", e);
            return Result.error("批量部署失败: " + e.getMessage());
        }
    }

    /**
     * 验证合约部署
     */
    @PostMapping("/verify")
    public Result<Boolean> verifyContractDeployment(
            @RequestBody VerifyContractRequest request) {
        try {
            log.info("收到验证合约部署请求: {}", request);
            
            // 验证请求参数
            if (request.getContractAddress() == null || request.getContractAddress().isEmpty()) {
                return Result.error("合约地址不能为空");
            }
            if (request.getContractType() == null || request.getContractType().isEmpty()) {
                return Result.error("合约类型不能为空");
            }

            boolean isValid = contractDeploymentService.verifyContractDeployment(
                    request.getContractAddress(), request.getContractType());

            return Result.success(isValid);

        } catch (Exception e) {
            log.error("验证合约部署失败", e);
            return Result.error("验证失败: " + e.getMessage());
        }
    }

    /**
     * 更新合约地址
     */
    @PutMapping("/address")
    public Result<Boolean> updateContractAddress(
            @RequestBody UpdateContractAddressRequest request) {
        try {
            log.info("收到更新合约地址请求: {}", request);
            
            // 验证请求参数
            if (request.getContractType() == null || request.getContractType().isEmpty()) {
                return Result.error("合约类型不能为空");
            }
            if (request.getContractAddress() == null || request.getContractAddress().isEmpty()) {
                return Result.error("合约地址不能为空");
            }

            boolean success = contractDeploymentService.updateContractAddress(
                    request.getContractType(), request.getContractAddress());

            if (success) {
                return Result.success(true);
            } else {
                return Result.error("更新合约地址失败");
            }

        } catch (Exception e) {
            log.error("更新合约地址失败", e);
            return Result.error("更新失败: " + e.getMessage());
        }
    }

    // 请求DTO类

    /**
     * 部署平台代币合约请求
     */
    public static class DeployPlatformTokenRequest {
        private String deployerPrivateKey;
        private String name;
        private String symbol;

        // Getters and Setters
        public String getDeployerPrivateKey() { return deployerPrivateKey; }
        public void setDeployerPrivateKey(String deployerPrivateKey) { this.deployerPrivateKey = deployerPrivateKey; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getSymbol() { return symbol; }
        public void setSymbol(String symbol) { this.symbol = symbol; }
    }

    /**
     * 部署游戏NFT合约请求
     */
    public static class DeployGameNFTRequest {
        private String deployerPrivateKey;
        private String name;
        private String symbol;
        private String platformTokenAddress;
        private String feeRecipient;
        private BigInteger creationFee;

        // Getters and Setters
        public String getDeployerPrivateKey() { return deployerPrivateKey; }
        public void setDeployerPrivateKey(String deployerPrivateKey) { this.deployerPrivateKey = deployerPrivateKey; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getSymbol() { return symbol; }
        public void setSymbol(String symbol) { this.symbol = symbol; }
        
        public String getPlatformTokenAddress() { return platformTokenAddress; }
        public void setPlatformTokenAddress(String platformTokenAddress) { this.platformTokenAddress = platformTokenAddress; }
        
        public String getFeeRecipient() { return feeRecipient; }
        public void setFeeRecipient(String feeRecipient) { this.feeRecipient = feeRecipient; }
        
        public BigInteger getCreationFee() { return creationFee; }
        public void setCreationFee(BigInteger creationFee) { this.creationFee = creationFee; }
    }

    /**
     * 部署智能体NFT合约请求
     */
    public static class DeployAgentNFTRequest {
        private String deployerPrivateKey;
        private String name;
        private String symbol;
        private String platformTokenAddress;
        private String feeRecipient;
        private BigInteger creationFee;

        // Getters and Setters
        public String getDeployerPrivateKey() { return deployerPrivateKey; }
        public void setDeployerPrivateKey(String deployerPrivateKey) { this.deployerPrivateKey = deployerPrivateKey; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getSymbol() { return symbol; }
        public void setSymbol(String symbol) { this.symbol = symbol; }
        
        public String getPlatformTokenAddress() { return platformTokenAddress; }
        public void setPlatformTokenAddress(String platformTokenAddress) { this.platformTokenAddress = platformTokenAddress; }
        
        public String getFeeRecipient() { return feeRecipient; }
        public void setFeeRecipient(String feeRecipient) { this.feeRecipient = feeRecipient; }
        
        public BigInteger getCreationFee() { return creationFee; }
        public void setCreationFee(BigInteger creationFee) { this.creationFee = creationFee; }
    }

    /**
     * 部署交易市场合约请求
     */
    public static class DeployMarketplaceRequest {
        private String deployerPrivateKey;
        private String platformTokenAddress;
        private String feeRecipient;
        private BigInteger feeBasisPoints;

        // Getters and Setters
        public String getDeployerPrivateKey() { return deployerPrivateKey; }
        public void setDeployerPrivateKey(String deployerPrivateKey) { this.deployerPrivateKey = deployerPrivateKey; }
        
        public String getPlatformTokenAddress() { return platformTokenAddress; }
        public void setPlatformTokenAddress(String platformTokenAddress) { this.platformTokenAddress = platformTokenAddress; }
        
        public String getFeeRecipient() { return feeRecipient; }
        public void setFeeRecipient(String feeRecipient) { this.feeRecipient = feeRecipient; }
        
        public BigInteger getFeeBasisPoints() { return feeBasisPoints; }
        public void setFeeBasisPoints(BigInteger feeBasisPoints) { this.feeBasisPoints = feeBasisPoints; }
    }

    /**
     * 部署奖励合约请求
     */
    public static class DeployRewardsRequest {
        private String deployerPrivateKey;
        private String platformTokenAddress;

        // Getters and Setters
        public String getDeployerPrivateKey() { return deployerPrivateKey; }
        public void setDeployerPrivateKey(String deployerPrivateKey) { this.deployerPrivateKey = deployerPrivateKey; }
        
        public String getPlatformTokenAddress() { return platformTokenAddress; }
        public void setPlatformTokenAddress(String platformTokenAddress) { this.platformTokenAddress = platformTokenAddress; }
    }

    /**
     * 批量部署所有合约请求
     */
    public static class DeployAllContractsRequest {
        private String deployerPrivateKey;
        private String platformTokenName;
        private String platformTokenSymbol;
        private String gameNFTName;
        private String gameNFTSymbol;
        private String agentNFTName;
        private String agentNFTSymbol;
        private String feeRecipient;
        private BigInteger gameCreationFee;
        private BigInteger agentCreationFee;
        private BigInteger marketplaceFeeBasisPoints;

        // Getters and Setters
        public String getDeployerPrivateKey() { return deployerPrivateKey; }
        public void setDeployerPrivateKey(String deployerPrivateKey) { this.deployerPrivateKey = deployerPrivateKey; }
        
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
     * 验证合约部署请求
     */
    public static class VerifyContractRequest {
        private String contractAddress;
        private String contractType;

        // Getters and Setters
        public String getContractAddress() { return contractAddress; }
        public void setContractAddress(String contractAddress) { this.contractAddress = contractAddress; }
        
        public String getContractType() { return contractType; }
        public void setContractType(String contractType) { this.contractType = contractType; }
    }

    /**
     * 更新合约地址请求
     */
    public static class UpdateContractAddressRequest {
        private String contractType;
        private String contractAddress;

        // Getters and Setters
        public String getContractType() { return contractType; }
        public void setContractType(String contractType) { this.contractType = contractType; }
        
        public String getContractAddress() { return contractAddress; }
        public void setContractAddress(String contractAddress) { this.contractAddress = contractAddress; }
    }
}
