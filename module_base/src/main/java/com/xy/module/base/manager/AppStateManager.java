package com.xy.module.base.manager;

import com.xy.commonbase.constants.Constants;
import com.xy.commonbase.utils.SharedPreferenceUtil;
import com.xy.module.base.constants.Constants;
import com.xy.module.base.utils.SharedPreferenceUtil;

public class AppStateManager {
    // 是否为夜间模式
    private boolean nightMode;
    // 文字类型（中：简体/繁体）
    private String typeFace;
    // 文字 文字缩放
    private float fontScale;

    private AppStateManager() {
    }

    private static final class AppStateManagerHolder{
        private static final AppStateManager instance = new AppStateManager();
    }

    public static AppStateManager getInstance(){
        return AppStateManagerHolder.instance;
    }

    public static void init(){
        AppStateManagerHolder.instance.nightMode = SharedPreferenceUtil.read(Constants.MY_SHARED_PREFERENCE,Constants.NIGHT_MODE_STATE,false);
        AppStateManagerHolder.instance.typeFace = SharedPreferenceUtil.read(Constants.MY_SHARED_PREFERENCE,Constants.TYPEFACE_STATE,"zh-TW");
        AppStateManagerHolder.instance.fontScale = SharedPreferenceUtil.read(Constants.MY_SHARED_PREFERENCE,Constants.SYSTEM_FONT_SIZE,1f);
    }

    public boolean isNightMode() {
        return nightMode;
    }

    public void setNightMode(boolean nightMode) {
        SharedPreferenceUtil.write(Constants.MY_SHARED_PREFERENCE,Constants.MERCHANT_STATE,nightMode);
        this.nightMode = nightMode;
    }

    public String getTypeFace() {
        return typeFace;
    }

    public void setTypeFace(String typeFace) {
        SharedPreferenceUtil.write(Constants.MY_SHARED_PREFERENCE,Constants.TYPEFACE_STATE,typeFace);
        this.typeFace = typeFace;
    }

    public float getFontScale() {
        return fontScale;
    }

    public void setFontScale(float fontScale) {
        SharedPreferenceUtil.write(Constants.MY_SHARED_PREFERENCE,Constants.SYSTEM_FONT_SIZE,fontScale);
        this.fontScale = fontScale;
    }
}
