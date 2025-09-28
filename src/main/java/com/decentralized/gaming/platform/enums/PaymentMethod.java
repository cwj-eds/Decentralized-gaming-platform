package com.decentralized.gaming.platform.enums;

/**
 * 支付方式枚举
 *
 * @author DecentralizedGamingPlatform
 */
public enum PaymentMethod {

    /**
     * 信用卡
     */
    CREDIT_CARD("CREDIT_CARD", "信用卡"),

    /**
     * 银行转账
     */
    BANK_TRANSFER("BANK_TRANSFER", "银行转账"),

    /**
     * 支付宝
     */
    ALIPAY("ALIPAY", "支付宝"),

    /**
     * 微信支付
     */
    WECHAT_PAY("WECHAT_PAY", "微信支付"),

    /**
     * PayPal
     */
    PAYPAL("PAYPAL", "PayPal"),

    /**
     * 加密货币
     */
    CRYPTO("CRYPTO", "加密货币");

    private final String code;
    private final String description;

    PaymentMethod(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static PaymentMethod fromCode(String code) {
        for (PaymentMethod method : values()) {
            if (method.code.equals(code)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Unknown payment method code: " + code);
    }
}
