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
     * @param source 图片源头（相机或相册）EMSource.CAMERA or EMSource.ALBUM
     */
    void onSuccess(List<ImgBean> IMGs , EMSource source);

//    void onError();

    /**
     * 没有选择就finish了
     */
//    void onCancel();
}
