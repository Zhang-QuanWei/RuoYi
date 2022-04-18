package com.ruoyi.web.controller.front;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.json.JSON;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.novel.domain.Book;
import com.ruoyi.novel.service.impl.BookServiceImpl;
import com.ruoyi.novel.vo.SearchDataVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/front/allWork")
public class AllWorkController extends BaseController {

    @Resource
    private BookServiceImpl bookService;

    private String prefix = "front/index";


    @GetMapping("/list")
    public String queryBook(SearchDataVo searchDataVo, ModelMap modelMap){
        SysUser user = ShiroUtils.getSysUser();
        modelMap.put("user",user);


        PageInfo<Book> pageInfo = bookService.getBookListByItem(1,10,searchDataVo);
        modelMap.put("pageInfo",pageInfo);
        modelMap.put("searchDataVo", JSONObject.toJSON(searchDataVo));

        return prefix + "/allWork";
    }

}
