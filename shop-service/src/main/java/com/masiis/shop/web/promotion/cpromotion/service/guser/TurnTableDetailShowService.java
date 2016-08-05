package com.masiis.shop.web.promotion.cpromotion.service.guser;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.masiis.shop.common.enums.promotion.SfTurnTableRuleTypeEnum;
import com.masiis.shop.common.enums.promotion.SfTurnTableStatusEnum;
import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.common.util.DateUtil;
import com.masiis.shop.dao.beans.promotion.TurnTableGiftInfo;
import com.masiis.shop.dao.beans.promotion.TurnTablelInfo;
import com.masiis.shop.dao.beans.promotion.UserTurnTableRecordInfo;
import com.masiis.shop.dao.po.*;
import com.masiis.shop.web.common.utils.RandomRateUtil;
import com.masiis.shop.web.promotion.cpromotion.service.gorder.SfTurnTableGiftService;
import com.masiis.shop.web.promotion.cpromotion.service.gorder.SfTurnTableRuleService;
import com.masiis.shop.web.promotion.cpromotion.service.gorder.SfTurnTableService;
import org.springframework.mail.MailParseException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hzz on 2016/8/1.
 */
@Service
public class TurnTableDetailShowService {

    private Log log = LogFactory.getLog(this.getClass());

    @Resource
    private SfTurnTableRuleService turnTableRuleService;
    @Resource
    private SfTurnTableService turnTableService;
    @Resource
    private SfTurnTableGiftService turnTableGiftService;
    @Resource
    private SfUserTurnTableService userTurnTableService;
    @Resource
    private SfUserTurnTableRecordService userTurnTableRecordService;

    /**
     * 获取转盘信息
     * @param comUser
     * @param turnTableType
     * @param turnTableRuleStatus
     * @param turnTableStatus
     * @return
     */
    public List<TurnTablelInfo> getTurnTableInfo(ComUser comUser,Integer turnTableType,Integer turnTableRuleStatus,Integer turnTableStatus){
        //查询所有进行中的转盘(sf_turn_table和sf_turn_table_rule联合查询)
        List<SfTurnTable> turnTables = turnTableService.getTurnTableByRuleTypeAndRuleStatusAndTableStatus(turnTableType,turnTableRuleStatus,turnTableStatus);
        List<TurnTablelInfo> turnTablelInfos = new ArrayList<>();
        for (SfTurnTable turnTable:turnTables){
            TurnTablelInfo turnTablelInfo = new TurnTablelInfo();
            //转盘规则
            SfTurnTableRule turnTableRule = turnTableRuleService.getRuleByTurnTableIdAndType(turnTable.getId(),turnTableType);
            if (turnTableRule==null){
                throw new BusinessException("-----获取转盘规则失败------");
            }
            turnTablelInfo.setTurnTableRule(turnTableRule);
            //转盘信息
            List<TurnTableGiftInfo> turnTableGiftInfos =  turnTableGiftService.getTurnTableGiftsByTableId(turnTable.getId());
/*            Map<Integer,Integer> giftIdMap = new LinkedHashMap<>();
            Map<Integer,String> giftNameMap = new LinkedHashMap<>();
            Map<Integer,String> giftImgMap = new LinkedHashMap<>();
            for (TurnTableGiftInfo turnTableGiftInfo:turnTableGiftInfos){
                giftIdMap.put(turnTableGiftInfo.getSort(),turnTableGiftInfo.getGiftId());
                giftNameMap.put(turnTableGiftInfo.getSort(),turnTableGiftInfo.getGiftName());
                giftImgMap.put(turnTableGiftInfo.getSort(),turnTableGiftInfo.getImgUrl());
            }
            turnTablelInfo.setGiftIdMap(giftIdMap);
            turnTablelInfo.setGiftNameMap(giftNameMap);
            turnTablelInfo.setGiftImgMap(giftImgMap);*/
            turnTablelInfo.setTurnTableGiftInfo(turnTableGiftInfos);
            turnTablelInfo.setTurnTableId(turnTable.getId());
            turnTablelInfo.setBeginTimeString(DateUtil.Date2String(turnTable.getBeginTime(),DateUtil.CHINESE_YEAR_MONTH_DATE_FMT));
            turnTablelInfo.setEndTimeString(DateUtil.Date2String(turnTable.getEndTime(),DateUtil.CHINESE_YEAR_MONTH_DATE_FMT));
            turnTablelInfo.setDescribe(turnTable.getDescribe());
            //用户抽奖次数信息
            SfUserTurnTable userTurnTable = userTurnTableService.getSfUserTurnTable(comUser.getId(),turnTable.getId());
            turnTablelInfo.setUserTurnTable(userTurnTable);
            //转盘中的奖品的所有中奖纪录
            List<UserTurnTableRecordInfo> recordInfos = userTurnTableRecordService.getRecordInfoByTableId(turnTable.getId());
            turnTablelInfo.setUserTurnTableRecordInfos(recordInfos);
            turnTablelInfos.add(turnTablelInfo);
        }
        return turnTablelInfos;
    }

    public int getRandomByGiftRate(Integer turnTableId){
        //获取转盘中的奖品
        List<TurnTableGiftInfo> turnTableGiftInfos =  turnTableGiftService.getTurnTableGiftsByTableId(turnTableId);
        Map<Integer,Double> map = new LinkedHashMap<>();
        for (TurnTableGiftInfo turnTableGiftInfo: turnTableGiftInfos){
                map.put(turnTableGiftInfo.getSort(),Double.parseDouble(turnTableGiftInfo.getProbability()+""));
        }
        int i = RandomRateUtil.getInstance().percentageRandom(map);
        if (i!=-1){
            return  i;
        }else{
            throw new BusinessException("获取中奖数字出错------");
        }
    }

}
