package com.cxd.photor.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.cxd.eventbox.EventBox;
import com.cxd.photor.utils.DensityUtil;

public abstract class BaseActivity extends AppCompatActivity {
    protected Activity context ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        context = this ;

        /*沉浸式*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            DensityUtil.fullScreen(this);
            this.getWindow().getDecorView().setBackgroundColor(Color.parseColor("#333333"));
            this.getWindow().getDecorView().setPadding(
                    0, DensityUtil.getStatusBarHeight(this), 0, 0);
        }

        /*权限判断*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ){
            if((context instanceof BucketActivity || context instanceof PhotoActivity)
                    && ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") ==
                    PackageManager.PERMISSION_DENIED){ //相册选择需要文件读取权限
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return;
            }
        }

        setViews();
        initialize();
        setListeners();
    }

    protected abstract int getLayoutId();

    protected abstract void setViews();

    protected abstract void initialize();

    protected abstract void setListeners();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1 && grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            setViews();
            initialize();
            setListeners();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBox.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBox.getDefault().unregister(this);
    }
}
