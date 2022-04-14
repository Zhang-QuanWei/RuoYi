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
 * @TableName book_check
 */
@TableName(value ="book_check")
@Data
public class BookCheck implements Serializable {
    /**
     * 审核记录id
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
     * 小说名称
     */
    private String bookName;

    /**
     * 编辑id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long editorId;

    /**
     * 编辑姓名
     */
    private String editorName;

    /**
     * 审核意见
     */
    private String checkOpinion;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}