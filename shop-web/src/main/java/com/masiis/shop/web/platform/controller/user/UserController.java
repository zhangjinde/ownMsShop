package com.masiis.shop.web.platform.controller.user;

import com.masiis.shop.dao.po.ComUser;
import com.masiis.shop.web.platform.service.user.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by cai_tb on 16/2/16.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/login", produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String login(HttpServletRequest request, HttpServletResponse response) {
        return "";
    }

    /**
     * 用户绑定手机号
     * @author hanzengzhi
     * @date 2016/3/16 13:54
     */
    public String bindPhone(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "phone",required = true)String phone){
        try {
           ComUser comUser =  userService.bindPhone(request,phone);
        }catch (Exception e){
            e.getMessage();
        }
        return null;
    }
}

