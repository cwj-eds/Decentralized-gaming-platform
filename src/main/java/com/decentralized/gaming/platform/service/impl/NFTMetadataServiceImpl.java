package com.decentralized.gaming.platform.service.impl;

import com.decentralized.gaming.platform.dto.AgentMintRequest;
import com.decentralized.gaming.platform.dto.GameMintRequest;
import com.decentralized.gaming.platform.service.NFTMetadataService;
import com.decentralized.gaming.platform.service.IpfsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * NFT元数据管理服务实现
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NFTMetadataServiceImpl implements NFTMetadataService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    // 如需直连 HTTP 读取可启用；当前未使用
    // private final RestTemplate restTemplate = new RestTemplate();
    private final IpfsService ipfsService;

    @Value("${app.ipfs.gateway:https://ipfs.io/ipfs/}")
    private String ipfsGateway;

    // 已由 IpfsService 统一处理上传

    @Override
    public String generateAgentMetadata(AgentMintRequest request) {
        try {
            ObjectNode metadata = objectMapper.createObjectNode();
            
            // 基础元数据
            metadata.put("name", request.getAgentName());
            metadata.put("description", request.getAgentDescription());
            metadata.put("image", request.getAgentAvatar() != null ? request.getAgentAvatar() : "");
            
            // 智能体特定属性
            ObjectNode attributes = objectMapper.createObjectNode();
            if (request.getAgentType() != null) {
                attributes.put("Agent Type", request.getAgentType());
            }
            if (request.getCapabilities() != null) {
                attributes.put("Capabilities", request.getCapabilities());
            }
            if (request.getPersonality() != null) {
                attributes.put("Personality", request.getPersonality());
            }
            if (request.getCodeHash() != null) {
                attributes.put("Code Hash", request.getCodeHash());
            }
            if (request.getModelHash() != null) {
                attributes.put("Model Hash", request.getModelHash());
            }
            if (request.getPrice() != null) {
                attributes.put("Price (wei)", request.getPrice());
            }
            
            metadata.set("attributes", attributes);
            
            // 元数据标准
            metadata.put("standard", "ERC-721");
            metadata.put("type", "Agent NFT");
            metadata.put("version", "1.0");
            
            return objectMapper.writeValueAsString(metadata);
        } catch (Exception e) {
            log.error("生成智能体元数据失败", e);
            throw new RuntimeException("生成智能体元数据失败: " + e.getMessage());
        }
    }

    @Override
    public String generateGameMetadata(GameMintRequest request) {
        try {
            ObjectNode metadata = objectMapper.createObjectNode();
            
            // 基础元数据
            metadata.put("name", request.getGameName());
            metadata.put("description", request.getGameDescription());
            metadata.put("image", request.getGameImageUrl() != null ? request.getGameImageUrl() : "");
            
            // 游戏特定属性
            ObjectNode attributes = objectMapper.createObjectNode();
            if (request.getGameType() != null) {
                attributes.put("Game Type", request.getGameType());
            }
            if (request.getDifficulty() != null) {
                attributes.put("Difficulty", request.getDifficulty());
            }
            if (request.getTags() != null) {
                attributes.put("Tags", request.getTags());
            }
            if (request.getCodeHash() != null) {
                attributes.put("Code Hash", request.getCodeHash());
            }
            if (request.getPrice() != null) {
                attributes.put("Price (wei)", request.getPrice());
            }
            
            metadata.set("attributes", attributes);
            
            // 元数据标准
            metadata.put("standard", "ERC-721");
            metadata.put("type", "Game NFT");
            metadata.put("version", "1.0");
            
            return objectMapper.writeValueAsString(metadata);
        } catch (Exception e) {
            log.error("生成游戏元数据失败", e);
            throw new RuntimeException("生成游戏元数据失败: " + e.getMessage());
        }
    }

    @Override
    public String uploadMetadataToIPFS(String metadata) {
        try {
            byte[] bytes = metadata.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            String cid = ipfsService.uploadBytes("metadata.json", bytes);
            log.info("IPFS 上传元数据成功: {}", cid);
            return cid;
        } catch (Exception e) {
            log.error("上传元数据到IPFS失败", e);
            throw new RuntimeException("上传元数据到IPFS失败: " + e.getMessage());
        }
    }

    @Override
    public String buildMetadataUri(String ipfsHash) {
        return ipfsGateway + ipfsHash;
    }

    @Override
    public String getMetadataFromIPFS(String ipfsHash) {
        try {
            byte[] data = ipfsService.download(ipfsHash);
            return new String(data, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("从IPFS获取元数据失败: {}", ipfsHash, e);
            throw new RuntimeException("从IPFS获取元数据失败: " + e.getMessage());
        }
    }

    @Override
    public boolean validateMetadata(String metadata) {
        try {
            ObjectNode jsonNode = objectMapper.readValue(metadata, ObjectNode.class);
            
            // 验证必需字段
            return jsonNode.has("name") && 
                   jsonNode.has("description") && 
                   jsonNode.has("attributes");
        } catch (Exception e) {
            log.error("元数据格式验证失败", e);
            return false;
        }
    }
}
