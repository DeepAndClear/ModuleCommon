package com.xy.module.base;

import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.alibaba.android.arouter.launcher.ARouter;
import com.xy.module.base.manager.AppStateManager;
import com.xy.module.base.receiver.NetworkChangeReceiver;
import com.xy.module.base.utils.StatusBarUtils;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseActivity extends AppCompatActivity implements BGASwipeBackHelper.Delegate {
    protected BGASwipeBackHelper mSwipeBackHelper;
    protected Context mContext;
    private NetworkChangeReceiver receiver;
    protected CompositeDisposable disposable;
    private Unbinder unbinder;

    // 将所有创建过得Activity对象添加入队列中,退出时需要
    {
        BaseApplication.getApplication().addActivity(this);
    }

    protected boolean isStrangePhone() {
        return  "mx5".equalsIgnoreCase(Build.DEVICE)
                || "Redmi Note2".equalsIgnoreCase(Build.DEVICE)
                || "Z00A_1".equalsIgnoreCase(Build.DEVICE)
                || "hwH60-L02".equalsIgnoreCase(Build.DEVICE)
                || "hermes".equalsIgnoreCase(Build.DEVICE)
                || ("V4".equalsIgnoreCase(Build.DEVICE) && "Meitu".equalsIgnoreCase(Build.MANUFACTURER))
                || ("m1metal".equalsIgnoreCase(Build.DEVICE) && "Meizu".equalsIgnoreCase(Build.MANUFACTURER));

    }

    /**
     * 对比保存的字体缩放比列调整app内字体显示
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration configuration = res.getConfiguration();
        configuration.fontScale = AppStateManager.getInstance().getFontScale();
        res.updateConfiguration(configuration,res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //必须在super 之前调用,不然无效。因为那时候fragment已经被恢复了。 fragment 重叠问题
        if (savedInstanceState != null) {
            // FRAGMENTS_TAG
            savedInstanceState.remove("android:support:fragments");
            savedInstanceState.remove("android:fragments");
            savedInstanceState.remove("androidx:fragments");
        }
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResId());
        /*--------------------------夜间模式？切换----------------------------------*/
        if (AppStateManager.getInstance().isNightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        /*--------------------------中英文切换----------------------------------*/
        // 获得res资源对象
        Resources resources = this.getResources();
        // 获得屏幕参数：主要是分辨率，像素等。
        DisplayMetrics metrics = resources.getDisplayMetrics();
        // 获得配置对象
        Configuration config = resources.getConfiguration();
        //区别17版本（其实在17以上版本通过 config.locale设置也是有效的，不知道为什么还要区别）
        //在这里设置需要转换成的语言，也就是选择用哪个values目录下的strings.xml文件
            String[] codes = AppStateManager.getInstance().getTypeFace().split("-");
            switch (codes[0]){
                case "en":
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        config.setLocale(Locale.ENGLISH);//设置英文
                    }else {
                        config.locale = Locale.ENGLISH;//设置英文
                    }
                    break;
                case "zh":
                    if (codes[1].equals("CN")){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            config.setLocale(Locale.SIMPLIFIED_CHINESE);//设置简体中文
                        }else {
                            config.locale = Locale.SIMPLIFIED_CHINESE;//设置简体中文
                        }
                    }else if (codes[1].equals("TW")){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            config.setLocale( Locale.TRADITIONAL_CHINESE );//设置繁体中文
                        }else {
                            config.locale = Locale.TRADITIONAL_CHINESE;//设置繁体中文
                        }
                    }
                    break;
            }
        resources.updateConfiguration(config, metrics);
        /*--------------------------注册绑定路由组件----------------------------------*/
        ARouter.getInstance().inject(this);
        /*--------------------------注册绑定butterKnife----------------------------------*/
        unbinder = ButterKnife.bind(this);

        mContext = this;
        /*--------------------------注册绑定事件控制器----------------------------------*/
        disposable = new CompositeDisposable();
        /*--------------------------初始化屏幕边缘左滑返回----------------------------------*/
        initSwipeBackFinish();
        /*--------------------------状态栏设置----------------------------------*/
        setStatusBarColor();
        /*--------------------------注册网络监听----------------------------------*/
        registerNetworkChangeReceiver();

        initData();
        initToolBar();
        initView();
    }

    public void setStatusBarColor() {
        StatusBarUtils.immersive(this);
    }

    protected abstract int getLayoutResId();

    protected abstract void initView();

    protected void initData() {

    }

    /**
     * 注册网络监听广播
     */
    private void registerNetworkChangeReceiver() {
        receiver = new NetworkChangeReceiver(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
    }

    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
//        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        mSwipeBackHelper.setShadowResId(android.R.color.black);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsNavigationBarOverlap(false);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver.onDestroy();
            receiver = null;
        }
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (disposable != null)
            disposable.dispose();
    }

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {
    }

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    @Override
    public void onSwipeBackLayoutCancel() {
    }

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

    @Override
    public void onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding()) {
            return;
        }
        mSwipeBackHelper.backward();
    }

    protected void initToolBar() {

    }

    protected void setEnableNightMode(boolean nightMode) {
        AppStateManager.getInstance().setNightMode(nightMode);
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    protected void configLanguage(boolean locale) {
        String code = locale ? Locale.TRADITIONAL_CHINESE.getLanguage()+"-"+Locale.TRADITIONAL_CHINESE.getCountry()
                : Locale.SIMPLIFIED_CHINESE.getLanguage()+"-" + Locale.SIMPLIFIED_CHINESE.getCountry();
        AppStateManager.getInstance().setTypeFace(code);
        // 获得res资源对象
        Resources resources = this.getResources();
        // 获得屏幕参数：主要是分辨率，像素等。
        DisplayMetrics metrics = resources.getDisplayMetrics();
        // 获得配置对象
        Configuration config = resources.getConfiguration();
        //区别17版本（其实在17以上版本通过 config.locale设置也是有效的，不知道为什么还要区别）
        //在这里设置需要转换成的语言，也就是选择用哪个values目录下的strings.xml文件
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale ? Locale.TRADITIONAL_CHINESE : Locale.SIMPLIFIED_CHINESE);//设置简体中文
        } else {
            config.locale = locale ? Locale.TRADITIONAL_CHINESE : Locale.SIMPLIFIED_CHINESE;//设置简体中文
        }
        resources.updateConfiguration(config, metrics);
    }
}
