package com.ruoyi.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.novel.domain.BookComment;
import com.ruoyi.novel.service.BookCommentService;
import com.ruoyi.novel.mapper.BookCommentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 64829
* @description 针对表【book_comment】的数据库操作Service实现
* @createDate 2022-04-08 22:24:27
*/
@Service
public class BookCommentServiceImpl extends ServiceImpl<BookCommentMapper, BookComment>
    implements BookCommentService{

    @Resource
    private BookCommentMapper bookCommentMapper;

    @Override
    public List<BookComment> selectCommentListByBookId(Long id) {
        QueryWrapper<BookComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id",id);

        List<BookComment> bookCommentList = bookCommentMapper.selectList(queryWrapper);

        return bookCommentList;
    }

}




