package com.ruoyi.web.controller.front;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.shiro.service.SysRegisterService;
import com.ruoyi.framework.shiro.util.AuthorizationUtils;
import com.ruoyi.framework.web.domain.server.Sys;
import com.ruoyi.framework.web.service.ConfigService;
import com.ruoyi.novel.domain.AuthorCode;
import com.ruoyi.novel.domain.Book;
import com.ruoyi.novel.service.AuthorCodeService;
import com.ruoyi.novel.service.impl.BookServiceImpl;
import com.ruoyi.novel.service.impl.BookShelfServiceImpl;
import com.ruoyi.novel.vo.SearchDataVo;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysNoticeService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/front/index")
public class IndexController extends BaseController {

    @Resource
    private BookServiceImpl bookService;

    @Resource
    private BookShelfServiceImpl bookShelfService;

    @Resource
    private ISysNoticeService noticeService;

    @Resource
    private ISysConfigService configService;

    @Resource
    private SysRegisterService registerService;

    @Resource
    private ISysUserService userService;

    @Resource
    private AuthorCodeService authorCodeService;

    private String prefix = "front/index";

    /**
     * 跳转首页
     * @return
     */
    @GetMapping("")
    public String toIndex(ModelMap modelMap)
    {
        Book book = new Book();
        book.setCheckStatus(1);
        Integer pageNum = 1;
        Integer pageSize = 10;
        //查询5本点击量最高的书籍
        List<Book> bookList = bookService.selectNewList(null);
        //查询书籍列表
        PageInfo<Book> pageInfo = bookService.getBookList(pageNum, pageSize,book);
        //查询3条公告
        List<SysNotice> noticeList = noticeService.selectNewList(null);

        SysUser user = ShiroUtils.getSysUser();
        modelMap.put("user",user);


        modelMap.put("pageInfo",pageInfo);
        modelMap.put("noticeList",noticeList);
        modelMap.put("bookList",bookList);
        return prefix + "/index";
    }

    /**
     * 跳转全部作品页面
     * @param modelMap
     * @return
     */
    @GetMapping("/allWork")
    public String toAllWork(SearchDataVo searchDataVo,ModelMap modelMap){

        PageInfo<Book> pageInfo = bookService.getBookListByItem(1,10,searchDataVo);

        SysUser user = ShiroUtils.getSysUser();
        modelMap.put("user",user);


        modelMap.put("searchDataVo", JSONObject.toJSON(searchDataVo));
        modelMap.put("pageInfo",pageInfo);

        return prefix + "/allWork";
    }

    /**
     * 展示书籍信息
     * @param pageNum
     * @param pageSize
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/list/{id}",method = RequestMethod.GET)
    public String list(Integer pageNum,Integer pageSize,ModelMap modelMap){

        //查询5本点击量最高的书籍
        List<Book> bookList = bookService.selectNewList(null);
        //查询书籍列表
        PageInfo<Book> pageInfo = bookService.getBookList(pageNum, pageSize,null);
        //查询3条公告
        List<SysNotice> noticeList = noticeService.selectNewList(null);

        SysUser user = ShiroUtils.getSysUser();

        if (StringUtils.isNotNull(user)){
            modelMap.put("user",user);
        }

        modelMap.put("pageInfo",pageInfo);
        modelMap.put("noticeList",noticeList);
        modelMap.put("bookList",bookList);

        return prefix + "/index";
    }

    /**
     * 跳转登录页
     * @return
     */
    @GetMapping("/login")
    public String toLogin(){
        //TODO 跳转到若依登录页，登录成功后跳转前端首页，后端首页通过作家专区进入
        return prefix + "/login";
    }

    /**
     * 登录请求
     * @param username
     * @param password
     * @param rememberMe
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public AjaxResult ajaxLogin(String username, String password, Boolean rememberMe)
    {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);

        Subject subject = SecurityUtils.getSubject();
        try
        {
            subject.login(token);
            return success();
        }
        catch (AuthenticationException e)
        {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage()))
            {
                msg = e.getMessage();
            }
            return error(msg);
        }
    }

    /**
     * 跳转注册页
     * @return
     */
    @GetMapping("/register")
    public String toRegister(){
        return  prefix + "/register";
    }

    /**
     * 注册请求
     * @param user
     * @return
     */
    @PostMapping("/register")
    @ResponseBody
    public AjaxResult ajaxRegister(SysUser user)
    {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser"))))
        {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);

        if (StringUtils.isEmpty(msg)){
            //为当前注册用户分配普通用户角色
            Long[] roleIds = {2L};
            userService.insertUserAuth(user.getUserId(), roleIds);
            AuthorizationUtils.clearAllCachedAuthorizationInfo();
            return success();
        }
        return error(msg);
    }

    /**
     * 跳转书架页面
     * @return
     */
    @GetMapping("/toBookShelf")
    public String toBookShelf(ModelMap modelMap){
        //获取当前用户
        SysUser user = ShiroUtils.getSysUser();

        //查询当前用户书架信息
        List<Book> bookList = bookShelfService.selectBookList(user.getUserId());

        // 跳转当前用户书架页
        modelMap.put("bookList",bookList);
        modelMap.put("user",user);
        return prefix + "/bookShelf";
    }

    /**
     * 跳转前台用户信息界面
     * @param modelMap
     * @return
     */
    @GetMapping("/toUserInfo")
    public String toUserInfo(ModelMap modelMap){

        SysUser user = ShiroUtils.getSysUser();
        modelMap.put("user",user);

        return prefix + "/userInfo";
    }

    /**
     * 跳转作家注册页面
     * @return
     */
    @GetMapping("/toAuthorRegis")
    public String toAuthorRegis(ModelMap modelMap){

        SysUser user = ShiroUtils.getSysUser();

        if (StringUtils.isNotNull(user)){
            modelMap.put("user",user);
        }
        return prefix + "/authorRegis";
    }

    /**
     * 跳转排行榜页面
     * @return
     */
    @GetMapping("/toRankList")
    public String toRankList(ModelMap modelMap){

        //根据点击榜条件查询排序后的书籍
        PageInfo<Book> pageInfo = bookService.selectBookListOrderByItem(0,1,10);

        SysUser user = ShiroUtils.getSysUser();

        if (StringUtils.isNotNull(user)){
            modelMap.put("user",user);
        }

        modelMap.put("currentQueryItem",0);
        modelMap.put("pageInfo",pageInfo);

        return prefix + "/rankList";
    }

    /**
     * 跳转通知公告界面
     * @return
     */
    @GetMapping("/toNotice")
    public String toNotice(ModelMap modelMap){

        SysUser user = ShiroUtils.getSysUser();
        modelMap.put("user",user);

        List<SysNotice> noticeList = noticeService.selectNoticeList(null);
        modelMap.put("noticeList",noticeList);

        return prefix + "/notice";
    }

    /**
     * 跳转表头查询结果页面
     * @param book
     * @param modelMap
     * @return
     */
    @PostMapping("/toNovelResult")
    public String toNovelResult(Book book,ModelMap modelMap){

        book.setCheckStatus(1);

        Integer pageNum = 1;
        Integer pageSize = 10;

        PageInfo<Book> pageInfo = bookService.getBookList(pageNum,pageSize,book);

        modelMap.put("pageInfo",pageInfo);

        return prefix + "/novelResult";
    }

    /**
     * 跳转用户信息编辑页面
     * @return
     */
    @GetMapping("/toEditUserInfo")
    public String toEditUserInfo(ModelMap modelMap){

        SysUser user = ShiroUtils.getSysUser();

        modelMap.put("user",user);

        return prefix + "/userInfoEdit";
    }

    /**
     * 跳转密码修改界面
     * @param modelMap
     * @return
     */
    @GetMapping("/toPwChange")
    public String toPwChange(ModelMap modelMap){
        SysUser user = ShiroUtils.getSysUser();

        modelMap.put("user",user);

        return prefix + "/pwChange";
    }

    /**
     * 跳转用户反馈界面
     * @param modelMap
     * @return
     */
    @GetMapping("/toFeedBack")
    public String toFeedBack(ModelMap modelMap){

        SysUser user = ShiroUtils.getSysUser();
        modelMap.put("user",user);

        return prefix + "/feedBack";
    }

    /**
     * 检查邀请码是否有效
     * @param authorCode
     * @return
     */
    @GetMapping("/checkAuthorCode")
    @ResponseBody
    public Boolean checkAuthorCode(AuthorCode authorCode){

        return authorCodeService.checkCode(authorCode.getInviteCode());

    }

    /**
     * 作家注册
     * @param inviteCode
     * @param email
     * @param phonenumber
     * @return
     */
    @PostMapping("/authorRegister")
    @ResponseBody
    public AjaxResult authorRegister(String inviteCode,String email,String phonenumber){
        System.out.println(inviteCode+","+email+","+phonenumber);
        //1. 为用户添加作家身份
        SysUser user = ShiroUtils.getSysUser();
        if (StringUtils.isNotNull(user)){
            //为当前注册用户分配作家角色
            Long[] roleIds = {100L};
            userService.insertUserAuth(user.getUserId(), roleIds);
            AuthorizationUtils.clearAllCachedAuthorizationInfo();
        }else {
            return AjaxResult.error("当前用户为空！");
        }
        //2. 作家用户添加邮箱、手机信息
        user.setEmail(email);
        user.setPhonenumber(phonenumber);
        int row = userService.updateUserInfo(user);
        //3. 标记邀请码已使用
        AuthorCode authorCode = authorCodeService.selectAuthorCodeByCode(inviteCode);
        authorCode.setIsUse(1);
        boolean result = authorCodeService.updateById(authorCode);

        if (row == 1 && result){
            return AjaxResult.success("作家注册成功！正在跳转管理界面...");
        }else {
            return AjaxResult.error("注册失败！请联系管理员");
        }

    }
}
