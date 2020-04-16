package com.cxd.photor;

import java.io.Serializable;

/**
 * create by cxd on 2020/4/7
 * 裁剪参数
 * */
public class PCropOption implements Serializable{
    int width ;
    int height ;

    public PCropOption(int width, int height ) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
