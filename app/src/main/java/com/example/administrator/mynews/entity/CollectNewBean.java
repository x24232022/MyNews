package com.example.administrator.mynews.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2016/12/22.
 */
@DatabaseTable(tableName = "new_database.db")
public class CollectNewBean {
    @DatabaseField(columnName = "_id",id = true)

    private long _id;
    @DatabaseField(columnName = "img")
    private String img;

    @DatabaseField(columnName = "title")
    private String title;
    @DatabaseField(columnName = "date")
    private String date;
    @DatabaseField(columnName = "url")
    private String url;

    @Override
    public String toString() {
        return "CollectNewBean{" +
                "_id=" + _id +
                ", img='" + img + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public CollectNewBean() {
    }

    public CollectNewBean(long _id, String img, String title, String date, String url) {
        this._id = _id;
        this.img = img;
        this.title = title;
        this.date = date;
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
