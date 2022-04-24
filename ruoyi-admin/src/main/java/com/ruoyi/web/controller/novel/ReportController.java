package com.ruoyi.web.controller.novel;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.novel.domain.Feedback;
import com.ruoyi.novel.domain.Report;
import com.ruoyi.novel.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/novel/report")
public class ReportController extends BaseController {

    @Resource
    private ReportService reportService;

    private String prefix = "novel/report";

    /**
     * 跳转举报管理页面
     * @return
     */
    @GetMapping()
    public String report(){
        return prefix + "/report";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Report report)
    {
        //查询未受理的举报信息
        // report.setSolveStatus(0);


        startPage();
        List<Report> list = reportService.selectReportList(report);
        return getDataTable(list);
    }

    @PostMapping("/solve")
    @ResponseBody
    public AjaxResult solve(Report report){
        Report info = reportService.selectReportById(report.getId());

        Boolean result = reportService.sloveReport(info);

        if (result){
            return AjaxResult.success("举报问题已解决！");
        }

        return AjaxResult.error("举报问题未解决，请联系开发人员！");
    }

}
