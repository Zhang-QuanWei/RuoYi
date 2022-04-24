package com.ruoyi.web.controller.novel;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.novel.domain.Book;
import com.ruoyi.novel.domain.BookChapter;
import com.ruoyi.novel.domain.ChapterContent;
import com.ruoyi.novel.service.impl.BookChapterServiceImpl;
import com.ruoyi.novel.service.impl.BookServiceImpl;
import com.ruoyi.novel.service.impl.ChapterContentServiceImpl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/novel/bookChapter")
public class bookChapterController extends BaseController {

    @Resource
    private BookChapterServiceImpl bookChapterService;

    @Resource
    private ChapterContentServiceImpl chapterContentService;

    @Resource
    private BookServiceImpl bookService;

    private String prefix = "/novel/bookChapter";

    /**
     * 根据小说id查询章节信息
     * @param bookChapter
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BookChapter bookChapter){


        startPage();
        // 查询小说作品的所有章节信息
        List<BookChapter> bookChapterList = bookChapterService.selectBookChapterList(bookChapter);

        return getDataTable(bookChapterList);
    }

    /**
     * 跳转新增章节页面
     */
    @GetMapping("/add/{id}")
    public String add(@PathVariable("id")Long id,Model model)
    {
        model.addAttribute("bookId",id);

        return prefix + "/add";
    }

    /**
     * 新增章节
     * @param bookChapter
     * @return
     */
    //@RequiresPermissions("system:post:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult add(@Validated BookChapter bookChapter){

        //查询当前小说下的章节数目
        Integer count = bookChapterService.selectChapterCountByBookId(bookChapter.getBookId());
        if (count == 0){
            bookChapter.setChapterIndex(count+1);
        }else {
            //防止删除中间章节导致的目录索引出错问题
            Integer index = bookChapterService.selectMaxChapterIndexByBookId(bookChapter.getBookId());
            bookChapter.setChapterIndex(index+1);
        }

        //设置更新时间
        bookChapter.setUpdateTime(DateUtils.parseDate(DateUtils.getTime()));

        //将信息插入目录表
        boolean result = bookChapterService.save(bookChapter);

        if (result){
            //将当前章节信息更新到对应小说的最新章节字段
            //1. 查询该本小说
            Book book = bookService.selectBookByBookId(bookChapter.getBookId());
            if (StringUtils.isNotNull(book)){
                //2. 设置小说最新章节信息
                book.setLastChapterId(bookChapter.getChapterId());
                book.setLastChapterTitle(bookChapter.getChapterTitle());
                //3. 更新该部小说
                bookService.updateById(book);
            }

            return AjaxResult.success("添加章节信息成功！请刷新目录页面显示");
        }
        return AjaxResult.error("添加章节信息失败！请联系管理员");
    }

    /**
     * 新增章节内容
     * @param chapterContent
     * @return
     */
    //@RequiresPermissions("system:post:add")
    @PostMapping("/addContent")
    @ResponseBody
    public AjaxResult addContent(@Validated ChapterContent chapterContent){

        //章节内容是否包含敏感词
        boolean isSenWords = SensitiveWordHelper.contains(chapterContent.getChapterContent());
        //返回所有敏感词
        List<String> wordList = SensitiveWordHelper.findAll(chapterContent.getChapterContent());
        if (isSenWords){
            return AjaxResult.error("章节内容包含敏感词："+wordList.toString());
        }

        ChapterContent data = chapterContentService.insertNewChapter(chapterContent);

        if (StringUtils.isNull(data)){
            AjaxResult.error("插入章节内容信息失败！请重试！");
        }
        //返回状态码与chapterContentid数据
        return AjaxResult.success(data);
    }

    /**
     * 删除目录与章节
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
            return toAjax(bookChapterService.deleteBookChapterByIds(ids));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 跳转修改章节页面
     */
    //@RequiresPermissions("system:post:edit")
    @GetMapping("/edit/{chapterId}")
    public String edit(@PathVariable("chapterId") Long chapterId, ModelMap mmap)
    {
        BookChapter bookChapter = bookChapterService.selectBookChapterByChapterId(chapterId);
        mmap.put("chapterTitle",bookChapter.getChapterTitle());
        ChapterContent chapterContent = chapterContentService.selectChapterContentById(chapterId);
        mmap.put("chapterContent",chapterContent.getChapterContent());
        mmap.put("chapterId",chapterId);

        return prefix + "/edit";
    }

    /**
     * 修改目录信息
     * @param bookChapter
     * @return
     */
    //@RequiresPermissions("system:post:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated BookChapter bookChapter){
        //更新时间
        bookChapter.setUpdateTime(DateUtils.parseDate(DateUtils.getTime()));
        //更新目录表
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id",bookChapter.getChapterId());
        boolean result = bookChapterService.update(bookChapter, queryWrapper);

        if (result){
            return AjaxResult.success();
        }else {
            return AjaxResult.error();
        }
    }

    /**
     * 修改章节信息
     * @param chapterContent
     * @return
     */
    //@RequiresPermissions("system:post:edit")
    @PostMapping("/editContent")
    @ResponseBody
    public AjaxResult editSaveContent(ChapterContent chapterContent){

        //更新内容表
        boolean result = chapterContentService.updateById(chapterContent);

        if (result){
            return AjaxResult.success();
        }else {
            return AjaxResult.error();
        }
    }




}
