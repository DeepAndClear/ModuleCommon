package com.xy.module.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.StyleRes;

import com.xy.commonbase.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public abstract class BasePopupWindow extends PopupWindow {

    protected Context mContext;

    private Unbinder binder;

    protected CompositeDisposable disposable;

    public BasePopupWindow(Context context, CompositeDisposable disposable) {
        this(context,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT, R.style.WindowGrowCenterAnimation,disposable);
    }

    public BasePopupWindow(Context context, int width, int height, @StyleRes int anim , CompositeDisposable disposable) {
        this.mContext = context;
        View enrollView = LayoutInflater.from(mContext).inflate(getLayout(), null);
        binder = ButterKnife.bind(this, enrollView);
        this.disposable = disposable;
        this.setContentView(enrollView);
        // 设置宽高
        this.setWidth(width);
        this.setHeight(height);
        // 设置弹出窗口可点击
        this.setOutsideTouchable(true);
        // 窗体背景色
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(anim);
        initData();
        initView();
    }

    public abstract int getLayout();

    protected abstract void initData();

    protected abstract void initView();
}