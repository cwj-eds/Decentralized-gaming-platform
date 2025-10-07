package com.decentralized.gaming.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 游戏NFT铸造请求DTO
 *
 * @author DecentralizedGamingPlatform
 */
@Data
@Schema(description = "游戏NFT铸造请求")
public class GameMintRequest {

    @NotBlank(message = "创建者私钥不能为空")
    @Schema(description = "创建者私钥", required = true)
    private String creatorPrivateKey;

    @NotBlank(message = "游戏名称不能为空")
    @Schema(description = "游戏名称", required = true, example = "去中心化冒险游戏")
    private String gameName;

    @NotBlank(message = "游戏描述不能为空")
    @Schema(description = "游戏描述", required = true, example = "一个基于区块链的冒险游戏")
    private String gameDescription;

    @Schema(description = "游戏封面图片URL", example = "https://example.com/game-cover.png")
    private String gameImageUrl;

    @Schema(description = "游戏类型", example = "RPG")
    private String gameType;

    @Schema(description = "游戏难度", example = "MEDIUM")
    private String difficulty;

    @Schema(description = "游戏标签", example = "冒险,角色扮演,区块链")
    private String tags;

    @Schema(description = "游戏代码哈希", example = "0x1234567890abcdef")
    private String codeHash;

    @Schema(description = "游戏价格(wei)", example = "5000000000000000000")
    private String price;

    @Schema(description = "游戏URL", example = "https://example.com/game")
    private String gameUrl;

    @Schema(description = "元数据URI", example = "https://ipfs.io/ipfs/QmXXX...")
    private String metadataUri;
}
