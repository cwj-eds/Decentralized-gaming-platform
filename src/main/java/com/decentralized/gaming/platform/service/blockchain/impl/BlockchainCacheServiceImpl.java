package com.decentralized.gaming.platform.service.blockchain.impl;

import com.decentralized.gaming.platform.service.blockchain.BlockchainCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 区块链缓存服务实现类
 * 提供区块链数据的本地缓存功能
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@Service
public class BlockchainCacheServiceImpl implements BlockchainCacheService {

    @Value("${app.blockchain.cache.balance-ttl:300}")
    private long balanceTtl; // 余额缓存5分钟

    @Value("${app.blockchain.cache.gas-price-ttl:60}")
    private long gasPriceTtl; // Gas价格缓存1分钟

    @Value("${app.blockchain.cache.block-number-ttl:10}")
    private long blockNumberTtl; // 区块号缓存10秒

    private final ConcurrentHashMap<String, CacheEntry<BigDecimal>> balanceCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CacheEntry<BigInteger>> gasPriceCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CacheEntry<BigInteger>> blockNumberCache = new ConcurrentHashMap<>();
    
    private final ScheduledExecutorService cleanupScheduler = Executors.newSingleThreadScheduledExecutor();

    public BlockchainCacheServiceImpl() {
        // 启动定期清理过期缓存的线程
        cleanupScheduler.scheduleAtFixedRate(this::cleanupExpiredEntries, 60, 60, TimeUnit.SECONDS);
    }

    /**
     * 缓存余额
     */
    @Override
    public void cacheBalance(String address, BigDecimal balance) {
        String key = address.toLowerCase();
        balanceCache.put(key, new CacheEntry<>(balance, System.currentTimeMillis() + balanceTtl * 1000));
        log.debug("缓存余额: {} -> {}", address, balance);
    }

    /**
     * 获取缓存的余额
     */
    @Override
    public BigDecimal getCachedBalance(String address) {
        String key = address.toLowerCase();
        CacheEntry<BigDecimal> entry = balanceCache.get(key);
        if (entry != null && !entry.isExpired()) {
            log.debug("从缓存获取余额: {} -> {}", address, entry.getValue());
            return entry.getValue();
        }
        return null;
    }

    /**
     * 缓存Gas价格
     */
    @Override
    public void cacheGasPrice(BigInteger gasPrice) {
        gasPriceCache.put("current", new CacheEntry<>(gasPrice, System.currentTimeMillis() + gasPriceTtl * 1000));
        log.debug("缓存Gas价格: {}", gasPrice);
    }

    /**
     * 获取缓存的Gas价格
     */
    @Override
    public BigInteger getCachedGasPrice() {
        CacheEntry<BigInteger> entry = gasPriceCache.get("current");
        if (entry != null && !entry.isExpired()) {
            log.debug("从缓存获取Gas价格: {}", entry.getValue());
            return entry.getValue();
        }
        return null;
    }

    /**
     * 缓存区块号
     */
    @Override
    public void cacheBlockNumber(BigInteger blockNumber) {
        blockNumberCache.put("current", new CacheEntry<>(blockNumber, System.currentTimeMillis() + blockNumberTtl * 1000));
        log.debug("缓存区块号: {}", blockNumber);
    }

    /**
     * 获取缓存的区块号
     */
    @Override
    public BigInteger getCachedBlockNumber() {
        CacheEntry<BigInteger> entry = blockNumberCache.get("current");
        if (entry != null && !entry.isExpired()) {
            log.debug("从缓存获取区块号: {}", entry.getValue());
            return entry.getValue();
        }
        return null;
    }

    /**
     * 清除地址相关的缓存
     */
    @Override
    public void clearAddressCache(String address) {
        String key = address.toLowerCase();
        balanceCache.remove(key);
        log.debug("清除地址缓存: {}", address);
    }

    /**
     * 清除所有区块链缓存
     */
    @Override
    public void clearAllCache() {
        balanceCache.clear();
        gasPriceCache.clear();
        blockNumberCache.clear();
        log.info("清除所有区块链缓存");
    }

    /**
     * 获取缓存统计信息
     */
    @Override
    public CacheStats getCacheStats() {
        return new CacheStats(
            balanceCache.size(),
            gasPriceCache.size(),
            blockNumberCache.size()
        );
    }

    /**
     * 清理过期的缓存条目
     */
    private void cleanupExpiredEntries() {
        balanceCache.entrySet().removeIf(entry -> entry.getValue().isExpired());
        gasPriceCache.entrySet().removeIf(entry -> entry.getValue().isExpired());
        blockNumberCache.entrySet().removeIf(entry -> entry.getValue().isExpired());
        
        log.debug("清理过期缓存条目完成");
    }

    /**
     * 缓存条目
     */
    private static class CacheEntry<T> {
        private final T value;
        private final long expiryTime;

        public CacheEntry(T value, long expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }

        public T getValue() {
            return value;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }
}
