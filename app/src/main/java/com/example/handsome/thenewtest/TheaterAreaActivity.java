package com.example.handsome.thenewtest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.example.handsome.thenewtest.adapter.TheaterAreaGridAdapter;
import com.example.handsome.thenewtest.entity.AreaObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by handsome on 2015/10/24.
 */
public class TheaterAreaActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater_area);
        Context c = this;
        GridView gridview = (GridView) findViewById(R.id.theater_gridview);

        List<AreaObject> allItems = getAllAreaObject();
        TheaterAreaGridAdapter customAdapter = new TheaterAreaGridAdapter(c, allItems);
        gridview.setAdapter(customAdapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

       // if (id == R.id.action_settings) {
           // return true;
       // }
        return super.onOptionsItemSelected(item);
    }

    private List<AreaObject> getAllAreaObject(){
       // AreaObject areaObject = null;
        List<AreaObject> items = new ArrayList<>();
        items.add(new AreaObject("基隆", "a01"));
        items.add(new AreaObject("台北東區", "a02z1"));
        items.add(new AreaObject("台北西區", "a02z2"));
        items.add(new AreaObject("台北南區", "a02z3"));
        items.add(new AreaObject("台北北區", "a02z4"));
        items.add(new AreaObject("新北市", "a02z5"));
        items.add(new AreaObject("台北二輪", "a02z6"));
        items.add(new AreaObject("桃園", "a03"));
        items.add(new AreaObject("台中", "a04"));
        items.add(new AreaObject("嘉義", "a05"));
        items.add(new AreaObject("台南", "a06"));
        items.add(new AreaObject("高雄", "a07"));
        items.add(new AreaObject("新竹", "a35"));
        items.add(new AreaObject("苗栗", "a37"));
        items.add(new AreaObject("花蓮", "a38"));
        items.add(new AreaObject("宜蘭", "a39"));
        items.add(new AreaObject("雲林", "a45"));
        items.add(new AreaObject("彰化", "a47"));
        items.add(new AreaObject("南投", "a49"));
        items.add(new AreaObject("金門", "a68"));
        items.add(new AreaObject("澎湖", "a69"));
        items.add(new AreaObject("屏東", "a87"));
        items.add(new AreaObject("台東", "a89"));

        return items;
    }
}
