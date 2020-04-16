package com.cxd.photor.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.Serializable;

/**
 * create by cxd on 2020/4/7
 */
public class ImgBean implements Serializable {
    private String bucketName ; //文件夹名
    private String url ; //文件地址              xxxx/xxx/abc.jpeg

    private String name ;  //文件名【不带后缀】    abc
    private String type ;  //后缀名              jpeg
    private int width ; //单位：px(像素)
    private int height ; //单位：px(像素)
    private long size ; //单位：b(字节)

    @NonNull
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();buffer.append("\n");
        buffer.append("bucketName = ");buffer.append(bucketName);buffer.append("\n");
        buffer.append("url = ");buffer.append(url);buffer.append("\n");
        buffer.append("name = ");buffer.append(getName());buffer.append("\n");
        buffer.append("type = ");buffer.append(getType());buffer.append("\n");
        buffer.append("width = ");buffer.append(getWidth());buffer.append("\n");
        buffer.append("height = ");buffer.append(getHeight());buffer.append("\n");
        buffer.append("size = ");buffer.append(getSize());buffer.append("\n");
        return buffer.toString();
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
        try{
            return url.substring(url.lastIndexOf("/")+1,url.lastIndexOf("."));
        }catch (Exception e){}

        return null;
    }

    public String getType(){
        if(url == null){
            return null;
        }

        try{
            return  url.substring(url.lastIndexOf(".")+1);
        }catch (Exception e){}
        return null ;
    }

    public int getWidth(){
        if(width == 0){
            generateBitmap();
        }

        return width;
    }

    public int getHeight(){
        if(height == 0){
            generateBitmap();
        }

        return height ;
    }

    public long getSize(){
        if(size == 0){
            generateBitmap();
        }

        return size;
    }

    private void generateBitmap(){
        if(url != null){
            File file = new File(url);
            this.size = file.length();
            Bitmap bm = BitmapFactory.decodeFile(url);
            if(bm == null){
                this.width = 0;
                this.height = 0;
            }else{
                this.width = bm.getWidth();
                this.height = bm.getHeight();
            }
            bm.recycle();
            bm = null;
            System.gc();
        }
    }
}
