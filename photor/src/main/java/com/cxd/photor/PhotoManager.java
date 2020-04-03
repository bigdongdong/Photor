package com.cxd.photor;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import com.cxd.eventbox.EventBox;
import com.cxd.photor.activity.BucketActivity;
import com.cxd.photor.activity.PhotoActivity;
import com.cxd.photor.model.BucketBean;
import com.cxd.photor.model.ImgBean;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PhotoManager {
    private static volatile PhotoManager instance ;

    private final static String[] CURSOR_PROJECTION = new String[]{
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, //文件夹名
            MediaStore.Images.Media.DATA}; //路径


    private List<ImgBean> mAllImgList ;
    private List<ImgBean> mSelectedList ; //当前选中数据集
    private int mTotalCount = 1;

    public static PhotoManager getInstance(){
        if (instance == null) {
            synchronized (PhotoManager.class) {
                if (instance == null) {
                    instance = new PhotoManager();
                }
            }
        }
        return instance;
    }

    public void init(int totalCount){
        mTotalCount = totalCount ;
        mAllImgList = null ;
        mSelectedList = null ;
    }

    /**
     * @param context
     * @return
     */
    private synchronized void initAllImgs(Context context){
        if(mAllImgList == null){
            Cursor cursor = context.getApplicationContext().getContentResolver()
                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, CURSOR_PROJECTION,
                            null, null, MediaStore.Images.Media.DATE_ADDED);

            mAllImgList = new ArrayList<>();
            if(cursor.moveToLast()){
                do{
                    mAllImgList.add(new ImgBean(cursor.getString(cursor.getColumnIndex(CURSOR_PROJECTION[0])),
                            cursor.getString(cursor.getColumnIndex(CURSOR_PROJECTION[1]))));
                }while (cursor.moveToPrevious());
            }

            cursor.close();
        }
    }

    /**
     * 更新数据集
     * @param context
     */
    public synchronized void update(Context context){
        mAllImgList = null ;
        initAllImgs(context);
    }

    /**
     * 游标转List<BucketBean>
     *
     * @return
     */
    public synchronized List<BucketBean> getBuckets(Context context) {
        if (mAllImgList == null) {
            initAllImgs(context);
        }

        HashMap<String, BucketBean> map = new HashMap<>();

        Iterator<ImgBean> it = mAllImgList.iterator();
        while (it.hasNext()) {
            ImgBean temp = it.next();
            String bucketName = temp.getBucketName();
            String url = temp.getUrl();
            if (map.get(bucketName) == null) {
                BucketBean bean = new BucketBean();
                bean.setCoverUrl(url);
                bean.setName(bucketName);
                bean.setList(new ArrayList<ImgBean>());
                map.put(bucketName, bean);
            }
            map.get(bucketName).getList().add(new ImgBean(bucketName,url));
        }

        /*遍历hashMap 返回list*/
        List<BucketBean> list = new ArrayList<>();
        Iterator<Map.Entry<String, BucketBean>> it1 = map.entrySet().iterator();
        while (it1.hasNext()) {
            list.add(it1.next().getValue());
        }

        /*list排序，数量多的在前*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.sort(new Comparator<BucketBean>() {
                @Override
                public int compare(BucketBean o1, BucketBean o2) {
                    return o1.getList().size() > o2.getList().size() ? -1 : +1;
                }
            });
        }
        return list;
    }

    /**
     * 游标转List<ImgBean>
     *
     * @return
     */
    public synchronized List<ImgBean> getAllImgs(Context context) {
        if(mAllImgList == null){
            initAllImgs(context);
        }

        return mAllImgList;
    }

    public synchronized List<ImgBean> getSelectedList(){
        if(mSelectedList == null){
            mSelectedList = new ArrayList<>();
        }

        return mSelectedList ;
    }


    public synchronized void addImg(ImgBean bean){
        if(bean == null){
            return;
        }

        if(mSelectedList == null){
            mSelectedList = new ArrayList<>();
        }
        if(mTotalCount == mSelectedList.size()){
            /*提示超出限制*/
            Log.i("aaa", "addImg: 超出限制了");
            return;
        }
        mSelectedList.add(bean);

        sendEvent();
    }

    public synchronized void removeImg(ImgBean bean){
        if(bean == null){
            return;
        }

        if(mSelectedList == null){
            mSelectedList = new ArrayList<>();
        }

        for (int i = 0; i < mSelectedList.size(); i++) {
            ImgBean temp = mSelectedList.get(i);
            if (temp.getUrl().equals(bean.getUrl())) {
                mSelectedList.remove(temp);
                break;
            }
        }

        sendEvent();
    }

    /**
     * 通过eventbox发送通知
     */
    private void sendEvent(){
        EventBox.getDefault().send(mSelectedList, BucketActivity.class, PhotoActivity.class);
    }

    /**
     * 一共可以选择的图片数量
     * @return
     */
    public int getTotalCount(){
        return mTotalCount ;
    }
}
