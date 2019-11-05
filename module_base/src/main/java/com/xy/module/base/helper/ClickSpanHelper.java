package com.xy.module.base.helper;

import android.content.Intent;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.xy.module.base.BaseActivity;
import com.xy.module.base.bean.ContentValue;

/**
 * Created by Administrator on 2017/12/1.
 */

public class ClickSpanHelper<T> extends ClickableSpan {

    private Class<T> mClass;
    private BaseActivity thisClass;
    private int mTextColor;
    private boolean isUnderLine;
    private ContentValue[] contentValues;


    public ClickSpanHelper(BaseActivity thisClass, Class<T> skipClass, int textColor,
                           boolean isUnderLine , ContentValue... contentValues){
        this.thisClass = thisClass;
        this.mClass = skipClass;
        this.mTextColor = textColor;
        this.isUnderLine = isUnderLine;
        this.contentValues = contentValues;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(mTextColor);
        ds.setUnderlineText(isUnderLine);
    }

    @Override
    public void onClick(View widget) {

        Intent intent = new Intent(thisClass,mClass);
        if (contentValues!= null){
            for (ContentValue value : contentValues) {
                if (value.value instanceof String) {
                    intent.putExtra(value.key, (String) value.value);
                } else if (value.value instanceof Integer)
                    intent.putExtra(value.key, Integer.parseInt(value.value.toString()));
                else if (value.value instanceof Boolean) {
                    intent.putExtra(value.key, Boolean.parseBoolean(value.value.toString()));
                }
            }
        }
        thisClass.startActivity(intent);
//        thisClass.overridePendingTransition(R.anim.machine_enter,0);
    }

}
