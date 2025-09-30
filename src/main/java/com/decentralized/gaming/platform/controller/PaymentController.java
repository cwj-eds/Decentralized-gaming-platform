package com.decentralized.gaming.platform.controller;

import com.decentralized.gaming.platform.common.PageResult;
import com.decentralized.gaming.platform.common.Result;
import com.decentralized.gaming.platform.dto.CreateRechargeRequest;
import com.decentralized.gaming.platform.dto.BankRechargeRequest;
import com.decentralized.gaming.platform.service.PaymentService;
import com.decentralized.gaming.platform.vo.RechargeRecordVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/recharge")
    public String recharge(Model model){
        log.info("访问充值页面");
        return "payment/recharge";
    }

    // ===== API =====
    @PostMapping("/api/recharge")
    @ResponseBody
    public Result<RechargeRecordVO> create(@Valid @RequestBody CreateRechargeRequest request){
        Long currentUserId = 1L; // TODO: 从登录态获取
        return Result.success(paymentService.createRechargeOrder(currentUserId, request));
    }

    @PostMapping("/api/recharge/{orderId}/confirm")
    @ResponseBody
    public Result<Boolean> confirm(@PathVariable Long orderId, @RequestParam(required = false) String paymentId){
        boolean ok = paymentService.confirmRecharge(orderId, paymentId);
        return ok ? Result.success(true) : Result.error("确认失败");
    }

    @GetMapping("/api/recharge/user/{userId}")
    @ResponseBody
    public Result<PageResult<RechargeRecordVO>> records(@PathVariable Long userId,
                                                        @RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "10") int size){
        return Result.success(paymentService.getUserRechargeRecords(userId, page, size));
    }

    // 银行转账充值
    @PostMapping("/api/recharge/bank")
    @ResponseBody
    public Result<RechargeRecordVO> bank(@Valid @RequestBody BankRechargeRequest request){
        Long currentUserId = 1L;
        return Result.success(paymentService.bankRecharge(currentUserId, request));
    }
}


