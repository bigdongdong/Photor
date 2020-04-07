package com.cxd.photor;

import com.cxd.photor.model.ImgBean;

import java.util.List;

/**
 * create by cxd on 2020/4/7
 * 图片选择回调
 */
public interface OnPhotorListener {

    /**
     * @param IMGs 图片集
     * @param SOURCE 图片源头（相机或相册）: Constant.PHOTO_SOURCE_CAMERA or Constant.PHOTO_SOURCE_ALBUM
     */
    void onSuccess(List<ImgBean> IMGs ,String SOURCE);

//    void onError();

    /**
     * 没有选择就finish了
     */
//    void onCancel();
}
