package com.ruoyi.novel.vo;

import lombok.Data;

/**
 * 书籍检索条件查询VO类
 */
@Data
public class SearchDataVo {

    String bookCategory;
    Integer bookStatus;
    Integer bookWord;
    String bookUpdate;
}
