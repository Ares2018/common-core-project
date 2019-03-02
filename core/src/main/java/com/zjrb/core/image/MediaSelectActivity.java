package com.zjrb.core.image;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zjrb.core.R;
import com.zjrb.core.base.BaseActivity;
import com.zjrb.core.dialog.DialogFragmentImpl;
import com.zjrb.core.dialog.OkDialog;
import com.zjrb.core.permission.AbsPermCallBack;
import com.zjrb.core.permission.AbsPermSingleCallBack;
import com.zjrb.core.permission.Permission;
import com.zjrb.core.permission.PermissionManager;
import com.zjrb.core.recycleView.listener.OnItemClickListener;
import com.zjrb.core.ui.divider.GridSpaceDivider;
import com.zjrb.core.utils.AppUtils;
import com.zjrb.core.utils.PathManager;
import com.zjrb.core.utils.click.ClickTracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * 多媒体资源选择 页面
 *
 * @author a_liYa
 * @date 16/10/21 下午9:11.
 */
public class MediaSelectActivity extends BaseActivity implements View.OnClickListener,
        OnItemClickListener, LocalMediaAdapter.Callback, DialogFragmentImpl.CreateDialog {

    TextView tvComplete;
    RecyclerView recycler;
    FrameLayout previewFrame;

    LocalMediaAdapter adapter;

    /**
     * 可选择最大个数, 默认1个
     */
    private int maxNum = 1;
    private boolean isShowSelectedNum = true;
    private String mTakePicPath;
    private File mTakePicFile;

    /**
     * 最大个数
     */
    public static final String MAX_NUM = "max_num";
    /**
     * 选择的照片返回数据 key
     */
    public static final String KEY_DATA = "key_data";

    /**
     * JS回调判定
     */
    private String JSCallBack;

    public static final String SHOW_SELECTED_NUM = "show_selected_num";
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_core_activity_media_select);
        initView();
        initState(savedInstanceState);
        initPermission();
        //JS调用判定
        if (getIntent() != null && getIntent().hasExtra("callback")) {
            JSCallBack = getIntent().getStringExtra("callback");
        }
    }


    private void initPermission() {
        PermissionManager.get().request(this, new AbsPermCallBack() {
            @Override
            public void onGranted(boolean isAlreadyDef) {
                new MediaQueryTask().execute();
            }

            @Override
            public void onDenied(List<String> neverAskPerms) {
                Toast.makeText(getApplicationContext(), "选择照片需要访问权限",
                        Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }, Permission.STORAGE_READE);
    }

    @Override
    public boolean isShowTopBar() {
        return false;
    }

    private void initView() {

        recycler = (RecyclerView) findViewById(R.id.recycler);
        previewFrame = (FrameLayout) findViewById(R.id.frame);
        tvComplete = (TextView) findViewById(R.id.tv_complete);
        findViewById(R.id.iv_back).setOnClickListener(this);
        tvComplete.setOnClickListener(this);

        recycler.setLayoutManager(new GridLayoutManager(this, 3));
        recycler.addItemDecoration(new GridSpaceDivider(3));

    }

    private void initState(Bundle savedState) {
        if (savedState != null) {
            maxNum = savedState.getInt(MAX_NUM, maxNum);
            isShowSelectedNum = savedState.getBoolean(SHOW_SELECTED_NUM, true);
        } else {
            maxNum = getIntent().getIntExtra(MAX_NUM, maxNum);
            isShowSelectedNum = getIntent().getBooleanExtra(SHOW_SELECTED_NUM, true);
        }
    }

    @Override
    protected void onDestroy() {
        Glide.get(this).clearMemory();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (ClickTracker.isDoubleClick()) return;

        int id = v.getId();
        if (R.id.iv_back == id) {
            onBackPressed();
        } else if (R.id.tv_complete == id) {
            complete();
        }
    }

    private void complete() {
        ArrayList<MediaEntity> list = adapter.getSelectedList();
        if (list != null && list.size() > 0) {
            Intent data = new Intent();
            data.putParcelableArrayListExtra(KEY_DATA, list);
            //回传判定
            if (!TextUtils.isEmpty(JSCallBack)) {
                data.putExtra("callback", JSCallBack);
            }
            setResult(RESULT_OK, data);
        }
        onBackPressed();
    }

    private void takePicture() {
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mTakePicFile = PathManager.get().obtainTakePicFile();
        if (mTakePicFile == null) return;
        mTakePicPath = mTakePicFile.getAbsolutePath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTakePicFile));
        PermissionManager.get().request(this, new AbsPermSingleCallBack() {
            @Override
            public void onGranted(boolean isAlreadyDef) {
                startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
            }

            @Override
            public void onDenied(List<String> neverAskPerms) {
                Toast.makeText(getActivity(), "拍照必须要允许使用相机权限", Toast.LENGTH_SHORT).show();
            }
        }, Permission.CAMERA);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (previewFrame.getVisibility() == View.VISIBLE) {
                previewFrame.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TAKE_PICTURE && resultCode == RESULT_OK) {
            finish();

            // 扫描拍照的图片,
            AppUtils.scanFile(mTakePicPath);

            // 以下是同步操作,但比较耗时 3M的照片耗时1233ms
            try {
                MediaStore.Images.Media.insertImage(getContentResolver(),
                        mTakePicPath, mTakePicPath.substring(mTakePicPath.lastIndexOf("/") + 1),
                        null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemClick(View itemView, int position) {
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (adapter != null) {
            ArrayList<MediaEntity> list = adapter.getSelectedList();
            if (list == null || list.isEmpty() || !isShowSelectedNum) {
                tvComplete.setText("完成");
            } else {
                tvComplete.setText(list.size() + " 完成");
            }
        }
    }

    @Override
    public void onSelectedLimit(int limit) {
        new DialogFragmentImpl().show(getSupportFragmentManager(), "提示");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState, int id) {
        return new OkDialog(getActivity())
                .setMsg("你最多只能选择" + maxNum + "张照片")
                .setOkText("确定");
    }

    /**
     * 异步查询资源文件
     *
     * @author a_liYa
     * @date 16/10/21 下午10:17.
     */
    private class MediaQueryTask extends AsyncTask<Integer, Integer, List<MediaEntity>> {

        @Override
        protected List<MediaEntity> doInBackground(Integer... params) {
            return new LocalMediaDaoHelper().queryImageMedia();
        }

        @Override
        protected void onPostExecute(List<MediaEntity> results) {
            adapter = new LocalMediaAdapter(results, maxNum, MediaSelectActivity.this);
            adapter.setOnItemClickListener(MediaSelectActivity.this);
            recycler.setAdapter(adapter);
        }
    }

}
