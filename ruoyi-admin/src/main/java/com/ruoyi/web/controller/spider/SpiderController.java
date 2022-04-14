package com.ruoyi.web.controller.spider;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.JsoupUtils;
import com.ruoyi.novel.service.SpiderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/spider")
public class SpiderController extends BaseController {

    @Resource
    private SpiderService spiderService;


    /**
     * 爬取单本小说（笔趣阁）
     * @return
     */
    @PostMapping("/spiderBook/BiQuGe")
    @ResponseBody
    public AjaxResult spiderOne(String url){

        //测试url是否可以正常链接
        int status = JsoupUtils.isConnection(url);
        if (status == 200){

            boolean flag = spiderService.insertOneBook(url);
            if (flag){
                return AjaxResult.success("该小说入库成功！");
            }else {
                return AjaxResult.error("小说入库失败，请重试！");
            }

        }else{
            return AjaxResult.error("网页链接错误，状态码："+status);
        }

    }

    /**
     * 爬取多本小说（笔趣阁）
     * @param url
     * @return
     */
    @PostMapping("/spiderBooks/BiQuGe")
    @ResponseBody
    public AjaxResult spiderMore(String url){
        //测试url是否可以正常链接
        int status = JsoupUtils.isConnection(url);
        if (status == 200){

            boolean flag = spiderService.insertBooks(url);
            if (flag){
                return AjaxResult.success("小说入库成功！");
            }else {
                return AjaxResult.error("小说入库失败，请重试！");
            }

        }else{
            return AjaxResult.error("网页链接错误，状态码："+status);
        }
    }
}
