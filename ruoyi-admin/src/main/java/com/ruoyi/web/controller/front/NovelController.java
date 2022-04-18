package com.ruoyi.web.controller.front;

import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.github.houbb.sensitive.word.support.result.WordResultHandlers;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.novel.domain.*;
import com.ruoyi.novel.service.ReportService;
import com.ruoyi.novel.service.impl.*;
import com.ruoyi.system.service.impl.SysUserServiceImpl;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/front/novel")
public class NovelController extends BaseController {

    @Resource
    private BookServiceImpl bookService;

    @Resource
    private BookChapterServiceImpl bookChapterService;

    @Resource
    private ChapterContentServiceImpl chapterContentService;

    @Resource
    private BookCommentServiceImpl bookCommentService;

    @Resource
    private BookShelfServiceImpl bookShelfService;

    @Resource
    private ReportService reportService;

    private String prefix = "/front/novel";

    /**
     * 跳转书籍详情页
     * @param book
     * @param modelMap
     * @return
     */
    @GetMapping()
    public String toNovelInfo(Book book, ModelMap modelMap){
        //book中只有id

        SysUser user = ShiroUtils.getSysUser();
        modelMap.put("user",user);

        //1. 获取书籍对象
        Book result = bookService.selectBookByBookId(book.getId());
        modelMap.put("book",result);

        //2. 查询同类型书籍里点击量最高的10本书
        List<Book> bookList = bookService.selectBookListByBookCategory(result.getBookCategory());
        modelMap.put("bookList",bookList);

        // 增加点击量
        // Long newVisit = book.getVisitCount() + (long) 1;
        //点击数+1
        // book.setVisitCount(newVisit);

        //查询评论集合
        List<BookComment> bookCommentList = bookCommentService.selectCommentListByBookId(book.getId());
        modelMap.put("bookCommentList",bookCommentList);

        //查询最新章节
        Integer MaxChapterIndex = bookChapterService.selectMaxChapterIndexByBookId(book.getId());
        if (StringUtils.isNotNull(MaxChapterIndex)){
            BookChapter bookChapter = bookService.getlastChapter(book.getId(),MaxChapterIndex);
            modelMap.put("lastBookChapter",bookChapter);
        }

        return prefix + "/novelInfo";
    }

    /**
     * 跳转书籍第一章阅读页
     * @param book
     * @param modelMap
     * @return
     */
    @GetMapping("toNovelContent")
    public String toNovelContent(Book book,ModelMap modelMap){
        //查询小说第一章
        BookChapter bookChapter = bookChapterService.selectBookChapter(book.getId(),1);
        //查询第一章的内容
        ChapterContent chapterContent = chapterContentService.selectChapterContentById(bookChapter.getChapterId());
        //查询最大章节索引
        Integer maxIndex = bookChapterService.selectMaxChapterIndexByBookId(book.getId());

        SysUser user = ShiroUtils.getSysUser();
        if (StringUtils.isNotNull(user)){
            modelMap.put("user",user);
        }

        modelMap.put("chapter",bookChapter);
        modelMap.put("content",chapterContent);
        modelMap.put("maxChpaterIndex",maxIndex);

        return prefix + "/novelContent";
    }

    /**
     * 跳转其他章节阅读页
     * @param bookChapter
     * @param modelMap
     * @return
     */
    @GetMapping("toChapter")
    public String toChapter(BookChapter bookChapter,ModelMap modelMap){
        //查询章节
        BookChapter chapter = bookChapterService.selectBookChapter(bookChapter.getBookId(),bookChapter.getChapterIndex());
        //查询章节内容
        ChapterContent content = chapterContentService.selectChapterContentById(chapter.getChapterId());
        //查询最大章节索引
        Integer maxIndex = bookChapterService.selectMaxChapterIndexByBookId(bookChapter.getBookId());

        SysUser user = ShiroUtils.getSysUser();
        if (StringUtils.isNotNull(user)){
            modelMap.put("user",user);
        }

        modelMap.put("chapter",chapter);
        modelMap.put("content",content);
        modelMap.put("maxChpaterIndex",maxIndex);

        return prefix + "/novelContent";
    }

    /**
     * 跳转小说目录页
     * @return
     */
    @GetMapping("toBookChapter")
    public String toBookChapter(Book book,ModelMap modelMap){

        //查询小说信息
        Book result = bookService.selectBookByBookId(book.getId());
        //查询小说全部章节
        List<BookChapter> bookChapters = bookChapterService.selectBookChapterByBookId(book.getId());

        SysUser user = ShiroUtils.getSysUser();
        if (StringUtils.isNotNull(user)){
            modelMap.put("user",user);
        }

        modelMap.put("book",result);
        modelMap.put("bookChapterList",bookChapters);

        return prefix + "/novelChapter";
    }

    /**
     * 添加评论
     * @param bookComment
     * @return
     */
    @PostMapping("/addComment")
    @ResponseBody
    public AjaxResult addComment(BookComment bookComment){

        //评论内容是否包含敏感词
        boolean isSenWords = SensitiveWordHelper.contains(bookComment.getCommentContent());
        //替换敏感词
        if (isSenWords){
            bookComment.setCommentContent(SensitiveWordHelper.replace(bookComment.getCommentContent()));
        }

        bookComment.setCreateTime(DateUtils.parseDate(DateUtils.getTime()));

        boolean result = bookCommentService.save(bookComment);

        if (result){
            return AjaxResult.success("添加成功！");
        }

        return AjaxResult.error("添加失败，请联系管理员！");
    }

    /**
     * 添加书籍到书架
     * @param bookShelf
     * @return
     */
    @PostMapping("/addBookShelf")
    @ResponseBody
    public AjaxResult addBookShelf(BookShelf bookShelf){

        boolean result = bookShelfService.save(bookShelf);

        if (result){
            return AjaxResult.success("添加成功！");
        }

        return AjaxResult.error("添加失败！");
    }

    /**
     * 添加书架索引
     * @param bookChapter
     */
    @PostMapping("/addShelfIndex")
    @ResponseBody
    public AjaxResult addShelfIndex(BookChapter bookChapter){
        SysUser user = ShiroUtils.getSysUser();
        if (StringUtils.isNotNull(user)){
            //1. 判断该本书籍是否被添加到了用户的书架中
            BookShelf bookShelf = bookShelfService.isShelf(user.getUserId(),bookChapter.getBookId());
            if (StringUtils.isNotNull(bookShelf)){
                //2. 为当前阅读页添加索引
                bookShelf.setChapterIndex(bookChapter.getChapterIndex());
                boolean result = bookShelfService.updateById(bookShelf);
                if (result){
                    return AjaxResult.success("索引添加成功！");
                }else {
                    return AjaxResult.error("索引添加失败！");
                }
            }
            return AjaxResult.success("无需添加索引");
        }
        return AjaxResult.success("无需添加索引");
    }

    /**
     * 跳转继续阅读
     * @return
     */
    @GetMapping("/toLastNovelContent")
    public String toLastNovelContent(BookChapter bookChapter,ModelMap modelMap){
        SysUser user = ShiroUtils.getSysUser();
        modelMap.put("user",user);
        BookChapter chapter;

        //1. 查询上次阅读索引
        BookShelf shelf = bookShelfService.isShelf(user.getUserId(), bookChapter.getBookId());
        if (StringUtils.isNotNull(shelf.getChapterIndex())){
            //阅读索引不为空则跳转上次阅读界面
            //查询上次阅读章节
            chapter = bookChapterService.selectBookChapter(bookChapter.getBookId(),shelf.getChapterIndex());
        }else {
            //阅读索引为空则跳转第一章界面
            //查询第一章章节
            chapter = bookChapterService.selectBookChapter(bookChapter.getBookId(),1);
        }


        //查询章节内容
        ChapterContent content = chapterContentService.selectChapterContentById(chapter.getChapterId());
        //查询最大章节索引
        Integer maxIndex = bookChapterService.selectMaxChapterIndexByBookId(bookChapter.getBookId());



        modelMap.put("chapter",chapter);
        modelMap.put("content",content);
        modelMap.put("maxChpaterIndex",maxIndex);

        return prefix + "/novelContent";
    }

    /**
     * 下架作品
     * @param book
     * @return
     */
    @PostMapping("/offShelf")
    @ResponseBody
    public AjaxResult offShelf(Book book){
        //设置当前书籍状态为审核不通过
        Boolean result = bookService.updateBookCheck(book.getId());

        if (result){
            return AjaxResult.success("下架作品成功！");
        }else {
            return AjaxResult.error("下架作品失败！请联系管理员！");
        }

    }

    /**
     * 跳转举报页面
     * @return
     */
    @GetMapping("/toReport")
    public String toReport(Book book,ModelMap modelMap){

        SysUser user = ShiroUtils.getSysUser();
        modelMap.put("user",user);

        //通过id查询到书籍
        Book result = bookService.selectBookByBookId(book.getId());
        modelMap.put("book",result);

        return prefix + "/report";
    }

    /**
     * 前台添加举报
     * @return
     */
    @PostMapping("/addReport")
    @ResponseBody
    public AjaxResult addReport(Report report){

        //设置创建时间
        report.setCreateTime(DateUtils.parseDate(DateUtils.getTime()));

        //设置受理状态
        report.setSolveStatus(0);

        boolean result = reportService.save(report);

        if (result){
            return AjaxResult.success("举报成功！感谢您对净化网络做出的贡献，我们将尽快处理。");
        }

        return AjaxResult.error("举报出错，请联系管理员！");
    }

}
