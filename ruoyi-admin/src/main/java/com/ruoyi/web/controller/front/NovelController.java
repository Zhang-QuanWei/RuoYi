package com.ruoyi.web.controller.front;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.novel.domain.*;
import com.ruoyi.novel.service.impl.*;
import com.ruoyi.system.service.impl.SysUserServiceImpl;
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

    private String prefix = "/front/novel";

    /**
     * 跳转书籍详情页
     * @param book
     * @param modelMap
     * @return
     */
    @GetMapping()
    public String toNovelInfo(Book book, ModelMap modelMap){

        SysUser user = ShiroUtils.getSysUser();

        if (StringUtils.isNotNull(user)){
            modelMap.put("user",user);
        }else {
            modelMap.put("user",null);
        }

        String lastChapterTitle = "";
        Date lastChapterUpdatetime = null;

        // TODO 前台获取作者头像问题（或许是权限问题）
        Book result = bookService.selectBookByBookId(book.getId());
        // SysUser user = userService.selectUserByLoginName(book.getAuthorName());

        // Long newVisit = book.getVisitCount() + (long) 1;

        //点击数+1
        // book.setVisitCount(newVisit);

        List<BookComment> bookCommentList = bookCommentService.selectCommentListByBookId(book.getId());
        if (StringUtils.isNull(bookCommentList)){
            modelMap.put("bookCommentList",null);
        }else {
            modelMap.put("bookCommentList",bookCommentList);
        }

        //查询最新章节
        Integer MaxChapterIndex = bookChapterService.selectMaxChapterIndexByBookId(book.getId());
        if (StringUtils.isNotNull(MaxChapterIndex)){
            BookChapter bookChapter = bookService.getlastChapter(book.getId(),MaxChapterIndex);
            lastChapterTitle = bookChapter.getChapterTitle();
            lastChapterUpdatetime = bookChapter.getUpdateTime();
        }


        if (StringUtils.isNotNull(result)){
            modelMap.put("book",result);
            modelMap.put("lastChapterTitle",lastChapterTitle);
            modelMap.put("lastChapterUpdatetime",lastChapterUpdatetime);

            // modelMap.put("authorPic",user.getAvatar());
        }else {
            System.out.println("未查询到相关书籍！");
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



}
