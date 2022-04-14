package com.ruoyi.novel.service;

import com.ruoyi.novel.domain.Book;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 64829
* @description 针对表【book】的数据库操作Service
* @createDate 2022-03-15 00:56:31
*/
public interface BookService extends IService<Book> {

    /**
     * 查询点击量最高的5本书籍
     * @param book
     * @return
     */
    List<Book> selectNewList(Book book);
}
