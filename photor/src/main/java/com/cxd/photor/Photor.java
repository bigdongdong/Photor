package com.cxd.photor;


import android.content.Context;
import android.provider.ContactsContract;

import com.cxd.eventbox.EventBox;
import com.cxd.eventbox.EventBoxSubscribe;
import com.cxd.photor.activity.BucketActivity;
import com.cxd.photor.activity.ClipActivity;
import com.cxd.photor.activity.PhotoActivity;
import com.cxd.photor.model.ImgBean;

import java.io.File;
import java.util.ArrayList;

/**
 * create by cxd on 2020/4/7
 */
public class Photor implements IPhotor{
    private static volatile Photor instance ;

    private Context mContext ;
    private OnPhotorListener mPhotoListener;
    private EMSource mSource ;
    private int mCropWidth = 0, mCropHeight = 0;

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

    /**
     *
     * @param context 跳转activity所需要的context， 本类不会长期持有context
     * @return
     */
    public Photor context(Context context){
        mContext = context;
        return this ;
    }

    /**
     * 裁剪参数，最终图片的宽高尺寸
     * @param width
     * @param height
     * @return
     */
    public Photor crop(int width , int height ){
        mCropWidth = width ;
        mCropHeight = height ;
        return this ;
    }

    /**
     * 获取图片结果回调监听
     * @param onPhotorListener
     * @return
     */
    public Photor onPhotorListener(OnPhotorListener onPhotorListener){
        mPhotoListener = onPhotorListener ;
        return this;
    }


    /**
     * 从相机拍摄捕捉
     */
    @Override
    public void requestImgFromCamera() {
        mSource = EMSource.CAMERA ;
    }

    /**
     * 从所有图片集中获取
     * @param limit
     */
    @Override
    public void requestImgs(int limit) {
        mSource = EMSource.ALBUM ;
        PhotoActivity.jump(mContext,limit);
    }

    /**
     * 从分类好的相册文件夹获取
     * @param limit
     */
    @Override
    public void requestImgsFromAlbum(int limit) {
        mSource = EMSource.ALBUM ;
        BucketActivity.jump(mContext,limit);
    }

    /**
     * 清除裁剪缓存
     * @param context
     */
    public synchronized void clearCropCache(Context context){
        File file = new File(context.getExternalCacheDir().getPath() + "/clipcache");
        if(!file.exists() || !file.isDirectory()){
            return;
        }

        try{
            File[] files = file.listFiles();
            for(File f : files){
                f.delete();
            }
            file.delete();
        }catch (Exception e){}

    }

    @EventBoxSubscribe
    public void onEvent(ArrayList<ImgBean> imgs){
        if(mPhotoListener == null){
            return;
        }

        /*判断裁剪*/
        if(mCropWidth > 0 && mCropHeight > 0){
            ClipActivity.jump(mContext,imgs,mCropWidth,mCropHeight);
            mCropWidth = mCropHeight = 0 ;
        }else{
            mPhotoListener.onSuccess(imgs,mSource);

            reset();

        }

    }

    /*reset*/
    public synchronized void reset(){
        mSource = null ;
        mCropWidth = mCropHeight = 0 ;
        mContext = null ; //释放context，避免造成内存泄漏
        mPhotoListener = null ;
    }

}
