package com.wy.slidingmenubestimplementation.util;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * 下一个Toast覆盖上一个Toast
 */
public class ToastUtil {
    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };

    public static void makeText(Context mContext, String text) {

        mHandler.removeCallbacks(r);
        if (mToast != null)
            mToast.setText(text);
        else
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
        mHandler.postDelayed(r, 2000);

        mToast.show();
    }

//    public static void makeText(Context mContext, int resId, int duration) {
//        makeText(mContext, mContext.getResources().getString(resId), duration);
//    }
}
