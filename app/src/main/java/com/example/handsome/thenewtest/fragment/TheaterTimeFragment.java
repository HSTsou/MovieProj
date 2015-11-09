package com.example.handsome.thenewtest.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.handsome.thenewtest.R;
import com.example.handsome.thenewtest.adapter.TheaterTimeAdapter;
import com.example.handsome.thenewtest.entity.MovieTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by handsome on 2015/10/30.
 */
public class TheaterTimeFragment extends Fragment {
    TheaterTimeAdapter adapter;
    ArrayList<MovieTime> mtList ;
    String[]  mvUnit;
    List<String[]> mvUnitList;
    public TheaterTimeFragment() {
    }

    public static TheaterTimeFragment createInstance(String  thTimeStr) {
        Log.i("hs", "MovieTimeFragment createInstance ");
        TheaterTimeFragment f = new TheaterTimeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("thTimeStr", thTimeStr);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_fragment, container, false);
        setTimeStrData();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new TheaterTimeAdapter( mvUnitList);
        recyclerView.setAdapter(adapter);


        return view;
    }

    void setTimeStrData(){
        Bundle bundle = getArguments();
        mvUnitList = new ArrayList<>();
        if (bundle != null) {
            String timeStr = bundle.getString("thTimeStr");
            mvUnit = timeStr.split("@");//split by the unit of a movie.
            for(String mv : mvUnit){
                String[] mvData =  mv.split("\\|");
                mvUnitList.add(mvData);
            }

        }

    }

}
