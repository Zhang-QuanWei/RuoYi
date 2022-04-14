package com.ruoyi.novel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.novel.domain.ChapterContent;
import com.ruoyi.novel.service.ChapterContentService;
import com.ruoyi.novel.mapper.ChapterContentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author 64829
* @description 针对表【chapter_content】的数据库操作Service实现
* @createDate 2022-03-15 00:56:31
*/
@Service
public class ChapterContentServiceImpl extends ServiceImpl<ChapterContentMapper, ChapterContent>
    implements ChapterContentService{

    @Resource
    private ChapterContentMapper chapterContentMapper;

    /**
     * 插入章节内容，返回新的章节ID
     * @param chapterContent
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)  //增删改加事务注解
    public ChapterContent insertNewChapter(ChapterContent chapterContent) {

        int rows = chapterContentMapper.insert(chapterContent);

        if (rows != 1){
            //抛出插入章节错误异常
            System.out.println("插入章节错误");
            return null;
        }

        return chapterContent;

    }

    /**
     * 根据章节ID查询记录
     * @param chapterId
     * @return
     */
    public ChapterContent selectChapterContentById(Long chapterId) {
        ChapterContent chapterContent = chapterContentMapper.selectById(chapterId);
        if (StringUtils.isNull(chapterContent)){
            System.out.println("未通过ID查询到相关内容表记录！");
        }
        return chapterContent;
    }

    /**
     * 爬取章节内容到数据库
     * @param contentHtml
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)  //增删改加事务注解
    public Long spiderOneContent(String contentHtml) {
        ChapterContent chapterContent = new ChapterContent();
        chapterContent.setChapterContent(contentHtml);

        int rows = chapterContentMapper.insert(chapterContent);

        if (rows != 1){
            throw new ServiceException("章节内容入库失败！");
        }

        return chapterContent.getId();

    }
}




