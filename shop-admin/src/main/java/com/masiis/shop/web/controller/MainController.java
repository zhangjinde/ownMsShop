package com.masiis.shop.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.masiis.shop.dao.menu.BMenu;
import com.masiis.shop.dao.menu.BUserMenu;
import com.masiis.shop.dao.menu.Tree;
import com.masiis.shop.dao.user.User;
import com.masiis.shop.service.menu.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 * Created by cai_tb on 16/2/22.
 */
@Controller
@RequestMapping("/main")
public class MainController {

    @Resource
    private MenuService menuService;

    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {

        ModelAndView mav = new ModelAndView("index");

        User user = (User)request.getSession().getAttribute("user");
        if (user != null){
            String menus = loadMainMenu(user.getId());
            mav.addObject("menus", menus);
        }

        return mav;
    }

    private List<BMenu> bm_list = null;
    private String userMenuIds = "";

    public String loadMainMenu(Long userID) throws JsonProcessingException {
        bm_list = menuService.getData();
        List<BUserMenu> list = menuService.getUserMenu(userID);
        List<Long> menus = new ArrayList();
        for (BUserMenu bm : list) {
            menus.add(bm.getMenuId());
        }
        userMenuIds = org.apache.commons.lang.StringUtils.join(menus, ',');
        JSONObject jo = new JSONObject();
        List<Tree> tree_list = new ArrayList<>();
        getTreeJson(0l, tree_list);
        jo.put("menus", tree_list);
        return jo.toJSONString();
    }

    private Tree tree = null;

    private void getTreeJson(Long pid, List<Tree> tree_list) {
        for (BMenu bm : bm_list) {
            if (userMenuIds.contains(bm.getId().toString()) && bm.getParentId() == pid) {
                tree = new Tree();
                tree.setMenuid(bm.getId());
                tree.setIcon(bm.getIcon());
                tree.setMenuname(bm.getName());
                tree.setParentId(bm.getParentId());
                tree.setUrl(bm.getUrl());
                tree_list.add(tree);
                getTreeJson(bm.getId(), tree.getMenus());
            }
        }
    }
}
