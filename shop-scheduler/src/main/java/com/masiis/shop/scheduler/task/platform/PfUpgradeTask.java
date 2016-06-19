package com.masiis.shop.scheduler.task.platform;

import com.masiis.shop.scheduler.platform.business.user.PfUserUpgradeTaskService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Date 2016/6/16
 * @Author lzh
 */
@Component
public class PfUpgradeTask {
    private Logger log = Logger.getLogger(this.getClass());

    @Resource
    private PfUserUpgradeTaskService upgradeTaskService;

    /**
     * 代理升级通知单2天未处理,统一默认上级不升级定时任务
     */
    public void unHandleUpgradeNoticeJob(){
        log.info("统一处理2天未处理升级通知单开始......");
        try {
            upgradeTaskService.handleUnsolvedUpgradeNotice();
        } catch (Exception e) {
            log.error("统一处理2天未处理升级通知单失败...");
        }

        log.info("统一处理2天未处理升级通知单结束......");
    }


    public void aa(){

    }
}
