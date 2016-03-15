package com.masiis.shop.web.platform.controller.pay.wxpay;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.dao.po.ComUser;
import com.masiis.shop.dao.po.PfBorder;
import com.masiis.shop.web.platform.beans.pay.wxpay.UnifiedOrderReq;
import com.masiis.shop.web.platform.beans.pay.wxpay.UnifiedOrderRes;
import com.masiis.shop.web.platform.beans.pay.wxpay.WxPaySysParamReq;
import com.masiis.shop.web.platform.constants.SysConstants;
import com.masiis.shop.web.platform.constants.WxConstants;
import com.masiis.shop.web.platform.controller.base.BaseController;
import com.masiis.shop.web.platform.service.order.BOrderService;
import com.masiis.shop.web.platform.service.order.COrderService;
import com.masiis.shop.web.platform.service.pay.wxpay.WxPayService;
import com.masiis.shop.web.platform.service.user.UserService;
import com.masiis.shop.web.platform.utils.HttpsRequest;
import com.masiis.shop.web.platform.utils.WXBeanUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

/**
 * Created by lzh on 2016/3/9.
 */
@Controller
@RequestMapping("/wxpay")
public class WxPayController extends BaseController{
    private Logger log = Logger.getLogger(this.getClass());

    @Resource
    private WxPayService wxPayService;
    @Resource
    private UserService userService;

    @RequestMapping("/wtpay")
    public String wxpayPage(HttpServletRequest request, String param) {
        String ip = getIpAddr(request);
        WxPaySysParamReq req = null;
        ComUser user = null;
        try{
            if(StringUtils.isBlank(param)){
                // 跳转错误页面,暂跳首页
                throw new BusinessException("参数错误,param为空!");
            }
            req = JSONObject.parseObject(param, WxPaySysParamReq.class);
            if(req == null
                    || StringUtils.isBlank(req.getOrderId())
                    || StringUtils.isBlank(req.getSign())
                    || StringUtils.isBlank(req.getNonceStr())
                    || StringUtils.isBlank(req.getSignType())){
                // 参数有空值
                // 跳转错误页面,暂跳首页
                throw new BusinessException("参数错误,有部分参数为空!");
            }
            if(!WXBeanUtils.toSignString(req).equals(req.getSign())){
                throw new BusinessException("签名错误");
            }

            user = (ComUser) request.getSession().getAttribute(SysConstants.SESSION_LOGIN_USER_NAME);
        } catch (Exception e) {
            log.error("" + e.getMessage());
            return "redirect:/index";
        }

        // 处理业务
        String url = request.getRequestURL().toString();
        // 组织微信预付订单参数对象,并生成签名
        HttpsRequest h = new HttpsRequest();
        UnifiedOrderReq uniOrder = wxPayService.createUniFiedOrder(req, user, ip);
        XStream xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        // 生成预付订单参数签名
        String res = null;
        try {
            uniOrder.setSign(WXBeanUtils.toSignString(uniOrder));
            // 微信下预付订单,并获取预付订单号
            res = h.sendPost(WxConstants.WX_PAY_URL_UNIORDER_NOTIFY, uniOrder);
            log.info("wxpayPage:下预付单响应成功,response:" + res);

            xStream.processAnnotations(UnifiedOrderRes.class);
            UnifiedOrderRes resObj = (UnifiedOrderRes) xStream.fromXML(res);
            if(resObj == null || StringUtils.isBlank(resObj.getReturn_code())){
                throw new BusinessException("网络错误");
            }
            if(!"SUCCESS".equals(resObj.getReturn_code())){
                // 通信错误,如参数格式错误,签名错误
                throw new BusinessException(resObj.getReturn_msg());
            }
            if(!"SUCCESS".equals(resObj.getResult_code())){
                // 业务错误,可以做一些操作
                throw new BusinessException(resObj.getErr_code_des());
            }
            // 预付单下单成功
            log.info("wxpayPage:预付单下单成功");
            // 创建支付记录
            if("B".equals(String.valueOf(uniOrder.getOut_trade_no().charAt(0)))){

            }
        } catch (Exception e) {
            log.error("wxpayPage:下预付单失败," + e.getMessage());
        }

        // 组织微信支付请求参数,并形成签名

        // 获取成功支付后的跳转页面url


        /*////// 检查jsapi_ticket和access_token有效性,次access_token不是微信网页授权access_token

        ////// 组织页面wx.config参数,并形成签名
        发现可以不用这种方式实现*/
        return "pay/wxpay/wxpayPage";
    }
}
