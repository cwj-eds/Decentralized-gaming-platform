package com.decentralized.gaming.platform.service.blockchain.impl;

import com.decentralized.gaming.platform.contracts.PlatformToken;
import com.decentralized.gaming.platform.exception.BlockchainException;
import com.decentralized.gaming.platform.service.blockchain.BlockchainService;
import com.decentralized.gaming.platform.service.blockchain.PlatformTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

/**
 * 平台代币服务实现类
 * 封装平台代币合约的所有操作
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
public class PlatformTokenServiceImpl implements PlatformTokenService {

    @Autowired
    private PlatformToken platformTokenContract;

    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private ContractGasProvider gasProvider;

    @Autowired
    private org.web3j.protocol.Web3j web3j;

    /**
     * 获取代币名称
     *
     * @return 代币名称
     */
    @Override
    public String getName() {
        try {
            return platformTokenContract.name().send();
        } catch (Exception e) {
            log.error("获取代币名称失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "name", "获取代币名称失败", e);
        }
    }

    /**
     * 获取代币符号
     *
     * @return 代币符号
     */
    @Override
    public String getSymbol() {
        try {
            return platformTokenContract.symbol().send();
        } catch (Exception e) {
            log.error("获取代币符号失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "symbol", "获取代币符号失败", e);
        }
    }

    /**
     * 获取代币精度
     *
     * @return 代币精度
     */
    @Override
    public BigInteger getDecimals() {
        try {
            return platformTokenContract.decimals().send();
        } catch (Exception e) {
            log.error("获取代币精度失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "decimals", "获取代币精度失败", e);
        }
    }

    /**
     * 获取代币总供应量
     *
     * @return 总供应量
     */
    @Override
    public BigInteger getTotalSupply() {
        try {
            return platformTokenContract.totalSupply().send();
        } catch (Exception e) {
            log.error("获取代币总供应量失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "totalSupply", "获取代币总供应量失败", e);
        }
    }

    /**
     * 获取账户余额
     *
     * @param address 账户地址
     * @return 余额
     */
    @Override
    public BigInteger getBalance(String address) {
        try {
            return platformTokenContract.balanceOf(address).send();
        } catch (Exception e) {
            log.error("获取账户余额失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "balanceOf", "获取账户余额失败", e);
        }
    }

    /**
     * 获取授权额度
     *
     * @param owner 拥有者地址
     * @param spender 被授权者地址
     * @return 授权额度
     */
    @Override
    public BigInteger getAllowance(String owner, String spender) {
        try {
            return platformTokenContract.allowance(owner, spender).send();
        } catch (Exception e) {
            log.error("获取授权额度失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "allowance", "获取授权额度失败", e);
        }
    }

    /**
     * 转账
     *
     * @param credentials 发送者凭证
     * @param to 接收者地址
     * @param amount 转账金额
     * @return 交易收据
     */
    @Override
    public TransactionReceipt transfer(Credentials credentials, String to, BigInteger amount) {
        try {
            PlatformToken contract = PlatformToken.load(
                platformTokenContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.transfer(to, amount).send();
            log.info("转账成功: {} -> {}, 金额: {}, 交易哈希: {}", 
                credentials.getAddress(), to, amount, receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("转账失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "transfer", "转账失败", e);
        }
    }

    /**
     * 授权转账
     *
     * @param credentials 授权者凭证
     * @param spender 被授权者地址
     * @param amount 授权金额
     * @return 交易收据
     */
    @Override
    public TransactionReceipt approve(Credentials credentials, String spender, BigInteger amount) {
        try {
            PlatformToken contract = PlatformToken.load(
                platformTokenContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.approve(spender, amount).send();
            log.info("授权成功: {} -> {}, 金额: {}, 交易哈希: {}", 
                credentials.getAddress(), spender, amount, receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("授权失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "approve", "授权失败", e);
        }
    }

    /**
     * 代理转账
     *
     * @param credentials 代理者凭证
     * @param from 发送者地址
     * @param to 接收者地址
     * @param amount 转账金额
     * @return 交易收据
     */
    @Override
    public TransactionReceipt transferFrom(Credentials credentials, String from, String to, BigInteger amount) {
        try {
            PlatformToken contract = PlatformToken.load(
                platformTokenContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.transferFrom(from, to, amount).send();
            log.info("代理转账成功: {} -> {}, 金额: {}, 交易哈希: {}", 
                from, to, amount, receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("代理转账失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "transferFrom", "代理转账失败", e);
        }
    }

    /**
     * 铸造代币
     *
     * @param credentials 操作者凭证
     * @param to 接收者地址
     * @param amount 铸造金额
     * @return 交易收据
     */
    @Override
    public TransactionReceipt mint(Credentials credentials, String to, BigInteger amount) {
        try {
            PlatformToken contract = PlatformToken.load(
                platformTokenContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.mint(to, amount).send();
            log.info("铸造代币成功: 接收者: {}, 金额: {}, 交易哈希: {}", 
                to, amount, receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("铸造代币失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "mint", "铸造代币失败", e);
        }
    }

    /**
     * 销毁代币
     *
     * @param credentials 操作者凭证
     * @param amount 销毁金额
     * @return 交易收据
     */
    @Override
    public TransactionReceipt burn(Credentials credentials, BigInteger amount) {
        try {
            PlatformToken contract = PlatformToken.load(
                platformTokenContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.burn(amount).send();
            log.info("销毁代币成功: 金额: {}, 交易哈希: {}", 
                amount, receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("销毁代币失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "burn", "销毁代币失败", e);
        }
    }

    /**
     * 管理员销毁代币
     *
     * @param credentials 管理员凭证
     * @param account 销毁者地址
     * @param amount 销毁金额
     * @return 交易收据
     */
    @Override
    public TransactionReceipt adminBurn(Credentials credentials, String account, BigInteger amount) {
        try {
            PlatformToken contract = PlatformToken.load(
                platformTokenContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.adminBurn(account, amount).send();
            log.info("管理员销毁代币成功: 销毁者: {}, 金额: {}, 交易哈希: {}", 
                account, amount, receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("管理员销毁代币失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "burnFrom", "管理员销毁代币失败", e);
        }
    }

    /**
     * 转移所有权
     *
     * @param credentials 当前拥有者凭证
     * @param newOwner 新拥有者地址
     * @return 交易收据
     */
    @Override
    public TransactionReceipt transferOwnership(Credentials credentials, String newOwner) {
        try {
            PlatformToken contract = PlatformToken.load(
                platformTokenContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.transferOwnership(newOwner).send();
            log.info("转移所有权成功: {} -> {}, 交易哈希: {}", 
                credentials.getAddress(), newOwner, receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("转移所有权失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "transferOwnership", "转移所有权失败", e);
        }
    }

    /**
     * 放弃所有权
     *
     * @param credentials 当前拥有者凭证
     * @return 交易收据
     */
    @Override
    public TransactionReceipt renounceOwnership(Credentials credentials) {
        try {
            PlatformToken contract = PlatformToken.load(
                platformTokenContract.getContractAddress(),
                web3j,
                credentials,
                gasProvider
            );
            
            TransactionReceipt receipt = contract.renounceOwnership().send();
            log.info("放弃所有权成功: 操作者: {}, 交易哈希: {}", 
                credentials.getAddress(), receipt.getTransactionHash());
            return receipt;
        } catch (Exception e) {
            log.error("放弃所有权失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.TRANSACTION_FAILED, "renounceOwnership", "放弃所有权失败", e);
        }
    }

    /**
     * 获取合约拥有者
     *
     * @return 拥有者地址
     */
    @Override
    public String getOwner() {
        try {
            return platformTokenContract.owner().send();
        } catch (Exception e) {
            log.error("获取合约拥有者失败: {}", e.getMessage());
            throw new BlockchainException(BlockchainException.ErrorCodes.CONTRACT_CALL_FAILED, "owner", "获取合约拥有者失败", e);
        }
    }

    /**
     * 异步转账
     *
     * @param credentials 发送者凭证
     * @param to 接收者地址
     * @param amount 转账金额
     * @return 异步交易结果
     */
    @Override
    public CompletableFuture<TransactionReceipt> transferAsync(Credentials credentials, String to, BigInteger amount) {
        return CompletableFuture.supplyAsync(() -> transfer(credentials, to, amount));
    }

    /**
     * 异步授权
     *
     * @param credentials 授权者凭证
     * @param spender 被授权者地址
     * @param amount 授权金额
     * @return 异步交易结果
     */
    @Override
    public CompletableFuture<TransactionReceipt> approveAsync(Credentials credentials, String spender, BigInteger amount) {
        return CompletableFuture.supplyAsync(() -> approve(credentials, spender, amount));
    }

    /**
     * 检查交易是否成功
     *
     * @param transactionHash 交易哈希
     * @return 是否成功
     */
    @Override
    public boolean isTransactionSuccessful(String transactionHash) {
        return blockchainService.isTransactionSuccessful(transactionHash);
    }

    /**
     * 等待交易确认
     *
     * @param transactionHash 交易哈希
     * @param maxWaitTime 最大等待时间(秒)
     * @return 交易收据
     */
    @Override
    public TransactionReceipt waitForTransactionReceipt(String transactionHash, int maxWaitTime) {
        return blockchainService.waitForTransactionReceipt(transactionHash, maxWaitTime);
    }
}
