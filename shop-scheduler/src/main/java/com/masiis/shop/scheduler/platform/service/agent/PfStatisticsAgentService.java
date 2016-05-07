package com.masiis.shop.scheduler.platform.service.agent;

import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.dao.platform.user.PfUserSkuMapper;
import com.masiis.shop.dao.po.PfUserSku;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wangbingjian on 2016/4/15.
 */
@Service
public class PfStatisticsAgentService {
    private final Logger logger = Logger.getLogger(PfStatisticsAgentService.class);

    @Autowired
    private PfUserSkuMapper pfUserSkuMapper;
    /**
     * 存放查询出来的所有倒数第二级
     */
    private List<PfUserSku> secondLastList;
    /**
     * 存放需要更新的数据
     */
    private List<PfUserSku> pfUserSkuUpdateList = new ArrayList<>();
    /**
     * 将list 以pid 分组放入map中
     */
    private Map<Integer,List> map;
    /**
     * 上一级list
     */
    private List<PfUserSku> parentList;
    /**
     * 当前处理的代理级别
     */
    private Integer currentLevel;

    /**
     * 每日统计代理下级人数
     * @author:wbj
     */
    public void statisticsAgent() throws Exception{
        logger.info("每日统计代理下级人数");
        logger.info("处理之前将下级代理数量都设置为0");
        int updateCount = pfUserSkuMapper.updateResetAgentNum();
        logger.info("受影响数量："+updateCount);
        logger.info("查找倒数第二级");
        secondLastList = pfUserSkuMapper.selectSecondLastLevel();
        if (secondLastList == null || secondLastList.size() == 0){
            logger.info("各个代理没有下一级代理");
            return;
        }
        //遍历处理倒数第二级代理数据
        Long count = 0l;
        for (int i = 0; i < secondLastList.size(); i++){
            PfUserSku pfUserSku = secondLastList.get(i);
            count = pfUserSkuMapper.findLowerCount(pfUserSku.getId()).longValue();
            if (count != pfUserSku.getAgentNum()){
                pfUserSku.setAgentNum(count);
                pfUserSkuUpdateList.add(pfUserSku);
                secondLastList.set(i,pfUserSku);
            }
            currentLevel = pfUserSku.getAgentLevelId();
        }
        //处理数据以pid分组 Map<Integer,List>
        lookForParent(secondLastList);
        //计算上一级代理数量
        parentList = computeParentAgentNum();
        //递归处理
        recursion(parentList);
        //更新数据
        try{
            for (PfUserSku p : pfUserSkuUpdateList){
                pfUserSkuMapper.updateByPrimaryKey(p);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException("更新代理数据失败");
        }

    }

    /**
     * 将list以pid分组 放到map中
     * @param pfUserSkus
     */
    private void lookForParent(List<PfUserSku> pfUserSkus){
        logger.info("处理寻找上级代理");
        List<PfUserSku> allList = pfUserSkuMapper.selectByLevel(currentLevel);
        if (allList.size() == pfUserSkus.size()){
            operateMap(pfUserSkus);
        }else {
            for (int i = 0; i < allList.size(); i++){
                for (int j = 0; j < pfUserSkus.size(); j++){
                    if (allList.get(i).getId() == pfUserSkus.get(j).getId()){
                        allList.set(i,pfUserSkus.get(j));
                    }
                }
            }
            operateMap(allList);
        }
    }

    private void operateMap(List<PfUserSku> pfUserSkus){
        map = new HashMap<>();
        List<PfUserSku> list;
        for (PfUserSku pfUserSku : pfUserSkus){
            if (map.containsKey(pfUserSku.getPid())){
                list = map.get(pfUserSku.getPid());
                list.add(pfUserSku);
                map.put(pfUserSku.getPid(),list);
            } else {
                list = new ArrayList<>();
                list.add(pfUserSku);
                map.put(pfUserSku.getPid(),list);
            }
        }
    }

    /**
     * 计算上一级代理数量
     * @return
     */
    private List<PfUserSku> computeParentAgentNum(){
        logger.info("计算上一级代理数量");
        Iterator it = map.entrySet().iterator();
        List<Integer> pids = new ArrayList<>();
        while (it.hasNext()){
            Map.Entry entry = (Map.Entry) it.next();
            Integer key = (Integer) entry.getKey();
            pids.add(key);
        }

        List<PfUserSku> pfUserSkus = null;
        if (pids.size() == 0){
            return pfUserSkus;
        }
        pfUserSkus = pfUserSkuMapper.selectByListId(pids);
        List<PfUserSku> value;
        for (int i = 0; i < pfUserSkus.size(); i++){
            Long sumAgentnum = 0l;
            PfUserSku pfUserSku = pfUserSkus.get(i);
            logger.info(pfUserSku.getId());
            value = map.get(pfUserSku.getId());
            for (PfUserSku userSku : value){
                sumAgentnum += userSku.getAgentNum();
            }
            sumAgentnum += value.size();
            if (pfUserSku.getAgentNum() != sumAgentnum){
                pfUserSku.setAgentNum(sumAgentnum);
                pfUserSkus.set(i,pfUserSku);
                pfUserSkuUpdateList.add(pfUserSku);
            }
        }
        return pfUserSkus;
    }

    /**
     * 递归处理
     * 将list以pid分组 放到map中
     * 计算上一级代理数量
     * @param list
     */
    private void recursion(List<PfUserSku> list){
        logger.info("递归处理");
        //处理数据以pid分组 Map<Integer,List>
        lookForParent(list);
        //计算上一级代理数量
        parentList = computeParentAgentNum();
        if (parentList != null && parentList.size() > 0){
            currentLevel = currentLevel - 1;
            recursion(parentList);
        }
    }
}
