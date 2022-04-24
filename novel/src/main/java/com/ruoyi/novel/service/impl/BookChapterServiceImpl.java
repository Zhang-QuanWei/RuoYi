package com.ruoyi.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.novel.domain.Book;
import com.ruoyi.novel.domain.BookChapter;
import com.ruoyi.novel.domain.ChapterContent;
import com.ruoyi.novel.mapper.ChapterContentMapper;
import com.ruoyi.novel.service.BookChapterService;
import com.ruoyi.novel.mapper.BookChapterMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 64829
* @description 针对表【book_chapter】的数据库操作Service实现
* @createDate 2022-03-15 00:56:31
*/
@Service
public class BookChapterServiceImpl extends ServiceImpl<BookChapterMapper, BookChapter>
    implements BookChapterService{

    @Resource
    BookChapterMapper bookChapterMapper;

    @Resource
    ChapterContentMapper chapterContentMapper;

    /**
     * 查询指定小说的目录
     * @param bookChapter
     * @return
     */
    public List<BookChapter> selectBookChapterList(BookChapter bookChapter) {
        List<BookChapter> bookChapterList = bookChapterMapper.selectBookChapterList(bookChapter);
        return bookChapterList;
    }

    /**
     * 查询当前小说的章节数目
     * @param bookId
     * @return
     */
    public Integer selectChapterCountByBookId(Long bookId) {

        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id",bookId);

        Integer count = bookChapterMapper.selectCount(queryWrapper);

        return count;
    }

    /**
     * 通过章节ID删除目录信息与章节信息
     * @param ids
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)  //增删改加事务注解
    public int deleteBookChapterByIds(String ids) {
        int rows = 0;
        Long[] bookChapterIds = Convert.toLongArray(ids);
        for (Long bookChapterId : bookChapterIds) {
            //1. 查询单条记录
            BookChapter bookChapter = bookChapterMapper.selectById(bookChapterId);
            //2. 删除该记录对应的章节内容
            chapterContentMapper.deleteById(bookChapter.getChapterId());
            //3. 删除该条记录
            rows = rows + bookChapterMapper.deleteById(bookChapterId);
        }
        if (rows != bookChapterIds.length){
            throw new ServiceException("删除章节信息失败！");
        }
        return rows;

    }

    /**
     * 查询当前小说目录下最大的章节序号
     * @param bookId
     * @return
     */
    public Integer selectMaxChapterIndexByBookId(Long bookId) {

        Integer index = bookChapterMapper.selectMaxChapterIndexByBookId(bookId);

        //当前小说尚无章节信息/目录信息
        if (StringUtils.isNull(index)){
            System.out.println("当前小说尚无章节信息");
            return null;
        }
        return index;
    }

    /**
     * 通过章节ID查询目录记录
     * @param chapterId
     * @return
     */
    public BookChapter selectBookChapterByChapterId(Long chapterId) {

        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id",chapterId);
        BookChapter bookChapter = bookChapterMapper.selectOne(queryWrapper);

        if (StringUtils.isNull(bookChapter)){
            System.out.println("未通过章节ID查询到相关记录！");
            return null;
        }
        return bookChapter;

    }

    /**
     * 更新章节审核结果
     * @param bookChapter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)  //增删改加事务注解
    public Integer checkOne(BookChapter bookChapter) {
        //1. 根据章节id查询到对应章节
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id",bookChapter.getChapterId());
        BookChapter one = bookChapterMapper.selectOne(queryWrapper);
        //2. 修改章节对应审核记录
        one.setCheckStatus(bookChapter.getCheckStatus());
        //3. 更新章节信息
        int rows = bookChapterMapper.updateById(one);

        if (rows != 1){
            throw new ServiceException("提交章节审核结果失败！");
        }

        return rows;

    }

    /**
     * 根据小说id与章节索引查询章节信息
     * @param bookId
     * @param chapterIndex
     * @return
     */
    public BookChapter selectBookChapter(Long bookId, int chapterIndex) {
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id",bookId).eq("chapter_index",chapterIndex);
        BookChapter bookChapter = bookChapterMapper.selectOne(queryWrapper);

        if (StringUtils.isNull(bookChapter)){
            return null;
        }

        return bookChapter;
    }

    /**
     * 通过小说id查询目录信息
     * @param id
     * @return
     */
    public List<BookChapter> selectBookChapterByBookId(Long id) {
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id",id);
        List<BookChapter> bookChapterList = bookChapterMapper.selectList(queryWrapper);
        return bookChapterList;

    }

    /**
     * 查询已审核过的小说章节目录
     * @param book
     * @return
     */
    public List<BookChapter> selectBookChapters(Book book) {

        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id",book.getId())
                .eq("check_status",1);
        List<BookChapter> bookChapterList = bookChapterMapper.selectList(queryWrapper);

        return bookChapterList;
    }
}




