package com.example.handsome.thenewtest.model;

/**
 * Created by hs on 2017/3/30.
 */

public class TheaterInfo {
    //@SerializedName("question_content")
    private String thId;
    private String thName;
    private String thAddress;
    private String phone;
    private String lat;
    private String lng;

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

    public String getThAddress() {
        return thAddress;
    }

    public void setThAddress(String thAddress) {
        this.thAddress = thAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
