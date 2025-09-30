package com.decentralized.gaming.platform.service;

import com.decentralized.gaming.platform.common.PageResult;
import com.decentralized.gaming.platform.dto.CreateRechargeRequest;
import com.decentralized.gaming.platform.dto.BankRechargeRequest;
import com.decentralized.gaming.platform.vo.RechargeRecordVO;

public interface PaymentService {

    /** 创建充值订单并返回二维码内容/支付链接占位 */
    RechargeRecordVO createRechargeOrder(Long userId, CreateRechargeRequest request);

    /** 模拟/处理第三方回调，更新状态并记账 */
    boolean confirmRecharge(Long orderId, String paymentId);

    /** 分页查询用户充值记录（LambdaQueryWrapper） */
    PageResult<RechargeRecordVO> getUserRechargeRecords(Long userId, int page, int size);

    /** 银行转账充值（人工审核/自动对账占位） */
    RechargeRecordVO bankRecharge(Long userId, BankRechargeRequest request);
}


