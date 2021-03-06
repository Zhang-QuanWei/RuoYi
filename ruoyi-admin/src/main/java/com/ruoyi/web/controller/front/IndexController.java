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
     * ????????????
     * @return
     */
    @GetMapping("")
    public String toIndex(ModelMap modelMap)
    {
        Book book = new Book();
        book.setCheckStatus(1);
        Integer pageNum = 1;
        Integer pageSize = 10;
        //??????5???????????????????????????
        List<Book> bookList = bookService.selectNewList(null);
        //??????????????????
        PageInfo<Book> pageInfo = bookService.getBookList(pageNum, pageSize,book);
        //??????3?????????
        List<SysNotice> noticeList = noticeService.selectNewList(null);

        SysUser user = ShiroUtils.getSysUser();
        modelMap.put("user",user);


        modelMap.put("pageInfo",pageInfo);
        modelMap.put("noticeList",noticeList);
        modelMap.put("bookList",bookList);
        return prefix + "/index";
    }

    /**
     * ????????????????????????
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
     * ??????????????????
     * @param pageNum
     * @param pageSize
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/list/{id}",method = RequestMethod.GET)
    public String list(Integer pageNum,Integer pageSize,ModelMap modelMap){

        Book book = new Book();
        book.setCheckStatus(1);

        //??????5???????????????????????????
        List<Book> bookList = bookService.selectNewList(null);
        //??????????????????
        PageInfo<Book> pageInfo = bookService.getBookList(pageNum, pageSize, book);
        //??????3?????????
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
     * ???????????????
     * @return
     */
    @GetMapping("/login")
    public String toLogin(){
        //TODO ???????????????????????????????????????????????????????????????????????????????????????????????????
        return prefix + "/login";
    }

    /**
     * ????????????
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
            String msg = "?????????????????????";
            if (StringUtils.isNotEmpty(e.getMessage()))
            {
                msg = e.getMessage();
            }
            return error(msg);
        }
    }

    /**
     * ???????????????
     * @return
     */
    @GetMapping("/register")
    public String toRegister(){
        return  prefix + "/register";
    }

    /**
     * ????????????
     * @param user
     * @return
     */
    @PostMapping("/register")
    @ResponseBody
    public AjaxResult ajaxRegister(SysUser user)
    {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser"))))
        {
            return error("???????????????????????????????????????");
        }
        String msg = registerService.register(user);

        if (StringUtils.isEmpty(msg)){
            //?????????????????????????????????????????????
            Long[] roleIds = {2L};
            userService.insertUserAuth(user.getUserId(), roleIds);
            AuthorizationUtils.clearAllCachedAuthorizationInfo();
            return success();
        }
        return error(msg);
    }

    /**
     * ??????????????????
     * @return
     */
    @GetMapping("/toBookShelf")
    public String toBookShelf(ModelMap modelMap){
        //??????????????????
        SysUser user = ShiroUtils.getSysUser();

        //??????????????????????????????
        List<Book> bookList = bookShelfService.selectBookList(user.getUserId());

        // ???????????????????????????
        modelMap.put("bookList",bookList);
        modelMap.put("user",user);
        return prefix + "/bookShelf";
    }

    /**
     * ??????????????????????????????
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
     * ????????????????????????
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
     * ?????????????????????
     * @return
     */
    @GetMapping("/toRankList")
    public String toRankList(ModelMap modelMap){

        //?????????????????????????????????????????????
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
     * ????????????????????????
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
     * ??????????????????????????????
     * @param book
     * @param modelMap
     * @return
     */
    @PostMapping("/toNovelResult")
    public String toNovelResult(Book book,ModelMap modelMap){
        SysUser user = ShiroUtils.getSysUser();
        modelMap.put("user",user);

        //????????????????????????
        book.setCheckStatus(1);

        Integer pageNum = 1;
        Integer pageSize = 10;

        PageInfo<Book> pageInfo = bookService.getBookList(pageNum,pageSize,book);

        modelMap.put("pageInfo",pageInfo);


        return prefix + "/novelResult";
    }

    /**
     * ??????????????????????????????
     * @param bookName
     * @param searchDataVo
     * @param pageNum
     * @param pageSize
     * @param modelMap
     * @return
     */
    @GetMapping("/toNovelResultPlus")
    public String toNovelResultPlus(String bookName,SearchDataVo searchDataVo,Integer pageNum,Integer pageSize,ModelMap modelMap){



        SysUser user = ShiroUtils.getSysUser();
        modelMap.put("user",user);

        if (StringUtils.isNull(pageNum) && StringUtils.isNull(pageSize)){
            pageNum = 1;
            pageSize = 10;
        }

        PageInfo<Book> pageInfo = bookService.getBookListBySearchItem(pageNum,pageSize,searchDataVo,bookName);

        modelMap.put("pageInfo",pageInfo);
        modelMap.put("searchDataVo", JSONObject.toJSON(searchDataVo));
        modelMap.put("bookItem",bookName);

        return prefix + "/searchResult";
    }

    /**
     * ??????????????????????????????
     * @return
     */
    @GetMapping("/toEditUserInfo")
    public String toEditUserInfo(ModelMap modelMap){

        SysUser user = ShiroUtils.getSysUser();

        modelMap.put("user",user);

        return prefix + "/userInfoEdit";
    }

    /**
     * ????????????????????????
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
     * ????????????????????????
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
     * ???????????????????????????
     * @param authorCode
     * @return
     */
    @GetMapping("/checkAuthorCode")
    @ResponseBody
    public Boolean checkAuthorCode(AuthorCode authorCode){

        return authorCodeService.checkCode(authorCode.getInviteCode());

    }

    /**
     * ????????????
     * @param inviteCode
     * @param email
     * @param phonenumber
     * @return
     */
    @PostMapping("/authorRegister")
    @ResponseBody
    public AjaxResult authorRegister(String inviteCode,String email,String phonenumber){
        System.out.println(inviteCode+","+email+","+phonenumber);
        //1. ???????????????????????????
        SysUser user = ShiroUtils.getSysUser();
        if (StringUtils.isNotNull(user)){
            //???????????????????????????????????????
            Long[] roleIds = {100L};
            userService.insertUserAuth(user.getUserId(), roleIds);
            AuthorizationUtils.clearAllCachedAuthorizationInfo();
        }else {
            return AjaxResult.error("?????????????????????");
        }
        //2. ???????????????????????????????????????
        user.setEmail(email);
        user.setPhonenumber(phonenumber);
        int row = userService.updateUserInfo(user);
        //3. ????????????????????????
        AuthorCode authorCode = authorCodeService.selectAuthorCodeByCode(inviteCode);
        authorCode.setIsUse(1);
        boolean result = authorCodeService.updateById(authorCode);

        if (row == 1 && result){
            return AjaxResult.success("?????????????????????????????????????????????...");
        }else {
            return AjaxResult.error("?????????????????????????????????");
        }

    }
}
