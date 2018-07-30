package com.qiming.venus.wxpay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.lingshou.jupiter.pay.R;
import com.lingshou.jupiter.toolbox.ToastUtil;
import com.lingshou.jupiter.toolbox.log.JupiterLog;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by liuqiming on 2017/6/29.
 */
public class WXPayCallbackActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = WXPayCallbackActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay_call_back);

        if (WXPay.getInstance() != null) {
            WXPay.getInstance().getWXApi().handleIntent(getIntent(), this);
        } else {
            finish();
            return;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (WXPay.getInstance() != null) {
            WXPay.getInstance().getWXApi().handleIntent(intent, this);
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (baseResp.errStr != null) {
                JupiterLog.e(TAG, "pay Error.errStr = " + baseResp.errStr);
            }
            WXPay.getInstance().onResp(baseResp.errCode);
            finish();
        } else if (baseResp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            String code = ((SendAuth.Resp) baseResp).code;
            WXPay.getInstance().onLoginResult(code);
        }
    }
}