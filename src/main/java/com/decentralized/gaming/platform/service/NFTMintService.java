package com.decentralized.gaming.platform.service;

import com.decentralized.gaming.platform.dto.AgentMintRequest;
import com.decentralized.gaming.platform.dto.GameMintRequest;
import com.decentralized.gaming.platform.dto.NFTMintResponse;

import java.math.BigInteger;

/**
 * NFT铸造服务接口
 * 提供智能体和游戏NFT的铸造功能
 *
 * @author DecentralizedGamingPlatform
 */
public interface NFTMintService {

    /**
     * 铸造智能体NFT
     *
     * @param request 智能体铸造请求
     * @return 铸造结果
     */
    NFTMintResponse mintAgentNFT(AgentMintRequest request);

    /**
     * 铸造游戏NFT
     *
     * @param request 游戏铸造请求
     * @return 铸造结果
     */
    NFTMintResponse mintGameNFT(GameMintRequest request);

    /**
     * 使用智能体生成并铸造游戏NFT
     *
     * @param request 游戏铸造请求
     * @return 铸造结果
     */
    NFTMintResponse generateAndMintGameNFT(GameMintRequest request);

    /**
     * 管理员铸造智能体NFT
     *
     * @param adminPrivateKey 管理员私钥
     * @param to 接收者地址
     * @param request 智能体铸造请求
     * @return 铸造结果
     */
    NFTMintResponse adminMintAgentNFT(String adminPrivateKey, String to, AgentMintRequest request);

    /**
     * 管理员铸造游戏NFT
     *
     * @param adminPrivateKey 管理员私钥
     * @param to 接收者地址
     * @param request 游戏铸造请求
     * @return 铸造结果
     */
    NFTMintResponse adminMintGameNFT(String adminPrivateKey, String to, GameMintRequest request);

    /**
     * 获取智能体NFT铸造费用
     *
     * @return 铸造费用(wei)
     */
    BigInteger getAgentMintFee();

    /**
     * 获取游戏NFT铸造费用
     *
     * @return 铸造费用(wei)
     */
    BigInteger getGameMintFee();

    /**
     * 验证智能体NFT所有权
     *
     * @param tokenId NFT Token ID
     * @param ownerAddress 拥有者地址
     * @return 是否拥有
     */
    boolean verifyAgentNFTOwnership(BigInteger tokenId, String ownerAddress);

    /**
     * 验证游戏NFT所有权
     *
     * @param tokenId NFT Token ID
     * @param ownerAddress 拥有者地址
     * @return 是否拥有
     */
    boolean verifyGameNFTOwnership(BigInteger tokenId, String ownerAddress);

    /**
     * 获取智能体NFT信息
     *
     * @param tokenId NFT Token ID
     * @return NFT信息
     */
    Object getAgentNFTInfo(BigInteger tokenId);

    /**
     * 获取游戏NFT信息
     *
     * @param tokenId NFT Token ID
     * @return NFT信息
     */
    Object getGameNFTInfo(BigInteger tokenId);
}
