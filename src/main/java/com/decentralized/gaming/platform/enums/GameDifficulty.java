package com.decentralized.gaming.platform.enums;

/**
 * 游戏难度枚举
 *
 * @author DecentralizedGamingPlatform
 */
public enum GameDifficulty {

    /**
     * 简单
     */
    EASY("EASY", "简单"),

    /**
     * 中等
     */
    MEDIUM("MEDIUM", "中等"),

    /**
     * 困难
     */
    HARD("HARD", "困难"),

    /**
     * 专家
     */
    EXPERT("EXPERT", "专家");

    private final String code;
    private final String description;

    GameDifficulty(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static GameDifficulty fromCode(String code) {
        for (GameDifficulty difficulty : values()) {
            if (difficulty.code.equals(code)) {
                return difficulty;
            }
        }
        throw new IllegalArgumentException("Unknown game difficulty code: " + code);
    }
}
