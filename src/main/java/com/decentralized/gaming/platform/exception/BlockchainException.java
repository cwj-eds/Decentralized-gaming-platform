package com.decentralized.gaming.platform.exception;

/**
 * 区块链相关异常
 *
 * @author DecentralizedGamingPlatform
 */
public class BlockchainException extends RuntimeException {
    
    private final String errorCode;
    private final String operation;
    
    public BlockchainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.operation = null;
    }
    
    public BlockchainException(String errorCode, String operation, String message) {
        super(message);
        this.errorCode = errorCode;
        this.operation = operation;
    }
    
    public BlockchainException(String errorCode, String operation, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.operation = operation;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getOperation() {
        return operation;
    }
    
    // 预定义的错误代码
    public static class ErrorCodes {
        public static final String NETWORK_ERROR = "BLOCKCHAIN_NETWORK_ERROR";
        public static final String TRANSACTION_FAILED = "BLOCKCHAIN_TRANSACTION_FAILED";
        public static final String INVALID_ADDRESS = "BLOCKCHAIN_INVALID_ADDRESS";
        public static final String INSUFFICIENT_BALANCE = "BLOCKCHAIN_INSUFFICIENT_BALANCE";
        public static final String GAS_ESTIMATION_FAILED = "BLOCKCHAIN_GAS_ESTIMATION_FAILED";
        public static final String CONTRACT_CALL_FAILED = "BLOCKCHAIN_CONTRACT_CALL_FAILED";
        public static final String SIGNATURE_VERIFICATION_FAILED = "BLOCKCHAIN_SIGNATURE_VERIFICATION_FAILED";
        public static final String TIMEOUT = "BLOCKCHAIN_TIMEOUT";
    }
}