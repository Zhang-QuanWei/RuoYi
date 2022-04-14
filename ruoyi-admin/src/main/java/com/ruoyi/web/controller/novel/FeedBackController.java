package com.ruoyi.web.controller.novel;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.novel.domain.Feedback;
import com.ruoyi.novel.service.FeedbackService;
import com.ruoyi.system.domain.SysPost;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/novel/feedback")
public class FeedBackController extends BaseController {

    @Resource
    private FeedbackService feedbackService;

    private String prefix = "novel/feedback";

    @GetMapping()
    public String open(){

        return prefix + "/feedback";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Feedback feedback)
    {
        startPage();
        List<Feedback> list = feedbackService.selectFeedBackList(feedback);
        return getDataTable(list);
    }

    @PostMapping("/solve")
    @ResponseBody
    public AjaxResult solve(Feedback feedback){
        Feedback info = feedbackService.selectFeedBackById(feedback.getId());

        Boolean result = feedbackService.sloveFeedBack(info);

        if (result){
            return AjaxResult.success("反馈问题已解决！");
        }

        return AjaxResult.error("反馈问题未解决，请联系开发人员！");
    }



}
