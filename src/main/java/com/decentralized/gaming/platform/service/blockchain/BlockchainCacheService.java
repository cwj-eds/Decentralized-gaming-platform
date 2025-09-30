package com.decentralized.gaming.platform.service.blockchain;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 区块链缓存服务接口
 * 提供区块链数据的本地缓存功能
 *
 * @author DecentralizedGamingPlatform
 */
public interface BlockchainCacheService {

    /**
     * 缓存余额
     *
     * @param address 地址
     * @param balance 余额
     */
    void cacheBalance(String address, BigDecimal balance);

    /**
     * 获取缓存的余额
     *
     * @param address 地址
     * @return 缓存的余额
     */
    BigDecimal getCachedBalance(String address);

    /**
     * 缓存Gas价格
     *
     * @param gasPrice Gas价格
     */
    void cacheGasPrice(BigInteger gasPrice);

    /**
     * 获取缓存的Gas价格
     *
     * @return 缓存的Gas价格
     */
    BigInteger getCachedGasPrice();

    /**
     * 缓存区块号
     *
     * @param blockNumber 区块号
     */
    void cacheBlockNumber(BigInteger blockNumber);

    /**
     * 获取缓存的区块号
     *
     * @return 缓存的区块号
     */
    BigInteger getCachedBlockNumber();

    /**
     * 清除地址相关的缓存
     *
     * @param address 地址
     */
    void clearAddressCache(String address);

    /**
     * 清除所有区块链缓存
     */
    void clearAllCache();

    /**
     * 获取缓存统计信息
     *
     * @return 缓存统计信息
     */
    CacheStats getCacheStats();

    /**
     * 缓存统计信息
     */
    class CacheStats {
        private final int balanceCacheSize;
        private final int gasPriceCacheSize;
        private final int blockNumberCacheSize;

        public CacheStats(int balanceCacheSize, int gasPriceCacheSize, int blockNumberCacheSize) {
            this.balanceCacheSize = balanceCacheSize;
            this.gasPriceCacheSize = gasPriceCacheSize;
            this.blockNumberCacheSize = blockNumberCacheSize;
        }

        public int getBalanceCacheSize() { return balanceCacheSize; }
        public int getGasPriceCacheSize() { return gasPriceCacheSize; }
        public int getBlockNumberCacheSize() { return blockNumberCacheSize; }
        public int getTotalCacheSize() { return balanceCacheSize + gasPriceCacheSize + blockNumberCacheSize; }
    }
}
