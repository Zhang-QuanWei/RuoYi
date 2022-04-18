package com.ruoyi.novel.mapper;

import com.ruoyi.novel.domain.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.novel.vo.SearchDataVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 64829
* @description 针对表【book】的数据库操作Mapper
* @createDate 2022-03-15 00:56:31
* @Entity com.ruoyi.novel.domain.Book
*/
public interface BookMapper extends BaseMapper<Book> {

    List<Book> selectBookList(Book book);


}




