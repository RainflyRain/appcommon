package com.friean.appcommon.common;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * dp/sp 和 px 转换工具类
 * <p/>
 * Create by friean on 2016/4/19 15:52
 */
public final class DensityUtil {

    /*是否初始化标志*/
    private static boolean mInitialized = false;
    /*sp分辨率*/
    private static float mScaledDensity = 3.0f;
    /*dp分辨率*/
    private static float mDensity = 3.0f;

    /*初始化屏幕密度比*/
    public static void initialize(@NonNull Context context) {
        mInitialized = true;
        mScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        mDensity = context.getResources().getDisplayMetrics().density;
    }

    /*px to sp*/
    public static float pixelsToSp(final float px) {
        verifyInitialized();
        return px / mScaledDensity;
    }

    /*sp to px*/
    public static float spToPixels(final float sp) {
        verifyInitialized();
        return sp * mScaledDensity;
    }

    /*检测屏幕分密度比是否已经初始化*/
    public static void verifyInitialized()
            throws IllegalStateException {
        if (!mInitialized) {
            throw new IllegalStateException("Missing call to GenericFunctions::initialize()");
        }
    }

    /*px to dp*/
    public static float pixelsTodp(final float px){
        verifyInitialized();
       return px/mDensity;
    }

    /*dp to px*/
    public static float dpTopixels(final float dp){
        return dp*mDensity;
    }
}
