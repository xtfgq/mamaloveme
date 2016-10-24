package com.netlab.loveofmum.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/15.
 */
public class PictureItem implements Serializable {
    private boolean isAdd;
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean isAdd) {
        this.isAdd = isAdd;
    }

}

