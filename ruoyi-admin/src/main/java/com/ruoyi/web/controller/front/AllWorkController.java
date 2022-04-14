package com.ruoyi.web.controller.front;

import com.ruoyi.novel.domain.Book;
import com.ruoyi.novel.service.impl.BookServiceImpl;
import com.ruoyi.novel.vo.SearchDataVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/front/allWork")
public class AllWorkController {

    @Resource
    private BookServiceImpl bookService;

    private String prefix = "front/index";

    //TODO 全部作品页的页面逻辑需要完善
    @PostMapping("/list")
    public String queryBook(SearchDataVo searchDataVo){
        Book book = new Book();
        book.setBookCategory(searchDataVo.getBookCategory());
        book.setBookStatus(searchDataVo.getBookStatus());

        bookService.selectBookList(book);

        return prefix + "/allWork";
    }

}
