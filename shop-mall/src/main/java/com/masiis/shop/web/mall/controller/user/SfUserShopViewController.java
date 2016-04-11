package com.masiis.shop.web.mall.controller.user;

import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.dao.po.ComUser;
import com.masiis.shop.dao.po.SfShopSku;
import com.masiis.shop.dao.po.SfUserShopView;
import com.masiis.shop.web.mall.controller.base.BaseController;
import com.masiis.shop.web.mall.service.shop.SfShopSkuService;
import com.masiis.shop.web.mall.service.user.SfUserShopViewService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangbingjian on 2016/4/10.
 */
@Controller
@RequestMapping(value = "shopview")
public class SfUserShopViewController extends BaseController {

    private final Logger logger = Logger.getLogger(SfUserShopViewController.class);

    @Autowired
    private SfUserShopViewService sfUserShopViewService;
    @Autowired
    private SfShopSkuService sfShopSkuService;
    /**
     * 用户查看浏览过的店铺
     * @param request
     * @auth wbj
     * @return
     */
    @RequestMapping(value = "home.shtml")
    public ModelAndView viewedShop(HttpServletRequest request) throws Exception{

        logger.info("用户查看浏览过的店铺");
        ComUser user = getComUser(request);
        if (user == null){
            throw new BusinessException("用户未登录");
        }
        Long userId = user.getId();
        logger.info("用户查看浏览过的店铺userId="+userId);
        List<SfUserShopView> shopViews = sfUserShopViewService.findShopViewByUserIdByLimit(userId,1,20);
        List<SfShopSku> sfShopSkus;
        List<SfUserShopView> sfUserShopViews = null;
        for (SfUserShopView sfUserShopView : shopViews){
            sfShopSkus = sfShopSkuService.findShopSkuByShopId(sfUserShopView.getShopId());
            sfUserShopView.setShopSkus(sfShopSkus);
            sfUserShopViews.add(sfUserShopView);
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("sfUserShopViews",sfUserShopViews);
        mv.setViewName("mall/shop/viewedShop");
        return mv;
    }
}