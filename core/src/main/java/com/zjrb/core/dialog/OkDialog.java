package com.zjrb.core.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.zjrb.core.R;

/**
 * 确定对话框 提示信息 + 确定按钮
 *
 * @author a_liYa
 * @date 2017/9/14 17:30.
 */
public class OkDialog extends android.app.AlertDialog implements View.OnClickListener {

    private View view;

    private Button btnOk;
    private TextView tvMsg;

    private OnClickCallback mOnClickCallback;

    public OkDialog(Context context) {
        super(context, android.R.style.Theme_Dialog);
        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
        configDialog();
    }

    private void configDialog() {
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        //设置对话框居中
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = getScreenWidthPx() * 5 / 6;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //因为某些机型是虚拟按键的,所以要加上以下设置防止挡住按键.
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public int getScreenWidthPx() {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        return Math.min(dm.widthPixels, dm.heightPixels);
    }

    public OnClickCallback getOnClickCallback() {
        return mOnClickCallback;
    }

    public void setOnClickCallback(OnClickCallback clickCallback) {
        mOnClickCallback = clickCallback;
    }

    private void initView() {
        view = LayoutInflater.from(getContext()).inflate(
                R.layout.module_core_dialog_media_select_alert, null);
        btnOk = (Button) view.findViewById(R.id.btn_ok);
        tvMsg = (TextView) view.findViewById(R.id.tv_msg);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_ok) {
            if (mOnClickCallback != null) {
                mOnClickCallback.onOkClick(v);
            }
        }
        dismiss();
    }

    public OkDialog setOkText(String text) {
        if (btnOk != null) {
            btnOk.setText(text);
        }
        return this;
    }

    public OkDialog setMsg(String msg) {
        if (tvMsg != null) {
            tvMsg.setText(msg);
        }
        return this;
    }

    public interface OnClickCallback {

        void onOkClick(View v);

    }

}
