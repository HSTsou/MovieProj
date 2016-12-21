package com.example.handsome.thenewtest.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.handsome.thenewtest.MovieInfoActivity;
import com.example.handsome.thenewtest.R;
import com.example.handsome.thenewtest.adapter.MovieListAdapter;
import com.example.handsome.thenewtest.entity.Movie;
import com.example.handsome.thenewtest.helper.DatabaseHelper;
import com.example.handsome.thenewtest.helper.ItemClickSupport;
import com.example.handsome.thenewtest.helper.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * Created by handsome on 2015/10/13.
 */
public class MovieListFragment extends Fragment {

  //  public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
    Context c;
    List<Movie> mvList_fragment;
    private SharedPreferencesHelper prefHelper;
    public static MovieListFragment createInstance(String state) {

        Log.i("hs", "createInstance!" + state);
        MovieListFragment f = new MovieListFragment();
        Bundle bundle = new Bundle();
      //  bundle.putInt(ITEMS_COUNT_KEY, itemsCount);//
        bundle.putString("state", state);
        f.setArguments(bundle);
        return f;
    }

   @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("hs", "onCreate");
        super.onCreate(savedInstanceState);
       prefHelper = new SharedPreferencesHelper(getActivity());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("hs", "onCreateView");
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recyclerview_fragment, container, false);

        setupRecyclerView(recyclerView);

        //longterm event to add the collection of favorites.
        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                Toast.makeText(getActivity(), "add " + mvList_fragment.get(position).getMvName() + " to fav. : )", Toast.LENGTH_SHORT).show();
                prefHelper.addCollection(mvList_fragment.get(position).getGaeId());
                return false;
            }
        });

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Log.i("hs", "position = " + position);
                // Log.i("hs", "item = " + allData.get(position));
                Intent i = new Intent();
                i.setClass(getActivity(), MovieInfoActivity.class);
                i.putExtra("mvId",  mvList_fragment.get(position).getGaeId());
                startActivity(i);
            }
        });

        return recyclerView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("hs", "onViewCreated");

    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        MovieListAdapter recyclerAdapter = new MovieListAdapter(getContext(), setMvDataListFromDB());//put Data in here

        recyclerView.setAdapter(recyclerAdapter);
    }



    List<Movie> setMvDataListFromDB(){
        List<Movie> mvList = new  ArrayList<Movie>();
        DatabaseHelper helper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();

        Bundle bundle = getArguments();

        String state = bundle.getString("state");
        Cursor c = null;
        try {
            c = helper.getMovieListData(db, null, state);

            while (c.moveToNext()){
                Movie mv = new Movie();
                //mv.setState(c.getString(0));
                mv.setAtMoviesMvId(c.getString(1));
                mv.setPlayingDate(c.getString(2));
                mv.setMvName(c.getString(3));
                mv.setEnName(c.getString(4));
                mv.setImgLink(c.getString(5));
                mv.setIMDbRating(c.getDouble(6));//get string from json but no translate to double.And then directly store into sqlite with a real data type.???
                mv.setTomatoesRating(c.getDouble(7));//like above.
                mv.setGaeId(c.getString(8));

                mvList.add(mv);
            }
        } finally {
            c.close();
            db.close();
        }

        if(state =="notYet"){
            Collections.reverse(mvList);//if order list by "playingDate", the notYet movie will reverse. So reverse it again.
        }

        mvList_fragment = mvList;

        return mvList;
    }

}
