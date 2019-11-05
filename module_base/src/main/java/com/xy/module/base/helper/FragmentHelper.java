package com.xy.module.base.helper;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xy.module.base.BaseFragment;
import com.xy.module.base.bean.ContentValue;

/**
 * Created by Administrator on 2017/11/21.
 */

public class FragmentHelper {

    private static final String TAG = FragmentHelper.class.getSimpleName();
    private final  String BASE_PACKAGE ;

    // 为Fragment 预留的占位布局Id
    private int id;
    // 要创建的所有Fragment 对象包名
    private String[] names;
    // Fragment 资源管理器
    private FragmentManager manager;

    // 需要展示的所有Fragment 对象数组
    private BaseFragment[] fragments;
    // 当前展示的Fragment 下标
    private int currentIndex = -1;

    /**
     * 构造方法
     *
     * @param resId           预留布局Id
     * @param fragmentNames   对象包名
     * @param fragmentManager fragment 资源管理器
     */
    public FragmentHelper(int resId, FragmentManager fragmentManager,String basePackage, String... fragmentNames) {
        this.id = resId;
        this.names = fragmentNames;
        this.manager = fragmentManager;
        this.fragments = new BaseFragment[names.length];
        this.BASE_PACKAGE = basePackage;
    }

    public FragmentHelper(String basePackage){
        this.BASE_PACKAGE = basePackage;
    }

    /**
     * 展示当前需要显示的Fragment 对象
     *
     * @param index Fragment 下标
     * @return 是否已展示
     */
    public int showFragment(int index, ContentValue... type) {
        if (currentIndex == index) {
            return -1;
        }
        FragmentTransaction ft = manager.beginTransaction();
        if (currentIndex != -1) {
            ft.hide(fragments[currentIndex]);
        }
        if (fragments[index] == null) {
            if (createFragment(index, type) == 0) {
                ft.add(id, fragments[index]);
            }
        } else {
            ft.show(fragments[index]);
        }
        currentIndex = index;
        ft.commit();
        return 0;
    }

    /**
     * 反射创建Fragment 对象
     *
     * @param index 需要创建的Fragment 下标
     * @return 是否创建成功
     */
    private int createFragment(int index, ContentValue... type) {
        String className = BASE_PACKAGE + names[index] + "Fragment";
        try {
            fragments[index] = (BaseFragment) Class.forName(className).newInstance();
            if (type != null && type.length > 0) {
                Bundle bundle = new Bundle();
                for (ContentValue value : type) {
                    value.fillBundle(bundle);
                }
                fragments[index].setArguments(bundle);
            }
            return 0;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return -1;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return -1;
        }
    }
    /**
     * 反射创建Fragment 对象
     *
     * @return 是否创建成功
     */
    public BaseFragment createFragment(String name, ContentValue... type) {
        String className = BASE_PACKAGE + name+"Fragment";
        try {
            BaseFragment fragment = (BaseFragment) Class.forName(className).newInstance();
            if (type != null && type.length > 0) {
                Bundle bundle = new Bundle();
                for (ContentValue value : type) {
                    value.fillBundle(bundle);
                }
            }
            return fragment;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据下标返回需要的Fragment 对象
     *
     * @param index 下标
     * @return 对象
     */
    public BaseFragment getFragment(int index) {
        if (fragments[index] == null) {
            return null;
        }
        return fragments[index];
    }

    /**
     * 隐藏当前显示的Fragment
     */
    public boolean hideCurFragment() {
        if (currentIndex != -1) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.hide(fragments[currentIndex]);
            ft.commit();
            currentIndex = -1;
        }
        return false;
    }

    /**
     * 移除所有Fragment
     */
    public void removeAllFragment(){
        FragmentTransaction ft = manager.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            if (fragments[i]!= null){
                ft.remove(fragments[i]);
            }
            currentIndex = -1;
        }
        ft.commit();
    }

    /**
     * 返回当前显示的Fragment 对象
     *
     * @return fragment
     */
    public BaseFragment getFragment() {
        if (currentIndex != -1) {
            return fragments[currentIndex];
        }
        return null;
    }


    public void exit() {
        if (fragments != null) {
            fragments = null;
        }
    }

    public void attachFragment(Fragment fragment,String... fragmentNames){
        for (int i = 0; i < fragmentNames.length; i++) {
            String className = BASE_PACKAGE+fragmentNames[i]+"Fragment";
            if (fragment.getClass().getName().equals(className)){
                fragments[i] = (BaseFragment) fragment;
                break;
            }
        }
    }

    public int getCurrentIndex(){
        return currentIndex;
    }

}
