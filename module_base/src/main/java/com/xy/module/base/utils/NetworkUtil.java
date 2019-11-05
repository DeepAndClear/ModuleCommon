package com.xy.module.base.utils;

import android.annotation.SuppressLint;
import android.app.usage.NetworkStats;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络链接工具类
 */
public class NetworkUtil {

    private NetworkUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 网络链接是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            @SuppressLint("MissingPermission") NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
            for (NetworkInfo networkInfo : networkInfos) {
                NetworkInfo.State state = networkInfo.getState();
                if (state == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }
}
