package com.fitnnestracker.models;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Goal implements Parcelable {
    private int id;
    private String name;
    private String type;
    private String targetValue;
    private String currentValue;
    private Date createdDate;

    public Goal() {
    }

    public Goal(String name, String type, String targetValue, String currentValue, Date createdDate) {
        this.name = name;
        this.type = type;
        this.targetValue = targetValue;
        this.currentValue = currentValue;
        this.createdDate = createdDate;
    }

    protected Goal(Parcel in) {
        id = in.readInt();
        name = in.readString();
        type = in.readString();
        targetValue = in.readString();
        currentValue = in.readString();
        createdDate = new Date(in.readLong());
    }

    public static final Creator<Goal> CREATOR = new Creator<>() {
        @Override
        public Goal createFromParcel(Parcel in) {
            return new Goal(in);
        }

        @Override
        public Goal[] newArray(int size) {
            return new Goal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(targetValue);
        dest.writeString(currentValue);
        dest.writeLong(createdDate.getTime());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }


    public int getProgressPercentage() {
        try {
            float current = Float.parseFloat(currentValue);
            float target = Float.parseFloat(targetValue);
            return (int) ((current / target) * 100);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
