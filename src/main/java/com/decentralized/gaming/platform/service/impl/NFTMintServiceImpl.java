package com.decentralized.gaming.platform.service.impl;

import com.decentralized.gaming.platform.dto.AgentMintRequest;
import com.decentralized.gaming.platform.dto.GameMintRequest;
import com.decentralized.gaming.platform.dto.NFTMintResponse;
import com.decentralized.gaming.platform.service.NFTMetadataService;
import com.decentralized.gaming.platform.service.NFTMintService;
import com.decentralized.gaming.platform.service.blockchain.AgentNFTService;
import com.decentralized.gaming.platform.service.blockchain.GameNFTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;

/**
 * NFT铸造服务实现
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NFTMintServiceImpl implements NFTMintService {

    @Autowired
    private AgentNFTService agentNFTService;

    @Autowired
    private GameNFTService gameNFTService;

    @Autowired
    private NFTMetadataService metadataService;

    @Override
    public NFTMintResponse mintAgentNFT(AgentMintRequest request) {
        try {
            log.info("开始铸造智能体NFT: {}", request.getAgentName());
            
            // 1. 生成元数据
            String metadata = metadataService.generateAgentMetadata(request);
            String ipfsHash = metadataService.uploadMetadataToIPFS(metadata);
            String metadataUri = metadataService.buildMetadataUri(ipfsHash);
            
            // 2. 创建凭据
            Credentials credentials = Credentials.create(request.getCreatorPrivateKey());
            
            // 3. 铸造NFT
            BigInteger price = request.getPrice() != null ? new BigInteger(request.getPrice()) : BigInteger.ZERO;
            TransactionReceipt receipt = agentNFTService.createAgent(
                credentials,
                request.getAgentName(),
                request.getAgentDescription(),
                request.getAgentAvatar() != null ? request.getAgentAvatar() : "",
                request.getAgentUrl() != null ? request.getAgentUrl() : "",
                price
            );
            
            // 4. 提取Token ID
            BigInteger tokenId = extractTokenIdFromReceipt(receipt);
            
            log.info("智能体NFT铸造成功，Token ID: {}, 交易哈希: {}", tokenId, receipt.getTransactionHash());
            
            return NFTMintResponse.success(
                tokenId,
                receipt.getTransactionHash(),
                "0xe7f1725E7734CE288F8367e1Bb143E90bb3F0512", // AgentNFT合约地址
                metadataUri,
                receipt.getGasUsed()
            );
            
        } catch (Exception e) {
            log.error("铸造智能体NFT失败", e);
            return NFTMintResponse.failure("铸造智能体NFT失败: " + e.getMessage());
        }
    }

    @Override
    public NFTMintResponse mintGameNFT(GameMintRequest request) {
        try {
            log.info("开始铸造游戏NFT: {}", request.getGameName());
            
            // 1. 生成元数据
            String metadata = metadataService.generateGameMetadata(request);
            String ipfsHash = metadataService.uploadMetadataToIPFS(metadata);
            String metadataUri = metadataService.buildMetadataUri(ipfsHash);
            
            // 2. 创建凭据
            Credentials credentials = Credentials.create(request.getCreatorPrivateKey());
            
            // 3. 铸造NFT
            BigInteger price = request.getPrice() != null ? new BigInteger(request.getPrice()) : BigInteger.ZERO;
            TransactionReceipt receipt = gameNFTService.createGame(
                credentials,
                request.getGameName(),
                request.getGameDescription(),
                request.getGameImageUrl() != null ? request.getGameImageUrl() : "",
                request.getGameUrl() != null ? request.getGameUrl() : "",
                price
            );
            
            // 4. 提取Token ID
            BigInteger tokenId = extractTokenIdFromReceipt(receipt);
            
            log.info("游戏NFT铸造成功，Token ID: {}, 交易哈希: {}", tokenId, receipt.getTransactionHash());
            
            return NFTMintResponse.success(
                tokenId,
                receipt.getTransactionHash(),
                "0x9fE46736679d2D9a65F0992F2272dE9f3c7fa6e0", // GameNFT合约地址
                metadataUri,
                receipt.getGasUsed()
            );
            
        } catch (Exception e) {
            log.error("铸造游戏NFT失败", e);
            return NFTMintResponse.failure("铸造游戏NFT失败: " + e.getMessage());
        }
    }

    @Override
    public NFTMintResponse generateAndMintGameNFT(GameMintRequest request) {
        try {
            log.info("使用智能体生成并铸造游戏NFT: {}", request.getGameName());
            
            // 这里可以集成AI服务来生成游戏内容
            // 目前直接使用提供的参数进行铸造
            
            return mintGameNFT(request);
            
        } catch (Exception e) {
            log.error("生成并铸造游戏NFT失败", e);
            return NFTMintResponse.failure("生成并铸造游戏NFT失败: " + e.getMessage());
        }
    }

    @Override
    public NFTMintResponse adminMintAgentNFT(String adminPrivateKey, String to, AgentMintRequest request) {
        try {
            log.info("管理员铸造智能体NFT给地址: {}", to);
            
            // 1. 生成元数据
            String metadata = metadataService.generateAgentMetadata(request);
            String ipfsHash = metadataService.uploadMetadataToIPFS(metadata);
            String metadataUri = metadataService.buildMetadataUri(ipfsHash);
            
            // 2. 创建管理员凭据
            Credentials credentials = Credentials.create(adminPrivateKey);
            
            // 3. 管理员铸造
            TransactionReceipt receipt = agentNFTService.adminMintAgent(
                credentials,
                to,
                request.getAgentName(),
                request.getAgentDescription(),
                request.getAgentAvatar() != null ? request.getAgentAvatar() : "",
                request.getAgentType() != null ? request.getAgentType() : ""
            );
            
            // 4. 提取Token ID
            BigInteger tokenId = extractTokenIdFromReceipt(receipt);
            
            log.info("管理员铸造智能体NFT成功，Token ID: {}, 接收者: {}", tokenId, to);
            
            return NFTMintResponse.success(
                tokenId,
                receipt.getTransactionHash(),
                "0xe7f1725E7734CE288F8367e1Bb143E90bb3F0512", // AgentNFT合约地址
                metadataUri,
                receipt.getGasUsed()
            );
            
        } catch (Exception e) {
            log.error("管理员铸造智能体NFT失败", e);
            return NFTMintResponse.failure("管理员铸造智能体NFT失败: " + e.getMessage());
        }
    }

    @Override
    public NFTMintResponse adminMintGameNFT(String adminPrivateKey, String to, GameMintRequest request) {
        try {
            log.info("管理员铸造游戏NFT给地址: {}", to);
            
            // 1. 生成元数据
            String metadata = metadataService.generateGameMetadata(request);
            String ipfsHash = metadataService.uploadMetadataToIPFS(metadata);
            String metadataUri = metadataService.buildMetadataUri(ipfsHash);
            
            // 2. 创建管理员凭据
            Credentials credentials = Credentials.create(adminPrivateKey);
            
            // 3. 管理员铸造
            TransactionReceipt receipt = gameNFTService.adminMint(
                credentials,
                to,
                request.getGameName(),
                request.getGameDescription(),
                request.getGameImageUrl() != null ? request.getGameImageUrl() : "",
                request.getGameUrl() != null ? request.getGameUrl() : ""
            );
            
            // 4. 提取Token ID
            BigInteger tokenId = extractTokenIdFromReceipt(receipt);
            
            log.info("管理员铸造游戏NFT成功，Token ID: {}, 接收者: {}", tokenId, to);
            
            return NFTMintResponse.success(
                tokenId,
                receipt.getTransactionHash(),
                "0x9fE46736679d2D9a65F0992F2272dE9f3c7fa6e0", // GameNFT合约地址
                metadataUri,
                receipt.getGasUsed()
            );
            
        } catch (Exception e) {
            log.error("管理员铸造游戏NFT失败", e);
            return NFTMintResponse.failure("管理员铸造游戏NFT失败: " + e.getMessage());
        }
    }

    @Override
    public BigInteger getAgentMintFee() {
        try {
            return BigInteger.valueOf(1000000000000000000L); // 1 ETH
        } catch (Exception e) {
            log.error("获取智能体铸造费用失败", e);
            return BigInteger.ZERO;
        }
    }

    @Override
    public BigInteger getGameMintFee() {
        try {
            return BigInteger.valueOf(2000000000000000000L); // 2 ETH
        } catch (Exception e) {
            log.error("获取游戏铸造费用失败", e);
            return BigInteger.ZERO;
        }
    }

    @Override
    public boolean verifyAgentNFTOwnership(BigInteger tokenId, String ownerAddress) {
        try {
            // 这里应该调用实际的合约方法，目前返回模拟结果
            log.info("验证智能体NFT所有权: tokenId={}, ownerAddress={}", tokenId, ownerAddress);
            return true; // 模拟验证成功
        } catch (Exception e) {
            log.error("验证智能体NFT所有权失败", e);
            return false;
        }
    }

    @Override
    public boolean verifyGameNFTOwnership(BigInteger tokenId, String ownerAddress) {
        try {
            // 这里应该调用实际的合约方法，目前返回模拟结果
            log.info("验证游戏NFT所有权: tokenId={}, ownerAddress={}", tokenId, ownerAddress);
            return true; // 模拟验证成功
        } catch (Exception e) {
            log.error("验证游戏NFT所有权失败", e);
            return false;
        }
    }

    @Override
    public Object getAgentNFTInfo(BigInteger tokenId) {
        try {
            // 这里应该调用实际的合约方法，目前返回模拟结果
            return new NFTInfo(
                "0x742d35Cc6634C0532925a3b8D4C9db96C4b4d8b6", // 模拟拥有者地址
                "https://ipfs.io/ipfs/QmXXX...", // 模拟元数据URI
                tokenId
            );
        } catch (Exception e) {
            log.error("获取智能体NFT信息失败", e);
            return null;
        }
    }

    @Override
    public Object getGameNFTInfo(BigInteger tokenId) {
        try {
            // 这里应该调用实际的合约方法，目前返回模拟结果
            return new NFTInfo(
                "0x742d35Cc6634C0532925a3b8D4C9db96C4b4d8b6", // 模拟拥有者地址
                "https://ipfs.io/ipfs/QmYYY...", // 模拟元数据URI
                tokenId
            );
        } catch (Exception e) {
            log.error("获取游戏NFT信息失败", e);
            return null;
        }
    }

    /**
     * 从交易收据中提取Token ID
     */
    private BigInteger extractTokenIdFromReceipt(TransactionReceipt receipt) {
        // 这里需要根据实际的事件日志来提取Token ID
        // 简化实现，返回一个模拟的Token ID
        return BigInteger.valueOf(System.currentTimeMillis() % 1000000);
    }

    /**
     * NFT信息内部类
     */
    private static class NFTInfo {
        private final String owner;
        private final String tokenURI;
        private final BigInteger tokenId;

        public NFTInfo(String owner, String tokenURI, BigInteger tokenId) {
            this.owner = owner;
            this.tokenURI = tokenURI;
            this.tokenId = tokenId;
        }

        public String getOwner() { return owner; }
        public String getTokenURI() { return tokenURI; }
        public BigInteger getTokenId() { return tokenId; }
    }
}
