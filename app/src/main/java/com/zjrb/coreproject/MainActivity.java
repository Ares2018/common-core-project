package com.zjrb.coreproject;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.zjrb.core.base.BaseActivity;
import com.zjrb.core.load.LoadingIndicatorDialog;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LoadingIndicatorDialog loadingIndicatorDialog=new LoadingIndicatorDialog(this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingIndicatorDialog.show();
                loadingIndicatorDialog.setToastText("加载中...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingIndicatorDialog.finish(true);
                    }
                },1000);
            }
        });




    }

}
