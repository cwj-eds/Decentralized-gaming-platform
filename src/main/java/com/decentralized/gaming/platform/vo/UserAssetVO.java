package com.decentralized.gaming.platform.vo;

import com.decentralized.gaming.platform.entity.UserAsset;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户资产VO
 *
 * @author DecentralizedGamingPlatform
 */
@Data
public class UserAssetVO {
    
    /**
     * 资产ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 资产类型
     */
    private UserAsset.AssetType assetType;
    
    /**
     * 资产ID
     */
    private Long assetId;
    
    /**
     * 资产名称
     */
    private String assetName;
    
    /**
     * 资产描述
     */
    private String assetDescription;
    
    /**
     * NFT合约地址
     */
    private String contractAddress;
    
    /**
     * NFT Token ID
     */
    private String tokenId;
    
    /**
     * 获得时间
     */
    private LocalDateTime acquiredAt;
    
    /**
     * 获得方式
     */
    private UserAsset.AcquisitionType acquisitionType;
    
    /**
     * 是否可交易
     */
    private Boolean isTradeable;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}