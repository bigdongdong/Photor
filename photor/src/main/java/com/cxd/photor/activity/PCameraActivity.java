package com.cxd.photor.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

import com.cxd.photor.PDataManager;
import com.cxd.photor.model.ImgBean;

import java.io.File;

public class PCameraActivity extends PBaseActivity {

    private File outputImage ;
    private Uri photoUri;

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void setViews() {

    }

    @Override
    protected void initialize() {
        File directory = new File(this.getExternalCacheDir().getPath() + "/Photor");
        if(!directory.exists() || !directory.isDirectory()){
            directory.mkdirs();
        }

        //创建file对象，用于存储拍照后的图片，这也是拍照成功后的照片路径
        long now = System.currentTimeMillis();
        outputImage = new File(directory,now+".jpg");

        //判断当前Android版本
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            photoUri = FileProvider.getUriForFile(this,
                    this.getApplication().getApplicationInfo().processName+".FileProvider",outputImage);
        }else {
            photoUri = Uri.fromFile(outputImage);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
        startActivityForResult(intent,111);
    }

    @Override
    protected void setListeners() {

    }

    public static void jump(Context context){
        if(context != null){
            Intent intent = new Intent(context, PCameraActivity.class);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //拍照返回
                case 111:
                    //这就是拍照成功后的图片的路径，因为刚开始我们创建了这个图片的路径
                    ImgBean ib = new ImgBean();
                    ib.setUrl(Uri.fromFile(outputImage).toString());
                    ib.setBucketName("Photor");
                    PDataManager.getInstance().addImg(ib);
                    PDataManager.getInstance().commit();
                    break;
            }
        }

        finish();
    }
}
