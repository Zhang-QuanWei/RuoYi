package com.ruoyi.web.controller.front;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.novel.domain.Feedback;
import com.ruoyi.novel.service.FeedbackService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/front/feedback")
public class FeedbackController {

    @Resource
    private FeedbackService feedbackService;

    @PostMapping("/add")
    @ResponseBody
    public AjaxResult add(Feedback feedback){

        SysUser user = ShiroUtils.getSysUser();
        feedback.setUserId(user.getUserId());
        feedback.setEmail(user.getEmail());
        feedback.setResolutionStatus(0);
        feedback.setCreateTime(DateUtils.parseDate(DateUtils.getTime()));

        boolean result = feedbackService.save(feedback);
        if (result){
            return AjaxResult.success("反馈成功！");
        }
        return AjaxResult.error("反馈失败！请重试！");
    }

}
