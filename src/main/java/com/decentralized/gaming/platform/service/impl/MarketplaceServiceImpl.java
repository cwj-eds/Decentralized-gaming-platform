//package com.decentralized.gaming.platform.service.impl;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.decentralized.gaming.platform.entity.MarketplaceItem;
//import com.decentralized.gaming.platform.mapper.MarketplaceItemMapper;
//import com.decentralized.gaming.platform.service.MarketplaceService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
///**
// * 交易市场服务实现类
// *
// * @author DecentralizedGamingPlatform
// */
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class MarketplaceServiceImpl extends ServiceImpl<MarketplaceItemMapper, MarketplaceItem> implements MarketplaceService {
//
//    @Override
//    public MarketplaceItem listItem(Long sellerId, String itemType, Long itemId,
//                                  BigDecimal price, String currency) {
//        MarketplaceItem item = new MarketplaceItem();
//        item.setSellerId(sellerId);
//        item.setItemType(itemType);
//        item.setItemId(itemId);
//        item.setPrice(price);
//        item.setCurrency(currency);
//        item.setStatus(MarketplaceItem.ItemStatus.ACTIVE.getCode());
//        item.setCreatedAt(LocalDateTime.now());
//        item.setUpdatedAt(LocalDateTime.now());
//
//        baseMapper.insert(item);
//        log.info("商品上架成功: 类型={}, 价格={}", itemType, price);
//
//        return item;
//    }
//
//    @Override
//    public Boolean purchaseItem(Long itemId, Long buyerId) {
//        MarketplaceItem item = baseMapper.selectById(itemId);
//        if (item == null || !MarketplaceItem.ItemStatus.ACTIVE.getCode().equals(item.getStatus())) {
//            throw new RuntimeException("商品不存在或已下架");
//        }
//
//        if (item.getSellerId().equals(buyerId)) {
//            throw new RuntimeException("不能购买自己的商品");
//        }
//
//        // TODO: 检查买家余额
//        // TODO: 执行转账交易
//        // TODO: 转移资产所有权
//
//        // 更新商品状态
//        item.setStatus(MarketplaceItem.ItemStatus.SOLD.getCode());
//        item.setUpdatedAt(LocalDateTime.now());
//        baseMapper.updateById(item);
//
//        log.info("商品购买成功: 买家={}, 商品={}", buyerId, itemId);
//        return true;
//    }
//
//    @Override
//    public Page<MarketplaceItem> getMarketplaceItems(String itemType, int page, int size) {
//        Page<MarketplaceItem> pageParam = new Page<>(page, size);
//        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<MarketplaceItem> wrapper =
//            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<MarketplaceItem>()
//                .eq("status", MarketplaceItem.ItemStatus.ACTIVE.getCode());
//
//        if (itemType != null && !itemType.isEmpty()) {
//            wrapper.eq("item_type", itemType);
//        }
//
//        return baseMapper.selectPage(pageParam, wrapper);
//    }
//
//    @Override
//    public List<MarketplaceItem> getUserSellingItems(Long userId) {
//        return baseMapper.selectList(
//            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<MarketplaceItem>()
//                .eq("seller_id", userId)
//        );
//    }
//
//    @Override
//    public Boolean cancelItem(Long itemId, Long userId) {
//        MarketplaceItem item = baseMapper.selectById(itemId);
//        if (item == null || !item.getSellerId().equals(userId)) {
//            throw new RuntimeException("商品不存在或无权限");
//        }
//
//        item.setStatus(MarketplaceItem.ItemStatus.CANCELLED.getCode());
//        item.setUpdatedAt(LocalDateTime.now());
//        baseMapper.updateById(item);
//
//        log.info("商品下架成功: {}", itemId);
//        return true;
//    }
//
//    @Override
//    public MarketplaceItem getItemDetail(Long itemId) {
//        return baseMapper.selectById(itemId);
//    }
//}
