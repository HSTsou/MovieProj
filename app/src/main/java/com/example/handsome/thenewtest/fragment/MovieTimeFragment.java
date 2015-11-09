package com.example.handsome.thenewtest.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.handsome.thenewtest.MovieListActivity;
import com.example.handsome.thenewtest.R;
import com.example.handsome.thenewtest.adapter.MovieTimeAdapter;
import com.example.handsome.thenewtest.entity.MovieTime;
import com.example.handsome.thenewtest.helper.ItemClickSupport;

import java.util.ArrayList;

/**
 * Created by handsome on 2015/10/22.
 */
public class MovieTimeFragment  extends Fragment {

    MovieTimeAdapter adapter;
    ArrayList<MovieTime> mtList ;

    public MovieTimeFragment() {
    }

    public static MovieTimeFragment createInstance(ArrayList<MovieTime> mtList) {
        Log.i("hs", "MovieTimeFragment createInstance ");
        MovieTimeFragment f = new MovieTimeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("mtList",  mtList);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_fragment, container, false);
        setMovieListData();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new MovieTimeAdapter(mtList);
        recyclerView.setAdapter(adapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Log.i("hs", "position = " + position);
                // Log.i("hs", "item = " + allData.get(position));
                Intent i = new Intent();
                i.setClass(getActivity(), MovieListActivity.class);
                i.putExtra("movie", mtList.get(position));
                String thId =  mtList.get(position).getThId();
                i.putExtra("thId", thId);
                startActivity(i);

            }
        });

        return view;
    }

    void setMovieListData(){
        Bundle bundle = getArguments();

        if (bundle != null) {
            mtList = bundle.getParcelableArrayList("mtList");

        }

    }


}
