package com.masiis.shop.web.platform.service.order;

import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.common.util.OrderMakeUtils;
import com.masiis.shop.dao.platform.order.*;
import com.masiis.shop.dao.platform.product.ComSkuMapper;
import com.masiis.shop.dao.platform.product.PfSkuAgentMapper;
import com.masiis.shop.dao.platform.product.PfSkuStatisticMapper;
import com.masiis.shop.dao.platform.product.PfSkuStockMapper;
import com.masiis.shop.dao.platform.user.ComUserAccountMapper;
import com.masiis.shop.dao.platform.user.ComUserMapper;
import com.masiis.shop.dao.platform.user.PfUserSkuMapper;
import com.masiis.shop.dao.platform.user.PfUserSkuStockMapper;
import com.masiis.shop.dao.po.*;
import com.masiis.shop.web.platform.constants.SysConstants;
import com.masiis.shop.web.platform.service.product.SkuAgentService;
import com.masiis.shop.web.platform.service.product.SkuService;
import com.masiis.shop.web.platform.service.user.UserSkuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ZhaoLiang on 2016/3/2.
 */
@Service
public class BOrderService {
    @Resource
    private PfBorderMapper pfBorderMapper;
    @Resource
    private PfBorderItemMapper pfBorderItemMapper;
    @Resource
    private PfBorderConsigneeMapper pfBorderConsigneeMapper;
    @Resource
    private PfBorderPaymentMapper pfBorderPaymentMapper;
    @Resource
    private PfUserSkuMapper pfUserSkuMapper;
    @Resource
    private ComUserMapper comUserMapper;
    @Resource
    private PfBorderOperationLogMapper pfBorderOperationLogMapper;
    @Resource
    private PfUserSkussMapper pfUserSkussMapper;
    @Resource
    private ComAgentLevelsMapper comAgentLevelsMapper;
    @Resource
    private PfSkuStatisticMapper pfSkuStatisticMapper;
    @Resource
    private PfSkuStockMapper pfSkuStockMapper;
    @Resource
    private PfUserSkuStockMapper pfUserSkuStockMapper;
    @Resource
    private PfBorderFreightMapper pfBorderFreightMapper;
    @Resource
    private SkuService skuService;
    @Resource
    private ComUserAccountMapper comUserAccountMapper;
    @Resource
    private PfSkuAgentMapper pfSkuAgentMapper;

    /**
     * 添加订单
     *
     * @param pfBorder
     * @param pfBorderItems
     */
    @Transactional
    public Long AddBOrder(PfBorder pfBorder, List<PfBorderItem> pfBorderItems, PfUserSku pfUserSku, ComUser comUser) throws Exception {
        if (pfBorder == null) {
            throw new BusinessException("pfBorder为空");
        }
        if (pfBorderItems == null || pfBorderItems.size() == 0) {
            throw new BusinessException("pfBorderItems为空");
        }
        if (comUser == null) {
            throw new BusinessException("comUser为空");
        }
        //添加订单
        pfBorderMapper.insert(pfBorder);
        //添加订单商品
        for (PfBorderItem pfBorderItem : pfBorderItems) {
            pfBorderItem.setPfBorderId(pfBorder.getId());
            pfBorderItemMapper.insert(pfBorderItem);
        }
        //添加用户代理商品关系
        if (pfUserSku != null) {
            pfUserSku.setPfBorderId(pfBorder.getId());
            pfUserSkuMapper.insert(pfUserSku);
        }
        //完善用户信息
        comUserMapper.updateByPrimaryKey(comUser);
        //添加订单日志
        PfBorderOperationLog pfBorderOperationLog = new PfBorderOperationLog();
        pfBorderOperationLog.setCreateTime(new Date());
        pfBorderOperationLog.setPfBorderId(pfBorder.getId());
        pfBorderOperationLog.setCreateMan(comUser.getId());
        pfBorderOperationLog.setPfBorderStatus(0);
        pfBorderOperationLog.setRemark("新增订单");
        pfBorderOperationLogMapper.insert(pfBorderOperationLog);
        return pfBorder.getId();
    }


    /**
     * 添加补货订单
     *
     * @return 订单id
     * @author ZhaoLiang
     * @date 2016/3/22 15:44
     */
    @Transactional
    public Long addReplenishmentOrders(Long userId, Integer skuId, int quantity) throws Exception {
        PfUserSku pfUserSku = pfUserSkuMapper.selectByUserIdAndSkuId(userId, skuId);
        if (pfUserSku == null) {
            throw new BusinessException("您还没有代理过此商品，不能补货。");
        }
        Integer levelId = pfUserSku.getAgentLevelId();//代理等级
        Long pUserId = 0l;//上级代理用户id
        BigDecimal amount = BigDecimal.ZERO;//订单总金额
        Long rBOrderId = 0l;//返回生成的订单id
        //获取上级代理
        PfUserSku paremtUserSku = pfUserSkuMapper.selectByPrimaryKey(pfUserSku.getPid());
        if (paremtUserSku != null) {
            pUserId = paremtUserSku.getUserId();
        }
        PfSkuAgent pfSkuAgent = pfSkuAgentMapper.selectBySkuIdAndLevelId(skuId, levelId);
        ComSku comSku = skuService.getSkuById(skuId);
        amount = comSku.getPriceRetail().multiply(BigDecimal.valueOf(quantity)).multiply(pfSkuAgent.getDiscount());
        //处理订单数据
        PfBorder order = new PfBorder();
        order.setCreateTime(new Date());
        order.setCreateMan(userId);
        String orderCode = OrderMakeUtils.makeOrder("B");
        order.setOrderCode(orderCode);
        order.setUserMessage("");
        order.setUserId(userId);
        order.setUserPid(pUserId);
        order.setSupplierId(0);
        order.setReceivableAmount(amount);
        order.setOrderAmount(amount);//运费到付，商品总价即订单总金额
        order.setProductAmount(amount);
        order.setShipAmount(BigDecimal.ZERO);
        order.setPayAmount(BigDecimal.ZERO);
        order.setShipType(0);
        order.setOrderStatus(0);
        order.setShipStatus(0);
        order.setPayStatus(0);
        order.setIsShip(0);
        order.setIsReplace(0);
        order.setIsReceipt(0);
        order.setIsCounting(0);
        order.setRemark("补货订单");
        order.setOrderType(1);
        pfBorderMapper.insert(order);
        rBOrderId = order.getId();
        PfBorderItem pfBorderItem = new PfBorderItem();
        pfBorderItem.setCreateTime(new Date());
        pfBorderItem.setPfBorderId(rBOrderId);
        pfBorderItem.setSpuId(comSku.getSpuId());
        pfBorderItem.setSkuId(comSku.getId());
        pfBorderItem.setSkuName(comSku.getName());
        pfBorderItem.setQuantity(quantity);
        pfBorderItem.setOriginalPrice(comSku.getPriceRetail());
        pfBorderItem.setUnitPrice(comSku.getPriceRetail().multiply(pfSkuAgent.getDiscount()));
        pfBorderItem.setTotalPrice(comSku.getPriceRetail().multiply(pfSkuAgent.getDiscount()).multiply(BigDecimal.valueOf(quantity)));
        pfBorderItem.setIsComment(0);
        pfBorderItem.setIsReturn(0);
        pfBorderItemMapper.insert(pfBorderItem);
        return rBOrderId;
    }

    /**
     * 添加拿货订单
     * @param userId    用户id
     * @param skuId     商品id
     * @param quantity  拿货数量
     * @param message   用户留言
     * @return
     * @throws Exception
     */
    public Long addProductTake(Long userId, Integer skuId, int quantity, String message) throws Exception{
        PfUserSku pfUserSku = pfUserSkuMapper.selectByUserIdAndSkuId(userId, skuId);
        if (pfUserSku == null) {
            throw new BusinessException("您还没有代理过此商品，不能补货。");
        }
        ComUser comUser = comUserMapper.selectByPrimaryKey(userId);
        if (!comUser.getSendType().equals("1")){
            throw new BusinessException("发货方式不是平台代发，不能拿货");
        }
        Integer levelId = pfUserSku.getAgentLevelId();//代理等级
        Long pUserId = 0l;//上级代理用户id
        BigDecimal amount = BigDecimal.ZERO;//订单总金额
        Long rBOrderId = 0l;//返回生成的订单id
        //获取上级代理
        PfUserSku paremtUserSku = pfUserSkuMapper.selectByPrimaryKey(pfUserSku.getPid());
        if (paremtUserSku != null) {
            pUserId = paremtUserSku.getUserId();
        }
        PfSkuAgent pfSkuAgent = pfSkuAgentMapper.selectBySkuIdAndLevelId(skuId, levelId);
        ComSku comSku = skuService.getSkuById(skuId);
        amount = comSku.getPriceRetail().multiply(BigDecimal.valueOf(quantity)).multiply(pfSkuAgent.getDiscount());
        //处理订单数据
        PfBorder order = new PfBorder();
        order.setCreateTime(new Date());
        order.setCreateMan(userId);
        String orderCode = OrderMakeUtils.makeOrder("B");
        order.setOrderCode(orderCode);
        order.setUserMessage(message);
        order.setUserId(userId);
        order.setUserPid(pUserId);
        order.setSupplierId(0);
        order.setReceivableAmount(amount);
        order.setOrderAmount(amount);//运费到付，商品总价即订单总金额
        order.setProductAmount(amount);
        order.setShipAmount(BigDecimal.ZERO);
        order.setPayAmount(BigDecimal.ZERO);
        order.setShipType(0);
        order.setOrderStatus(1);    //已付款
        order.setShipStatus(0);
        order.setPayStatus(1);      //已付款
        order.setIsShip(0);
        order.setIsReplace(0);
        order.setIsReceipt(0);
        order.setIsCounting(0);
        order.setRemark("拿货订单");
        order.setOrderType(2);
        pfBorderMapper.insert(order);
        rBOrderId = order.getId();
        PfBorderItem pfBorderItem = new PfBorderItem();
        pfBorderItem.setCreateTime(new Date());
        pfBorderItem.setPfBorderId(rBOrderId);
        pfBorderItem.setSpuId(comSku.getSpuId());
        pfBorderItem.setSkuId(comSku.getId());
        pfBorderItem.setSkuName(comSku.getName());
        pfBorderItem.setQuantity(quantity);
        pfBorderItem.setOriginalPrice(comSku.getPriceRetail());
        pfBorderItem.setUnitPrice(comSku.getPriceRetail().multiply(pfSkuAgent.getDiscount()));
        pfBorderItem.setTotalPrice(comSku.getPriceRetail().multiply(pfSkuAgent.getDiscount()).multiply(BigDecimal.valueOf(quantity)));
        pfBorderItem.setIsComment(0);
        pfBorderItem.setIsReturn(0);
        pfBorderItemMapper.insert(pfBorderItem);
        //添加订单日志
        PfBorderOperationLog pfBorderOperationLog = new PfBorderOperationLog();
        pfBorderOperationLog.setCreateMan(order.getUserId());
        pfBorderOperationLog.setCreateTime(new Date());
        pfBorderOperationLog.setPfBorderStatus(1);
        pfBorderOperationLog.setPfBorderId(order.getId());
        pfBorderOperationLog.setRemark("订单已支付");
        pfBorderOperationLogMapper.insert(pfBorderOperationLog);

        PfSkuStock pfSkuStock = null;
        PfUserSkuStock pfUserSkuStock = null;
        //冻结sku库存 如果用户id是0 则为平台直接代理商扣减平台商品库存
        if (order.getUserPid() == 0) {
            pfSkuStock = pfSkuStockMapper.selectBySkuId(pfBorderItem.getSkuId());
            if (pfSkuStock.getStock() - pfSkuStock.getFrozenStock() < quantity) {
                order.setOrderStatus(6);//排队订单
                pfBorderMapper.updateById(order);
            }
            pfSkuStock.setFrozenStock(pfSkuStock.getFrozenStock() + quantity);
            if (pfSkuStockMapper.updateByIdAndVersion(pfSkuStock) == 0) {
                throw new BusinessException("并发修改库存失败");
            }
        }else {
            pfUserSkuStock = pfUserSkuStockMapper.selectByUserIdAndSkuId(order.getUserPid(), pfBorderItem.getSkuId());
            if (pfUserSkuStock.getStock() - pfUserSkuStock.getFrozenStock() < quantity) {
                order.setOrderStatus(6);//排队订单
                pfBorderMapper.updateById(order);
            }
            pfUserSkuStock.setFrozenStock(pfUserSkuStock.getFrozenStock() + quantity);
            if (pfUserSkuStockMapper.updateByIdAndVersion(pfUserSkuStock) == 0) {
                throw new BusinessException("并发修改库存失败");
            }
        }
        //初始化个人库存信息
        PfUserSkuStock SkuStock = pfUserSkuStockMapper.selectByUserIdAndSkuId(order.getUserId(), pfBorderItem.getSkuId());
        if (SkuStock == null){
            SkuStock = new PfUserSkuStock();
            SkuStock.setUserId(order.getUserId());
            SkuStock.setCreateTime(new Date());
            SkuStock.setSpuId(pfBorderItem.getSpuId());
            SkuStock.setSkuId(pfBorderItem.getSkuId());
            SkuStock.setStock(0);
            SkuStock.setFrozenStock(0);
            SkuStock.setVersion(0);
            pfUserSkuStockMapper.insert(SkuStock);
        }
        return rBOrderId;
    }

    /**
     * 修改订单
     *
     * @author ZhaoLiang
     * @date 2016/3/17 14:59
     */
    @Transactional
    public void updateBOrder(PfBorder pfBorder) throws Exception {
        pfBorderMapper.updateById(pfBorder);
    }

    @Transactional
    public void toPayBOrder(PfBorder pfBorder, PfBorderConsignee pfBorderConsignee) throws Exception {
        pfBorderMapper.updateById(pfBorder);
        PfBorderConsignee pbc = pfBorderConsigneeMapper.selectByBorderId(pfBorderConsignee.getPfBorderId());
        if (pbc != null) {
            pfBorderConsigneeMapper.deleteByOrderId(pfBorderConsignee.getPfBorderId());
        }
        pfBorderConsigneeMapper.insert(pfBorderConsignee);
    }

    /**
     * 合伙人订单支付回调
     *
     * @author ZhaoLiang
     * @date 2016/3/16 10:04
     * 操作详情：
     * <1>修改订单支付信息
     * <2>修改订单数据
     * <3>添加订单日志
     * <4>修改合伙人商品关系状态
     * <5>修改用户sku代理关系支付状态
     * <6>修改sku代理量
     * <7>冻结sku库存
     * <8>初始化个人库存信息
     * <9>更新上级用户资产 (暂不更新)
     */
    @Transactional
    public void payBOrder(PfBorderPayment pfBorderPayment, String outOrderId) throws Exception {
        //<1>修改订单支付信息
        pfBorderPayment.setOutOrderId(outOrderId);
        pfBorderPayment.setIsEnabled(1);//设置为有效
        pfBorderPaymentMapper.updateById(pfBorderPayment);
        BigDecimal payAmount = pfBorderPayment.getAmount();
        Long bOrderId = pfBorderPayment.getPfBorderId();
        //<2>修改订单数据
        PfBorder pfBorder = pfBorderMapper.selectByPrimaryKey(bOrderId);
        pfBorder.setReceivableAmount(pfBorder.getReceivableAmount().subtract(payAmount));
        pfBorder.setPayAmount(pfBorder.getPayAmount().add(payAmount));
        pfBorder.setPayTime(new Date());
        pfBorder.setPayStatus(1);//已付款
        pfBorder.setOrderStatus(1);//已付款
        pfBorderMapper.updateById(pfBorder);
        //<3>添加订单日志
        PfBorderOperationLog pfBorderOperationLog = new PfBorderOperationLog();
        pfBorderOperationLog.setCreateMan(pfBorder.getUserId());
        pfBorderOperationLog.setCreateTime(new Date());
        pfBorderOperationLog.setPfBorderStatus(1);
        pfBorderOperationLog.setPfBorderId(bOrderId);
        pfBorderOperationLog.setRemark("订单已支付");
        pfBorderOperationLogMapper.insert(pfBorderOperationLog);
        //<4>修改合伙人商品关系状态
        ComUser comUser = comUserMapper.selectByPrimaryKey(pfBorder.getUserId());
        if (comUser.getIsAgent() == 0) {
            comUser.setIsAgent(1);
            comUserMapper.updateByPrimaryKey(comUser);
        }
        //<5>修改用户sku代理关系支付状态
        PfUserSku pfUserSku = pfUserSkuMapper.selectByOrderId(bOrderId);
        if (pfUserSku != null) {
            pfUserSku.setIsPay(1);
            pfUserSkuMapper.updateByPrimaryKey(pfUserSku);
        }
        //**************维护商品信息******************
        PfSkuStatistic pfSkuStatistic = null;
        PfSkuStock pfSkuStock = null;
        PfUserSkuStock pfUserSkuStock = null;
        for (PfBorderItem pfBorderItem : pfBorderItemMapper.selectAllByOrderId(bOrderId)) {
            //<6>修改sku代理量
            pfSkuStatistic = pfSkuStatisticMapper.selectBySkuId(pfBorderItem.getSkuId());
            pfSkuStatistic.setAgentNum(pfSkuStatistic.getAgentNum() + 1);
            pfSkuStatisticMapper.updateById(pfSkuStatistic);
            //<7>冻结sku库存 如果用户id是0 则为平台直接代理商扣减平台商品库存
            if (pfBorder.getUserPid() == 0) {
                pfSkuStock = pfSkuStockMapper.selectBySkuId(pfBorderItem.getSkuId());
                if (pfSkuStock.getStock() - pfSkuStock.getFrozenStock() < pfBorderItem.getQuantity()) {
                    pfBorder.setOrderStatus(6);//排队订单
                    pfBorderMapper.updateById(pfBorder);
                }
                pfSkuStock.setFrozenStock(pfSkuStock.getFrozenStock() + pfBorderItem.getQuantity());
                if (pfSkuStockMapper.updateByIdAndVersion(pfSkuStock) == 0) {
                    throw new BusinessException("并发修改库存失败");
                }
            } else {
                pfUserSkuStock = pfUserSkuStockMapper.selectByUserIdAndSkuId(pfBorder.getUserPid(), pfBorderItem.getSkuId());
                if (pfUserSkuStock.getStock() - pfUserSkuStock.getFrozenStock() < pfBorderItem.getQuantity()) {
                    pfBorder.setOrderStatus(6);//排队订单
                    pfBorderMapper.updateById(pfBorder);
                }
                pfUserSkuStock.setFrozenStock(pfUserSkuStock.getFrozenStock() + pfBorderItem.getQuantity());
                if (pfUserSkuStockMapper.updateByIdAndVersion(pfUserSkuStock) == 0) {
                    throw new BusinessException("并发修改库存失败");
                }
            }
            //<8>初始化个人库存信息
            PfUserSkuStock defaultUserSkuStock = pfUserSkuStockMapper.selectByUserIdAndSkuId(pfBorder.getUserId(), pfBorderItem.getSkuId());
            if (defaultUserSkuStock == null) {
                defaultUserSkuStock = new PfUserSkuStock();
                defaultUserSkuStock.setCreateTime(new Date());
                defaultUserSkuStock.setUserId(pfBorder.getUserId());
                defaultUserSkuStock.setSpuId(pfBorderItem.getSpuId());
                defaultUserSkuStock.setSkuId(pfBorderItem.getSkuId());
                defaultUserSkuStock.setStock(0);
                defaultUserSkuStock.setFrozenStock(0);
                defaultUserSkuStock.setVersion(0);
                pfUserSkuStockMapper.insert(defaultUserSkuStock);
            }
            //<9>更新上级用户资产 (暂不更新)
//            if (pfBorder.getUserPid() != 0) {
//                int i = comUserAccountMapper.payBOrderToUpdateUserAccount(pfBorder.getUserPid(), pfBorder.getPayAmount());
//                if (i == 0) {
//                    throw new BusinessException("更新用户资产失败");
//                }
//            }
        }

    }


    /**
     * 更新出货库存
     *
     * @author muchaofeng
     * @date 2016/3/21 14:35
     */
    public void updateStock(PfBorder pfBorder, ComUser user) {
        PfUserSkuStock pfUserSkuStock = null;
        for (PfBorderItem pfBorderItem : pfBorderItemMapper.selectAllByOrderId(pfBorder.getId())) {
            pfUserSkuStock = pfUserSkuStockMapper.selectByUserIdAndSkuId(user.getId(), pfBorderItem.getSkuId());
            if (pfUserSkuStock != null) {
                if (pfUserSkuStock.getStock() - pfBorderItem.getQuantity() < 0) {
                    throw new BusinessException("当前库存不足！");
                } else {
                    pfUserSkuStock.setStock(pfUserSkuStock.getStock() - pfBorderItem.getQuantity());
                    if (pfUserSkuStockMapper.updateByIdAndVersion(pfUserSkuStock) == 0) {
                        throw new BusinessException("并发修改库存失败");
                    }
                }
            }
        }
    }

    /**
     * 更新进货库存
     *
     * @author muchaofeng
     * @date 2016/3/21 16:22
     */
    public void updateGetStock(PfBorder pfBorder, ComUser user) {
        PfUserSkuStock pfUserSkuStock = null;
        for (PfBorderItem pfBorderItem : pfBorderItemMapper.selectAllByOrderId(pfBorder.getId())) {
            pfUserSkuStock = pfUserSkuStockMapper.selectByUserIdAndSkuId(user.getId(), pfBorderItem.getSkuId());
            if (pfUserSkuStock != null) {
                pfUserSkuStock.setStock(pfUserSkuStock.getStock() + pfBorderItem.getQuantity());
                if (pfUserSkuStockMapper.updateByIdAndVersion(pfUserSkuStock) == 0) {
                    throw new BusinessException("并发修改库存失败");
                }
            }
        }
    }

    /**
     * 获取订单
     *
     * @author ZhaoLiang
     * @date 2016/3/9 11:07
     */
    public PfBorder getPfBorderById(Long id) {
        return pfBorderMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据订单号获取订单商品
     *
     * @author ZhaoLiang
     * @date 2016/3/9 11:45
     */
    public List<PfBorderItem> getPfBorderItemByOrderId(Long pfBorderId) {
        return pfBorderItemMapper.selectAllByOrderId(pfBorderId);
    }

    /**
     * 通过spuId分组，根据id查询
     * @param pfBorderId
     * @return
     */
    public List<PfBorderItem> getPfBorderItemGroupByspuId(Long pfBorderId) {
        return pfBorderItemMapper.selectPfBorderItemGroupByspuId(pfBorderId);
    }
    /**
     * 查用户商品关系表
     *
     * @author muchaofeng
     * @date 2016/3/9 18:12
     */
    public PfUserSku findPfUserSkuById(Long id) {
        return pfUserSkussMapper.selectPfUserSkusById(id);
    }

    /**
     * 获取合伙人等级
     *
     * @author muchaofeng
     * @date 2016/3/9 18:52
     */
    public ComAgentLevel findComAgentLevel(Integer id) {
        return comAgentLevelsMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据订单号获取订单
     *
     * @author muchaofeng
     * @date 2016/3/14 13:23
     */
    public PfBorder findByOrderCode(String orderId) {
        return pfBorderMapper.selectByOrderCode(orderId);
    }

    /**
     * 根据用户id获取订单
     *
     * @author muchaofeng
     * @date 2016/3/14 13:22
     */

    public List<PfBorder> findByUserId(Long UserId, Integer orderStatus, Integer shipStatus) {
        return pfBorderMapper.selectByUserId(UserId, orderStatus, shipStatus);
    }

    /**
     * 根据用户id获取出货订单
     *
     * @author muchaofeng
     * @date 2016/3/23 14:36
     */
    public List<PfBorder> findByUserPid(Long UserId, Integer orderStatus, Integer shipStatus) {
        return pfBorderMapper.selectByUserPid(UserId, orderStatus, shipStatus);
    }

    /**
     * 添加订单支付记录
     *
     * @author ZhaoLiang
     * @date 2016/3/16 12:42
     */
    @Transactional
    public void addBOrderPayment(PfBorderPayment pfBorderPayment) throws Exception {
        pfBorderPaymentMapper.insert(pfBorderPayment);
    }

    /**
     * 根据支付流水号查询支付记录
     *
     * @param paySerialNum
     * @return
     */
    public PfBorderPayment findOrderPaymentBySerialNum(String paySerialNum) {
        return pfBorderPaymentMapper.selectBySerialNum(paySerialNum);
    }

    /**
     * 根据订单号获取快递信息
     *
     * @author muchaofeng
     * @date 2016/3/16 15:21
     */
    public List<PfBorderFreight> findByPfBorderFreightOrderId(Long id) {
        return pfBorderFreightMapper.selectByBorderId(id);
    }

    /**
     * 根据订单号获取收货人信息
     *
     * @author muchaofeng
     * @date 2016/3/16 15:36
     */
    public PfBorderConsignee findpfBorderConsignee(Long id) {
        return pfBorderConsigneeMapper.selectByBorderId(id);
    }

    /**
     * 根据userId获取关系
     *
     * @author muchaofeng
     * @date 2016/3/21 17:37
     */
    public PfUserSku findPfUserSku(long userId, Integer skuId) {
        return pfUserSkuMapper.selectByUserIdAndSkuId(userId, skuId);
    }

    /**
     * 判断订单库存是否充足
     *
     * @author ZhaoLiang
     * @date 2016/3/18 14:25
     */
    public boolean checkBOrderStock(PfBorder pfBorder) {
        List<PfBorderItem> pfBorderItems = pfBorderItemMapper.selectAllByOrderId(pfBorder.getId());
        for (PfBorderItem pfBorderItem : pfBorderItems) {
            int n = skuService.checkSkuStock(pfBorderItem.getSkuId(), pfBorderItem.getQuantity(), pfBorder.getUserPid());
            if (n < 0) {
                return false;
            }
        }
        return true;
    }
}
