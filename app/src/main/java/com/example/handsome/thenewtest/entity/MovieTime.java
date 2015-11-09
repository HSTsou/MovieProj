package com.example.handsome.thenewtest.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by handsome on 2015/10/22.
 */
public class MovieTime implements Parcelable{
    String area;
    String thId;
    String thName;
    String hall;
    String timeStr;

    protected MovieTime(Parcel in) {
        area = in.readString();
        thId = in.readString();
        thName = in.readString();
        hall = in.readString();
        timeStr = in.readString();
    }


    public MovieTime() {

    }

    public static final Creator<MovieTime> CREATOR = new Creator<MovieTime>() {
        @Override
        public MovieTime createFromParcel(Parcel in) {
            return new MovieTime(in);
        }

        @Override
        public MovieTime[] newArray(int size) {
            return new MovieTime[size];
        }
    };

    public String getThId() {
        return thId;
    }

    public void setThId(String thId) {
        this.thId = thId;
    }

    public String getThName() {
        return thName;
    }

    public void setThName(String thName) {
        this.thName = thName;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public String getArea() {
        return area;

    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(area);
        parcel.writeString(thId);
        parcel.writeString(thName);
        parcel.writeString(hall);
        parcel.writeString(timeStr);
    }
}
