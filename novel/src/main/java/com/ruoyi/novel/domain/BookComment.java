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
 * @TableName book_comment
 */
@TableName(value ="book_comment")
@Data
public class BookComment implements Serializable {
    /**
     * 评论主键
     */
    @TableId
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    /**
     * 小说id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long bookId;

    /**
     * 评论人id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long userId;

    /**
     * 评论人姓名
     */
    private String userName;

    /**
     * 评论分数
     */
    private Integer score;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 父评论id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long parentId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}