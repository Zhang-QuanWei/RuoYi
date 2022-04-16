package com.ruoyi.novel.service;

import com.ruoyi.novel.domain.Report;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 64829
* @description 针对表【report】的数据库操作Service
* @createDate 2022-04-16 19:23:42
*/
public interface ReportService extends IService<Report> {

    /**
     * 查询举报信息列表
     * @param report
     * @return
     */
    List<Report> selectReportList(Report report);

    /**
     * 通过ID查询举报信息
     * @param id
     * @return
     */
    Report selectReportById(Long id);

    /**
     * 受理举报问题
     * @param info
     * @return
     */
    Boolean sloveReport(Report info);
}
