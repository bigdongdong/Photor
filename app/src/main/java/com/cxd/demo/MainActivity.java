package com.cxd.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cxd.photor.OnPhotorListener;
import com.cxd.photor.PDataManager;
import com.cxd.photor.Photor;
import com.cxd.photor.activity.BucketActivity;
import com.cxd.photor.activity.PhotoActivity;
import com.cxd.photor.model.ImgBean;

import java.util.List;

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
        Photor.getInstance()
                .context(this)
                .onPhotoListener(new OnPhotorListener() {
                    @Override
                    public void onSuccess(List<ImgBean> IMGs, String SOURCE) {
                        Log.i("aaa", "onSuccess: IMGs = "+ IMGs);
                        Log.i("aaa", "onSuccess: SOURCE = "+ SOURCE);
                    }
                })
                .requestImgsFromAlbum(9);
    }

    public void jumpToPhotoActivity(View v){
        Photor.getInstance()
                .context(this)
                .onPhotoListener(new OnPhotorListener() {
                    @Override
                    public void onSuccess(List<ImgBean> IMGs, String SOURCE) {
                        Log.i("aaa", "onSuccess: IMGs = "+ IMGs);
                        Log.i("aaa", "onSuccess: SOURCE = "+ SOURCE);
                    }
                })
                .requestImgs(10);
    }
}
