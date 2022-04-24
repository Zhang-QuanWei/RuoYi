package com.ruoyi.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.novel.domain.Report;
import com.ruoyi.novel.service.ReportService;
import com.ruoyi.novel.mapper.ReportMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 64829
* @description 针对表【report】的数据库操作Service实现
* @createDate 2022-04-16 19:23:42
*/
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report>
    implements ReportService{

    @Resource
    private ReportMapper reportMapper;

    /**
     * 查询举报记录
     * @param report
     * @return
     */
    @Override
    public List<Report> selectReportList(Report report) {
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(report.getBookName()),"book_name",report.getBookName())
                .eq(StringUtils.isNotNull(report.getReportType()),"report_type",report.getReportType())
                .eq(StringUtils.isNotNull(report.getSolveStatus()),"solve_status",report.getSolveStatus());
        List<Report> reportList = reportMapper.selectList(queryWrapper);

        return reportList;
    }

    /**
     * 通过ID查询举报信息
     * @param id
     * @return
     */
    @Override
    public Report selectReportById(Long id) {

        return reportMapper.selectById(id);

    }

    /**
     * 受理举报问题
     * @param info
     * @return
     */
    @Override
    public Boolean sloveReport(Report info) {

        //设置受理状态为已受理
        info.setSolveStatus(1);

        int rows = reportMapper.updateById(info);

        return rows == 1 ? true : false;
    }
}




