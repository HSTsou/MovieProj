package com.example.handsome.thenewtest.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesHelper {

    private SharedPreferences sharedPreferences;
    private static final String PREF = "MM_PREF";
    private static final String IS_FIRST_TIME = "isFirstTime";
    private static final String MY_FAV = "myFavorites";

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF, 0);
    }

    public boolean isFirstTime() {
        return sharedPreferences.getBoolean(IS_FIRST_TIME, true);
    }

    public void setFirstTime(boolean flag) {
        sharedPreferences.edit().putBoolean(IS_FIRST_TIME, flag).commit();
    }

    //加入收藏 20160222
    public void addCollection(String mvId) {
        Gson gson = new Gson();
        List<String> mvIdList = getCollection();

        if (mvIdList != null) {
            mvIdList.add(mvId);
            String gsonStr = gson.toJson(mvIdList);
            sharedPreferences.edit().putString(MY_FAV, gsonStr).commit();
        } else {
            List<String> mvList = new ArrayList<>();
            mvList.add(mvId);
            String gsonStr = gson.toJson(mvList);
            sharedPreferences.edit().putString(MY_FAV, gsonStr).commit();
        }


    }

    public List<String> getCollection() {
        String gsonStr = sharedPreferences.getString(MY_FAV, null);

        if (gsonStr != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {
            }.getType();
            List<String> mvIdList = gson.fromJson(gsonStr, type);

            return mvIdList;
        }
        return null;
    }

    public void removeCollection(String mvId) {

    }


}
