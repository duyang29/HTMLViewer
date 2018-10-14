package com.example.htmlviewer.ui.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.example.htmlviewer.DataApplication;
import com.example.htmlviewer.R;
import com.example.htmlviewer.common.ConstantSet;
import com.example.htmlviewer.ui.BaseActivity;
import com.example.htmlviewer.ui.setting.SettingActivity;
import com.example.htmlviewer.util.ActivityManager;
import com.example.htmlviewer.util.ISkipActivityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yangdu <youngdu29@gmail.com>
 * @desc MainActivity
 * @createtime 2018/10/14 1:17 PM
 **/
public class MainActivity extends BaseActivity {

    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.layout_title)
    RelativeLayout mLayoutTitle;

    private long mLastExitClickTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        loadWebView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String defaultUrl = PreferenceManager.getDefaultSharedPreferences(DataApplication.getInstance()).getString(ConstantSet.KEY_SET_URL, "");
        if (!TextUtils.isEmpty(defaultUrl) && !TextUtils.equals(defaultUrl, mWebView.getUrl())) {
//            quitFullScreen();//退出全屏
//            mLayoutTitle.setVisibility(View.VISIBLE);

            mWebView.clearHistory();
            mWebView.loadUrl(defaultUrl);
        }
        hideKeyboard();
    }

    @OnClick(R.id.img_setting)
    public void onViewClicked() {
        ISkipActivityUtil.startIntent(MainActivity.this, SettingActivity.class);
    }

    /**
     * 加载MapView
     */
    private void loadWebView() {
        //加载WebView
        String defaultUrl = PreferenceManager.getDefaultSharedPreferences(DataApplication.getInstance()).getString(ConstantSet.KEY_SET_URL, null);
        defaultUrl=!TextUtils.isEmpty(defaultUrl)&&!TextUtils.equals(defaultUrl,"")?defaultUrl:"https://www.baidu.com/";
        mWebView.loadUrl(defaultUrl);
        mWebView.addJavascriptInterface(MainActivity.this, "Android");//"Android"是Fragment暴露给JS的对象
        mWebView.setWebViewClient(webViewClient);
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int flag = getWindow().getAttributes().flags;
                if ((flag & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                        == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
                    quitFullScreen();//退出全屏
                    mLayoutTitle.setVisibility(View.VISIBLE);
                } else {
                    setFullScreen();//设置全屏
                    mLayoutTitle.setVisibility(View.GONE);
                }
                return false;
            }
        });
        initWebSettings();
    }

    /**
     * 设置WebSettings 开启定位权限
     */
    private void initWebSettings() {
        WebSettings webSettings = mWebView.getSettings();
        //webview支持js脚本
        webSettings.setJavaScriptEnabled(true);

        //支持缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);

        //启用数据库
        webSettings.setDatabaseEnabled(true);

        //开启DomStorage缓存
        webSettings.setDomStorageEnabled(true);

        //设置定位的数据库路径
        String dir = DataApplication.getInstance().getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setGeolocationDatabasePath(dir);

        //启用地理定位
        webSettings.setGeolocationEnabled(true);

        //配置权限
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);

            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

        });
    }


    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            mWebView.loadUrl("javascript:window.addEventListener('DOMContentLoaded',function(){Android.resize(document.body.clientHeight)})");
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mWebView.getProgress() == 100) {
                //JS注入调用native方法重新设置高度
//                mWebView.loadUrl("javascript:Android.resize(document.body.getBoundingClientRect().height)");//document.body.scrollHeight
                setFullScreen();//设置全屏
                mLayoutTitle.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }
    };

    /**
     * JS调用android的方法
     *
     * @param str
     * @return
     */
    @JavascriptInterface
    public void getClient(String str) {
        //js 调用客户端
    }

    /**
     * 通过JS注入，前端JS调用native方法，把document的高度传递native，然后通过LayoutParams设置WebView高度
     *
     * @param height
     */
    @JavascriptInterface
    public void resize(final float height) {
        runOnUiThread(() -> {
            if (height > 0) {
                ViewGroup.LayoutParams layoutParams = mWebView.getLayoutParams();
                layoutParams.width = getResources().getDisplayMetrics().widthPixels;
                layoutParams.height = (int) (height * getResources().getDisplayMetrics().density);
                mWebView.setLayoutParams(layoutParams);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
//        menu.add(0, 1, 0, "进入设置");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.menu.main:
                ISkipActivityUtil.startIntent(MainActivity.this, SettingActivity.class);
                break;
        }
        return true;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("ansen", "是否有上一个页面:" + mWebView.canGoBack());
        if (mWebView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {//点击返回按钮的时候判断有没有上一页
            mWebView.goBack(); // goBack()表示返回webView的上一页面
            return true;
        } else {
            exitApp();
            return false;
        }
    }

    /**
     * APP退出
     *
     * @author yangdu <youngdu29@gmail.com>
     * @createtime 2018/10/14 3:44 PM
     */
    private void exitApp() {
        if (mLastExitClickTime == 0 || System.currentTimeMillis() - mLastExitClickTime >= 2000) {
            showToast("再按一次退出!");
            mLastExitClickTime = System.currentTimeMillis();
        } else {
            ActivityManager.getInstance().exit();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放资源
        mWebView.destroy();
        mWebView = null;
    }

}
