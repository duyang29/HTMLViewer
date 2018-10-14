package com.example.htmlviewer.ui.setting;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.example.htmlviewer.DataApplication;
import com.example.htmlviewer.R;
import com.example.htmlviewer.common.ConstantSet;
import com.example.htmlviewer.ui.BaseActivity;
import com.example.htmlviewer.view.ResetEditView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ModifyPWDActivity
 * <p>
 *
 * @author yangdu <youngdu29@gmail.com>
 * @createtime 2018/10/14 4:42 PM
 **/
public class ModifyPWDActivity extends BaseActivity {

    @BindView(R.id.activity_reset_password_old_editView)
    ResetEditView activityResetPasswordOldEditView;
    @BindView(R.id.activity_reset_password_new_editView)
    ResetEditView activityResetPasswordNewEditView;
    @BindView(R.id.activity_reset_password_new_editView_two)
    ResetEditView activityResetPasswordNewEditViewTwo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        ButterKnife.bind(this);
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("navigation_bar_tint", true)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
        }

        setTitle(R.string.modify_pwd);

    }

    @OnClick(R.id.btn_activity_modify_password_submit)
    public void onViewClicked() {
        if (checkData()) {
            PreferenceManager.getDefaultSharedPreferences(DataApplication.getInstance()).edit().putString(ConstantSet.KEY_PWD, activityResetPasswordNewEditViewTwo.getText()).commit();
            showToast("修改成功！");
            finish();
        }

    }

    //检查修改密码的数据格式
    private boolean checkData() {
        if (TextUtils.isEmpty(activityResetPasswordNewEditView.getText())
                || TextUtils.isEmpty(activityResetPasswordOldEditView.getText())
                || TextUtils.isEmpty(activityResetPasswordNewEditViewTwo.getText())) {
            showToast("密码框不能为空");
            return false;
        }
        String pwd=PreferenceManager.getDefaultSharedPreferences(DataApplication.getInstance()).getString(ConstantSet.KEY_PWD, "");
        if (!TextUtils.equals(activityResetPasswordOldEditView.getText(), pwd)) {
            showToast("原始密码错误");
            return false;
        }
        if (!TextUtils.equals(activityResetPasswordNewEditView.getText(), activityResetPasswordNewEditViewTwo.getText())) {
            showToast("新密码前后输入不一致");
            return false;
        }
        return true;
    }


}
