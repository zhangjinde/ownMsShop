package com.masiis.shop.scheduler.service.order;

import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.common.util.DateUtil;
import com.masiis.shop.dao.platform.order.PfBorderItemMapper;
import com.masiis.shop.dao.platform.order.PfBorderMapper;
import com.masiis.shop.dao.platform.order.PfBorderOperationLogMapper;
import com.masiis.shop.dao.platform.user.PfUserSkuStockMapper;
import com.masiis.shop.dao.po.*;
import com.masiis.shop.scheduler.service.user.ComUserAccountService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by lzh on 2016/3/23.
 */
@Service
public class PfBorderService {
    private Logger log = Logger.getLogger(this.getClass());

    @Resource
    private PfBorderMapper borderMapper;
    @Resource
    private PfBorderOperationLogMapper logMapper;
    @Resource
    private ComUserAccountService comUserAccountService;
    @Resource
    private PfBorderItemMapper pfBorderItemMapper;
    @Resource
    private PfUserSkuStockMapper pfUserSkuStockMapper;


    public List<PfBorder> findListByStatusAndDate(Date expiraTime, Integer orderStatus, Integer payStatus) {
        log.info("查询创建时间大于:" + DateUtil.Date2String(expiraTime, "yyyy-MM-dd HH:mm:ss")
                + ",订单状态为:" + orderStatus + ",支付状态为:" + payStatus + "的订单");
        // 查询
        List<PfBorder> resList = borderMapper.selectByStatusAndDate(expiraTime, orderStatus, payStatus);
        if(resList == null || resList.size() == 0)
            return null;
        return resList;
    }

    /**
     * 取消未支付订单
     *
     * @param bOrder
     */
    @Transactional
    public void cancelUnPayBOrder(PfBorder bOrder) {
        try{
            // 重新根据id查询该订单
            bOrder = borderMapper.selectByPrimaryKey(bOrder.getId());
            // 检查订单状态的有效性
            if(bOrder.getOrderStatus() != 0){
                throw new BusinessException("订单状态不正确,订单号:" + bOrder.getOrderCode()
                        + ",当前订单状态为:" + bOrder.getOrderStatus());
            }
            if(bOrder.getPayStatus() != 0){
                throw new BusinessException("订单支付状态不正确,订单号:" + bOrder.getOrderCode()
                        + ",当前订单支付状态为:" + bOrder.getPayStatus());
            }
            log.info("订单状态和支付状态校验通过!");
            // 修改订单的状态为已取消状态
            int result = borderMapper.updateOrderCancelById(bOrder.getId());
            if(result != 1){
                bOrder = borderMapper.selectByPrimaryKey(bOrder.getId());
                throw new BusinessException("订单取消失败,订单此时状态为:" + bOrder.getOrderStatus()
                        + ",支付状态为:" + bOrder.getPayStatus());
            }
            // 插入订单操作记录
            PfBorderOperationLog oLog = new PfBorderOperationLog();
            oLog.setCreateMan(0L);
            oLog.setCreateTime(new Date());
            oLog.setPfBorderId(bOrder.getId());
            // 取消状态
            oLog.setPfBorderStatus(2);
            oLog.setRemark("超过72小时未支付,系统自动取消");
            logMapper.insert(oLog);
        } catch (Exception e) {
            log.error("订单超72小时未支付订单取消失败," + e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 订单发货7天后自动收货
     *
     * @param bOrder
     */
    @Transactional
    public void confirmOrderReceive(PfBorder bOrder) {
        ComUser user = null;
        // 3是已完成状态
        Integer orderStatus = 3;
        Integer shipStatus = 9;
        if (bOrder.getSendType() == 1) {//平台代发
            if (bOrder.getOrderType() == 2) {//拿货
                bOrder.setOrderStatus(orderStatus);
                bOrder.setShipStatus(shipStatus);
                updateGetStock(bOrder, user);
                updateBOrder(bOrder);
                comUserAccountService.countingByOrder(bOrder);
            }
        } else if (bOrder.getSendType() == 1) {//自己发货
            bOrder.setOrderStatus(orderStatus);
            bOrder.setShipStatus(shipStatus);
            updateBOrder(bOrder);
            comUserAccountService.countingByOrder(bOrder);
        }
    }

    /**
     * 修改订单
     *
     * @author ZhaoLiang
     * @date 2016/3/17 14:59
     */
    @Transactional
    public void updateBOrder(PfBorder pfBorder) {
        borderMapper.updateById(pfBorder);
    }

    /**
     * 更新进货库存
     *
     * @author muchaofeng
     * @date 2016/3/21 16:22
     */
    @Transactional
    public void updateGetStock(PfBorder pfBorder, ComUser user) {
        PfUserSkuStock pfUserSkuStock = null;
        List<PfBorderItem> itemList = pfBorderItemMapper.selectAllByOrderId(pfBorder.getId());
        for (PfBorderItem pfBorderItem : itemList) {
            pfUserSkuStock = pfUserSkuStockMapper.selectByUserIdAndSkuId(user.getId(), pfBorderItem.getSkuId());
            if (pfUserSkuStock != null) {
                pfUserSkuStock.setStock(pfUserSkuStock.getStock() + pfBorderItem.getQuantity());
                if (pfUserSkuStockMapper.updateByIdAndVersion(pfUserSkuStock) == 0) {
                    throw new BusinessException("并发修改库存失败");
                }
            }
        }
    }
}
