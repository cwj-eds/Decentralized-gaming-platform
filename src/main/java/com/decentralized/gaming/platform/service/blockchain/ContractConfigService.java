package com.decentralized.gaming.platform.service.blockchain;

import java.util.Map;

/**
 * 合约配置服务接口
 * 管理所有智能合约的配置信息
 *
 * @author DecentralizedGamingPlatform
 */
public interface ContractConfigService {

    /**
     * 获取所有合约信息
     *
     * @return 合约信息映射
     */
    Map<String, Object> getAllContractsInfo();

    /**
     * 获取平台代币合约信息
     *
     * @return 平台代币合约信息
     */
    Map<String, Object> getPlatformTokenInfo();

    /**
     * 获取游戏NFT合约信息
     *
     * @return 游戏NFT合约信息
     */
    Map<String, Object> getGameNFTInfo();

    /**
     * 获取代理NFT合约信息
     *
     * @return 代理NFT合约信息
     */
    Map<String, Object> getAgentNFTInfo();

    /**
     * 获取市场合约信息
     *
     * @return 市场合约信息
     */
    Map<String, Object> getMarketplaceInfo();

    /**
     * 获取奖励合约信息
     *
     * @return 奖励合约信息
     */
    Map<String, Object> getRewardsInfo();

    /**
     * 获取合约地址
     *
     * @param contractName 合约名称
     * @return 合约地址
     */
    String getContractAddress(String contractName);

    /**
     * 检查合约是否已部署
     *
     * @param contractName 合约名称
     * @return 是否已部署
     */
    boolean isContractDeployed(String contractName);
}
