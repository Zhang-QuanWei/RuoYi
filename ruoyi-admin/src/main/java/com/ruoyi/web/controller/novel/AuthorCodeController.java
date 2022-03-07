package com.ruoyi.web.controller.novel;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.novel.domain.AuthorCode;
import com.ruoyi.novel.service.IAuthorCodeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 邀请码Controller
 * 
 * @author zqw
 * @date 2022-03-06
 */
@Controller
@RequestMapping("/novel/code")
public class AuthorCodeController extends BaseController
{
    private String prefix = "novel/code";

    @Autowired
    private IAuthorCodeService authorCodeService;

    @RequiresPermissions("novel:code:view")
    @GetMapping()
    public String code()
    {
        return prefix + "/code";
    }

    /**
     * 查询邀请码列表
     */
    @RequiresPermissions("novel:code:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AuthorCode authorCode)
    {
        startPage();
        List<AuthorCode> list = authorCodeService.selectAuthorCodeList(authorCode);
        return getDataTable(list);
    }

    /**
     * 导出邀请码列表
     */
    @RequiresPermissions("novel:code:export")
    @Log(title = "邀请码", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(AuthorCode authorCode)
    {
        List<AuthorCode> list = authorCodeService.selectAuthorCodeList(authorCode);
        ExcelUtil<AuthorCode> util = new ExcelUtil<AuthorCode>(AuthorCode.class);
        return util.exportExcel(list, "邀请码数据");
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
     */
    @RequiresPermissions("novel:code:add")
    @Log(title = "邀请码", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(AuthorCode authorCode)
    {
        SysUser user = getSysUser();
        authorCode.setCreateUserName(user.getUserName());
        return toAjax(authorCodeService.insertAuthorCode(authorCode));
    }

    /**
     * 修改邀请码
     */
    @RequiresPermissions("novel:code:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        AuthorCode authorCode = authorCodeService.selectAuthorCodeById(id);
        mmap.put("authorCode", authorCode);
        return prefix + "/edit";
    }

    /**
     * 修改保存邀请码
     */
    @RequiresPermissions("novel:code:edit")
    @Log(title = "邀请码", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(AuthorCode authorCode)
    {
        return toAjax(authorCodeService.updateAuthorCode(authorCode));
    }

    /**
     * 删除邀请码
     */
    @RequiresPermissions("novel:code:remove")
    @Log(title = "邀请码", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(authorCodeService.deleteAuthorCodeByIds(ids));
    }
}
