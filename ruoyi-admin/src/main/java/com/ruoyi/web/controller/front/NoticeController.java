package com.ruoyi.web.controller.front;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/front/notice")
public class NoticeController extends BaseController {

    private String prefix = "front/index";

    @Resource
    private ISysNoticeService noticeService;

    /**
     * 根据通知类型查询
     * @param notice
     * @param modelMap
     * @return
     */
    @GetMapping("/change/{id}")
    public String changeType(SysNotice notice, ModelMap modelMap){
        List<SysNotice> noticeList = noticeService.selectNoticeList(notice);

        modelMap.put("noticeList",noticeList);

        return prefix + "/notice";
    }

    /**
     * 跳转通知内容界面
     * @return
     */
    @GetMapping("/toNoticeContent/{id}")
    public String toNoticeContent(SysNotice notice,ModelMap modelMap){

        SysUser user = ShiroUtils.getSysUser();
        modelMap.put("user",user);

        SysNotice sysNotice = noticeService.selectNoticeById(notice.getNoticeId());

        modelMap.put("sysNotice",sysNotice);

        return prefix + "/noticeContent";
    }
}
