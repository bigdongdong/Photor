package com.cxd.photor;
/**
 * create by cxd on 2020/4/7
 */
public interface IPhotor {

    /*从相机获取*/
    void requestImgFromCamera();

    /*直接选择图片*/
    void requestImgsFromAlbum(int limit);

    /*从相册选择图片*/
    void requestImgsFromDirectory(int limit);
}
