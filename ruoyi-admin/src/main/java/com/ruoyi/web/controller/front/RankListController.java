package com.ruoyi.web.controller.front;

import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.novel.domain.Book;
import com.ruoyi.novel.service.impl.BookChapterServiceImpl;
import com.ruoyi.novel.service.impl.BookServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/front/rankList")
public class RankListController extends BaseController {

    @Resource
    private BookServiceImpl bookService;

    @Resource
    private BookChapterServiceImpl bookChapterService;

    /**
     * 根据条件显示榜单
     * @param queryItem 榜单类型（0：点击榜 1：订阅榜 2：新书榜）
     * @return
     */
    @GetMapping("/list/{id}")
    public String list(Integer queryItem, Integer pageNum, Integer pageSize, ModelMap modelMap){

        //根据条件查询排序后的书籍
        PageInfo<Book> pageInfo = bookService.selectBookListOrderByItem(queryItem,pageNum,pageSize);

        modelMap.put("currentQueryItem",queryItem);
        modelMap.put("pageInfo",pageInfo);

        return "front/index/rankList";
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
}
