package com.decentralized.gaming.platform.enums;

/**
 * 代币类型枚举
 *
 * @author DecentralizedGamingPlatform
 */
public enum TokenType {

    /**
     * 平台代币
     */
    PLATFORM_TOKEN("PLT", "平台代币"),

    /**
     * 以太坊
     */
    ETH("ETH", "以太坊"),

    /**
     * 美元
     */
    USD("USD", "美元"),

    /**
     * 人民币
     */
    CNY("CNY", "人民币");

    private final String code;
    private final String description;

    TokenType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static TokenType fromCode(String code) {
        for (TokenType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown token type code: " + code);
    }
}
