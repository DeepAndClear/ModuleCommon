package com.xy.module.base.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;

import com.xy.module.base.helper.LogHelper;

import java.lang.reflect.Field;

public class ViewInflateUtil {

    public static boolean showLog = false;

    /**
     * 获取传入View在窗口中的绝对坐标
     * 不包括状态栏
     *
     * @param view 需求view
     * @return view顶点坐标
     */
    public static int[] getLocationInWindow(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        if (showLog)
            LogHelper.i("InWindow           x：" + location[0] + "         y : " + location[1]);
        return location;
    }

    /**
     * 获取传入View在屏幕中的绝对坐标
     * 包括状态栏
     *
     * @param view 需求view
     * @return view顶点坐标
     */
    public static int[] getLocationInScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        if (showLog)
            LogHelper.i("InScreen           x：" + location[0] + "         y : " + location[1]);
        return location;
    }

    /**
     * 获取传入View在??的绝对坐标
     * 包括状态栏
     *
     * @param view 需求view
     * @return view顶点坐标
     */
    public static int[] getLocationInSurface(View view) {
        int[] location = new int[2];
        view.getLocationInSurface(location);
        if (showLog)
            LogHelper.i("InSurface           x：" + location[0] + "         y : " + location[1]);
        return location;
    }

    /**
     * 获取View的展示状态
     *
     * @param view 需求view
     * @return true 展示 false 隐藏
     */
    public static boolean getViewState(View view) {
        return view.isShown();
    }

    /**
     * 获取view的显示状态 3 种
     *
     * @param view 需求view
     * @return View.Visible  / View.InVisible / View.GONE
     */
    public static int getViewStatus(View view) {
        return view.getVisibility();
    }

    /**
     * 获取View 的可视区域 5 种状态
     * 1、全部可见 view处于屏幕底部 rect   left:view的相对《自己》x的起始位置（一般为0）
     * ------------------------------------right：view相对《自己》x的终点位置（一般为自身宽度）
     * ------------------------------------top：view相对于《自己》y的起始位置（一般为 0）
     * ------------------------------------bottom：view相对于《自己》y的可视位置（一般为自身高度）
     * 2、部分可见 view处于屏幕底部 rect   left:view的相对《自己》x的起始位置（一般为）
     * ------------------------------------right：view相对《自己》x的终点位置（一般为）
     * ------------------------------------top：view相对于《自己》y的起始位置（一般为 0）
     * ------------------------------------bottom：view相对于《自己》y的可视位置（一般为可视区域高度）
     * 3、部分可见 view处于屏幕顶部 rect   left:view的相对《自己》x的起始位置（一般为）
     * ------------------------------------right：view相对《自己》x的终点位置（一般为）
     * ------------------------------------top：view相对于《自己》y的可视位置（一般为可视区域高度）
     * ------------------------------------bottom：view相对于《自己》y的终点位置（一般为自身高度）
     * 4、不可见 view处于屏幕底部 rect   left:view的相对《父控件可视区域》x的自身左顶点位置（一般为parent.padding+view.margin）
     * ------------------------------------right：view相对《父控件可视区域》x的自身右顶点位置（一般为parent.padding+view.margin+view.width）
     * ------------------------------------top：view相对于《父控件可视区域》y的自身上顶点位置（一般为parent.visible+view.hideHeight）
     * ------------------------------------bottom：view相对于《父控件可视区域》y的自身右顶点位置（一般为parent.visible+view.hideHeight+view.height）
     * 5、不可见 view处于屏幕顶部 rect   left:view的相对《父控件可视区域》x的起始位置（一般为parent.padding+view.margin）
     * ------------------------------------right：view相对《父控件可视区域》x的终点位置（一般为parent.padding+view.margin+view.width）
     * ------------------------------------top：view相对于《父控件可视区域》y的可视位置（一般为可视区域高度0-view.hideHeight）
     * ------------------------------------bottom：view相对于《父控件可视区域》y的终点位置（一般为自身高度0-view.hideHeight-view.height）
     *
     * @param view
     * @return
     */
    public static Rect getLocalVisibleRect(View view) {
        Rect rect = new Rect();
        view.getLocalVisibleRect(rect);
        if (showLog)
            LogHelper.i("LocalVisibleRect           left：" + rect.left + "         right : " + rect.right
                    + "         top : " + rect.top + "         bottom : " + rect.bottom);
        return rect;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        return localDisplayMetrics.widthPixels;
    }
    public static int getScreenHeight(Context context) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        return localDisplayMetrics.heightPixels - getStatusBarHeight(context);
    }

    public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
}
