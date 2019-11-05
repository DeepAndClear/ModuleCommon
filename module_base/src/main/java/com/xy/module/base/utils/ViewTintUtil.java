package com.xy.module.base.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.DrawableCompat;

public class ViewTintUtil {
    /**
     * stateListDrawable (背景选择器）
     * @param drawable 图片资源（shape png svg）
     * @param states view 状态数组（ep：按下，选中）
     * @return
     */
    public static StateListDrawable getStateListDrawable(Drawable drawable,int[][] states){
        StateListDrawable stateListDrawable = new StateListDrawable();
        for (int[] state : states) {
            stateListDrawable.addState(state,drawable);
        }
        return stateListDrawable;
    }

    /**
     * Tint 方式实现 selector
     * @param drawable 图片资源（shape png svg）
     * @param colors 不同状态显示不同颜色
     * @param states view 状态数组（ep：按下，选中）
     * @return
     */
    public static Drawable getStateDrawable(Drawable drawable,int[] colors ,int[][] states){
        ColorStateList colorStateList = new ColorStateList(states,colors);
        Drawable.ConstantState state = drawable.getConstantState();
        drawable = DrawableCompat
                .wrap(state == null?drawable:state.newDrawable())
                .mutate();
        DrawableCompat.setTintList(drawable,colorStateList);
        return drawable;
    }
}
