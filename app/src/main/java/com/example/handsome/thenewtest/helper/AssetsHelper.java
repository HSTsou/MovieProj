package com.example.handsome.thenewtest.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AssetsHelper {

	private Context context;
	private DatabaseHelper dbHelper;

	public AssetsHelper(Context context, DatabaseHelper dbHelper) {
		this.context = context;
		this.dbHelper = dbHelper;
	}

	public void loadTheaterData(SQLiteDatabase db) throws IOException {
		InputStream in = context.getAssets().open("theaterAssetsNew.txt");
		BufferedReader bfr = new BufferedReader(new InputStreamReader(in,
				"UTF-8"));

		String temp;
		String area = null;
		String[] theater = null;

		while ((temp = bfr.readLine()) != null) {
			if (temp.indexOf("area") != -1) {
				String[] th = temp.split(" ");
				area = th[1];
			} else {
				String[] th = temp.split("\\|");
				
				theater = th;
				dbHelper.insertTheater(db, theater, area);
			}
		}
	}


}
