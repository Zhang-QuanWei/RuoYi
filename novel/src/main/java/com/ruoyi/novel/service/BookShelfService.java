package com.ruoyi.novel.service;

import com.ruoyi.novel.domain.BookShelf;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 64829
* @description 针对表【book_shelf】的数据库操作Service
* @createDate 2022-04-09 23:53:17
*/
public interface BookShelfService extends IService<BookShelf> {

    Boolean delBookShelf(BookShelf bookShelf);

    /**
     * 判断书籍是否在用户书架中
     * @param userId
     * @param bookId
     * @return
     */
    BookShelf isShelf(Long userId, Long bookId);
}
