package com.ruoyi.web.controller.novel;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.novel.domain.Book;
import com.ruoyi.novel.service.IBookService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 小说Controller
 * 
 * @author zqw
 * @date 2022-03-06
 */
@Controller
@RequestMapping("/novel/book")
public class BookController extends BaseController
{
    private String prefix = "novel/book";

    @Autowired
    private IBookService bookService;

    @RequiresPermissions("novel:book:view")
    @GetMapping()
    public String book()
    {
        return prefix + "/book";
    }

    /**
     * 查询小说列表
     */
    @RequiresPermissions("novel:book:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Book book)
    {
        startPage();
        List<Book> list = bookService.selectBookList(book);
        return getDataTable(list);
    }

    /**
     * 导出小说列表
     */
    @RequiresPermissions("novel:book:export")
    @Log(title = "小说", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Book book)
    {
        List<Book> list = bookService.selectBookList(book);
        ExcelUtil<Book> util = new ExcelUtil<Book>(Book.class);
        return util.exportExcel(list, "小说数据");
    }

    /**
     * 新增小说
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存小说
     */
    @RequiresPermissions("novel:book:add")
    @Log(title = "小说", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Book book)
    {
        //获取当前作者id
        SysUser user = getSysUser();
        book.setAuthorId(user.getUserId());
        return toAjax(bookService.insertBook(book));
    }

    /**
     * 修改小说
     */
    @RequiresPermissions("novel:book:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Book book = bookService.selectBookById(id);
        mmap.put("book", book);
        return prefix + "/edit";
    }

    /**
     * 修改保存小说
     */
    @RequiresPermissions("novel:book:edit")
    @Log(title = "小说", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Book book)
    {
        return toAjax(bookService.updateBook(book));
    }

    /**
     * 删除小说
     */
    @RequiresPermissions("novel:book:remove")
    @Log(title = "小说", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(bookService.deleteBookByIds(ids));
    }
}
