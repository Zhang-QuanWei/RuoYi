package com.ruoyi.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.novel.domain.Book;
import com.ruoyi.novel.domain.BookChapter;
import com.ruoyi.novel.domain.BookComment;
import com.ruoyi.novel.mapper.BookChapterMapper;
import com.ruoyi.novel.mapper.BookCommentMapper;
import com.ruoyi.novel.mapper.ChapterContentMapper;
import com.ruoyi.novel.service.BookService;
import com.ruoyi.novel.mapper.BookMapper;
import com.ruoyi.novel.vo.SearchDataVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* @author 64829
* @description 针对表【book】的数据库操作Service实现
* @createDate 2022-03-15 00:56:31
*/
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book>
    implements BookService{

    @Resource
    private BookMapper bookMapper;

    @Resource
    private BookChapterMapper bookChapterMapper;

    @Resource
    private ChapterContentMapper chapterContentMapper;

    /**
     * 查询所有小说信息（可附带条件）
     * @param book
     * @return
     */
    public List<Book> selectBookList(Book book){

        List<Book> bookList = bookMapper.selectBookList(book);

        return bookList;
    }

    /**
     * 添加新小说
     * @param book
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)  //增删改加事务注解
    public boolean insertNewBook(Book book) {
        if (StringUtils.isEmpty(book.getAuthorSpeak())){
            book.setAuthorSpeak("新作出品，希望大家支持欧！！！");
        }

        //设置创建时间
        book.setCreateTime(DateUtils.parseDate(DateUtils.getTime()));
        //设置更新时间
        book.setUpdateTime(DateUtils.parseDate(DateUtils.getTime()));

        //设置默认封面
        if (StringUtils.isEmpty(book.getPicUrl())){
            book.setPicUrl("/profile/upload/2022/03/16/default-cover_20220316154010A001.png");
        }

        int rows = bookMapper.insert(book);

        if (rows != 1){
            //抛出增加小说异常
            System.out.println("增加小说异常！");
            return false;
        }

        return true;
    }

    /**
     * 通过bookId查询book
     * @param bookId
     * @return
     */
    public Book selectBookByBookId(Long bookId) {

        Book book = bookMapper.selectById(bookId);

        return book;

    }

    /**
     * 根据小说id与最大章节索引查询当前小说的最新章节标题
     * @param id
     * @param maxChapterIndex
     * @return
     */
    public String getlastChapterTitle(Long id, Integer maxChapterIndex) {
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id",id).eq("chapter_index",maxChapterIndex);
        BookChapter bookChapter = bookChapterMapper.selectOne(queryWrapper);
        return bookChapter.getChapterTitle();
    }

    /**
     * 通过id删除小说
     * @param ids
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)  //增删改加事务注解
    public int deleteBookByIds(String ids) {

        Long[] bookIds = Convert.toLongArray(ids);
        int bookCount = 0;  //记录删除的书本数量

        for (Long bookId : bookIds)
        {
            //1. 根据bookId找到chapterID集合，进而删除该本书所有章节
            QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("chapter_id").eq("book_id",bookId);
            List<Object> chapterIdList = bookChapterMapper.selectObjs(queryWrapper);
            //将List<Object>转为List<Long>
            List<Long> list = new ArrayList<>();
            for (Object id : chapterIdList) {
                Long lid = Long.valueOf(String.valueOf(id));
                list.add(lid);
            }
            System.out.println(list);
            //若当前书籍尚未添加章节，则无需删除目录与章节信息
            if (!list.isEmpty()){

                //1.1 删除该本书所有章节
                int ContentResult = chapterContentMapper.deleteBatchIds(list);

                //2. 根据bookID删除该本书所有目录信息
                QueryWrapper<BookChapter> deleteWrapper = new QueryWrapper<>();
                deleteWrapper.eq("book_id",bookId);
                int ChapterResult = bookChapterMapper.delete(deleteWrapper);

                if (ContentResult < 1){
                    System.out.println("bookId为"+bookId+"的书章节删除出错!");
                } else if (ChapterResult < 1){
                    System.out.println("bookId为"+bookId+"的书目录信息删除出错!");
                }
            }

            //3. 根据bookID删除该本书的信息
            int bookResult = bookMapper.deleteById(bookId);

            if (bookResult > 0){
                //删除成功，记录删除的书本数量
                bookCount++;
            }
        }

        if (bookCount != bookIds.length){
            System.out.println("小说删除出现错误,有信息删除失败！");
        }

        return bookCount;

    }

    /**
     * 分页查询小说
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Book> getBookList(int pageNum, int pageSize,Book book) {

        PageHelper.startPage(pageNum,pageSize);
        List<Book> bookList = bookMapper.selectBookList(book);
        PageInfo<Book> pageInfo = new PageInfo<>(bookList);

        return pageInfo;

    }

    /**
     * 查询待审核小说
     * @return
     */
    public List<Book> selectCheckBook(Book book) {
        //查询连载中的小说（无论审核与否：已审核的需要审核后续章节，未审核的需要进行审核）
        book.setBookStatus(0);

        List<Book> bookList = bookMapper.selectBookList(book);

        return bookList;
    }

    /**
     * 查询当前小说的最新章节
     * @param id
     * @param maxChapterIndex
     * @return
     */
    public BookChapter getlastChapter(Long id, Integer maxChapterIndex) {
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id",id).eq("chapter_index",maxChapterIndex);
        BookChapter bookChapter = bookChapterMapper.selectOne(queryWrapper);
        return bookChapter;
    }

    /**
     * 爬取单本小说增添入库
     * @param book
     * @return
     */
    public Long spiderInsertOneBook(Book book) {
        book.setAuthorSpeak("新作出品，希望大家支持欧！！！");
        //设置创建时间
        book.setCreateTime(DateUtils.parseDate(DateUtils.getTime()));
        //设置审核状态为审核通过
        book.setCheckStatus(1);
        int rows = bookMapper.insert(book);
        if (rows != 1){
            throw new ServiceException("小说 "+book.getBookName()+" 入库失败！");
        }
        return book.getId();
    }

    /**
     * 根据小说名称查询是否有重名小说存在
     * @param bookName
     * @return
     */
    public Boolean selectBookByBookName(String bookName) {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_name",bookName);
        Book book = bookMapper.selectOne(queryWrapper);
        if (StringUtils.isNull(book)){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 根据条件查询书籍排行榜
     * @param queryItem
     * @return
     */
    public PageInfo<Book> selectBookListOrderByItem(Integer queryItem,int pageNum, int pageSize) {
        //0:点击榜、1:订阅榜、2:更新榜
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("check_status",1)
                .orderByDesc(queryItem == 0,"visit_count")
                .orderByDesc(queryItem == 1,"subs_num")
                .orderByDesc(queryItem == 2,"update_time");

        PageHelper.startPage(pageNum,pageSize);
        List<Book> bookList = bookMapper.selectList(queryWrapper);
        PageInfo<Book> pageInfo = new PageInfo<>(bookList);

        return pageInfo;
    }

    /**
     * 查询点击量最高的5本书籍
     * @param book
     * @return
     */
    @Override
    public List<Book> selectNewList(Book book) {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("check_status",1)
                .orderByDesc("visit_count").last("limit 5");
        List<Book> bookList = bookMapper.selectList(queryWrapper);

        return bookList;
    }

    /**
     * 查询同类型书籍点击量最高的10本书
     * @param bookCategory
     * @return
     */
    public List<Book> selectBookListByBookCategory(String bookCategory) {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("check_status",1)
                .eq("book_category",bookCategory).orderByDesc("visit_count").last("limit 10");
        List<Book> bookList = bookMapper.selectList(queryWrapper);

        return bookList;
    }

    /**
     * 设置书籍审核状态为待审核
     * @param id
     * @return
     */
    public Boolean updateBookCheck(Long id) {

        //查询当前书籍
        Book book = bookMapper.selectById(id);
        book.setCheckStatus(2);
        return bookMapper.updateById(book) > 0? true : false;

    }

    /**
     * 全部作品条件查询
     * @param pageNum
     * @param pageSize
     * @param searchDataVo
     * @return
     */
    public PageInfo<Book> getBookListByItem(int pageNum, int pageSize, SearchDataVo searchDataVo) {

        // 字数条件
        int startWord = 0;
        int endWord = 0;
        if (StringUtils.isNotNull(searchDataVo.getBookWord())){
            if (searchDataVo.getBookWord() == 0){
                //0-30万字
                startWord = 0;
                endWord = 300000;
            }
            if (searchDataVo.getBookWord() == 1){
                //30-50万字
                startWord = 300000;
                endWord = 500000;
            }
            if (searchDataVo.getBookWord() == 2){
                //50-100万字
                startWord = 500000;
                endWord = 1000000;
            }
            if (searchDataVo.getBookWord() == 3){
                //100万字以上
                startWord = 1000000;
                endWord = 100000000;
            }
        }

        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("check_status",1)
                .eq(StringUtils.isNotNull(searchDataVo.getBookCategory()),"book_category",String.valueOf(searchDataVo.getBookCategory()))
                .eq(StringUtils.isNotNull(searchDataVo.getBookStatus()),"book_status",searchDataVo.getBookStatus())
                .between(StringUtils.isNotNull(searchDataVo.getBookWord()),"book_word",startWord,endWord);

        // 更新时间
        if (StringUtils.isNotNull(searchDataVo.getTimeItem())){
            if (searchDataVo.getTimeItem() == 0){
                //三日内
                queryWrapper.apply("DATE_SUB(CURDATE(), INTERVAL 2 DAY) <= date(update_time)");
            }
            if (searchDataVo.getTimeItem() == 1){
                //七日内
                queryWrapper.apply("DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(update_time)");
            }
            if (searchDataVo.getTimeItem() == 2){
                //半月内
                queryWrapper.apply("DATE_SUB(CURDATE(), INTERVAL 14 DAY) <= date(update_time)");
            }
            if (searchDataVo.getTimeItem() == 3){
                //一月内
                queryWrapper.apply("DATE_FORMAT( update_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' )");
            }
        }

        // 排序条件
        if (StringUtils.isNotNull(searchDataVo.getOrderByItem())){
            if (searchDataVo.getOrderByItem() == 0){
                queryWrapper.orderByDesc("update_time");
            }
            if (searchDataVo.getOrderByItem() == 1){
                queryWrapper.orderByDesc("book_word");
            }
            if (searchDataVo.getOrderByItem() == 2){
                queryWrapper.orderByDesc("visit_count");
            }
        }

        PageHelper.startPage(pageNum,pageSize);
        List<Book> bookList = bookMapper.selectList(queryWrapper);
        PageInfo<Book> pageInfo = new PageInfo<>(bookList);

        return pageInfo;
    }

    /**
     * 表头查询结果按条件显示
     * @param pageNum
     * @param pageSize
     * @param searchDataVo
     * @param bookName
     * @return
     */
    @Override
    public PageInfo<Book> getBookListBySearchItem(Integer pageNum, Integer pageSize, SearchDataVo searchDataVo, String bookName) {

        // 字数条件
        int startWord = 0;
        int endWord = 0;
        if (StringUtils.isNotNull(searchDataVo.getBookWord())){
            if (searchDataVo.getBookWord() == 0){
                //0-30万字
                startWord = 0;
                endWord = 300000;
            }
            if (searchDataVo.getBookWord() == 1){
                //30-50万字
                startWord = 300000;
                endWord = 500000;
            }
            if (searchDataVo.getBookWord() == 2){
                //50-100万字
                startWord = 500000;
                endWord = 1000000;
            }
            if (searchDataVo.getBookWord() == 3){
                //100万字以上
                startWord = 1000000;
                endWord = 100000000;
            }
        }

        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("book_name",bookName)
                .eq("check_status",1)
                .eq(StringUtils.isNotNull(searchDataVo.getBookCategory()),"book_category",String.valueOf(searchDataVo.getBookCategory()))
                .eq(StringUtils.isNotNull(searchDataVo.getBookStatus()),"book_status",searchDataVo.getBookStatus())
                .between(StringUtils.isNotNull(searchDataVo.getBookWord()),"book_word",startWord,endWord);

        // 更新时间
        if (StringUtils.isNotNull(searchDataVo.getTimeItem())){
            if (searchDataVo.getTimeItem() == 0){
                //三日内
                queryWrapper.apply("DATE_SUB(CURDATE(), INTERVAL 2 DAY) <= date(update_time)");
            }
            if (searchDataVo.getTimeItem() == 1){
                //七日内
                queryWrapper.apply("DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(update_time)");
            }
            if (searchDataVo.getTimeItem() == 2){
                //半月内
                queryWrapper.apply("DATE_SUB(CURDATE(), INTERVAL 14 DAY) <= date(update_time)");
            }
            if (searchDataVo.getTimeItem() == 3){
                //一月内
                queryWrapper.apply("DATE_FORMAT( update_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' )");
            }
        }

        // 排序条件
        if (StringUtils.isNotNull(searchDataVo.getOrderByItem())){
            if (searchDataVo.getOrderByItem() == 0){
                queryWrapper.orderByDesc("update_time");
            }
            if (searchDataVo.getOrderByItem() == 1){
                queryWrapper.orderByDesc("book_word");
            }
            if (searchDataVo.getOrderByItem() == 2){
                queryWrapper.orderByDesc("visit_count");
            }
        }

        PageHelper.startPage(pageNum,pageSize);
        List<Book> bookList = bookMapper.selectList(queryWrapper);
        PageInfo<Book> pageInfo = new PageInfo<>(bookList);

        return pageInfo;
    }
}




