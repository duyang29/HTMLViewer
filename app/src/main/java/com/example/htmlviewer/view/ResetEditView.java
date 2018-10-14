package com.example.htmlviewer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.htmlviewer.R;

import butterknife.ButterKnife;

/**
 * ResetEditView
 * <p> 带重置内置的EditView
 * @author yangdu <youngdu29@gmail.com>
 * @createtime 2018/10/14 1:56 PM
 **/
public class ResetEditView extends LinearLayout implements TextWatcher {

    private ImageView resetEditViewIconIv;
    private EditText resetEditView;
    private ImageView resetEditViewCloseIv;
    private TextView lineTextView,lineFocusTextView;

    public ResetEditView(Context context) {
        super(context, null);
    }

    public ResetEditView(Context context, AttributeSet attri) {
        super(context, attri, 0);
        init(context);
        parseAttrs(context, attri, 0);
    }

    /**
     * 初始化
     * <p>
     * author: hezhiWu
     * created at 2017/3/17 17:09
     */
    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.reset_editeview_layout, null);
        resetEditViewIconIv = ButterKnife.findById(view, R.id.reset_editView_icon_iv);
        resetEditView = ButterKnife.findById(view, R.id.reset_editView);
        resetEditViewCloseIv = ButterKnife.findById(view, R.id.reset_editView_close_iv);
        lineTextView= ButterKnife.findById(view,R.id.reset_line_textView);
        lineFocusTextView= ButterKnife.findById(view,R.id.reset_line_focus_textView);

        resetEditView.addTextChangedListener(this);
        resetEditViewCloseIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                resetEditView.getText().clear();
                resetEditViewCloseIv.setVisibility(GONE);
            }
        });

//        resetEditView.setOnFocusChangeListener(new OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus){
//                    lineFocusTextView.setVisibility(VISIBLE);
//                    lineTextView.setVisibility(GONE);
//                }else {
//                    lineFocusTextView.setVisibility(GONE);
//                    lineTextView.setVisibility(VISIBLE);
//                }
//            }
//        });

        addView(view);
    }

    /**
     * 初始化样式
     * <p>
     * author: hezhiWu
     * created at 2017/3/17 17:08
     */
    private void parseAttrs(Context context, AttributeSet attrs, int defStyle) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ResetEditView, defStyle, 0);
        Drawable drawable = typedArray.getDrawable(R.styleable.ResetEditView_edite_icon);
        int type = typedArray.getInt(R.styleable.ResetEditView_edit_inputType, InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        String hint = typedArray.getString(R.styleable.ResetEditView_edit_hint);

        if (drawable != null) {
            resetEditViewIconIv.setImageDrawable(drawable);
        }

        if (type == 0x10) {
            resetEditView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            resetEditView.setInputType(type);
        }

        if (!TextUtils.isEmpty(hint)) {
            resetEditView.setHint(hint);
        }

        typedArray.recycle();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s)) {
            resetEditViewCloseIv.setVisibility(GONE);
        } else {
            resetEditViewCloseIv.setVisibility(VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    /**
     * 设置内容
     * <p>
     * author: hezhiWu
     * created at 2017/3/17 17:09
     *
     * @param text
     */
    public void setText(String text) {
        resetEditView.setText(text);
    }

    /**
     * 设置内容
     * <p>
     * author: hezhiWu
     * created at 2017/3/17 17:09
     *
     * @param resId
     */
    public void setText(int resId) {
        resetEditView.setText(resId);
    }

    /**
     * 返回内容
     * <p>
     * author: hezhiWu
     * created at 2017/3/17 17:09
     *
     * @return
     */
    public String getText() {
        return resetEditView.getText().toString();
    }

    public EditText getResetEditView() {
        return resetEditView;
    }
}
