package com.ruoyi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.novel.domain.Book;
import com.ruoyi.novel.domain.BookChapter;
import com.ruoyi.novel.mapper.BookChapterMapper;
import com.ruoyi.novel.mapper.BookMapper;
import com.ruoyi.novel.service.impl.BookChapterServiceImpl;
import com.ruoyi.novel.service.impl.BookServiceImpl;
import com.ruoyi.novel.service.impl.BookShelfServiceImpl;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.mapper.SysNoticeMapper;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class Test {

    @Resource
    BookMapper bookMapper;

    @Resource
    BookChapterMapper bookChapterMapper;

    @Resource
    BookChapterServiceImpl bookChapterService;

    @Resource
    BookServiceImpl bookService;

    @Resource
    BookShelfServiceImpl bookShelfService;

    @Resource
    SysNoticeMapper noticeMapper;

    @org.junit.jupiter.api.Test
    public void test01(){

        //Long id = 1L;
        //Integer index = bookChapterMapper.selectMaxChapterIndexByBookId(id);
        //System.out.println("结果为"+index);

        //BookChapter bookChapter = new BookChapter();
        //bookChapter.setChapterId(1504698946833707010L);
        //bookChapter.setChapterTitle("第二章 这回换了");
        //bookChapter.setChapterWord(27);
        //
        ////更新目录表
        //QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        //queryWrapper.eq("chapter_id",bookChapter.getChapterId());
        ////boolean result = bookChapterService.update(queryWrapper);
        ////boolean rows = bookChapterService.updateByChapterId(bookChapter);
        //boolean result = bookChapterService.update(bookChapter, queryWrapper);
        //System.out.println(result);


        // BookChapter bookChapter = new BookChapter();
        // bookChapter.setBookId(1504411930543812610L);
        //
        // QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        // queryWrapper.select("chapter_id").eq("book_id",bookChapter.getBookId());
        // List<Object> chapterIdList = bookChapterMapper.selectObjs(queryWrapper);
        //
        // chapterIdList.forEach(System.out::println);

        // Book book = new Book();
        // book.setBookStatus(0);
        //
        // List<Book> bookList = bookService.selectCheckBook(book);
        //
        // bookList.forEach(System.out::println);

        // Long id = 1L;
        // List<Book> bookList = bookShelfService.selectBookList(id);
        // bookList.forEach(System.out::println);

        SysNotice notice = null;

        List<SysNotice> noticeList = noticeMapper.selectListOrderCreateTime(null);

        noticeList.forEach(System.out::println);
    }
}

