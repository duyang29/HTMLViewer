/*
 *  Copyright(c) 2017 lizhaotailang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.htmlviewer.ui.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.htmlviewer.DataApplication;
import com.example.htmlviewer.R;
import com.example.htmlviewer.common.ConstantSet;
import com.example.htmlviewer.util.ISkipActivityUtil;
import com.example.htmlviewer.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * SettingFragment 
 * <p>
 * @author yangdu <youngdu29@gmail.com>
 * @createtime 2018/10/14 7:38 PM
 **/
public class SettingFragment extends PreferenceFragmentCompat {

    private Preference mURLPreference, mPWDModifyPreference,mClearPreference;
    private ListPreference mHistoryPreference;
    private SwitchPreference mUnlockPreference;

    private SharedPreferences sp;

    private String mDefaultUrl;

    private List<String> mUrlHistoryList;

    private InputMethodManager mInputManager;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_prefs);

        initPrefs();

        mInputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        sp = PreferenceManager.getDefaultSharedPreferences(DataApplication.getInstance());

        //默认路径
        mDefaultUrl = sp.getString(ConstantSet.KEY_SET_URL, "");
        if (!TextUtils.isEmpty(mDefaultUrl)) {
            mURLPreference.setSummary(mDefaultUrl);
//            sp.edit().putString(ConstantSet.KEY_SET_URL, "http://").commit();
        }

        //默认锁定编辑
        mUnlockPreference.setChecked(true);
        mURLPreference.setVisible(false);
        mPWDModifyPreference.setVisible(false);

//        mURLPreference.setEnabled(false);
//        mPWDModifyPreference.setEnabled(false);

        //输入历史
        loadHistory();

        //解锁
        mUnlockPreference.setOnPreferenceClickListener(preference -> {
            if (!mUnlockPreference.isChecked()) {
                //Unlock
                EditText editText = new EditText(SettingFragment.this.getContext());
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editText.setTransformationMethod(new PasswordTransformationMethod());//设置密码不可见
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.tip_pwd_input)
                        .setView(editText)
                        .setPositiveButton(getString(R.string.dialog_confirm), (dialog, which) -> {
                            if (TextUtils.isEmpty(editText.getText())) {
                                ToastUtil.showToast(R.string.tip_pwd_empty);
                                lockSetting(false);
                                return;
                            }

                            String pwd = PreferenceManager.getDefaultSharedPreferences(DataApplication.getInstance()).getString(ConstantSet.KEY_PWD, "");
                            if (!TextUtils.equals(editText.getText(), pwd)) {
                                ToastUtil.showToast(R.string.tip_pwd_wrong);
                                lockSetting(false);
                                return;
                            }
                            unlockSetting();
                            dialog.dismiss();
                        })
                        .setNegativeButton(getString(R.string.dialog_cancel), (dialog, which) -> {
                            lockSetting(false);
                            dialog.dismiss();
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                lockSetting(true);
            }
            return true;
        });

        //输入URL
        mURLPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            String url = (String) newValue;
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(ConstantSet.KEY_SET_URL, url);
            mURLPreference.setSummary(url);
            editor.apply();

            if (null != mInputManager && mInputManager.isActive()) {
//                    mInputManager.hideSoftInputFromWindow(et.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                mInputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }

            refreshHistory(url);

            getActivity().finish();

            return true;
        });

        //修改密码
        mPWDModifyPreference.setOnPreferenceClickListener(preference -> {
            ISkipActivityUtil.startIntent(getActivity(),ModifyPWDActivity.class);
            return true;
        });

        //历史记录
        mHistoryPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue != null) {
                    String url = (String) newValue;
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(ConstantSet.KEY_SET_URL, url);
                    mURLPreference.setSummary(url);
                    editor.apply();

                    if (null != mInputManager && mInputManager.isActive()) {
//                    mInputManager.hideSoftInputFromWindow(et.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        mInputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                    refreshHistory(url);

                    getActivity().finish();

                }

                return true;
            }
        });

        //清除历史
        mClearPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("是否清空历史记录？")
                        .setPositiveButton("清除", (dialog, which) -> {
                            clearCache();
                            dialog.dismiss();
                        })
                        .setNegativeButton("取消",(dialog, which) ->{
                            dialog.dismiss();
                        })
                        .create()
                        .show();
                return true;
            }
        });
    }

    private void loadHistory() {
        String historyURLJson = sp.getString(ConstantSet.KEY_URL_HISTORY, "");
        if (!TextUtils.isEmpty(historyURLJson)) {
            try {
                mUrlHistoryList =new Gson().fromJson(historyURLJson,new TypeToken<List<String>>(){}.getType());
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
            if (mUrlHistoryList != null && mUrlHistoryList.size() > 0) {
                String[] urlEntities = new String[mUrlHistoryList.size()];
                for (int i = 0; i < mUrlHistoryList.size(); i++) {
                    urlEntities[i] = mUrlHistoryList.get(mUrlHistoryList.size() - i - 1);//倒序
                }
                mHistoryPreference.setEntries(urlEntities);
                mHistoryPreference.setEntryValues(urlEntities);
            }
        }
    }

    /**
     * Init the preferences.
     */
    private void initPrefs() {
        mURLPreference = findPreference("pre_url_address");
        mPWDModifyPreference = findPreference("pre_modify_pwd");
        mClearPreference = findPreference("pre_clear_history");
        mUnlockPreference = (SwitchPreference) findPreference("pre_unlock");
        mHistoryPreference = (ListPreference) findPreference("pre_url_history");
    }

    /**
     * Save to History
     * @author yangdu <youngdu29@gmail.com>
     * @createtime 2018/10/14 6:30 PM
     */
    private void refreshHistory(String url) {
        if (mUrlHistoryList == null) {
            mUrlHistoryList = new ArrayList<>();
        }
        if (!mUrlHistoryList.contains(url)) {
            mUrlHistoryList.add(url);
        }
        String historyJson = new Gson().toJson(mUrlHistoryList);
        sp.edit().putString(ConstantSet.KEY_URL_HISTORY, historyJson).apply();

        String[] urlEntities = new String[mUrlHistoryList.size()];
        for (int i = 0; i < mUrlHistoryList.size(); i++) {
            urlEntities[i] = mUrlHistoryList.get(mUrlHistoryList.size() - i - 1);//倒序
        }
        mHistoryPreference.setEntries(urlEntities);
    }

    /**
     * 清空缓存
     * @author yangdu <youngdu29@gmail.com>
     * @createtime 2018/10/14 6:54 PM
     */
    private void clearCache() {
        if (mUrlHistoryList != null) {
            mUrlHistoryList.clear();
        }
        sp.edit().putString(ConstantSet.KEY_URL_HISTORY, "")
                .putString(ConstantSet.KEY_SET_URL, "")
                .apply();
        mHistoryPreference.setEntryValues(null);
        mHistoryPreference.setEntries(null);
        ToastUtil.showToast("清除成功！");
    }

    private void lockSetting(boolean showTip) {
        mUnlockPreference.setChecked(true);
        mURLPreference.setVisible(false);
        mPWDModifyPreference.setVisible(false);
        if(showTip)ToastUtil.showToast(getString(R.string.tip_locked));
    }

    private void unlockSetting() {
        mUnlockPreference.setChecked(false);
        mURLPreference.setVisible(true);
        mPWDModifyPreference.setVisible(true);
        ToastUtil.showToast(getString(R.string.tip_unlocked));
    }

}
