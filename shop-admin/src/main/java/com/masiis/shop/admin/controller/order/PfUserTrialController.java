package com.masiis.shop.admin.controller.order;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.masiis.shop.admin.beans.order.TrialInfo;
import com.masiis.shop.admin.controller.base.BaseController;
import com.masiis.shop.admin.service.order.PfUserTrialService;
import com.masiis.shop.admin.service.product.SkuService;
import com.masiis.shop.admin.service.user.ComUserService;
import com.masiis.shop.admin.service.user.SfUserRelationService;
import com.masiis.shop.dao.po.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/trial")
public class PfUserTrialController extends BaseController {

    @Resource
    private PfUserTrialService trialService;
    @Resource
    private ComUserService comUserService;
    @Resource
    private SkuService skuService;
    @Resource
    private SfUserRelationService sfUserRelationService;

    @RequestMapping("list.shtml")
    public String list(){
        return "order/trial-list";
    }

    @RequestMapping("list.do")
    @ResponseBody
    public Object list(HttpServletRequest request, HttpServletResponse response,
                       String search,
                       String sort,
                       String order,
                       Integer offset,
                       Integer limit
    ){

        offset = offset==null ? 0 : offset;
        limit  = limit ==null ? 10 : limit;
        Integer pageNo = offset/limit + 1;
        PageHelper.startPage(pageNo, limit);
        List<PfUserTrial> pfUserTrials = trialService.listByCondition(new PfUserTrial());
        PageInfo<PfUserTrial> pageInfo = new PageInfo<>(pfUserTrials);

        List<TrialInfo> trialInfos = new ArrayList<>();
        if(pfUserTrials != null && pfUserTrials.size() > 0){
            for(PfUserTrial pfUserTrial : pfUserTrials){
                ComUser comUser = comUserService.findById(pfUserTrial.getUserId());
                SfUserRelation sfUserRelation = sfUserRelationService.findByUserId(comUser.getId());
                ComUser referrer = comUserService.findById(sfUserRelation.getParentUserId());
                ComSku comSku = skuService.findById(pfUserTrial.getSkuId());

                TrialInfo trialInfo = new TrialInfo();
                trialInfo.setPfUserTrial(pfUserTrial);
                trialInfo.setComUser(comUser);
                trialInfo.setReferrer(referrer);
                trialInfo.setComSku(comSku);

                trialInfos.add(trialInfo);
            }
        }

        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("total", pageInfo.getTotal());
        pageMap.put("rows", trialInfos);

        return pageMap;
    }


    @RequestMapping("pass")
    public String pass(PfUserTrial pfUserTrial){
        trialService.pass(pfUserTrial);
        pfUserTrial = trialService.findById(pfUserTrial.getId());
        SfUserRelation sfUserRelation =  trialService.findPidById(pfUserTrial.getUserId());

        //生成试用订单
        PfCorder pfCorder = new PfCorder();

        pfCorder.setCreateTime(new Date());
        pfCorder.setCreateMan(pfUserTrial.getId());
        pfCorder.setOrderCode("00000");
        pfCorder.setOrderType(0);
        pfCorder.setSkuId(pfUserTrial.getSkuId());
        pfCorder.setUserId(pfUserTrial.getUserId());
        pfCorder.setUserPid(sfUserRelation.getParentUserId());
        pfCorder.setUserMassage("");
        pfCorder.setSupplierId(0);

        //生成试用日志
        PfCorderOperationLog pcol = new PfCorderOperationLog();
        pcol.setCreateTime(new Date());
        pcol.setCreateMan(pfUserTrial.getId());

        pcol.setPfCorderStatus(0);

        trialService.insert(pfCorder,pcol);

        return "redirect:list.shtml";
    }

    @RequestMapping("/detail.shtml")
    public String detail(){
        return "order/detail";
    }

    @RequestMapping("reason")
    public String reason(PfUserTrial pfUserTrial){
        trialService.reason(pfUserTrial);
        return "redirect:list.shtml";
    }

    @RequestMapping("cha.do")
    @ResponseBody
    public String cha(Long id){
        String reason = trialService.selectById(id);
        return reason;
    }
}
