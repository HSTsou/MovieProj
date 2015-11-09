package com.example.handsome.thenewtest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.handsome.thenewtest.adapter.MovieListAdapter;
import com.example.handsome.thenewtest.entity.Movie;
import com.example.handsome.thenewtest.util.AppController;
import com.example.handsome.thenewtest.helper.ItemClickSupport;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by handsome on 2015/9/12.
 */
public class MovieListActivityOrin extends Activity {

    public static  RecyclerView mRecyclerView;
    private RecyclerView.Adapter<MovieListAdapter.ViewHolder> mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context;
    private ProgressDialog pDialog;
    final String TAG = "hs";
    private static final String mmUrL = "https://movingmovie99.appspot.com/";
    private static final String allmV = "all-mv";
    RequestQueue mQueue;
    List<Movie> mvList = new ArrayList<Movie>();

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist_orin);
        context = this;
        pDialog = new ProgressDialog(this);
        //pDialog.setMessage("Please wait...");
       // pDialog.setCancelable(false);
        Log.i("hs", "onCreate");

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mQueue = Volley.newRequestQueue(context);

        makeJsonArrayRequest();

        new Thread() {
            @Override
            public void run() {
                try {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter = new MovieListAdapter(context, mvList);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    });
                } catch (Exception e) {
                } finally {
                    //lock = true;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter = new MovieListAdapter(context, mvList);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    });
                }
            }
        }.start();

        ItemClickSupport.addTo(MovieListActivityOrin.mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Log.i("hs", "position = " + position);
                // Log.i("hs", "item = " + allData.get(position));
                Intent i = new Intent();
                i.setClass(context, MovieInfoActivity.class);
                i.putExtra("movie", mvList.get(position));
                startActivity(i);
            }
        });
    }


    private void makeJsonArrayRequest() {
        //Log.i("hs", "makeJsonArrayRequest()");

        showpDialog();
        String url = mmUrL+allmV;
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Movie mv = new Movie();

                                mv.setMvName(obj.getString("mvName"));
                                mv.setEnName(obj.getString("enName"));
                                mv.setPlayingDate(obj.getString("playingDate"));
                                mv.setIMDbRating(Double.parseDouble(obj.getString("IMDbRating")));
                                mv.setImgLink(obj.getString("imgLink"));
                              // List<String> youtubeUrlList = obj.getJSONArray("youtubeUrlList");
                              //  mv.setYoutubeUrlList();
                                mvList.add(mv);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                   //   Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                            }

                        }
                        mAdapter.notifyDataSetChanged();
                        hidepDialog();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.i("hs", "Error = " +  error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }

        });

        AppController.getInstance().addToRequestQueue(req);

    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
