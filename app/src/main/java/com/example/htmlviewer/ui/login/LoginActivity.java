package com.example.htmlviewer.ui.login;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;

import com.example.htmlviewer.DataApplication;
import com.example.htmlviewer.R;
import com.example.htmlviewer.common.ConstantSet;
import com.example.htmlviewer.ui.BaseActivity;
import com.example.htmlviewer.ui.main.MainActivity;
import com.example.htmlviewer.util.ISkipActivityUtil;
import com.example.htmlviewer.view.ResetEditView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * LoginActivity
 * <p> 登陆
 * @author yangdu <youngdu29@gmail.com>
 * @createtime 2018/10/14 1:43 PM
 **/
public class LoginActivity extends BaseActivity {

    @BindView(R.id.edit_account)
    ResetEditView editAccount;
    @BindView(R.id.edt_pwd)
    ResetEditView edtPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        editAccount.setText("html");
        edtPwd.getResetEditView().requestFocus();
    }


    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        // TODO: 2018/10/14 验证密码
        if (TextUtils.isEmpty(editAccount.getText())) {
            showToast(R.string.tip_account_empty);
            return;
        }

        if (TextUtils.isEmpty(edtPwd.getText())) {
            showToast(R.string.tip_pwd_empty);
            return;
        }

        String pwd= PreferenceManager.getDefaultSharedPreferences(DataApplication.getInstance()).getString(ConstantSet.KEY_PWD, "");
        if (!TextUtils.equals(edtPwd.getText(), pwd)) {
            showToast(R.string.tip_pwd_wrong);
            return;
        }

        ISkipActivityUtil.startIntent(LoginActivity.this, MainActivity.class);
    }
}
