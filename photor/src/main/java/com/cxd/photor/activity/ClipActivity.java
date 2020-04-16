package com.cxd.photor.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cxd.clipview.ClipImageView;
import com.cxd.eventbox.EventBox;
import com.cxd.photor.PCropOption;
import com.cxd.photor.Photor;
import com.cxd.photor.R;
import com.cxd.photor.model.ImgBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * create by cxd on 2020/4/6
 */
public class ClipActivity extends BaseActivity {

    private ClipImageView civ ;
    private TextView cancelTV , confirmTV ;
    private ArrayList<ImgBean> mImgs;
    private PCropOption mPCropOption;
    private Iterator<ImgBean> itImgs ;
    private ImgBean curImgBean ;

    public static void jump(Context context , ArrayList<ImgBean> imgs , int width , int height ){
        PCropOption pCropOption = new PCropOption(width,height);
        if(context != null && imgs != null && imgs.size() >0){
            Intent intent = new Intent(context,ClipActivity.class);
            intent.putExtra("imgs",imgs);
            intent.putExtra("pCropOption",pCropOption);
            context.startActivity(intent);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_clip;
    }

    @Override
    protected void setViews() {
        civ = findViewById(R.id.civ);
        cancelTV = findViewById(R.id.cancelTV);
        confirmTV = findViewById(R.id.confirmTV);
    }

    @Override
    protected void initialize() {
        mImgs = (ArrayList<ImgBean>) getIntent().getSerializableExtra("imgs");
        itImgs = mImgs.iterator();

        /*设置第一张图片*/
        curImgBean = itImgs.next();
        Glide.with(context).load(curImgBean.getUrl()).into(civ);

        mPCropOption = (PCropOption) getIntent().getSerializableExtra("pCropOption");

        if(mPCropOption != null){
            civ.setCropWindowSize(mPCropOption.getWidth(),mPCropOption.getHeight());
        }
    }


    @Override
    protected void setListeners() {
        cancelTV.setOnClickListener(v -> finish());
        confirmTV.setOnClickListener(v -> {
            Bitmap bm = null;
            try{
                bm = civ.getCropBitmapWithZip();
            }catch (Exception e){}

            /*
            一般点击过快会导致bm来不及裁剪，直接return，
            让其下一次点击的时候继续裁剪*/
            if(bm == null){
                return;
            }

            /*处理bm*/
            curImgBean.setUrl(getFileFromBitmap(curImgBean,bm));

            if(itImgs.hasNext()){
                /*继续裁剪*/
                curImgBean = itImgs.next();
                Glide.with(context).load(curImgBean.getUrl()).into(civ);
            }else{
                EventBox.getDefault().send(mImgs, Photor.class);
                finish();
            }

        });
    }

    /**
     * 将bitmap转存到本地，生成file
     *
     * @param imgBean
     * @param bm
     * @return  返回file的url
     */
    private String getFileFromBitmap(ImgBean imgBean, Bitmap bm){
        if(bm == null || imgBean == null){
            return null ;
        }

        File file = new File(context.getExternalCacheDir().getPath() + "/clipcache");
        if(!file.exists()){
            file.mkdir();
        }

        long now = System.currentTimeMillis();
        file = new File(file.getPath() + "/" + now + ".jpeg");

        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getPath() ;
    }
}
