package com.qiming.venus.model;

import android.text.TextUtils;

import com.tencent.mm.opensdk.modelpay.PayReq;

import java.io.Serializable;

/**
 * Created by liuqiming on 2018/03/30.
 */

public class WXPayParam implements Serializable {
    public String appId;
    public String partnerId;
    public String prepayId;
    public String packageValue;
    public String nonceStr;
    public String timeStamp;
    public String sign;

    public WXPayParam(String appId, String partnerId, String prepayId, String packageValue
            , String nonceStr, String timeStamp, String sign) {
        this.appId = appId;
        this.partnerId = partnerId;
        this.prepayId = prepayId;
        this.packageValue = packageValue;
        this.nonceStr = nonceStr;
        this.timeStamp = timeStamp;
        this.sign = sign;
    }

    /**
     * 校验所有的字段是否合格
     *
     * @return true:通过、false:不通过
     */
    public boolean verifyAllParam() {
        if (TextUtils.isEmpty(this.appId) || TextUtils.isEmpty(this.partnerId)
                || TextUtils.isEmpty(this.prepayId)
                || TextUtils.isEmpty(this.packageValue)
                || TextUtils.isEmpty(this.nonceStr)
                || TextUtils.isEmpty(this.timeStamp)
                || TextUtils.isEmpty(this.sign)) {
            return false;
        }
        return true;
    }

    /**
     * 返回PayReq对象，作为WxApi发起支付的参数
     *
     * @return PayReq
     */
    public PayReq convertToPayReq() {
        PayReq req = new PayReq();
        req.appId = this.appId;
        req.partnerId = this.partnerId;
        req.prepayId = this.prepayId;
        req.packageValue = this.packageValue;
        req.nonceStr = this.nonceStr;
        req.timeStamp = this.timeStamp;
        req.sign = this.sign;
        return req;
    }

}