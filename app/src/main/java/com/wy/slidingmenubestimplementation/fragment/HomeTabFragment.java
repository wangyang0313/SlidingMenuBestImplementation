package com.wy.slidingmenubestimplementation.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wy.slidingmenubestimplementation.MainActivity;
import com.wy.slidingmenubestimplementation.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 首页
 */

public class HomeTabFragment extends Fragment {

    @BindView(R.id.tv_titlebar_center)
    TextView tvTitlebarCenter;
    @BindView(R.id.iv_titlebar_left)
    ImageView ivTitlebarLeft;
    @BindView(R.id.iv_titlebar_right)
    ImageView ivTitlebarRight;
    private Unbinder unbinder;
    private Context context;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        if (view == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                view = View.inflate(context, R.layout.fragment_tab_home_v19, null);
            } else {
                view = View.inflate(context, R.layout.fragment_tab_home, null);
            }
        }
        unbinder = ButterKnife.bind(this, view);


        return view;
    }

    @OnClick({R.id.iv_titlebar_left, R.id.iv_titlebar_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_titlebar_left://左侧滑菜单栏
                if (MainActivity.slidingMenu != null) {
                    MainActivity.slidingMenu.showMenu();
                }
                break;
            case R.id.iv_titlebar_right://右侧滑菜单栏
                if (MainActivity.slidingMenu != null) {
                    MainActivity.slidingMenu.showSecondaryMenu();
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
