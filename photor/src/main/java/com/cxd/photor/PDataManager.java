package com.cxd.photor;

import android.app.Activity;
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
import com.cxd.photor.utils.Constant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * create by cxd on 2020/4/7
 * photo数据集manager
 */
public class PDataManager {
    private static volatile PDataManager instance ;

    private final static String[] CURSOR_PROJECTION = new String[]{
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, //文件夹名
            MediaStore.Images.Media.DATA}; //路径


    private List<ImgBean> mImgList ; //所有相片数据集
    private List<BucketBean> mBucketList ; //所有文件夹数据集
    private List<ImgBean> mSelectedList ; //当前选中数据集
    private int mLimit = 1; //默认选择张数限制：1

    public static PDataManager getInstance(){
        if (instance == null) {
            synchronized (PDataManager.class) {
                if (instance == null) {
                    instance = new PDataManager();
                }
            }
        }
        return instance;
    }

    /*初始化，赋值limit，重置数据集合以及选中集合*/
    public synchronized void init(int limit){
        mLimit = limit ;
        mImgList = null ;
        mBucketList = null ;
        mSelectedList = null ;
        activities = new ArrayList<>() ;
    }

    /*从cursor获取imgs*/
    private synchronized void initImgs(Context context){
        if(mImgList == null){
            Cursor cursor = context.getApplicationContext().getContentResolver()
                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, CURSOR_PROJECTION,
                            null, null, MediaStore.Images.Media.DATE_ADDED);

            mImgList = new ArrayList<>();
            if(cursor.moveToLast()){
                do{
                    ImgBean ib = new ImgBean();
                    ib.setBucketName(cursor.getString(cursor.getColumnIndex(CURSOR_PROJECTION[0])));
                    ib.setUrl(cursor.getString(cursor.getColumnIndex(CURSOR_PROJECTION[1])));
                    mImgList.add(ib);
                }while (cursor.moveToPrevious());
            }

            cursor.close();
        }
    }

//    /*更新数据集*/
//    public synchronized void updateImgs(Context context){
//        mImgList = null ;
//        initImgs(context);
//    }

    /*获取文件夹数据集*/
    public synchronized List<BucketBean> getBuckets(Context context) {
        if(mBucketList != null && mBucketList.size()>0){
            return mBucketList;
        }

        if (mImgList == null) {
            initImgs(context);
        }

        HashMap<String, BucketBean> map = new HashMap<>();

        Iterator<ImgBean> ImgBeanIt = mImgList.iterator();
        while (ImgBeanIt.hasNext()) {
            ImgBean temp = ImgBeanIt.next();
            String bucketName = temp.getBucketName();
            String url = temp.getUrl();
            if (map.get(bucketName) == null) {
                BucketBean bb = new BucketBean();
                bb.setCoverUrl(url);
                bb.setName(bucketName);
                bb.setList(new ArrayList<>());
                map.put(bucketName, bb);
            }
            ImgBean ib = new ImgBean();
            ib.setBucketName(bucketName);
            ib.setUrl(url);
            map.get(bucketName).getList().add(ib);
        }

        /*遍历hashMap 返回list*/
        mBucketList = new ArrayList<>();
        Iterator<Map.Entry<String, BucketBean>> MapIt = map.entrySet().iterator();
        while (MapIt.hasNext()) {
            mBucketList.add(MapIt.next().getValue());
        }

        /*list排序，数量多的在前，需要Android 7.0*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mBucketList.sort(new Comparator<BucketBean>() {
                @Override
                public int compare(BucketBean o1, BucketBean o2) {
                    return o1.getList().size() > o2.getList().size() ? -1 : +1;
                }
            });
        }
        return mBucketList;
    }

    /*获取所有的图片集list*/
    public synchronized List<ImgBean> getImgs(Context context) {
        if(mImgList == null){
            initImgs(context);
        }

        return mImgList;
    }

    /*获取当前选中集合list*/
    public synchronized List<ImgBean> getSelectedImgs(){
        if(mSelectedList == null){
            mSelectedList = new ArrayList<>();
        }

        return mSelectedList ;
    }

    /*添加选中img*/
    public synchronized void addImg(ImgBean bean){
        if(bean == null){
            return;
        }

        if(mSelectedList == null){
            mSelectedList = new ArrayList<>();
        }
        if(mSelectedList.size() >= mLimit ){
            /*提示超出限制*/
            sendEvent(Constant.EVENTBOX_EXCEED_LIMIT);
            return;
        }
        mSelectedList.add(bean);

        sendEvent(Constant.EVENTBOX_UPDATE);
    }

    /*移除选中img*/
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

        sendEvent(Constant.EVENTBOX_UPDATE);
    }

    /**
     * 通过eventbox发送通知，通知对象：
     * {@link BucketActivity } & {@link PhotoActivity}
     * @param event
     */
    private void sendEvent(int event){
        switch(event){
            case Constant.EVENTBOX_UPDATE:
            case Constant.EVENTBOX_COMMIT_FINISH:
                if(activities.contains(BucketActivity.class)){
                    EventBox.getDefault().send(event,BucketActivity.class);
                }
                if(activities.contains(PhotoActivity.class)){
                    EventBox.getDefault().send(event,PhotoActivity.class);
                }
                break;
            case Constant.EVENTBOX_EXCEED_LIMIT:
                EventBox.getDefault().send(event, PhotoActivity.class);
                break;
        }

    }

    /*选择图片的数量*/
    public int getLimit(){
        return mLimit ;
    }

    /*向Photor提交数据*/
    public synchronized void commit(){
        sendEvent(Constant.EVENTBOX_COMMIT_FINISH);
        EventBox.getDefault().send(mSelectedList, Photor.class);
    }

    /*避免EventBox粘性事件解决方案*/
    private List<Class<? extends Activity>> activities = new ArrayList<>();
    public synchronized void addActivity(Activity activity){
        activities.add(activity.getClass());
    }
    public synchronized void removeActivity(Activity activity){
        Iterator<Class<? extends Activity>> it = activities.iterator();
        while (it.hasNext()){
            if(it.next() == activity.getClass()){
                it.remove();
                break;
            }
        }
    }
}
