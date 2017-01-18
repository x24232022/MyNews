package com.example.administrator.mynews.entity;

/**
 * Created by Administrator on 2016/12/23.
 */

public class MyMessage {
    private int type;
    private String text;

    public MyMessage(int type, String text) {
        this.type = type;
        this.text = text;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
