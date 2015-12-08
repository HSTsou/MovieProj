package com.example.handsome.thenewtest.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

	private SharedPreferences sharedPreferences;
	private static final String PREF = "MM_PREF";
	private static final String IS_FIRST_TIME = "isFirstTime";

	public SharedPreferencesHelper(Context context) {
		sharedPreferences = context.getSharedPreferences(PREF, 0);
	}
	

	public boolean isFirstTime() {
		return sharedPreferences.getBoolean(IS_FIRST_TIME, true);
	}

	public void setFirstTime(boolean flag) {
		sharedPreferences.edit().putBoolean(IS_FIRST_TIME, flag).commit();
	}


	

	
}
