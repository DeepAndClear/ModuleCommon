package com.xy.module.base.helper;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AbsListView;

/**
 * Created by Administrator on 2017/12/20.
 */

public class KeyboardPatch
{
    private Activity activity;
    private View decorView;
    private View contentView;

    /**
     * 构造函数
     * @param act 需要解决bug的activity
     * @param contentView 界面容器，activity中一般是R.id.content，也可能是Fragment的容器，根据个人需要传递
     * */
    public KeyboardPatch(Activity act, View contentView)
    {
        this.activity = act;
        this.decorView = act.getWindow().getDecorView();
        this.contentView = contentView;
        saveInitialState();
    }

    private int left,right,top,bottom;

    private void saveInitialState() {
        left = contentView.getPaddingLeft();
        right = contentView.getPaddingRight();
        top = contentView.getPaddingTop();
        bottom = contentView.getPaddingBottom();
    }

    /**
     * 监听layout变化
     * */
    public void enable()
    {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= 19)
        {
            decorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    }

    /**
     * 取消监听
     * */
    public void disable()
    {
//        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= 19)
        {
            decorView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    }

    private static final String TAG = "KeyboardPatch";

    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener()
    {
        @Override
        public void onGlobalLayout()
        {
            Rect r = new Rect();

            decorView.getWindowVisibleDisplayFrame(r);
            int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
            int diff = height - r.bottom;

            Rect f = new Rect();
            contentView.getGlobalVisibleRect(f);
            int offsetHeight = height - f.bottom ;

            if (contentView instanceof AbsListView){
                AbsListView view = (AbsListView) contentView;if (diff > 0) {
                    if (view.getPaddingBottom() != diff) {
                        view.setPadding(left, top, right, diff- offsetHeight+ bottom);
                    }
                    if (onKeyBoardListener!= null)
                        onKeyBoardListener.show();
                } else {
                    if (view.getPaddingBottom() != 0) {
                        view.setPadding(left, top, right, bottom);
                    }
                    if (onKeyBoardListener!= null)
                        onKeyBoardListener.hide();
                }
            }else {
                    if (diff > 0) {
                        if (contentView.getPaddingBottom() != diff) {
                            contentView.setPadding(left, top, right, diff- offsetHeight+ bottom);
                        }
                        if (onKeyBoardListener!= null)
                            onKeyBoardListener.show();
                    } else {
                        if (contentView.getPaddingBottom() != 0) {
                            contentView.setPadding(left, top, right, bottom);
                        }
                        if (onKeyBoardListener!= null)
                            onKeyBoardListener.hide();
                    }
                }
        }
    };

    public interface OnKeyBoardListener{
        void show();

        void hide();
    }

    private OnKeyBoardListener onKeyBoardListener;

    public void setOnKeyBoardListener(OnKeyBoardListener onKeyBoardListener) {
        this.onKeyBoardListener = onKeyBoardListener;
    }
}
