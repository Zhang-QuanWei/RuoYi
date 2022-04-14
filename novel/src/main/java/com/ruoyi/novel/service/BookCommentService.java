package com.ruoyi.novel.service;

import com.ruoyi.novel.domain.BookComment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 64829
* @description 针对表【book_comment】的数据库操作Service
* @createDate 2022-04-08 22:24:27
*/
public interface BookCommentService extends IService<BookComment> {

    List<BookComment> selectCommentListByBookId(Long id);

}
