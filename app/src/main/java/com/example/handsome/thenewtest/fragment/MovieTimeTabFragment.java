package com.example.handsome.thenewtest.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.handsome.thenewtest.entity.MovieTime;
import com.example.handsome.thenewtest.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by handsome on 2015/10/22.
 */
public class MovieTimeTabFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    View view;
    LinkedHashMap<String, ArrayList<MovieTime>> areaMvTime = new LinkedHashMap<>();

    public MovieTimeTabFragment() {
    }

    public static MovieTimeTabFragment createInstance(ArrayList<String> timeList) {
        Log.i("hs", "MovieTimeTabFragment createInstance ");
        MovieTimeTabFragment f = new MovieTimeTabFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("timeList", timeList);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        List<MovieTime> allMtList =  setTimeList();
        if(allMtList.size()>0){

            view = inflater.inflate(R.layout.movietime_tab_fragment, container, false);
            //get the same area title and save into hashmap
            for (MovieTime mt : allMtList){
                ArrayList<MovieTime> mvTimeByArea = new ArrayList<>();
                if(!areaMvTime.containsKey(mt.getArea())){
                    mvTimeByArea.add(mt) ;
                    areaMvTime.put(mt.getArea(), mvTimeByArea);
                }else{
                    ArrayList<MovieTime> existList = areaMvTime.get(mt.getArea());
                    existList.add(mt);
                    areaMvTime.put(mt.getArea(), existList);
                }

            }

            viewPager = (ViewPager) view.findViewById(R.id.time_viewpager);
            setupViewPager(viewPager);

            tabLayout = (TabLayout) view.findViewById(R.id.time_tabLayout);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            tabLayout.setupWithViewPager(viewPager);

            return view;
        }else{
            view = inflater.inflate(R.layout.content_no_time, container, false);
            //viewPager = (ViewPager) view.findViewById(R.id.time_viewpager);
            //setupViewPager(viewPager);
            return view;
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        for(String key :areaMvTime.keySet()){
            adapter.addFrag(MovieTimeFragment.createInstance(areaMvTime.get(key)), key);
        }
        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    List<MovieTime> setTimeList(){
        Bundle bundle = getArguments();
        ArrayList<String> timeList = null;
        List<MovieTime> movieTimeList = new ArrayList<>();

        if (bundle != null) {
            timeList = bundle.getStringArrayList("timeList");
            for(String timeStr : timeList){
               //Log.i("hs", "str= " + timeStr);
                String[] timeObj = timeStr.split("\\|");

                MovieTime mt = new MovieTime();
                mt.setArea(timeObj[0]);
                mt.setThId(timeObj[1]);
                mt.setThName(timeObj[2]);
                mt.setHall(timeObj[3]);
                mt.setTimeStr(timeObj[4]);
                movieTimeList.add(mt);
            }



        }

        return  movieTimeList;
    }
}


