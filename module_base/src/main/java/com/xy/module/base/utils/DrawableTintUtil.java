package com.xy.module.base.utils;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;

public class DrawableTintUtil {

    /**
     * Drawable 颜色转化类
     *
     * @param drawable
     * @param color 资源
     * @return 改变颜色后的Drawable
     */
    public static Drawable tintDrawable(@NonNull Drawable drawable, int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }

    /**
     * Drawable 颜色转化类
     *
     * @param drawable 源Drawable
     * @param colors 状态变化映射颜色列表
     * @return 改变颜色后的Drawable
     */
    public static Drawable tintListDrawable(@NonNull Drawable drawable, ColorStateList colors) {
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }
}
