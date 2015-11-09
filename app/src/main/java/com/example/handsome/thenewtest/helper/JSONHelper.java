package com.example.handsome.thenewtest.helper;

import com.example.handsome.thenewtest.exception.StatusNotOkException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class JSONHelper {
	
	public static JSONObject checkStatus(String jsonObject) throws StatusNotOkException {
		try {
			JSONObject obj = new JSONObject(jsonObject);
			if (obj.getString("status").equalsIgnoreCase("OK")) {

				return obj;
			}
		} catch (Exception e) {

		}
		throw new StatusNotOkException();
	}

	public static List<String> getStringListFromJsonArray(JSONArray jArray) throws JSONException {
		List<String> returnList = new ArrayList<String>();
		for (int i = 0; i < jArray.length(); i++) {
			String val = jArray.getString(i);
			returnList.add(val);
		}
		return returnList;
	}
}
