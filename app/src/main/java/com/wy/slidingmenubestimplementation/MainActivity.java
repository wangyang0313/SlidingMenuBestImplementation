package com.wy.slidingmenubestimplementation;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.wy.slidingmenubestimplementation.customview.RoundImageView;
import com.wy.slidingmenubestimplementation.menu.MainTabs;
import com.wy.slidingmenubestimplementation.util.DensityUtil;
import com.wy.slidingmenubestimplementation.util.ToastUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.tabHost)
    FragmentTabHost tabHost;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;
    public static Context context;
    public static MainActivity instance = null;
    private final String TAG = "MainActivity";
    public static SlidingMenu slidingMenu;
    public static boolean slidingMenuToggleStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;
        instance = this;
        //判断当前设备版本号是否为4.4以上，如果是，则通过调用setTranslucentStatus让状态栏变透明
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        initFragmentTabHost();
        initSlidingMenu();
    }

    /**
     * 初始化FragmentTabHost
     */
    private void initFragmentTabHost() {
        //初始化tabHost
        FragmentTabHost tabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        //将tabHost和FragmentLayout关联
        tabHost.setup(context, getSupportFragmentManager(), R.id.fl_content);

        //去掉分割线
        if (Build.VERSION.SDK_INT > 10) {
            tabHost.getTabWidget().setShowDividers(0);
        }
        //添加tab和其对应的fragment
        MainTabs[] tabs = MainTabs.values();
        for (int i = 0; i < tabs.length; i++) {
            MainTabs mainTabs = tabs[i];
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(mainTabs.getName());

            View indicator = View.inflate(context, R.layout.tab_indicator, null);
            TextView tv_indicator = (TextView) indicator.findViewById(R.id.tv_indicator);
            Drawable drawable = context.getResources().getDrawable(mainTabs.getIcon());

            tv_indicator.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            tv_indicator.setText(mainTabs.getName());
            tabSpec.setIndicator(indicator);
            tabHost.addTab(tabSpec, mainTabs.getCla(), null);
        }
    }

    /**
     * 初始化SlidingMenu实现侧滑菜单栏
     */
    private void initSlidingMenu() {
        slidingMenu = new SlidingMenu(this);
        //设置呈现模式（分为左、右、左右三种）
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        //设置侧滑界面偏移出的尺寸(剩余部分)
        slidingMenu.setBehindOffset(DensityUtil.dip2px(100, context));
        //设置剩余部分的阴影
        slidingMenu.setOffsetFadeDegree(0.4f);
        //设置全屏都可以触摸
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置菜单内可以滑动（会导致菜单内的点击事件无效）
//        slidingMenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //添加到当前Activity当中
        //（SLIDING_WINDOW配合沉浸式状态栏，SLIDING_CONTENT配合非沉浸式状态栏）
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        //给侧滑界面传入View，与点击事件使用同一个View
        //如果给侧滑界面引用一个布局资源作为所展示的界面，点击事件无效，
        //因为侧滑界面和点击事件是不同的View
        View view = View.inflate(context, R.layout.my_sliding_menu, null);
        slidingMenu.setMenu(view);
        slidingMenu.setSecondaryMenu(R.layout.my_sliding_menu_right);

        initSlidingMenuButton(view);

        //openListener仅在侧滑菜单栏开启的时候调用；
        //openedListener在侧滑菜单栏开启后调用（open之后）；
        //      并且在滑动且未关闭的时候再次调用。
        //      （也就是滑动未关闭然后又回到开启的状态时再次调用）
        slidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
            @Override
            public void onOpened() {
                slidingMenuToggleStatus = true;
            }
        });

        //closeListener仅在侧滑菜单栏开启的时候调用；
        //closedListener在侧滑菜单栏开启后调用（close之后）；
        //      并且在滑动且未关闭的时候再次调用。
        //      （也就是滑动未关闭然后又回到开启的状态时再次调用）
        slidingMenu.setOnClosedListener(new SlidingMenu.OnClosedListener() {
            @Override
            public void onClosed() {
                slidingMenuToggleStatus = false;
            }
        });
    }

    /**
     * 初始化侧滑菜单栏中的按钮
     *
     * @param view
     */
    private void initSlidingMenuButton(View view) {
        RoundImageView rivHead = (RoundImageView) view.findViewById(R.id.riv_head);
        TextView tvUsername = (TextView) view.findViewById(R.id.tv_username);
        TextView tvDepartment = (TextView) view.findViewById(R.id.tv_department);
        LinearLayout llHome = (LinearLayout) view.findViewById(R.id.ll_home);
        LinearLayout llOrder = (LinearLayout) view.findViewById(R.id.ll_order);
        LinearLayout llStatistic = (LinearLayout) view.findViewById(R.id.ll_statistic);
        LinearLayout llRefresh = (LinearLayout) view.findViewById(R.id.ll_refresh);
        LinearLayout llLogout = (LinearLayout) view.findViewById(R.id.ll_logout);

        llHome.setOnClickListener(this);
        llOrder.setOnClickListener(this);
        llStatistic.setOnClickListener(this);
        llRefresh.setOnClickListener(this);
        llLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_home://首页
                tabHost.setCurrentTab(0);
                slidingMenu.toggle();
                break;
            case R.id.ll_order://订单
                tabHost.setCurrentTab(1);
                slidingMenu.toggle();
                break;
            case R.id.ll_statistic://统计
                tabHost.setCurrentTab(2);
                slidingMenu.toggle();
                break;
            case R.id.ll_refresh://检查更新
                ToastUtil.makeText(context,"点击了检查更新");
                slidingMenu.toggle();
                break;
            case R.id.ll_logout://切换用户/注销
                ToastUtil.makeText(context,"点击了切换用户/注销");
                slidingMenu.toggle();
                break;
            default:
                break;
        }
    }

    private long mExitTime;
    private long mSlidingMenuTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //防止频繁点击返回键，导致侧滑一直在滑动状态
            //可以放弃时间的判断，当成个小玩意
            if (slidingMenuToggleStatus == true) {
                if ((System.currentTimeMillis() - mSlidingMenuTime) > 1000){
                    mSlidingMenuTime = System.currentTimeMillis();
                    slidingMenu.toggle();
                }
                return true;
            }

            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtil.makeText(context, "再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                //模拟Home键操作
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            return true;
        }

        //拦截MENU按钮点击事件，让它无任何操作
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
