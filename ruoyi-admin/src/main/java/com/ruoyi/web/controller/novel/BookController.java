package com.ruoyi.web.controller.novel;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.novel.domain.Book;
import com.ruoyi.novel.service.impl.BookChapterServiceImpl;
import com.ruoyi.novel.service.impl.BookServiceImpl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/novel/book")
public class BookController extends BaseController {

    @Resource
    private BookServiceImpl bookService;

    @Resource
    private BookChapterServiceImpl bookChapterService;

    private String prefix = "/novel/book";

//    @RequiresPermissions("novel:book:view")
    @GetMapping()
    public String book(Model model){

        //获取user
        SysUser user = ShiroUtils.getSysUser();

        model.addAttribute("userId", user.getUserId());

        return prefix + "/book";
    }

    /**
     * 根据作者id查询小说
     * @param book
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Book book){

        //查询当前作者的作品
        SysUser user = ShiroUtils.getSysUser();
        book.setAuthorId(user.getUserId());

        startPage();
        List<Book> books = bookService.selectBookList(book);

        return getDataTable(books);

    }

    /**
     * 获取最新章节信息
     * @param id    小说id
     * @return
     */
    @PostMapping("/getlastChapterTitle")
    @ResponseBody
    public String list(Long id){


        //查询最大章节索引
        Integer MaxChapterIndex = bookChapterService.selectMaxChapterIndexByBookId(id);

        if (StringUtils.isNull(MaxChapterIndex)){
            return null;
        }
        //根据bookID与章节索引查询最新章节标题
        String chapterTitle = bookService.getlastChapterTitle(id,MaxChapterIndex);

        return chapterTitle;

    }

    /**
     * 跳转新增小说页面
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增小说
     * @param book
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated Book book){

        //获得用户id设置为authorId
        SysUser user = ShiroUtils.getSysUser();
        book.setAuthorId(user.getUserId());
        book.setAuthorName(user.getUserName());

        boolean result = bookService.insertNewBook(book);

        return toAjax(result);

    }

    /**
     * 跳转目录页
     * @return
     */
    @RequestMapping("/bookChapter/{id}")
    public String toBookChapter(@PathVariable("id")Long id, Model model) {

        model.addAttribute("bookId",id);

        return "/novel/bookChapter/bookChapter";
    }

    /**
     * 删除小说、目录和章节
     * @param ids
     * @return
     */
    //@RequiresPermissions("system:post:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(bookService.deleteBookByIds(ids));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 跳转修改小说界面
     * @param bookId
     * @param mmap
     * @return
     */
    // @RequiresPermissions("system:post:edit")
    @GetMapping("/edit/{bookId}")
    public String edit(@PathVariable("bookId") Long bookId, ModelMap mmap)
    {

        Book book = bookService.selectBookByBookId(bookId);
        mmap.put("book",book);

        return prefix + "/edit";
    }

    /**
     * 修改小说
     * @param book
     * @return
     */
    // @RequiresPermissions("system:post:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated Book book)
    {

        boolean result = bookService.updateById(book);

        if (result){
            return AjaxResult.success("修改书籍信息成功！");
        }

        return AjaxResult.error("修改书籍信息失败！");
    }



}
