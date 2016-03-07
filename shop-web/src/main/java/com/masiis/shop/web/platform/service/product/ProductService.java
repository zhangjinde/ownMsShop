package com.masiis.shop.web.platform.service.product;

import com.masiis.shop.common.util.PropertiesUtils;
import com.masiis.shop.dao.beans.product.Product;
import com.masiis.shop.dao.platform.product.ComSkuImageMapper;
import com.masiis.shop.dao.platform.product.ComSpuMapper;
import com.masiis.shop.dao.platform.product.ProductMapper;
import com.masiis.shop.dao.po.ComAgentLevel;
import com.masiis.shop.dao.po.ComSkuImage;
import com.masiis.shop.dao.po.ComSpu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhaoLiang on 2016/3/2.
 */
@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Resource
    private ComSpuMapper comSpuMapper;
    @Resource
    private ComSkuImageMapper comSkuImageMapper;

    /**
      * @Author 贾晶豪
      * @Date 2016/3/5 0005 下午 2:30
      * 根据商品ID展示商品属性详情
      */
    public Product getSkuDetails(String skuId) throws Exception {
        Product product = productMapper.getSkuDetailsBySkuId(skuId);
        if (product != null && product.getName().length() > 40) {
            product.setName(product.getName().substring(0, 41) + "......");
        }
        if (product != null && product.getSlogan().length() > 50) {
            product.setSlogan(product.getSlogan().substring(0, 51) + "......");
        }
        List<ComSkuImage> skuImgList = productMapper.getSkuImgById(skuId);
        String productImgValue = PropertiesUtils.getStringValue("index_product_200_200_url");
        if (skuImgList != null && skuImgList.size() > 0) {
            for (ComSkuImage comSkuImage : skuImgList) {
                comSkuImage.setFullImgUrl(productImgValue + comSkuImage.getImgUrl());
            }
            product.setComSkuImages(skuImgList);
        }
        return product;
    }
    /**
      * @Author 贾晶豪
      * @Date 2016/3/5 0005 下午 2:30
      * 代理商折扣，基础数据
      */
    public String getDiscountByAgentLevel() throws Exception {
        String discountLevel = null;
        List<ComAgentLevel> comAgentLevels = productMapper.agentLevelDiscount();
        if (comAgentLevels != null && comAgentLevels.size() > 0) {
            discountLevel = comAgentLevels.get(0).getDiscount() + "-" + comAgentLevels.get(comAgentLevels.size()-1).getDiscount();
        }
        return discountLevel;
    }
    /**
     * 跳转到试用申请页
     * @author  hanzengzhi
     * @date  2016/3/5 16:19
     */
    public Product applyTrialToPageService(Integer skuId,Integer spuId){
        Product product = null;
        try {
            product = getSkuDetails(skuId.toString());
            if (product!=null){
                //获取运费
                ComSpu comSpu =  comSpuMapper.selectByPrimaryKey(spuId);
                if (comSpu!=null){
                    //获取默认图片
                    ComSkuImage comSkuImage = comSkuImageMapper.selectDefaultImgBySkuId(skuId);
                    List<ComSkuImage> comSkuImages = new ArrayList<ComSkuImage>();
                    comSkuImages.add(comSkuImage);
                    product.setShipAmount(comSpu.getShipAmount());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  product;
    }
    /**
     * 获取SKU简单数据
     * @param skuId
     * @return
     * @throws Exception
     */
    public ProductSimple getSkuSimple(Integer skuId) throws Exception {
        return productSimpleMapper.selectBySkuId(skuId);
    }
}
