## Android  第三方库封装
### Author：刘启明(liuqiminggood@gmail.com) [blog(http://timmy6.github.io/)](http://timmy6.github.io/)

### 一、微信支付宝支付
###### 1.导入开发资源
1.在Module级的build.gradle文件中，添加如下语句
```java
implementation 'com.venus.pay:venus_pay:1.0.1'
compile 'com.venus.utils:venus_utils:1.0.1'
```

###### 2.权限配置
```java
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

###### 3.微信配置
因为唤起微信支付时，会校验app的应用签名,所以微信支付需要一些额外配置，步骤如下：
- 登录微信开放平台获取到APPID
- 在应用包名路径下创建一个包名wxapi包路径(例如，AndroidManifest 文件中，package="com.qiming.demo"，则WXPayEntryActivity的路径为com.qiming.demo.wxapi.WXPayCallbackActivity)，且创建一个name为WXPayEntryActivity的文件，并继承 WXPayCallbackActivity，里面什么都不需要写.
```java
public class WXPayEntryActivity extends WXPayCallbackActivity {

}
```
- 在AndroidManifest.xml文件中声明 WXPayEntryActivity
```java
<activity
            android:name=".wxapi.WXPayEntryActivity"
            android:configChanges="keyboardHidden|orientation|screenSize"
            android:exported="true"
            android:screenOrientation="portrait"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />
```

###### 4.配置混淆规则
- 支付宝
```java
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.sdk.app.H5PayCallback {
    <fields>;
    <methods>;
}
-keep class com.alipay.android.phone.mrpc.core.** { *; }
-keep class com.alipay.apmobilesecuritysdk.** { *; }
-keep class com.alipay.mobile.framework.service.annotation.** { *; }
-keep class com.alipay.mobilesecuritysdk.face.** { *; }
-keep class com.alipay.tscenter.biz.rpc.** { *; }
-keep class org.json.alipay.** { *; }
-keep class com.alipay.tscenter.** { *; }
-keep class com.ta.utdid2.** { *;}
-keep class com.ut.device.** { *;}
```

- 微信
```java
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}
-keep class com.qiming.venus.** { *; }
```

###### 5.使用
- 支付宝支付
```java
			PayHelper.getInstance().doAliPay(payUrl), payPrams, new PayResultCallBack() {
			                @Override
			                public void onSuccess() {
			                	//支付成功
			                }

			                @Override
			                public void onDealing() {

			                }

			                @Override
			                public void onError(int error_code) {
			                	//支付失败
			                }

			                @Override
			                public void onCancel() {
			                	//取消支付
			                }
			            });
```

- 微信支付
```java
            WXPayParam wxPayParam = new WXPayParam(appId, partnerId, prepayId, packageValue, nonceStr, timeStamp, sign);
            PayHelper.getInstance().doWXPay(JupiterGlobal.context(), wxPayParam, new PayResultCallBack() {
                @Override
                public void onSuccess() {
                	//支付成功
                }

                @Override
                public void onDealing() {

                }

                @Override
                public void onError(int error_code) {
                	//支付失败
                }

                @Override
                public void onCancel() {
                	//取消支付
                }
            });
```
