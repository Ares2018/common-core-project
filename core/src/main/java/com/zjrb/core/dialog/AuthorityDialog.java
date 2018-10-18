package com.zjrb.core.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.core.R;
import com.zjrb.core.base.BaseActivity;
import com.zjrb.core.permission.PermissionGroup;
import com.zjrb.core.load.ILoad;
import com.zjrb.core.utils.AppUtils;

import java.util.ArrayList;


public class AuthorityDialog extends BaseActivity implements View.OnClickListener {
    public static final String DENIED_PERMISSION = "denied_permission";
    public static final String ICON = "icon";

    private ImageView mIconView;
    private TextView mMessageView;
    private TextView mCancelView;
    private TextView mSettingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_core_authority_dialog);
        mIconView = (ImageView) findViewById(R.id.authority_icon);
        mMessageView = (TextView) findViewById(R.id.authority_message);
        mCancelView = (TextView) findViewById(R.id.authority_cancel);
        mCancelView.setOnClickListener(this);
        mSettingView = (TextView) findViewById(R.id.authority_setting);
        mSettingView.setOnClickListener(this);
        if (getIntent().getIntExtra(ICON, 0) != 0) {
            mIconView.setImageResource(getIntent().getIntExtra(ICON, 0));
        }

        ArrayList<String> deniedPerms = getIntent().getStringArrayListExtra(DENIED_PERMISSION);
        if (deniedPerms != null && deniedPerms.size() > 0) {
            ArrayList<String> permission = new ArrayList<>();
            for (String string : deniedPerms) {
                String name = PermissionGroup.getGroupName(string);
                if (name != null && !permission.contains(name)) {
                    permission.add(name);
                }
            }
            String permissionInfo = TextUtils.join("、", permission);
            String message = String.format(getString(R.string.authority_tip), permissionInfo, permissionInfo);
            mMessageView.setText(message);
        } else {
            String message = String.format(getString(R.string.authority_tip), "相机权限", "相机权限");
            mMessageView.setText(message);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.authority_setting) {
            AppUtils.openSetting(this);
            setResult(Activity.RESULT_OK);
        } else if (v.getId() == R.id.authority_cancel) {
            setResult(Activity.RESULT_CANCELED);
        }
        finish();
    }

    @Override
    public ILoad replaceLoad(ViewGroup parent, View pageView) {
        return null;
    }

    @Override
    public boolean isShowTopBar() {
        return false;
    }
}
