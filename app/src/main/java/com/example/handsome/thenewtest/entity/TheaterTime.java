package com.example.handsome.thenewtest.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by handsome on 2015/10/30.
 */
public class TheaterTime implements Parcelable {
    String thDate;
    String allThTimeStr;

    public TheaterTime(){

    }
    protected TheaterTime(Parcel in) {
        thDate = in.readString();
        allThTimeStr = in.readString();
    }

    public static final Creator<TheaterTime> CREATOR = new Creator<TheaterTime>() {
        @Override
        public TheaterTime createFromParcel(Parcel in) {
            return new TheaterTime(in);
        }

        @Override
        public TheaterTime[] newArray(int size) {
            return new TheaterTime[size];
        }
    };

    public String getAllThTimeStr() {
        return allThTimeStr;
    }

    public void setAllThTimeStr(String allThTimeStr) {
        this.allThTimeStr = allThTimeStr;
    }

    public String getThDate() {
        return thDate;
    }

    public void setThDate(String thDate) {
        this.thDate = thDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(thDate);
        dest.writeString(allThTimeStr);
    }
}
