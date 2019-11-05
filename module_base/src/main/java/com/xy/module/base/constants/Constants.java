package com.xy.module.base.constants;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Constants {

    public static final String ARG_PARAM1 = "fragment_param1";

    public static final String ARG_PARAM2 = "fragment_param2";

    public static final String ARG_PARAM3 = "fragment_param3";

    public static final String ARG_PARAM4 = "fragment_param4";

    private static final int OPTIONS_TEXT = 1407;

    private static final int OPTIONS_EDIT = 1408;

    private static final int OPTIONS_IMAGE = 1409;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({OPTIONS_TEXT,OPTIONS_EDIT,OPTIONS_IMAGE})
    public @interface OptionsType{
        int TEXT = OPTIONS_TEXT;
        int EDIT = OPTIONS_EDIT;
        int IMAGE = OPTIONS_IMAGE;
    }

    /**
     * Shared Preference key
     */
    public static final String MY_SHARED_PREFERENCE = "my_shared_preference";

    public static final String ACCOUNT = "account";

    public static final String TOKEN = "token";

    public static final String SESSION = "session";

    public static final String MEMBER_ID = "member_id";

    public static final String LOGIN_STATUS = "login_status";

    public static final String AUTO_CACHE_STATE = "auto_cache_state";

    public static final String NO_IMAGE_STATE = "no_image_state";

    public static final String NIGHT_MODE_STATE = "night_mode_state";

    public static final String TYPEFACE_STATE = "typeface_state";

    public static final String MERCHANT_STATE = "merchant_state";

    public static final String LOACTION_LAST_CODE = "last_country_code";

    public static final String FIRST_INSTALL = "apk_first_install";

    public static final String SYSTEM_FONT_SIZE = "font_size";
}
