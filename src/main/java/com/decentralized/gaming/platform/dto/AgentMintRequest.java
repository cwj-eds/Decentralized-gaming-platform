package com.decentralized.gaming.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 智能体NFT铸造请求DTO
 *
 * @author DecentralizedGamingPlatform
 */
@Data
@Schema(description = "智能体NFT铸造请求")
public class AgentMintRequest {

    @NotBlank(message = "创建者私钥不能为空")
    @Schema(description = "创建者私钥", required = true)
    private String creatorPrivateKey;

    @NotBlank(message = "智能体名称不能为空")
    @Schema(description = "智能体名称", required = true, example = "GPT-4游戏助手")
    private String agentName;

    @NotBlank(message = "智能体描述不能为空")
    @Schema(description = "智能体描述", required = true, example = "一个专门用于游戏开发的AI助手")
    private String agentDescription;

    @Schema(description = "智能体头像URL", example = "https://example.com/agent-avatar.png")
    private String agentAvatar;

    @Schema(description = "智能体类型", example = "GPT-4")
    private String agentType;

    @Schema(description = "智能体能力描述", example = "游戏开发、关卡设计、角色创建")
    private String capabilities;

    @Schema(description = "智能体性格特征", example = "友好、专业、创新")
    private String personality;

    @Schema(description = "智能体代码哈希", example = "0x1234567890abcdef")
    private String codeHash;

    @Schema(description = "智能体模型哈希", example = "0xabcdef1234567890")
    private String modelHash;

    @Schema(description = "智能体价格(wei)", example = "1000000000000000000")
    private String price;

    @Schema(description = "智能体URL", example = "https://example.com/agent")
    private String agentUrl;

    @Schema(description = "元数据URI", example = "https://ipfs.io/ipfs/QmXXX...")
    private String metadataUri;
}
