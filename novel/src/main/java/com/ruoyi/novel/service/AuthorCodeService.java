package com.ruoyi.novel.service;

import com.ruoyi.novel.domain.AuthorCode;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 64829
* @description 针对表【author_code(作家邀请码表)】的数据库操作Service
* @createDate 2022-04-12 14:01:23
*/
public interface AuthorCodeService extends IService<AuthorCode> {

    /**
     * 查询邀请码信息集合
     * @param authorCode
     * @return
     */
    List<AuthorCode> selectAuthorCodeList(AuthorCode authorCode);

    /**
     * 校检邀请码内容
     * @param authorCode
     * @return
     */
    String checkInviteCodeUnique(AuthorCode authorCode);

    /**
     * 批量删除邀请码
     * @param ids
     * @return
     */
    int deleteAuthorCodeByIds(String ids);

    /**
     * 通过id查询邀请码信息
     * @param id
     * @return
     */
    AuthorCode selectAuthorCodeById(Long id);

    /**
     * 检查邀请码是否有效
     * @param inviteCode
     * @return
     */
    Boolean checkCode(String inviteCode);

    /**
     * 根据邀请码内容查询记录
     * @param inviteCode
     * @return
     */
    AuthorCode selectAuthorCodeByCode(String inviteCode);
}
