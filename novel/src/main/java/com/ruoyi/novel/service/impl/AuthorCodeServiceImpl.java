package com.ruoyi.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.novel.domain.AuthorCode;
import com.ruoyi.novel.service.AuthorCodeService;
import com.ruoyi.novel.mapper.AuthorCodeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
* @author 64829
* @description 针对表【author_code(作家邀请码表)】的数据库操作Service实现
* @createDate 2022-04-12 14:01:23
*/
@Service
public class AuthorCodeServiceImpl extends ServiceImpl<AuthorCodeMapper, AuthorCode>
    implements AuthorCodeService{

    @Resource
    private AuthorCodeMapper authorCodeMapper;

    /**
     * 查询邀请码信息集合
     * @param authorCode
     * @return
     */
    @Override
    public List<AuthorCode> selectAuthorCodeList(AuthorCode authorCode) {
        QueryWrapper<AuthorCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(authorCode.getInviteCode()),"invite_code",authorCode.getInviteCode())
                .eq(StringUtils.isNotEmpty(authorCode.getCreateUser()),"create_user",authorCode.getCreateUser())
                .eq(StringUtils.isNotNull(authorCode.getIsUse()),"is_use",authorCode.getIsUse());
        return authorCodeMapper.selectList(queryWrapper);
    }

    /**
     * 根据id查询邀请码
     * @param id
     * @return
     */
    @Override
    public AuthorCode selectAuthorCodeById(Long id) {

        return  authorCodeMapper.selectById(id);

    }

    /**
     * 校验邀请码信息是否唯一
     * @param authorCode
     * @return
     */
    @Override
    public String checkInviteCodeUnique(AuthorCode authorCode) {

        Long id = StringUtils.isNull(authorCode.getId()) ? -1L : authorCode.getId();

        QueryWrapper<AuthorCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("invite_code",authorCode.getInviteCode());
        AuthorCode info = authorCodeMapper.selectOne(queryWrapper);

        if (StringUtils.isNotNull(info) && info.getId().longValue() != id.longValue())
        {
            return UserConstants.POST_CODE_NOT_UNIQUE;
        }
        return UserConstants.POST_CODE_UNIQUE;
    }

    /**
     * 批量删除邀请码信息
     * @param ids
     * @return
     */
    @Override
    public int deleteAuthorCodeByIds(String ids) {

        Long[] authorCodeIds = Convert.toLongArray(ids);

        return authorCodeMapper.deleteBatchIds(Arrays.asList(authorCodeIds));

    }

    /**
     * 检查邀请码是否有效
     * @param inviteCode
     * @return
     */
    @Override
    public Boolean checkCode(String inviteCode) {

        QueryWrapper<AuthorCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("invite_code",inviteCode).eq("is_use",0);
        AuthorCode authorCode = authorCodeMapper.selectOne(queryWrapper);

        if (StringUtils.isNotNull(authorCode)){
            return true;
        }

        return false;
    }

    /**
     * 根据邀请码内容查询记录
     * @param inviteCode
     * @return
     */
    @Override
    public AuthorCode selectAuthorCodeByCode(String inviteCode) {

        QueryWrapper<AuthorCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("invite_code",inviteCode);
        AuthorCode authorCode = authorCodeMapper.selectOne(queryWrapper);

        return authorCode;
    }
}




