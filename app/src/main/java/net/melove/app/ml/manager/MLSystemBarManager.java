package net.melove.app.ml.manager;


/**
 * Created by lzan13 on 2015/3/6.
 */


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;


import java.lang.reflect.Method;


public class MLSystemBarManager {

    static {
        // Android allows a system property to override the presence of the navigation bar.
        // Used by the emulator.
        // See https://github.com/android/platform_frameworks_base/blob/master/policy/src/com/android/internal/policy/impl/PhoneWindowManager.java#L1076
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
                sNavBarOverride = null;
            }
        }
    }


    /**
     * The default system bar tint color value.
     */
    public static final int DEFAULT_TINT_COLOR = 0x99000000;

    private static String sNavBarOverride;

    private final SystemBarConfig mConfig;
    private boolean mStatusBarAvailable;
    private boolean mNavBarAvailable;
    private boolean mStatusBarTintEnabled;
    private boolean mNavBarTintEnabled;
    private View mStatusBarTintView;
    private View mNavBarTintView;

    public View getmNavBarTintView() {
        return mNavBarTintView;
    }

    public View getmStatusBarTintView() {
        return mStatusBarTintView;
    }

    /**
     * 构造函数在Activity调用onCreate()初始化之后
     *
     * @param activity 当前活动的Activity.
     */
    public MLSystemBarManager(Activity activity) {

        Window win = activity.getWindow();
        ViewGroup decorViewGroup = (ViewGroup) win.getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // check theme attrs
            int[] attrs = {android.R.attr.windowTranslucentStatus,
                    android.R.attr.windowTranslucentNavigation};
            TypedArray a = activity.obtainStyledAttributes(attrs);
            try {
                mStatusBarAvailable = a.getBoolean(0, false);
                mNavBarAvailable = a.getBoolean(1, false);
            } finally {
                a.recycle();
            }

            // check window flags
            WindowManager.LayoutParams winParams = win.getAttributes();
            int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if ((winParams.flags & bits) != 0) {
                mStatusBarAvailable = true;
            }
            bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            if ((winParams.flags & bits) != 0) {
                mNavBarAvailable = true;
            }
        }

        mConfig = new SystemBarConfig(activity, mStatusBarAvailable, mNavBarAvailable);
        // device might not have virtual navigation keys
        if (!mConfig.hasNavigtionBar()) {
            mNavBarAvailable = false;
        }

        if (mStatusBarAvailable) {
            setupStatusBarView(activity, decorViewGroup);
        }
        if (mNavBarAvailable) {
            setupNavBarView(activity, decorViewGroup);
        }

    }

    /**
     * 设定系统状态栏是否显示View
     *
     * @param enabled 为true mStatusBarView 显示设定的颜色，为false 显示为透明（default）
     */
    public void setStatusBarTintEnabled(boolean enabled) {
        mStatusBarTintEnabled = enabled;
        if (mStatusBarAvailable) {
            mStatusBarTintView.setVisibility(enabled ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 为系统导航栏设置是否显示有背景色
     *
     * 如果设备没有虚拟按键，则这个方法无效
     *
     * @param enabled 为true mNavBarView 显示设定的颜色，为false 显示为透明（default）
     */
    public void setNavigationBarTintEnabled(boolean enabled) {
        mNavBarTintEnabled = enabled;
        if (mNavBarAvailable) {
            mNavBarTintView.setVisibility(enabled ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 设置系统栏的颜色
     *
     * @param color 背景色.
     */
    public void setTintColor(int color) {
        setStatusBarTintColor(color);
        setNavigationBarTintColor(color);
    }

    /**
     * 指定颜色资源或者文件文件资源为系统栏的背景色
     *
     * @param res 资源id.
     */
    public void setTintResource(int res) {
        setStatusBarTintResource(res);
        setNavigationBarTintResource(res);
    }

    /**
     * 设置系统栏使用可拉伸的系统资源
     *
     * @param drawable 资源文件或者 null.
     */
    public void setTintDrawable(Drawable drawable) {
        setStatusBarTintDrawable(drawable);
        setNavigationBarTintDrawable(drawable);
    }

    /**
     * 设置系统栏透明度
     *
     * @param alpha 透明度
     */
    public void setTintAlpha(float alpha) {
        setStatusBarAlpha(alpha);
        setNavigationBarAlpha(alpha);
    }

    /**
     * 为状态栏设置背景颜色
     *
     * @param color The color of the background tint.
     */
    public void setStatusBarTintColor(int color) {
        if (mStatusBarAvailable) {
            mStatusBarTintView.setBackgroundColor(color);
        }
    }

    /**
     * 为状态栏设置背景资源
     *
     * @param res 资源id
     */
    public void setStatusBarTintResource(int res) {
        if (mStatusBarAvailable) {
            mStatusBarTintView.setBackgroundResource(res);
        }
    }

    /**
     * 为状态栏设置背景资源
     *
     * @param drawable The drawable to use as the background, or null to remove it.
     */
    @SuppressWarnings("deprecation")
    public void setStatusBarTintDrawable(Drawable drawable) {
        if (mStatusBarAvailable) {
            mStatusBarTintView.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 为状态栏设置透明度
     *
     * @param alpha The alpha to use
     */
    public void setStatusBarAlpha(float alpha) {
        if (mStatusBarAvailable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mStatusBarTintView.setAlpha(alpha);
        }
    }

    /**
     * 为导航栏设置背景颜色
     *
     * @param color The color of the background tint.
     */
    public void setNavigationBarTintColor(int color) {
        if (mNavBarAvailable) {
            mNavBarTintView.setBackgroundColor(color);
        }
    }

    /**
     * 为导航栏设置背景资源
     *
     * @param res The identifier of the resource.
     */
    public void setNavigationBarTintResource(int res) {
        if (mNavBarAvailable) {
            mNavBarTintView.setBackgroundResource(res);
        }
    }

    /**
     * 为导航栏设置背景图片资源
     *
     * @param drawable The drawable to use as the background, or null to remove it.
     */
    @SuppressWarnings("deprecation")
    public void setNavigationBarTintDrawable(Drawable drawable) {
        if (mNavBarAvailable) {
            mNavBarTintView.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 为导航栏设置透明度
     *
     * @param alpha The alpha to use
     */
    public void setNavigationBarAlpha(float alpha) {
        if (mNavBarAvailable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mNavBarTintView.setAlpha(alpha);
        }
    }

    /**
     * 获取系统栏配置
     *
     * @return 返回当前系统栏被配置
     */
    public SystemBarConfig getConfig() {
        return mConfig;
    }

    /**
     * 是否激活状态栏
     *
     * @return 激活为 true，否则为false
     */
    public boolean isStatusBarTintEnabled() {
        return mStatusBarTintEnabled;
    }

    /**
     * 是否激活导航栏背景
     *
     * @return 激活为 true，否则为 false
     */
    public boolean isNavBarTintEnabled() {
        return mNavBarTintEnabled;
    }

    private void setupStatusBarView(Context context, ViewGroup decorViewGroup) {
        mStatusBarTintView = new View(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mConfig.getStatusBarHeight());
        params.gravity = Gravity.TOP;
        if (mNavBarAvailable && !mConfig.isNavigationAtBottom()) {
            params.rightMargin = mConfig.getNavigationBarWidth();
        }
        mStatusBarTintView.setLayoutParams(params);
        mStatusBarTintView.setBackgroundColor(DEFAULT_TINT_COLOR);
        mStatusBarTintView.setVisibility(View.GONE);
        decorViewGroup.addView(mStatusBarTintView);
    }

    private void setupNavBarView(Context context, ViewGroup decorViewGroup) {
        mNavBarTintView = new View(context);
        LayoutParams params;
        if (mConfig.isNavigationAtBottom()) {
            params = new LayoutParams(LayoutParams.MATCH_PARENT, mConfig.getNavigationBarHeight());
            params.gravity = Gravity.BOTTOM;
        } else {
            params = new LayoutParams(mConfig.getNavigationBarWidth(), LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.RIGHT;
        }
        mNavBarTintView.setLayoutParams(params);
        mNavBarTintView.setBackgroundColor(DEFAULT_TINT_COLOR);
        mNavBarTintView.setVisibility(View.GONE);
        decorViewGroup.addView(mNavBarTintView);
    }

    /**
     * 系统栏配置保存类，保存了状态栏的大小，位置，颜色等配置信息
     */
    public static class SystemBarConfig {

        private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
        private static final String NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height";
        private static final String NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME = "navigation_bar_height_landscape";
        private static final String NAV_BAR_WIDTH_RES_NAME = "navigation_bar_width";
        private static final String SHOW_NAV_BAR_RES_NAME = "config_showNavigationBar";

        private final boolean mTranslucentStatusBar;
        private final boolean mTranslucentNavBar;
        private final int mStatusBarHeight;
        private final int mActionBarHeight;
        private final boolean mHasNavigationBar;
        private final int mNavigationBarHeight;
        private final int mNavigationBarWidth;
        private final boolean mInPortrait;
        private final float mSmallestWidthDp;

        private SystemBarConfig(Activity activity, boolean translucentStatusBar, boolean traslucentNavBar) {
            Resources res = activity.getResources();
            mInPortrait = (res.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
            mSmallestWidthDp = getSmallestWidthDp(activity);
            mStatusBarHeight = getInternalDimensionSize(res, STATUS_BAR_HEIGHT_RES_NAME);
            mActionBarHeight = getActionBarHeight(activity);
            mNavigationBarHeight = getNavigationBarHeight(activity);
            mNavigationBarWidth = getNavigationBarWidth(activity);
            mHasNavigationBar = (mNavigationBarHeight > 0);
            mTranslucentStatusBar = translucentStatusBar;
            mTranslucentNavBar = traslucentNavBar;
        }

        private int getActionBarHeight(Context context) {
            int result = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                TypedValue tv = new TypedValue();
                context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
                result = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
            }
            return result;
        }

        private int getNavigationBarHeight(Context context) {
            Resources res = context.getResources();
            int result = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                if (hasNavBar(context)) {
                    String key;
                    if (mInPortrait) {
                        key = NAV_BAR_HEIGHT_RES_NAME;
                    } else {
                        key = NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME;
                    }
                    return getInternalDimensionSize(res, key);
                }
            }
            return result;
        }

        private int getNavigationBarWidth(Context context) {
            Resources res = context.getResources();
            int result = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                if (hasNavBar(context)) {
                    return getInternalDimensionSize(res, NAV_BAR_WIDTH_RES_NAME);
                }
            }
            return result;
        }

        private boolean hasNavBar(Context context) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier(SHOW_NAV_BAR_RES_NAME, "bool", "android");
            if (resourceId != 0) {
                boolean hasNav = res.getBoolean(resourceId);
                // check override flag (see static block)
                if ("1".equals(sNavBarOverride)) {
                    hasNav = false;
                } else if ("0".equals(sNavBarOverride)) {
                    hasNav = true;
                }
                return hasNav;
            } else { // fallback
                return !ViewConfiguration.get(context).hasPermanentMenuKey();
            }
        }

        private int getInternalDimensionSize(Resources res, String key) {
            int result = 0;
            int resourceId = res.getIdentifier(key, "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
            return result;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        private float getSmallestWidthDp(Activity activity) {
            DisplayMetrics metrics = new DisplayMetrics();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            } else {
                // TODO this is not correct, but we don't really care pre-kitkat
                activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            }
            float widthDp = metrics.widthPixels / metrics.density;
            float heightDp = metrics.heightPixels / metrics.density;
            return Math.min(widthDp, heightDp);
        }

        /**
         * Should a navigation bar appear at the bottom of the screen in the current
         * device configuration? A navigation bar may appear on the right side of
         * the screen in certain configurations.
         *
         * @return True if navigation should appear at the bottom of the screen, False otherwise.
         */
        public boolean isNavigationAtBottom() {
            return (mSmallestWidthDp >= 600 || mInPortrait);
        }

        /**
         * Get the height of the system status bar.
         *
         * @return The height of the status bar (in pixels).
         */
        public int getStatusBarHeight() {
            return mStatusBarHeight;
        }

        /**
         * Get the height of the action bar.
         *
         * @return The height of the action bar (in pixels).
         */
        public int getActionBarHeight() {
            return mActionBarHeight;
        }

        /**
         * Does this device have a system navigation bar?
         *
         * @return True if this device uses soft key navigation, False otherwise.
         */
        public boolean hasNavigtionBar() {
            return mHasNavigationBar;
        }

        /**
         * Get the height of the system navigation bar.
         *
         * @return The height of the navigation bar (in pixels). If the device does not have
         * soft navigation keys, this will always return 0.
         */
        public int getNavigationBarHeight() {
            return mNavigationBarHeight;
        }

        /**
         * Get the width of the system navigation bar when it is placed vertically on the screen.
         *
         * @return The width of the navigation bar (in pixels). If the device does not have
         * soft navigation keys, this will always return 0.
         */
        public int getNavigationBarWidth() {
            return mNavigationBarWidth;
        }

        /**
         * Get the layout inset for any system UI that appears at the top of the screen.
         *
         * @param withActionBar True to include the height of the action bar, False otherwise.
         * @return The layout inset (in pixels).
         */
        public int getPixelInsetTop(boolean withActionBar) {
            return (mTranslucentStatusBar ? mStatusBarHeight : 0) + (withActionBar ? mActionBarHeight : 0);
        }

        /**
         * Get the layout inset for any system UI that appears at the bottom of the screen.
         *
         * @return The layout inset (in pixels).
         */
        public int getPixelInsetBottom() {
            if (mTranslucentNavBar && isNavigationAtBottom()) {
                return mNavigationBarHeight;
            } else {
                return 0;
            }
        }

        /**
         * Get the layout inset for any system UI that appears at the right of the screen.
         *
         * @return The layout inset (in pixels).
         */
        public int getPixelInsetRight() {
            if (mTranslucentNavBar && !isNavigationAtBottom()) {
                return mNavigationBarWidth;
            } else {
                return 0;
            }
        }
    }
}

