package com.cxd.photor.model;

import com.cxd.photor.PCropOption;

import java.io.Serializable;

/**
 * create by cxd on 2020/4/7
 */
public class ImgBean implements Serializable {
    private String bucketName ; //文件夹名
    private String url ; //                     xxxx/xxx/abc.jpeg

    private String name ;  //文件名【不带后缀】    abc
    private String type ;  //后缀名              jpeg

    public Size size ; //图标size

    class Size implements Serializable{
        int width ;
        int height ;
        int kb ;

        public Size(int width, int height, int kb) {
            this.width = width;
            this.height = height;
            this.kb = kb;
        }
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        if(url == null){
            return null;
        }
        return url.substring(url.lastIndexOf("/")+1,url.lastIndexOf("."));
    }

    public String getType(){
        if(url == null){
            return null;
        }
        return  url.substring(url.lastIndexOf(".")+1);
    }

//    public Size getSize(){
//        if(url == null){
//            return null;
//        }
//
//    }
}
