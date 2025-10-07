package com.decentralized.gaming.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * NFT铸造响应DTO
 *
 * @author DecentralizedGamingPlatform
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "NFT铸造响应")
public class NFTMintResponse {

    @Schema(description = "是否成功", example = "true")
    private boolean success;

    @Schema(description = "NFT Token ID", example = "1")
    private BigInteger tokenId;

    @Schema(description = "交易哈希", example = "0x1234567890abcdef...")
    private String transactionHash;

    @Schema(description = "合约地址", example = "0x9fE46736679d2D9a65F0992F2272dE9f3c7fa6e0")
    private String contractAddress;

    @Schema(description = "元数据URI", example = "https://ipfs.io/ipfs/QmXXX...")
    private String metadataUri;

    @Schema(description = "Gas使用量", example = "150000")
    private BigInteger gasUsed;

    @Schema(description = "错误信息", example = "铸造失败")
    private String errorMessage;

    @Schema(description = "响应时间戳", example = "1640995200000")
    private long timestamp;

    /**
     * 创建成功响应
     */
    public static NFTMintResponse success(BigInteger tokenId, String transactionHash, 
                                        String contractAddress, String metadataUri, BigInteger gasUsed) {
        return NFTMintResponse.builder()
                .success(true)
                .tokenId(tokenId)
                .transactionHash(transactionHash)
                .contractAddress(contractAddress)
                .metadataUri(metadataUri)
                .gasUsed(gasUsed)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * 创建失败响应
     */
    public static NFTMintResponse failure(String errorMessage) {
        return NFTMintResponse.builder()
                .success(false)
                .errorMessage(errorMessage)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
