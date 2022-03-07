package com.ruoyi.novel.mapper;

import java.util.List;
import com.ruoyi.novel.domain.AuthorCode;

/**
 * 邀请码Mapper接口
 * 
 * @author zqw
 * @date 2022-03-06
 */
public interface AuthorCodeMapper 
{
    /**
     * 查询邀请码
     * 
     * @param id 邀请码主键
     * @return 邀请码
     */
    public AuthorCode selectAuthorCodeById(Long id);

    /**
     * 查询邀请码列表
     * 
     * @param authorCode 邀请码
     * @return 邀请码集合
     */
    public List<AuthorCode> selectAuthorCodeList(AuthorCode authorCode);

    /**
     * 新增邀请码
     * 
     * @param authorCode 邀请码
     * @return 结果
     */
    public int insertAuthorCode(AuthorCode authorCode);

    /**
     * 修改邀请码
     * 
     * @param authorCode 邀请码
     * @return 结果
     */
    public int updateAuthorCode(AuthorCode authorCode);

    /**
     * 删除邀请码
     * 
     * @param id 邀请码主键
     * @return 结果
     */
    public int deleteAuthorCodeById(Long id);

    /**
     * 批量删除邀请码
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAuthorCodeByIds(String[] ids);
}
