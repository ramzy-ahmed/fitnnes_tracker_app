package com.fitnnestracker.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

public class Workout implements Parcelable {
    private int id;
    private String type;
    private String duration;
    private String calories;
    private Date date;

    public Workout() {}

    public Workout(String type, String duration, String calories, Date date) {
        this.type = type;
        this.duration = duration;
        this.calories = calories;
        this.date = date;
    }

    protected Workout(Parcel in) {
        id = in.readInt();
        type = in.readString();
        duration = in.readString();
        calories = in.readString();
        date = new Date(in.readLong());
    }

    public static final Creator<Workout> CREATOR = new Creator<>() {
        @Override
        public Workout createFromParcel(Parcel in) {
            return new Workout(in);
        }

        @Override
        public Workout[] newArray(int size) {
            return new Workout[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeString(duration);
        dest.writeString(calories);
        dest.writeLong(date.getTime());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}