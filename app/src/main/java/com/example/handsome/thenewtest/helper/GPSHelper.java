package com.example.handsome.thenewtest.helper;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import com.example.handsome.thenewtest.exception.NoNetworkConnectedException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class GPSHelper {

	public static final String URL_GET_ADDRESS = "http://maps.google.com/maps/api/geocode/json?latlng=%s,%s&language=zh-TW&sensor=false&region=tw";

	public static String getAddress(Location l)
			throws NoNetworkConnectedException, JSONException {
		if (l == null)
			return null;

		String url = String.format(URL_GET_ADDRESS, l.getLatitude(), l.getLongitude());

		String y = URLHelper.getRequest(url);
		JSONObject obj = new JSONObject(y);
		JSONArray arr = obj.getJSONArray("results");
		String add = arr.getJSONObject(0).getString("formatted_address");
		if (add != null)
			return add;
		else
			return null;

	}

	/*public static GeoPoint getGeoPoint(String address)
			throws ClientProtocolException, IOException, JSONException {
		String url = "http://maps.google.com/maps/api/geocode/json";
		HttpGet httpGet = new HttpGet(url + "?sensor=false&region=tw&address="
				+ address);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		response = client.execute(httpGet);
		HttpEntity entity = response.getEntity();
		InputStream stream = entity.getContent();
		int b;
		while ((b = stream.read()) != -1) {
			stringBuilder.append((char) b);
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject = new JSONObject(stringBuilder.toString());

		return getGeoPoint(jsonObject);
	}*/

	/*public static GeoPoint getGeoPoint(JSONObject jsonObject)
			throws JSONException {
		JSONArray array = (JSONArray) jsonObject.get("results");
		JSONObject first = array.getJSONObject(0);
		JSONObject geometry = first.getJSONObject("geometry");
		JSONObject location = geometry.getJSONObject("location");

		double lat = location.getDouble("lat");
		double lng = location.getDouble("lng");

		return new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
	}
*/
	public static Location getLocation(Context context) {
		Location l = null;
		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		Criteria mCriteria01 = new Criteria();
		mCriteria01.setAccuracy(Criteria.ACCURACY_FINE);
		mCriteria01.setAltitudeRequired(false);
		mCriteria01.setBearingRequired(false);
		mCriteria01.setCostAllowed(true);
		mCriteria01.setPowerRequirement(Criteria.POWER_LOW);
		String strLocationPrivider = lm.getBestProvider(mCriteria01, true);
	//	l = lm.getLastKnownLocation(strLocationPrivider);

		return l;
	}

	/*
	 * public static double getDistance(GeoPoint a, Location b) { double bLat =
	 * b.getLatitude(); double bLng = b.getLongitude();
	 * 
	 * double aLat = a.getLatitudeE6()/1E6; double aLng =
	 * a.getLongitudeE6()/1E6;
	 * 
	 * double R = 6371; double distance = Math.acos(Math.sin(aLat) *
	 * Math.sin(bLat) + Math.cos(aLat) Math.cos(bLat) * Math.cos(bLng - aLng))*
	 * R; return distance; }
	 */
	public static double formatDistance(double d) {
		double formatDistance;
		NumberFormat df = new DecimalFormat("#.#");
		formatDistance = Double.parseDouble(df.format(d));
		return formatDistance;
	}

	/*public static double getDistance(GeoPoint gp1, GeoPoint gp2) {

		double Lat1r = ConvertDegreeToRadians(gp1.getLatitude() / 1E6);
		double Lat2r = ConvertDegreeToRadians(gp2.getLatitude() / 1E6);
		double Long1r = ConvertDegreeToRadians(gp1.getLongitude() / 1E6);
		double Long2r = ConvertDegreeToRadians(gp2.getLongitude() / 1E6);

		double R = 6371;
		double d = Math
				.acos(Math.sin(Lat1r) * Math.sin(Lat2r) + Math.cos(Lat1r)
						* Math.cos(Lat2r) * Math.cos(Long2r - Long1r))
				* R;
		return d * 1000;
	}

	private static double ConvertDegreeToRadians(double degrees) {
		return (Math.PI / 180) * degrees;
	}

	public static GeoPoint getGeoByLocation(Location location) {
		GeoPoint gp = null;
		try {
			if (location != null) {
				double geoLatitude = location.getLatitude() * 1E6;
				double geoLongitude = location.getLongitude() * 1E6;
				gp = new GeoPoint((int) geoLatitude, (int) geoLongitude);
			}
		} catch (Exception e) {

		}
		return gp;
	}*/

	// hs
	/*public static String GeoPointToString(GeoPoint gp) {
		String strReturn = "";
		try {
			/* 當Location存
			if (gp != null) {
				double geoLatitude = gp.getLatitude() / 1E6;
				double geoLongitude = gp.getLongitude() / 1E6;
				strReturn = String.valueOf(geoLatitude) + ","
						+ String.valueOf(geoLongitude);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strReturn;
	}*/

	/*public static JSONObject getLocationInfoByAddress(String address) {

		HttpGet httpGet = new HttpGet("http://maps.google."
				+ "com/maps/api/geocode/json?address=" + address
				+ "&sensor=false");
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (ClientProtocolException e) {
			Log.i("TEST", e.toString());
		} catch (IOException e) {
			Log.i("TEST", e.toString());
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.i("TEST", e.toString());
		}
		return jsonObject;
	}*/
}
