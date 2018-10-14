package com.example.htmlviewer.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.htmlviewer.util.ActivityManager;
import com.example.htmlviewer.util.ToastUtil;

/**
 * BaseActivity 
 * <p> 
 * @author yangdu <youngdu29@gmail.com>
 * @createtime 2018/10/14 2:19 PM
 **/
public abstract class BaseActivity extends AppCompatActivity {

    private InputMethodManager mInputManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        ActivityManager.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        ActivityManager.getInstance().removeActivity(this);
        super.onDestroy();
    }

    protected void showToast(String msg) {
        ToastUtil.showToast(msg);
    }

    protected void showToast(@StringRes int msg) {
        ToastUtil.showToast(msg);
    }

    /**
     * 显示软键盘
     * <p>
     * author: hezhiWu
     * created at 2017/3/14 20:31
     */
    public void showSoftInput(final EditText edittext) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                edittext.requestFocus();
                if (mInputManager != null) mInputManager.showSoftInput(edittext, 0);
            }
        }, 700);
    }

    /**
     * 隐藏软键盘
     * <p>
     * author: hezhiWu
     * created at 2017/3/14 20:31
     */
    public void hideSoftInput(EditText et) {
        if (null != mInputManager && mInputManager.isActive()) {
            mInputManager.hideSoftInputFromWindow(et.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void hideKeyboard() {
        if (null != mInputManager && mInputManager.isActive()) {
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(this);
            }
            mInputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 设置全屏
     * @author yangdu <youngdu29@gmail.com>
     * @createtime 2018/10/14 3:51 PM
     */
    protected void setFullScreen(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 退出全屏
     * @author yangdu <youngdu29@gmail.com>
     * @createtime 2018/10/14 3:52 PM
     */
    protected void quitFullScreen(){
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attrs);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

}
