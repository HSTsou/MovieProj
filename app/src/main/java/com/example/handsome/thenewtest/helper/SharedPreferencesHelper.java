package com.example.handsome.thenewtest.helper;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

	private SharedPreferences sharedPreferences;
	private static final String PREF = "MM_PREF";
	private static final String IS_FIRST_TIME = "isFirstTime";
	private static final String AREA = "area";
	private static final String AREA_NAME = "areaName";
	private static final String AREA_NAME_TAG = "areaNameTag";
	private static final String MVLIST_ORDERBY = "mvList_orderBy";
	
	private static final String FB_NAME = "fb_name";
	
	private static final String IS_INSTRUCTION = "IS_INSTRUCTION(%s)";
	private static final String RECOMMEND_RESULT = "RECOMMEND_RESULT";
	private static final String COMPLETE_UPDATE_DATE = "COMPLETE_UPDATE_DATE";
	
	public SharedPreferencesHelper(Context context) {
		sharedPreferences = context.getSharedPreferences(PREF, 0);
	}
	
	public Date getCompleteUpdateDate() {
		Long l = sharedPreferences.getLong(COMPLETE_UPDATE_DATE, 0);
		if(l != 0)
			return new Date(l);
		else
			return null;
	}
	
	public void setCompleteUpdateDate(Date date) {
		sharedPreferences.edit().putLong(COMPLETE_UPDATE_DATE, date.getTime()).commit();
	}

	public String getRecommendResult() {
		return sharedPreferences.getString(RECOMMEND_RESULT, null);
	}
	
	public void setRecommendResult(String result) {
		sharedPreferences.edit().putString(RECOMMEND_RESULT, result).commit();
	}

	public boolean isFirstTime() {
		return sharedPreferences.getBoolean(IS_FIRST_TIME, true);
	}

	public void setFirstTime(boolean flag) {
		sharedPreferences.edit().putBoolean(IS_FIRST_TIME, flag).commit();
	}

	public String getArea() {
		return sharedPreferences.getString(AREA, null);
	}
	
	public void setArea(String area) {
		sharedPreferences.edit().putString(AREA, area).commit();
	}
	
	public String getAreaName() {
		return sharedPreferences.getString(AREA_NAME, null);
	}
	
	public void setAreaName(String area) {
		sharedPreferences.edit().putString(AREA_NAME, area).commit();
	}
	
	public int getAreaNameTag() {
		return sharedPreferences.getInt(AREA_NAME_TAG, -1);
	}
	
	public void setAreaNameTag(int areaId) {
		sharedPreferences.edit().putInt(AREA_NAME_TAG, areaId).commit();
	}

	public String getMvListOrderBy() {
		return sharedPreferences.getString(MVLIST_ORDERBY, null);
	}
	
	public void setMvListOrderBy(String orderBy) {
		sharedPreferences.edit().putString(MVLIST_ORDERBY, orderBy).commit();
	}

	public String getFbName() {
		return sharedPreferences.getString(FB_NAME , null);
	}

	public void setFbName(String name) {
		sharedPreferences.edit().putString(FB_NAME , name).commit();
	}

	public boolean isInstruction(String act) {
		String s = String.format(IS_INSTRUCTION, act);
		return sharedPreferences.getBoolean(s, true);
	}
	
	public void setInstruction(String act, boolean boo) {
		String s = String.format(IS_INSTRUCTION, act);
		sharedPreferences.edit().putBoolean(s , boo).commit();
	}
	
}
