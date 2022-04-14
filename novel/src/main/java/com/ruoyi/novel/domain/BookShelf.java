package com.ruoyi.novel.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 
 * @TableName book_shelf
 */
@TableName(value ="book_shelf")
@Data
public class BookShelf implements Serializable {
    /**
     * 主键
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
     * 用户id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long userId;

    /**
     * 章节序号
     */
    private Integer chapterIndex;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}