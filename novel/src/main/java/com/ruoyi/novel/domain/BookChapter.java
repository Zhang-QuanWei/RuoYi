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

import javax.validation.constraints.NotBlank;

/**
 * 
 * @TableName book_chapter
 */
@TableName(value ="book_chapter")
@Data
public class BookChapter implements Serializable {
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
     * 章节内容id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long chapterId;

    /**
     * 章节序号
     */
    private Integer chapterIndex;

    /**
     * 章节标题
     */
    @NotBlank(message = "小说标题不能为空！")
    private String chapterTitle;

    /**
     * 章节字数
     */
    private Integer chapterWord;

    /**
     * 章节更新时间
     */
    private Date updateTime;

    /**
     * 审核状态， 0：审核中 1：审核通过 2：审核不通过
     */
    private Integer checkStatus;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}