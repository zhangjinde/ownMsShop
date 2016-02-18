package com.masiis.shop.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.masiis.shop.dao.user.User;
import com.masiis.shop.service.user.UserService;
import com.masiis.shop.web.utils.KeysUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cai_tb on 16/2/16.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 登陆页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/login.shtml")
    public String login(HttpServletRequest request, HttpServletResponse response){
        return "user/login";
    }

    /**
     * 用户列表页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/list.shtml")
    public String list(HttpServletRequest request, HttpServletResponse response){
        return "user/userList";
    }

    /**
     * 登陆逻辑
     * @param request
     * @param response
     * @param user      登陆参数
     * @return
     */
    @RequestMapping("/login")
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response, User user){
        ModelAndView mav = new ModelAndView();

        /* 已登陆 */
        HttpSession session = request.getSession();
        if(!session.isNew() && session.getAttribute("user") != null){
            mav.setViewName("index");
            return mav;
        }

        /* 未登陆 */

        //用户名或密码为空
        if(StringUtil.isEmpty(user.getUserName()) || StringUtil.isEmpty(user.getPassword())){
            mav.setViewName("redirect:toLogin");
            mav.addObject("user", user);
            return mav;
        }

        User u = this.userService.findByUserNameAndPwd(user.getUserName(), KeysUtil.md5Encrypt(user.getPassword()));
//        User u1 = new User();
//        u1.setUserName("admin");
//        u1.setTrueName("admin");
//        u1.setPassword(KeysUtil.md5Encrypt("000000"));
//        u1.setEmail("admin@qq.com");
//        u1.setPhone("13669660493");
//        this.userService.addUser(u1);

        //用户名或密码不对
        if (u == null){
            mav.setViewName("redirect:toLogin");
            mav.addObject("user", user);
            return mav;
        }

        //登陆成功
        session.setAttribute("user", user);
        mav.setViewName("/index");
        return mav;
    }

    /**
     * 条件分页查询用户数据
     * @param request
     * @param response
     * @param user        用户相关查询条件
     * @param pageNum     当前页
     * @param pageSize    每页显示条数
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(HttpServletRequest request, HttpServletResponse response,
                             User user,
                             Integer pageNum,
                             Integer pageSize
                            ) throws JsonProcessingException {

        pageNum  = pageNum  == null ? 1  : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;

        PageHelper.startPage(pageNum, pageSize);
        List<User> users = this.userService.listUserByCondition(user);
        PageInfo<User> pageInfo = new PageInfo<>(users);

        Map<String, Object> usersMap = new HashMap<>();
        usersMap.put("total", pageInfo.getTotal());
        usersMap.put("rows", users);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(usersMap);

        //return usersMap;
    }

    /**
     * 添加用户
     * @param request
     * @param response
     * @param user      新添用户数据
     */
    @RequestMapping("/add")
    public void add(HttpServletRequest request, HttpServletResponse response, User user){
        if (StringUtil.isNotEmpty(user.getPassword())){
            user.setPassword(KeysUtil.md5Encrypt(user.getPassword()));
        }

        this.userService.addUser(user);
    }
}
