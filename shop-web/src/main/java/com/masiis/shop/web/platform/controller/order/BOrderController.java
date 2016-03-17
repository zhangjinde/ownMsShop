package com.masiis.shop.web.platform.controller.order;

import com.alibaba.fastjson.JSONObject;
import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.common.util.OrderMakeUtils;
import com.masiis.shop.common.util.PropertiesUtils;
import com.masiis.shop.dao.beans.order.OrderUserSku;
import com.masiis.shop.dao.beans.product.ProductSimple;
import com.masiis.shop.dao.platform.product.ComSkuImageMapper;
import com.masiis.shop.dao.po.*;
import com.masiis.shop.web.platform.constants.SysConstants;
import com.masiis.shop.web.platform.controller.base.BaseController;
import com.masiis.shop.web.platform.service.order.BOrderService;
import com.masiis.shop.web.platform.service.product.ProductService;
import com.masiis.shop.web.platform.service.product.SkuAgentService;
import com.masiis.shop.web.platform.service.product.SkuService;
import com.masiis.shop.web.platform.service.user.UserAddressService;
import com.masiis.shop.web.platform.service.user.UserService;
import com.masiis.shop.web.platform.service.user.UserSkuService;
import com.masiis.shop.web.platform.utils.MobileMessageUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @autor jipengkun
 */
@Controller
@RequestMapping("/border")
public class BOrderController extends BaseController {

    private Logger log = Logger.getLogger(this.getClass());

    @Resource
    private SkuAgentService skuAgentService;
    @Resource
    private SkuService skuService;
    @Resource
    private UserService userService;
    @Resource
    private BOrderService bOrderService;
    @Resource
    private UserSkuService userSkuService;
    @Resource
    private UserAddressService userAddressService;
    @Resource
    private ComSkuImageMapper comSkuImageMapper;

    /**
     * 用户确认生成订单
     *
     * @author ZhaoLiang
     * @date 2016/3/8 12:50
     */
    @ResponseBody
    @RequestMapping("/addBOrder.do")
    public String addBOrder(HttpServletRequest request,
                            HttpServletResponse response,
                            @RequestParam(value = "realName", required = true) String realName,
                            @RequestParam(value = "weixinId", required = true) String weixinId,
                            @RequestParam(value = "skuId", required = true) Integer skuId,
                            @RequestParam(value = "levelId", required = true) Integer levelId,
                            @RequestParam(value = "parentUserId", required = true) Long parentUserId) {
        JSONObject obj = new JSONObject();
        try {
            if (StringUtils.isBlank(realName)) {
                throw new BusinessException("名称不能为空");
            }
            if (StringUtils.isBlank(weixinId)) {
                throw new BusinessException("微信号不能为空");
            }
            if (levelId <= 0) {
                throw new BusinessException("代理等级有误");
            }
            ComUser pUser = userService.getUserById(parentUserId);
            if (pUser == null) {
                throw new BusinessException("您的推荐人还未注册，请联系您的推荐人先注册");
            } else {
                PfUserSku pfUserSku = userSkuService.getUserSkuByUserIdAndSkuId(pUser.getId(), skuId);
                if (pfUserSku == null) {
                    throw new BusinessException("您的推荐人还未代理此款商品");
                } else {
                    if (pfUserSku.getAgentLevelId() >= levelId) {
                        throw new BusinessException("您的代理等级只能低于您的推荐人代理等级");
                    }
                }
            }
            //处理用户数据
            ComUser comUser = (ComUser) request.getSession().getAttribute("comUser");
            comUser.setRealName(realName);
            comUser.setWxId(weixinId);
            PfSkuAgent pfSkuAgent = skuAgentService.getBySkuIdAndLevelId(skuId, levelId);
            ComSku comSku = skuService.getSkuById(skuId);
            //折扣后单价
            BigDecimal unitPrice = comSku.getPriceRetail().multiply(pfSkuAgent.getDiscount());
            //折扣后总价
            BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(pfSkuAgent.getQuantity()));
            //处理订单数据
            PfBorder order = new PfBorder();
            order.setCreateTime(new Date());
            order.setCreateMan(comUser.getId());
            String orderCode = OrderMakeUtils.makeOrder("B");
            order.setOrderCode(orderCode);
            order.setUserMassage("");
            order.setUserId(comUser.getId());
            order.setUserPid(0l);
            order.setSupplierId(0);
            order.setReceivableAmount(totalPrice);
            order.setOrderAmount(totalPrice);//运费到付，商品总价即订单总金额
            order.setProductAmount(totalPrice);
            order.setShipAmount(BigDecimal.ZERO);
            order.setPayAmount(BigDecimal.ZERO);
            order.setShipType(0);
            order.setOrderStatus(0);
            order.setShipStatus(0);
            order.setPayStatus(0);
            order.setIsShip(0);
            order.setIsReplace(0);
            order.setIsReceipt(0);
            //处理订单商品数据
            List<PfBorderItem> orderItems = new ArrayList<>();
            PfBorderItem pfBorderItem = new PfBorderItem();
            pfBorderItem.setCreateTime(new Date());
            pfBorderItem.setSpuId(comSku.getSpuId());
            pfBorderItem.setSkuId(comSku.getId());
            pfBorderItem.setSkuName(comSku.getName());
            pfBorderItem.setQuantity(pfSkuAgent.getQuantity());
            pfBorderItem.setOriginalPrice(comSku.getPriceRetail());
            pfBorderItem.setUnitPrice(unitPrice);
            pfBorderItem.setTotalPrice(totalPrice);
            pfBorderItem.setIsComment(0);
            pfBorderItem.setIsReturn(0);
            orderItems.add(pfBorderItem);
            //处理用户sku关系数据
            PfUserSku pfUserSku = userSkuService.getUserSkuByUserIdAndSkuId(parentUserId, comSku.getId());//获取上级代理ID
            PfUserSku userSku = new PfUserSku();
            userSku.setCreateTime(new Date());
            if (pfUserSku == null) {
                userSku.setPid(0);
            } else {
                userSku.setPid(pfUserSku.getId());
            }
            userSku.setCode("");
            userSku.setUserId(comUser.getId());
            userSku.setSkuId(comSku.getId());
            userSku.setAgentLevelId(levelId);
            userSku.setIsPay(0);
            userSku.setIsCertificate(0);
            Long bOrderId = bOrderService.AddBOrder(order, orderItems, userSku, comUser);
            obj.put("isError", false);
            obj.put("bOrderId", bOrderId);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            obj.put("isError", true);
            obj.put("message", ex.getMessage());
        }
        return obj.toJSONString();
    }

    /**
     * 合伙人支付
     *
     * @author ZhaoLiang
     * @date 2016/3/5 16:32
     */
    @RequestMapping("/payBOrder.shtml")
    public ModelAndView payBOrder(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam(value = "userAddressId", required = false) Long userAddressId,
                                     @RequestParam(value = "userMessage", required = false) String userMessage,
                                     @RequestParam(value = "bOrderId", required = false) Long bOrderId
    ) {
        ModelAndView mv = new ModelAndView();
        String skuImg = PropertiesUtils.getStringValue("index_product_220_220_url");
        PfBorder pfBorder = bOrderService.getPfBorderById(bOrderId);
        List<PfBorderItem> pfBorderItems = bOrderService.getPfBorderItemByOrderId(bOrderId);
        StringBuffer stringBuffer = new StringBuffer();
        int sumQuantity = 0;
        for (PfBorderItem pfBorderItem : pfBorderItems) {
            ComSkuImage comSkuImage = comSkuImageMapper.selectDefaultImgBySkuId(pfBorderItem.getSkuId());
            stringBuffer.append("<section class=\"sec2\" >");
            stringBuffer.append("<p class=\"photo\" >");
            stringBuffer.append("<img src = '" + skuImg + comSkuImage.getImgUrl() + "' alt = \"\" >");
            stringBuffer.append("</p>");
            stringBuffer.append("<div>");
            stringBuffer.append("<h2> " + pfBorderItem.getSkuName() + "'</h2>");
            stringBuffer.append("<h3 ></h3>");
            stringBuffer.append("<p ><span> ￥" + pfBorderItem.getUnitPrice() + " </span ><b style = \"float:right; margin-right:10px;font-size:12px;\" > x" + pfBorderItem.getQuantity() + " </b ></p >");
            stringBuffer.append("</div>");
            stringBuffer.append("</section>");
            sumQuantity += pfBorderItem.getQuantity();
        }

        //获得地址
        ComUser comUser = (ComUser) request.getSession().getAttribute("comUser");
        Long userId = null;
        if (comUser != null) {
            userId = comUser.getId();
        } else {
            userId = 1L;
        }
        ComUserAddress comUserAddress = userAddressService.getOrderAddress(request, userAddressId, userId);
        if (comUserAddress != null) {
            request.getSession().setAttribute(SysConstants.SESSION_ORDER_SELECTED_ADDRESS, comUserAddress.getId());
        }
        mv.addObject("comUserAddress", comUserAddress);

        mv.addObject("bOrderId", bOrderId);
        mv.addObject("receivableAmount", pfBorder.getReceivableAmount());
        mv.addObject("orderAmount", pfBorder.getOrderAmount());
        mv.addObject("productInfo", stringBuffer.toString());
        mv.addObject("quantity", sumQuantity);
        mv.setViewName("platform/order/zhifu");

        return mv;
    }

    /**
     * 成功支付订单
     *
     * @author muchaofeng
     * @date 2016/3/9 15:06
     */
    @RequestMapping("/payBOrdersSuccess.shtml")
    public ModelAndView payBOrdersSuccess(HttpServletRequest request,
                                          @RequestParam(value = "bOrderId", required = true) Long bOrderId) throws Exception {
        ComUser comUser = (ComUser) request.getSession().getAttribute("comUser");
        OrderUserSku orderUserSku = new OrderUserSku();
        PfBorder pfBorder = bOrderService.getPfBorderById(bOrderId);
        List<PfBorderItem> pfBorderItem = bOrderService.getPfBorderItemByOrderId(bOrderId);
        List<String> skuNames = new ArrayList<>();
        Integer skuId = 0;
        for (PfBorderItem pforderItem : pfBorderItem) {
            skuNames.add(pforderItem.getSkuName());
            skuId = pforderItem.getSkuId();
        }
        ComUser userpId = userService.getUserById(pfBorder.getUserPid());
        ;
        if (userpId == null) {
            //上级姓名
            orderUserSku.setSuperiorName("");
        } else {
            //上级姓名
            orderUserSku.setSuperiorName(userpId.getRealName());
        }
        orderUserSku.setUserName(comUser.getRealName());
        //商品名字集合
        orderUserSku.setSkuName(skuNames);
        //获取用户商品信息
        PfUserSku pfUserSku = userSkuService.getUserSkuByUserIdAndSkuId(comUser.getId(), skuId);
        //获取用户代理等级
        ComAgentLevel comAgentLevel = bOrderService.findComAgentLevel(pfUserSku.getAgentLevelId());
        orderUserSku.setAgentLevel(comAgentLevel.getName());
        ModelAndView mav = new ModelAndView();
        mav.addObject("orderUserSku", orderUserSku);
        mav.addObject("userSkuId", pfUserSku.getId());
        mav.setViewName("platform/order/lingquzhengshu");
        return mav;
    }


}
