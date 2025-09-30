package com.decentralized.gaming.platform.service;

import com.decentralized.gaming.platform.entity.User;
import com.decentralized.gaming.platform.exception.BusinessException;
import com.decentralized.gaming.platform.service.blockchain.BlockchainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetCode;
import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * 钱包服务
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
public class WalletService {

    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private Web3j web3j;

    // 以太坊地址正则表达式
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^0x[a-fA-F0-9]{40}$");

    /**
     * 验证以太坊地址格式
     *
     * @param address 钱包地址
     * @return 是否有效
     */
    public boolean isValidAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return false;
        }
        return ADDRESS_PATTERN.matcher(address).matches();
    }

    /**
     * 验证钱包签名并登录
     *
     * @param walletAddress 钱包地址
     * @param signature 签名
     * @param message 原始消息
     * @return 用户信息
     */
    public User authenticateWallet(String walletAddress, String signature, String message) {
        // 验证地址格式
        if (!isValidAddress(walletAddress)) {
            throw new BusinessException("无效的钱包地址格式");
        }

        // 验证签名
        if (!blockchainService.verifySignature(message, signature, walletAddress)) {
            throw new BusinessException("签名验证失败");
        }

        // 检查网络连接
        if (!blockchainService.isConnected()) {
            throw new BusinessException("区块链网络连接失败");
        }

        // 获取用户余额
        BigDecimal balance = blockchainService.getBalance(walletAddress);
        log.info("用户 {} 钱包余额: {} ETH", walletAddress, balance);

        // 创建或更新用户信息
        User user = new User();
        user.setWalletAddress(walletAddress);
        user.setUsername(generateUsernameFromAddress(walletAddress));
        
        return user;
    }

    /**
     * 从地址生成用户名
     *
     * @param address 钱包地址
     * @return 用户名
     */
    private String generateUsernameFromAddress(String address) {
        return "User_" + address.substring(2, 8).toUpperCase();
    }

    /**
     * 生成登录消息
     *
     * @param timestamp 时间戳
     * @return 登录消息
     */
    public String generateLoginMessage(long timestamp) {
        return String.format("登录到去中心化游戏平台\n时间戳: %d", timestamp);
    }

    /**
     * 检查地址是否为合约地址
     *
     * @param address 地址
     * @return 是否为合约地址
     */
    public boolean isContractAddress(String address) {
        try {
            // 首先验证地址格式
            if (!isValidAddress(address)) {
                return false;
            }

            // 检查地址是否有合约代码
            EthGetCode ethGetCode = web3j.ethGetCode(address, DefaultBlockParameterName.LATEST).send();
            String code = ethGetCode.getCode();
            
            // 如果代码不为空且不是"0x"，则说明是合约地址
            boolean isContract = code != null && !code.equals("0x") && !code.isEmpty();
            
            log.debug("地址 {} 的合约代码: {}, 是否为合约: {}", address, code, isContract);
            return isContract;
        } catch (Exception e) {
            log.error("检查合约地址失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取地址的校验和格式
     *
     * @param address 地址
     * @return 校验和格式地址
     */
    public String toChecksumAddress(String address) {
        try {
            return Keys.toChecksumAddress(address);
        } catch (Exception e) {
            log.error("转换为校验和地址失败: {}", e.getMessage());
            return address;
        }
    }
}
