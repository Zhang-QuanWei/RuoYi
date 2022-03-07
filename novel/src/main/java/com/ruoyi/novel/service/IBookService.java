package com.ruoyi.novel.service;

import java.util.List;
import com.ruoyi.novel.domain.Book;

/**
 * 小说Service接口
 * 
 * @author zqw
 * @date 2022-03-06
 */
public interface IBookService 
{
    /**
     * 查询小说
     * 
     * @param id 小说主键
     * @return 小说
     */
    public Book selectBookById(Long id);

    /**
     * 查询小说列表
     * 
     * @param book 小说
     * @return 小说集合
     */
    public List<Book> selectBookList(Book book);

    /**
     * 新增小说
     * 
     * @param book 小说
     * @return 结果
     */
    public int insertBook(Book book);

    /**
     * 修改小说
     * 
     * @param book 小说
     * @return 结果
     */
    public int updateBook(Book book);

    /**
     * 批量删除小说
     * 
     * @param ids 需要删除的小说主键集合
     * @return 结果
     */
    public int deleteBookByIds(String ids);

    /**
     * 删除小说信息
     * 
     * @param id 小说主键
     * @return 结果
     */
    public int deleteBookById(Long id);
}
