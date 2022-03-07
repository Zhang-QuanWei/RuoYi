package com.ruoyi.novel.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 邀请码对象 author_code
 * 
 * @author zqw
 * @date 2022-03-06
 */
public class AuthorCode extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createUserName;

    /** 邀请码内容 */
    @Excel(name = "邀请码内容")
    private String code;

    /** 有效时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "有效时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date validityTime;

    /** 使用状态 */
    @Excel(name = "使用状态")
    private Integer isUse;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCreateUserName(String createUserName) 
    {
        this.createUserName = createUserName;
    }

    public String getCreateUserName() 
    {
        return createUserName;
    }
    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }
    public void setValidityTime(Date validityTime) 
    {
        this.validityTime = validityTime;
    }

    public Date getValidityTime() 
    {
        return validityTime;
    }
    public void setIsUse(Integer isUse) 
    {
        this.isUse = isUse;
    }

    public Integer getIsUse() 
    {
        return isUse;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("createUserName", getCreateUserName())
            .append("code", getCode())
            .append("validityTime", getValidityTime())
            .append("createTime", getCreateTime())
            .append("isUse", getIsUse())
            .toString();
    }
}
