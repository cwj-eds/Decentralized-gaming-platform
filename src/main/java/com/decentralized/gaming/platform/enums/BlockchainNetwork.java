package com.decentralized.gaming.platform.enums;

/**
 * 区块链网络枚举
 *
 * @author DecentralizedGamingPlatform
 */
public enum BlockchainNetwork {

    /**
     * 以太坊主网
     */
    ETHEREUM_MAINNET("ETHEREUM_MAINNET", "以太坊主网", 1),

    /**
     * 以太坊测试网
     */
    ETHEREUM_TESTNET("ETHEREUM_TESTNET", "以太坊测试网", 5),

    /**
     * BSC主网
     */
    BSC_MAINNET("BSC_MAINNET", "BSC主网", 56),

    /**
     * BSC测试网
     */
    BSC_TESTNET("BSC_TESTNET", "BSC测试网", 97),

    /**
     * Polygon主网
     */
    POLYGON_MAINNET("POLYGON_MAINNET", "Polygon主网", 137),

    /**
     * Polygon测试网
     */
    POLYGON_TESTNET("POLYGON_TESTNET", "Polygon测试网", 80001);

    private final String code;
    private final String description;
    private final Integer chainId;

    BlockchainNetwork(String code, String description, Integer chainId) {
        this.code = code;
        this.description = description;
        this.chainId = chainId;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public Integer getChainId() {
        return chainId;
    }

    public static BlockchainNetwork fromCode(String code) {
        for (BlockchainNetwork network : values()) {
            if (network.code.equals(code)) {
                return network;
            }
        }
        throw new IllegalArgumentException("Unknown blockchain network code: " + code);
    }

    public static BlockchainNetwork fromChainId(Integer chainId) {
        for (BlockchainNetwork network : values()) {
            if (network.chainId.equals(chainId)) {
                return network;
            }
        }
        throw new IllegalArgumentException("Unknown blockchain network chain ID: " + chainId);
    }
}
