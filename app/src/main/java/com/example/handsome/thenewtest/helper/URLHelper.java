package com.example.handsome.thenewtest.helper;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.handsome.thenewtest.exception.NoNetworkConnectedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class URLHelper {

    public static final String ERR_NO_CONNECTION = "請開啟網路連線！";

	/*
     * public static String getSource(String url) throws IOException { URL html
	 * = new URL(url); InputStreamReader is = new
	 * InputStreamReader(html.openStream(), "UTF-8"); BufferedReader in = new
	 * BufferedReader(is);
	 *
	 * StringBuffer temp = new StringBuffer(); while (in.ready()) {
	 * temp.append(in.readLine()); }
	 *
	 * return temp.toString(); }
	 */

    public static String getRequest(String url)
            throws NoNetworkConnectedException {
        String response = null;
        Log.i("hs", "getRequest");
        try {
            URL u = new URL(url);
            URLConnection uc = u.openConnection();
            HttpURLConnection connection = (HttpURLConnection) uc;

            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");

            InputStream in = connection.getInputStream();
            try {
                byte[] resp = getBytes(in);
                response = new String(resp, "utf-8");
            } finally {
                in.close();
            }
        } catch (Exception e) {
            Log.i("hs", e.toString());
        }

        if (response == null)
            throw new NoNetworkConnectedException();

        return response;
    }

    public static void getImage(String url, OutputStream output) {
        try {
            URL u = new URL(url);
            URLConnection uc = u.openConnection();
            HttpURLConnection connection = (HttpURLConnection) uc;

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");

            InputStream in = connection.getInputStream();
            try {
                int c;
                while ((c = in.read()) != -1) {
                    output.write(c);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                in.close();
            }

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static byte[] getBytes(InputStream is) throws IOException {

        int len;
        int size = 1024;
        byte[] buf;

        if (is instanceof ByteArrayInputStream) {
            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size)) != -1)
                bos.write(buf, 0, len);
            buf = bos.toByteArray();
            bos.close();
        }
        return buf;
    }

    /*public static byte[] getBytes(String url) {
        try {
            DefaultHttpClient mHttpClient = new DefaultHttpClient();
            HttpGet mHttpGet = new HttpGet(url);
            HttpResponse mHttpResponse = mHttpClient.execute(mHttpGet);

            if (mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = mHttpResponse.getEntity();
                if (entity != null)
                    return EntityUtils.toByteArray(entity);
            }
        } catch (Exception e) {
        }
        return null;
    }*/

    public static Drawable getDrawable(String url) throws NoNetworkConnectedException {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "poster");
            return d;
        } catch (Exception e) {
            throw new NoNetworkConnectedException();
        }
    }

}
