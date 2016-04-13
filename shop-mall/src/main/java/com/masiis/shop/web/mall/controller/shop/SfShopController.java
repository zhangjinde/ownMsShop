package com.masiis.shop.web.mall.controller.shop;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.masiis.shop.common.util.ImageUtils;
import com.masiis.shop.common.util.OSSObjectUtils;
import com.masiis.shop.dao.mallBeans.SkuInfo;
import com.masiis.shop.dao.platform.user.ComUserMapper;
import com.masiis.shop.dao.po.ComSkuImage;
import com.masiis.shop.dao.po.ComUser;
import com.masiis.shop.web.mall.controller.base.BaseController;
import com.masiis.shop.web.mall.service.product.SkuService;
import com.masiis.shop.web.mall.service.shop.SfShopService;
import com.masiis.shop.web.mall.service.user.UserService;
import com.masiis.shop.web.mall.utils.DownloadImage;
import com.masiis.shop.web.mall.utils.qrcode.CreateParseCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date:2016/4/7
 * @auth:lzh
 */
@Controller
@RequestMapping("/shop")
public class SfShopController extends BaseController {

    private Log log = LogFactory.getLog(SfShopController.class);

    @Resource
    private ComUserMapper comUserMapper;
    @Resource
    private SfShopService sfShopService;
    @Resource
    private SkuService skuService;
    @Resource
    private UserService userService;

    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mav = new ModelAndView("mall/shop/index");

        return mav;
    }

    /**
     * 呐喊
     * @param request
     * @param response
     * @param shopId
     * @return
     */
    @RequestMapping("/shout")
    @ResponseBody
    public Object shout(HttpServletRequest request, HttpServletResponse response, Long shopId){

        try {
            ComUser comUser = getComUser(request);

            boolean result = sfShopService.shout(shopId, comUser.getId());

            return result;
        } catch (Exception e) {
            log.error("呐喊失败![shopId="+shopId+"][comUser="+getComUser(request)+"]");
            e.printStackTrace();
        }

        return false;
    }

    @RequestMapping("/getPoster")
    public ModelAndView getPoster(HttpServletRequest request, HttpServletResponse response, Long shopId){
        ModelAndView mav = new ModelAndView("mall/shop/exclusivePoster");

        try {
            ComUser comUser = getComUser(request);
                    comUser = comUserMapper.selectByPrimaryKey(comUser.getId());
            String realPath = request.getServletContext().getRealPath("/");
            String posterName = comUser.getId() + ".jpg";

            File posterDir = new File(realPath + "static/images/shop/poster/");
            if(!posterDir.exists()) posterDir.mkdirs();

            //二维码
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            String qrCodePath = posterDir.getAbsolutePath()+"/"+posterName;
            CreateParseCode.createCode(200, 200, basePath+"index?shopId="+shopId+"&userPid="+comUser.getId(), qrCodePath);

            //用户头像
            String headImgPath = posterDir.getAbsolutePath()+"/h-"+comUser.getId()+".jpg";
            DownloadImage.download(comUser.getWxHeadImg(), "h-"+comUser.getId()+".jpg", posterDir.getAbsolutePath());
            ImageUtils.scale2(headImgPath, headImgPath, 130, 130, false);

            //画专属海报
            String bgPath = realPath + "static/images/shop/background-img/bg-shop.png";
            String shopPosterPath = realPath + "static/images/shop/poster/shop-poster-"+comUser.getId()+".jpg";
            drawShopPoster(headImgPath, qrCodePath, bgPath, "我是"+comUser.getWxNkName(), shopPosterPath);

            mav.addObject("shopQRCode", "static/images/shop/poster/"+posterName);
            mav.addObject("userImg", "static/images/shop/poster/h-"+comUser.getId()+".jpg");
            mav.addObject("userName", comUser.getWxNkName());
            mav.addObject("bgShop", "static/images/shop/background-img/bg-shop.png");
            mav.addObject("shopPoster", basePath + "static/images/shop/poster/shop-poster-"+comUser.getId()+".jpg");
            return mav;
        } catch (Exception e) {
            log.error("获取专属海报失败![shopId=" + shopId + "][comUser=" + getComUser(request) + "]");
            e.printStackTrace();
        }

        mav.setViewName("error");
        return mav;
    }

    private void drawShopPoster(String headImgPath, String qrCodePath, String bgPath, String content, String shopPosterPath){
        ImageIcon headImgIcon = new ImageIcon(headImgPath);
        ImageIcon qrCodeIcon = new ImageIcon(qrCodePath);
        ImageIcon bgIcon = new ImageIcon(bgPath);
        Image headImage = headImgIcon.getImage();
        Image qrCodeImage = qrCodeIcon.getImage();
        Image bgImage = bgIcon.getImage();

        int width = bgImage.getWidth(null) == -1 ? 520 : bgImage.getWidth(null);
        int height = bgImage.getHeight(null) == -1 ? 710 : bgImage.getHeight(null);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();

        g.drawImage(headImage, 195, 130, null);
        g.drawImage(bgImage, 0, 0, null);
        g.drawImage(qrCodeImage, 160, 368, null);

        g.setFont(new Font("雅黑", Font.PLAIN, 28));
        g.setColor(new Color(247,60,140));
        g.drawString(content, 520/2-content.length()/2*28-(content.length()%2*14), 306);
        g.dispose();

        try {
            ImageIO.write(bufferedImage, "jpg", new File(shopPosterPath));
        } catch (Exception e) {
            log.error("画海报出错了!");
            e.printStackTrace();
            return;
        }
    }

    public static void main(String[] args){
        System.out.println(7%2);
    }

    @RequestMapping("/getSkuPoster")
    @ResponseBody
    public Object getSkuPoster(HttpServletRequest request, HttpServletResponse response, Long shopId, Integer skuId){
        //ModelAndView mav = new ModelAndView("mall/shop/skuPoster");

        try {
            ComUser comUser = getComUser(request);
            comUser = comUserMapper.selectByPrimaryKey(comUser.getId());
            String realPath = request.getServletContext().getRealPath("/");
            String posterName = comUser.getId() + "-" + shopId + "-" + skuId + ".jpg";

            File posterDir = new File(realPath + "static/images/shop/poster/");
            if(!posterDir.exists()) posterDir.mkdirs();

            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            CreateParseCode.createCode(300, 300, basePath+"shop/detail.shtml?shopId="+shopId+"&skuId="+skuId+"&fromUserId="+comUser.getId(), posterDir.getAbsolutePath()+"/"+posterName);
            DownloadImage.download(comUser.getWxHeadImg(), "h-"+comUser.getId()+".jpg", posterDir.getAbsolutePath());

            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("shopQRCode", "static/images/shop/poster/"+posterName);
            dataMap.put("userImg", "static/images/shop/poster/h-"+comUser.getId()+".jpg");
            dataMap.put("userName", comUser.getWxNkName());
            dataMap.put("skuName", skuService.getSkuById(skuId).getName());
            dataMap.put("skuImg", "static/images/shop/background-img/sku-"+skuId+".png");
            return dataMap;
        } catch (Exception e) {
            log.error("获取专属海报失败![shopId=" + shopId + "][skuId="+skuId+"][comUser=" + getComUser(request) + "]");
            e.printStackTrace();
        }

        //mav.setViewName("error");
        return "error";
    }

    @RequestMapping("/sharePlan")
    public String sharePlan(HttpServletRequest request, HttpServletResponse response, Model model, Integer shopId){
        model.addAttribute("shopId", shopId);

        return "mall/shop/sharePlan";
    }

    /**
     * @Author jjh
     * @Date 2016/4/8 0008 下午 5:50
     * 商品详情
     */
    @RequestMapping("/detail.shtml")
    public ModelAndView getSkuDetail(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam(value="skuId",required = true) Integer skuId,
                                     @RequestParam(value="shopId",required = true) Long shopId,
                                     @RequestParam(value="fromUserId",required = false) Long fromUserId) throws Exception {
        SkuInfo skuInfo = skuService.getSkuInfoBySkuId(shopId, skuId);
        List<ComSkuImage> comSkuImageList =  skuService.findComSkuImages(skuId);
        ComSkuImage comSkuImage = skuService.findDefaultComSkuImage(skuId);
        ComUser user = getComUser(request);
        ComUser fromUser = userService.getUserById(fromUserId);
        userService.getShareUser(user.getId(),fromUserId,shopId);//来自分享人的信息
        ModelAndView mav = new ModelAndView("/mall/shop/shop_product");
        mav.addObject("skuInfo", skuInfo);//商品信息
        mav.addObject("SkuImageList", comSkuImageList);//图片列表
        mav.addObject("defaultSkuImage", comSkuImage);//默认图片
        mav.addObject("shopId", shopId);
        mav.addObject("fromUser", fromUser);//分享链接人信息
        return mav;
    }

}
