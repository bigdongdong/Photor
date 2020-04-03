package com.cxd.photor.model;

import android.support.annotation.NonNull;

import com.cxd.photor.model.ImgBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BucketBean implements Serializable {
    private String name ;
    private String coverUrl ;
    private List<ImgBean> list ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public List<ImgBean> getList() {
        if(list == null){
            return new ArrayList<>();
        }
        return list;
    }

    public void setList(List<ImgBean> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public String toString() {
        return "name = "+name+"   coverUrl = "+coverUrl+"   list.size()="+list.size();
    }
}
