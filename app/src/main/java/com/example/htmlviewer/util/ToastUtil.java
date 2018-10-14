package com.example.htmlviewer.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.widget.Toast;

import com.example.htmlviewer.DataApplication;

/**
 * ToastUtil 
 * <p>
 * @author yangdu <youngdu29@gmail.com>
 * @createtime 2018/10/14 2:26 PM
 **/
public class ToastUtil {

    private static final String TAG = "IToastUtils";
    static Toast mToast = null;

    public static void showToast(String msg) {
        showToast(DataApplication.getInstance(),msg);
    }

    public static void showToast(@StringRes int msg) {
        showToast(DataApplication.getInstance(),DataApplication.getInstance().getString(msg));
    }

    public static void showToast(Context context, int resId) {
        if (context == null || ((Activity) context).isFinishing()) {
            return;
        }
        showToast(context, context.getString(resId));
    }

    public static void showToast(Context context, String msg) {
        try {
            if (context == null) {
                return;
            }
            if (mToast == null) {
                synchronized (ToastUtil.class) {
                    if (mToast == null) {
                        mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                    } else {
                        mToast.setText(msg);
                        mToast.setDuration(Toast.LENGTH_SHORT);
                    }
                }
            } else {
                mToast.setText(msg);
                mToast.setDuration(Toast.LENGTH_SHORT);
            }
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
