package com.xy.module.base.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.xy.module.base.helper.LogHelper;

import java.util.Locale;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * 系统工具类
 * Created by zhuwentao on 2016-07-18.
 */
public class SystemUtil {

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getSystemVersionCode() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
    public static String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getDeviceId();
        }
        return null;
    }

    //获取版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //获取版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    //通过PackageInfo得到的想要启动的应用的包名
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pInfo = null;

        try {
            //通过PackageManager可以得到PackageInfo
            PackageManager pManager = context.getPackageManager();
            pInfo = pManager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pInfo;
    }


    /**
     * 获取应用包名
     *
     * @param context 上下文
     * @return 包名
     */
    public static String getAppName(Context context) {
        //获取包信息
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo == null)
            return null;
        else {
            //获取应用 信息
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            //获取albelRes
            int labelRes = applicationInfo.labelRes;
            //返回App的名称
            return context.getResources().getString(labelRes);
        }
    }

    /**
     * 获取剪切板内筒
     *
     * @param context 上下文
     * @return 内容
     */
    public static String getClipBoardContent(Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData data = cm.getPrimaryClip();
        for (int i = 0; i < data.getItemCount(); i++) {
            if (data.getItemAt(i) != null)
                LogHelper.e("ClipBoardContent :   " + data.getItemAt(i).getHtmlText()
                        + "\n\n" + data.getItemAt(i).getText().toString()
                        + "\n\n" + data.getItemAt(i).getUri().getPath()
                        + "\n\n" + data.getItemAt(i).toString());
        }
        ClipData.Item item = data.getItemAt(0);
        return item.getText().toString();
    }

    /**
     * 获取当前activity 栈 栈顶 activity
     *
     * @param context 上下文
     * @return 栈顶activity 对象
     * @throws ClassNotFoundException 无当前对象
     */
    public static Class getStackTopActivity(Context context) throws ClassNotFoundException {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String activityName = am.getRunningTasks(1).get(0).topActivity.getClassName();
        return Class.forName(activityName);
    }

}
