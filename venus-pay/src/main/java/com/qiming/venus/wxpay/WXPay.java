package com.qiming.venus.wxpay;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.qiming.venus.callback.PayResultCallBack;
import com.qiming.venus.model.WXPayParam;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信支付
 * Created by liuqiming on 2017/6/29.
 */
public class WXPay {

    private static final String TAG = WXPay.class.getSimpleName();

    public static final int NO_OR_LOW_WX = 1;   //未安装微信或微信版本过低
    public static final int ERROR_PAY_PARAM = 2;  //支付参数错误
    public static final int ERROR_PAY = 3;  //支付失败
    private static final String WX_GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

    private String appId;
    private static WXPay wxPay;
    private IWXAPI wxApi;
    private PayResultCallBack callBack;

    private WXPay(Context context) {
        wxApi = WXAPIFactory.createWXAPI(context, null);
        appId = getValueFromManifestFile(context, "wx_app_id", "");
        if (TextUtils.isEmpty(appId)) {
            Log.e(TAG, "请在Manifests文件中，配置wx_app_id");
        } else {
            wxApi.registerApp(appId);
        }
    }

    /**
     * 获取在Manifests文件中的value
     *
     * @param context  上下文
     * @param key      主键
     * @param defValue 默认值
     * @return
     */
    public String getValueFromManifestFile(Context context, String key, String defValue) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            if (TextUtils.isEmpty(appInfo.metaData.getString(key))) {
                return defValue;
            } else {
                return appInfo.metaData.getString(key);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return defValue;
    }

    /**
     * 初始化
     *
     * @param context 上下文
     * @return wxPay 支付对象
     */
    public static WXPay init(Context context) {
        if (wxPay == null) {
            wxPay = new WXPay(context);
        }
        return wxPay;
    }

    public static WXPay getInstance() {
        return wxPay;
    }

    public IWXAPI getWXApi() {
        return wxApi;
    }

    /**
     * 发起微信支付
     */
    public synchronized void doPay(WXPayParam payParam, PayResultCallBack callback) {
        if (wxApi == null) {
            return;
        }
        if (payParam == null) {
            return;
        }
        if (callback == null) {
            return;
        }
        callBack = callback;
        if (!check()) {
            callBack.onError(NO_OR_LOW_WX);
            return;
        }
        if (!payParam.verifyAllParam()) {
            callBack.onError(ERROR_PAY_PARAM);
            return;
        }
        PayReq payReq = payParam.convertToPayReq();
        wxApi.sendReq(payReq);
    }

    /**
     * 支付回调响应
     *
     * @param errorCode 错误码
     */
    public void onResp(int errorCode) {
        if (callBack == null) {
            return;
        }
        String result = "";
        if (errorCode == 0) {   //成功
            callBack.onSuccess();
            result = "success";
        } else if (errorCode == -1) {   //错误
            callBack.onError(ERROR_PAY);
            result = "error";
        } else if (errorCode == -2) {   //取消
            callBack.onCancel();
            result = "cancel";
        }
        callBack = null;
    }

    /**
     * 检测是否支持微信支付
     *
     * @return 是否支持微信支付
     */
    private boolean check() {
        return wxApi.isWXAppInstalled() && wxApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }
}