package com.decentralized.gaming.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 系统配置实体类
 *
 * @author DecentralizedGamingPlatform
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("system_configs")
public class SystemConfig {

    /**
     * 配置ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 配置键
     */
    @TableField("config_key")
    private String configKey;

    /**
     * 配置值
     */
    @TableField("config_value")
    private String configValue;

    /**
     * 配置类型
     */
    @TableField("config_type")
    private ConfigType configType;

    /**
     * 配置描述
     */
    @TableField("description")
    private String description;

    /**
     * 是否启用
     */
    @TableField("is_active")
    private Boolean isActive;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 配置类型枚举
     */
    public enum ConfigType {
        STRING("字符串"),
        INTEGER("整数"),
        DECIMAL("小数"),
        BOOLEAN("布尔值"),
        JSON("JSON");

        private final String description;

        ConfigType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
