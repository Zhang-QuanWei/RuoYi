package com.ruoyi.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.novel.domain.BookCheck;
import com.ruoyi.novel.service.BookCheckService;
import com.ruoyi.novel.mapper.BookCheckMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 64829
* @description 针对表【book_check】的数据库操作Service实现
* @createDate 2022-03-27 13:53:43
*/
@Service
public class BookCheckServiceImpl extends ServiceImpl<BookCheckMapper, BookCheck>
    implements BookCheckService{

    @Resource
    private BookCheckMapper bookCheckMapper;

    /**
     * 根据ID查询
     * @param bookCheck
     * @return
     */
    @Override
    public BookCheck selectOptionById(BookCheck bookCheck) {

        QueryWrapper<BookCheck> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id",bookCheck.getBookId());
        BookCheck result = bookCheckMapper.selectOne(queryWrapper);

        return result;
    }
}




