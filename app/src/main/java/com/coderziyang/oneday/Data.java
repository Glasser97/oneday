package com.coderziyang.oneday;

import android.net.Uri;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Data {
    @Id
    private long dataId;
    private int category;
    private String title;
    private String image;
    private String content;

    public Data(){}

    @Generated(hash = 1684049343)
    public Data(long dataId, int category, String title, String image,
                String content) {
        this.dataId = dataId;
        this.category = category;
        this.title = title;
        this.image = image;
        this.content = content;
    }
    public Data(long dataId, int category, String title, Uri image,
                String content) {
        this.dataId = dataId;
        this.category = category;
        this.title = title;
        this.image = image.toString();
        this.content = content;
    }

    public long getDataId() {
        return this.dataId;
    }

    public void setDataId(long dataId) {
        this.dataId = dataId;
    }

    public int getCategory() {
        return this.category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return this.image;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setImage(Uri img) {
        this.image = img.toString();
    }

}