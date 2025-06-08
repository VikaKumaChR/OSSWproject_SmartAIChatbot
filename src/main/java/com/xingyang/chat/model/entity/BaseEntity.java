package com.xingyang.chat.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base Entity with common fields
 *
 * @author XingYang
 */
@Data
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Primary key ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * Creation time
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * Update time
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * Creator ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * Updater ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * Logical delete flag (0: not deleted, 1: deleted)
     */
    @TableLogic
    private Integer deleted;

    /**
     * Optimistic locking version
     */
    @Version
    private Integer version;
} 