package com.ruoyi.novel.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.novel.mapper.AuthorCodeMapper;
import com.ruoyi.novel.domain.AuthorCode;
import com.ruoyi.novel.service.IAuthorCodeService;
import com.ruoyi.common.core.text.Convert;

/**
 * 邀请码Service业务层处理
 * 
 * @author zqw
 * @date 2022-03-06
 */
@Service
public class AuthorCodeServiceImpl implements IAuthorCodeService 
{
    @Autowired
    private AuthorCodeMapper authorCodeMapper;

    /**
     * 查询邀请码
     * 
     * @param id 邀请码主键
     * @return 邀请码
     */
    @Override
    public AuthorCode selectAuthorCodeById(Long id)
    {
        return authorCodeMapper.selectAuthorCodeById(id);
    }

    /**
     * 查询邀请码列表
     * 
     * @param authorCode 邀请码
     * @return 邀请码
     */
    @Override
    public List<AuthorCode> selectAuthorCodeList(AuthorCode authorCode)
    {
        return authorCodeMapper.selectAuthorCodeList(authorCode);
    }

    /**
     * 新增邀请码
     * 
     * @param authorCode 邀请码
     * @return 结果
     */
    @Override
    public int insertAuthorCode(AuthorCode authorCode)
    {
        authorCode.setCreateTime(DateUtils.getNowDate());
        return authorCodeMapper.insertAuthorCode(authorCode);
    }

    /**
     * 修改邀请码
     * 
     * @param authorCode 邀请码
     * @return 结果
     */
    @Override
    public int updateAuthorCode(AuthorCode authorCode)
    {
        return authorCodeMapper.updateAuthorCode(authorCode);
    }

    /**
     * 批量删除邀请码
     * 
     * @param ids 需要删除的邀请码主键
     * @return 结果
     */
    @Override
    public int deleteAuthorCodeByIds(String ids)
    {
        return authorCodeMapper.deleteAuthorCodeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除邀请码信息
     * 
     * @param id 邀请码主键
     * @return 结果
     */
    @Override
    public int deleteAuthorCodeById(Long id)
    {
        return authorCodeMapper.deleteAuthorCodeById(id);
    }
}
