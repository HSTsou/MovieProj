package com.example.handsome.thenewtest.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.handsome.thenewtest.MovieInfoActivity;
import com.example.handsome.thenewtest.R;
import com.example.handsome.thenewtest.adapter.TheaterTimeAdapter;
import com.example.handsome.thenewtest.entity.MovieTime;
import com.example.handsome.thenewtest.helper.DatabaseHelper;
import com.example.handsome.thenewtest.helper.ItemClickSupport;

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

        adapter = new TheaterTimeAdapter(mvUnitList);
        recyclerView.setAdapter(adapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Log.i("hs", "position = " + position);

                Intent i = new Intent();
                i.setClass(getActivity(), MovieInfoActivity.class);
                String[] thMvStr = mvUnitList.get(position);
                String mvId =  getGaeIdByMvId(thMvStr[0]);
                i.putExtra("mvId", mvId);
                Log.i("hs", "gaeId = " + mvId);
                startActivity(i);

            }
        });


        return view;
    }


    String getGaeIdByMvId(String mvId){
        DatabaseHelper helper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();


        String gaeId="";
        Cursor c = null;
        try {
            c = helper.getGaeIDByAtsMovieMvId(db, mvId);

            if (c.moveToFirst()){
                gaeId =  c.getString(0);//gaeId
            }

        }  finally {
            c.close();
            db.close();
        }
        return gaeId;
    }

//@fluk43569230|金牌黑幫|http://www.photowant.com/photo101/fluk43569230/pm_fluk43569230_0010.jpg|132分|藍廳|10：20 14：40 19：20
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
