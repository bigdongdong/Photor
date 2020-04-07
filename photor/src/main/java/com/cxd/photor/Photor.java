package com.cxd.photor;


import android.content.Context;
import android.util.Log;

import com.cxd.eventbox.EventBox;
import com.cxd.eventbox.EventBoxSubscribe;
import com.cxd.photor.activity.BucketActivity;
import com.cxd.photor.activity.PhotoActivity;
import com.cxd.photor.model.ImgBean;
import com.cxd.photor.utils.Constant;

import java.util.ArrayList;

/**
 * create by cxd on 2020/4/7
 */
public class Photor implements IPhotor{
    private static volatile Photor instance ;

    private Context mContext ;
    private OnPhotorListener mPhotoListener;
    private String mResouce ;

    public static Photor getInstance(){
        if (instance == null) {
            synchronized (PDataManager.class) {
                if (instance == null) {
                    instance = new Photor();
                    EventBox.getDefault().register(instance);
                }
            }
        }
        return instance;
    }

    public Photor context(Context context){
        mContext = context;
        return this ;
    }

    public Photor onPhotoListener(OnPhotorListener onPhotorListener){
        mPhotoListener = onPhotorListener ;
        return this;
    }

    @Override
    public void requestImgFromCamera() {
        mResouce = Constant.PHOTO_SOURCE_CAMERA ;
    }

    @Override
    public void requestImgs(int limit) {
        mResouce = Constant.PHOTO_SOURCE_ALBUM ;
        PhotoActivity.jump(mContext,limit);
    }

    @Override
    public void requestImgsFromAlbum(int limit) {
        mResouce = Constant.PHOTO_SOURCE_ALBUM ;
        BucketActivity.jump(mContext,limit);
    }

    @EventBoxSubscribe
    public void onEvent(ArrayList<ImgBean> imgs){
        if(mPhotoListener != null){
            mPhotoListener.onSuccess(imgs,mResouce);
        }

    }

}
