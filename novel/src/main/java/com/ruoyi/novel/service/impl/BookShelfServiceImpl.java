package com.ruoyi.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.novel.domain.Book;
import com.ruoyi.novel.domain.BookShelf;
import com.ruoyi.novel.mapper.BookMapper;
import com.ruoyi.novel.service.BookShelfService;
import com.ruoyi.novel.mapper.BookShelfMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* @author 64829
* @description 针对表【book_shelf】的数据库操作Service实现
* @createDate 2022-04-09 23:53:17
*/
@Service
public class BookShelfServiceImpl extends ServiceImpl<BookShelfMapper, BookShelf>
    implements BookShelfService{

    @Resource
    private BookShelfMapper bookShelfMapper;

    @Resource
    private BookMapper bookMapper;

    /**
     * 根据用户ID查询书架书籍列表
     * @param userId
     * @return
     */
    public List<Book> selectBookList(Long userId) {



        QueryWrapper<BookShelf> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("book_id").eq("user_id",userId);
        List<Object> bookIds = bookShelfMapper.selectObjs(queryWrapper);

        if (bookIds.isEmpty()){
            return null;
        }else {
            // Object集合转换为Long集合
            List<Long> longList = new ArrayList<>();
            for(Object bookId : bookIds){
                Long id = Long.valueOf(String.valueOf(bookId));
                longList.add(id);

            }

            List<Book> bookList = bookMapper.selectBatchIds(longList);
            return bookList;
        }
    }

    /**
     * 删除书架书籍
     * @param bookShelf
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Boolean delBookShelf(BookShelf bookShelf) {

        QueryWrapper<BookShelf> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id",bookShelf.getBookId()).eq("user_id",bookShelf.getUserId());
        int rows = bookShelfMapper.delete(queryWrapper);

        if (rows != 1){
            return false;
        }

        return true;
    }
}




