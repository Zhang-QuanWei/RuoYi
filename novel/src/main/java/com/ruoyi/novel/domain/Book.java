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
 * @TableName book
 */
@TableName(value ="book")
@Data
public class Book implements Serializable {
    /**
     * 小说主键
     */
    @TableId
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    /**
     * 作者id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long authorId;

    /**
     * 作者姓名
     */
    private String authorName;

    /**
     * 小说名称
     */
    @NotBlank(message = "小说名称不能为空！")
    private String bookName;

    /**
     * 小说分类名称
     */
    private String bookCategory;

    /**
     * 小说封面
     */
    private String picUrl;

    /**
     * 作品简介
     */
    @NotBlank(message = "作品简介不能为空！")
    private String bookIntro;

    /**
     * 作者有话说
     */
    private String authorSpeak;

    /**
     * 作品字数
     */
    private Integer bookWord;

    /**
     * 订阅数
     */
    private Integer subsNum;

    /**
     * 点击量
     */
    private Long visitCount;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 最新章节id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long lastChapterId;

    /**
     * 最新章节标题
     */
    private String lastChapterTitle;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 审核状态， 0：审核中 1：审核通过 2：审核不通过
     */
    private Integer checkStatus;

    /**
     * 完结状态， 0：连载中 1：已完结
     */
    private Integer bookStatus;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}