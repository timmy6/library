package com.qiming.venus;

import android.app.Activity;
import android.content.Context;

import com.qiming.venus.alipay.Alipay;
import com.qiming.venus.callback.PayResultCallBack;
import com.qiming.venus.model.WXPayParam;
import com.qiming.venus.wxpay.WXPay;


/**
 * Created by liuqiming on 2018/07/30.
 */

public class PayHelper {
    
    private static class PayHelperHolder {
        private static final PayHelper instance = new PayHelper();
    }

    private PayHelper() {

    }

    public static final PayHelper getInstance() {
        return PayHelperHolder.instance;
    }

    /**
     * 发起微信支付
     *
     * @param context
     * @param payParams
     * @param callBack
     */
    public void doWXPay(Context context, WXPayParam payParams, PayResultCallBack callBack) {
        WXPay.getInstance().init(context).doPay(payParams, callBack);
    }

    /**
     * 发起支付宝支付
     *
     * @param context
     * @param payParams partner="xxxx"&seller_id="xxxxx"&out_trade_no="xxxxx"&subject="商品名"&total_fee="999.00"&service="xxxxxx"&payment_type="1"&_input_charset="utf-8"&it_b_pay="xxxx"&notify_url="xxxxxxx"&sign_type="RSA"&sign="xxxxxx"
     * @param callBack
     */
    public void doAliPay(Activity context, String payParams, PayResultCallBack callBack) {
        Alipay.getInstance(context).doPay(payParams, callBack);
    }
}