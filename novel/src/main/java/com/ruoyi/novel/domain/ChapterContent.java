package com.ruoyi.novel.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 
 * @TableName chapter_content
 */
@TableName(value ="chapter_content")
@Data
public class ChapterContent implements Serializable {
    /**
     * 主键
     */
    @TableId
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    /**
     * 小说章节内容
     */
    @NotBlank(message = "章节内容不能为空！")
    private String chapterContent;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}