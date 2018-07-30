package com.qiming.venus.alipay;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.qiming.venus.callback.PayResultCallBack;

import java.util.Map;

/**
 * 支付宝支付
 * Created by liuqiming on 2018/07/30.
 */
public class Alipay {

    public static final int ERROR_RESULT = 1;   //支付结果解析错误
    public static final int ERROR_PAY = 2;  //支付失败
    public static final int ERROR_NETWORK = 3;  //网络连接错误

    private PayTask payTask;
    private PayResultCallBack callBack;
    private static Alipay aliPay;

    private Alipay(Activity context) {
        payTask = new PayTask(context);
    }

    public static Alipay getInstance(Activity activity) {
        if (aliPay == null) {
            aliPay = new Alipay(activity);
        }
        return aliPay;
    }

    public synchronized void doPay(String params, PayResultCallBack callBack) {
        this.callBack = callBack;
        new Thread(new AliPayRunnable(params)).start();
    }

    private class AliPayRunnable implements Runnable {
        private final Handler handler = new Handler();
        private String params;

        public AliPayRunnable(String params) {
            this.params = params;
        }

        @Override
        public void run() {
            final Map<String, String> payResult = payTask.payV2(params, true);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (callBack == null) {
                        return;
                    }
                    if (payResult == null) {
                        callBack.onError(ERROR_RESULT);
                        return;
                    }
                    String resultStatus = payResult.get("resultStatus");
                    if (TextUtils.equals(resultStatus, "9000")) {    //支付成功
                        callBack.onSuccess();
                    } else if (TextUtils.equals(resultStatus, "8000")) { //支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        callBack.onDealing();
                    } else if (TextUtils.equals(resultStatus, "6001")) {        //支付取消
                        callBack.onCancel();
                    } else if (TextUtils.equals(resultStatus, "6002")) {     //网络连接出错
                        callBack.onError(ERROR_NETWORK);
                    } else if (TextUtils.equals(resultStatus, "4000")) {        //支付错误
                        callBack.onError(ERROR_PAY);
                    }
                }
            });
        }
    }
}
