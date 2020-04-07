package com.cxd.photor;

import java.io.Serializable;

/**
 * create by cxd on 2020/4/7
 */
public class PCropOption {
    public Size size ;

    class Size implements Serializable{
        int width ;
        int height ;
        int kb ;

    }

}
