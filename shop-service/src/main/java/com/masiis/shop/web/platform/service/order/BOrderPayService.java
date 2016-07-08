package com.masiis.shop.web.platform.service.order;

import com.masiis.shop.common.enums.platform.BOrderShipStatus;
import com.masiis.shop.common.enums.platform.BOrderStatus;
import com.masiis.shop.common.enums.platform.SkuStockLogType;
import com.masiis.shop.common.enums.platform.UserSkuStockLogType;
import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.common.util.DateUtil;
import com.masiis.shop.common.util.MobileMessageUtil;
import com.masiis.shop.common.util.OSSObjectUtils;
import com.masiis.shop.common.util.PropertiesUtils;
import com.masiis.shop.dao.mall.shop.SfShopMapper;
import com.masiis.shop.dao.mall.shop.SfShopSkuMapper;
import com.masiis.shop.dao.mall.user.SfUserRelationMapper;
import com.masiis.shop.dao.platform.order.PfBorderItemMapper;
import com.masiis.shop.dao.platform.order.PfBorderMapper;
import com.masiis.shop.dao.platform.order.PfBorderPaymentMapper;
import com.masiis.shop.dao.platform.product.ComAgentLevelMapper;
import com.masiis.shop.dao.platform.product.PfSkuStatisticMapper;
import com.masiis.shop.dao.platform.user.ComUserMapper;
import com.masiis.shop.dao.platform.user.PfUserCertificateMapper;
import com.masiis.shop.dao.platform.user.PfUserSkuMapper;
import com.masiis.shop.dao.po.*;
import com.masiis.shop.common.constant.platform.SysConstants;
import com.masiis.shop.web.platform.service.product.PfSkuStockService;
import com.masiis.shop.web.platform.service.product.PfUserSkuStockService;
import com.masiis.shop.web.common.service.SkuService;
import com.masiis.shop.web.platform.service.user.PfUserRecommendRelationService;
import com.masiis.shop.web.platform.service.user.PfUserStatisticsService;
import com.masiis.shop.web.common.utils.DrawPicUtil;
import com.masiis.shop.web.common.utils.wx.WxPFNoticeUtils;
import com.masiis.shop.web.mall.service.shop.SfShopStatisticsService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * 合伙订单支付回调处理类
 */
@Service
@Transactional
public class BOrderPayService {

    private Logger log = Logger.getLogger(this.getClass());
    @Resource
    private PfBorderMapper pfBorderMapper;
    @Resource
    private PfBorderItemMapper pfBorderItemMapper;
    @Resource
    private PfBorderPaymentMapper pfBorderPaymentMapper;
    @Resource
    private PfUserSkuMapper pfUserSkuMapper;
    @Resource
    private ComUserMapper comUserMapper;
    @Resource
    private PfSkuStatisticMapper pfSkuStatisticMapper;
    @Resource
    private PfUserCertificateMapper pfUserCertificateMapper;
    @Resource
    private ComAgentLevelMapper comAgentLevelMapper;
    @Resource
    private SfShopMapper sfShopMapper;
    @Resource
    private SfShopSkuMapper sfShopSkuMapper;
    @Resource
    private PfSupplierBankService supplierBankService;
    @Resource
    private BOrderOperationLogService bOrderOperationLogService;
    @Resource
    private PfSkuStockService pfSkuStockService;
    @Resource
    private PfUserSkuStockService pfUserSkuStockService;
    @Resource
    private SkuService skuService;
    @Resource
    private SfUserRelationMapper sfUserRelationMapper;
    @Resource
    private PfUserStatisticsService pfUserStatisticsService;
    @Resource
    private SfShopStatisticsService shopStatisticsService;
    @Resource
    private BOrderStatisticsService orderStatisticsService;
    @Resource
    private BOrderBillAmountService billAmountService;
    @Resource
    private PfUserRecommendRelationService pfUserRecommendRelationService;
    @Resource
    private PfBorderRecommenRewardService pfBorderRecommenRewardService;
    @Resource
    private BUpgradePayService upgradePayService;
    @Resource
    private BOrderPayEndMessageService bOrderPayEndMessageService;
    @Resource
    private BOrderShipService bOrderShipService;

    /**
     * 订单支付回调入口
     *
     * @param pfBorderPayment 订单支付信息表数据
     * @param outOrderId      第三方支付订单号
     * @param rootPath        项目相对路径用户获取数据
     * @throws Exception
     */
    public void mainPayBOrder(PfBorderPayment pfBorderPayment, String outOrderId, String rootPath) throws Exception {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:m:s");
//        System.out.println(dateFormat.format(new Date()));
        if (pfBorderPayment == null) {
            throw new BusinessException("pfBorderPayment为空");
        }
        if (pfBorderPayment.getIsEnabled() == 1) {
            throw new BusinessException("该支付记录已经被处理成功");
        }
        PfBorder pfBorder = pfBorderMapper.selectByPrimaryKey(pfBorderPayment.getPfBorderId());
        //处理支付逻辑 订单类型(0代理1补货2拿货)
        if (pfBorder.getOrderType() == 0) {
            payBOrderTypeI(pfBorderPayment, outOrderId, rootPath);
        } else if (pfBorder.getOrderType() == 1) {
            payBOrderTypeII(pfBorderPayment, outOrderId, rootPath);
        } else if (pfBorder.getOrderType() == 2) {
            payBOrderTypeIII(pfBorderPayment, outOrderId, rootPath);
        } else if (pfBorder.getOrderType() == 3) {
            upgradePayService.paySuccessCallBack(pfBorderPayment, outOrderId, rootPath);
        } else {
            throw new BusinessException("订单类型有误");
        }
        //支付完成推送消息(发送失败不回滚事务)
        try {
            bOrderPayEndMessageService.payEndPushMessage(pfBorderPayment);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
//        System.out.println(dateFormat.format(new Date()));
    }

    /**
     * 平台代发支付订单
     * <1>修改订单支付信息
     * <2>修改订单数据
     * <3>添加订单日志
     * <4>修改用户为已代理如果用户没有选择拿货方式更新用户的拿货方式为订单的拿货方式
     * <5>为用户生成小铺
     * <6>初始化分销关系(暂删除)
     * <7>初始化合伙推荐关系
     * <8>为小铺生成商品
     * <9>修改用户sku代理关系数据
     * <10>修改代理人数(如果是代理类型的订单增加修改sku代理人数)
     * <11>处理发货库存
     * <12>处理收货库存
     * <13>实时统计数据显示
     * <14>修改结算中数据
     *
     * @param pfBorderPayment
     * @param outOrderId
     * @param rootPath
     */
    private void payBOrderTypeI(PfBorderPayment pfBorderPayment, String outOrderId, String rootPath) {
        log.info("<1>修改订单支付信息");
        pfBorderPayment.setOutOrderId(outOrderId);
        pfBorderPayment.setIsEnabled(1);//设置为有效
        pfBorderPaymentMapper.updateById(pfBorderPayment);
        BigDecimal payAmount = pfBorderPayment.getAmount();
        Long bOrderId = pfBorderPayment.getPfBorderId();
        log.info("<2>修改订单数据");
        PfBorder pfBorder = pfBorderMapper.selectByPrimaryKey(bOrderId);
        if (pfBorder.getPayStatus() != BOrderStatus.NotPaid.getCode() && pfBorder.getPayStatus() != BOrderStatus.offLineNoPay.getCode()) {
            throw new BusinessException("订单号:" + pfBorder.getId() + ",已经支付成功.");
        }
        pfBorder.setReceivableAmount(pfBorder.getReceivableAmount().subtract(payAmount));
        pfBorder.setPayAmount(pfBorder.getPayAmount().add(payAmount));
        pfBorder.setPayTime(new Date());
        pfBorder.setPayStatus(1);
        pfBorder.setOrderStatus(BOrderStatus.WaitShip.getCode());//待发货
        pfBorderMapper.updateById(pfBorder);
        log.info("<3>添加订单日志");
        bOrderOperationLogService.insertBOrderOperationLog(pfBorder, "订单支付成功");
        log.info("<4>修改用户为已代理如果用户没有选择拿货方式更新用户的拿货方式为订单的拿货方式");
        ComUser comUser = comUserMapper.selectByPrimaryKey(pfBorder.getUserId());
        if (comUser.getIsAgent() == 0) {
            comUser.setIsAgent(1);
        }
        if (comUser.getSendType() == 0) {
            comUser.setSendType(pfBorder.getSendType());
        }
        comUserMapper.updateByPrimaryKey(comUser);
        log.info("<5>为用户生成小铺");
        SfShop sfShop = sfShopMapper.selectByUserId(comUser.getId());
        if (sfShop == null) {
            sfShop = new SfShop();
            sfShop.setCreateTime(new Date());
            sfShop.setUserId(comUser.getId());
            sfShop.setStatus(1);
            sfShop.setExplanation("主营各类化妆品、保健品");
            sfShop.setLogo(comUser.getWxHeadImg());
            sfShop.setName(comUser.getRealName() + "的小店");
            sfShop.setPageviews(0l);
            sfShop.setQrCode("");
            sfShop.setSaleAmount(BigDecimal.ZERO);
            sfShop.setShipType(1);//运费类型0：消费者出运费1：代理商出运费
            sfShop.setShipAmount(BigDecimal.ZERO);
            sfShop.setAgentShipAmount(new BigDecimal(8));
            sfShop.setShoutNum(0l);
            sfShop.setVersion(0l);
            sfShop.setRemark("");
            sfShopMapper.insert(sfShop);
        }
        SfShopStatistics shopStatistics = shopStatisticsService.selectByShopUserId(comUser.getId());
        if (shopStatistics == null) {
            shopStatistics = new SfShopStatistics();
            shopStatistics.setCreateTime(new Date());
            shopStatistics.setShopId(sfShop.getId());
            shopStatistics.setUserId(comUser.getId());
            shopStatistics.setIncomeFee(new BigDecimal(0));
            shopStatistics.setProfitFee(new BigDecimal(0));
            shopStatistics.setOrderCount(0);
            shopStatistics.setProductCount(0);
            shopStatistics.setPageviewsCount(0);
            shopStatistics.setShareCount(0);
            shopStatistics.setReturnOrderCount(0);
            shopStatistics.setVersion(0);
            shopStatistics.setRemark("");
            shopStatisticsService.insert(shopStatistics);
        }
        log.info("<6>初始化分销关系");
        SfUserRelation sfUserRelation = new SfUserRelation();
        sfUserRelation.setCreateTime(new Date());
        sfUserRelation.setUserPid(0l);
        sfUserRelation.setUserId(comUser.getId());
        sfUserRelation.setShopId(sfShop.getId());
        sfUserRelation.setIsBuy(0);
        sfUserRelation.setTreeLevel(1);
        sfUserRelation.setRemark("代理人初始分销关系");
        sfUserRelationMapper.insert(sfUserRelation);
        String sfUserRelation_treeCode = sfUserRelation.getId() + ",";
        sfUserRelationMapper.updateTreeCodeById(sfUserRelation.getId(), sfUserRelation_treeCode);
//                    sfUserRelation.setUserPid(0l);
//                    sfUserRelation.setRemark("代理人解除分销关系");
//                    int i = sfUserRelationMapper.updateByPrimaryKey(sfUserRelation);
//                    if (i != 1) {
//                        throw new BusinessException("分销关系修改失败");
//                    }
//                    Long id = sfUserRelation.getId();
//                    String treeCode = sfUserRelation.getTreeCode();
//                    Integer id_index = treeCode.indexOf(String.valueOf(id)) + 1;
//                    Integer treeLevel = sfUserRelation.getTreeLevel() - 1;
//                    i = sfUserRelationMapper.updateTreeCodes(treeCode, id_index, treeLevel);
//                    if (i <= 0) {
//                        throw new BusinessException("分销关系树结构修改失败");
//                    }
        for (PfBorderItem pfBorderItem : pfBorderItemMapper.selectAllByOrderId(bOrderId)) {
            log.info("<7>v1.2处理合伙推荐关系");
            PfUserRecommenRelation pfUserRecommenRelation = pfUserRecommendRelationService.selectRecommenRelationByUserIdAndSkuId(comUser.getId(), pfBorderItem.getSkuId());
            if (pfUserRecommenRelation == null) {
                PfBorderRecommenReward pfBorderRecommenReward = pfBorderRecommenRewardService.getByPfBorderItemId(pfBorderItem.getId());
                if (pfBorder.getUserPid() != 0 && pfBorderRecommenReward != null) {
                    PfUserRecommenRelation parentPfUserRecommenRelation = pfUserRecommendRelationService.selectRecommenRelationByUserIdAndSkuId(pfBorderRecommenReward.getRecommenUserId(), pfBorderItem.getSkuId());
                    pfUserRecommenRelation = new PfUserRecommenRelation();
                    pfUserRecommenRelation.setCreateTime(new Date());
                    pfUserRecommenRelation.setPid(parentPfUserRecommenRelation.getId());
                    pfUserRecommenRelation.setUserId(comUser.getId());
                    pfUserRecommenRelation.setUserPid(parentPfUserRecommenRelation.getUserId());
                    pfUserRecommenRelation.setSkuId(pfBorderItem.getSkuId());
                    pfUserRecommenRelation.setPfBorderId(bOrderId);
                    pfUserRecommenRelation.setTreeCode("");
                    pfUserRecommenRelation.setTreeLevel(parentPfUserRecommenRelation.getTreeLevel() + 1);
                    pfUserRecommenRelation.setRemark("绑定合伙人推荐关系");
                    pfUserRecommendRelationService.insert(pfUserRecommenRelation);
                    String treeCode = parentPfUserRecommenRelation.getTreeCode() + pfUserRecommenRelation.getId() + ",";
                    pfUserRecommendRelationService.updateTreeCodeById(pfUserRecommenRelation.getId(), treeCode);
                } else {
                    pfUserRecommenRelation = new PfUserRecommenRelation();
                    pfUserRecommenRelation.setCreateTime(new Date());
                    pfUserRecommenRelation.setPid(0);
                    pfUserRecommenRelation.setUserId(comUser.getId());
                    pfUserRecommenRelation.setUserPid(0l);
                    pfUserRecommenRelation.setSkuId(pfBorderItem.getSkuId());
                    pfUserRecommenRelation.setPfBorderId(bOrderId);
                    pfUserRecommenRelation.setTreeCode("");
                    pfUserRecommenRelation.setTreeLevel(1);
                    pfUserRecommenRelation.setRemark("初始化合伙人推荐关系");
                    pfUserRecommendRelationService.insert(pfUserRecommenRelation);
                    String treeCode = pfUserRecommenRelation.getId() + ",";
                    pfUserRecommendRelationService.updateTreeCodeById(pfUserRecommenRelation.getId(), treeCode);
                }
            }
            PfUserSku thisUS = pfUserSkuMapper.selectByUserIdAndSkuId(comUser.getId(), pfBorderItem.getSkuId());
            if (thisUS == null) {
                log.info("<8>为小铺生成商品");
                SfShopSku sfShopSku = sfShopSkuMapper.selectByShopIdAndSkuId(sfShop.getId(), pfBorderItem.getSkuId());
                if (sfShopSku == null) {
                    sfShopSku = new SfShopSku();
                    sfShopSku.setCreateTime(new Date());
                    sfShopSku.setShopId(sfShop.getId());
                    sfShopSku.setShopUserId(comUser.getId());
                    sfShopSku.setSpuId(pfBorderItem.getSpuId());
                    sfShopSku.setSkuId(pfBorderItem.getSkuId());
                    sfShopSku.setIsOwnShip(0);
                    sfShopSku.setIsSale(1);
                    sfShopSku.setAgentLevelId(pfBorderItem.getAgentLevelId());
                    sfShopSku.setBail(pfBorderItem.getBailAmount());
                    sfShopSku.setSaleNum(0l);
                    sfShopSku.setShareNum(0l);
                    sfShopSku.setQrCode("");
                    sfShopSku.setRemark("");
                    sfShopSkuMapper.insert(sfShopSku);
                }
                log.info("<9>修改用户sku代理关系数据");
                ComSku comSku = skuService.getSkuById(pfBorderItem.getSkuId());
                thisUS = new PfUserSku();
                thisUS.setCreateTime(new Date());
                thisUS.setCreateTime(new Date());
                PfUserSku parentUS = pfUserSkuMapper.selectByUserIdAndSkuId(pfBorder.getUserPid(), pfBorderItem.getSkuId());
                if (parentUS == null) {
                    thisUS.setPid(0);
                    thisUS.setUserPid(0L);
                    thisUS.setTreeLevel(1);
                } else {
                    thisUS.setPid(parentUS.getId());
                    thisUS.setUserPid(parentUS.getUserId());
                    thisUS.setTreeLevel(parentUS.getTreeLevel() + 1);
                }
                thisUS.setAgentNum(0l);
                thisUS.setUserId(pfBorder.getUserId());
                thisUS.setSkuId(pfBorderItem.getSkuId());
                thisUS.setAgentLevelId(pfBorderItem.getAgentLevelId());
                thisUS.setIsPay(1);
                thisUS.setIsCertificate(1);
                thisUS.setPfBorderId(pfBorder.getId());
                thisUS.setBail(pfBorder.getBailAmount());
                thisUS.setRewardUnitPrice(comSku.getRewardUnitPrice());
                thisUS.setRemark("");
                PfUserCertificate pfUserCertificate = new PfUserCertificate();
                pfUserCertificate.setCreateTime(new Date());
                pfUserCertificate.setUserId(pfBorder.getUserId());
                pfUserCertificate.setSpuId(pfBorderItem.getSpuId());
                pfUserCertificate.setSkuId(pfBorderItem.getSkuId());
                pfUserCertificate.setIdCard(comUser.getIdCard());
                pfUserCertificate.setMobile(comUser.getMobile());
                pfUserCertificate.setWxId(pfBorderItem.getWxId());
                Calendar calendar = Calendar.getInstance();
                pfUserCertificate.setBeginTime(calendar.getTime());
                calendar.set(Calendar.MONTH, 11);
                calendar.set(Calendar.DAY_OF_MONTH, 31);
                pfUserCertificate.setEndTime(calendar.getTime());
                pfUserCertificate.setAgentLevelId(pfBorderItem.getAgentLevelId());
                pfUserCertificate.setStatus(1);
                pfUserCertificate.setRemark("");
                String code = getCertificateCode(pfUserCertificate);
                thisUS.setCode(code);
                pfUserSkuMapper.insert(thisUS);
                String treeCode = "";
                if (parentUS == null) {
                    treeCode = thisUS.getSkuId() + "," + thisUS.getId() + ",";
                } else {
                    treeCode = parentUS.getTreeCode() + thisUS.getId() + ",";
                }
                pfUserSkuMapper.updateTreeCodeById(thisUS.getId(), treeCode);
                pfUserCertificate.setPfUserSkuId(thisUS.getId());
                pfUserCertificate.setCode(code);
                ComAgentLevel comAgentLevel = comAgentLevelMapper.selectByPrimaryKey(pfUserCertificate.getAgentLevelId());
                String newIdCard = comUser.getIdCard().substring(0, 4) + "**********" + comUser.getIdCard().substring(comUser.getIdCard().length() - 4, comUser.getIdCard().length());
                String picName = uploadFile(rootPath + "/static/images/certificate/" + comAgentLevel.getImgUrl(),//filePath - 原图的物理路径
                        rootPath + "/static/font/",//字体路径
                        pfUserCertificate.getCode(),//certificateCode - 证书编号
                        comUser.getRealName(),//userName - 用户名称
                        comAgentLevel.getName(),//levelName - 代理等级名称
                        pfBorderItem.getSkuName(),//skuName - 商品名称
                        comSku.geteName(),
                        newIdCard,//idCard - 身份证号
                        comUser.getMobile(),//mobile - 手机号
                        pfBorderItem.getWxId(),//wxId - 微信号
                        DateUtil.Date2String(pfUserCertificate.getBeginTime(), "yyyy-MM-dd", null),//beginDate - 开始日期
                        DateUtil.Date2String(pfUserCertificate.getEndTime(), "yyyy-MM-dd", null));//endDate - 结束日期
                pfUserCertificate.setImgUrl(picName + ".jpg");
                pfUserCertificateMapper.insert(pfUserCertificate);
            }
            log.info("<10>修改代理人数(如果是代理类型的订单增加修改sku代理人数)");
            if (pfBorder.getOrderType() == 0) {
                pfSkuStatisticMapper.updateAgentNumBySkuId(pfBorderItem.getSkuId());
            }
            log.info("<11>初始化库存");
            PfUserSkuStock pfUserSkuStock = pfUserSkuStockService.selectByUserIdAndSkuId(pfBorder.getUserId(), pfBorderItem.getSkuId());
            if (pfUserSkuStock == null) {
                pfUserSkuStock = new PfUserSkuStock();
                pfUserSkuStock.setCreateTime(new Date());
                pfUserSkuStock.setUserId(pfBorder.getUserId());
                pfUserSkuStock.setSpuId(pfBorderItem.getSpuId());
                pfUserSkuStock.setSkuId(pfBorderItem.getSkuId());
                pfUserSkuStock.setStock(0);
                pfUserSkuStock.setFrozenStock(0);
                pfUserSkuStock.setFrozenCustomStock(0);
                pfUserSkuStock.setCustomStock(0);
                pfUserSkuStock.setVersion(0);
                pfUserSkuStockService.insert(pfUserSkuStock);
            }
            log.info("<12>增加冻结库存");
            if (pfBorderItem.getQuantity() > 0) {
                if (pfBorder.getUserPid() == 0) {
                    PfSkuStock pfSkuStock = pfSkuStockService.selectBySkuId(pfBorderItem.getSkuId());
                    //如果可售库存不足或者排单开关打开的情况下 订单进入排单
                    if (pfSkuStock.getIsQueue() == 1 || pfSkuStock.getStock() - pfSkuStock.getFrozenStock() < pfBorderItem.getQuantity()) {
                        //平台库存不足，排单处理
                        pfBorder.setOrderStatus(BOrderStatus.MPS.getCode());//排队订单
                        pfBorderMapper.updateById(pfBorder);
                    }
                    //增加平台冻结库存
                    pfSkuStock.setFrozenStock(pfSkuStock.getFrozenStock() + pfBorderItem.getQuantity());
                    if (pfSkuStockService.updateByIdAndVersions(pfSkuStock) != 1) {
                        throw new BusinessException("(平台发货)排队订单增加冻结量失败");
                    }
                } else {
                    PfUserSkuStock parentSkuStock = pfUserSkuStockService.selectByUserIdAndSkuId(pfBorder.getUserPid(), pfBorderItem.getSkuId());
                    //上级合伙人库存不足，排单处理
                    if (pfBorder.getSendType() == 1 && (parentSkuStock.getStock() - parentSkuStock.getFrozenStock() < pfBorderItem.getQuantity())) {
                        pfBorder.setOrderStatus(BOrderStatus.MPS.getCode());//排队订单
                        pfBorderMapper.updateById(pfBorder);
                    }
                    //增加平台冻结库存
                    parentSkuStock.setFrozenStock(parentSkuStock.getFrozenStock() + pfBorderItem.getQuantity());
                    if (pfUserSkuStockService.updateByIdAndVersions(parentSkuStock) != 1) {
                        throw new BusinessException("(代理发货)排队订单增加冻结量失败");
                    }
                }
            }
        }
        log.info("<13>实时统计数据显示");
        orderStatisticsService.statisticsOrder(pfBorder.getId());
        log.info("<14>修改结算中数据");
        billAmountService.orderBillAmount(pfBorder.getId());
        //拿货方式(0未选择1平台代发2自己发货)
        if (pfBorder.getSendType() == 1 && pfBorder.getOrderStatus() == BOrderStatus.WaitShip.getCode()) {
            //处理平台发货类型订单
            bOrderShipService.shipAndReceiptBOrder(pfBorder);
        }
    }

    /**
     * 自己拿货支付订单
     * <1>修改订单支付信息
     * <2>修改订单数据
     * <3>添加订单日志
     * <4>处理发货库存
     * <5>实时统计数据显示
     * <6>修改结算中数据
     */
    private void payBOrderTypeII(PfBorderPayment pfBorderPayment, String outOrderId, String rootPath) {
        log.info("<1>修改订单支付信息");
        pfBorderPayment.setOutOrderId(outOrderId);
        pfBorderPayment.setIsEnabled(1);//设置为有效
        pfBorderPaymentMapper.updateById(pfBorderPayment);
        BigDecimal payAmount = pfBorderPayment.getAmount();
        Long bOrderId = pfBorderPayment.getPfBorderId();
        log.info("<2>修改订单数据");
        PfBorder pfBorder = pfBorderMapper.selectByPrimaryKey(bOrderId);
        if (pfBorder.getPayStatus() != BOrderStatus.NotPaid.getCode() && pfBorder.getPayStatus() != BOrderStatus.offLineNoPay.getCode()) {
            throw new BusinessException("订单号:" + pfBorder.getId() + ",已经支付成功.");
        }
        pfBorder.setReceivableAmount(pfBorder.getReceivableAmount().subtract(payAmount));
        pfBorder.setPayAmount(pfBorder.getPayAmount().add(payAmount));
        pfBorder.setPayTime(new Date());
        pfBorder.setPayStatus(1);
        pfBorder.setOrderStatus(BOrderStatus.WaitShip.getCode());//待发货
        pfBorderMapper.updateById(pfBorder);
        log.info("<3>添加订单日志");
        bOrderOperationLogService.insertBOrderOperationLog(pfBorder, "");
        for (PfBorderItem pfBorderItem : pfBorderItemMapper.selectAllByOrderId(bOrderId)) {
            log.info("<4>处理发货库存");
            if (pfBorder.getUserPid() == 0) {
                PfSkuStock pfSkuStock = pfSkuStockService.selectBySkuId(pfBorderItem.getSkuId());
                //如果可售库存不足或者排单开关打开的情况下 订单进入排单
                if (pfSkuStock.getIsQueue() == 1 || pfSkuStock.getStock() - pfSkuStock.getFrozenStock() < pfBorderItem.getQuantity()) {
                    //平台库存不足，排单处理
                    pfBorder.setOrderStatus(BOrderStatus.MPS.getCode());//排队订单
                    pfBorderMapper.updateById(pfBorder);
                }
                //增加平台冻结库存
                pfSkuStock.setFrozenStock(pfSkuStock.getFrozenStock() + pfBorderItem.getQuantity());
                if (pfSkuStockService.updateByIdAndVersions(pfSkuStock) != 1) {
                    throw new BusinessException("(平台发货)排队订单增加冻结量失败");
                }
            } else {
                PfUserSkuStock parentSkuStock = pfUserSkuStockService.selectByUserIdAndSkuId(pfBorder.getUserPid(), pfBorderItem.getSkuId());
                //上级合伙人库存不足，排单处理
                if (pfBorder.getSendType() == 1 && (parentSkuStock.getStock() - parentSkuStock.getFrozenStock() < pfBorderItem.getQuantity())) {
                    pfBorder.setOrderStatus(BOrderStatus.MPS.getCode());//排队订单
                    pfBorderMapper.updateById(pfBorder);
                }
                //增加平台冻结库存
                parentSkuStock.setFrozenStock(parentSkuStock.getFrozenStock() + pfBorderItem.getQuantity());
                if (pfUserSkuStockService.updateByIdAndVersions(parentSkuStock) != 1) {
                    throw new BusinessException("(代理发货)排队订单增加冻结量失败");
                }
            }
        }
        log.info("<5>实时统计数据显示");
        orderStatisticsService.statisticsOrder(pfBorder.getId());
        log.info("<6>修改结算中数据");
        billAmountService.orderBillAmount(pfBorder.getId());
        //拿货方式(0未选择1平台代发2自己发货)
        if (pfBorder.getSendType() == 1 && pfBorder.getOrderStatus() == BOrderStatus.WaitShip.getCode()) {
            //处理平台发货类型订单
            bOrderShipService.shipAndReceiptBOrder(pfBorder);
        }
    }

    /**
     * @Author jjh
     * @Date 2016/6/6 0006 下午 4:37
     * 自己提货的订单回调处理
     * <1>修改订单支付信息
     * <2>修改订单数据
     * <3>添加订单日志
     * <4>处理发货库存
     * <5>增加统计数据
     */
    public void payBOrderTypeIII(PfBorderPayment pfBorderPayment, String outOrderId, String rootPath) {
        log.info("<1>修改订单支付信息");
        pfBorderPayment.setOutOrderId(outOrderId);
        pfBorderPayment.setIsEnabled(1);//设置为有效
        pfBorderPaymentMapper.updateById(pfBorderPayment);
        BigDecimal payAmount = pfBorderPayment.getAmount();
        Long bOrderId = pfBorderPayment.getPfBorderId();
        log.info("<2>修改订单数据");
        PfBorder pfBorder = pfBorderMapper.selectByPrimaryKey(bOrderId);
        if (pfBorder.getPayStatus() != BOrderStatus.NotPaid.getCode() && pfBorder.getPayStatus() != BOrderStatus.offLineNoPay.getCode()) {
            throw new BusinessException("订单号:" + pfBorder.getId() + ",已经支付成功.");
        }
        pfBorder.setReceivableAmount(pfBorder.getReceivableAmount().subtract(payAmount));
        pfBorder.setPayAmount(pfBorder.getPayAmount().add(payAmount));
        pfBorder.setPayTime(new Date());
        pfBorder.setPayStatus(1);
        pfBorder.setOrderStatus(BOrderStatus.WaitShip.getCode());//待发货
        pfBorderMapper.updateById(pfBorder);
        log.info("<3>添加订单日志");
        bOrderOperationLogService.insertBOrderOperationLog(pfBorder, "订单已支付,拿货订单");
        for (PfBorderItem pfBorderItem : pfBorderItemMapper.selectAllByOrderId(bOrderId)) {
            log.info("<4>处理发货库存");
            //冻结usersku库存 用户加冻结库存
            PfUserSkuStock pfUserSkuStock = null;
            pfUserSkuStock = pfUserSkuStockService.selectByUserIdAndSkuId(pfBorder.getUserId(), pfBorderItem.getSkuId());
            if (pfUserSkuStock == null) {
                throw new BusinessException("拿货失败：没有库存信息");
            }
            if (pfUserSkuStock.getStock() - pfUserSkuStock.getFrozenStock() < pfBorderItem.getQuantity()) {
                throw new BusinessException("拿货失败：拿货数量超过库存数量");
            }
            pfUserSkuStock.setFrozenStock(pfUserSkuStock.getFrozenStock() + pfBorderItem.getQuantity());
            if (pfUserSkuStockService.updateByIdAndVersions(pfUserSkuStock) == 0) {
                throw new BusinessException("并发修改库存失败");
            }
            log.info("<5>增加统计数据");
            PfUserStatistics pfUserStatistics = pfUserStatisticsService.selectByUserIdAndSkuId(pfBorder.getUserId(), pfBorderItem.getSkuId());
            pfUserStatistics.setTakeOrderCount(pfUserStatistics.getTakeOrderCount() + 1);
            pfUserStatistics.setTakeProductCount(pfUserStatistics.getTakeProductCount() + pfBorderItem.getQuantity());
            pfUserStatistics.setTakeFee(pfBorderItem.getTotalPrice());
            pfUserStatistics.setVersion(pfUserStatistics.getVersion());
            pfUserStatisticsService.updateByIdAndVersion(pfUserStatistics);
        }

    }

    /**
     * 获取证书编码
     *
     * @author ZhaoLiang
     * @date 2016/3/31 11:26
     */
    private String getCertificateCode(PfUserCertificate certificateInfo) {
        String certificateCode = "";
        String value = "";
        StringBuffer Code = new StringBuffer("MASIIS");
        value = DateUtil.Date2String(certificateInfo.getBeginTime(), "yyyy", null).substring(2);//时间
        String value1 = certificateInfo.getAgentLevelId().toString();
        String value2 = String.format("%04d", certificateInfo.getSkuId());
        String value3 = String.format("%05d", certificateInfo.getUserId());
        certificateCode = Code.append(value1).append(value2).append(value).append(value3).toString();
        return certificateCode;
    }

    /**
     * 给jpg添加文字并上传
     *
     * @param filePath        原图的物理路径
     * @param fontPath        字体地址
     * @param certificateCode 证书编号
     * @param userName        用户名称
     * @param levelName       代理等级名称
     * @param skuName         商品名称
     * @param skuEName        商品英文名称
     * @param idCard          身份证号
     * @param mobile          手机号
     * @param wxId            微信号
     * @param beginDate       开始日期
     * @param endDate         结束日期
     * @return
     */
    public String uploadFile(String filePath,
                             String fontPath,
                             String certificateCode,
                             String userName,
                             String levelName,
                             String skuName,
                             String skuEName,
                             String idCard,
                             String mobile,
                             String wxId,
                             String beginDate,
                             String endDate) {
        DrawPicUtil drawPicUtil = new DrawPicUtil();
        BufferedImage bufferedImage = drawPicUtil.loadImageLocal(filePath);
        //宋体
        Font simsun_font = null;
        //微软雅黑
        Font msyh_font = null;
        try {
            simsun_font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath + "simsun.ttc"));
            msyh_font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath + "msyh.ttc"));
        } catch (Exception e) {
            throw new BusinessException("创建字体异常");
        }
        Font msyh_font_25 = msyh_font.deriveFont(Font.PLAIN, 25);
        Font msyh_font_30 = msyh_font.deriveFont(Font.PLAIN, 30);
        Font msyh_font_100 = msyh_font.deriveFont(Font.PLAIN, 100);
        Font simsun_font_30 = simsun_font.deriveFont(Font.PLAIN, 30);
        Font simsun_font_45 = simsun_font.deriveFont(Font.PLAIN, 45);
        Font simsun_font_50 = simsun_font.deriveFont(Font.PLAIN, 50);
        List<DrawPicUtil.DrawPicParam> drawPicParams = new ArrayList<>();
        //授权书编号
        DrawPicUtil.DrawPicParam drawPicParam1 = drawPicUtil.getDrawPicParam();
        drawPicParam1.setX(90);
        drawPicParam1.setY(200);
        drawPicParam1.setContent(certificateCode);
        drawPicParam1.setFont(msyh_font_30);
        drawPicParams.add(drawPicParam1);
        //代理等级名称
        DrawPicUtil.DrawPicParam drawPicParam5 = drawPicUtil.getDrawPicParam();
        drawPicParam5.setY(200);
        drawPicParam5.setContent(levelName);
        drawPicParam5.setFont(msyh_font_100);
        drawPicParam5.setColor(Color.WHITE);
        drawPicParams.add(drawPicParam5);
        //代理人姓名
        DrawPicUtil.DrawPicParam drawPicParam3 = drawPicUtil.getDrawPicParam();
        drawPicParam3.setX(900);
        drawPicParam3.setY(565);
        drawPicParam3.setContent(userName);
        drawPicParam3.setFont(simsun_font_50);
        drawPicParams.add(drawPicParam3);
        //英文介绍
        DrawPicUtil.DrawPicParam drawPicParam4 = drawPicUtil.getDrawPicParam();
        drawPicParam4.setY(690);
        drawPicParam4.setContent("Has the right to sell the product of Beijing Masiis Biotech Co., Ltd.(" + skuEName + ")");
        drawPicParam4.setFont(msyh_font_25);
        drawPicParams.add(drawPicParam4);
        DrawPicUtil.DrawPicParam drawPicParam11 = drawPicUtil.getDrawPicParam();
        drawPicParam11.setY(725);
        drawPicParam11.setContent("via both e-commerce channel and physical store. ");
        drawPicParam11.setFont(msyh_font_25);
        drawPicParams.add(drawPicParam11);
        //代理商品名称
        DrawPicUtil.DrawPicParam drawPicParam9 = drawPicUtil.getDrawPicParam();
        drawPicParam9.setY(770);
        drawPicParam9.setContent(skuName);
        drawPicParam9.setFont(simsun_font_30);
        drawPicParam9.setColor(Color.decode("#FF4B21"));
        drawPicParams.add(drawPicParam9);
        //证件号等
        DrawPicUtil.DrawPicParam drawPicParam6 = drawPicUtil.getDrawPicParam();
        drawPicParam6.setY(850);
        drawPicParam6.setContent("证件号：" + idCard + "，手机：" + mobile + "，微信：" + wxId);
        drawPicParam6.setFont(simsun_font_45);
        drawPicParams.add(drawPicParam6);
        //授权期限
        DrawPicUtil.DrawPicParam drawPicParam2 = drawPicUtil.getDrawPicParam();
        drawPicParam2.setY(920);
        drawPicParam2.setContent("授权期限：" + beginDate + "至" + endDate);
        drawPicParam2.setFont(simsun_font_45);
        drawPicParams.add(drawPicParam2);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        try {
            ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
            ImageIO.write(drawPicUtil.modifyImage(bufferedImage, drawPicParams), "png", imOut);
        } catch (Exception ex) {
            throw new BusinessException("生成证书异常");
        }
        InputStream is = new ByteArrayInputStream(bs.toByteArray());
        String pname = getRandomFileName();
        OSSObjectUtils.uploadFile("static/user/certificate/" + pname + ".jpg", is);
        return pname;
    }

    /**
     * 生成随机文件名：当前年月日时分秒+五位随机数
     *
     * @return
     */
    private String getRandomFileName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        Random random = new Random();
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数
        return rannum + str;// 当前时间
    }

    /**
     * 获取订单详情
     *
     * @author hanzengzhi
     * @date 2016/5/11 20:33
     */
    public PfBorder getOrderDetail(Long orderId) {
        return pfBorderMapper.selectByPrimaryKey(orderId);
    }

    /**
     * 线下支付
     *
     * @author hanzengzhi
     * @date 2016/4/25 14:46
     */
    public Boolean offinePayment(ComUser comUser, Long bOrderId) throws Exception {
        //修改订单状态
        try {
            PfBorder pfBorder = updateOrderStatus(BOrderStatus.offLineNoPay.getCode(), bOrderId);
            if (pfBorder != null) {
                //插入订单支付表
                insertOrderPayment(pfBorder);
                bOrderOperationLogService.insertBOrderOperationLog(pfBorder, "订单线下已支付");
                PfSupplierBank supplierBank = getDefaultBack();
                List<PfBorderItem> orderItems = pfBorderItemMapper.selectAllByOrderId(pfBorder.getId());
                if (orderItems != null && orderItems.size() != 0) {
                    //发送微信短信提醒
                    offinePaymentNotice(comUser, pfBorder, orderItems.get(0), supplierBank);
                } else {
                    throw new BusinessException("线下支付失败:查询子帐单为null");
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

    }

    /**
     * 修改订单的状态
     *
     * @author hanzengzhi
     * @date 2016/4/25 14:49
     */
    private PfBorder updateOrderStatus(Integer status, Long bOrderId) throws Exception {
        PfBorder pfBorder = pfBorderMapper.selectByPrimaryKey(bOrderId);
        if (pfBorder != null) {
            Integer payStatus = pfBorder.getPayStatus();
            if (payStatus.equals(1)) {
                throw new BusinessException("该订单已支付,无需再支付");
            }
            pfBorder.setOrderStatus(status);
            int i = pfBorderMapper.updateById(pfBorder);
            if (i != 1) {
                throw new BusinessException("线下支付更新订单状态失败");
            }
        } else {
            throw new BusinessException("线下支付失败:查询订单信息失败");
        }
        return pfBorder;
    }

    /**
     * 线下支付微信短信通知
     *
     * @author hanzengzhi
     * @date 2016/5/5 10:56
     */
    private void offinePaymentNotice(ComUser comUser, PfBorder border, PfBorderItem orderItem, PfSupplierBank supplierBank) {
        //微信通知
        StringBuffer sb = new StringBuffer();
        log.info("用户id----" + comUser.getId() + "-------skuId----" + orderItem.getSkuId());
        String agentLevelName = null;
        if (orderItem != null) {
            log.info("合伙人等级id--------" + orderItem.getAgentLevelId());
            ComAgentLevel comAgentLevel = comAgentLevelMapper.selectByPrimaryKey(orderItem.getAgentLevelId());
            if (comAgentLevel != null) {
                agentLevelName = comAgentLevel.getName();
                log.info("合伙人等级名字--------" + agentLevelName);
            }
        }
        sb.append(orderItem.getSkuName());
        if (!StringUtils.isEmpty(agentLevelName)) {
            sb.append(agentLevelName);
        }
        sb.append("订单");
        String[] param = new String[]{border.getOrderCode(), sb.toString()};
        String offinePaymentUrl = PropertiesUtils.getStringValue("web.domain.name.address") + "/borderManage/borderDetils.html?id=" + border.getId();
        WxPFNoticeUtils.getInstance().offLinePayNotice(comUser, param, border.getCreateTime(), offinePaymentUrl);
        //短信通知
        StringBuffer sb2 = new StringBuffer();
        if (supplierBank != null) {
            sb2.append("开户行:").append(supplierBank.getBankName());
            sb2.append(" 开户名:").append(supplierBank.getAccountName());
            sb2.append(" 卡号:").append(supplierBank.getCardNumber());
            sb2.append(" 账号上");
        }
        log.info("银行卡信息-------" + sb2.toString());
        //最迟日期
        MobileMessageUtil.getInitialization("B").offlinePaymentsRemind(comUser.getMobile(), border.getOrderCode(), border.getReceivableAmount().toString(), DateUtil.insertDay(border.getCreateTime()), sb2.toString());
    }

    /**
     * 插入订单支付表
     *
     * @author hanzengzhi
     * @date 2016/4/27 14:19
     */
    private void insertOrderPayment(PfBorder pfBorder) {
        PfBorderPayment orderPayment = new PfBorderPayment();
        orderPayment.setPayTypeId(1);
        orderPayment.setPayTypeName("线下支付");
        orderPayment.setPfBorderId(pfBorder.getId());
        orderPayment.setIsEnabled(0);
        orderPayment.setAmount(pfBorder.getOrderAmount());
        orderPayment.setCreateTime(new Date());
        orderPayment.setOutOrderId("");
        orderPayment.setPaySerialNum(UUID.randomUUID().toString());
        orderPayment.setRemark("线下支付插入");
        PfBorderPayment _orderPayment = pfBorderPaymentMapper.selectByOrderIdAndPayTypeIdAndIsEnabled(pfBorder.getId(), 1, 0);
        if (_orderPayment == null) {
            log.info("线下支付:订单支付表中没有输入，插入数据----start");
            int i = pfBorderPaymentMapper.insert(orderPayment);
            if (i != 1) {
                throw new BusinessException("线下支付往订单支付表插入失败");
            }
            log.info("线下支付:订单支付表中没有输入，插入数据----end");
        }
    }

    /**
     * 线下支付
     *
     * @author hanzengzhi
     * @date 2016/4/25 14:46
     */
    public Map<String, Object> getOffinePaymentDeatil(Long bOrderId) throws BusinessException {
        Map<String, Object> map = null;
        PfBorder pfBorder = pfBorderMapper.selectByPrimaryKey(bOrderId);
        if (pfBorder != null) {
            PfSupplierBank supplierBank = getDefaultBack();
            List<PfBorderItem> orderItems = pfBorderItemMapper.selectAllByOrderId(pfBorder.getId());
            if (orderItems != null && orderItems.size() != 0) {
                map = new LinkedHashMap<String, Object>();
                map.put("latestTime", DateUtil.addDays(SysConstants.OFFINE_PAYMENT_LATEST_TIME));
                map.put("supplierBank", supplierBank);
                map.put("orderItem", orderItems.get(0));
                map.put("border", pfBorder);
            } else {
                throw new BusinessException("获取订单信息失败");
            }
        }
        return map;
    }


    /**
     * 获得运营商的默认银行卡信息
     *
     * @author hanzengzhi
     * @date 2016/4/25 14:50
     */
    private PfSupplierBank getDefaultBack() {
        return supplierBankService.getDefaultBank();
    }
}
