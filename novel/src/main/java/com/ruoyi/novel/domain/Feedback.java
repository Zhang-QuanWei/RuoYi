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
 * 
 * @TableName feedback
 */
@TableName(value ="feedback")
@Data
public class Feedback implements Serializable {
    /**
     * 主键
     */
    @TableId
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    /**
     * 反馈内容
     */
    private String feedbackContent;

    /**
     * 解决状态
     */
    private Integer resolutionStatus;

    /**
     * 用户ID
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long userId;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}