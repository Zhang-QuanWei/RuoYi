package com.ruoyi.web.controller.novel;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.novel.domain.Book;
import com.ruoyi.novel.service.impl.BookServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 编辑小说管理
 */
@Controller
@RequestMapping("/novel/bookManage")
public class BookManageController extends BaseController {

    @Resource
    private BookServiceImpl bookService;

    private String prefix = "/novel/bookManage";

    /**
     * 跳转小说管理界面
     * @param model
     * @return
     */
    @GetMapping()
    public String book(Model model){

        //获取user
        SysUser user = ShiroUtils.getSysUser();

        model.addAttribute("userId", user.getUserId());

        return prefix + "/book";
    }

    /**
     * 查询审核通过已上架的小说
     * @param book
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Book book){

        //构建审核通过条件
        book.setCheckStatus(1);

        //查询所有作品
        startPage();
        List<Book> books = bookService.selectBookList(book);

        return getDataTable(books);

    }

    /**
     * 跳转目录页
     * @return
     */
    @RequestMapping("/bookChapter/{id}")
    public String toBookChapter(@PathVariable("id")Long id, Model model) {

        model.addAttribute("bookId",id);

        return "/novel/bookManage/bookChapter";
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
}
