package com.cxd.photor.model;

import java.io.Serializable;

public class ImgBean implements Serializable {
    private String bucketName ;
    private String url ;
    private String name ;
    private String type ;

    public String getBucketName() {
        return bucketName;
    }

    public ImgBean(String bucketName, String url) {
        this.bucketName = bucketName;
        this.url = url;
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
}
