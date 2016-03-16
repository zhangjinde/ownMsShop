package com.masiis.shop.admin.controller.order;

import com.masiis.shop.admin.beans.order.Order;
import com.masiis.shop.admin.service.order.BOrderService;
import com.masiis.shop.dao.po.PfBorder;
import com.masiis.shop.dao.po.PfBorderFreight;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by cai_tb on 16/3/12.
 */
@Controller
@RequestMapping("/order/border")
public class PfBorderController {

    @Resource
    private BOrderService bOrderService;

    @RequestMapping("/list.shtml")
    public String list(){
        return "order/border/list";
    }

    @RequestMapping("/list.do")
    @ResponseBody
    public Object list(HttpServletRequest request, HttpServletResponse response,
                       Integer pageNumber,
                       Integer pageSize,
                       String sortOrder,
                       PfBorder pfBorder){

        Map<String, Object> pageMap = bOrderService.listByCondition(pageNumber, pageSize, pfBorder);

        return pageMap;
    }

    @RequestMapping("detail.shtml")
    public ModelAndView detail(HttpServletRequest request, HttpServletResponse response, Long borderId){

        ModelAndView mav = new ModelAndView("order/border/detail");

        Order order = bOrderService.find(borderId);

        mav.addObject("order", order);

        return mav;
    }

    @RequestMapping("/delivery.do")
    @ResponseBody
    public Object delivery(HttpServletRequest request, HttpServletResponse response,
                           PfBorderFreight pfBorderFreight){

        if (pfBorderFreight.getShipManId() == null){
            return "请选择一个快递";
        }
        if(StringUtils.isBlank(pfBorderFreight.getFreight())){
            return "请填写运单号";
        }

        bOrderService.delivery(pfBorderFreight);

        return "success";
    }
}
