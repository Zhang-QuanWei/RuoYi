package com.ruoyi.web.controller.novel;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.novel.domain.Book;
import com.ruoyi.novel.domain.BookCheck;
import com.ruoyi.novel.service.impl.BookCheckServiceImpl;
import com.ruoyi.novel.service.impl.BookServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/novel/bookCheck")
public class bookCheckController extends BaseController {

    @Resource
    private BookServiceImpl bookService;

    @Resource
    private BookCheckServiceImpl bookCheckService;

    private String prefix = "/novel/check";

    @GetMapping()
    public String list(){
        return prefix + "/bookCheck";
    }

    /**
     * 查询待审核小说
     * @param book
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Book book){

        startPage();
        List<Book> books = bookService.selectCheckBook(book);

        return getDataTable(books);
    }

    /**
     * 跳转目录审核页
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/bookChapterCheck/{id}")
    public String toBookChapter(@PathVariable("id")Long id, Model model) {

        model.addAttribute("bookId",id);

        return "/novel/check/bookChapterCheck";
    }

    /**
     * 跳转审核不通过意见页面
     * @return
     */
    @GetMapping("/form")
    public String form(BookCheck bookCheck, ModelMap modelMap){
        modelMap.put("bookId",bookCheck.getBookId());
        modelMap.put("bookName",bookCheck.getBookName());
        return prefix + "/checkReason";
    }

    /**
     * 修改审核信息为审核通过
     * @return
     */
    @PostMapping("/check")
    @ResponseBody
    public AjaxResult check(Book book){
        boolean result = bookService.updateById(book);
        if (result){
            return AjaxResult.success("提交成功！");
        }
        return AjaxResult.error("提交失败，请联系管理员！");
    }

    /**
     * 提交审核意见
     * @return
     */
    @PostMapping("/checkOpinion")
    @ResponseBody
    public AjaxResult checkOpinion(BookCheck bookCheck){
        SysUser sysUser = ShiroUtils.getSysUser();
        bookCheck.setEditorId(sysUser.getUserId());
        bookCheck.setEditorName(sysUser.getUserName());
        Boolean result = false;

        //修改审核状态为不通过
        //1. 根据小说ID搜索到此小说
        Book book = bookService.selectBookByBookId(bookCheck.getBookId());
        book.setCheckStatus(2);
        //2. 修改该小说的审核状态
        boolean checkResult = bookService.updateById(book);

        //增添或更新审核记录
        //3. 根据bookID查询是否已经拥有审核记录
        BookCheck info = bookCheckService.selectOptionById(bookCheck);

        if (StringUtils.isNotNull(info)){
            //更新审核记录
            info.setCheckOpinion(bookCheck.getCheckOpinion());
            result = bookCheckService.updateById(info);
        }else {
            //插入审核记录
            result = bookCheckService.save(bookCheck);
        }


        if (checkResult && result){
            return AjaxResult.success("提交信息成功！");
        }

        return AjaxResult.error("提交信息失败，请联系管理员！");

    }
}
