package com.masiis.shop.scheduler.platform.service.order;

import com.masiis.shop.common.enums.BOrder.BOrderShipStatus;
import com.masiis.shop.common.enums.BOrder.BOrderStatus;
import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.dao.platform.order.PfBorderItemMapper;
import com.masiis.shop.dao.platform.order.PfBorderMapper;
import com.masiis.shop.dao.platform.product.PfSkuStockMapper;
import com.masiis.shop.dao.platform.user.PfUserSkuStockMapper;
import com.masiis.shop.dao.po.*;
import com.masiis.shop.scheduler.platform.service.user.ComUserAccountService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * payBOrderService
 *
 * @author ZhaoLiang
 * @date 2016/3/30
 */
@Service
public class BOrderPayService {

    private Logger log = Logger.getLogger(this.getClass());
    @Resource
    private PfBorderMapper pfBorderMapper;
    @Resource
    private PfBorderItemMapper pfBorderItemMapper;
    @Resource
    private PfSkuStockMapper pfSkuStockMapper;
    @Resource
    private PfUserSkuStockMapper pfUserSkuStockMapper;
    @Resource
    private BOrderOperationLogService bOrderOperationLogService;
    @Resource
    private ComUserAccountService comUserAccountService;


    /**
     * 处理平台拿货类型订单
     *
     * @author ZhaoLiang
     * @date 2016/3/30 14:33
     * 操作详情：
     * <1>减少发货方库存 如果用户id是0操作平台库存
     * <2>增加收货方库存
     * <3>订单完成处理
     */
    private void saveBOrderSendType(PfBorder pfBorder) throws Exception {
        for (PfBorderItem pfBorderItem : pfBorderItemMapper.selectAllByOrderId(pfBorder.getId())) {
            log.info("<1>减少发货方库存和冻结库存 如果用户id是0操作平台库存");
            if (pfBorder.getUserPid() == 0) {
                PfSkuStock pfSkuStock = pfSkuStockMapper.selectBySkuId(pfBorderItem.getSkuId());
                //减少平台库存
                pfSkuStock.setStock(pfSkuStock.getStock() - pfBorderItem.getQuantity());
                //减少冻结库存
                pfSkuStock.setFrozenStock(pfSkuStock.getFrozenStock() - pfBorderItem.getQuantity());
                if (pfSkuStockMapper.updateByIdAndVersion(pfSkuStock) != 1) {
                    throw new BusinessException("减少平台库存失败");
                }
            } else {
                PfUserSkuStock parentSkuStock = pfUserSkuStockMapper.selectByUserIdAndSkuId(pfBorder.getUserPid(), pfBorderItem.getSkuId());
                //减少上级合伙人库存
                parentSkuStock.setStock(parentSkuStock.getStock() - pfBorderItem.getQuantity());
                //减少上级合伙人冻结库存
                parentSkuStock.setFrozenStock(parentSkuStock.getFrozenStock() - pfBorderItem.getQuantity());
                if (pfUserSkuStockMapper.updateByIdAndVersion(parentSkuStock) != 1) {
                    throw new BusinessException("减少上级合伙人平台库存失败");
                }
            }
            log.info("<2>增加收货方库存");
            PfUserSkuStock pfUserSkuStock = pfUserSkuStockMapper.selectByUserIdAndSkuId(pfBorder.getUserId(), pfBorderItem.getSkuId());
            //如果还没有库存信息直接初始化库存
            pfUserSkuStock.setStock(pfUserSkuStock.getStock() + pfBorderItem.getQuantity());
            if (pfUserSkuStockMapper.updateByIdAndVersion(pfUserSkuStock) != 1) {
                throw new BusinessException("增加用户平台库存失败");
            }
        }
        log.info("<3>订单完成处理");
        pfBorder.setShipStatus(BOrderShipStatus.Receipt.getCode());
        pfBorder.setShipTime(new Date());
        pfBorder.setIsShip(1);
        completeBOrder(pfBorder);
    }

    /**
     * 订单完成处理统一入口
     *
     * @author ZhaoLiang
     * @date 2016/4/9 11:22
     */
    @Transactional
    public void completeBOrder(PfBorder pfBorder) throws Exception {
        if (pfBorder == null) {
            throw new BusinessException("订单为空对象");
        }
        if (pfBorder.getPayStatus() != 1) {
            throw new BusinessException("订单还未支付怎么能完成呢？");
        }
        //拿货方式(0未选择1平台代发2自己发货)
        if (pfBorder.getSendType() == 1) {
            if (!pfBorder.getOrderStatus().equals(BOrderStatus.accountPaid.getCode())) {
                throw new BusinessException("订单状态异常:" + pfBorder.getOrderStatus() + ",应是" + BOrderStatus.accountPaid.getCode());
            }
        } else if (pfBorder.getSendType() == 2) {
            if (!pfBorder.getOrderStatus().equals(BOrderStatus.Ship.getCode())) {
                throw new BusinessException("订单状态异常:" + pfBorder.getOrderStatus() + ",应是" + BOrderStatus.Ship.getCode());
            }
        } else {
            throw new BusinessException("订单拿货方式异常");
        }
        pfBorder.setOrderStatus(BOrderStatus.Complete.getCode());//订单完成
        pfBorder.setShipStatus(BOrderShipStatus.Receipt.getCode());//已收货
        pfBorder.setIsReceipt(1);
        pfBorder.setReceiptTime(new Date());//收货时间
        pfBorderMapper.updateById(pfBorder);
        //添加订单日志
        bOrderOperationLogService.insertBOrderOperationLog(pfBorder, "订单完成");
        //订单类型(0代理1补货2拿货)
        if (pfBorder.getOrderType() == 0 || pfBorder.getOrderType() == 1) {
            comUserAccountService.countingByOrder(pfBorder);
        }
    }
}
