package com.example.handsome.thenewtest;

import android.os.AsyncTask;

/**
 * Created by handsome on 2015/11/7.
 */
public class UpdateActivity extends AsyncTask {
    private OnUpdateListener listener;


    protected void onPreExecute() {
    }


    protected Object doInBackground(Object[] params) {
        return null;
    }


    protected void onProgressUpdate(String... progress) {

    }

    protected void onPostExecutet(String... progress) {
        listener.updateResult("Finished! " + progress + " unit");
    }
}
