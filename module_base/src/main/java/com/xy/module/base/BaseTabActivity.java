package com.xy.module.base;

import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.google.android.material.tabs.TabLayout;
import com.jakewharton.rxbinding2.view.RxView;
import com.xy.commonbase.R;
import com.xy.commonbase.R2;
import com.xy.commonbase.helper.LogHelper;
import com.xy.commonbase.utils.StatusBarUtils;

import butterknife.BindView;

public abstract class BaseTabActivity extends BaseActivity {

    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.back)
    ImageView back;
    @BindView(R2.id.extra)
    ViewStub extra;
    @BindView(R2.id.toolbar)
    ConstraintLayout toolBar;
    @BindView(R2.id.tab_layout)
    TabLayout contentTab;
    @BindView(R2.id.fragment_group)
    ViewPager fragmentGroup;

    private String[] tabs;

    @Override
    protected int getLayoutResId() {
        return R.layout.base_activity_tab;
    }

    @Override
    protected void initData() {
        super.initData();
        tabs = getResources().getStringArray(getTabArrayId());
    }

    @Override
    protected void initView() {
        contentTab.setTabMode(TabLayout.MODE_FIXED);
        initViewPager();
    }

    private void initViewPager() {
        fragmentGroup.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager(),FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @Override
            public Fragment getItem(int position) {
                return getFragment(position);
            }

            @Override
            public int getCount() {
                return tabs.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return tabs[position];
            }
        });
        fragmentGroup.setOffscreenPageLimit(tabs.length);
        contentTab.setupWithViewPager(fragmentGroup);
        fragmentGroup.setCurrentItem(getCurShowPosition());
    }

    @Override
    protected void initToolBar() {
        disposable.add(RxView.clicks(back).subscribe(o -> finish()));
        title.setText(getTitleName());
        StatusBarUtils.setHeightAndPadding(mContext, toolBar);
        configTitleExtra(extra);
    }

    protected void configTitleExtra(ViewStub extra){

    }

    protected abstract String getTitleName();

    protected abstract int getCurShowPosition();

    protected abstract int getTabArrayId();

    protected abstract Fragment getFragment(int position);

}
