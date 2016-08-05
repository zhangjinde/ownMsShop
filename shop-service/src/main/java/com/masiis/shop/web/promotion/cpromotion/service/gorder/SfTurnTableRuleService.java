package com.masiis.shop.web.promotion.cpromotion.service.gorder;

import com.masiis.shop.common.enums.promotion.SfTurnTableRuleStatusEnum;
import com.masiis.shop.common.enums.promotion.SfTurnTableRuleTypeEnum;
import com.masiis.shop.dao.mall.promotion.SfTurnTableRuleMapper;
import com.masiis.shop.dao.po.SfTurnTableRule;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 转盘规则service
 */
@Service
public class SfTurnTableRuleService {

    @Resource
    private SfTurnTableRuleMapper turnTableRuleMapper;


    public SfTurnTableRule getRuleByTurnTableIdAndType(Integer turnTableId,Integer type){
        return turnTableRuleMapper.getRuleByTurnTableIdAndType(turnTableId,type);
    }

    public List<SfTurnTableRule> getRuleByTypeAndStatus(Integer type,Integer status){
        return turnTableRuleMapper.getRuleByTypeAndStatus(type,status);
    }

    /**
     * 判断是否有大转盘抽奖活动
     * @param turnTableRuleType
     * @return
     */
    public Boolean isTurnTableRule(Integer turnTableRuleType){
        List<SfTurnTableRule>  turnTableRules = getRuleByTypeAndStatus(turnTableRuleType, SfTurnTableRuleStatusEnum.EFFECT.getCode());
        if (turnTableRules==null||turnTableRules.size()==0){
            return false;
        }
        return true;
    }
}
