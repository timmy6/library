package com.qiming.venus;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * Created by liuqiming on 2018/7/30.
 */
public class AppUtil {

    
    /**
     * 获取在Manifests文件中的value
     *
     * @param context  上下文
     * @param key      主键
     * @param defValue 默认值
     * @return
     */
    public static String getValueFromManifestFile(Context context, String key, String defValue) {
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
}