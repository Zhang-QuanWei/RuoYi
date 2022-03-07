package com.ruoyi.novel.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 小说对象 book
 * 
 * @author zqw
 * @date 2022-03-06
 */
public class Book extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 小说主键 */
    private Long id;

    /** 作者id */
    private Long authorId;

    /** 小说名称 */
    @Excel(name = "小说名称")
    private String bookName;

    /** 小说分类 */
    @Excel(name = "小说分类")
    private Long bookCategory;

    /** 目录号 */
    private Long bookIndexId;

    /** 小说封面 */
    @Excel(name = "小说封面")
    private String picUrl;

    /** 作品简介 */
    @Excel(name = "作品简介")
    private String bookIntro;

    /** 作者有话说 */
    @Excel(name = "作者有话说")
    private String authorSpeak;

    /** 作品字数 */
    @Excel(name = "作品字数")
    private Long bookWord;

    /** 订阅数 */
    @Excel(name = "订阅数")
    private Long subsNum;

    /** 点击量 */
    @Excel(name = "点击量")
    private Long visitCount;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date cteateTime;

    /** 最新章节id */
    private Long lastChapterId;

    /** 最新章节 */
    @Excel(name = "最新章节")
    private String lastChapterTitle;

    /** 审核状态 */
    @Excel(name = "审核状态")
    private Integer checkStatus;

    /** 完结状态 */
    @Excel(name = "完结状态")
    private Integer bookStatus;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setAuthorId(Long authorId) 
    {
        this.authorId = authorId;
    }

    public Long getAuthorId() 
    {
        return authorId;
    }
    public void setBookName(String bookName) 
    {
        this.bookName = bookName;
    }

    public String getBookName() 
    {
        return bookName;
    }
    public void setBookCategory(Long bookCategory) 
    {
        this.bookCategory = bookCategory;
    }

    public Long getBookCategory() 
    {
        return bookCategory;
    }
    public void setBookIndexId(Long bookIndexId) 
    {
        this.bookIndexId = bookIndexId;
    }

    public Long getBookIndexId() 
    {
        return bookIndexId;
    }
    public void setPicUrl(String picUrl) 
    {
        this.picUrl = picUrl;
    }

    public String getPicUrl() 
    {
        return picUrl;
    }
    public void setBookIntro(String bookIntro) 
    {
        this.bookIntro = bookIntro;
    }

    public String getBookIntro() 
    {
        return bookIntro;
    }
    public void setAuthorSpeak(String authorSpeak) 
    {
        this.authorSpeak = authorSpeak;
    }

    public String getAuthorSpeak() 
    {
        return authorSpeak;
    }
    public void setBookWord(Long bookWord) 
    {
        this.bookWord = bookWord;
    }

    public Long getBookWord() 
    {
        return bookWord;
    }
    public void setSubsNum(Long subsNum) 
    {
        this.subsNum = subsNum;
    }

    public Long getSubsNum() 
    {
        return subsNum;
    }
    public void setVisitCount(Long visitCount) 
    {
        this.visitCount = visitCount;
    }

    public Long getVisitCount() 
    {
        return visitCount;
    }
    public void setCteateTime(Date cteateTime) 
    {
        this.cteateTime = cteateTime;
    }

    public Date getCteateTime() 
    {
        return cteateTime;
    }
    public void setLastChapterId(Long lastChapterId) 
    {
        this.lastChapterId = lastChapterId;
    }

    public Long getLastChapterId() 
    {
        return lastChapterId;
    }
    public void setLastChapterTitle(String lastChapterTitle) 
    {
        this.lastChapterTitle = lastChapterTitle;
    }

    public String getLastChapterTitle() 
    {
        return lastChapterTitle;
    }
    public void setCheckStatus(Integer checkStatus) 
    {
        this.checkStatus = checkStatus;
    }

    public Integer getCheckStatus() 
    {
        return checkStatus;
    }
    public void setBookStatus(Integer bookStatus) 
    {
        this.bookStatus = bookStatus;
    }

    public Integer getBookStatus() 
    {
        return bookStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("authorId", getAuthorId())
            .append("bookName", getBookName())
            .append("bookCategory", getBookCategory())
            .append("bookIndexId", getBookIndexId())
            .append("picUrl", getPicUrl())
            .append("bookIntro", getBookIntro())
            .append("authorSpeak", getAuthorSpeak())
            .append("bookWord", getBookWord())
            .append("subsNum", getSubsNum())
            .append("visitCount", getVisitCount())
            .append("cteateTime", getCteateTime())
            .append("lastChapterId", getLastChapterId())
            .append("lastChapterTitle", getLastChapterTitle())
            .append("updateTime", getUpdateTime())
            .append("checkStatus", getCheckStatus())
            .append("bookStatus", getBookStatus())
            .toString();
    }
}
