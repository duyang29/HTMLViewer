package com.example.htmlviewer.ui.login;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.htmlviewer.R;
import com.example.htmlviewer.ui.BaseActivity;
import com.example.htmlviewer.ui.main.MainActivity;
import com.example.htmlviewer.util.ISkipActivityUtil;
import com.example.htmlviewer.util.SystemUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * SplashActivity
 * <p>
 *
 * @author yangdu <youngdu29@gmail.com>
 * @createtime 2018/10/14 2:28 PM
 **/
public class SplashActivity extends BaseActivity {

    @BindView(R.id.tv_app_version)
    TextView tvAppVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        tvAppVersion.setText(String.format("V%s",SystemUtil.getVersionName()));
        new Handler().postDelayed(() -> {
            ISkipActivityUtil.startIntent(SplashActivity.this, MainActivity.class);
            finish();
        }, 1200L);

    }
}
