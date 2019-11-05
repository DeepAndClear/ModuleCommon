package com.xy.module.base.utils;

import android.content.Context;
import android.widget.Toast;

import com.xy.module.base.BaseApplication;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ToastUtil {

    private static Toast toast;

    private ToastUtil() {

    }

    public static void show(Context context, int resId) {
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
            toast.setText(resId);
            toast.show();
            return;
        } else {
            toast.setText(resId);
        }
        toast.show();
    }

    public static void show(Context context, CharSequence text) {
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
            toast.setText(text);
            toast.show();
            return;
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static void show(int resId){
        Observable.just(resId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(s->
        show(BaseApplication.getApplication(),s));
    }
    public static void show(CharSequence text){
        Observable.just(text)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s->show(BaseApplication.getApplication(),s));
    }
}
