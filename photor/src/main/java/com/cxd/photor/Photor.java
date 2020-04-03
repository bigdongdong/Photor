package com.cxd.photor;

public class Photor {
    private static volatile Photor instance ;
    public static Photor getInstance(){
        if (instance == null) {
            synchronized (Photor.class) {
                if (instance == null) {
                    instance = new Photor();
                }
            }
        }
        return instance;
    }
}
