package com.masiis.shop.common.beans.wx.notice;

/**
 * @Date 2016/6/17
 * @Author lzh
 */
public class WxPFUpgradeApplyNotice extends WxNoticeDataBase {
    /**
     * 内容标题
     */
    private WxNoticeDataItem first;
    /**
     * 姓名
     */
    private WxNoticeDataItem keyword1;
    /**
     * 现在等级
     */
    private WxNoticeDataItem keyword2;
    /**
     * 申请等级
     */
    private WxNoticeDataItem keyword3;
    /**
     * 申请时间
     */
    private WxNoticeDataItem keyword4;
    /**
     * 备注
     */
    private WxNoticeDataItem remark;

    public WxNoticeDataItem getFirst() {
        return first;
    }

    public void setFirst(WxNoticeDataItem first) {
        this.first = first;
    }

    public WxNoticeDataItem getKeyword1() {
        return keyword1;
    }

    public void setKeyword1(WxNoticeDataItem keyword1) {
        this.keyword1 = keyword1;
    }

    public WxNoticeDataItem getKeyword2() {
        return keyword2;
    }

    public void setKeyword2(WxNoticeDataItem keyword2) {
        this.keyword2 = keyword2;
    }

    public WxNoticeDataItem getKeyword3() {
        return keyword3;
    }

    public void setKeyword3(WxNoticeDataItem keyword3) {
        this.keyword3 = keyword3;
    }

    public WxNoticeDataItem getKeyword4() {
        return keyword4;
    }

    public void setKeyword4(WxNoticeDataItem keyword4) {
        this.keyword4 = keyword4;
    }

    public WxNoticeDataItem getRemark() {
        return remark;
    }

    public void setRemark(WxNoticeDataItem remark) {
        this.remark = remark;
    }
}
