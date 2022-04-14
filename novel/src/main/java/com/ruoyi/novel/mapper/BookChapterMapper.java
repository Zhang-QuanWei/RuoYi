package com.ruoyi.novel.mapper;
import org.apache.ibatis.annotations.Param;

import com.ruoyi.novel.domain.BookChapter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 64829
* @description 针对表【book_chapter】的数据库操作Mapper
* @createDate 2022-03-15 00:56:31
* @Entity com.ruoyi.novel.domain.BookChapter
*/
public interface BookChapterMapper extends BaseMapper<BookChapter> {

    List<BookChapter> selectBookChapterList(BookChapter bookChapter);

    Integer selectMaxChapterIndexByBookId(Long bookId);
}




