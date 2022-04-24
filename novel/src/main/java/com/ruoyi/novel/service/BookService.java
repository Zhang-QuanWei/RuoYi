package com.ruoyi.novel.service;

import com.github.pagehelper.PageInfo;
import com.ruoyi.novel.domain.Book;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.novel.vo.SearchDataVo;

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

    /**
     * 表头查询结果按条件显示
     * @param pageNum
     * @param pageSize
     * @param searchDataVo
     * @param bookName
     * @return
     */
    PageInfo<Book> getBookListBySearchItem(Integer pageNum, Integer pageSize, SearchDataVo searchDataVo, String bookName);
}
