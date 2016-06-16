package com.masiis.shop.scheduler.platform.business.user;

import com.masiis.shop.common.enums.upgrade.UpGradeStatus;
import com.masiis.shop.common.enums.upgrade.UpGradeUpStatus;
import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.common.interfaces.IParallelThread;
import com.masiis.shop.common.util.CurrentThreadUtils;
import com.masiis.shop.common.util.DateUtil;
import com.masiis.shop.dao.po.ComUser;
import com.masiis.shop.dao.po.PfUserUpgradeNotice;
import com.masiis.shop.scheduler.platform.service.user.PfUserUpgradeNoticeService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 代理升级通知单定时任务类
 *
 * @Date 2016/6/16
 * @Author lzh
 */
@Service
public class PfUserUpgradeTaskService {
    private Logger log = Logger.getLogger(this.getClass());

    @Resource
    private PfUserUpgradeNoticeService pfUserUpgradeNoticeService;

    /**
     * 处理2天未处理的升级通知单
     */
    public void handleUnsolvedUpgradeNotice() {
        Date time = DateUtil.getDateNextdays(new Date(), -2);
        log.info("计算时间临界点:" + time);

        // 查询要处理的升级通知单
        List<PfUserUpgradeNotice> notices = pfUserUpgradeNoticeService.findAllUnsolvedNoticesByDate(time);
        if(notices == null || notices.size() <= 0){
            log.info("暂无需要处理的升级通知单...");
            return;
        }

        log.info("开始处理升级通知单");

        final List<PfUserUpgradeNotice> wxNotices = new ArrayList<>();
        // 多线程处理
        CurrentThreadUtils.parallelJob(new IParallelThread() {
            public Boolean doMyJob(Object obj) throws Exception {
                PfUserUpgradeNotice notice = (PfUserUpgradeNotice) obj;
                try{
                    pfUserUpgradeNoticeService.handleUnSolvedUpgradeNotice(notice);
                    log.info("处理未处理升级单成功");
                    synchronized (this) {
                        wxNotices.add(notice);
                    }
                    return true;
                } catch (Exception e) {
                    log.error("处理未处理升级单失败,noticeCode:" + notice.getCode() + "," + e.getMessage());
                    return false;
                }
            }
        }, new LinkedBlockingDeque<Object>(notices), 0);

        // 多线程发送成功微信通知
        CurrentThreadUtils.parallelJob(new IParallelThread() {
            public Boolean doMyJob(Object obj) throws Exception {
                PfUserUpgradeNotice notice = (PfUserUpgradeNotice) obj;
                try{
                    // 发送微信

                    log.info("发送微信通知成功,noticeCode:" + notice.getCode());
                    return true;
                } catch (Exception e) {
                    log.error("发送微信通知失败," + "," + e.getMessage());
                    return false;
                }
            }
        }, new LinkedBlockingDeque<Object>(wxNotices), 0);
    }


    public static void main(String... args){
        System.out.println(DateUtil.getDateNextdays(new Date(), -2));
    }
}
