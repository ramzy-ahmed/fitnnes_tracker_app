package com.fitnnestracker.models;

import java.io.Serializable;

public class Lesson implements Serializable {
    private int id;
    private String title;
    private String duration;
    private String picUrl;
    private String link;

    public Lesson(String title, String duration, String picUrl, String link) {
        this.title = title;
        this.duration = duration;
        this.picUrl = picUrl;
        this.link = link;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
