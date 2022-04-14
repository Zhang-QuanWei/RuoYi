package com.ruoyi.web.controller.novel;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.novel.domain.AuthorCode;
import com.ruoyi.novel.service.AuthorCodeService;
import com.ruoyi.system.domain.SysPost;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/novel/code")
public class CodeController extends BaseController {

    @Resource
    private AuthorCodeService authorCodeService;

    private String prefix = "/novel/code";

    @GetMapping()
    public String code(){

        return prefix + "/code";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AuthorCode authorCode)
    {
        startPage();
        List<AuthorCode> list = authorCodeService.selectAuthorCodeList(authorCode);
        return getDataTable(list);
    }

    /**
     * 新增邀请码
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存邀请码
     * @param authorCode
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(AuthorCode authorCode)
    {
        if (UserConstants.POST_NAME_NOT_UNIQUE.equals(authorCodeService.checkInviteCodeUnique(authorCode)))
        {
            return error("新增邀请码'" + authorCode.getInviteCode() + "'失败，该邀请码已存在");
        }
        authorCode.setCreateUser(getLoginName());
        authorCode.setCreateUserId(ShiroUtils.getSysUser().getUserId());
        authorCode.setCreateTime(DateUtils.parseDate(DateUtils.getTime()));
        return toAjax(authorCodeService.save(authorCode));
    }

    /**
     * 批量删除邀请码
     * @param ids
     * @return
     */
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(authorCodeService.deleteAuthorCodeByIds(ids));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 校验邀请码内容
     */
    @PostMapping("/checkInviteCodeUnique")
    @ResponseBody
    public String checkInviteCodeUnique(AuthorCode authorCode)
    {
        return authorCodeService.checkInviteCodeUnique(authorCode);
    }

}
