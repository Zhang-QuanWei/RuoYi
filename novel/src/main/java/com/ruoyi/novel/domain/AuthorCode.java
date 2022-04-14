package com.ruoyi.novel.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 作家邀请码表
 * @TableName author_code
 */
@TableName(value ="author_code")
@Data
public class AuthorCode implements Serializable {
    /**
     * 主键
     */
    @TableId
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 是否使用过，0：未使用，1:已使用
     */
    private Integer isUse;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建人ID
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long createUserId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}