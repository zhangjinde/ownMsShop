package com.masiis.shop.web.platform.service.order;

import com.masiis.shop.dao.beans.product.Product;
import com.masiis.shop.dao.platform.order.PfCorderMapper;
import com.masiis.shop.dao.po.ComUser;
import com.masiis.shop.dao.po.ComUserAddress;
import com.masiis.shop.dao.po.PfCorder;
import com.masiis.shop.dao.po.PfUserTrial;
import com.masiis.shop.web.platform.service.product.ProductService;
import com.masiis.shop.web.platform.service.user.UserAddressService;
import com.masiis.shop.web.platform.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhaoLiang on 2016/3/2.
 */
@Service
@Transactional
public class COrderService {

    @Resource
    private UserService userService;
    @Resource
    private UserAddressService userAddressService;
    @Resource
    private PfCorderMapper pfCorderMapper;
    @Resource
    private ProductService productService;
    @Resource
    private PfCorderService pfCorderService;

    /**
     * 试用申请
     * @author  hanzengzhi
     * @date  2016/3/5 15:14
     */
    public void trialApplyService(ComUser comUser,PfUserTrial pfUserTrial){
        try {
            //插入试用表
            //userService.insertUserTrial(pfUserTrial);
            //更新试用用户信息
           // userService.updateComUser(comUser);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 确认订单
     * @author  hanzengzhi
     * @date  2016/3/8 14:55
     */
    public Map<String,Object> confirmOrder(HttpServletRequest request, Long orderId, Long userId, Integer selectedAddressId){
        Map<String,Object> pfCorderMap = new HashMap<String, Object>();
        ComUserAddress comUserAddress =  userAddressService.getOrderAddress(request,selectedAddressId,userId);
        Product product = getProductDetail(request,orderId);
        pfCorderMap.put("comUserAddress",comUserAddress);
        pfCorderMap.put("product",product);
        return pfCorderMap;
    }

    /**
     * 获得订单产品信息
     * @param request
     * @param orderId
     * @return
     */
    public Product getProductDetail(HttpServletRequest request,Long orderId){
        //获得订单信息
        PfCorder pfCorder = queryPfCorderById(orderId);
        //获得商品信息
        Product product = null;
        if (pfCorder!=null){
            product = productService.applyTrialToPageService(pfCorder.getSkuId());
        }
        return product;
    }

    /**
     *
     * @author  hanzengzhi
     * @date  2016/3/8 15:45 
     */
    public PfCorder queryPfCorderById(Long id){
        return  pfCorderMapper.selectById(id);
    }

    /**
     * 判断用户是否使用过商品
     * @author  hanzengzhi
     * @date  2016/3/9 11:39
     */
    public PfCorder isApplyTrial(Long userId,Integer skuId){
        try{
            PfCorder pfCorder = new PfCorder();
            pfCorder.setUserId(userId);
            pfCorder.setSkuId(skuId);
            pfCorder.setOrderType(0);
            pfCorder = pfCorderService.trialCorder(pfCorder);
            return pfCorder;
        }catch (Exception e){
            e.getMessage();
        }
        return null;
    }
    /**
     * 根据订单编号查询试用订单
     *
     * @param orderCode
     * @return
     */
    public PfCorder findByOrderCode(String orderCode) {
        return pfCorderMapper.selectByOrderCode(orderCode);
    }

}
