package com.cxd.demo;

import android.app.usage.NetworkStats;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cxd.photor.activity.BucketActivity;
import com.cxd.photor.activity.PhotoActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void jumpToBucketActivity(View v){
        this.startActivity(new Intent(this, BucketActivity.class));
    }
    public void jumpToPhotoActivity(View v){
        this.startActivity(new Intent(this, PhotoActivity.class));
    }
}
