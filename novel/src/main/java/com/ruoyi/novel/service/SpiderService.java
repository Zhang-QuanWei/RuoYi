package com.ruoyi.novel.service;

public interface SpiderService {

    //抓取单本小说
    boolean insertOneBook(String url);

    boolean insertBooks(String url);
}
