package com.example.handsome.thenewtest.entity;

/**
 * Created by handsome on 2015/10/24.
 */
public class AreaObject {
    String AreaName;
    String AreaId;

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getAreaId() {
        return AreaId;
    }

    public void setAreaId(String areaId) {
        AreaId = areaId;
    }


    public AreaObject(String areaName, String areaId) {
        AreaName = areaName;
        AreaId = areaId;
    }

    public AreaObject() {
    }
}
