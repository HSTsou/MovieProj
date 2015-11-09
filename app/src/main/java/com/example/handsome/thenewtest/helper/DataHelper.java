package com.example.handsome.thenewtest.helper;



/**
 * Created by handsome on 2015/10/14.
 */
public class DataHelper {

    public static String getGateTilteByTag(String tag){
        String gateTitle = null;
        switch(tag) {
            case "G":
                gateTitle = "普遍G";
                break;
            case "P":
                gateTitle = "保護P";
                break;
            case "PG":
                gateTitle = "輔導PG";
                break;
            case "R":
                gateTitle = "限制R";
                break;
            default:

                break;
        }
        return gateTitle;
    }
}
