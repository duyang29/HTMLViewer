package com.example.htmlviewer;

import android.app.Application;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.htmlviewer.common.ConstantSet;

/**
 * DataApplication 
 * <p>
 * @author yangdu <youngdu29@gmail.com>
 * @createtime 2018/10/14 2:23 PM
 **/
public class DataApplication extends Application {

    private static DataApplication INSTANCE;


    public static DataApplication getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        initData();
    }

    private void initData() {
        if (TextUtils.isEmpty(PreferenceManager.getDefaultSharedPreferences(DataApplication.getInstance()).getString(ConstantSet.KEY_PWD, ""))) {
            PreferenceManager.getDefaultSharedPreferences(DataApplication.getInstance()).edit().putString(ConstantSet.KEY_PWD, "123456").commit();
        }
    }


}
