package com.ruoyi.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.novel.domain.Feedback;
import com.ruoyi.novel.service.FeedbackService;
import com.ruoyi.novel.mapper.FeedbackMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 64829
* @description 针对表【feedback】的数据库操作Service实现
* @createDate 2022-04-13 22:03:26
*/
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback>
    implements FeedbackService{

    @Resource
    private FeedbackMapper feedbackMapper;

    /**
     * 查询集合
     * @param feedback
     * @return
     */
    @Override
    public List<Feedback> selectFeedBackList(Feedback feedback) {

        QueryWrapper<Feedback> queryWrapper = new QueryWrapper<>(feedback);
        List<Feedback> feedbackList = feedbackMapper.selectList(queryWrapper);

        return feedbackList;
    }

    /**
     * 根据id查询
     */
    @Override
    public Feedback selectFeedBackById(Long id) {

        Feedback feedback = feedbackMapper.selectById(id);


        return feedback;
    }

    /**
     * 反馈已解决
     * @param info
     * @return
     */
    @Override
    public Boolean sloveFeedBack(Feedback info) {

        info.setResolutionStatus(1);

        int row = feedbackMapper.updateById(info);

        if (row != 1){
            return false;
        }

        return true;
    }
}




