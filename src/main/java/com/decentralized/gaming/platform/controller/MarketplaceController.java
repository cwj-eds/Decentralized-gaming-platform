package com.decentralized.gaming.platform.controller;

import com.decentralized.gaming.platform.common.PageResult;
import com.decentralized.gaming.platform.common.Result;
import com.decentralized.gaming.platform.dto.ListItemRequest;
import com.decentralized.gaming.platform.service.MarketplaceAppService;
import com.decentralized.gaming.platform.vo.MarketplaceItemVO;
import com.decentralized.gaming.platform.vo.TransactionVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/marketplace")
@RequiredArgsConstructor
public class MarketplaceController {

    private final MarketplaceAppService marketplaceAppService;

    // 页面：市场首页
    @GetMapping
    public String index(@RequestParam(required = false) String itemType,
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "12") int size,
                        Model model) {
        PageResult<MarketplaceItemVO> items = marketplaceAppService.getMarketplaceItems(itemType, page, size);
        model.addAttribute("items", items);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("itemType", itemType);
        return "marketplace/index";
    }

    // 页面：上架表单
    @GetMapping("/sell")
    public String sellPage(){
        return "marketplace/sell";
    }

    // API：获取市场商品
    @GetMapping("/api/items")
    @ResponseBody
    public Result<PageResult<MarketplaceItemVO>> getItems(@RequestParam(required = false) String itemType,
                                                         @RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "12") int size) {
        return Result.success(marketplaceAppService.getMarketplaceItems(itemType, page, size));
    }

    // API：上架商品
    @PostMapping("/api/items")
    @ResponseBody
    public Result<MarketplaceItemVO> listItem(@Valid @RequestBody ListItemRequest request) {
        Long currentUserId = 1L; // TODO: 从会话获取
        return Result.success(marketplaceAppService.listItem(currentUserId, request));
    }

    // API：购买商品
    @PostMapping("/api/items/{itemId}/purchase")
    @ResponseBody
    public Result<TransactionVO> purchase(@PathVariable Long itemId) {
        Long currentUserId = 1L; // TODO: 从会话获取
        return Result.success(marketplaceAppService.purchaseItem(currentUserId, itemId));
    }

    // API：用户交易历史
    @GetMapping("/api/transactions/user/{userId}")
    @ResponseBody
    public Result<List<TransactionVO>> getUserTransactions(@PathVariable Long userId) {
        return Result.success(marketplaceAppService.getUserTransactions(userId));
    }

    // API：取消上架
    @PostMapping("/api/items/{listingId}/cancel")
    @ResponseBody
    public Result<Boolean> cancel(@PathVariable Long listingId) {
        Long currentUserId = 1L; // TODO: 从会话获取
        return Result.success(marketplaceAppService.cancelListing(currentUserId, listingId));
    }

    // API：我的上架（ACTIVE）
    @GetMapping("/api/my/listings")
    @ResponseBody
    public Result<PageResult<MarketplaceItemVO>> myListings(@RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "12") int size) {
        Long currentUserId = 1L; // TODO: 从会话获取
        return Result.success(marketplaceAppService.getMyListings(currentUserId, page, size));
    }

    // API：我买到/卖出的交易
    @GetMapping("/api/my/purchases")
    @ResponseBody
    public Result<List<TransactionVO>> myPurchases() {
        Long currentUserId = 1L; // TODO: 从会话获取
        return Result.success(marketplaceAppService.getMyPurchases(currentUserId));
    }

    @GetMapping("/api/my/sales")
    @ResponseBody
    public Result<List<TransactionVO>> mySales() {
        Long currentUserId = 1L; // TODO: 从会话获取
        return Result.success(marketplaceAppService.getMySales(currentUserId));
    }
}


