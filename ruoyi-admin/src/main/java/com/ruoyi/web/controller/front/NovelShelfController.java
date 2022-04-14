package com.ruoyi.web.controller.front;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.novel.domain.BookShelf;
import com.ruoyi.novel.service.impl.BookShelfServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/front/bookShelf")
public class NovelShelfController extends BaseController {

    @Resource
    private BookShelfServiceImpl bookShelfService;

    @PostMapping("/delete")
    @ResponseBody
    public AjaxResult delBookShelf(BookShelf bookShelf){

        Boolean result = bookShelfService.delBookShelf(bookShelf);

        if (result){
            return AjaxResult.success("移除书籍成功！");
        }

        return AjaxResult.error("移除书籍失败！");
    }


}
