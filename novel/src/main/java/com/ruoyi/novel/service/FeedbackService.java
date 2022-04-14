package com.ruoyi.novel.service;

import com.ruoyi.novel.domain.Feedback;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 64829
* @description 针对表【feedback】的数据库操作Service
* @createDate 2022-04-13 22:03:27
*/
public interface FeedbackService extends IService<Feedback> {

    List<Feedback> selectFeedBackList(Feedback feedback);

    Feedback selectFeedBackById(Long id);

    Boolean sloveFeedBack(Feedback info);
}
