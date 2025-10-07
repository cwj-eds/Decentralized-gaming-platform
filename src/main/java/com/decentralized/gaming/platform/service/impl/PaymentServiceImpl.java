package com.decentralized.gaming.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.decentralized.gaming.platform.common.PageResult;
import com.decentralized.gaming.platform.dto.CreateRechargeRequest;
import com.decentralized.gaming.platform.dto.BankRechargeRequest;
import com.decentralized.gaming.platform.entity.RechargeRecord;
import com.decentralized.gaming.platform.entity.UserBalance;
import com.decentralized.gaming.platform.mapper.RechargeRecordMapper;
import com.decentralized.gaming.platform.mapper.UserBalanceMapper;
import com.decentralized.gaming.platform.service.PaymentService;
import com.decentralized.gaming.platform.vo.RechargeRecordVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final RechargeRecordMapper rechargeRecordMapper;
    private final UserBalanceMapper userBalanceMapper;

    @Override
    @Transactional
    public RechargeRecordVO createRechargeOrder(Long userId, CreateRechargeRequest request) {
        RechargeRecord record = new RechargeRecord();
        record.setUserId(userId);
        record.setAmount(request.getAmount());
        record.setCurrency(request.getCurrency());
        record.setPaymentMethod(request.getPaymentMethod());
        record.setStatus(RechargeRecord.RechargeStatus.PENDING);
        // 生成“二维码内容”，这里用 UUID 模拟支付单号
        record.setPaymentId("QR-" + UUID.randomUUID());
        rechargeRecordMapper.insert(record);

        RechargeRecordVO vo = new RechargeRecordVO();
        BeanUtils.copyProperties(record, vo);
        return vo;
    }

    @Override
    @Transactional
    public boolean confirmRecharge(Long orderId, String paymentId) {
        RechargeRecord record = rechargeRecordMapper.selectById(orderId);
        if (record == null || record.getStatus() != RechargeRecord.RechargeStatus.PENDING) {
            return false;
        }
        record.setPaymentId(paymentId != null ? paymentId : record.getPaymentId());
        record.setStatus(RechargeRecord.RechargeStatus.COMPLETED);
        rechargeRecordMapper.updateById(record);

        // 充值成功 -> 增加用户平台代币余额（user_balances）
        LambdaQueryWrapper<UserBalance> wrapper = new LambdaQueryWrapper<UserBalance>()
                .eq(UserBalance::getUserId, record.getUserId())
                .eq(UserBalance::getTokenType, "PLATFORM_TOKEN");
        UserBalance balance = userBalanceMapper.selectOne(wrapper);
        if (balance == null) {
            balance = new UserBalance();
            balance.setUserId(record.getUserId());
            balance.setTokenType("PLATFORM_TOKEN");
            balance.setBalance(BigDecimal.ZERO);
            userBalanceMapper.insert(balance);
        }
        balance.setBalance(balance.getBalance().add(record.getAmount()));
        userBalanceMapper.updateById(balance);
        return true;
    }

    @Override
    public PageResult<RechargeRecordVO> getUserRechargeRecords(Long userId, int page, int size) {
        Page<RechargeRecord> p = new Page<>(page, size);
        LambdaQueryWrapper<RechargeRecord> wrapper = new LambdaQueryWrapper<RechargeRecord>()
                .eq(RechargeRecord::getUserId, userId)
                .orderByDesc(RechargeRecord::getCreatedAt);
        IPage<RechargeRecord> res = rechargeRecordMapper.selectPage(p, wrapper);
        List<RechargeRecordVO> vos = res.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return PageResult.of(vos, res.getTotal(), (long) page, (long) size);
    }

    @Override
    @Transactional
    public RechargeRecordVO bankRecharge(Long userId, BankRechargeRequest request) {
        // 录入一条待审核/待对账的记录
        RechargeRecord record = new RechargeRecord();
        record.setUserId(userId);
        record.setAmount(request.getAmount());
        record.setCurrency(request.getCurrency());
        record.setPaymentMethod("BANK_TRANSFER");
        record.setPaymentId(request.getReferenceNo());
        record.setStatus(RechargeRecord.RechargeStatus.PENDING);
        rechargeRecordMapper.insert(record);

        // 这里可接入对账系统；演示：直接确认入账
        confirmRecharge(record.getId(), record.getPaymentId());
        return toVO(rechargeRecordMapper.selectById(record.getId()));
    }

    private RechargeRecordVO toVO(RechargeRecord r){
        RechargeRecordVO vo = new RechargeRecordVO();
        BeanUtils.copyProperties(r, vo);
        return vo;
    }
}


