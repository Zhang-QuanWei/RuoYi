package com.ruoyi.novel.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.JsoupUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.novel.domain.Book;
import com.ruoyi.novel.domain.BookChapter;
import com.ruoyi.novel.service.SpiderService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Slf4j
@Service
public class SpiderServiceImpl implements SpiderService {

    @Resource
    private BookServiceImpl bookService;

    @Resource
    private BookChapterServiceImpl bookChapterService;

    @Resource
    private ChapterContentServiceImpl chapterContentService;

    

    /**
     * 抓取单本小说并保存到数据库
     * @param url
     * @return
     */
    @Override
    public boolean insertOneBook(String url) {
        //爬取小说基本信息
        //1. 获取当前页面的DOM对象
        Document document = JsoupUtils.getDoc(url);
        //2. 获取小说信息
        Book book = getOneBook(document);
        Boolean flag = bookService.selectBookByBookName(book.getBookName());
        if (!flag){
            //3. 增添小说到数据库,返回目录ID
            Long bookId = bookService.spiderInsertOneBook(book);
            //4. 获取目录信息
            Elements chapterElems = document.select("dd>a");
            //5. 目录信息与章节信息入库
            for (int i = 12; i < chapterElems.size(); i++){
                BookChapter bookChapter = getBookChapter(chapterElems,i,bookId,url,document);
                Boolean result = bookChapterService.save(bookChapter);
                if (!result){
                    throw new ServiceException("当前小说目录信息入库失败！");
                }
            }
        }else {
            log.debug("已存在同名小说！");
            return false;
        }

        return true;
    }

    /**
     * 抓取多本小说并保存到数据库
     * @param url
     * @return
     */
    @Override
    public boolean insertBooks(String url) {

        //1. 获取当前页面的DOM对象
        Document document = JsoupUtils.getDoc(url);
        //2. 获取小说页面链接集合
        List<String> bookUrls = getBookUrl(document);
        //3. 批量抓取单本小说
        for (String bookUrl : bookUrls){
            boolean result = insertOneBook(bookUrl);
            if (!result){
                // throw new ServiceException("小说入库失败！");
                System.out.println("小说:"+bookUrl+"入库失败！");
                continue;
            }
        }

        return true;
    }

    /**
     * 获取单本小说页面链接
     * @param document
     * @return
     */
    private List<String> getBookUrl(Document document) {

        List<String> urls = new ArrayList<>();

        Elements elements = document.select("span.s2 a");

        for (Element element : elements){
            String bookUrl = element.attr("href");
            urls.add(bookUrl);
        }

        return urls;

    }

    /**
     * 根据DOM对象获取小说目录信息
     *
     * @param chapterElems 章节dom信息
     * @param index 章节dom索引
     * @param bookId 小说ID
     * @param url 小说页面url
     * @param document 小说dom
     * @return
     */
    private BookChapter getBookChapter(Elements chapterElems, int index, Long bookId, String url, Document document) {

        BookChapter bookChapter = new BookChapter();
        bookChapter.setBookId(bookId);

        String chapterTitle = chapterElems.get(index).text();
        String href = chapterElems.get(index).attr("href");
        //切割章节url
        href = JsoupUtils.sunStringHref(url,href);

        try {
            //章节信息入库
            document = Jsoup.connect(href).get();
            String contentHtml = document.select("div#content").html();
            Long chapterId = chapterContentService.spiderOneContent(contentHtml);
            bookChapter.setChapterId(chapterId);
            bookChapter.setChapterWord(contentHtml.length());

        } catch (IOException e) {
            e.printStackTrace();
        }
        bookChapter.setChapterTitle(chapterTitle);
        bookChapter.setChapterIndex(index-11);
        bookChapter.setUpdateTime(DateUtils.parseDate(DateUtils.getTime()));
        bookChapter.setCheckStatus(1);

        return bookChapter;

    }

    /**
     * 根据DOM对象获取单本小说的信息
     * @param document
     * @return
     */
    private Book getOneBook(Document document) {

        Book book = new Book();

        Elements info = document.select("div.info");
        Elements info_detail = info.select("div.small");
        Elements intro = document.select("div.intro");

        //封面URl
        String picUrl = info.select("img").attr("src");
        book.setPicUrl(picUrl);
        //小说标题
        String bookName = info.select("h2").text();
        book.setBookName(bookName);
        //小说作者
        String authorName = JsoupUtils.subStrStart(info_detail.select("span").get(0).text());
        book.setAuthorName(authorName);
        //小说分类
        String bookCategory = JsoupUtils.subStrStart(info_detail.select("span").get(1).text());
        if (StringUtils.equals(bookCategory,"玄幻魔法")){
            book.setBookCategory("0");
        } else if (StringUtils.equals(bookCategory,"武侠修真")){
            book.setBookCategory("1");
        } else if (StringUtils.equals(bookCategory,"历史军事")){
            book.setBookCategory("2");
        } else if (StringUtils.equals(bookCategory,"都市言情")){
            book.setBookCategory("3");
        } else if (StringUtils.equals(bookCategory,"科幻灵异")){
            book.setBookCategory("4");
        } else if (StringUtils.equals(bookCategory,"侦探推理")){
            book.setBookCategory("5");
        } else if (StringUtils.equals(bookCategory,"网游动漫")){
            book.setBookCategory("6");
        } else if (StringUtils.equals(bookCategory,"其他类型")){
            book.setBookCategory("7");
        }
        //小说状态
        String bookStatus = JsoupUtils.subStrStart(info_detail.select("span").get(2).text());
        if (StringUtils.equals(bookStatus,"连载中")){
            book.setBookStatus(0);
        }else if (StringUtils.equals(bookStatus,"已完结")){
            book.setBookStatus(1);
        }
        //小说字数
        Integer bookWord = Integer.parseInt(JsoupUtils.subStrStart(info_detail.select("span").get(3).text()));
        book.setBookWord(bookWord);
        //小说更新时间
        Date updateTime = DateUtils.parseDate(JsoupUtils.subStrStart(info_detail.select("span").get(4).text()));
        book.setUpdateTime(updateTime);
        //小说最新章节

        //小说简介
        String bookIntro = JsoupUtils.subStrFull(intro.get(0).text());
        book.setBookIntro(bookIntro);

        return book;
    }
}
