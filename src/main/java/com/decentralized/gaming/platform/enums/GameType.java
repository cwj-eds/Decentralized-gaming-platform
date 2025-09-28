package com.decentralized.gaming.platform.enums;

/**
 * 游戏类型枚举
 *
 * @author DecentralizedGamingPlatform
 */
public enum GameType {

    /**
     * 动作游戏
     */
    ACTION("ACTION", "动作游戏"),

    /**
     * 冒险游戏
     */
    ADVENTURE("ADVENTURE", "冒险游戏"),

    /**
     * 角色扮演游戏
     */
    RPG("RPG", "角色扮演游戏"),

    /**
     * 策略游戏
     */
    STRATEGY("STRATEGY", "策略游戏"),

    /**
     * 益智游戏
     */
    PUZZLE("PUZZLE", "益智游戏"),

    /**
     * 模拟游戏
     */
    SIMULATION("SIMULATION", "模拟游戏"),

    /**
     * 竞速游戏
     */
    RACING("RACING", "竞速游戏"),

    /**
     * 体育游戏
     */
    SPORTS("SPORTS", "体育游戏"),

    /**
     * 射击游戏
     */
    SHOOTER("SHOOTER", "射击游戏"),

    /**
     * 休闲游戏
     */
    CASUAL("CASUAL", "休闲游戏");

    private final String code;
    private final String description;

    GameType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static GameType fromCode(String code) {
        for (GameType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown game type code: " + code);
    }
}
