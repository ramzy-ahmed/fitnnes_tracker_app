package com.fitnnestracker.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Workout implements Serializable {
    private int id;
    private String title;
    private String discretion;
    private String duration;
    private String calories;
    private String picUrl;
    private Date date;

    private ArrayList<Lesson> lesson;

    public Workout(String title, String discretion, String duration, String calories, String picUrl, ArrayList<Lesson> lesson) {
        this.title = title;
        this.discretion = discretion;
        this.duration = duration;
        this.calories = calories;
        this.picUrl = picUrl;
        this.lesson = lesson;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscretion() {
        return discretion;
    }

    public void setDiscretion(String discretion) {
        this.discretion = discretion;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Lesson> getLesson() {
        return lesson;
    }

    public void setLesson(ArrayList<Lesson> lesson) {
        this.lesson = lesson;
    }
}