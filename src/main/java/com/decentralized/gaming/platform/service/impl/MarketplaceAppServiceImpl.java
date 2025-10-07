package com.decentralized.gaming.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.decentralized.gaming.platform.common.PageResult;
import com.decentralized.gaming.platform.dto.ListItemRequest;
import com.decentralized.gaming.platform.entity.MarketplaceItem;
import com.decentralized.gaming.platform.entity.Transaction;
import com.decentralized.gaming.platform.entity.UserAsset;
import com.decentralized.gaming.platform.mapper.MarketplaceItemMapper;
import com.decentralized.gaming.platform.mapper.TransactionMapper;
import com.decentralized.gaming.platform.mapper.UserAssetMapper;
import com.decentralized.gaming.platform.service.MarketplaceAppService;
import com.decentralized.gaming.platform.vo.MarketplaceItemVO;
import com.decentralized.gaming.platform.vo.TransactionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketplaceAppServiceImpl implements MarketplaceAppService {

    private final MarketplaceItemMapper marketplaceItemMapper;
    private final TransactionMapper transactionMapper;
    private final UserAssetMapper userAssetMapper;

    @Override
    public PageResult<MarketplaceItemVO> getMarketplaceItems(String itemType, int page, int size) {
        Page<MarketplaceItem> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<MarketplaceItem> wrapper = new LambdaQueryWrapper<MarketplaceItem>()
                .eq(MarketplaceItem::getStatus, MarketplaceItem.ItemStatus.ACTIVE)
                .orderByDesc(MarketplaceItem::getCreatedAt);
        if (itemType != null && !itemType.isEmpty()) {
            try {
                wrapper.eq(MarketplaceItem::getItemType, MarketplaceItem.ItemType.valueOf(itemType.toUpperCase()));
            } catch (IllegalArgumentException ignored) {
                // 无效类型则忽略筛选
            }
        }
        IPage<MarketplaceItem> itemPage = marketplaceItemMapper.selectPage(pageParam, wrapper);
        List<MarketplaceItemVO> vos = itemPage.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return PageResult.of(vos, itemPage.getTotal(), (long) page, (long) size);
    }

    @Override
    @Transactional
    public MarketplaceItemVO listItem(Long sellerId, ListItemRequest request) {
        // 校验用户是否拥有且可交易
        LambdaQueryWrapper<UserAsset> assetWrapper = new LambdaQueryWrapper<UserAsset>()
                .eq(UserAsset::getUserId, sellerId)
                .eq(UserAsset::getAssetId, request.getItemId())
                .eq(UserAsset::getIsTradeable, true);
        // 按资产类型匹配
        switch (request.getItemType()) {
            case GAME -> assetWrapper.eq(UserAsset::getAssetType, UserAsset.AssetType.GAME);
            case AGENT -> assetWrapper.eq(UserAsset::getAssetType, UserAsset.AssetType.AGENT);
            case GAME_ITEM -> assetWrapper.eq(UserAsset::getAssetType, UserAsset.AssetType.GAME_ITEM);
        }

        UserAsset owned = userAssetMapper.selectOne(assetWrapper);
        if (owned == null) {
            throw new IllegalStateException("用户不拥有该资产或资产不可交易");
        }

        MarketplaceItem item = new MarketplaceItem();
        item.setSellerId(sellerId);
        item.setItemType(request.getItemType());
        item.setItemId(request.getItemId());
        item.setPrice(request.getPrice());
        item.setCurrency(request.getCurrency());
        item.setStatus(MarketplaceItem.ItemStatus.ACTIVE);
        marketplaceItemMapper.insert(item);

        return toVO(item);
    }

    @Override
    @Transactional
    public TransactionVO purchaseItem(Long buyerId, Long itemId) {
        MarketplaceItem item = marketplaceItemMapper.selectById(itemId);
        if (item == null || item.getStatus() != MarketplaceItem.ItemStatus.ACTIVE) {
            throw new IllegalStateException("商品不存在或已下架");
        }

        // 写入交易记录（区块链调用集成点留给后续联调）
        Transaction tx = new Transaction();
        tx.setBuyerId(buyerId);
        tx.setSellerId(item.getSellerId());
        tx.setMarketplaceItemId(item.getId());
        tx.setAmount(item.getPrice());
        tx.setCurrency(item.getCurrency());
        tx.setStatus(Transaction.TransactionStatus.COMPLETED);
        transactionMapper.insert(tx);

        // 更新商品状态
        item.setStatus(MarketplaceItem.ItemStatus.SOLD);
        marketplaceItemMapper.updateById(item);

        // 资产所有权转移（简化：更新 user_assets 表）
        transferOwnership(buyerId, item);

        return toVO(tx);
    }

    @Override
    public List<TransactionVO> getUserTransactions(Long userId) {
        LambdaQueryWrapper<Transaction> wrapper = new LambdaQueryWrapper<Transaction>()
                .eq(Transaction::getBuyerId, userId)
                .or()
                .eq(Transaction::getSellerId, userId)
                .orderByDesc(Transaction::getCreatedAt);
        List<Transaction> list = transactionMapper.selectList(wrapper);
        return list.stream().map(this::toVO).collect(Collectors.toList());
    }

    private void transferOwnership(Long buyerId, MarketplaceItem item) {
        // 删除卖家持有
        LambdaQueryWrapper<UserAsset> del = new LambdaQueryWrapper<UserAsset>()
                .eq(UserAsset::getUserId, item.getSellerId())
                .eq(UserAsset::getAssetId, item.getItemId());
        switch (item.getItemType()) {
            case GAME -> del.eq(UserAsset::getAssetType, UserAsset.AssetType.GAME);
            case AGENT -> del.eq(UserAsset::getAssetType, UserAsset.AssetType.AGENT);
            case GAME_ITEM -> del.eq(UserAsset::getAssetType, UserAsset.AssetType.GAME_ITEM);
        }
        userAssetMapper.delete(del);

        // 新增买家持有
        UserAsset ua = new UserAsset();
        ua.setUserId(buyerId);
        switch (item.getItemType()) {
            case GAME -> ua.setAssetType(UserAsset.AssetType.GAME);
            case AGENT -> ua.setAssetType(UserAsset.AssetType.AGENT);
            case GAME_ITEM -> ua.setAssetType(UserAsset.AssetType.GAME_ITEM);
        }
        ua.setAssetId(item.getItemId());
        ua.setAcquisitionType(UserAsset.AcquisitionType.PURCHASED);
        ua.setIsTradeable(true);
        userAssetMapper.insert(ua);
    }

    private MarketplaceItemVO toVO(MarketplaceItem item) {
        MarketplaceItemVO vo = new MarketplaceItemVO();
        BeanUtils.copyProperties(item, vo);
        return vo;
    }

    private TransactionVO toVO(Transaction tx) {
        TransactionVO vo = new TransactionVO();
        BeanUtils.copyProperties(tx, vo);
        return vo;
    }
}


