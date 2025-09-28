package com.decentralized.gaming.platform.exception;

/**
 * 区块链异常
 *
 * @author DecentralizedGamingPlatform
 */
public class BlockchainException extends BusinessException {

    public BlockchainException(String message) {
        super(message);
    }

    public BlockchainException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlockchainException(int code, String message) {
        super(code, message);
    }

    public BlockchainException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
} 