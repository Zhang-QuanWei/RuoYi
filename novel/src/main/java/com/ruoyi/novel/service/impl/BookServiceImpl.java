package com.ruoyi.novel.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.novel.mapper.BookMapper;
import com.ruoyi.novel.domain.Book;
import com.ruoyi.novel.service.IBookService;
import com.ruoyi.common.core.text.Convert;

/**
 * 小说Service业务层处理
 * 
 * @author zqw
 * @date 2022-03-06
 */
@Service
public class BookServiceImpl implements IBookService 
{
    @Autowired
    private BookMapper bookMapper;

    /**
     * 查询小说
     * 
     * @param id 小说主键
     * @return 小说
     */
    @Override
    public Book selectBookById(Long id)
    {
        return bookMapper.selectBookById(id);
    }

    /**
     * 查询小说列表
     * 
     * @param book 小说
     * @return 小说
     */
    @Override
    public List<Book> selectBookList(Book book)
    {
        return bookMapper.selectBookList(book);
    }

    /**
     * 新增小说
     * 
     * @param book 小说
     * @return 结果
     */
    @Override
    public int insertBook(Book book)
    {
        //默认封面

        //作者有话说
        if (StringUtils.isEmpty(book.getAuthorSpeak())){
            book.setAuthorSpeak("亲亲们，你们的支持是我最大的动力！求点击、求推荐、求书评哦！");
        }
        //创建时间
        book.setCteateTime(DateUtils.parseDate(DateUtils.getTime()));
        //更新时间
        book.setUpdateTime(DateUtils.parseDate(DateUtils.getTime()));
        return bookMapper.insertBook(book);
    }

    /**
     * 修改小说
     * 
     * @param book 小说
     * @return 结果
     */
    @Override
    public int updateBook(Book book)
    {
        book.setUpdateTime(DateUtils.getNowDate());
        return bookMapper.updateBook(book);
    }

    /**
     * 批量删除小说
     * 
     * @param ids 需要删除的小说主键
     * @return 结果
     */
    @Override
    public int deleteBookByIds(String ids)
    {
        return bookMapper.deleteBookByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除小说信息
     * 
     * @param id 小说主键
     * @return 结果
     */
    @Override
    public int deleteBookById(Long id)
    {
        return bookMapper.deleteBookById(id);
    }
}
