package com.decentralized.gaming.platform.service;

import com.decentralized.gaming.platform.dto.AgentMintRequest;
import com.decentralized.gaming.platform.dto.GameMintRequest;

/**
 * NFT元数据管理服务接口
 * 负责生成和上传NFT元数据到IPFS
 *
 * @author DecentralizedGamingPlatform
 */
public interface NFTMetadataService {

    /**
     * 生成智能体NFT元数据
     *
     * @param request 智能体铸造请求
     * @return 元数据JSON字符串
     */
    String generateAgentMetadata(AgentMintRequest request);

    /**
     * 生成游戏NFT元数据
     *
     * @param request 游戏铸造请求
     * @return 元数据JSON字符串
     */
    String generateGameMetadata(GameMintRequest request);

    /**
     * 上传元数据到IPFS
     *
     * @param metadata 元数据JSON字符串
     * @return IPFS哈希
     */
    String uploadMetadataToIPFS(String metadata);

    /**
     * 构建元数据URI
     *
     * @param ipfsHash IPFS哈希
     * @return 完整的元数据URI
     */
    String buildMetadataUri(String ipfsHash);

    /**
     * 从IPFS获取元数据
     *
     * @param ipfsHash IPFS哈希
     * @return 元数据JSON字符串
     */
    String getMetadataFromIPFS(String ipfsHash);

    /**
     * 验证元数据格式
     *
     * @param metadata 元数据JSON字符串
     * @return 是否有效
     */
    boolean validateMetadata(String metadata);
}
