package com.ruoyi.web.controller.novel;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.novel.domain.BookChapter;
import com.ruoyi.novel.domain.ChapterContent;
import com.ruoyi.novel.service.impl.BookChapterServiceImpl;
import com.ruoyi.novel.service.impl.BookServiceImpl;
import com.ruoyi.novel.service.impl.ChapterContentServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/novel/bookChapterCheck")
public class bookChapterCheckController extends BaseController {

    @Resource
    private BookChapterServiceImpl bookChapterService;

    @Resource
    private ChapterContentServiceImpl chapterContentService;

    @Resource
    private BookServiceImpl bookService;

    private String prefix = "/novel/bookChapterCheck";

    /**
     * 查询待审核目录
     * @param bookChapter
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BookChapter bookChapter){
        //设置未审核条件
        // bookChapter.setCheckStatus(0);

        startPage();
        // 查询小说作品的所有章节信息
        List<BookChapter> bookChapterList = bookChapterService.selectBookChapterList(bookChapter);

        return getDataTable(bookChapterList);

    }

    /**
     * 查询待审核章节
     * @param chapterId
     * @param mmap
     * @return
     */
    @GetMapping("/listContent/{chapterId}")
    public String listContent(@PathVariable("chapterId") Long chapterId, ModelMap mmap)
    {
        BookChapter bookChapter = bookChapterService.selectBookChapterByChapterId(chapterId);
        mmap.put("chapterTitle",bookChapter.getChapterTitle());
        ChapterContent chapterContent = chapterContentService.selectChapterContentById(chapterId);
        mmap.put("chapterContent",chapterContent.getChapterContent());
        mmap.put("chapterId",chapterId);

        return "novel/check/chapterContentCheck";
    }

    /**
     * 提交审核结果
     * @param bookChapter
     * @return
     */
    @PostMapping("/check")
    @ResponseBody
    public AjaxResult check(BookChapter bookChapter){

        Integer result = bookChapterService.checkOne(bookChapter);

        return AjaxResult.success("审核结果提交成功！");
    }



}
