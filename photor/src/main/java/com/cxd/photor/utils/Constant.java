package com.cxd.photor.utils;


/**
 * create by cxd on 2020/4/7
 */
public class Constant {

    /*BucketActivity -> PhotoActivity*/
    public final static String INTENT_PHOTO_LIST = "intent_photo_list"; //对应bucket下的相片集
    public final static String INTENT_BUCKET_NAME = "intent_bucket_name"; //对应的bucket名

    /*相片获取源*/
    public final static String PHOTO_SOURCE_CAMERA = "photo_source_camera"; //从相机捕捉
    public final static String PHOTO_SOURCE_ALBUM = "photo_source_album"; //从相册获取

    /*EventBox Integer 通知*/
    public final static int EVENTBOX_UPDATE = 1001; //更新选中信息
    public final static int EVENTBOX_EXCEED_LIMIT = 1002; //超出最大张数提示
    public final static int EVENTBOX_COMMIT_FINISH = 1003; //提交内容后通知关闭界面
}
