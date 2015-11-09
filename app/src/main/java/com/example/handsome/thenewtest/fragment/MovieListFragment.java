package com.example.handsome.thenewtest.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.handsome.thenewtest.helper.ItemClickSupport;
import com.example.handsome.thenewtest.MainActivity;
import com.example.handsome.thenewtest.entity.Movie;
import com.example.handsome.thenewtest.MovieInfoActivity;
import com.example.handsome.thenewtest.R;
import com.example.handsome.thenewtest.adapter.MovieListAdapter;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by handsome on 2015/10/13.
 */
public class MovieListFragment extends Fragment {

  //  public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";

    List<Movie> mvList_fragment;
    public static MovieListFragment createInstance(String state) {

        Log.i("hs", "createInstance!" + state);
        MovieListFragment f = new MovieListFragment();
        Bundle bundle = new Bundle();
      //  bundle.putInt(ITEMS_COUNT_KEY, itemsCount);//
        bundle.putString("state", state);
        f.setArguments(bundle);
        return f;
    }

   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("hs", "onCreate");
        super.onCreate(savedInstanceState);
        //get data from Argument
        //position = getArguments().getInt("position");
    }*/


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("hs", "onCreateView");
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recyclerview_fragment, container, false);

        setupRecyclerView(recyclerView);


        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Log.i("hs", "position = " + position);
                // Log.i("hs", "item = " + allData.get(position));
                Intent i = new Intent();
                i.setClass(getActivity(), MovieInfoActivity.class);
                i.putExtra("movie",  mvList_fragment.get(position));
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

        MovieListAdapter recyclerAdapter = new MovieListAdapter(getContext(), createMovie());//put Data in here

        recyclerView.setAdapter(recyclerAdapter);
    }

    List<Movie> createMovie(){

        List<Movie> choosenMvList = new ArrayList<>();
        Bundle bundle = getArguments();
        if(bundle!=null) {
            String state = bundle.getString("state");

            for (int i = 0; i < MainActivity.MV_LIST.size(); i++) {
                Movie mv  = MainActivity.MV_LIST.get(i);
               // Log.i("hs", "bundle.getString(\"state\") = " + mv.getState());
               if(state.equalsIgnoreCase(mv.getState())){
                   Log.i("hs", mv.getMvName()+" (" + mv.getState());
                  choosenMvList.add(mv);
               }

            }
        }
        mvList_fragment = choosenMvList;
        return choosenMvList;
    }

}
