package com.decentralized.gaming.platform.service;

import com.decentralized.gaming.platform.common.PageResult;
import com.decentralized.gaming.platform.dto.ListItemRequest;
import com.decentralized.gaming.platform.vo.MarketplaceItemVO;
import com.decentralized.gaming.platform.vo.TransactionVO;

import java.util.List;

/**
 * 交易平台应用服务（面向业务聚合），与区块链服务与资产服务交互
 */
public interface MarketplaceAppService {

    PageResult<MarketplaceItemVO> getMarketplaceItems(String itemType, int page, int size);

    MarketplaceItemVO listItem(Long sellerId, ListItemRequest request);

    TransactionVO purchaseItem(Long buyerId, Long itemId);

    List<TransactionVO> getUserTransactions(Long userId);

    /** 取消上架（仅卖家可操作） */
    boolean cancelListing(Long sellerId, Long listingId);

    /** 我的上架（仅ACTIVE） */
    PageResult<MarketplaceItemVO> getMyListings(Long userId, int page, int size);

    /** 我买到的交易 */
    List<TransactionVO> getMyPurchases(Long userId);

    /** 我卖出的交易 */
    List<TransactionVO> getMySales(Long userId);
}


