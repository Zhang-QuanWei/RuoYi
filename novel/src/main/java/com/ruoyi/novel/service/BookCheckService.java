package com.ruoyi.novel.service;

import com.ruoyi.novel.domain.BookCheck;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 64829
* @description 针对表【book_check】的数据库操作Service
* @createDate 2022-03-27 13:53:43
*/
public interface BookCheckService extends IService<BookCheck> {

    /**
     * 根据ID查询记录
     * @param bookCheck
     * @return
     */
    BookCheck selectOptionById(BookCheck bookCheck);
}
